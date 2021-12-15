package anecdote;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// todo: edit javadoc
/**
 * Представляет собой вспомогательный класс, который используется
 * для десериализации репозитория анекдотов InternetAnecdoteRepository в файл Json.
 */
public class CommonAnecdoteListDeserializer implements JsonDeserializer<CommonAnecdoteList> {

    @Override
    public CommonAnecdoteList deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ArrayList<Anecdote> anecdotes = deserializeAnecdotes(jsonObject, "commonAnecdotes");
        CommonAnecdoteList list = CommonAnecdoteList.get();
        list.clear();
        list.addAll(anecdotes);
        return list;
    }

    private ArrayList<Anecdote> deserializeAnecdotes(JsonObject jsonObject, String elementName) {
        ArrayList<Anecdote> anecdotes = new ArrayList<>();
        JsonArray jsonAnecdotes = jsonObject.get(elementName).getAsJsonArray();
        for (JsonElement jsonAnecdote : jsonAnecdotes) {
            JsonObject anecdote = jsonAnecdote.getAsJsonObject();
            String text = anecdote.get("text").getAsString();
            Rating rating = Rating.valueOf(anecdote.get("rating").getAsString());
            if (anecdote.has("type") && anecdote.get("type").getAsString().equals("UnfinishedAnecdote")) {
                String ending = null;
                long authorId = 0;
                if (anecdote.has("ending")) {
                    ending = anecdote.get("ending").getAsString();
                    authorId = anecdote.get("authorId").getAsLong();
                }
                anecdotes.add(new UnfinishedAnecdote(text, rating, authorId, ending));
            } else {
                anecdotes.add(new RatableAnecdote(text, rating));
            }
        }
        return anecdotes;
    }

}
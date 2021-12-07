package anecdote;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой вспомогательный класс, который используется
 * для десериализации репозитория анекдотов InternetAnecdoteRepository в файл Json.
 */
public class AnecdoteRepositoryDeserializer implements JsonDeserializer<InternetAnecdoteRepository> {

    @Override
    public InternetAnecdoteRepository deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        var ratedAnecdotes = deserializeRatedAnecdotes(jsonObject);
        var anecdotes = deserializeAnecdotes(jsonObject, "anecdotes");
        var toldAnecdotes = deserializeAnecdotes(jsonObject, "toldAnecdotes");
        var bannedAnecdotes = deserializeAnecdotes(jsonObject, "bannedAnecdotes");

        return new InternetAnecdoteRepository(anecdotes, toldAnecdotes, bannedAnecdotes, ratedAnecdotes);
    }

    private Map<Rating, ArrayList<Anecdote>> deserializeRatedAnecdotes(JsonObject jsonObject) {
        Map<Rating, ArrayList<Anecdote>> ratedAnecdotes = new HashMap<>();
        var jsonMap = jsonObject.get("ratedAnecdotes").getAsJsonObject();
        for (var rating : Rating.values()) {
            var anecdotes = deserializeAnecdotes(jsonMap, rating.name());
            ratedAnecdotes.put(rating, anecdotes);
        }
        return ratedAnecdotes;
    }

    private ArrayList<Anecdote> deserializeAnecdotes(JsonObject jsonObject, String elementName) {
        var anecdotes = new ArrayList<Anecdote>();
        var jsonAnecdotes = jsonObject.get(elementName).getAsJsonArray();
        for (var jsonAnecdote : jsonAnecdotes) {
            var anecdote = jsonAnecdote.getAsJsonObject();
            var text = anecdote.get("text").getAsString();
            var rating = Rating.valueOf(anecdote.get("rating").getAsString());
            if (anecdote.has("type") && anecdote.get("type").getAsString().equals("UnfinishedAnecdote")) {
                String ending = null;
                if (anecdote.has("ending"))
                    ending = anecdote.get("ending").getAsString();
                anecdotes.add(new UnfinishedAnecdote(text, rating, ending));
            } else {
                anecdotes.add(new RatableAnecdote(text, rating));
            }

        }
        return anecdotes;
    }

}
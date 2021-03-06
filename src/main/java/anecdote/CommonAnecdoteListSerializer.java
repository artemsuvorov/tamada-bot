package anecdote;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Представляет собой вспомогательный класс, который используется
 * для сериализации списка общих анекдотов {@link CommonAnecdoteList} в файл Json.
 */
public class CommonAnecdoteListSerializer implements JsonSerializer<CommonAnecdoteList> {

    private static final String commonAnecdotesFieldName = "commonAnecdotes";

    @Override
    public JsonElement serialize(CommonAnecdoteList list, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        ArrayList<Anecdote> anecdotes = (ArrayList<Anecdote>) getValueOfField(list, commonAnecdotesFieldName);
        json.add(commonAnecdotesFieldName, serializeAnecdotes(anecdotes));
        return json;
    }

    private JsonArray serializeAnecdotes(ArrayList<Anecdote> anecdotes) {
        JsonArray jsonAnecdotes = new JsonArray();
        for (Anecdote anecdote : anecdotes) {
            JsonObject jsonAnecdote = new JsonObject();
            jsonAnecdote.addProperty("text", anecdote.getText());
            if (anecdote instanceof RatableAnecdote ratableAnecdote)
                jsonAnecdote.addProperty("rating", ratableAnecdote.getRating().name());
            if (anecdote instanceof UnfinishedAnecdote unfinishedAnecdote) {
                jsonAnecdote.addProperty("text", unfinishedAnecdote.getTextWithoutEnding());
                jsonAnecdote.addProperty("type", "UnfinishedAnecdote");
                if (unfinishedAnecdote.hasEnding()) {
                    jsonAnecdote.addProperty("ending", unfinishedAnecdote.getEnding());
                    jsonAnecdote.addProperty("authorId", unfinishedAnecdote.getAuthorId());
                    jsonAnecdote.add("totalRating", serializeTotalRatingOf(unfinishedAnecdote));
                }
            }
            jsonAnecdotes.add(jsonAnecdote);
        }
        return jsonAnecdotes;
    }

    private JsonObject serializeTotalRatingOf(Anecdote anecdote) {
        JsonObject jsonObject = new JsonObject();

        final String totalRatingFieldName = "totalRating";
        final String ratingSumFieldName = "ratingSum", ratingCountFieldName = "ratingCount";
        var totalRating = (TotalRating)getValueOfField(anecdote, totalRatingFieldName);

        double sum = (double)getValueOfField(totalRating, ratingSumFieldName);
        double count = (double)getValueOfField(totalRating, ratingCountFieldName);

        jsonObject.addProperty(ratingSumFieldName, sum);
        jsonObject.addProperty(ratingCountFieldName, count);

        return jsonObject;
    }

    private <T> Object getValueOfField(T list, String fieldName) {
        try {
            Field field = list.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            var value = field.get(list);
            field.setAccessible(false);
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
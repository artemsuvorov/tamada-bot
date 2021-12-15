package anecdote;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой вспомогательный класс, который используется
 * для десериализации репозитория анекдотов InternetAnecdoteRepository из файла Json.
 */
public class InternetAnecdoteRepositoryDeserializer implements JsonDeserializer<InternetAnecdoteRepository> {

    @Override
    public InternetAnecdoteRepository deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        long id = jsonObject.get("id").getAsLong();
        Map<Rating, ArrayList<Anecdote>> ratedAnecdotes = deserializeRatedAnecdotes(jsonObject);
        ArrayList<Anecdote> anecdotes = deserializeAnecdotes(jsonObject, "anecdotes");
        ArrayList<Anecdote> toldAnecdotes = deserializeAnecdotes(jsonObject, "toldAnecdotes");
        ArrayList<Anecdote> bannedAnecdotes = deserializeAnecdotes(jsonObject, "bannedAnecdotes");

        return new InternetAnecdoteRepository(id, anecdotes, toldAnecdotes, bannedAnecdotes, ratedAnecdotes);
    }

    private Map<Rating, ArrayList<Anecdote>> deserializeRatedAnecdotes(JsonObject jsonObject) {
        Map<Rating, ArrayList<Anecdote>> ratedAnecdotes = new HashMap<>();
        JsonObject jsonMap = jsonObject.get("ratedAnecdotes").getAsJsonObject();
        for (Rating rating : Rating.values()) {
            ArrayList<Anecdote> anecdotes = deserializeAnecdotes(jsonMap, rating.name());
            ratedAnecdotes.put(rating, anecdotes);
        }
        return ratedAnecdotes;
    }

    private ArrayList<Anecdote> deserializeAnecdotes(JsonObject jsonObject, String elementName) {
        ArrayList<Anecdote> anecdotes = new ArrayList<>();
        JsonArray jsonAnecdotes = jsonObject.get(elementName).getAsJsonArray();
        for (JsonElement jsonAnecdote : jsonAnecdotes) {
            JsonObject anecdote = jsonAnecdote.getAsJsonObject();
            String text = anecdote.get("text").getAsString();
            Rating rating = Rating.valueOf(anecdote.get("rating").getAsString());
            if (anecdote.has("type") && anecdote.get("type").getAsString().equals("UnfinishedAnecdote")) {
                TotalRating totalRating = null;
                long authorId = 0;
                String ending = null;
                if (anecdote.has("ending")) {
                    ending = anecdote.get("ending").getAsString();
                    authorId = anecdote.get("authorId").getAsLong();
                    totalRating = deserializeTotalRating(anecdote);
                }
                anecdotes.add(new UnfinishedAnecdote(text, rating, totalRating, authorId, ending));
            } else {
                anecdotes.add(new RatableAnecdote(text, rating));
            }
        }
        return anecdotes;
    }

    private TotalRating deserializeTotalRating(JsonObject anecdote) {
        JsonObject jsonTotalRating = anecdote.get("totalRating").getAsJsonObject();
        double sum = jsonTotalRating.get("ratingSum").getAsDouble();
        double count = jsonTotalRating.get("ratingCount").getAsDouble();
        return new TotalRating(sum, count);
    }

}
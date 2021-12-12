package anecdote;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Представляет собой вспомогательный класс, который используется
 * для сериализации репозитория анекдотов InternetAnecdoteRepository из файла Json.
 */
public class AnecdoteRepositorySerializer implements JsonSerializer<InternetAnecdoteRepository> {

    @Override
    public JsonElement serialize(InternetAnecdoteRepository repository, Type type, JsonSerializationContext context) {
        var json = new JsonObject();

        json.add("ratedAnecdotes", serializeRatedAnecdotes(repository.ratedAnecdotes));
        json.add("anecdotes", serializeAnecdotes(repository.anecdotes));
        json.add("toldAnecdotes", serializeAnecdotes(repository.toldAnecdotes));
        json.add("bannedAnecdotes", serializeAnecdotes(repository.bannedAnecdotes));

        return json;
    }

    private JsonElement serializeRatedAnecdotes(Map<Rating, ArrayList<Anecdote>> ratedAnecdotes) {
        var jsonRatedAnecdotes = new JsonObject();

        for (var rating : Rating.values()) {
            var anecdotes = serializeAnecdotes(ratedAnecdotes.get(rating));
            jsonRatedAnecdotes.add(rating.name(), anecdotes);
        }

        return jsonRatedAnecdotes;
    }

    private JsonArray serializeAnecdotes(ArrayList<Anecdote> anecdotes) {
        var jsonAnecdotes = new JsonArray();
        for (var anecdote : anecdotes) {
            var jsonAnecdote = new JsonObject();
            jsonAnecdote.addProperty("text", anecdote.getText());
            if (anecdote instanceof RatableAnecdote ratableAnecdote)
                jsonAnecdote.addProperty("rating", ratableAnecdote.getRating().name());
            if (anecdote instanceof UnfinishedAnecdote unfinishedAnecdote) {
                jsonAnecdote.addProperty("type", "UnfinishedAnecdote");
                if (unfinishedAnecdote.hasEnding())
                    jsonAnecdote.addProperty("ending", unfinishedAnecdote.getEnding());
            }
            jsonAnecdotes.add(jsonAnecdote);
        }
        return jsonAnecdotes;
    }

}
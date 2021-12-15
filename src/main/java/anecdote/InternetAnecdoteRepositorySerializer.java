package anecdote;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Представляет собой вспомогательный класс, который используется
 * для сериализации репозитория анекдотов InternetAnecdoteRepository из файла Json.
 */
public class InternetAnecdoteRepositorySerializer implements JsonSerializer<InternetAnecdoteRepository> {

    @Override
    public JsonElement serialize(InternetAnecdoteRepository repository, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        json.addProperty("id", repository.id);
        json.add("ratedAnecdotes", serializeRatedAnecdotes(repository.ratedAnecdotes));
        json.add("anecdotes", serializeAnecdotes(repository.anecdotes, false));
        json.add("toldAnecdotes", serializeAnecdotes(repository.toldAnecdotes, false));
        json.add("bannedAnecdotes", serializeAnecdotes(repository.bannedAnecdotes, false));

        return json;
    }

    private JsonElement serializeRatedAnecdotes(Map<Rating, ArrayList<Anecdote>> ratedAnecdotes) {
        JsonObject jsonRatedAnecdotes = new JsonObject();

        for (Rating rating : Rating.values()) {
            JsonArray anecdotes = serializeAnecdotes(ratedAnecdotes.get(rating), true);
            jsonRatedAnecdotes.add(rating.name(), anecdotes);
        }

        return jsonRatedAnecdotes;
    }

    private JsonArray serializeAnecdotes(ArrayList<Anecdote> anecdotes, boolean authorAnecdotes) {
        JsonArray jsonAnecdotes = new JsonArray();
        for (Anecdote anecdote : anecdotes) {
            if (!authorAnecdotes && anecdote instanceof UnfinishedAnecdote unfinished && unfinished.hasEnding())
                continue;
            JsonObject jsonAnecdote = new JsonObject();
            jsonAnecdote.addProperty("text", anecdote.getText());
            if (anecdote instanceof RatableAnecdote ratableAnecdote)
                jsonAnecdote.addProperty("rating", ratableAnecdote.getRating().name());
            if (authorAnecdotes && anecdote instanceof UnfinishedAnecdote unfinishedAnecdote) {
                jsonAnecdote.addProperty("text", unfinishedAnecdote.getTextWithoutEnding());
                jsonAnecdote.addProperty("type", "UnfinishedAnecdote");
                if (unfinishedAnecdote.hasEnding()) {
                    jsonAnecdote.addProperty("ending", unfinishedAnecdote.getEnding());
                    jsonAnecdote.addProperty("authorId", unfinishedAnecdote.getAuthorId());
                }
            }
            jsonAnecdotes.add(jsonAnecdote);
        }
        return jsonAnecdotes;
    }

}
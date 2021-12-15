package anecdote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import event.AnecdoteActionEvent;
import event.AnecdotesActionEvent;
import event.EventHandler;

import java.util.ArrayList;

// todo: add javadocs
public final class CommonAnecdoteList {

    private final Gson gson;
    private ArrayList<Anecdote> commonAnecdotes;

    private final CommonAnecdoteListSerializer serializer = new CommonAnecdoteListSerializer();
    private final CommonAnecdoteListDeserializer deserializer = new CommonAnecdoteListDeserializer();

    public final EventHandler AnecdoteAddedEvent;
    public final EventHandler ListDeserializedEvent;

    private static CommonAnecdoteList instance;

    private CommonAnecdoteList() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        commonAnecdotes = new ArrayList<>();
        AnecdoteAddedEvent = new EventHandler();
        ListDeserializedEvent = new EventHandler();
    }

    public static CommonAnecdoteList get() {
        if (instance == null)
            instance = new CommonAnecdoteList();
        return instance;
    }

    public ArrayList<Anecdote> getAnecdotes() {
        return commonAnecdotes;
    }

    public void add(Anecdote anecdote) {
        commonAnecdotes.add(anecdote);
        var event = new AnecdoteActionEvent(anecdote);
        AnecdoteAddedEvent.invoke(this, event);
    }

    public void addAll(ArrayList<Anecdote> anecdotes) {
        commonAnecdotes.addAll(anecdotes); // todo: mb use add with event invoke ?
    }

    public void clear() {
        commonAnecdotes.clear();
    }

    public String serializeCommonAnecdotes() {
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), serializer)
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public void deserializeCommonAnecdotes(String json) {
        if (json == null) return;
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), deserializer)
                .create();
        CommonAnecdoteList list = gson.fromJson(json, this.getClass());
        this.commonAnecdotes = list.commonAnecdotes;
        var event = new AnecdotesActionEvent(this.commonAnecdotes);
        ListDeserializedEvent.invoke(this, event);
    }

}
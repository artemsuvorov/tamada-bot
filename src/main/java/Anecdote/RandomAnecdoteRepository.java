package Anecdote;

import Utils.Randomizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RandomAnecdoteRepository implements IAnecdoteRepository {

    private ArrayList<IAnecdote> _anecdotes;
    protected ArrayList<IAnecdote> _toldAnecdotes;

    public RandomAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAncdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new Anecdote(x)).collect(Collectors.toList()));
    }

    public RandomAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        if (anecdotes == null || anecdotes.isEmpty())
            throw new IllegalArgumentException("Anecdotes collection cannot be null or empty.");

        _anecdotes = anecdotes;
        _toldAnecdotes = new ArrayList<IAnecdote>();
    }

    @Override
    public int getCount() {
        return _anecdotes.size() + _toldAnecdotes.size();
    }

    @Override
    public IAnecdote getNextAnecdote() {
        if (_anecdotes.isEmpty()) {
            _anecdotes = _toldAnecdotes;
            _toldAnecdotes = new ArrayList<IAnecdote>();
        }

        return getRandomAnecdote();
    }

    protected IAnecdote getRandomAnecdote() {
        var index = Randomizer.getRandomNumber(_anecdotes.size());
        var anecdote = _anecdotes.get(index);
        _anecdotes.remove(index);
        _toldAnecdotes.add(anecdote);
        return anecdote;
    }

}
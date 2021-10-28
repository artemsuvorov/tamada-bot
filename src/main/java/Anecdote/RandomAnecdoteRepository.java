package Anecdote;

import Utils.Randomizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents an anecdote repository class
 * that returns anecdotes one by one in a random sequence.
 */
public class RandomAnecdoteRepository implements IAnecdoteRepository {

    private ArrayList<IAnecdote> _anecdotes;
    protected ArrayList<IAnecdote> _toldAnecdotes;

    public RandomAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new Anecdote(x)).collect(Collectors.toList()));
    }

    public RandomAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        if (anecdotes == null || anecdotes.isEmpty())
            throw new IllegalArgumentException("Anecdotes collection cannot be null or empty.");

        _anecdotes = anecdotes;
        _toldAnecdotes = new ArrayList<IAnecdote>();
    }

    /**
     * Returns the count of anecdotes stored in the repository.
     * @return the count of anecdotes stored in the repository.
     */
    @Override
    public int getCount() {
        return _anecdotes.size() + _toldAnecdotes.size();
    }

    /**
     * Indicates if the repository is empty.
     * @return true if the repository has at least one anecdote, otherwise false.
     */
    @Override
    public boolean hasAnecdotes() {
        return !_anecdotes.isEmpty() || !_toldAnecdotes.isEmpty();
    }

    /**
     * Picks and returns random anecdote from the pool of the untold ones.
     * When all the anecdotes from the repository are told, considers told ones as untold.
     * @return a random anecdote from the pool of untold ones.
     */
    @Override
    public IAnecdote getNextAnecdote() {
        if (_anecdotes.isEmpty() && _toldAnecdotes.isEmpty())
            throw new IllegalArgumentException("There are no anecdotes in repository!");

        if (_anecdotes.isEmpty()) {
            _anecdotes = _toldAnecdotes;
            _toldAnecdotes = new ArrayList<IAnecdote>();
        }

        return getRandomAnecdote();
    }

    /**
     * Returns a random anecdote and removes it from the pool of the untold ones.
     * @return a random anecdote from the pool of untold ones.
     */
    protected IAnecdote getRandomAnecdote() {
        var index = Randomizer.getRandomNumber(_anecdotes.size());
        var anecdote = _anecdotes.get(index);
        _anecdotes.remove(index);
        _toldAnecdotes.add(anecdote);
        return anecdote;
    }

}
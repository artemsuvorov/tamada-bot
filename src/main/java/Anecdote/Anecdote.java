package Anecdote;

/**
 * Represents a wrapper class of an anecdote string.
 */
public class Anecdote implements IAnecdote {

    private final String _anecdote;

    public Anecdote(String anecdote) {
        _anecdote = anecdote;
    }

    /**
     * Returns an anecdote as a string.
     * @return the string with an anecdote.
     */
    @Override
    public String getAnecdote() {
        return _anecdote;
    }

    /**
     * Returns an anecdote as a string.
     * @return the string with an anecdote.
     */
    @Override
    public String toString() {
        return getAnecdote();
    }

}
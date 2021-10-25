package Anecdote;

public class Anecdote implements IAnecdote {

    private final String _anecdote;

    public Anecdote(String anecdote) {
        _anecdote = anecdote;
    }

    @Override
    public String getAnecdote() {
        return _anecdote;
    }

    @Override
    public String toString() {
        return getAnecdote();
    }

}
package Anecdote;

public interface IAnecdoteRepository {

    int getCount();
    boolean hasAnecdotes();
    IAnecdote getNextAnecdote();

}

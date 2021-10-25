package Anecdote;

/**
 * Defines an interface of an anecdote repository
 * that returns anecdotes one by one.
 */
public interface IAnecdoteRepository {

    /**
     * When overridden, returns the count of anecdotes stored in the repository.
     * @return the count of anecdotes stored in the repository.
     */
    int getCount();

    /**
     * When overridden, indicates if the repository is empty.
     * @return true if the repository has at least one anecdote, otherwise false.
     */
    boolean hasAnecdotes();

    /**
     * When overridden, returns the next anecdote from the repository.
     * @return the next anecdote from the repository.
     */
    IAnecdote getNextAnecdote();

}
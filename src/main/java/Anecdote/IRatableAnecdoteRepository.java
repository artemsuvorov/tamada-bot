package Anecdote;

import java.util.ArrayList;

/**
 * Defines an interface of an anecdote repository
 * in which each anecdote can be rated.
 */
public interface IRatableAnecdoteRepository extends IAnecdoteRepository {

    /**
     * When overridden, returns the list of favorite anecdotes.
     * @return the list of favorite anecdotes.
     */
    ArrayList<IAnecdote> getFavorites();


    /**
     * When overridden, returns a list of anecdotes with the specified rating.
     * @param rating the rating of the anecdotes list.
     * @return a list of anecdotes with the specified rating.
     */
    ArrayList<IAnecdote> getAnecdotesOfRating(Rating rating);

}
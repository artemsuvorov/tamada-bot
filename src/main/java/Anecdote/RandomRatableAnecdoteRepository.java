package Anecdote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.stream.Collectors;

/**
 * Represents a repository of ratable anecdotes class
 * that returns anecdotes one by one in a random sequence.
 */
public final class RandomRatableAnecdoteRepository
        extends RandomAnecdoteRepository implements IRatableAnecdoteRepository, PropertyChangeListener {

    private Dictionary<Rating, ArrayList<IAnecdote>> _ratedAnecdotes;

    public RandomRatableAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new RatableAnecdote(x)).collect(Collectors.toList()));
    }

    public RandomRatableAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        super(anecdotes);
        listenRatableAnecdotes(anecdotes);

        _ratedAnecdotes = new Hashtable<Rating, ArrayList<IAnecdote>>();
        for (var rating : Rating.values()) {
            _ratedAnecdotes.put(rating, new ArrayList<IAnecdote>());
        }
    }

    /**
     * Returns the list of favorite anecdotes.
     *
     * @return the list of favorite anecdotes.
     */
    @Override
    public ArrayList<IAnecdote> getFavorites() {
        return getAnecdotesOfRating(Rating.Excellent);
    }

    /**
     * Returns a list of anecdotes with the specified rating.
     * @param rating the rating of the anecdotes list.
     * @return a list of anecdotes with the specified rating.
     */
    @Override
    public ArrayList<IAnecdote> getAnecdotesOfRating(Rating rating) {
        return _ratedAnecdotes.get(rating);
    }

    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the
     *            event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof IRatableAnecdote anecdote && evt.getPropertyName() == "_rating")
            if (evt.getOldValue() instanceof Rating oldRating && evt.getNewValue() instanceof Rating newRating)
                onAnecdoteRatingChanged(anecdote, oldRating, newRating);
    }

    /**
     * Action to be performed when the rating of one of the anecdotes
     * from the repository was changed.
     * If anecdote was liked, it is to be added to the favorite anecdote list.
     * If anecdote was disliked, it is to be removed from the repository.
     * @param anecdote anecdote which rating was changed.
     * @param oldRating the rating before the change occurred.
     * @param newRating the rating after the change occured.
     */
    private void onAnecdoteRatingChanged(IAnecdote anecdote, Rating oldRating, Rating newRating) {
        if (oldRating != newRating) {
            getAnecdotesOfRating(newRating).add(anecdote);
            if (oldRating != Rating.None)
                getAnecdotesOfRating(oldRating).remove(anecdote);
        }
        if (newRating == Rating.Dislike) {
            _toldAnecdotes.remove(anecdote);
        }
    }

    /**
     * Subscribes the repository to listen to the rating property change
     * for each of the specified anecdotes.
     * @param anecdotes the anecdotes which ratings are to be listened.
     */
    private void listenRatableAnecdotes(ArrayList<IAnecdote> anecdotes) {
        for (var anecdote : anecdotes)
            if (anecdote instanceof IRatableAnecdote ratableAnecdote)
                ratableAnecdote.addListener(this);
    }

}
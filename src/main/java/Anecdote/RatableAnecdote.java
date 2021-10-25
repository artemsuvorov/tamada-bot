package Anecdote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents a wrapper class of an anecdote string that can be rated.
 */
public class RatableAnecdote extends Anecdote implements IRatableAnecdote {

    private final PropertyChangeSupport _support;
    private Rating _rating;

    public RatableAnecdote(String anecdote) {
        super(anecdote);
        _support = new PropertyChangeSupport(this);
        _rating = Rating.None;
    }

    /**
     * Returns the current rating of the anecdote.
     * @return the current rating of the anecdote.
     */
    @Override
    public Rating getRating() {
        return _rating;
    }

    /**
     * Assigns the specified rating to the anecdote and
     * notifies all the listeners about the anecdote rating change.
     * @param rating the rating to be assigned to the anecdote.
     */
    @Override
    public void rate(Rating rating) {
        if (_rating == rating) return;
        Rating oldRating = _rating, newRating = rating;
        _rating = rating;
        _support.firePropertyChange("_rating", oldRating, newRating);
    }

    /**
     * Subscribes the specified object to listen to the rating property change.
     * @param listener the object that to be subscribed to listen to the rating property change.
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        _support.addPropertyChangeListener(listener);
    }

    /**
     * Unsubscribes the specified object from listening to the rating property change.
     * @param listener the object that to be unsubscribed from
     *                 listening to the rating property change.
     */
    @Override
    public void removeListener(PropertyChangeListener listener) {
        _support.removePropertyChangeListener(listener);
    }

}
package Anecdote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RatableAnecdote extends Anecdote implements IRatableAnecdote {

    private final PropertyChangeSupport _support;
    private Rating _rating;

    public RatableAnecdote(String anecdote) {
        super(anecdote);
        _support = new PropertyChangeSupport(this);
        _rating = Rating.None;
    }

    @Override
    public Rating getRating() {
        return _rating;
    }

    @Override
    public void rate(Rating rating) {
        if (_rating == rating) return;
        Rating oldRating = _rating, newRating = rating;
        _rating = rating;
        _support.firePropertyChange("_rating", oldRating, newRating);
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        _support.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        _support.removePropertyChangeListener(listener);
    }

}
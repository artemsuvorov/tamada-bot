package Anecdote;

import java.beans.PropertyChangeListener;
import java.util.Observable;

public interface IRatableAnecdote extends IAnecdote {

    Rating getRating();
    void rate(Rating rating);

    void addListener(PropertyChangeListener listener);
    void removeListener(PropertyChangeListener listener);

}
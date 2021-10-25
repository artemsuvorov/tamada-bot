package Anecdote;

import java.beans.PropertyChangeListener;
import java.util.Observable;


/**
 * Defines an interface of an anecdote string that can be rated.
 */
public interface IRatableAnecdote extends IAnecdote {

    /**
     * When overridden, returns the current rating of the anecdote.
     * @return the current rating of the anecdote.
     */
    Rating getRating();

    /**
     * When overridden, assigns the specified rating to the anecdote.
     * @param rating the rating to be assigned to the anecdote.
     */
    void rate(Rating rating);

    /**
     * When overridden, subscribes the specified object to listen to the rating property change.
     * @param listener the object that to be subscribed to listen to the rating property change.
     */
    void addListener(PropertyChangeListener listener);

    /**
     * When overridden, unsubscribes the specified object from
     * listening to the rating property change.
     * @param listener the object that to be unsubscribed from
     *                 listening to the rating property change.
     */
    void removeListener(PropertyChangeListener listener);

}
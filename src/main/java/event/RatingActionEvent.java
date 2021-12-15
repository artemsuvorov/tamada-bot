package event;

import anecdote.Anecdote;
import anecdote.Rating;

public class RatingActionEvent extends ActionEvent {

    private final long senderId;
    private final Anecdote anecdote;
    private final Rating oldRating;
    private final Rating newRating;

    public RatingActionEvent(long senderId, Anecdote anecdote, Rating oldRating, Rating newRating) {
        this.senderId = senderId;
        this.anecdote = anecdote;
        this.oldRating = oldRating;
        this.newRating = newRating;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public Anecdote getAnecdote() {
        return this.anecdote;
    }

    public Rating getOldRating() {
        return this.oldRating;
    }

    public Rating getNewRating() {
        return this.newRating;
    }

}

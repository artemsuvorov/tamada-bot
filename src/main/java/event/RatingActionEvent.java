package event;

import anecdote.Anecdote;
import anecdote.Rating;

public class RatingActionEvent extends ActionEvent {

    private final Anecdote anecdote;
    private final Rating oldRating;
    private final Rating newRating;

    public RatingActionEvent(Anecdote anecdote, Rating oldRating, Rating newRating) {
        this.anecdote = anecdote;
        this.oldRating = oldRating;
        this.newRating = newRating;
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

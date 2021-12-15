package event;

import anecdote.Anecdote;

public class AnecdoteActionEvent extends ActionEvent {

    private final Anecdote anecdote;

    public AnecdoteActionEvent(Anecdote anecdote) {
        this.anecdote = anecdote;
    }

    public Anecdote getAnecdote() {
        return this.anecdote;
    }

}
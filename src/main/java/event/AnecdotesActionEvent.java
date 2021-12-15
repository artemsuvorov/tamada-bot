package event;

import anecdote.Anecdote;

import java.util.ArrayList;

public class AnecdotesActionEvent extends ActionEvent {

    private final ArrayList<Anecdote> anecdotes;

    public AnecdotesActionEvent(ArrayList<Anecdote> anecdotes) {
        this.anecdotes = anecdotes;
    }

    public ArrayList<Anecdote> getAnecdotes() {
        return this.anecdotes;
    }

}
package Anecdote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class RandomRatableAnecdoteRepository
        extends RandomAnecdoteRepository implements IRatableAnecdoteRepository, PropertyChangeListener {

    private ArrayList<IAnecdote> _favoriteAnecdotes;

    public RandomRatableAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new RatableAnecdote(x)).collect(Collectors.toList()));
    }

    public RandomRatableAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        super(anecdotes);
        listenRatableAnecdotes(anecdotes);
        _favoriteAnecdotes = new ArrayList<IAnecdote>();
    }

    @Override
    public ArrayList<IAnecdote> getFavorites() {
        return _favoriteAnecdotes;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof IRatableAnecdote anecdote && evt.getPropertyName() == "_rating")
            if (evt.getOldValue() instanceof Rating oldRating && evt.getNewValue() instanceof Rating newRating)
                onAnecdoteRatingChanged(anecdote, oldRating, newRating);
    }

    private void onAnecdoteRatingChanged(IAnecdote anecdote, Rating oldRating, Rating newRating) {
        if (oldRating == Rating.Like && newRating != Rating.Like)
            _favoriteAnecdotes.remove(anecdote);
        if (newRating == Rating.Like)
            _favoriteAnecdotes.add(anecdote);
        if (newRating == Rating.Dislike)
            _toldAnecdotes.remove(anecdote);
    }

    private void listenRatableAnecdotes(ArrayList<IAnecdote> anecdotes) {
        for (var anecdote : anecdotes)
            if (anecdote instanceof IRatableAnecdote ratableAnecdote)
                ratableAnecdote.addListener(this);
    }

}
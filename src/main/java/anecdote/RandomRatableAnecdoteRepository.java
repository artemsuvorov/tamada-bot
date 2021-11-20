package anecdote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.stream.Collectors;

/**
 * Представляет собой репозиторий, который состоит из поддерживающих оценивание анекдотов
 * и который выдает анекдот один за другим в случайной неповторяющейся последовательности.
 */
public final class RandomRatableAnecdoteRepository
        extends RandomAnecdoteRepository implements IRatableAnecdoteRepository, PropertyChangeListener {

    private final Dictionary<Rating, ArrayList<IAnecdote>> ratedAnecdotes;

    public RandomRatableAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new RatableAnecdote(x)).collect(Collectors.toList()));
    }

    public RandomRatableAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        super(anecdotes);
        listenRatableAnecdotes(anecdotes);

        ratedAnecdotes = new Hashtable<Rating, ArrayList<IAnecdote>>();
        for (var rating : Rating.values()) {
            ratedAnecdotes.put(rating, new ArrayList<IAnecdote>());
        }
    }

    /**
     * Возвращает список любимых анекдотов.
     * @return Список любимых анекдотов.
     */
    @Override
    public IAnecdote[] getFavorites() {
        return getAnecdotesOfRating(Rating.Excellent);
    }

    /**
     * Возвращает список анекдотов, имеющих указанную оценку.
     * @param rating оценка, которую имеют анекдоты.
     * @return Список анекдотов, имеющих указанную оценку.
     */
    @Override
    public IAnecdote[] getAnecdotesOfRating(Rating rating) {
        return ratedAnecdotes.get(rating).toArray(new IAnecdote[0]);
    }

    /**
     * Этот метод вызывается, когда происходит изменение свойства,
     * на которое кто-то был подписан.
     * @param evt PropertyChangeEvent объект, описывающий источник
     *            события и свойство, которое было изменено.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof IRatableAnecdote anecdote && evt.getPropertyName() == "rating")
            if (evt.getOldValue() instanceof Rating oldRating && evt.getNewValue() instanceof Rating newRating)
                onAnecdoteRatingChanged(anecdote, oldRating, newRating);
    }

    /**
     * Действие, которое должно быть исполнено,
     * когда оценка одного из анекдотов была изменена.
     * Если анекдот был лайкнут, он добавляется в список любымых анекдотов.
     * Если анекдот был дизлайкнут, он удаляется из этого репозитория.
     * @param anecdote анекдот, чья оценка была изменена.
     * @param oldRating предыдущая оценка анекдота.
     * @param newRating новая оценка анекдота.
     */
    private void onAnecdoteRatingChanged(IAnecdote anecdote, Rating oldRating, Rating newRating) {
        if (oldRating != newRating) {
            ratedAnecdotes.get(newRating).add(anecdote);
            if (oldRating != Rating.None)
                ratedAnecdotes.get(oldRating).remove(anecdote);
        }
        if (newRating == Rating.Dislike) {
            toldAnecdotes.remove(anecdote);
        }
    }

    /**
     * Подписывает этот репозиторий на прослушивание изменения
     * оценки у каждого из указанных анекдотов.
     * @param anecdotes анекдоты, чьи оценки будут прослушиваться.
     */
    private void listenRatableAnecdotes(ArrayList<IAnecdote> anecdotes) {
        for (var anecdote : anecdotes)
            if (anecdote instanceof IRatableAnecdote ratableAnecdote)
                ratableAnecdote.addListener(this);
    }

}
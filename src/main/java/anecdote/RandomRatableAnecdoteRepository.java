package anecdote;

import event.ActionEvent;
import event.ActionListener;
import event.RatingActionEvent;

import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Представляет собой репозиторий, который состоит из поддерживающих оценивание анекдотов
 * и который выдает анекдот один за другим в случайной неповторяющейся последовательности.
 */
public class RandomRatableAnecdoteRepository
        extends RandomAnecdoteRepository implements IRatableAnecdoteRepository, ActionListener {

    protected Map<Rating, ArrayList<Anecdote>> ratedAnecdotes;

    public RandomRatableAnecdoteRepository(ArrayList<Anecdote> anecdotes, ArrayList<Anecdote> toldAnecdotes,
        ArrayList<Anecdote> bannedAnecdotes, Map<Rating, ArrayList<Anecdote>> ratedAnecdotes) {
        super(anecdotes, toldAnecdotes, bannedAnecdotes);
        this.ratedAnecdotes = ratedAnecdotes;
    }

    public RandomRatableAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<Anecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(Anecdote)new RatableAnecdote(x)).collect(Collectors.toList()));
    }

    public RandomRatableAnecdoteRepository(ArrayList<Anecdote> anecdotes) {
        super(anecdotes);
        listenRatableAnecdotes(this.anecdotes);

        ratedAnecdotes = new HashMap<>();
        for (var rating : Rating.values()) {
            ratedAnecdotes.put(rating, new ArrayList<>());
        }
    }

    /**
     * Возвращает список любимых анекдотов.
     * @return Список любимых анекдотов.
     */
    @Override
    public Anecdote[] getFavorites() {
        return getAnecdotesOfRating(Rating.Excellent);
    }

    /**
     * Возвращает список анекдотов, имеющих указанную оценку.
     * @param rating оценка, которую имеют анекдоты.
     * @return Список анекдотов, имеющих указанную оценку.
     */
    @Override
    public Anecdote[] getAnecdotesOfRating(Rating rating) {
        return ratedAnecdotes.get(rating).toArray(new Anecdote[0]);
    }

    //todo: remove commented out block of code
    /**
     * Этот метод вызывается, когда происходит изменение свойства,
     * на которое кто-то был подписан.
     * @param evt PropertyChangeEvent объект, описывающий источник
     *            события и свойство, которое было изменено.
     */
    /*
    public void propertyChange(PropertyChangeEvent evt) {
        // todo: probably after repository refill propChange method won't invoke at all
        if (evt.getSource() instanceof RatableAnecdote anecdote && evt.getPropertyName() == "rating")
            if (evt.getOldValue() instanceof Rating oldRating && evt.getNewValue() instanceof Rating newRating)
                onAnecdoteRatingChanged(anecdote, oldRating, newRating);
    }*/

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt instanceof RatingActionEvent ratingEvent)
            onAnecdoteRatingChanged(ratingEvent.getAnecdote(), ratingEvent.getOldRating(), ratingEvent.getNewRating());
    }

    /**
     * Подписывает этот репозиторий на прослушивание изменения
     * оценки у каждого из указанных анекдотов.
     * @param anecdotes анекдоты, чьи оценки будут прослушиваться.
     */
    protected void listenRatableAnecdotes(ArrayList<Anecdote> anecdotes) {
        for (var anecdote : anecdotes)
            if (anecdote instanceof IRatableAnecdote ratableAnecdote)
                listenRatableAnecdote(ratableAnecdote);
    }

    /**
     * Подписывает этот репозиторий на прослушивание изменения оценки у указанного анекдота.
     * @param anecdote анекдот, чья оценка будет прослушиваться.
     */
    protected void listenRatableAnecdote(IRatableAnecdote anecdote) {
        anecdote.addListener(this);
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
    private void onAnecdoteRatingChanged(Anecdote anecdote, Rating oldRating, Rating newRating) {
        if (oldRating != newRating) {
            ratedAnecdotes.get(newRating).add(anecdote);
            if (oldRating != Rating.None)
                ratedAnecdotes.get(oldRating).remove(anecdote);
        }
        if (newRating == Rating.Dislike) {
            toldAnecdotes.remove(anecdote);
            bannedAnecdotes.add(anecdote);
        }
    }

}
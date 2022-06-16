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
        listenRatableAnecdotes(this.anecdotes);
        listenRatableAnecdotes(this.toldAnecdotes);
        listenRatableAnecdotes(this.bannedAnecdotes);
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

    // todo: implement fixes from task2

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

    /**
     * Указывает, содержится ли переданный анекдот среди оцененных анекдотов.
     * @param anecdote анекдот, который будет проверен на нахождение среди оцененных анекдотов.
     * @return true, если анекдот содержится среди оцененных анекдотов, иначе false.
     */
    @Override
    public boolean containsRatedAnecdote(Anecdote anecdote) {
        for (ArrayList<Anecdote> list : ratedAnecdotes.values()) {
            if (list.contains(anecdote)) return true;
        }
        return false;
    }

    /**
     * Этот метод вызывается, когда происходит событие, на которое был подписан этот класс.
     * @param event ActionEvent объект, описывающий все свойства события.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event instanceof RatingActionEvent ratingEvent)
            onAnecdoteRatingChanged(ratingEvent.getAnecdote(), ratingEvent.getOldRating(), ratingEvent.getNewRating());
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
    protected void onAnecdoteRatingChanged(Anecdote anecdote, Rating oldRating, Rating newRating) {
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

}
package anecdote;

import event.ActionListener;
import event.EventHandler;
import event.RatingActionEvent;

/**
 * Представляет собой класс анекдота, который может быть оценен.
 */
public class RatableAnecdote extends Anecdote implements IRatableAnecdote {

    private final EventHandler ratingChanged;
    private Rating rating;

    public RatableAnecdote(String anecdote, Rating rating) {
        super(anecdote);
        ratingChanged = new EventHandler();
        this.rating = rating;
    }

    public RatableAnecdote(String anecdote) {
        this(anecdote, Rating.None);
    }

    /**
     * Возвращает текущую оценку анекдота.
     * По умолчанию, каждый анекдот имеет оценку Rating.None.
     * @return Текущая оценка анекдота.
     */
    @Override
    public Rating getRating() {
        return rating;
    }

    /**
     * Присваивает указанную оценку анекдоту.
     * Если анекдот изменил свою оценку, метод оповещает об этом всех подписчиков.
     * @param newRating оценка, которая будет присвоена анекдоту.
     */
    @Override
    public void setRating(long senderId, Rating newRating) {
        if (rating == newRating) return;
        var oldRating = rating;
        rating = newRating;
        RatingActionEvent event = new RatingActionEvent(senderId, this, oldRating, newRating);
        ratingChanged.invoke(this, event);
    }

    /**
     * Подписывает указанный объект на прослушивание изменений оценки этого анекдота.
     * @param listener объект, который будет подписан на прослушивание изменений оценки.
     */
    @Override
    public void addListener(ActionListener listener) {
        ratingChanged.addListener(listener);
    }

    /**
     * Отписывает указанный объект от прослушивания изменений оценки этого анекдота.
     * @param listener объект, который будет отписан от прослушивания изменений оценки.
     */
    @Override
    public void removeListener(ActionListener listener) {
        ratingChanged.removeListener(listener);
    }

}
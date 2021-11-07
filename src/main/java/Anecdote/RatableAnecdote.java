package Anecdote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Представляет собой класс анекдота, который может быть оценен.
 */
public class RatableAnecdote extends Anecdote implements IRatableAnecdote {

    private final PropertyChangeSupport support;
    private Rating rating;

    public RatableAnecdote(String anecdote) {
        super(anecdote);
        support = new PropertyChangeSupport(this);
        rating = Rating.None;
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
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    @Override
    public void setRating(Rating rating) {
        if (this.rating == rating) return;
        Rating oldRating = this.rating, newRating = rating;
        this.rating = rating;
        support.firePropertyChange("rating", oldRating, newRating);
    }

    /**
     * Подписывает указанный объект на прослушивание изменений оценки этого анекдота.
     * @param listener объект, который будет подписан на прослушивание изменений оценки.
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Отписывает указанный объект от прослушивания изменений оценки этого анекдота.
     * @param listener объект, который будет отписан от прослушивания изменений оценки.
     */
    @Override
    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
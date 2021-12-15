package anecdote;

import event.ActionListener;

/**
 * Определяет интерфейс анекдота, который может быть оценен.
 */
public interface IRatableAnecdote extends IAnecdote {

    /**
     * Когда переопределен, возвращает текущую оценку анекдота.
     * @return Текущая оценка анекдота.
     */
    Rating getRating();

    /**
     * Когда переопределен, присваивает указанную оценку анекдоту.
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    void setRating(Rating rating);

    /**
     * Когда переопределен, подписывает указанный объект на прослушивание изменений оценки этого анекдота.
     * @param listener объект, который будет подписан на прослушивание изменений оценки.
     */
    void addListener(ActionListener listener);

    /**
     * Когда переопределен, отписывает указанный объект от прослушивания изменений оценки этого анекдота.
     * @param listener объект, который будет отписан от прослушивания изменений оценки.
     */
    void removeListener(ActionListener listener);

}
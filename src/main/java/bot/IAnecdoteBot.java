package bot;

import anecdote.IAnecdote;
import anecdote.IRatableAnecdoteRepository;
import anecdote.Rating;

/**
 * Определяет интерфейс бота, который может
 * отравлять анекдоты и принимать оценки для анекдотов.
 */
public interface IAnecdoteBot {

    /**
     * Когда переопределен, возвращает имя бота.
     * @return Имя бота.
     */
    String getName();

    /**
     * Когда переопределен, указывает активен ли бот, т.е. ожидает ли он
     * следующего ввода пользователем сообщения.
     * @return true, если бот активен, иначе false.
     */
    boolean isActive();

    /**
     * Когда переопределен, указывает, был ли прежде рассказан анекдот.
     * @return true, если анекдот был прежде рассказан, иначе false.
     */
    boolean wasAnecdoteTold();

    /**
     * Когда переопределен, сбрасывает текущее состояние бота к стандартному.
     */
    void resetState();

    /**
     * Когда переопределен, заставляет бота деактивироваться и
     * перестать ожидать ввода пользователя.
     */
    void deactivate();

    /**
     * Когда переопределен, указывает есть ли у бота еще анекдоты,
     * которые могут быть рассказаны.
     * @return true, если у бота еще анекдоты, которые могут быть рассказаны,
     * иначе false.
     */
    boolean hasAnecdotes();

    /**
     * Когда переопределен, возвращает следующий анекдот.
     * @return Анекдот.
     */
    IAnecdote getNextAnecdote();

    /**
     * Когда переопределен, возвращает массив анекдотов,
     * которые имеют указанную оценку.
     * @return Массив анекдотов, которые имеют указанную оценку.
     */
    IAnecdote[] getAnecdotesOfRating(Rating rating);

    /**
     * Когда переопределен, присваивает указанную оценку
     * последнему рассказанному анекдоту, если таковой есть.
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    void setRatingForLastAnecdote(Rating rating);

    // todo: add javadoc
    String executeCommand(String input);

    // todo: remove it later
    IRatableAnecdoteRepository getAnecdoteRepository();
}
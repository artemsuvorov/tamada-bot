package anecdote;

/**
 * Определяет интерфейс репозитория, состоящего из поддерживающих оценивание анекдотов.
 */
public interface IRatableAnecdoteRepository extends IAnecdoteRepository {

    /**
     * Когда переопределен, возвращает список любимых анекдотов.
     * @return Список любимых анекдотов.
     */
    Anecdote[] getFavorites();

    /**
     * Когда переопределен, возвращает список анекдотов, имеющих указанную оценку.
     * @param rating оценка, которую имеют анекдоты.
     * @return Список анекдотов, имеющих указанную оценку.
     */
    Anecdote[] getAnecdotesOfRating(Rating rating);

}
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

    /**
     * Когда переопределен, указывает, содержится ли переданный анекдот среди оцененных анекдотов.
     * @param anecdote анекдот, который будет проверен на нахождение среди оцененных анекдотов.
     * @return true, если анекдот содержится среди оцененных анекдотов, иначе false.
     */
    boolean containsRatedAnecdote(Anecdote anecdote);
}
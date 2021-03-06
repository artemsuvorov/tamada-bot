package anecdote;

import java.io.Serializable;

/**
 * Определяет интерфейс репозитория анекдотов, который выдает анекдоты один за другим.
 */
public interface IAnecdoteRepository extends Serializable {

    /**
     * Когда переопределен, возвращает количество анекдотов,
     * которые содержатся в этом репозитории.
     * @return количество анекдотов, которые содержатся в этом репозитории.
     */
    int getCount();

    /**
     * Когда переопределен, указывает, является ли этот репозиторий пустым.
     * @return true, если репозиторий пуст, иначе false.
     */
    boolean hasAnecdotes();

    /**
     * Когда переопределен, указывает, содержится ли переданный анекдот в этом репозитории.
     * @param anecdote анекдот, который будет проверен на нахождение в этом репозитории.
     * @return true, если анекдот содержится в этом репозитории, иначе false.
     */
    boolean contains(Anecdote anecdote);

    /**
     * Когда переопределен, возвращает следующий анекдот из этого репозитория.
     * @return Следующий анекдот из этого репозитория.
     */
    Anecdote getNextAnecdote();

}
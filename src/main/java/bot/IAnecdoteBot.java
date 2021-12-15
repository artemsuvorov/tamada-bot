package bot;

import anecdote.Anecdote;
import anecdote.IRatableAnecdoteRepository;
import anecdote.InternetAnecdoteRepository;
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

    // todo: add javadoc
    long getAssociatedId();

    /**
     * Когда переопределен, возвращает текущее состояние бота BotState.
     * @return Текущее состояние бота BotState.
     */
    BotState getState();

    /**
     * Когда переопределен, сбрасывает текущее состояние бота к стандартному.
     */
    void resetState();

    /**
     * Когда переопределен, заставляет бота активироваться.
     */
    void activate();

    /**
     * Когда переопределен, заставляет бота деактивироваться.
     */
    void deactivate();

    /**
     * Когда переопределен, устанавливает новую конфигурацию боту,
     * которая содержит все его сообщения.
     * @param config новая конфигурация боту, которая содержит все его сообщения.
     */
    void setConfig(BotConfiguration config);

    /**
     * Когда переопределен, указывает есть ли у бота еще анекдоты,
     * которые могут быть рассказаны.
     * @return true, если у бота еще анекдоты, которые могут быть рассказаны,
     * иначе false.
     */
    boolean hasAnecdotes();

    // todo: add javadoc
    Anecdote getLastAnecdote();

    /**
     * Когда переопределен, возвращает следующий анекдот.
     * @return Анекдот.
     */
    Anecdote getNextAnecdote();

    /**
     * Когда переопределен, возвращает массив анекдотов,
     * которые имеют указанную оценку.
     * @return Массив анекдотов, которые имеют указанную оценку.
     */
    Anecdote[] getAnecdotesOfRating(Rating rating);

    /**
     * Когда переопределен, присваивает указанную оценку
     * последнему рассказанному анекдоту, если таковой есть.
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    void setRatingForLastAnecdote(Rating rating);

    /**
     * Когда переопределен, дописывает указанную концовку
     * последнему рассказанному анекдоту, если таковой есть,
     * и возвращает получившийся анекдот в виде строки.
     * @param ending концовка, которая будет дописана анекдоту.
     * @return Анекдот с новой концовкой в виде строки.
     */
    void setEndingForLastAnecdote(String ending);

    /**
     * Когда переопределен, заставляет бота исполнить команду, содержащуюся
     * в указанной строке ввода, и возвращает строку, содержащую сообщение результата.
     * @param input строка ввода, которая содержит команду и передаваемые аргументы.
     * @return Строку, содержащая сообщение результата.
     */
    String executeCommand(String input);

    // todo: probably remove it later
    void pullCommonAnecdotes();

    /**
     * Когда переопределен, сереализует этого бота в строку String.
     * @return Строку String, содержащую данные сериализованного бота.
     */
    String serialize();

    /**
     * Когда переопределен, десереализует бота и перезаписывает поля этого бота
     * новыми данными из указанных данных, переданных в виде строки String.
     * @param data Строка, содержащая сериализованного бота, чьи данные будут
     *             десериализованы и присвоены этому боту.
     */
    void deserialize(String data);

    // todo: add javadoc and place it up
    InternetAnecdoteRepository getAnecdoteRepository();
}
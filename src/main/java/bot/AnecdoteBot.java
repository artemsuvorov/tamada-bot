package bot;

import anecdote.*;
import commands.CommandStorage;
import commands.InputPredicateStorage;
import commands.UserInput;

import java.io.PrintStream;

/**
 * Представляет собой класс бота, который может отравлять анекдоты,
 * принимать оценки для анекдотов и выполнять другие ботовские команды.
 */
public final class AnecdoteBot implements IAnecdoteBot {

    private final String name;
    private final IRatableAnecdoteRepository anecdoteRepository;

    private final InputPredicateStorage predicates;
    private final CommandStorage commands;

    private BotState state = BotState.Default;
    private IRatableAnecdote lastAnecdote;

    public AnecdoteBot(BotConfiguration config, PrintStream out) {
        this.name = config.BotName;
        anecdoteRepository = new InternetAnecdoteRepository(config.Anecdotes);

        predicates = new InputPredicateStorage();
        commands = new CommandStorage(this, config, out);
    }

    /**
     * Возвращает имя бота.
     * @return Имя бота.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Указывает активен ли бот, т.е. ожидает ли он
     * следующего ввода пользователем сообщения.
     * @return true, если бот активен, иначе false.
     */
    @Override
    public boolean isActive() {
        return state != BotState.Deactivated;
    }

    /**
     * Указывает, был ли прежде рассказан анекдот.
     * @return true, если анекдот был прежде рассказан, иначе false.
     */
    @Override
    public boolean wasAnecdoteTold() {
        return state == BotState.AnecdoteTold;
    }

    /**
     * Сбрасывает текущее состояние бота к стандартному.
     */
    @Override
    public void resetState() {
        state = BotState.Default;
    }

    /**
     * Заставляет бота деактивироваться и перестать ожидать ввода пользователя.
     */
    @Override
    public void deactivate() {
        state = BotState.Deactivated;
    }

    /**
     * Указывает есть ли у бота еще анекдоты, которые могут быть рассказаны.
     * @return true, если у бота еще анекдоты, которые могут быть рассказаны,
     * иначе false.
     */
    @Override
    public boolean hasAnecdotes() {
        return anecdoteRepository.hasAnecdotes();
    }

    /**
     * Возвращает следующий анекдот.
     * @return Анекдот.
     */
    @Override
    public IAnecdote getNextAnecdote() {
        resetState();
        var anecdote = anecdoteRepository.getNextAnecdote();
        if (anecdote == null)
            return null;
        state = BotState.AnecdoteTold;
        if (anecdote instanceof IRatableAnecdote ratableAnecdote)
            lastAnecdote = ratableAnecdote;
        return anecdote;
    }

    /**
     * Возвращает массив анекдотов, которые имеют указанную оценку.
     * @return Массив анекдотов, которые имеют указанную оценку.
     */
    @Override
    public IAnecdote[] getAnecdotesOfRating(Rating rating) {
        resetState();
        var anecdotes = anecdoteRepository.getAnecdotesOfRating(rating);
        if (anecdotes == null || anecdotes.length <= 0)
            return null;
        return anecdotes;
    }

    /**
     * Присваивает указанную оценку последнему рассказанному анекдоту,
     * если таковой есть.
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    @Override
    public void setRatingForLastAnecdote(Rating rating) {
        if (state != BotState.AnecdoteTold) return;
        lastAnecdote.setRating(rating);
        resetState();
    }

    // todo: add javadoc
    @Override
    public String executeCommand(String input) {
        var commandName = predicates.getCommandNameOrNull(input);
        var command = commands.getCommandOrNull(commandName);
        if (command == null) command = commands.getNotUnderstandCommand();
        return command.execute(new UserInput(input));
    }

    // todo: remove it later
    @Override
    public IRatableAnecdoteRepository getAnecdoteRepository() {
        return anecdoteRepository;
    }

}
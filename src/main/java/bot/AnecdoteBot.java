package bot;

import anecdote.*;
import commands.CommandStorage;
import commands.InputPredicateStorage;
import commands.UserInput;
import commands.botCommands.BotCommand;

import java.io.PrintStream;

/**
 * Представляет собой класс бота, который может отравлять анекдоты,
 * принимать оценки для анекдотов и выполнять другие ботовские команды.
 */
public final class AnecdoteBot implements IAnecdoteBot {

    private String name;
    private final IRatableAnecdoteRepository anecdoteRepository;

    private final InputPredicateStorage predicates;
    private CommandStorage commands;
    private final PrintStream out;

    private BotState state = BotState.Deactivated;
    private IAnecdote lastAnecdote;

    public AnecdoteBot(BotConfiguration config, PrintStream out) {
        this.name = config.BotName;
        anecdoteRepository = new InternetAnecdoteRepository();

        this.out = out;
        this.predicates = new InputPredicateStorage();
        this.commands = new CommandStorage(this, config, this.out);
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
     * Возвращает текущее состояние бота BotState.
     * @return Текущее состояние бота BotState.
     */
    @Override
    public BotState getState() {
        return state;
    }

    /**
     * Сбрасывает текущее состояние бота к стандартному.
     */
    @Override
    public void resetState() {
        state = BotState.Default;
    }

    /**
     * Заставляет бота активироваться.
     */
    @Override
    public void activate() {
        state = BotState.Default;
    }

    /**
     * Заставляет бота деактивироваться.
     */
    @Override
    public void deactivate() {
        state = BotState.Deactivated;
    }

    /**
     * Устанавливает новую конфигурацию боту, которая содержит все его сообщения.
     * @param config новая конфигурация боту, которая содержит все его сообщения.
     */
    @Override
    public void setConfig(BotConfiguration config) {
        this.name = config.BotName;
        this.commands = new CommandStorage(this, config, this.out);
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
        if (anecdote instanceof UnfinishedAnecdote unfinished && !unfinished.hasEnding())
            state = BotState.UnfinishedAnecdoteTold;
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
        ((RatableAnecdote)lastAnecdote).setRating(rating);
        resetState();
    }

    /**
     * Когда переопределен, дописывает указанную концовку
     * последнему рассказанному анекдоту, если таковой есть,
     * и возвращает получившийся анекдот в виде строки.
     * @param ending концовка, которая будет дописана анекдоту.
     * @return Анекдот с новой концовкой в виде строки.
     */
    @Override
    public String setEndingForLastAnecdote(String ending) {
        if (state != BotState.UnfinishedAnecdoteTold)
            return null;
        ((UnfinishedAnecdote)lastAnecdote).setEnding(ending);
        var resultingAnecdote = lastAnecdote.getText();
        resetState();
        state = BotState.AnecdoteTold;
        return resultingAnecdote;
    }

    /**
     * Заставляет бота исполнить команду, содержащуюся в указанной строке ввода,
     * и возвращает строку, содержащую сообщение результата.
     * @param input строка ввода, которая содержит команду и передаваемые аргументы.
     * @return Строку, содержащая сообщение результата.
     */
    @Override
    public String executeCommand(String input) {
        if (input == null)
            return  commands.getNotUnderstandCommand().execute(UserInput.Empty);

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
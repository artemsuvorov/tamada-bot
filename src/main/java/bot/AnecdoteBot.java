package bot;

import anecdote.*;
import commands.CommandResult;
import commands.CommandStorage;
import commands.InputPredicateStorage;
import commands.UserInput;

import java.io.PrintStream;

/**
 * Представляет собой класс бота, который может отравлять анекдоты,
 * принимать оценки для анекдотов и выполнять другие ботовские команды.
 */
public final class AnecdoteBot implements IAnecdoteBot {

    private final long id;
    private String name;

    private IRatableAnecdoteRepository anecdoteRepository;

    private CommandStorage commands;
    private final PrintStream out;

    private BotState state = BotState.Deactivated;
    private Anecdote lastAnecdote;

    public AnecdoteBot(BotConfiguration config, PrintStream out) {
        this(0, config, new InternetAnecdoteRepository(0), out);
    }

    public AnecdoteBot(long id, BotConfiguration config, IRatableAnecdoteRepository repository, PrintStream out) {
        this.id = id;
        this.name = config.BotName;
        this.anecdoteRepository = repository;

        this.out = out;
        this.commands = new CommandStorage(this, config, this.out);
    }

    /**
     * Возвращает имя бота.
     *
     * @return Имя бота.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Возвращает уникальный id номер, проассоциированный с этим ботом.
     * Этот id номер соответствует номеру текущего Telegram-чата.
     * @return id номер, проассоциированный с этим ботом.
     */
    @Override
    public long getAssociatedId() {
        return id;
    }

    /**
     * Возвращает текущее состояние бота BotState.
     *
     * @return Текущее состояние бота BotState.
     */
    @Override
    public BotState getState() {
        return state;
    }

    /**
     * Возвращает репозиторий анекдотов бота.
     * @return репозиторий анекдотов бота.
     */
    @Override
    public IRatableAnecdoteRepository getAnecdoteRepository() {
        return anecdoteRepository;
    }

    /**
     * Устанавливает боту указанный репозиторий анекдотов.
     * @param repository репозиторий анекдотов, который будет установлен боту.
     */
    @Override
    public void setAnecdoteRepository(IRatableAnecdoteRepository repository) {
        anecdoteRepository = repository;
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
     *
     * @param config новая конфигурация боту, которая содержит все его сообщения.
     */
    @Override
    public void setConfig(BotConfiguration config) {
        this.name = config.BotName;
        this.commands = new CommandStorage(this, config, this.out);
    }

    /**
     * Указывает есть ли у бота еще анекдоты, которые могут быть рассказаны.
     *
     * @return true, если у бота еще анекдоты, которые могут быть рассказаны,
     * иначе false.
     */
    @Override
    public boolean hasAnecdotes() {
        return anecdoteRepository.hasAnecdotes();
    }

    /**
     * Возвращает последний рассказанный ботом анекдот.
     * @return Последний рассказанный ботом анекдот.
     */
    @Override
    public Anecdote getLastAnecdote() {
        return lastAnecdote;
    }

    /**
     * Возвращает следующий анекдот.
     *
     * @return Анекдот.
     */
    @Override
    public Anecdote getNextAnecdote() {
        resetState();
        var anecdote = anecdoteRepository.getNextAnecdote();
        if (anecdote == null)
            return null;
        state = BotState.AnecdoteTold;
        if (anecdote instanceof RatableAnecdote ratableAnecdote)
            lastAnecdote = ratableAnecdote;
        if (anecdote instanceof UnfinishedAnecdote unfinished) {
            if (!unfinished.hasEnding())
                state = BotState.UnfinishedAnecdoteTold;
            else if (id == unfinished.getAuthorId())
                state = BotState.UserAnecdoteTold;
        }
        return anecdote;
    }

    /**
     * Возвращает массив анекдотов, которые имеют указанную оценку.
     *
     * @return Массив анекдотов, которые имеют указанную оценку.
     */
    @Override
    public Anecdote[] getAnecdotesOfRating(Rating rating) {
        resetState();
        var anecdotes = anecdoteRepository.getAnecdotesOfRating(rating);
        if (anecdotes == null || anecdotes.length <= 0)
            return null;
        return anecdotes;
    }

    /**
     * Присваивает указанную оценку последнему рассказанному анекдоту,
     * если таковой есть.
     *
     * @param rating оценка, которая будет присвоена анекдоту.
     */
    @Override
    public void setRatingForLastAnecdote(Rating rating) {
        if (!state.wasAnecdoteTold()) return;
        ((RatableAnecdote) lastAnecdote).setRating(id, rating);
        resetState();
    }

    /**
     * Когда переопределен, дописывает указанную концовку
     * последнему рассказанному анекдоту, если таковой есть,
     * и возвращает получившийся анекдот в виде строки.
     *
     * @param ending концовка, которая будет дописана анекдоту.
     * @return Анекдот с новой концовкой в виде строки.
     */
    @Override
    public void setEndingForLastAnecdote(String ending) {
        if (!state.wasUnfinishedAnecdoteTold()) return;
        UnfinishedAnecdote unfinishedAnecdote = (UnfinishedAnecdote)lastAnecdote;
        unfinishedAnecdote.setEnding(id, ending);
        CommonAnecdoteList.get().add(unfinishedAnecdote);
        state = BotState.UserAnecdoteTold;
    }

    /**
     * Заставляет бота исполнить команду, содержащуюся в указанной строке ввода,
     * и возвращает строку, содержащую сообщение результата.
     * @param input строка ввода, которая содержит команду и передаваемые аргументы.
     * @return Результат исполнения команды {@link CommandResult},
     * содержащий сообщение {@link String} и текущее состояние бота {@link BotState}.
     */
    @Override
    public CommandResult executeCommand(String input) {
        if (input == null) {
            String message = commands.getNotUnderstandCommand().execute(UserInput.Empty);
            return new CommandResult(message, getState());
        }

        var commandName = InputPredicateStorage.getCommandNameOrNull(input);
        var command = commands.getCommandOrNull(commandName);
        if (command == null) command = commands.getNotUnderstandCommand();

        String message = command.execute(new UserInput(input));
        return new CommandResult(message, getState());
    }

}
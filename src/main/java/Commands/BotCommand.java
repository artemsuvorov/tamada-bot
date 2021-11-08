package Commands;

import Bot.BotMessage;
import Bot.IAnecdoteBot;

/**
 * Определяет абстрактный класс команды, которая исполняется ботом.
 */
public abstract class BotCommand {

    protected final IAnecdoteBot Bot;

    protected BotCommand(IAnecdoteBot bot) {
        Bot = bot;
    }

    // TODO: make return void
    /**
     * Когда переопределена в наследуемом классе,
     * заставляет бота выполнить некоторую команду.
     */
    public abstract void execute();

}
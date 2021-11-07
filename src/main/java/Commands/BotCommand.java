package Commands;

import Bot.Bot;
import Bot.BotMessage;

/**
 * Определяет абстрактный класс команды, которая исполняется ботом.
 */
public abstract class BotCommand {

    protected final Bot Bot;

    protected BotCommand(Bot bot) {
        Bot = bot;
    }

    // TODO: make return void
    /**
     * Когда переопределена в наследуемом классе,
     * заставляет бота выполнить некоторую команду.
     */
    public abstract BotMessage execute();

}
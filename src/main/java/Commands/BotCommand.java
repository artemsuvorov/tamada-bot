package Commands;

import Bot.Bot;
import Bot.BotMessage;

/**
 * Defines an abstract class of a command that can be executed by the bot.
 */
public abstract class BotCommand {

    protected final Bot Bot;

    protected BotCommand(Bot bot) {
        Bot = bot;
    }

    /**
     * When overridden in a derived class, executes the command
     * by the bot and returns the resulting message.
     * @return the result message after the command execution.
     */
    public abstract BotMessage execute();

}
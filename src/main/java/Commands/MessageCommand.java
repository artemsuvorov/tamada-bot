package Commands;

import Bot.Bot;
import Bot.BotMessage;

import java.util.function.Function;

/**
 * Represents a command that after execution by the bot returns a message.
 */
public class MessageCommand extends BotCommand {

    Function<Bot, BotMessage> _messageSelector;

    public MessageCommand(Bot bot, Function<Bot, BotMessage> messageSelector) {
        super(bot);
        _messageSelector = messageSelector;
    }

    /**
     * Returns the message from the bot.
     * @return the message from the bot.
     */
    @Override
    public BotMessage execute() {
        return _messageSelector.apply(Bot);
    }

}
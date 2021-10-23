package Commands;

import Bot.Bot;
import Bot.BotMessage;

import java.util.function.Function;

public class MessageCommand extends BotCommand {

    Function<Bot, BotMessage> _messageSelector;

    public MessageCommand(Bot bot, Function<Bot, BotMessage> messageSelector) {
        super(bot);
        _messageSelector = messageSelector;
    }

    @Override
    public BotMessage execute() {
        return _messageSelector.apply(getBot());
    }

}
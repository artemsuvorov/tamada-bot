package Commands;

import Bot.Bot;
import Bot.BotMessage;

import java.util.function.Function;

/**
 * Представляет собой комманду, которая может быть напечатана в поток
 * выходных данных как сообщение после выполнения действия бота.
 */
public class MessageCommand extends BotCommand {

    Function<Bot, BotMessage> messageSelector;

    public MessageCommand(Bot bot, Function<Bot, BotMessage> messageSelector) {
        super(bot);
        this.messageSelector = messageSelector;
    }

    // TODO: make execute method to print message into out stream
    /**
     * Выполняет заданное действие бота и
     * печатает результирующее сообщение бота в поток выходных данных.
     */
    @Override
    public BotMessage execute() {
        return messageSelector.apply(Bot);
    }

}
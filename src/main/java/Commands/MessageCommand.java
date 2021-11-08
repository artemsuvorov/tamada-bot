package Commands;

import Bot.BotMessage;
import Bot.IAnecdoteBot;

import java.io.PrintStream;
import java.util.function.Function;

/**
 * Представляет собой комманду, которая может быть напечатана в поток
 * выходных данных как сообщение после выполнения действия бота.
 */
public class MessageCommand extends BotCommand {

    private final Function<IAnecdoteBot, BotMessage> action;
    private final PrintStream out;

    public MessageCommand(IAnecdoteBot bot, PrintStream out, Function<IAnecdoteBot, BotMessage> action) {
        super(bot);
        this.action = action;
        this.out = out;
    }

    /**
     * Выполняет заданное действие бота и
     * печатает результирующее сообщение бота в поток выходных данных.
     */
    @Override
    public void execute() {
        var message = action.apply(Bot);
        out.println(message);
    }

}
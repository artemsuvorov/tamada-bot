package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class NotUnderstandCommand extends BotCommand {

    public NotUnderstandCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().notUnderstand();
    }

}
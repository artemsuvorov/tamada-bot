package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class HelpCommand extends BotCommand {

    public HelpCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().onWhatCanYouDo();
    }

}
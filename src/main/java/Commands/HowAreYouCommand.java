package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class HowAreYouCommand extends BotCommand {

    public HowAreYouCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().onHowAreYou();
    }

}
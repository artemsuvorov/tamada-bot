package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class HelloCommand extends BotCommand {

    public HelloCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().greet();
    }

}
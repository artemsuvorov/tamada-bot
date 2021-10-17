package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class IntroduceCommand extends BotCommand {

    public IntroduceCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().introduce();
    }

}
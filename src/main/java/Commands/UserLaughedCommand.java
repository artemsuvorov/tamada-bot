package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class UserLaughedCommand extends BotCommand {

    public UserLaughedCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().onUserLaughed();
    }

}
package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class StartConversationCommand extends BotCommand {

    public StartConversationCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().onStartConversation();
    }

}
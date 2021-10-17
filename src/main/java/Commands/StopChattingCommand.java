package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class StopChattingCommand extends BotCommand {

    public StopChattingCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().stopChatting();
    }

}
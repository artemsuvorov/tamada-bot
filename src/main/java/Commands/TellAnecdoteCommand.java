package Commands;

import Bot.Bot;
import Bot.BotMessage;

public class TellAnecdoteCommand extends BotCommand {

    public TellAnecdoteCommand(Bot bot) {
        super(bot);
    }

    @Override
    public BotMessage execute() {
        return getBot().tellAnecdote();
    }

}
package Commands;

import Bot.Bot;
import Bot.BotMessage;

public abstract class BotCommand {

    private final Bot _bot;

    protected BotCommand(Bot bot) {
        _bot = bot;
    }

    protected final Bot getBot() {
        return _bot;
    }

    public abstract BotMessage execute();

}
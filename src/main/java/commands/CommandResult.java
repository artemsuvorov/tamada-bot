package commands;

import bot.BotState;

public class CommandResult {

    public final String Message;
    public final BotState BotState;

    public CommandResult(String message, BotState botState) {
        Message = message;
        BotState = botState;
    }

}
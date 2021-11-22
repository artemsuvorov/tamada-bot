package commands.botCommands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;

import java.io.PrintStream;

public class StartConversationCommand extends BotCommand {

    public StartConversationCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        var text = Config.ConversationStart;
        return printBotMessage(text);
    }

}
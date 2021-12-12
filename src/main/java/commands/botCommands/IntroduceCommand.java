package commands.botCommands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;

import java.io.PrintStream;

public class IntroduceCommand extends BotCommand {

    public IntroduceCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        var text = Config.Introduction;
        return printBotMessage(text);
    }

}
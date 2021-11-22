package commands.botCommands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class StopCommand extends BotCommand {

    public StopCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        var text = Randomizer.getRandomElement(Config.StopChatMessages);
        Bot.deactivate();
        return printBotMessage(text);
    }

}
package commands.botCommands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class HowAreYouCommand extends BotCommand {

    public HowAreYouCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        var text = Randomizer.getRandomElement(Config.OnHowAreYouMessages);
        return printBotMessage(text);
    }

}
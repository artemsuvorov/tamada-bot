package commands.botCommands;

import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class NotUnderstandCommand extends BotCommand {

    public NotUnderstandCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        var text = Randomizer.getRandomElement(Config.NotUnderstandMessages);
        printBotMessage(text);
    }

}
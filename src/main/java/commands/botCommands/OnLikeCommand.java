package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class OnLikeCommand extends BotCommand {

    public OnLikeCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        if (!Bot.wasAnecdoteTold()) {
            printBotMessage(Config.OnNoAnecdotesToRateMessage);
            return;
        }

        Bot.setRatingForLastAnecdote(Rating.Excellent);
        printBotMessage(Randomizer.getRandomElement(Config.OnLikeRatingMessages));
    }

}
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
    public String execute(UserInput input) {
        if (!Bot.wasAnecdoteTold())
            return printBotMessage(Config.OnNoAnecdotesToRateMessage);

        Bot.setRatingForLastAnecdote(Rating.Excellent);
        return printBotMessage(Randomizer.getRandomElement(Config.OnLikeRatingMessages));
    }

}
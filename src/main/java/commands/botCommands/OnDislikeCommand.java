package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.BotState;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class OnDislikeCommand extends BotCommand {

    public OnDislikeCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (Bot.getState() != BotState.AnecdoteTold)
            return printBotMessage(Config.OnNoAnecdotesToRateMessage);

        Bot.setRatingForLastAnecdote(Rating.Dislike);
        return printBotMessage(Randomizer.getRandomElement(Config.OnDislikeRatingMessages));
    }

}
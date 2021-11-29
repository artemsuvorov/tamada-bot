package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.BotState;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class OnUserRatingCommand extends BotCommand {

    public OnUserRatingCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (Bot.getState() != BotState.AnecdoteTold)
            return printBotMessage(Config.OnNoAnecdotesToRateMessage);

        if (!input.hasInteger())
            return printBotMessage(Config.OnRateNoRatingProvided);

        var number = input.getNextInteger();
        if (number < 1 || number > 5)
            return printBotMessage(Config.OnRateInvalidRatingProvided);

        var rating = Rating.fromInteger(number);
        var text = getTextForRating(rating);
        Bot.setRatingForLastAnecdote(rating);
        return printBotMessage(text);
    }

    private String getTextForRating(Rating rating) {
        String[] messages;
        if (rating == Rating.Excellent)
            messages = Config.OnLikeRatingMessages;
        else if (rating == Rating.Dislike)
            messages = Config.OnDislikeRatingMessages;
        else
            messages = Config.OnAnyRatingMessages;
        return Randomizer.getRandomElement(messages);
    }

}
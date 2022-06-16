package commands.botCommands;

import anecdote.RatableAnecdote;
import anecdote.Rating;
import anecdote.UnfinishedAnecdote;
import bot.BotConfiguration;
import bot.BotState;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;
import java.text.DecimalFormat;

public class OnUserRatingCommand extends BotCommand {

    public OnUserRatingCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (!Bot.getState().wasAnecdoteTold())
            return printBotMessage(Config.OnNoAnecdotesToRateMessage);

        var anecdote = Bot.getLastAnecdote();
        if (anecdote instanceof RatableAnecdote)
            if (Bot.getState() == BotState.UserAnecdoteTold && Bot.getAnecdoteRepository().containsRatedAnecdote(anecdote))
                return printBotMessage(Config.OnUserAnecdoteRateTwiceMessage);

        if (!input.hasInteger())
            return printBotMessage(Config.OnRateNoRatingProvided);

        var number = input.getNextInteger();
        if (number < 1 || number > 5)
            return printBotMessage(Config.OnRateInvalidRatingProvided);

        var rating = Rating.fromInteger(number);
        var text = getTextForRating(rating);
        Bot.setRatingForLastAnecdote(rating);

        if (anecdote instanceof UnfinishedAnecdote unfinishedAnecdote && unfinishedAnecdote.hasEnding()) {
            var formattedRating = new DecimalFormat("#.00").format(unfinishedAnecdote.getTotalRating());
            text += "\r\n" + Config.AnecdoteCurrentRatingMessage + " " + formattedRating;
        }

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
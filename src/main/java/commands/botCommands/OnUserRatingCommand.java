package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class OnUserRatingCommand extends BotCommand {

    public OnUserRatingCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        if (!Bot.wasAnecdoteTold()) {
            printBotMessage(Config.OnNoAnecdotesToRateMessage);
            return;
        }

        if (!input.hasInteger()) {
            printBotMessage(Config.OnRateNoRatingProvided);
            return;
        }

        var number = input.extractInteger();
        if (number < 1 || number > 5) {
            printBotMessage(Config.OnRateInvalidRatingProvided);
            return;
        }

        var rating = Rating.fromInteger(number);
        var text = getTextForRating(rating);
        Bot.setRatingForLastAnecdote(rating);
        printBotMessage(text);
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
package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ShowAnecdotesCommand extends BotCommand {

    public ShowAnecdotesCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (!input.hasInteger())
            return printBotMessage(Config.OnShowNoRatingProvided);

        var number = input.extractInteger();
        if (number < 1 || number > 5)
            return printBotMessage(Config.OnShowInvalidRatingProvided);

        var rating = Rating.fromInteger(number);
        var starter = Randomizer.getRandomElement(Config.OnShowAnecdotesMessages);
        var anecdotes = Bot.getAnecdotesOfRating(rating);

        if (anecdotes == null || anecdotes.length <= 0)
            return printBotMessage(Config.OnAnecdotesEmptyMessage);

        var joinedAnecdotes = Arrays.stream(anecdotes)
            .map(Object::toString).collect(Collectors.joining("\r\n\r\n"));
        return printBotMessage(starter + "\r\n\r\n" + joinedAnecdotes);
    }

}
package Commands.BotCommands;

import Anecdote.Rating;
import Bot.BotConfiguration;
import Bot.IAnecdoteBot;
import Commands.UserInput;
import Utils.Randomizer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ShowAnecdotesCommand extends BotCommand {

    public ShowAnecdotesCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        if (!input.hasInteger()) {
            printBotMessage(Config.OnShowNoRatingProvided);
            return;
        }

        var number = input.extractInteger();
        if (number < 1 || number > 5) {
            printBotMessage(Config.OnShowInvalidRatingProvided);
            return;
        }

        var rating = Rating.fromInteger(number);
        var starter = Randomizer.getRandomElement(Config.OnShowAnecdotesMessages);
        var anecdotes = Bot.getAnecdotesOfRating(rating);

        if (anecdotes == null || anecdotes.length <= 0) {
            printBotMessage(Config.OnAnecdotesEmptyMessage);
            return;
        }
        var joinedAnecdotes = Arrays.stream(anecdotes)
            .map(Object::toString).collect(Collectors.joining("\r\n\r\n"));
        printBotMessage(starter + "\r\n\r\n" + joinedAnecdotes);
    }

}
package commands.botCommands;

import anecdote.Rating;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ShowFavoritesCommand extends BotCommand {

    public ShowFavoritesCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        var starter = Randomizer.getRandomElement(Config.OnShowAnecdotesMessages);
        var anecdotes = Bot.getAnecdotesOfRating(Rating.Excellent);

        if (anecdotes == null || anecdotes.length <= 0)
            return printBotMessage(Config.OnAnecdotesEmptyMessage);

        var joinedAnecdotes = Arrays.stream(anecdotes)
                .map(Object::toString).collect(Collectors.joining("\r\n\r\n"));
        return printBotMessage(starter + "\r\n\r\n" + joinedAnecdotes);
    }

}
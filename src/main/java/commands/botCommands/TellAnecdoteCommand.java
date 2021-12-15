package commands.botCommands;

import anecdote.Anecdote;
import anecdote.UnfinishedAnecdote;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;
import java.text.DecimalFormat;

public class TellAnecdoteCommand extends BotCommand {

    public TellAnecdoteCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (!Bot.hasAnecdotes())
            return printBotMessage(Config.OnNoAnecdotesMessage);

        var anecdote = Bot.getNextAnecdote();
        if (anecdote == null)
            return printBotMessage(Config.OnNoAnecdotesMessage);

        if (anecdote instanceof UnfinishedAnecdote unfinished)
            return printUnfinishedAnecdote(unfinished);
        else
            return printAnecdote(anecdote);
    }

    private String printAnecdote(Anecdote anecdote) {
        var starter = Randomizer.getRandomElement(Config.AnecdoteStarters);
        return printBotMessage(starter + "\r\n\r\n" + anecdote.getText());
    }

    private String printUnfinishedAnecdote(UnfinishedAnecdote anecdote) {
        if (!anecdote.hasEnding())
            return printBotMessage(Config.OnTellUnfinishedAnecdote + "\r\n\r\n" + anecdote.getText());// todo: extract method
        else if (Bot.getAssociatedId() == anecdote.getAuthorId())
            return printFinishedAnecdote(Config.OnTellUsersAnecdote, Config.UserAnecdoteRatingMessage, anecdote);
        else if (Bot.getAssociatedId() != anecdote.getAuthorId())
            return printFinishedAnecdote(Config.OnTellAuthorAnecdote, Config.AuthorAnecdoteRatingMessage, anecdote);
        else
            return printAnecdote(anecdote);
    }

    private String printFinishedAnecdote(String starter, String ratingMessage, UnfinishedAnecdote anecdote) {
        var formattedRating = new DecimalFormat("#.00").format(anecdote.getTotalRating());
        return printBotMessage(starter + "\r\n\r\n" + anecdote.getText() +
                "\r\n\r\n" + ratingMessage + " " + formattedRating);
    }

}
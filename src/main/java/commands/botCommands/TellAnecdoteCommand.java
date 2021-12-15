package commands.botCommands;

import anecdote.Anecdote;
import anecdote.UnfinishedAnecdote;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

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

    private String printUnfinishedAnecdote(UnfinishedAnecdote unfinished) {
        if (!unfinished.hasEnding())
            return printBotMessage(Config.OnTellUnfinishedAnecdote + "\r\n\r\n" + unfinished.getText());
        else if (Bot.getAssociatedId() == unfinished.getAuthorId())
            return printBotMessage(Config.OnTellUsersAnecdote + "\r\n\r\n" + unfinished.getText()); // todo: extract method
        else if (Bot.getAssociatedId() != unfinished.getAuthorId())
            return printBotMessage(Config.OnTellAuthorAnecdote + "\r\n\r\n" + unfinished.getText() +
                    "\r\n\r\n" + Config.AuthorAnecdoteRatingMessage + " " + unfinished.getRating());
        else
            return printAnecdote(unfinished);
    }

}
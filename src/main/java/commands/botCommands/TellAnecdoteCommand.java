package commands.botCommands;

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

        var starter = Randomizer.getRandomElement(Config.AnecdoteStarters);
        var anecdote = Bot.getNextAnecdote();
        if (anecdote == null)
            return printBotMessage(Config.OnNoAnecdotesMessage);

        if (anecdote instanceof UnfinishedAnecdote unfinished && !unfinished.hasEnding())
            return printBotMessage(Config.OnTellUnfinishedAnecdote + "\r\n\r\n" + unfinished.getText());

        return printBotMessage(starter + "\r\n\r\n" + anecdote.getText());
    }

}
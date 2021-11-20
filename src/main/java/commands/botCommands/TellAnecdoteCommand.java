package commands.botCommands;

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
    public void execute(UserInput input) {
        if (!Bot.hasAnecdotes()) {
            printBotMessage(Config.OnNoAnecdotesMessage);
            return;
        }

        var starter = Randomizer.getRandomElement(Config.AnecdoteStarters);
        var anecdote = Bot.getNextAnecdote().getText();
        printBotMessage(starter + " " + anecdote);
    }

}
package Commands.BotCommands;

import Bot.BotConfiguration;
import Bot.IAnecdoteBot;
import Commands.UserInput;
import Utils.Randomizer;

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
package Commands.BotCommands;

import Anecdote.Rating;
import Bot.BotConfiguration;
import Bot.IAnecdoteBot;
import Commands.UserInput;
import Utils.Randomizer;

import java.io.PrintStream;

public class OnLikeCommand extends BotCommand {

    public OnLikeCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        if (!Bot.wasAnecdoteTold()) {
            printBotMessage(Config.OnNoAnecdotesToRateMessage);
            return;
        }

        Bot.setRatingForLastAnecdote(Rating.Excellent);
        printBotMessage(Randomizer.getRandomElement(Config.OnLikeRatingMessages));
    }

}
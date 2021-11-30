package commands.botCommands;

import bot.BotConfiguration;
import bot.BotState;
import bot.IAnecdoteBot;
import commands.UserInput;
import utils.Randomizer;

import java.io.PrintStream;

public class OnSuggestEndingCommand extends BotCommand {

    public OnSuggestEndingCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (Bot.getState() != BotState.UnfinishedAnecdoteTold)
            return printBotMessage(Config.OnNoAnecdotesToSuggest);

        var ending = String.join(" ", input.getArguments());
        if (ending == null || ending.isBlank())
            return printBotMessage(Config.OnInvalidSuggestion);

        var text = Randomizer.getRandomElement(Config.OnEndingSuggested);
        var anecdote = Bot.setEndingForLastAnecdote(ending);
        return printBotMessage(text + "\r\n\r\n" + anecdote);
    }

}
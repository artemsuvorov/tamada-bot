package Commands.BotCommands;

import Bot.BotConfiguration;
import Bot.IAnecdoteBot;
import Commands.UserInput;
import Utils.Randomizer;

import java.io.PrintStream;

public class OnLaughCommand extends BotCommand {

    public OnLaughCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        var text = Randomizer.getRandomElement(Config.OnLaughMessages);
        printBotMessage(text);
    }

}
package Commands.BotCommands;

import Bot.BotConfiguration;
import Bot.IAnecdoteBot;
import Commands.UserInput;

import java.io.PrintStream;

public class IntroduceCommand extends BotCommand {

    public IntroduceCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public void execute(UserInput input) {
        var text = Config.Introduction;
        printBotMessage(text);
    }

}
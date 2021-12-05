package commands.botCommands;

import bot.BotConfigRepository;
import bot.BotConfiguration;
import bot.IAnecdoteBot;
import commands.UserInput;

import java.io.PrintStream;
import java.util.Locale;

public class StartConversationCommand extends BotCommand {

    public StartConversationCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        super(bot, config, out);
    }

    @Override
    public String execute(UserInput input) {
        if (Bot.isActive() && !input.hasArguments())
            return printBotMessage(Config.OnAlreadyStarted);

        var config = getConfigFrom(input);
        if (config.BotName.equals(Config.BotName))
            return printBotMessage(Config.OnAlreadyStarted);

        Bot.setConfig(config);
        Bot.activate();
        var text = config.ConversationStart;
        return printBotMessage(text);
    }

    private BotConfiguration getConfigFrom(UserInput input) {
        if (input.hasArguments())
            return BotConfigRepository.getConfig(getMoodArgument(input));
        else
            return BotConfigRepository.getDefaultConfig();
    }

    private String getMoodArgument(UserInput input) {
        return input.getArguments()[0].trim().toLowerCase(Locale.ROOT);
    }

}
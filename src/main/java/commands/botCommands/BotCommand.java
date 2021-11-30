package commands.botCommands;

import bot.BotConfiguration;
import bot.BotMessage;
import bot.IAnecdoteBot;
import commands.UserInput;

import java.io.PrintStream;

/**
 * Определяет абстрактный класс команды, которая исполняется ботом.
 */
public abstract class BotCommand {

    protected final IAnecdoteBot Bot;
    protected final BotConfiguration Config;
    protected final PrintStream Out;

    protected BotCommand(IAnecdoteBot bot, BotConfiguration config, PrintStream out) {
        Bot = bot;
        Config = config;
        Out = out;
    }

    /**
     * Когда переопределен в наследуемом классе, заставляет бота выполнить
     * некоторую команду с учетом входного сообщения пользователя.
     * @param input входное сообщение пользователя, в котором содержится
     *              строковая команда боту.
     */
    public abstract String execute(UserInput input);

    /**
     * Вспомогательный метод, который заставляет бота
     * напечатать указанное сообщение в выходной поток данных.
     * @param text текст, который будет напечатан от имени бота.
     */
    protected String printBotMessage(String text) {
        var botMessage = new BotMessage(Bot.getName(), text);
        Out.println(botMessage);
        return botMessage.toString();
    }

}
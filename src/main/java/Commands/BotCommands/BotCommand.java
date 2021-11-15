package Commands.BotCommands;

import Bot.BotConfiguration;
import Bot.BotMessage;
import Bot.IAnecdoteBot;
import Commands.UserInput;

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
    public abstract void execute(UserInput input);

    /**
     * Вспомогательный метод, который заставляет бота
     * напечатать указанное сообщение в выходной поток данных.
     * @param text текст, который будет напечатан от имени бота.
     */
    protected void printBotMessage(String text) {
        var botMessage = new BotMessage(Bot.getName(), text);
        Out.println(botMessage);
    }

}
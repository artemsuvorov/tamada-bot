package Bot;

import Commands.BotCommand;
import Commands.CommandParser;
import Commands.MessageCommand;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Представляет собой вспомогательный клсс который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public final class BotRoutine {

    private final Bot bot;

    private final PrintStream out;
    private final InputStream in;

    private final Scanner scanner;
    private final CommandParser commandParser;

    public BotRoutine(Bot bot, PrintStream out, InputStream in) {
        this.bot = bot;

        this.out = out;
        this.in = in;

        scanner = new Scanner(this.in);
        commandParser = new CommandParser(this.bot);
    }

    /**
     * Запускает цикл взаимодействия пользователя и бота (ввод -> парсинг -> вывод).
     */
    public void start() {
        var startConversation = new MessageCommand(bot, bot -> bot.onStartConversation());
        executeCommand(startConversation);

        while (bot.isChatting()) {
            var input = scanner.nextLine();
            var command = commandParser.parse(input);
            executeCommand(command);
        }
    }

    /**
     * Исполняет указанную команду и печатает сообщение результата
     * в поток выходных данных.
     * @param command команда, которая будет исполнена.
     */
    private void executeCommand(BotCommand command) {
        var message = command.execute();
        out.println(message);
    }

}
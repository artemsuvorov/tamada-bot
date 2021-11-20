package Bot;

import Commands.CommandStorage;
import Commands.InputPredicateStorage;
import Commands.UserInput;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Представляет собой вспомогательный класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public class IOTamadaBotService extends IOBotService {

    private static final File configFile = new File("src\\main\\resources\\tamada-Config.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    private final InputPredicateStorage predicates;
    private final CommandStorage commands;

    protected final BotConfiguration Config;
    protected final IAnecdoteBot Bot;

    public IOTamadaBotService(PrintStream out, InputStream in) {
        super(out, in);
        Config = deserializeBotConfig(configFile, defaultEncoding);
        Bot = new AnecdoteBot(Config.BotName, Config.Anecdotes);
        predicates = new InputPredicateStorage();
        commands = new CommandStorage(Bot, Config, Out);
    }

    /**
     * Запускает цикл взаимодействия пользователя и бота (ввод -> парсинг -> вывод).
     */
    public void start() {
        var startConversation = commands.getStartConversationCommand();
        startConversation.execute(UserInput.Empty);

        try (var scanner = new Scanner(this.In)) {
            while (Bot.isActive()) {
                var input = scanner.nextLine();
                executeCommand(input);
            }
        }
    }

    /**
     * Когда переопределена в наследуемом классе, заставляет бота выполнить
     * некоторую команду с учетом входного сообщения пользователя.
     * @param input входное сообщение пользователя, в котором содержится
     *              строковая команда боту.
     */
    private void executeCommand(String input) {
        var commandName = predicates.getCommandNameOrNull(input);
        var command = commands.getCommandOrNull(commandName);
        if (command == null) command = commands.getNotUnderstandCommand();
        command.execute(new UserInput(input));
    }

}
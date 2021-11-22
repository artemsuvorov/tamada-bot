package bot;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Представляет собой вспомогательный класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public class IOTamadaBotService implements IBotService {

    private static final File configFile = new File("src\\main\\resources\\tamada-config.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    private final IAnecdoteBot bot;
    private final PrintStream out;
    private final InputStream in;

    public IOTamadaBotService(PrintStream out, InputStream in) {
        var config = BotConfiguration.deserializeBotConfig(configFile, defaultEncoding);
        this.out = out;
        this.in = in;
        bot = new AnecdoteBot(config, this.out);
    }

    /**
     * Запускает цикл взаимодействия пользователя и бота (ввод -> парсинг -> вывод).
     */
    public void start() {
        bot.executeCommand("/start");
        try (var scanner = new Scanner(this.in)) {
            while (bot.isActive()) {
                var input = scanner.nextLine();
                bot.executeCommand(input);
            }
        }
    }

}
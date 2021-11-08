package Bot;

import Commands.BotCommand;
import Commands.CommandParser;
import Commands.MessageCommand;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Представляет собой вспомогательный клсс который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public final class ConsoleTamadaBotService {

    private static final File configFile = new File("src\\main\\resources\\tamada-config.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    private final BotConfiguration config;
    private final IAnecdoteBot bot;
    private final CommandParser commandParser;
    private final PrintStream out;
    private final InputStream in;

    public ConsoleTamadaBotService(PrintStream out, InputStream in) {
        this.config = deserializeBotConfig();
        this.bot = new TamadaBot(config);
        this.out = out;
        this.in = in;
        commandParser = new CommandParser(this.bot, this.out);
    }

    /**
     * Запускает цикл взаимодействия пользователя и бота (ввод -> парсинг -> вывод).
     */
    public void start() {
        var startConversation = new MessageCommand(bot, out, IAnecdoteBot::onStartConversation);
        startConversation.execute();

        try (var scanner = new Scanner(this.in)) {
            while (bot.isActive()) {
                var input = scanner.nextLine();
                var command = commandParser.parse(input);
                command.execute();
            }
        }
    }

    // todo: delete it later
    // for tests
    public IAnecdoteBot getBot() {
        return bot;
    }

    // for tests
    public BotConfiguration getConfig() {
        return config;
    }

    /**
     * Парсит файл bot-config.json и возвращает полученную конфигурацию бота BotConfiguration.
     * @return Конфигурация бота полученная из файла bot-config.json.
     * @throws com.google.gson.JsonSyntaxException
     */
    private static BotConfiguration deserializeBotConfig() {
        var json = readBotConfigFile();
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    /**
     * Читает bot-config.json и возвращает его содержание в виде строки,
     * если операция чтения прошла успешно, иначе null.
     * @return Содержание файла bot-config.json в виде строки.
     */
    private static String readBotConfigFile() {
        try (var fileStream = new FileInputStream(configFile)) {
            var data = new byte[(int)configFile.length()];
            fileStream.read(data);
            return new String(data, defaultEncoding);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
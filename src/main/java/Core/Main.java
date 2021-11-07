package Core;

import Bot.BotConfiguration;
import Bot.BotRoutine;
import Bot.TamadaBot;

import com.google.gson.Gson;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final String configFilePath = new File("src\\main\\resources\\tamada-config.json").getAbsolutePath();
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * Основная точка входа в программу.
     * @param args Аргументы, задаваемые при запуске программы.
     */
    public static void main(String[] args) {
        var config = deserializeBotConfig();
        var tamada = new TamadaBot(config);
        var routine = new BotRoutine(tamada, System.out, System.in);
        routine.start();
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
        var path = Path.of(configFilePath);
        try {
            return Files.readString(path, defaultEncoding);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
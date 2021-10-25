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

    private static final String _configFilePath = new File("src\\main\\resources\\tamada-config.json").getAbsolutePath();
    private static final Charset _encoding = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        var config = deserializeBotConfig();
        var tamada = new TamadaBot(config);
        var routine = new BotRoutine(tamada, System.out, System.in);
        routine.start();
    }

    /**
     * Parses bot-config.json and returns Bot configuration object.
     * @return Bot configuration object parsed from bot-config.json
     * @throws com.google.gson.JsonSyntaxException
     */
    private static BotConfiguration deserializeBotConfig() {
        var json = readBotConfigFile();
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    /**
     * Reads and returns the contents of bot the config file.
     * @return The contents of the bot config file.
     */
    private static String readBotConfigFile() {
        var path = Path.of(_configFilePath);
        try {
            return Files.readString(path, _encoding);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
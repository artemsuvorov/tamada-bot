package Bot;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Представляет собой абстрактный класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public abstract class IOBotService implements IBotService {

    protected final PrintStream Out;
    protected final InputStream In;

    public IOBotService(PrintStream out, InputStream in) {
        Out = out;
        In = in;
    }

    public abstract void start();

    /**
     * Парсит файл Bot-Config.json и возвращает полученную конфигурацию бота BotConfiguration.
     * @return Конфигурация бота полученная из файла Bot-Config.json.
     * @throws com.google.gson.JsonSyntaxException
     */
    protected static BotConfiguration deserializeBotConfig(File configFile, Charset encoding) {
        var json = readBotConfigFile(configFile, encoding);
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    /**
     * Читает Bot-Config.json и возвращает его содержание в виде строки,
     * если операция чтения прошла успешно, иначе null.
     * @return Содержание файла Bot-Config.json в виде строки.
     */
    private static String readBotConfigFile(File configFile, Charset encoding) {
        try (var fileStream = new FileInputStream(configFile)) {
            var data = new byte[(int)configFile.length()];
            fileStream.read(data);
            return new String(data, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
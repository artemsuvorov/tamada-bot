package anecdote;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Представляет собой конфигурацию, содержащую массивы из анекдотов.
 */
public class AnecdotesConfiguration {

    private static final File anecdotesFile = new File("src\\main\\resources\\anecdotes.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * Массив обычных анекдотов.
     */
    public String[] Anecdotes;

    /**
     * Массив неоконченных анекдотов.
     */
    public String[] UnfinishedAnecdotes;

    /**
     * Возвращает общее количество анекдотов из всех массивов,
     * которые содержатся в этой конфигурации.
     * @return Общее количество анекдотов.
     */
    public int getTotalCount() {
        return (Anecdotes == null ? 0 : Anecdotes.length) +
                (UnfinishedAnecdotes == null ? 0 : UnfinishedAnecdotes.length);
    }

    /**
     * Парсит файл и возвращает полученную конфигурацию анекдотов AnecdotesConfiguration.
     * @return Конфигурация анекдотов полученная из файла.
     * @throws com.google.gson.JsonSyntaxException
     */
    public static AnecdotesConfiguration deserializeAnecdotesConfig() {
        var json = readBotConfigFile(anecdotesFile, defaultEncoding);
        var gson = new Gson();
        return gson.fromJson(json, AnecdotesConfiguration.class);
    }

    /**
     * Читает указанный файл и возвращает его содержание в виде строки,
     * если операция чтения прошла успешно, иначе null.
     * @return Содержание указанного файла в виде строки.
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
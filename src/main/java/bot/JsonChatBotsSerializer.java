package bot;

import anecdote.CommonAnecdoteList;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Представляет собой вспомогательный класс, который позволяет организованно
 * сериализовать данные для некоторого Telegram-бота в локально хранящийся Json файл,
 * а также десериализовать данные для всех Telegram-ботов во время их
 * совокупной инициализации (см. класс {@link bot.TelegramChatBots}).
 */
public class JsonChatBotsSerializer {

    private final static String extension = "json";
    private final static String commonAnecdotesFilename = "common";
    private final static File serializeDirectory = new File("src\\main\\resources\\chats");
    private final static Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * Сериализует в Json файл указанного бота IAnecdoteBot, связанного с указанным id Telegram-чата.
     * @param chatId id Telegram-чата, с которым связан указанный бот IAnecdoteBot.
     * @param bot бот, который будет сериализован в Json файл.
     */
    public void serializeBot(Long chatId, IAnecdoteBot bot) {
        String filename = chatId.toString() + "." + extension;
        String json = bot.serialize();
        writeFile(filename, json);
    }

    /**
     * Десериализует все файлы с данными ботов из локального хранилища ресурсов
     * и возвращает объект {@link bot.TelegramChatBots} т.е. коллекцию полученных ботов.
     * @return Коллекция десериализованных ботов в виде объекта {@link bot.TelegramChatBots}.
     */
    public TelegramChatBots deserializeAll() {
        TelegramChatBots bots = new TelegramChatBots();
        for (File file : serializeDirectory.listFiles()) {
            long chatId;
            try {
                chatId = Long.parseLong(getFilename(file));
            } catch (NumberFormatException ex) {
                continue;
            }
            IAnecdoteBot bot = bots.initNewBot(chatId);
            String json = readFile(file.toPath());
            bot.deserialize(json);
        }
        return bots;
    }

    // todo: add javadoc
    public void serializeCommonAnecdotes() {
        String filename = commonAnecdotesFilename + "." + extension;
        CommonAnecdoteList common = CommonAnecdoteList.get();
        String json = common.serializeCommonAnecdotes();
        writeFile(filename, json);
    }

    // todo: add javadoc
    public CommonAnecdoteList deserializeCommonAnecdotes() {
        String filename = commonAnecdotesFilename + "." + extension;
        Path path = serializeDirectory.toPath().resolve(filename);
        CommonAnecdoteList common = CommonAnecdoteList.get();
        if (Files.exists(path)) {
            String json = readFile(path);
            common.deserializeCommonAnecdotes(json);
        }
        return common;
    }

    // todo: add javadoc
    private void writeFile(String filename, String content) {
        File file = new File(serializeDirectory, filename);
        try (PrintWriter writer = new PrintWriter(file, defaultEncoding)) {
            writer.write(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // todo: add javadoc
    private String readFile(Path path) {
        try {
            String json = Files.readString(path, defaultEncoding);
            return json;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Извлекает из названия указанного файла его имя без расширения.
     * @param file файл, чье имя будет извлечено из названия.
     * @return Имя файла без расширения.
     */
    private String getFilename(File file) {
        return file.getName().split("\\.")[0];
    }

}
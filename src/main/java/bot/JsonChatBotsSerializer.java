package bot;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Представляет собой вспомогательный класс, который позволяет организованно
 * сериализовать данные для некоторого Telegram-бота в локально хранящийся Json файл,
 * а также десериализовать данные для всех Telegram-ботов во время их
 * совокупной инициализации (см. класс {@link bot.TelegramChatBots}).
 */
public class JsonChatBotsSerializer {

    private final static String extension = "json";
    private final static File serializeDirectory = new File("src\\main\\resources\\chats");
    private final static Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * Сериализует в Json файл указанного бота IAnecdoteBot, связанного с указанным id Telegram-чата.
     * @param chatId id Telegram-чата, с которым связан указанный бот IAnecdoteBot.
     * @param bot бот, который будет сериализован в Json файл.
     */
    public void serializeBot(Long chatId, IAnecdoteBot bot) {
        var filename = chatId.toString() + "." + extension;
        var file = new File(serializeDirectory, filename);
        var json = bot.serialize();
        try (PrintWriter writer = new PrintWriter(file, defaultEncoding)) {
            writer.write(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Десериализует все файлы с данными ботов из локального хранилища ресурсов
     * и возвращает объект {@link bot.TelegramChatBots} т.е. коллекцию полученных ботов.
     * @return Коллекция десериализованных ботов в виде объекта {@link bot.TelegramChatBots}.
     */
    public TelegramChatBots deserializeAll() {
        var bots = new TelegramChatBots();
        for (var file : serializeDirectory.listFiles()) {
            var chatId = Long.parseLong(getFilename(file));
            var bot = bots.initNewBot(chatId);
            try {
                var json = Files.readString(file.toPath(), defaultEncoding);
                bot.deserialize(json);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bots;
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
package bot;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой коллекцию, которая каждому
 * чату в Telegram по его id сопоставляет бота IAnecdoteBot.
 */
public class TelegramChatBots {

    /**
     * Поток выходных данных, куда боты IAnecdoteBot "сливают" свои сообщения.
     */
    private final PrintStream dump;
    private final Map<Long, IAnecdoteBot> bots;

    public TelegramChatBots() {
        var outByteArray = new ByteArrayOutputStream();
        dump = new PrintStream(outByteArray);
        bots = new HashMap<>();
    }

    /**
     * Возвращает бота IAnecdoteBot по указанному id чата, если такой бот уже существует,
     * иначе инициализирует и возвращает нового бота IAnecdoteBot, а также кэширует его в
     * хэш-мап по указанному id чата.
     * @param chatId id Telegram-чата.
     * @return Возвращает бота IAnecdoteBot.
     */
    public IAnecdoteBot getOrAdd(long chatId) {
        var bot = bots.get(chatId);
        if (bot == null) bot = initNewBot(chatId);
        return bot;
    }

    /**
     * Инициализирует и возвращает нового бота IAnecdoteBot,
     * а также кэширует его в хэш-мап по указанному id чата.
     * @param chatId id Telegram-чата, по которому будет создан новый бот.
     * @return Возвращает нового бота IAnecdoteBot.
     */
    public IAnecdoteBot initNewBot(long chatId) {
        var config = BotConfigRepository.getDefaultConfig();
        var newBot = new AnecdoteBot(config, dump);
        bots.put(chatId, newBot);
        return newBot;
    }

}
package bot;

import anecdote.IRatableAnecdoteRepository;
import anecdote.InternetAnecdoteRepository;

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
        IAnecdoteBot bot = bots.get(chatId);
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
        var repo = new InternetAnecdoteRepository(chatId);
        var newBot = new AnecdoteBot(chatId, config, repo, dump);
        bots.put(chatId, newBot);
        return newBot;
    }

    /**
     * Синхронизирует репозитории Telegram-пользователей со списком анекдотов,
     * общих для всех пользователей. Т.е. заставляет все пользовательские репозитории добавить
     * к себе все недостающие анекдоты из списка анекдотов, общих для всех пользователей.
     */
    public void syncCommonAnecdotesForAllRepos() {
        for (IAnecdoteBot bot : bots.values()) {
            IRatableAnecdoteRepository repository = bot.getAnecdoteRepository();
            if (repository instanceof InternetAnecdoteRepository internetAnecdoteRepository)
                internetAnecdoteRepository.pullCommonAnecdotes();
        }
    }

}
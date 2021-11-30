package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * посредством использования API TelegramBots.
 */
public class TelegramBotService extends TelegramLongPollingBot implements IBotService {

    private final static String botToken = "2111079902:AAFpPaiiQpZnT3K0Lm5hArMaiEmyWi2nxdI";
    private final static String botUsername = "@tamada_ru_bot";

    private final BotConfigRepository configs;
    private final Map<Long, IAnecdoteBot> bots;
    private final PrintStream dump;

    public TelegramBotService() {
        configs = new BotConfigRepository();
        bots = new HashMap<>();
        var outByteArray = new ByteArrayOutputStream();
        dump = new PrintStream(outByteArray);
    }

    /**
     * Запускает в отдельном потоке цикл взаимодействия
     * пользователя и бота (ввод -> парсинг -> вывод).
     */
    @Override
    public void start() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает имя пользователя (Username) Telegram-бота.
     * @return имя пользователя (Username) Telegram-бота.
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Возвращает токен Telegram-бота.
     * @return токен Telegram-бота.
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Метод, вызываемый каждый раз, когда Telegram-боту приходит обновление Update.
     * @param update обновление, которое будет обработано.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() == null)
            return;

        var chatId = update.getMessage().getChatId();
        var currentBot = bots.get(chatId);
        if (currentBot == null) currentBot = initNewBot(chatId);

        var input = update.getMessage().getText();
        if (!currentBot.isActive()) {
            activateBot(chatId, currentBot, input);
            return;
        }

        var text = currentBot.executeCommand(input);
        sendBotMessage(chatId, text);
    }

    /**
     * Инициализирует и возвращает нового бота IAnecdoteBot, а также кэширует его в хэш-мап.
     * @param chatId id Telegram-чата, по которому будет создан новый бот.
     * @return Возвращает нового бота IAnecdoteBot.
     */
    private IAnecdoteBot initNewBot(long chatId) {
        var config = configs.getDefaultConfig();
        var newBot = new AnecdoteBot(config, dump);
        bots.put(chatId, newBot);
        return newBot;
    }

    /**
     * Активирует указанного бота по указанному id Telegram-чата,
     * если таковая команда активации содержится в указанной строке ввода.
     * @param chatId id Telegram-чата, с которым связан бот.
     * @param bot бот, который будет активирован, если так указано в строке ввода.
     * @param input строка ввода, которая содержит или не содержит
     *              команду активации бота и все передаваемые аргументы.
     */
    private void activateBot(Long chatId, IAnecdoteBot bot, String input) {
        var literals = input.split("\\s+");
        if (literals.length <= 0 || !literals[0].equals("/start"))
            return;
        var config = configs.getDefaultConfig();
        if (literals.length >= 2)
            config = configs.getConfig(literals[1]);
        bot.setConfig(config);
        var text = bot.executeCommand("/start");
        sendBotMessage(chatId, text);
    }

    /**
     * Отправляет непустое сообщение в Telegram-чат по указанному id чата.
     * @param chatId id Telegram-чата, в который будет отправлено сообщение.
     * @param text текст сообщения, которое будет оправлено в Telegram-чат.
     */
    private void sendBotMessage(Long chatId, String text) {
        if (text == null || text.isBlank())
            return;
        try {
            execute(SendMessage.builder().chatId(chatId.toString()).text(text).build());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
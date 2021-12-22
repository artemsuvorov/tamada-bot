package bot;

import commands.CommandResult;
import commands.buttonMarkup.CommandButtonMarkups;
import commands.InputPredicate;
import commands.InputPredicateStorage;
import commands.buttonMarkup.IButtonMarkupFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Представляет собой класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * посредством использования API TelegramBots.
 */
public class TelegramBotService extends TelegramLongPollingBot implements IBotService {

    private final static String botUsername = "@tamada_ru_bot";
    private static String botToken = null;
    private final static Path tokenFilePath = Paths.get("src\\main\\resources\\token.txt");

    private final TelegramChatBots bots;
    private final JsonChatBotsSerializer serializer;
    private final CommandButtonMarkups markups;

    public TelegramBotService() {
        serializer = new JsonChatBotsSerializer();
        bots = serializer.deserializeAll();
        serializer.deserializeCommonAnecdotes();
        bots.syncCommonAnecdotesForAllRepos();
        markups = new CommandButtonMarkups();
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
        if (botToken != null)
            return botToken;

        try {
            return botToken = Files.readString(tokenFilePath, StandardCharsets.US_ASCII);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Метод, вызываемый каждый раз, когда Telegram-боту приходит обновление Update.
     * @param update обновление, которое будет обработано.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String input;
        Long chatId;

        if (update.hasMessage() && update.getMessage().getText() != null && !update.getMessage().getText().isBlank()) {
            input = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            input = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return;
        }

        IAnecdoteBot currentBot = bots.getOrAdd(chatId);

        InputPredicate startPredicate = InputPredicateStorage.StartCommandPredicate;
        if (!currentBot.getState().isActive() && !startPredicate.match(input))
            return;

        CommandResult result = currentBot.executeCommand(input);
        sendBotMessage(chatId, input, result);

        serializer.serializeBot(chatId, currentBot);
        serializer.serializeCommonAnecdotes();
        bots.syncCommonAnecdotesForAllRepos();
    }

    /**
     * Отправляет непустое сообщение в Telegram-чат по указанному id чата.
     * @param chatId id Telegram-чата, в который будет отправлено сообщение.
     * @param result результат команды CommandResult, выполненной ботом.
     */
    private void sendBotMessage(Long chatId, String input, CommandResult result) {
        if (result.Message == null || result.Message.isBlank())
            return;
        try {
            IButtonMarkupFactory markup = markups.getButtonMarkupOrNull(input);
            var message = SendMessage.builder().chatId(chatId.toString()).text(result.Message);
            if (markup != null)
                message = message.replyMarkup(markup.build(result));
            execute(message.build());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
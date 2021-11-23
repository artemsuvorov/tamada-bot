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

// todo: add javadocs
public class TelegramBotService extends TelegramLongPollingBot implements IBotService {

    private final BotConfigRepository configs;
    private final Map<Long, IAnecdoteBot> bots;
    private final PrintStream dump;

    public TelegramBotService() {
        configs = new BotConfigRepository();
        bots = new HashMap<>();
        var outByteArray = new ByteArrayOutputStream();
        dump = new PrintStream(outByteArray);
    }

    @Override
    public void start() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@tamada_ru_bot";
    }

    @Override
    public String getBotToken() {
        return "2111079902:AAFpPaiiQpZnT3K0Lm5hArMaiEmyWi2nxdI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var currentBot = bots.get(chatId);
        if (currentBot == null) currentBot = initNewBot(chatId);

        var input = update.getMessage().getText();
        if (!currentBot.isActive()) {
            executeCommand(chatId, currentBot, input);
            return;
        }
        var text = currentBot.executeCommand(input);
        sendMessage(chatId, currentBot, text);
    }

    private IAnecdoteBot initNewBot(long chatId) {
        var config = configs.getDefaultConfig();
        var newBot = new AnecdoteBot(config, dump);
        bots.put(chatId, newBot);
        return newBot;
    }

    private void executeCommand(Long chatId, IAnecdoteBot bot, String input) {
        var literals = input.split("\\s+");
        if (literals.length <= 0 || !literals[0].equals("/start"))
            return;
        if (literals.length >= 2) {
            var config = configs.getConfig(literals[1]);
            bot.setConfig(config);
        }
        var text = bot.executeCommand("/start");
        sendMessage(chatId, bot, text);
    }

    private void sendMessage(Long chatId, IAnecdoteBot bot, String text) {
        if (text == null) text = bot.executeCommand("");
        try {
            execute(SendMessage.builder().chatId(chatId.toString()).text(text).build());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
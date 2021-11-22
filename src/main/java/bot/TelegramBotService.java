package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TelegramBotService extends TelegramLongPollingBot implements IBotService {

    private static final File configFile = new File("src\\main\\resources\\tamada-config.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    private final BotConfiguration config;
    private final Map<Long, IAnecdoteBot> bots;
    private final PrintStream dump;

    public TelegramBotService() {
        config = BotConfiguration.deserializeBotConfig(configFile, defaultEncoding);
        bots = new HashMap();
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

        if (!currentBot.isActive()) return;
        var text = currentBot.executeCommand(update.getMessage().getText());
        try {
            execute(SendMessage.builder().chatId(chatId.toString()).text(text).build());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    private IAnecdoteBot initNewBot(long chatId) {
        var newBot = new AnecdoteBot(config, dump);
        bots.put(chatId, newBot);
        return newBot;
    }

}
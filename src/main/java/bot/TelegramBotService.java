package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.Randomizer;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TelegramBotService extends TelegramLongPollingBot implements IBotService {

    private static final File configFile = new File("src\\main\\resources\\tamada-config.json");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    //private final InputPredicateStorage predicates;
    //private final CommandStorage commands;

    protected final BotConfiguration Config;
    protected final IAnecdoteBot Bot;

    public TelegramBotService() {
        Config = BotConfiguration.deserializeBotConfig(configFile, defaultEncoding);
        Bot = new AnecdoteBot(Config.BotName, Config.Anecdotes);
        //predicates = new InputPredicateStorage();
        //commands = new CommandStorage(Bot, Config, Out);
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
        var chatId = update.getMessage().getChatId().toString();
        var text = Randomizer.getRandomElement(Config.NotUnderstandMessages);
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}
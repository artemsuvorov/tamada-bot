import Bot.Bot;
import Bot.BotConfiguration;
import Bot.BotRoutine;
import Bot.TamadaBot;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
public class TamadaBotTests {

    private static final String _configFilePath = new File("src\\main\\resources\\tamada-config.json").getAbsolutePath();
    private static final Charset _encoding = StandardCharsets.UTF_8;

    private BotConfiguration _botConfig;
    private Bot _bot;
    private OutputStream _byteArrayOut;
    private PrintStream _out;
    private InputStream _in;
    private BotRoutine _routine;

    @Test
    public void testTwoAnecdotesWereAddedToFavorites() {
        var input = """
                расскажи анекдот
                оценить
                нравится
                расскажи анекдот
                нравится
                стоп
                """;
        executeBotRoutineWith(input);
        var actualLength = _bot.getAnecdoteRepository().getFavorites().size();
        assertEqualsOnBot(2, actualLength);
    }

    @Test
    public void testAnecdoteWasDeletedFromRepository() {
        var input = """
                расскажи анекдот
                оценить
                НЕ НРАВИТСЯ
                стоп
                """;
        executeBotRoutineWith(input);
        var expected = _botConfig.Anecdotes.length-1;
        var actualLength = _bot.getAnecdoteRepository().getCount();
        assertEqualsOnBot(expected, actualLength);
    }

    private void executeBotRoutineWith(String input)  {
        try {
            _bot = createTamadaBotFromTamadaConfig();
            _byteArrayOut = new ByteArrayOutputStream();
            _out = new PrintStream(_byteArrayOut);
            _in = new ByteArrayInputStream(input.getBytes());

            _routine = new BotRoutine(_bot, _out, _in);
            _routine.start();

            _in.close();
            _out.close();
        }
        catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    private <T> void assertEqualsOnBot(T expected, T actual) {
        var botOutput = "Bot's output:\r\n" + _byteArrayOut.toString();
        Assertions.assertEquals(expected, actual, botOutput);
    }

    private Bot createTamadaBotFromTamadaConfig() {
        _botConfig = deserializeBotConfig();
        return new TamadaBot(_botConfig);
    }

    private BotConfiguration deserializeBotConfig() {
        var json = readBotConfigFile();
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    private String readBotConfigFile() {
        var path = Path.of(_configFilePath);
        try {
            return Files.readString(path, _encoding);
        } catch (Exception ex) {
            Assertions.fail(ex);
            return null;
        }
    }

}
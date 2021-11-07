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

    private static final String configFilePath = new File("src\\main\\resources\\tamada-config.json").getAbsolutePath();
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    private BotConfiguration botConfig;
    private Bot bot;
    private OutputStream byteArrayOut;
    private PrintStream out;
    private InputStream in;
    private BotRoutine routine;

    @Test
    public void testTwoAnecdotesWereAddedToFavorites() {
        var input = """
                расскажи анекдот
                оценить 5
                расскажи анекдот
                оценить 5
                стоп
                """;
        executeBotRoutineWith(input);
        var actualLength = bot.getAnecdoteRepository().getFavorites().size();
        assertEqualsOnBot(2, actualLength);
    }

    @Test
    public void testAnecdoteWasDeletedFromRepository() {
        var input = """
                расскажи анекдот
                оценить 1
                стоп
                """;
        executeBotRoutineWith(input);
        var expected = botConfig.Anecdotes.length-1;
        var actualLength = bot.getAnecdoteRepository().getCount();
        assertEqualsOnBot(expected, actualLength);
    }

    private void executeBotRoutineWith(String input)  {
        try {
            bot = createTamadaBotFromTamadaConfig();
            byteArrayOut = new ByteArrayOutputStream();
            out = new PrintStream(byteArrayOut);
            in = new ByteArrayInputStream(input.getBytes());

            routine = new BotRoutine(bot, out, in);
            routine.start();

            in.close();
            out.close();
        }
        catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    private <T> void assertEqualsOnBot(T expected, T actual) {
        var botOutput = "Bot's output:\r\n" + byteArrayOut.toString();
        Assertions.assertEquals(expected, actual, botOutput);
    }

    private Bot createTamadaBotFromTamadaConfig() {
        botConfig = deserializeBotConfig();
        return new TamadaBot(botConfig);
    }

    private BotConfiguration deserializeBotConfig() {
        var json = readBotConfigFile();
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    private String readBotConfigFile() {
        var path = Path.of(configFilePath);
        try {
            return Files.readString(path, defaultEncoding);
        } catch (Exception ex) {
            Assertions.fail(ex);
            return null;
        }
    }

}
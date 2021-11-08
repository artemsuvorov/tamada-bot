import Bot.BotConfiguration;
import Bot.ConsoleTamadaBotService;
import Bot.IAnecdoteBot;
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

    private OutputStream byteArrayOut;
    private PrintStream out;
    private InputStream in;
    private BotConfiguration config;
    private IAnecdoteBot bot;
    private ConsoleTamadaBotService service;

    @Test
    public void testTwoAnecdotesWereAddedToFavorites() {
        var input = """
                расскажи анекдот
                оценить 5
                расскажи анекдот
                оценить 5
                стоп
                """;
        executeUserCommands(input);
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
        executeUserCommands(input);
        var expected = config.Anecdotes.length-1;
        var actualLength = bot.getAnecdoteRepository().getCount();
        assertEqualsOnBot(expected, actualLength);
    }

    private void executeUserCommands(String input)  {
        try {
            byteArrayOut = new ByteArrayOutputStream();
            out = new PrintStream(byteArrayOut);
            in = new ByteArrayInputStream(input.getBytes());

            service = new ConsoleTamadaBotService(out, in);
            config = service.getConfig();
            bot = service.getBot();
            service.start();

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

}
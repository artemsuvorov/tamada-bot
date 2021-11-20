import bot.BotConfiguration;
import bot.IAnecdoteBot;

import org.junit.jupiter.api.*;

import java.io.*;

@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
public class TamadaBotTests {

    private OutputStream byteArrayOut;
    private PrintStream out;
    private InputStream in;
    private BotConfiguration config;
    private IAnecdoteBot bot;

    private CustomBotService service;

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
        var actualLength = bot.getAnecdoteRepository().getFavorites().length;
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

            service = new CustomBotService(out, in);
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
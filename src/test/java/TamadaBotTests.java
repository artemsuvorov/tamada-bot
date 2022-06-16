import anecdote.IRatableAnecdoteRepository;
import anecdote.RandomRatableAnecdoteRepository;
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
    private IRatableAnecdoteRepository repository;

    private CustomBotService service;

    @Test
    public void testTwoAnecdotesWereAddedToFavorites() {
        String[] anecdotes = { "___ANECDOTE1___", "___ANECDOTE2___" };
        String input = """
                расскажи анекдот
                оценить 5
                расскажи анекдот
                оценить 5
                стоп
                """;
        initBotService(input, anecdotes);
        startExecution();
        int actualLength = bot.getAnecdoteRepository().getFavorites().length;
        assertEqualsOnBot(2, actualLength);
    }

    @Test
    public void testAnecdoteWasDeletedFromRepository() {
        String[] anecdotes = { "___ANECDOTE1___", "___ANECDOTE2___" };
        String input = """
                расскажи анекдот
                оценить 1
                стоп
                """;
        initBotService(input, anecdotes);
        startExecution();
        int actualLength = bot.getAnecdoteRepository().getCount();
        assertEqualsOnBot(anecdotes.length - 1, actualLength);
    }

    // todo: проверить репозиторий на добавление анекдота в соответствующие оценки списки
    // т.е. если в input находится строчка "оценить 3", то анекдот должен попасть в список анекдотов с оценкой 3
    // todo: проверить репозиторий, что при переоценке анекдота, он попадает в соответствующий список И
    // todo: И пропадает из списка, в котором находился прежде
    // т.е. если в input находится строчки
    //      "оценить 3
    //       оценить 4",
    // то анекдот должен попасть в список анекдотов с оценкой 3,
    // а потом в список с оценкой 4 и пропасть из списка с оценкой 3

    private void initBotService(String input, String[] anecdotes) {
        try {
            byteArrayOut = new ByteArrayOutputStream();
            out = new PrintStream(byteArrayOut);
            in = new ByteArrayInputStream(input.getBytes());
            service = new CustomBotService(out, in);
            config = service.getConfig();
            bot = service.getBot();
            repository = new RandomRatableAnecdoteRepository(anecdotes);
            bot.setAnecdoteRepository(repository);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    private void startExecution()  {
        try {
            service.start();
            in.close();
            out.close();
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    private <T extends Comparable<T>> void assertEqualsOnBot(T expected, T actual) {
        String botOutput = "Bot's output:\r\n" + byteArrayOut.toString();
        Assertions.assertEquals(expected, actual, botOutput);
    }

}
package Commands;

import Anecdote.Rating;
import Bot.BotMessage;
import Bot.IAnecdoteBot;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Представляет собой класс, который на основе от заданного входного
 * сообщения пользователя может создать соответствующую команду для бота.
 */
public final class CommandParser {

    private final IAnecdoteBot bot;
    private final PrintStream out;

    public CommandParser(IAnecdoteBot bot, PrintStream out) {
        this.bot = bot;
        this.out = out;
    }

    /**
     * Конвертирует строку инпута в соответствующую команду для бота.
     * @param input строка инпута, которая будет сконвертирована
     *              в соответствующую команду для бота.
     * @return Команда боту.
     */
    public BotCommand parse(String input) {
        var ninput = input.trim().toLowerCase();

        if (inputContainsAll(ninput, "что", "умеешь"))
            return newMessageCommand(IAnecdoteBot::onWhatCanYouDo);

        if (inputContainsAll(ninput, "кто", "ты") || ninput.contains("представься"))
            return newMessageCommand(IAnecdoteBot::introduce);

        if (inputContainsAny(ninput, "привет", "здравст", "здраст", "салют", "доброго времени суток", "хай"))
            return newMessageCommand(IAnecdoteBot::greet);

        if (inputContainsAll(ninput, "как", "дела") || inputContainsAll(ninput, "как", "ты"))
            return newMessageCommand(IAnecdoteBot::onHowAreYou);

        if (inputContainsAll(ninput, "скажи", "анекдот"))
            return newMessageCommand(IAnecdoteBot::tellAnecdote);

        if (inputContainsAny(ninput, "ха", "смешно") && !ninput.contains("не"))
            return newMessageCommand(IAnecdoteBot::onUserLaughed);

        if (inputContainsAny(ninput, "избранное"))
            return newMessageCommand(IAnecdoteBot::showFavorites);

        if (inputContainsAny(ninput, "покажи", "показать"))
            return newShowAnecdotesCommand(ninput);

        if (inputContainsAny(ninput, "оцен"))
            return newRatingCommand(ninput);

        if (inputContainsAny(ninput, "нрав") && !ninput.contains("не"))
            return newMessageCommand(bot -> bot.onRatingSubmitted(Rating.Excellent));

        if (inputContainsAll(ninput, "не", "нрав"))
            return newMessageCommand(bot -> bot.onRatingSubmitted(Rating.Dislike));

        if (inputContainsAny(ninput, "отмен"))
            return newMessageCommand(IAnecdoteBot::onCancelRating);

        if (inputContainsAny(ninput, "хватит", "стоп", "пока", "до свидания"))
            return newMessageCommand(IAnecdoteBot::stop);

        return newMessageCommand(IAnecdoteBot::notUnderstand);
    }

    /**
     * Парсит оценку из введенного инпута, а затем создает и возвращает
     * соответствующую команду боту, а именно:
     * выставить анекдоту распаршенную оценку пользователя.
     * @param input Входной инпут пользователя.
     * @return Команда боту выставить анекдоту распаршенную оценку пользователя.
     */
    private BotCommand newRatingCommand(String input) {
        var scanner = new Scanner(input).useDelimiter("\\D+");
        if (!scanner.hasNextInt())
            return newMessageCommand(IAnecdoteBot::onRateNoRatingProvided);
        var number = scanner.nextInt();
        if (number < 1 || number > 5)
            return  newMessageCommand(IAnecdoteBot::onRateInvalidRatingProvided);
        var rating = Rating.fromInteger(number);
        return newMessageCommand(bot -> bot.onRatingSubmitted(rating));
    }

    /**
     * Парсит оценку из введенного инпута, а затем создает и возвращает
     * соответствующую команду боту, а именно: показать анекдоты с
     * распаршенной оценкой пользователя.
     * @param input Входной инпут пользователя.
     * @return Команда боту показать анекдоты с распаршенной оценкой пользователя.
     */
    private BotCommand newShowAnecdotesCommand(String input) {
        var scanner = new Scanner(input).useDelimiter("\\D+");
        if (!scanner.hasNextInt())
            return newMessageCommand(IAnecdoteBot::onShowNoRatingProvided);
        var number = scanner.nextInt();
        if (number < 1 || number > 5)
            return  newMessageCommand(IAnecdoteBot::onShowInvalidRatingProvided);
        var rating = Rating.fromInteger(number);
        return newMessageCommand(bot -> bot.showAnecdotesOfRating(rating));
    }

    /**
     * Возвращает новую команду, которая печатает сообщение, передав
     * в ее конструктор бота bot и поток вывода out.
     * @param action действие, которое будет выполнено ботом, и результат будет напечатан в out.
     * @return новую команду, которая печатает сообщение, передав
     * в ее конструктор бота bot и поток вывода out.
     */
    private BotCommand newMessageCommand(Function<IAnecdoteBot, BotMessage> action) {
        return new MessageCommand(bot, out, action);
    }

    /**
     * Указывает, содержит ли указанная строка любую из перечисленных подстрок.
     * @param input строка, в которой будет произведен поиск перечисленных подстрок.
     * @param values подстроки, которые будут искаться в указанной строке.
     * @return true, если строка содержит любую из перечисленных подстрок, иначе false.
     */
    private boolean inputContainsAny(String input, String... values) {
        var result = false;
        for (var value : values) {
            result = input.contains(value);
            if (result) break;
        }
        return result;
    }

    /**
     * Указывает, содержит ли указанная строка каждую из перечисленных подстрок.
     * @param input строка, в которой будет произведен поиск перечисленных подстрок.
     * @param values подстроки, которые будут искаться в указанной строке.
     * @return true, если строка содержит каждую из перечисленных подстрок, иначе false.
     */
    private boolean inputContainsAll(String input, String... values) {
        var result = true;
        for (var value : values) {
            result = input.contains(value);
            if (!result) break;
        }
        return result;
    }

}
package Commands;

import Anecdote.Rating;
import Bot.Bot;

import java.util.Scanner;

/**
 * Представляет собой класс, который на основе от заданного входного
 * сообщения пользователя может создать соответствующую команду для бота.
 */
public final class CommandParser {

    private final Bot bot;

    public CommandParser(Bot bot) {
        this.bot = bot;
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
            return new MessageCommand(bot, bot -> bot.onWhatCanYouDo());

        if (inputContainsAll(ninput, "кто", "ты") || ninput.contains("представься"))
            return new MessageCommand(bot, bot -> bot.introduce());

        if (inputContainsAny(ninput, "привет", "здравствуй", "здрасте", "салют", "доброго времени суток", "хай"))
            return new MessageCommand(bot, bot -> bot.greet());

        if (inputContainsAll(ninput, "как", "дела") || inputContainsAll(ninput, "как", "ты"))
            return new MessageCommand(bot, bot -> bot.onHowAreYou());

        if (inputContainsAll(ninput, "скажи", "анекдот"))
            return new MessageCommand(bot, bot -> bot.tellAnecdote());

        if (inputContainsAny(ninput, "ха", "смешно") && !ninput.contains("не"))
            return new MessageCommand(bot, bot -> bot.onUserLaughed());

        if (inputContainsAny(ninput, "избранное"))
            return new MessageCommand(bot, bot -> bot.showFavorites());

        if (inputContainsAny(ninput, "покажи", "показать"))
            return newShowAnecdotesCommand(ninput);

        if (inputContainsAny(ninput, "оцен"))
            return newRatingCommand(ninput);

        if (inputContainsAny(ninput, "нрав") && !ninput.contains("не"))
            return new MessageCommand(bot, bot -> bot.onRatingSubmitted(Rating.Excellent));

        if (inputContainsAll(ninput, "не", "нрав"))
            return new MessageCommand(bot, bot -> bot.onRatingSubmitted(Rating.Dislike));

        if (inputContainsAny(ninput, "отмен"))
            return new MessageCommand(bot, bot -> bot.onCancelRating());

        if (inputContainsAny(ninput, "хватит", "стоп", "пока", "до свидания"))
            return new MessageCommand(bot, bot -> bot.stopChatting());

        return new MessageCommand(bot, bot -> bot.notUnderstand());
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
            return new MessageCommand(bot, bot -> bot.onRateNoRatingProvided());
        var number = scanner.nextInt();
        if (number < 1 || number > 5)
            return  new MessageCommand(bot, bot -> bot.onRateInvalidRatingProvided());
        var rating = Rating.fromInteger(number);
        return new MessageCommand(bot, bot -> bot.onRatingSubmitted(rating));
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
            return new MessageCommand(bot, bot -> bot.onShowNoRatingProvided());
        var number = scanner.nextInt();
        if (number < 1 || number > 5)
            return  new MessageCommand(bot, bot -> bot.onShowInvalidRatingProvided());
        var rating = Rating.fromInteger(number);
        return new MessageCommand(bot, bot -> bot.showAnecdotesOfRating(rating));
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
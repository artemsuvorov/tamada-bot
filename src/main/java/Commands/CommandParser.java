package Commands;

import Bot.Bot;

public final class CommandParser {

    private final Bot _bot;

    public CommandParser(Bot bot) {
        _bot = bot;
    }

    public BotCommand parse(String input) {
        var ninput = input.trim().toLowerCase();

        if (inputContainsAll(ninput, "что", "умеешь"))
            return new MessageCommand(_bot, bot -> bot.onWhatCanYouDo());

        if (inputContainsAll(ninput, "кто", "ты") || ninput.contains("представься"))
            return new MessageCommand(_bot, bot -> bot.introduce());

        if (inputContainsAny(ninput, "привет", "здравствуй", "здрасте", "салют", "доброго времени суток", "хай"))
            return new MessageCommand(_bot, bot -> bot.greet());

        if (inputContainsAll(ninput, "как", "дела") || inputContainsAll(ninput, "как", "ты"))
            return new MessageCommand(_bot, bot -> bot.onHowAreYou());

        if (inputContainsAll(ninput, "скажи", "анекдот"))
            return new MessageCommand(_bot, bot -> bot.tellAnecdote());

        if (inputContainsAny(ninput, "ха", "смешно") && !ninput.contains("не"))
            return new MessageCommand(_bot, bot -> bot.onUserLaughed());

        if (inputContainsAny(ninput,"оцен"))
            return new MessageCommand(_bot, bot -> bot.inviteToRate());

        if (inputContainsAny(ninput, "нрав") && !ninput.contains("не"))
            return new MessageCommand(_bot, bot -> bot.onLikeRating());

        if (inputContainsAll(ninput, "не", "нрав"))
            return new MessageCommand(_bot, bot -> bot.onDislikeRating());

        if (inputContainsAny(ninput, "отмен"))
            return new MessageCommand(_bot, bot -> bot.onCancelRating());

        if (inputContainsAll(ninput, "избранн"))
            return new MessageCommand(_bot, bot -> bot.showFavorites());

        if (inputContainsAny(ninput, "хватит", "стоп", "пока", "до свидания"))
            return new MessageCommand(_bot, bot -> bot.stopChatting());

        return new MessageCommand(_bot, bot -> bot.notUnderstand());
    }

    private boolean inputContainsAny(String input, String... values) {
        var result = false;
        for (var value : values) {
            result = input.contains(value);
            if (result) break;
        }
        return result;
    }

    private boolean inputContainsAll(String input, String... values) {
        var result = true;
        for (var value : values) {
            result = input.contains(value);
            if (!result) break;
        }
        return result;
    }

}
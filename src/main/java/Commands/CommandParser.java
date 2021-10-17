package Commands;

import Bot.Bot;

public final class CommandParser {

    private final Bot _bot;

    public CommandParser(Bot bot) {
        _bot = bot;
    }

    public BotCommand parse(String input) {

        var ninput = input.trim().toLowerCase();

        if (inputContainsAll(ninput, "что", "ты", "умеешь"))
            return new HelpCommand(_bot);

        if (inputContainsAll(ninput, "кто", "ты") || ninput.contains("представься"))
            return new IntroduceCommand(_bot);

        if (inputContainsAny(ninput, "привет", "здравствуй", "здрасте", "салют", "доброго времени суток", "хай"))
            return new HelloCommand(_bot);

        if (inputContainsAll(ninput, "как", "дела") || inputContainsAll(ninput, "как", "ты"))
            return new HowAreYouCommand(_bot);

        if (inputContainsAll(ninput, "скажи", "анекдот"))
            return new TellAnecdoteCommand(_bot);

        if (inputContainsAny(ninput, "хватит", "стоп"))
            return new StopChattingCommand(_bot);

        if (inputContainsAny(ninput, "ха", "смешно") && !ninput.contains("несмешно"))
            return new UserLaughedCommand(_bot);

        return new NotUnderstandCommand(_bot);
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
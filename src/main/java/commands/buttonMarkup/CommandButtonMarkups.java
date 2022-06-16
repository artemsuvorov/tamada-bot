package commands.buttonMarkup;

import commands.InputPredicateStorage;

import java.util.HashMap;
import java.util.Map;

public class CommandButtonMarkups {

    private final Map<String, IButtonMarkupFactory> markupsByCommands;

    public CommandButtonMarkups() {
        this.markupsByCommands = new HashMap<>();
        fillPredicatesByCommandNames();
    }

    public IButtonMarkupFactory getButtonMarkupOrNull(String input) {
        if (input == null) return null;
        String commandName = InputPredicateStorage.getCommandNameOrNull(input);
        return markupsByCommands.get(commandName);
    }

    /**
     * Заполняет hash map названиями команд бота, каждой из которых
     * ставится в соответствие предикат от введенного сообщения пользователя.
     */
    private void fillPredicatesByCommandNames() {
        markupsByCommands.put("startConversation", new ButtonMarkupFactory()
                .addButtonRow("Что ты умеешь?", "Кто ты такой?").addButtonRow("Расскажи анекдот!"));
        markupsByCommands.put("onWhatCanYouDo", new ButtonMarkupFactory()
                .addButtonRow("Кто ты такой?", "Как дела?").addButtonRow("Расскажи анекдот!"));
        markupsByCommands.put("introduce", new ButtonMarkupFactory()
                .addButtonRow("Что ты умеешь?", "Как дела?").addButtonRow("Расскажи анекдот!"));
        markupsByCommands.put("getNextAnecdote", getOnTellAnecdoteButtonMarkup());
        markupsByCommands.put("onUserLaughed", getDefaultButtonMarkup());
        markupsByCommands.put("onRatingSubmitted_Like", new ButtonMarkupFactory()
                .addButtonRow("Покажи избранное").addButtonRow("Расскажи анекдот!"));
        markupsByCommands.put("onRatingSubmitted_Dislike", getDefaultButtonMarkup());
        markupsByCommands.put("onRatingSubmitted", getOnRatingSubmittedButtonMarkup());
        markupsByCommands.put("onEndingSuggested", getOnTellAnecdoteButtonMarkup());
        markupsByCommands.put("showFavorites", getDefaultButtonMarkup());
        markupsByCommands.put("showAnecdotesOfRating", getDefaultButtonMarkup());

        /*predicatesByCommandName.put("startConversation", StartCommandPredicate);
        predicatesByCommandName.put("onWhatCanYouDo", new InputPredicate().all("что", "умеешь"));
        predicatesByCommandName.put("introduce", new InputPredicate().all("кто", "ты").or().has("представься"));
        predicatesByCommandName.put("greet", new InputPredicate().any("привет", "здравст", "здраст", "салют", "доброго времени суток", "хай"));
        predicatesByCommandName.put("onHowAreYou", new InputPredicate().all("как", "дела").or().all("как", "ты"));
        predicatesByCommandName.put("getNextAnecdote", new InputPredicate().all("расскажи", "анекдот"));
        predicatesByCommandName.put("onUserLaughed", new InputPredicate().any("ха", "смешно").and().not().has("не"));
        predicatesByCommandName.put("showFavorites", new InputPredicate().has("избранное"));
        predicatesByCommandName.put("onRatingSubmitted_Like", new InputPredicate().has("нрав").and().not().has("не"));
        predicatesByCommandName.put("onRatingSubmitted_Dislike", new InputPredicate().all("не", "нрав"));
        predicatesByCommandName.put("onRatingSubmitted", new InputPredicate().has("оцен"));
        predicatesByCommandName.put("onEndingSuggested", new InputPredicate().any("предложить", "предлагаю", "концовка"));
        predicatesByCommandName.put("showAnecdotesOfRating", new InputPredicate().any("покажи", "показать"));
        predicatesByCommandName.put("deactivate", new InputPredicate().any("хватит", "стоп", "до свидания"));*/
    }

    // todo: make it singleton
    private IButtonMarkupFactory getDefaultButtonMarkup() {
        return new ButtonMarkupFactory().addButtonRow("Расскажи еще анекдот!");
    }

    private IButtonMarkupFactory getOnRatingSubmittedButtonMarkup() {
        ButtonMarkupFactory showOfRatingMarkup = new ButtonMarkupFactory()
                .addButtonRow(
                        new InlineButton("1️⃣", "Покажи 1"),
                        new InlineButton("2️⃣", "Покажи 2"),
                        new InlineButton("3️⃣", "Покажи 3"),
                        new InlineButton("4️⃣", "Покажи 4"),
                        new InlineButton("5️⃣", "Покажи 5")
                )
                .addButtonRow("Расскажи еще анекдот!");
        return showOfRatingMarkup;
    }

    private IButtonMarkupFactory getOnTellAnecdoteButtonMarkup() {
        ButtonMarkupFactory suggestRatingMarkup = new ButtonMarkupFactory()
                .addButtonRow(
                        new InlineButton("1️⃣", "Оценить 1"),
                        new InlineButton("2️⃣", "Оценить 2"),
                        new InlineButton("3️⃣", "Оценить 3"),
                        new InlineButton("4️⃣", "Оценить 4"),
                        new InlineButton("5️⃣", "Оценить 5")
                );
        ButtonMarkupFactory suggestEndingMarkup = new ButtonMarkupFactory()
                .addButtonRow("Предложить");
        return new DynamicButtonMarkupFactory()
                .addButtonRow("Расскажи еще анекдот!")
                .addButtonsOnCondition(result -> result.BotState.wasAnecdoteTold(), suggestRatingMarkup)
                .addButtonsOnCondition(result -> result.BotState.wasUnfinishedAnecdoteTold(), suggestEndingMarkup);
    }

}

package commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandButtonMarkups {

    private final Map<String, InlineKeyboardMarkup> markupsByCommands;

    public CommandButtonMarkups() {
        this.markupsByCommands = new HashMap<>();
        fillPredicatesByCommandNames();
    }

    public InlineKeyboardMarkup getButtonMarkupOrNull(String input) {
        var commandName = InputPredicateStorage.getCommandNameOrNull(input);
        return markupsByCommands.get(commandName);
    }

    /**
     * Заполняет hash map названиями команд бота, каждой из которых
     * ставится в соответствие предикат от введенного сообщения пользователя.
     */
    private void fillPredicatesByCommandNames() {
        markupsByCommands.put("startConversation", newButtons("Что ты умеешь?", "Кто ты такой?", "Расскажи анекдот!"));
        markupsByCommands.put("onWhatCanYouDo", newButtons("Как дела?", "Расскажи анекдот!"));
        markupsByCommands.put("introduce", newButtons("Что ты умеешь?", "Расскажи анекдот!"));
        markupsByCommands.put("getNextAnecdote", newButtons("Расскажи еще анекдот!"));
        markupsByCommands.put("showAnecdotesOfRating", newButtons("Расскажи еще анекдот!"));

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

    private InlineKeyboardMarkup newButtons(String... buttonNames) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var buttonName : buttonNames)
            buttons.add(List.of(InlineKeyboardButton.builder().text(buttonName).callbackData(buttonName).build()));
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

}

package Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandStorage {

    private final Map<String, InputPredicate> inputPredicatesByCommandName;

    public CommandStorage() {
        this.inputPredicatesByCommandName = new HashMap<>();
        fillPredicatesByCommandNames();
    }

    private void fillPredicatesByCommandNames() {
        inputPredicatesByCommandName.put("onWhatCanYouDo", new InputPredicate().all("что", "умеешь"));
        inputPredicatesByCommandName.put("introduce", new InputPredicate().any("кто", "ты").or().has("представься"));
        inputPredicatesByCommandName.put("greet", new InputPredicate().any("привет", "здравст", "здраст", "салют", "доброго времени суток", "хай"));
        inputPredicatesByCommandName.put("onHowAreYou", new InputPredicate().all("как", "дела").or().all("как", "ты"));
        inputPredicatesByCommandName.put("tellAnecdote", new InputPredicate().all("расскажи", "анекдот"));
        inputPredicatesByCommandName.put("onUserLaughed", new InputPredicate().any("ха", "смешно").and().not().has("не"));
        inputPredicatesByCommandName.put("showFavorites", new InputPredicate().has("избранное"));
        inputPredicatesByCommandName.put("showAnecdotesOfRating", new InputPredicate().any("покажи", "показать"));
        inputPredicatesByCommandName.put("onRatingSubmitted", new InputPredicate().has("оцен"));
        inputPredicatesByCommandName.put("onCancelRating", new InputPredicate().has("отмен"));
        inputPredicatesByCommandName.put("stop", new InputPredicate().any("хватит", "стоп", "пока", "до свидания"));
    }

}
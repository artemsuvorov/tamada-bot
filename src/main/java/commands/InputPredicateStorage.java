package commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет собой хранилище, в котором каждому названию команды бота
 * поставлен в соответствие предикат от введенного сообщения пользователя.
 */
public class InputPredicateStorage {

    private final Map<String, InputPredicate> predicatesByCommandName;

    public InputPredicateStorage() {
        this.predicatesByCommandName = new HashMap<>();
        fillPredicatesByCommandNames();
    }

    /**
     * Ищет в хранилище предикат, которому удовлетворяет указанная строка,
     * и возвращает название команды бота, которой поставлен в соответствие
     * найденный предикат.
     * @param input ввод пользователя, который будет проверен на соответствие
     *              какому-либо предикату из этого хранилища.
     * @return Название команды бота, которой поставлен в соответствие найденный
     * предикат. Если такой предикат не был найден, возвращает null.
     */
    public String getCommandNameOrNull(String input) {
        var lowerInput = input.toLowerCase().trim();
        String commandName = null;
        for (var entry : predicatesByCommandName.entrySet()) {
            var predicate = entry.getValue();
            if (!predicate.match(lowerInput)) continue;
            commandName = entry.getKey();
            break;
        }
        return commandName;
    }

    /**
     * Заполняет hash map названиями команд бота, каждой из которых
     * ставится в соответствие предикат от введенного сообщения пользователя.
     */
    private void fillPredicatesByCommandNames() {
        predicatesByCommandName.put("startConversation", new InputPredicate().any("/start", "старт", "начать"));
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
        predicatesByCommandName.put("showAnecdotesOfRating", new InputPredicate().any("покажи", "показать"));
        predicatesByCommandName.put("deactivate", new InputPredicate().any("хватит", "стоп", "до свидания"));
    }

}
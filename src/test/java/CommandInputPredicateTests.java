import commands.InputPredicate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class CommandInputPredicateTests {

    // it is irrelevant what string you provide here
    private final String anyInput = "%some_input_garbage_is_ahead_234ufdakdf1las;'.1%";

    @Test
    public void testEmptyPredicateReturnsTrue() {
        Assertions.assertTrue(new InputPredicate().match(anyInput));
    }

    @Test
    public void testInvalidPredicatesThrowIllegalArgExc() {
        Executable action = () -> new InputPredicate().all().match(anyInput);
        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @Test
    public void testCaseSensitivePredicates() {
        var input = "ABCD";
        Assertions.assertTrue(new InputPredicate().caseSensitive().has("C").match(input));
        Assertions.assertTrue(new InputPredicate().caseSensitive().has("D").match(input));
        Assertions.assertFalse(new InputPredicate().caseSensitive().has("a").match(input));
        Assertions.assertFalse(new InputPredicate().caseSensitive().has("b").match(input));
    }

    @Test
    public void testHasMethodIsCorrect() {
        var input = "ABCD";
        Assertions.assertTrue(new InputPredicate().has("A").match(input));
        Assertions.assertTrue(new InputPredicate().has("B").match(input));
        Assertions.assertTrue(new InputPredicate().has("a").match(input));
        Assertions.assertTrue(new InputPredicate().has("b").match(input));
        Assertions.assertTrue(new InputPredicate().not().has("E").match(input));
        Assertions.assertTrue(new InputPredicate().not().has("X").match(input));
    }

    @Test
    public void testNegateMethodIsCorrect() {
        String input = "AB_DE...";
        Assertions.assertTrue(new InputPredicate().not().has("C").match(input));
    }

    @Test
    public void testPredicateForHelloInputs() {
        String input1 = "привет", input2 = "здравствуйте", input3 = "добрый день";
        var predicate = new InputPredicate()
                .any("привет", "здравст", "здраст", "салют", "доброго времени суток", "хай");
        Assertions.assertTrue(predicate.match(input1));
        Assertions.assertTrue(predicate.match(input2));
        Assertions.assertFalse(predicate.match(input3));
    }

    @Test
    public void testPredicateForHowAreYouInput() {
        var predicate = new InputPredicate().all("как", "дела").or().all("как", "ты");
        Assertions.assertTrue(predicate.match("как ты?"));
        Assertions.assertTrue(predicate.match("как дела?"));
        Assertions.assertTrue(predicate.match("как у тебя дела?"));
    }

    @Test
    public void testPredicatesForTellAnecdoteInput() {
        var input = "расскажи анекдот";
        Assertions.assertTrue(new InputPredicate().all("расскажи", "анекдот").match(input));
        Assertions.assertTrue(new InputPredicate().has("расскажи").and().has("анекдот").match(input));
        Assertions.assertTrue(new InputPredicate().any("расскажи").and().any("анекдот").match(input));
    }

    @Test
    public void testPredicateForLaughInput() {
        var predicate = new InputPredicate().any("ха", "смешно").and().not().has("не");
        Assertions.assertTrue(predicate.match("хахахах"));
        Assertions.assertTrue(predicate.match("ха ха очень смешно!"));
        Assertions.assertFalse(predicate.match("......."));
        Assertions.assertFalse(predicate.match("совсем не смешно"));
    }

    @Test
    public void testPredicateForShowAnecdotesInputs() {
        String input1 = "покажи", input2 = "показать";
        var predicate = new InputPredicate().any("покажи", "показать");
        var equivalentPredicate1 = new InputPredicate().has("покажи").or().has("показать");
        var equivalentPredicate2 = new InputPredicate().any("покажи").or().any("показать");

        Assertions.assertTrue(predicate.match(input1));
        Assertions.assertTrue(predicate.match(input1));
        Assertions.assertTrue(equivalentPredicate1.match(input1));
        Assertions.assertTrue(equivalentPredicate1.match(input2));
        Assertions.assertTrue(equivalentPredicate2.match(input1));
        Assertions.assertTrue(equivalentPredicate2.match(input2));
    }

    @Test
    public void testPredicateForLikeRatingInputs() {
        var predicate = new InputPredicate().has("нрав").and().not().has("не");
        Assertions.assertTrue(predicate.match("нравится"));
        Assertions.assertTrue(predicate.match("нрав"));
        Assertions.assertFalse(predicate.match("не нрав"));
        Assertions.assertFalse(predicate.match("не нравится"));
    }

    @Test
    public void testPredicateForDislikeRatingInputs() {
        var predicate = new InputPredicate().all("не", "нрав");
        Assertions.assertTrue(predicate.match("не нравится"));
        Assertions.assertTrue(predicate.match("не нрав"));
        Assertions.assertFalse(predicate.match("нравится"));
        Assertions.assertFalse(predicate.match("нрав"));
    }

}
package Utils;

import java.util.Random;

/**
 * Represents a helper randomizer class.
 */
public class Randomizer {

    private static final Random _random = new Random();

    /**
     * Returns a non-negative random integer that is less than the specified maximum.
     * @param max the exclusive upper bound of the random number to be generated.
     * @return non-negative random integer that is less than the specified maximum.
     */
    public static int getRandomNumber(int max) {
        return _random.nextInt(max);
    }

    /**
     * Returns random element from the specified collection.
     * @param elements a collection from which the element is to be picked.
     * @param <T> type of the collection of elements.
     * @return random element from the specified collection.
     */
    public static <T> T getRandomElement(T... elements) {
        var randomIndex = getRandomNumber(elements.length);
        return elements[randomIndex];
    }

}
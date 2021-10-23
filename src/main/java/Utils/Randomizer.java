package Utils;

import java.util.Random;

public class Randomizer {

    private static final Random _random = new Random();

    public static int getRandomNumber(int max) {
        return _random.nextInt(max);
    }

    public static <T> T getRandomElement(T... elements) {
        var randomIndex = getRandomNumber(elements.length);
        return elements[randomIndex];
    }

}
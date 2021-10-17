package Utils;

import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();

    public static <T> T getRandomElementFrom(T... elements) {
        var randomIndex = random.nextInt(elements.length);
        return elements[randomIndex];
    }

}
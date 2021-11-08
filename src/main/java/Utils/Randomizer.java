package Utils;

import java.util.Random;

/**
 * Представляет собой класс, содержащий вспомогательные статические методы для рандомизации.
 */
public class Randomizer {

    private static final Random random = new Random();

    /**
     * Возвращает случайное неотрицательное целое число,
     * которое строго меньше, чем указазанный максимум.
     * @param max верхняя граница случайного числа (не включительно).
     * @return Неотрицательное целое число,
     * которое строго меньше, чем указазанный максимум.
     */
    public static int getRandomNumber(int max) {
        return random.nextInt(max);
    }

    /**
     * Возвращает случайный элемент из указанной коллекции.
     * @param elements коллекция, из которой выбирается случайный элемент.
     * @param <T> тип коллекции элементов.
     * @return Случайный элемент из указанной коллекции.
     */
    public static <T> T getRandomElement(T... elements) {
        var randomIndex = getRandomNumber(elements.length);
        return elements[randomIndex];
    }

}
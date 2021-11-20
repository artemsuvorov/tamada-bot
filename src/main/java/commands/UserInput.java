package commands;

import java.util.Scanner;

/**
 * Представляет собой обертку над строкой String,
 * которая позволяет удобно извлекать необходимые токены из себя.
 */
public final class UserInput {

    private final String INTEGER_PATTERN = "(-*|\\+*)\\d+";
    private final String WHITESPACE_PATTERN = "\\s+";
    private final String input;

    public static final UserInput Empty = new UserInput("");

    public UserInput(String input) {
        this.input = input;
    }

    /**
     * Указывает, является ли строка ввода пустой.
     * @return true, если строка пуста или равна null,
     * иначе false.
     */
    public boolean isEmpty() {
        return input == null || input.isBlank();
    }

    /**
     * Возвращает первый токен из строки ввода.
     * @return Первый токен из строки ввода.
     */
    public String getCommandLiteral() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        var literals = input.split(WHITESPACE_PATTERN);
        return literals[0];
    }

    /**
     * Указывает, содержит ли строка ввода число Integer по шаблону +/-[0-9]+.
     * @return true, если строка ввода содержит такое число, иначе false.
     */
    public boolean hasInteger() {
        if (isEmpty()) return false;
        try (var scanner = new Scanner(input)) {
            return scanner.findInLine(INTEGER_PATTERN) != null;
        }
    }

    /**
     * Возвращает из строки ввода первое найденное число Integer по шаблону +/-[0-9]+.
     * @return Первое найденное по такому шаблону число.
     * @throws IllegalStateException строка ввода была пуста.
     */
    public int extractInteger() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        try (var scanner = new Scanner(input)) {
            var match = scanner.findInLine(INTEGER_PATTERN);
            if (match == null) return -1;
            return Integer.parseInt(match);
        }
    }

}
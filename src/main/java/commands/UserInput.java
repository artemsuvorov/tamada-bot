package commands;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Представляет собой обертку над строкой String,
 * которая позволяет удобно извлекать необходимые токены из себя.
 */
public final class UserInput {

    private final String INTEGER_PATTERN = "(-*|\\+*)\\d+";
    private final String WHITESPACE_PATTERN = "\\s+";

    private final String input;
    private final String[] tokens;

    public static final UserInput Empty = new UserInput();

    private UserInput() {
        this.input = null;
        this.tokens = null;
    }

    public UserInput(String input) {
        this.input = input.trim();
        this.tokens = this.input.split(WHITESPACE_PATTERN);
    }

    /**
     * Указывает, является ли строка ввода пустой.
     * @return true, если строка пуста или равна null, иначе false.
     */
    public boolean isEmpty() {
        return input == null || input.isBlank();
    }

    /**
     * Возвращает текст строки ввода пользователя.
     * @return Текст строки ввода пользователя.
     */
    public String getText() {
        return input;
    }

    /**
     * Возвращает первый токен из строки ввода.
     * @return Первый токен из строки ввода.
     */
    public String getCommand() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        return tokens[0];
    }

    /**
     * Указывает, содержит ли строка ввода пользователя аргументы,
     * т.е. какие-либо слова (токены), идущие после первого.
     * @return true, если строка ввода содержит аргументы, иначе false.
     */
    public boolean hasArguments() {
        if (isEmpty())
            return false;
        return tokens.length >= 2;
    }

    /**
     * Возвращает массив аргументов из строки ввода пользователя,
     * т.е. все слова (токены), идущие после первого.
     * @return Массив аргументов из строки ввода пользователя.
     */
    public String[] getArguments() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        var arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
        return arguments;
    }

    /**
     * Указывает, содержит ли строка ввода число Integer по шаблону +/-[0-9]+.
     * @return true, если строка ввода содержит такое число, иначе false.
     */
    public boolean hasInteger() {
        if (isEmpty())
            return false;
        try (var scanner = new Scanner(input)) {
            return scanner.findInLine(INTEGER_PATTERN) != null;
        }
    }

    /**
     * Возвращает из строки ввода первое найденное число Integer по шаблону +/-[0-9]+.
     * @return Первое найденное по такому шаблону число.
     * @throws IllegalStateException строка ввода была пуста.
     */
    public int getNextInteger() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        try (var scanner = new Scanner(input)) {
            var match = scanner.findInLine(INTEGER_PATTERN);
            if (match == null) return -1;
            return Integer.parseInt(match);
        }
    }

    /**
     * Возвращает текст строки ввода пользователя.
     * @return Текст строки ввода пользователя.
     */
    @Override
    public String toString() {
        return getText();
    }

}
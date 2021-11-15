package Commands;

import java.util.Scanner;

public final class UserInput {

    private final String INTEGER_PATTERN = "(-*|\\+*)\\d+";
    private final String WHITESPACE_PATTERN = "\\s+";
    private final String input;

    public static final UserInput Empty = new UserInput("");

    public UserInput(String input) {
        this.input = input;
    }

    public boolean isEmpty() {
        return input == null || input.isBlank();
    }

    public String getCommandLiteral() {
        if (isEmpty())
            throw new IllegalStateException("User input was empty.");
        var literals = input.split(WHITESPACE_PATTERN);
        return literals[0];
    }

    public boolean hasInteger() {
        if (isEmpty()) return false;
        try (var scanner = new Scanner(input)) {
            return scanner.findInLine(INTEGER_PATTERN) != null;
        }
    }

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
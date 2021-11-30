package anecdote;

import java.util.Arrays;

public enum Rating {
    None(0),
    Dislike(1),
    Unsatisfactory(2),
    Satisfactory(3),
    Good(4),
    Excellent(5);

    private final int value;

    Rating(final int value) {
        this.value = value;
    }

    public int getValue() { return value; }

    public static Rating fromInteger(int number) {
        return Arrays.stream(Rating.values())
                .filter(x -> x.getValue() == number).findFirst().orElse(None);
    }
}
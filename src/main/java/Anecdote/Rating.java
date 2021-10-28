package Anecdote;

public enum Rating {
    None,
    Excellent,
    Good,
    Satisfactory,
    Unsatisfactory,
    Dislike;

    public static Rating fromInteger(int number) {
        switch(number) {
            case 1: return Dislike;
            case 2: return Unsatisfactory;
            case 3: return Satisfactory;
            case 4: return Good;
            case 5: return Excellent;
            default: return None;
        }
    }
}
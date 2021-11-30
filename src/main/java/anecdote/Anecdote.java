package anecdote;

/**
 * Представляет собой класс анекдота с функцией получить содержание анекдота.
 */
public class Anecdote implements IAnecdote {

    private final String text;

    public Anecdote(String anecdote) {
        text = anecdote;
    }

    /**
     * Возвращает содержание анекдота в виде строки.
     * @return Содержание анекдота в виде строки.
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Преобразует анекдот в строку и возвращает содержание анекдота.
     * @return Содержание анекдота в виде строки.
     */
    @Override
    public String toString() {
        return getText();
    }

}
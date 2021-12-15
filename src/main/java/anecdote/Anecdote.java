package anecdote;

import java.util.Objects;

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

    // todo: add javadoc
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Anecdote anecdote = (Anecdote) other;
        return Objects.equals(text, anecdote.text);
    }

    // todo: add javadoc
    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
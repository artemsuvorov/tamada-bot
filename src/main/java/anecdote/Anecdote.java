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

    /**
     * Указывает, равны ли этот анекдот и переданный объект.
     * Анекдоты равны тогда, когда равны их тексты.
     * @param other объект, который подлежит сравнению с этим анекдотом.
     * @return true, если анекдоты равны, иначе false.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Anecdote anecdote = (Anecdote) other;
        return Objects.equals(text, anecdote.text);
    }

    /**
     * Возвращает хэш-код анекдота.
     * @return Хэш-код анекдота.
     */
    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
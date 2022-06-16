package anecdote;

// todo: remove the interface
/**
 * Определяет интерфейс анекдота с функцией получить содержание анекдота.
 */
public interface IAnecdote {

    /**
     * Когда переопределен, возвращает содержание анекдота в виде строки.
     * @return Содержание анекдота в виде строки.
     */
    String getText();

}
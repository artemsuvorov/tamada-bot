package Bot;

/**
 * Определяет объект, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод).
 */
public interface IBotService {

    /**
     * Когда переопределен, запускает цикл взаимодействия
     * пользователя и бота (ввод -> парсинг -> вывод).
     */
    void start();

}
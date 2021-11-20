package bot;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Представляет собой абстрактный класс, который организует цикл
 * взаимодействия пользователя и бота (ввод -> парсинг -> вывод)
 * в предоставленных потоках входных и выходных данных.
 */
public abstract class IOBotService implements IBotService {

    protected final PrintStream Out;
    protected final InputStream In;

    public IOBotService(PrintStream out, InputStream in) {
        Out = out;
        In = in;
    }

    /**
     * Когда переопределен, запускает цикл взаимодействия
     * пользователя и бота (ввод -> парсинг -> вывод).
     */
    public abstract void start();

}
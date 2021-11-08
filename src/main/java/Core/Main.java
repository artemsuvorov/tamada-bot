package Core;

import Bot.ConsoleTamadaBotService;

public class Main {

    /**
     * Основная точка входа в программу.
     * @param args Аргументы, задаваемые при запуске программы.
     */
    public static void main(String[] args) {
        var service = new ConsoleTamadaBotService(System.out, System.in);
        service.start();
    }

}
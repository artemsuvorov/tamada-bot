package core;

import bot.IOTamadaBotService;

public class Main {

    /**
     * Основная точка входа в программу.
     * @param args Аргументы, задаваемые при запуске программы.
     */
    public static void main(String[] args) {
        var service = new IOTamadaBotService(System.out, System.in);
        service.start();
    }

}
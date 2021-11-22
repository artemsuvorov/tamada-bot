package core;

import bot.IOTamadaBotService;
import bot.TelegramBotService;
import commands.InputPredicate;

import java.util.Scanner;

public class Main {

    /**
     * Основная точка входа в программу.
     * @param args Аргументы, задаваемые при запуске программы.
     */
    public static void main(String[] args) {
        runTelegramBot();
    }

    /**
     * Запускает бота в режиме работы с консолью.
     */
    private static void runConsoleBot() {
        var service = new IOTamadaBotService(System.out, System.in);
        service.start();
    }

    /**
     * Запускает Telegram бота @tamada_ru_bot.
     */
    private static void runTelegramBot() {
        System.out.println("[INFO]: Starting Telegram Bot Service ...");
        var service = new TelegramBotService();
        service.start();
        System.out.println("[INFO]: Telegram Bot Service started successfully in a separate thread.");
        System.out.println("[INFO]: Type 'exit' to stop Telegram Bot Service.");
        handleUserInput();
    }

    /**
     * Обрабатывает ввод пользователя из консоли.
     * Если пользователь ввел 'exit', завершает программу с кодом выхода 0.
     */
    private static void handleUserInput() {
        try (var scanner = new Scanner(System.in)) {
            while (true)
                if (scanner.nextLine().equals("exit"))
                    System.exit(0);
        }
    }

}
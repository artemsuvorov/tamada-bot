package Bot;

import Commands.BotCommand;
import Commands.CommandParser;
import Commands.MessageCommand;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public final class BotRoutine {

    private final Bot _bot;

    private final PrintStream _out;
    private final InputStream _in;

    private final Scanner _scanner;
    private final CommandParser _commandParser;

    public BotRoutine(Bot bot, PrintStream out, InputStream in) {
        _bot = bot;

        _out = out;
        _in = in;

        _scanner = new Scanner(_in);
        _commandParser = new CommandParser(_bot);
    }

    public void start() {
        var startConversation = new MessageCommand(_bot, bot -> bot.onStartConversation());
        executeCommand(startConversation);

        while (_bot.isChatting()) {
            var input = _scanner.nextLine();
            var command = _commandParser.parse(input);
            executeCommand(command);
        }
    }

    private void executeCommand(BotCommand command) {
        var message = command.execute();
        _out.println(message);
    }

}
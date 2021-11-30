import bot.*;

import java.io.InputStream;
import java.io.PrintStream;

public class CustomBotService extends IOTamadaBotService {

    public CustomBotService(PrintStream out, InputStream in) {
        super(out, in);
    }

    public IAnecdoteBot getBot() {
        return Bot;
    }

    public BotConfiguration getConfig() {
        return Config;
    }


}
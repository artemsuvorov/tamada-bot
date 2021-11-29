package bot;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BotConfigRepository {

    private static final File configDirectory = new File("src\\main\\resources\\");
    private static final Charset defaultEncoding = StandardCharsets.UTF_8;

    public BotConfigRepository() {

    }

    public BotConfiguration getDefaultConfig() {
        return getConfig("tamada-config.json");
    }

    public BotConfiguration getConfig(String configName) {
        var configFile = new File(configDirectory, getConfigFileName(configName));
        return BotConfiguration.deserializeBotConfig(configFile, defaultEncoding);
    }

    private String getConfigFileName(String configName) {
        switch (configName) {
            case "тамада":
            default:
                return "tamada-config.json";

            case "веселый тамада":
            case "веселый":
            case "весёлый тамада":
            case "весёлый":
                return "tamad-happyСonfig.json";

            case "грустный":
            case "грустный тамада":
                return "tamada-sedСonfig.json";
        }
    }

}
package Bot;

/**
 * Represents a string wrapper class that consists of
 * bot's name and bot's message.
 */
public class BotMessage {

    private final String _botName;
    private final String _value;

    public BotMessage(String botName, String value) {
        _botName = botName;
        _value = value;
    }

    /**
     * Returns the string consisting of the bot's name and bot's message.
     * @return the string consisting of the bot's name and bot's message.
     */
    @Override
    public String toString() {
        return buildMessageString();
    }

    /**
     * Builds and returns the string consisting of the bot's name and bot's message.
     * @return the string consisting of the bot's name and bot's message.
     */
    private String buildMessageString() {
        return _botName + ": " + _value;
    }

}
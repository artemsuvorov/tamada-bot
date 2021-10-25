package Bot;

public class BotMessage {

    private final String _botName;
    private final String _value;

    public BotMessage(String botName, String value) {
        _botName = botName;
        _value = value;
    }

    @Override
    public String toString() {
        return buildMessageString();
    }

    private String buildMessageString() {
        return _botName + ": " + _value;
    }

}
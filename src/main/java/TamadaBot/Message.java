package TamadaBot;

public class Message {

    private final String _senderName;
    private final String _value;

    public Message(String senderName, String value) {
        _senderName = senderName;
        _value = value;
    }

    @Override
    public String toString() {
        return buildMessageString();
    }

    private String buildMessageString() {
        return _senderName + ": " + _value;
    }

}
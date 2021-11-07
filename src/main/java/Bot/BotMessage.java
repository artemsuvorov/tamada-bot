package Bot;

/**
 * Представляет собой класс обертку над String,
 * которая является строкой, состоящей из имени бота и текста его сообщения.
 */
public class BotMessage {

    private final String botName;
    private final String text;

    public BotMessage(String botName, String value) {
        this.botName = botName;
        text = value;
    }

    /**
     * Возвращает строку состояющую из имени бота и текста его сообщения.
     * @return Строку состояющую из имени бота и текста его сообщения.
     */
    @Override
    public String toString() {
        return botName + ": " + text;
    }

}
package bot;

/**
 * Представляет собой класс обертку над String,
 * которая является строкой, состоящей из имени бота и текста его сообщения.
 */
public class BotMessage {

    private final String botName;
    private final String text;

    public BotMessage(String botName, String text) {
        this.botName = botName;
        this.text = text;
    }

    public BotMessage(String botName, String... texts) {
        this.botName = botName;
        this.text = String.join(" ", texts);
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
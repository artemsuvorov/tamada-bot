package TamadaBot;

public abstract class Bot {

    protected Message buildBotMessage(String... messages) {
        var name = getBotName();
        return new Message(name, String.join(" ", messages));
    }

    protected abstract String getBotName();

    public abstract Message greet();

    public abstract Message introduce();

    public abstract Message tellAnecdote();

}
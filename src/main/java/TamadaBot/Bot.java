package TamadaBot;

public abstract class Bot {

    protected Message buildBotMessage(String message) {
        var name = getBotName();
        return new Message(name, message);
    }

    protected abstract String getBotName();

    protected abstract Message greet();

}
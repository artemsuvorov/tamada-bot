package TamadaBot;

public final class TamadaBot extends Bot {

    private final String _name = "Тамада бот";

    public Message greet() {
        return buildBotMessage("Привет, мир!");
    }

    @Override
    protected String getBotName() {
        return _name;
    }

}
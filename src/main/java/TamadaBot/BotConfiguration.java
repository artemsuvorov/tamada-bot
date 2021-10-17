package TamadaBot;

public class BotConfiguration {

    private final String _name;
    private final String _introduction;
    private final String[] _greetings;
    private final String[] _starters;
    private final String[] _anecdotes;

    public BotConfiguration(String name, String introduction,
                            String[] greetings, String[] starters, String[] anecdotes) {
        _name = name;
        _introduction = introduction;
        _greetings = greetings;
        _starters = starters;
        _anecdotes = anecdotes;
    }

    public String getName() {
        return _name;
    }

    public String getIntroduction() {
        return _introduction;
    }

    public String[] getGreetings() {
        return _greetings;
    }

    public String[] getStarters() {
        return _starters;
    }

    public String[] getAnecdotes() {
        return  _anecdotes;
    }

}
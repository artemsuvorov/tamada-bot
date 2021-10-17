package TamadaBot;

import Utils.Randomizer;

public final class TamadaBot extends Bot {

    private final BotConfiguration _configuration;

    public TamadaBot(BotConfiguration configuration) {
        _configuration = configuration;
    }

    @Override
    public Message greet() {
        var greetings = _configuration.getGreetings();
        var greeting = Randomizer.getRandomElementFrom(greetings);
        return buildBotMessage(greeting);
    }

    @Override
    public Message introduce() {
        var introduction = _configuration.getIntroduction();
        return buildBotMessage(introduction);
    }

    @Override
    public Message tellAnecdote() {
        var anecdotes = _configuration.getAnecdotes();
        var anecdote = Randomizer.getRandomElementFrom(anecdotes);
        var starters = _configuration.getStarters();
        var starter = Randomizer.getRandomElementFrom(starters);
        return buildBotMessage(starter, anecdote);
    }

    @Override
    protected String getBotName() {
        return _configuration.getName();
    }

}
package Bot;

import Anecdote.IAnecdoteRepository;
import Utils.Randomizer;

public final class TamadaBot extends Bot {

    private final BotConfiguration _configuration;
    private final IAnecdoteRepository _anecdotes;

    private boolean awaitsRating = false;

    public TamadaBot(BotConfiguration configuration) {
        _configuration = configuration;
        _anecdotes = _configuration.getAnecdoteRepository();
    }

    @Override
    public BotMessage onStartConversation() {
        resetState();
        var start = _configuration.getConversationStart();
        return buildBotMessage(start);
    }

    @Override
    public BotMessage onWhatCanYouDo() {
        resetState();
        var helpMessage = _configuration.getHelpMessage();
        return buildBotMessage(helpMessage);
    }

    @Override
    public BotMessage greet() {
        resetState();
        var greetings = _configuration.getGreetings();
        return buildBotMessage(Randomizer.getRandomElementFrom(greetings));
    }

    @Override
    public BotMessage onHowAreYou() {
        resetState();
        var onHowAreYouMessages = _configuration.getOnHowAreYouMessages();
        return buildBotMessage(Randomizer.getRandomElementFrom(onHowAreYouMessages));
    }

    @Override
    public BotMessage introduce() {
        resetState();
        var introduction = _configuration.getIntroduction();
        return buildBotMessage(introduction);
    }

    @Override
    public BotMessage tellAnecdote() {
        resetState();
        var anecdote = _anecdotes.getNextAnecdote();
        var starters = _configuration.getStarters();
        var starter = Randomizer.getRandomElementFrom(starters);
        awaitsRating = true;
        return buildBotMessage(starter, anecdote.getAnecdote());
    }

    @Override
    public BotMessage onUserLaughed() {
        if (awaitsRating) {
            var onUserLikedMessages = _configuration.getOnLikedMessages();
            resetState();
            return buildBotMessage(Randomizer.getRandomElementFrom(onUserLikedMessages));
        } else {
            var onUserLaughedMessages = _configuration.getOnLaughMessages();
            resetState();
            return buildBotMessage(Randomizer.getRandomElementFrom(onUserLaughedMessages));
        }
    }

    @Override
    public BotMessage notUnderstand() {
        resetState();
        var notUnderstands = _configuration.getNotUnderstands();
        return buildBotMessage(Randomizer.getRandomElementFrom(notUnderstands));
    }

    @Override
    public BotMessage stopChatting() {
        resetState();
        super.stopChatting();
        var stopChatWords = _configuration.getStopChatWords();
        return buildBotMessage(Randomizer.getRandomElementFrom(stopChatWords));
    }

    @Override
    protected String getBotName() {
        return _configuration.getName();
    }

    private void resetState() {
        awaitsRating = false;
    }

}
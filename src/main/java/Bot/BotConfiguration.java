package Bot;

import Anecdote.IAnecdoteRepository;
import Anecdote.RandomRatableAnecdoteRepository;

public class BotConfiguration {

    private final String _name;
    private final String _conversationStart;
    private final String _introduction;
    private final String _helpMessage;
    private final String[] _greetings;
    private final String[] _onHowAreYouMessages;
    private final String[] _starters;
    private final String[] _anecdotes;
    private final String[] _onLikedMessages;
    private final String[] _onLaughMessages;
    private final String[] _notUnderstands;
    private final String[] _stopChatWords;

    public BotConfiguration(String name, String conversationStart, String introduction,
                            String helpMessage, String[] greetings, String[] onHowAreYouMessages,
                            String[] starters, String[] anecdotes, String[] onLikedMessages,
                            String[] onLaughMessages, String[] notUnderstands, String[] stopChatWords) {
        _name = name;
        _conversationStart = conversationStart;
        _introduction = introduction;
        _helpMessage = helpMessage;
        _greetings = greetings;
        _onHowAreYouMessages = onHowAreYouMessages;
        _starters = starters;
        _anecdotes = anecdotes;
        _onLikedMessages = onLikedMessages;
        _onLaughMessages = onLaughMessages;
        _notUnderstands = notUnderstands;
        _stopChatWords = stopChatWords;
    }

    public String getName() {
        return _name;
    }

    public String getConversationStart() {
        return _conversationStart;
    }

    public String getIntroduction() {
        return _introduction;
    }

    public String getHelpMessage() {
        return _helpMessage;
    }

    public String[] getGreetings() {
        return _greetings;
    }

    public String[] getOnHowAreYouMessages() {
        return _onHowAreYouMessages;
    }

    public String[] getStarters() {
        return _starters;
    }

    public IAnecdoteRepository getAnecdoteRepository() {
        var repository = new RandomRatableAnecdoteRepository(_anecdotes);
        return repository;
    }

    public String[] getOnLikedMessages() {
        return _onLikedMessages;
    }

    public String[] getOnLaughMessages() {
        return _onLaughMessages;
    }

    public String[] getNotUnderstands() {
        return _notUnderstands;
    }

    public String[] getStopChatWords() {
        return _stopChatWords;
    }

}
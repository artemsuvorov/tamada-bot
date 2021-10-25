package Bot;

import Anecdote.*;
import Utils.Randomizer;
import java.util.stream.Collectors;

public final class TamadaBot extends Bot {

    private final BotConfiguration _configuration;
    private final IRatableAnecdoteRepository _anecdoteRepository;

    private IRatableAnecdote lastAnecdote;
    private boolean anecdoteIsTold = false;
    private boolean awaitsRating = false;

    public TamadaBot(BotConfiguration configuration) {
        _configuration = configuration;
        _anecdoteRepository = new RandomRatableAnecdoteRepository(_configuration.Anecdotes);
    }

    @Override
    public BotMessage onStartConversation() {
        resetState();
        return buildBotMessage(_configuration.ConversationStart);
    }

    @Override
    public BotMessage onWhatCanYouDo() {
        resetState();
        return buildBotMessage(_configuration.HelpMessage);
    }

    @Override
    public BotMessage greet() {
        resetState();
        var greetings = _configuration.Greetings;
        return buildBotMessage(Randomizer.getRandomElement(greetings));
    }

    @Override
    public BotMessage onHowAreYou() {
        resetState();
        var onHowAreYouMessages = _configuration.OnHowAreYouMessages;
        return buildBotMessage(Randomizer.getRandomElement(onHowAreYouMessages));
    }

    @Override
    public BotMessage introduce() {
        resetState();
        return buildBotMessage(_configuration.Introduction);
    }

    @Override
    public BotMessage tellAnecdote() {
        resetState();

        if (!_anecdoteRepository.hasAnecdotes())
            return buildBotMessage(_configuration.OnNoAnecdotesMessage);

        var anecdote = _anecdoteRepository.getNextAnecdote();
        var starters = _configuration.AnecdoteStarters;
        var starter = Randomizer.getRandomElement(starters);

        anecdoteIsTold = true;
        if (anecdote instanceof IRatableAnecdote ratableAnecdote)
            lastAnecdote = ratableAnecdote;

        return buildBotMessage(starter, anecdote.getAnecdote());
    }

    @Override
    public BotMessage inviteToRate() {
        if (!anecdoteIsTold) {
            resetState();
            return buildBotMessage(_configuration.OnNoAnecdotesToRateMessage);
        }
        if (lastAnecdote == null) {
            resetState();
            return buildBotMessage(_configuration.OnCannotRateAnecdoteMessage);
        }

        awaitsRating = true;
        return buildBotMessage(_configuration.RateAnecdoteInvitation);
    }

    @Override
    public BotMessage onLikeRating() {
        return rateLastAnecdote(Rating.Like);
    }

    @Override
    public BotMessage onDislikeRating() {
        return rateLastAnecdote(Rating.Dislike);
    }

    @Override
    public BotMessage onCancelRating() {
        if (awaitsRating) {
            resetState();
            var messages = _configuration.OnCancelRatingMessages;
            return buildBotMessage(Randomizer.getRandomElement(messages));
        } else {
            resetState();
            return notUnderstand();
        }
    }

    @Override
    public BotMessage showFavorites() {
        resetState();

        var onShowFavoritesMessages = _configuration.OnShowFavoritesMessages;
        var message = Randomizer.getRandomElement(onShowFavoritesMessages);
        if (_anecdoteRepository.getFavorites().isEmpty())
            return buildBotMessage(_configuration.OnFavoritesEmptyMessage);

        var favorites = _anecdoteRepository.getFavorites()
                .stream().map(Object::toString).collect(Collectors.joining("\r\n"));
        return buildBotMessage(message, "\r\n", favorites);
    }

    @Override
    public BotMessage onUserLaughed() {
        if (anecdoteIsTold) {
            var onUserLikedMessages = _configuration.OnLikedMessages;
            resetState();
            return buildBotMessage(Randomizer.getRandomElement(onUserLikedMessages));
        } else {
            var onUserLaughedMessages = _configuration.OnLaughMessages;
            resetState();
            return buildBotMessage(Randomizer.getRandomElement(onUserLaughedMessages));
        }
    }

    @Override
    public BotMessage notUnderstand() {
        resetState();
        var notUnderstandMessages = _configuration.NotUnderstandMessages;
        return buildBotMessage(Randomizer.getRandomElement(notUnderstandMessages));
    }

    @Override
    public BotMessage stopChatting() {
        resetState();
        super.stopChatting();
        var stopChatMessages = _configuration.StopChatMessages;
        return buildBotMessage(Randomizer.getRandomElement(stopChatMessages));
    }

    @Override
    protected String getBotName() {
        return _configuration.BotName;
    }

    private BotMessage rateLastAnecdote(Rating rating) {
        if (!anecdoteIsTold) {
            resetState();
            return notUnderstand();
        }

        resetState();
        lastAnecdote.rate(rating);

        if (rating == Rating.Like)
            return buildBotMessage(Randomizer.getRandomElement(_configuration.OnLikeRatingMessages));
        else if (rating == Rating.Dislike)
            return buildBotMessage(Randomizer.getRandomElement(_configuration.OnDislikeRatingMessages));
        else
            throw new IllegalArgumentException("Unexpected rating.");
    }

    private void resetState() {
        anecdoteIsTold = false;
        awaitsRating = false;
    }

}
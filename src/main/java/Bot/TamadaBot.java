package Bot;

import Anecdote.*;
import Utils.Randomizer;
import java.util.stream.Collectors;

/**
 * Represents a Tamada bot implementation with all the bot's messages
 * being taken from the specified bot's configuration.
 */
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

    /**
     * Returns bot's message for conversation start.
     * @return bot's message for conversation start.
     */
    @Override
    public BotMessage onStartConversation() {
        resetState();
        return buildBotMessage(_configuration.ConversationStart);
    }

    /**
     * Returns bot's message for answer on "what can you do" question.
     * @return bot's message for answer on "what can you do" question.
     */
    @Override
    public BotMessage onWhatCanYouDo() {
        resetState();
        return buildBotMessage(_configuration.HelpMessage);
    }

    /**
     * Returns bot's message for greeting.
     * @return bot's message for greeting.
     */
    @Override
    public BotMessage greet() {
        resetState();
        var greetings = _configuration.Greetings;
        return buildBotMessage(Randomizer.getRandomElement(greetings));
    }

    /**
     * Returns bot's message for answer on "how are you" question.
     * @return bot's message for answer on "how are you" question.
     */
    @Override
    public BotMessage onHowAreYou() {
        resetState();
        var onHowAreYouMessages = _configuration.OnHowAreYouMessages;
        return buildBotMessage(Randomizer.getRandomElement(onHowAreYouMessages));
    }

    /**
     * Returns bot's message for introduction itself.
     * @return bot's message for introduction itself.
     */
    @Override
    public BotMessage introduce() {
        resetState();
        return buildBotMessage(_configuration.Introduction);
    }

    /**
     * Returns the anecdote message.
     * @return the anecdote message.
     */
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

    /**
     * Returns bot's message for the rating invitation.
     * @return bot's message for the rating invitation.
     */
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

    /**
     * Returns bot's message for the reaction on user's like.
     * @return bot's message for the reaction on user's like.
     */
    @Override
    public BotMessage onLikeRating() {
        return rateLastAnecdote(Rating.Like);
    }

    /**
     * Returns bot's message for the reaction on user's dislike.
     * @return bot's message for the reaction on user's dislike.
     */
    @Override
    public BotMessage onDislikeRating() {
        return rateLastAnecdote(Rating.Dislike);
    }

    /**
     * Returns bot's message for the reaction on user canceling anecdote rating.
     * @return bot's message for the reaction on user canceling anecdote rating.
     */
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

    /**
     * Returns the message with user's favorite anecdotes list.
     * @return the message with user's favorite anecdotes list.
     */
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

    /**
     * Returns bot's message for reaction on user's laugh.
     * @return bot's message for reaction on user's laugh.
     */
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

    /**
     * Returns bot's message for the situation when the user input is unintelligible.
     * @return bot's message for the situation when the user input is unintelligible.
     */
    @Override
    public BotMessage notUnderstand() {
        resetState();
        var notUnderstandMessages = _configuration.NotUnderstandMessages;
        return buildBotMessage(Randomizer.getRandomElement(notUnderstandMessages));
    }

    /**
     * Forces the bot to stop awaiting user's input.
     * @return bot's message after the stop chatting command.
     */
    @Override
    public BotMessage stopChatting() {
        resetState();
        super.stopChatting();
        var stopChatMessages = _configuration.StopChatMessages;
        return buildBotMessage(Randomizer.getRandomElement(stopChatMessages));
    }

    /**
     * Returns the name of the bot.
     * @return the name of the bot.
     */
    @Override
    protected String getBotName() {
        return _configuration.BotName;
    }

    /**
     * Assigns the specified rating to the last anecdote and
     * returns bot's message for reaction on user liked or disliked the last anecdote.
     * @param rating the rating to be assigned to the last told anecdote.
     * @return bot's message for reaction on user liked or disliked the last anecdote.
     */
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

    /**
     * Resets the state of the bot.
     */
    private void resetState() {
        anecdoteIsTold = false;
        awaitsRating = false;
    }

}
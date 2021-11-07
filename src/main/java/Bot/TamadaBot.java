package Bot;

import Anecdote.*;
import Utils.Randomizer;
import java.util.stream.Collectors;

/**
 * Представляет собой класс, который реализует абстрактный класс бота,
 * беря все необходимые сообщения бота из указанной конфигурации.
 */
public final class TamadaBot extends Bot {

    private final BotConfiguration configuration;
    private final IRatableAnecdoteRepository anecdoteRepository;

    private IRatableAnecdote lastAnecdote;
    private boolean anecdoteIsTold = false;
    private boolean awaitsRating = false;

    public TamadaBot(BotConfiguration configuration) {
        this.configuration = configuration;
        anecdoteRepository = new RandomRatableAnecdoteRepository(this.configuration.Anecdotes);
    }

    // TODO: maybe there is no need in all of these methods

    /**
     * Возвращает сообщение бота, предназначенное для старта общения.
     * @return Сообщение бота, предназначенное для старта общения.
     */
    @Override
    public BotMessage onStartConversation() {
        resetState();
        return buildBotMessage(configuration.ConversationStart);
    }

    /**
     * Возвращает сообщение бота, являющееся ответом на вопрос "что ты умеешь".
     * @return сообщение бота, являющееся ответом на вопрос "что ты умеешь".
     */
    @Override
    public BotMessage onWhatCanYouDo() {
        resetState();
        return buildBotMessage(configuration.HelpMessage);
    }

    /**
     * Возвращает сообщение бота, содержащее приветствие.
     * @return Сообщение бота, содержащее приветствие.
     */
    @Override
    public BotMessage greet() {
        resetState();
        var greetings = configuration.Greetings;
        return buildBotMessage(Randomizer.getRandomElement(greetings));
    }

    /**
     * Returns bot's message for answer on "how are you" question.
     * @return bot's message for answer on "how are you" question.
     */
    @Override
    public BotMessage onHowAreYou() {
        resetState();
        var onHowAreYouMessages = configuration.OnHowAreYouMessages;
        return buildBotMessage(Randomizer.getRandomElement(onHowAreYouMessages));
    }

    /**
     * Returns bot's message for introduction itself.
     * @return bot's message for introduction itself.
     */
    @Override
    public BotMessage introduce() {
        resetState();
        return buildBotMessage(configuration.Introduction);
    }

    /**
     * Returns the anecdote message.
     * @return the anecdote message.
     */
    @Override
    public BotMessage tellAnecdote() {
        resetState();

        if (!anecdoteRepository.hasAnecdotes())
            return buildBotMessage(configuration.OnNoAnecdotesMessage);

        var anecdote = anecdoteRepository.getNextAnecdote();
        var starters = configuration.AnecdoteStarters;
        var starter = Randomizer.getRandomElement(starters);

        anecdoteIsTold = true;
        if (anecdote instanceof IRatableAnecdote ratableAnecdote)
            lastAnecdote = ratableAnecdote;

        awaitsRating = true;
        return buildBotMessage(starter, anecdote.getText());
    }

    /**
     * Returns bot's message for the rating invitation.
     * @return bot's message for the rating invitation.
     */
    @Override
    public BotMessage inviteToRate() {
        if (!anecdoteIsTold) {
            resetState();
            return buildBotMessage(configuration.OnNoAnecdotesToRateMessage);
        }
        if (lastAnecdote == null) {
            resetState();
            return buildBotMessage(configuration.OnCannotRateAnecdoteMessage);
        }

        awaitsRating = true;
        return buildBotMessage(configuration.RateAnecdoteInvitation);
    }

    /**
     * Returns bot's message for the reaction on user's like.
     * @return bot's message for the reaction on user's like.
     */
    @Override
    public BotMessage onRatingSubmitted(Rating rating) {
        return rateLastAnecdote(rating);
    }

    /**
     * Returns bot's message for the reaction on user canceling anecdote rating.
     * @return bot's message for the reaction on user canceling anecdote rating.
     */
    @Override
    public BotMessage onCancelRating() {
        if (awaitsRating) {
            resetState();
            var messages = configuration.OnCancelRatingMessages;
            return buildBotMessage(Randomizer.getRandomElement(messages));
        } else {
            resetState();
            return notUnderstand();
        }
    }

    /**
     * Returns bot's message for the reaction on user didn't provide rating.
     * @return bot's message for the reaction on user didn't provide rating.
     */
    @Override
    public BotMessage onRateNoRatingProvided() {
        if (awaitsRating) {
            resetState();
            awaitsRating = true;
            return buildBotMessage(configuration.OnRateNoRatingProvided);
        } else {
            resetState();
            return notUnderstand();
        }
    }

    /**
     * Returns bot's message for the reaction on user provided invalid rating
     * during anecdote rating.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    @Override
    public BotMessage onRateInvalidRatingProvided() {
        if (awaitsRating) {
            resetState();
            awaitsRating = true;
            return buildBotMessage(configuration.OnRateInvalidRatingProvided);
        } else {
            resetState();
            return notUnderstand();
        }
    }

    /**
     * Returns bot's message for the reaction on user provided no rating
     * during showing anecdotes.
     * @return bot's message for the reaction on user provided no rating.
     */
    @Override
    public BotMessage onShowNoRatingProvided() {
        resetState();
        return buildBotMessage(configuration.OnShowNoRatingProvided);
    }

    /**
     * Returns bot's message for the reaction on user provided invalid rating
     * during showing anecdotes.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    @Override
    public BotMessage onShowInvalidRatingProvided() {
        resetState();
        return buildBotMessage(configuration.OnShowInvalidRatingProvided);
    }

    /**
     * Returns the message with anecdotes which have the specified rating.
     * @return the message with anecdotes which have the specified rating.
     */
    @Override
    public BotMessage showAnecdotesOfRating(Rating rating) {
        resetState();

        var onShowAnecdotesMessages = configuration.OnShowAnecdotesMessages;
        var message = Randomizer.getRandomElement(onShowAnecdotesMessages);
        if (anecdoteRepository.getAnecdotesOfRating(rating).isEmpty())
            return buildBotMessage(configuration.OnAnecdotesEmptyMessage);

        var anecdotes = anecdoteRepository.getAnecdotesOfRating(rating)
                .stream().map(Object::toString).collect(Collectors.joining("\r\n"));
        return buildBotMessage(message, "\r\n", anecdotes);
    }

    /**
     * Returns the message with user's favorite anecdotes list.
     * @return the message with user's favorite anecdotes list.
     */
    @Override
    public BotMessage showFavorites() {
        return showAnecdotesOfRating(Rating.Excellent);
    }

    /**
     * Returns bot's message for reaction on user's laugh.
     * @return bot's message for reaction on user's laugh.
     */
    @Override
    public BotMessage onUserLaughed() {
        if (anecdoteIsTold) {
            var onUserLikedMessages = configuration.OnLikedMessages;
            resetState();
            return buildBotMessage(Randomizer.getRandomElement(onUserLikedMessages));
        } else {
            var onUserLaughedMessages = configuration.OnLaughMessages;
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
        var notUnderstandMessages = configuration.NotUnderstandMessages;
        return buildBotMessage(Randomizer.getRandomElement(notUnderstandMessages));
    }

    /**
     * Forces the bot to stop awaiting user's input.
     * @return bot's message after the stop chatting command.
     */
    @Override
    public IRatableAnecdoteRepository getAnecdoteRepository() {
        return anecdoteRepository;
    }

    /**
     * Forces the bot to stop awaiting user's input.
     * @return bot's resulting message after this action.
     */
    @Override
    public BotMessage stopChatting() {
        resetState();
        super.stopChatting();
        var stopChatMessages = configuration.StopChatMessages;
        return buildBotMessage(Randomizer.getRandomElement(stopChatMessages));
    }

    /**
     * Returns the name of the bot.
     * @return the name of the bot.
     */
    @Override
    protected String getBotName() {
        return configuration.BotName;
    }

    /**
     * Assigns the specified rating to the last anecdote and
     * returns bot's message for reaction on user liked or disliked the last anecdote.
     * @param rating the rating to be assigned to the last told anecdote.
     * @return bot's message for reaction on user liked or disliked the last anecdote.
     */
    private BotMessage rateLastAnecdote(Rating rating) {
        if (!awaitsRating) {
            resetState();
            return notUnderstand();
        }

        resetState();
        lastAnecdote.setRating(rating);

        if (rating == Rating.Excellent)
            return buildBotMessage(Randomizer.getRandomElement(configuration.OnLikeRatingMessages));
        else if (rating == Rating.Dislike)
            return buildBotMessage(Randomizer.getRandomElement(configuration.OnDislikeRatingMessages));
        else
            return buildBotMessage(Randomizer.getRandomElement(configuration.OnAnyRatingMessages));
    }

    /**
     * Resets the state of the bot.
     */
    private void resetState() {
        anecdoteIsTold = false;
        awaitsRating = false;
    }

}
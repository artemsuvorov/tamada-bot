package Bot;

import Anecdote.IRatableAnecdoteRepository;
import Anecdote.Rating;

/**
 * Определяет абстрактный класс бота, который может
 * отправлять сообщения такие, как приветствие, справка, анекдот и т.д.
 */
public abstract class Bot {

    private boolean isActive = true;

    /**
     * Builds and returns the message concatenated with the bot's name in the front.
     * @param messages strings to be concatenated and be put into the bot's message.
     * @return the bot's message with the bot's name.
     */
    protected BotMessage buildBotMessage(String... messages) {
        var name = getBotName();
        return new BotMessage(name, String.join(" ", messages));
    }

    /**
     * Indicates if the bot is awaiting user's input.
     * @return true if the bot is awaiting user's input, otherwise false.
     */
    public boolean isChatting() {
        return isActive;
    }

    /**
     * Forces the bot to stop awaiting user's input.
     * @return bot's resulting message after this action.
     */
    public BotMessage stopChatting() {
        isActive = false;
        return buildBotMessage();
    }

    /**
     * When overridden in a derived class, returns the name of the bot.
     * @return the name of the bot.
     */
    protected abstract String getBotName();

    /**
     * When overridden in a derived class, returns bot's message for conversation start.
     * @return bot's message for conversation start.
     */
    public abstract BotMessage onStartConversation();

    /**
     * When overridden in a derived class, returns bot's message for
     * answer on "what can you do" question.
     * @return bot's message for answer on "what can you do" question.
     */
    public abstract BotMessage onWhatCanYouDo();

    /**
     * When overridden in a derived class, returns bot's message for greeting.
     * @return bot's message for greeting.
     */
    public abstract BotMessage greet();

    /**
     * When overridden in a derived class, returns bot's message for
     * answer on "how are you" question.
     * @return bot's message for answer on "how are you" question.
     */
    public abstract BotMessage onHowAreYou();

    /**
     * When overridden in a derived class, returns bot's message for introduction itself.
     * @return bot's message for introduction itself.
     */
    public abstract BotMessage introduce();

    /**
     * When overridden in a derived class, returns the anecdote message.
     * @return the anecdote message.
     */
    public abstract BotMessage tellAnecdote();

    /**
     * When overridden in a derived class, returns bot's message for
     * the rating invitation.
     * @return bot's message for the rating invitation.
     */
    public abstract BotMessage inviteToRate();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user's like.
     * @return bot's message for the reaction on user's like.
     */
    public abstract BotMessage onRatingSubmitted(Rating rating);

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user canceling anecdote rating.
     * @return bot's message for the reaction on user canceling anecdote rating.
     */
    public abstract BotMessage onCancelRating();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user didn't provide rating.
     * @return bot's message for the reaction on user didn't provide rating.
     */
    public abstract BotMessage onRateNoRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided invalid rating during anecdote rating.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    public abstract BotMessage onRateInvalidRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided no rating during showing anecdotes.
     * @return bot's message for the reaction on user provided no rating.
     */
    public abstract BotMessage onShowNoRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided invalid rating during showing anecdotes.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    public abstract BotMessage onShowInvalidRatingProvided();

    /**
     * When overridden in a derived class, returns the message with anecdotes
     * which have the specified rating.
     * @return the message with anecdotes which have the specified rating.
     */
    public abstract BotMessage showAnecdotesOfRating(Rating rating);

    /**
     * When overridden in a derived class, returns the message with user's favorite anecdotes list.
     * @return the message with user's favorite anecdotes list.
     */
    public abstract BotMessage showFavorites();

    /**
     * When overridden in a derived class, returns bot's message for
     * reaction on user's laugh.
     * @return bot's message for reaction on user's laugh.
     */
    public abstract BotMessage onUserLaughed();

    /**
     * When overridden in a derived class, returns bot's message for
     * the situation when the user input is unintelligible.
     * @return bot's message for the situation when the user input is unintelligible.
     */
    public abstract BotMessage notUnderstand();

    // todo: delete later
    public abstract IRatableAnecdoteRepository getAnecdoteRepository();
}
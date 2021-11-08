package Bot;

import Anecdote.IRatableAnecdoteRepository;
import Anecdote.Rating;

/**
 * Определяет интерфейс бота, который может
 * отправлять сообщения такие, как приветствие, справка, анекдот и т.д.
 */
public interface IAnecdoteBot {

    /**
     * Indicates if the bot is awaiting user's input.
     * @return true if the bot is awaiting user's input, otherwise false.
     */
    boolean isActive();

    /**
     * Forces the bot to stop awaiting user's input.
     * @return bot's resulting message after this action.
     */
    BotMessage stop();

    /**
     * When overridden in a derived class, returns bot's message for conversation start.
     * @return bot's message for conversation start.
     */
    BotMessage onStartConversation();

    /**
     * When overridden in a derived class, returns bot's message for
     * answer on "what can you do" question.
     * @return bot's message for answer on "what can you do" question.
     */
    BotMessage onWhatCanYouDo();

    /**
     * When overridden in a derived class, returns bot's message for greeting.
     * @return bot's message for greeting.
     */
    BotMessage greet();

    /**
     * When overridden in a derived class, returns bot's message for
     * answer on "how are you" question.
     * @return bot's message for answer on "how are you" question.
     */
    BotMessage onHowAreYou();

    /**
     * When overridden in a derived class, returns bot's message for introduction itself.
     * @return bot's message for introduction itself.
     */
    BotMessage introduce();

    /**
     * When overridden in a derived class, returns the anecdote message.
     * @return the anecdote message.
     */
    BotMessage tellAnecdote();

    /**
     * When overridden in a derived class, returns bot's message for
     * the rating invitation.
     * @return bot's message for the rating invitation.
     */
    BotMessage inviteToRate();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user's like.
     * @return bot's message for the reaction on user's like.
     */
    BotMessage onRatingSubmitted(Rating rating);

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user canceling anecdote rating.
     * @return bot's message for the reaction on user canceling anecdote rating.
     */
    BotMessage onCancelRating();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user didn't provide rating.
     * @return bot's message for the reaction on user didn't provide rating.
     */
    BotMessage onRateNoRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided invalid rating during anecdote rating.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    BotMessage onRateInvalidRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided no rating during showing anecdotes.
     * @return bot's message for the reaction on user provided no rating.
     */
    BotMessage onShowNoRatingProvided();

    /**
     * When overridden in a derived class, returns bot's message for
     * the reaction on user provided invalid rating during showing anecdotes.
     * @return bot's message for the reaction on user provided invalid rating.
     */
    BotMessage onShowInvalidRatingProvided();

    /**
     * When overridden in a derived class, returns the message with anecdotes
     * which have the specified rating.
     * @return the message with anecdotes which have the specified rating.
     */
    BotMessage showAnecdotesOfRating(Rating rating);

    /**
     * When overridden in a derived class, returns the message with user's favorite anecdotes list.
     * @return the message with user's favorite anecdotes list.
     */
    BotMessage showFavorites();

    /**
     * When overridden in a derived class, returns bot's message for
     * reaction on user's laugh.
     * @return bot's message for reaction on user's laugh.
     */
    BotMessage onUserLaughed();

    /**
     * When overridden in a derived class, returns bot's message for
     * the situation when the user input is unintelligible.
     * @return bot's message for the situation when the user input is unintelligible.
     */
    BotMessage notUnderstand();

    // todo: delete it later
    IRatableAnecdoteRepository getAnecdoteRepository();
}
package Bot;

/**
 * Represents a configuration class that stores bot's name and all its messages.
 */
public class BotConfiguration {

    public String BotName;

    public String ConversationStart;
    public String HelpMessage;
    public String Introduction;

    public String[] Greetings;

    public String[] OnHowAreYouMessages;

    public String[] AnecdoteStarters;
    public String[] Anecdotes;

    public String RateAnecdoteInvitation;
    public String OnNoAnecdotesToRateMessage;
    public String OnCannotRateAnecdoteMessage;
    public String[] OnLikeRatingMessages;
    public String[] OnDislikeRatingMessages;
    public String[] OnAnyRatingMessages;
    public String[] OnCancelRatingMessages;
    public String OnRateNoRatingProvided;
    public String OnRateInvalidRatingProvided;

    public String OnShowNoRatingProvided;
    public String OnShowInvalidRatingProvided;
    public String OnAnecdotesEmptyMessage;
    public String[] OnShowAnecdotesMessages;

    public String[] OnLikedMessages;
    public String[] OnLaughMessages;

    public String[] NotUnderstandMessages;
    public String[] StopChatMessages;

    public String OnNoAnecdotesMessage;

}
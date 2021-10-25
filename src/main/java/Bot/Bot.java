package Bot;

import Anecdote.IRatableAnecdoteRepository;

public abstract class Bot {

    private boolean _isChatting = true;

    protected BotMessage buildBotMessage(String... messages) {
        var name = getBotName();
        return new BotMessage(name, String.join(" ", messages));
    }

    public boolean isChatting() {
        return _isChatting;
    }

    public BotMessage stopChatting() {
        _isChatting = false;
        return buildBotMessage();
    }

    protected abstract String getBotName();

    public abstract BotMessage onStartConversation();

    public abstract BotMessage onWhatCanYouDo();

    public abstract BotMessage greet();

    public abstract BotMessage onHowAreYou();

    public abstract BotMessage introduce();

    public abstract BotMessage tellAnecdote();

    public abstract BotMessage inviteToRate();

    public abstract BotMessage onLikeRating();

    public abstract BotMessage onDislikeRating();

    public abstract BotMessage onCancelRating();

    public abstract BotMessage showFavorites();

    public abstract BotMessage onUserLaughed();

    public abstract BotMessage notUnderstand();

    // todo: delete later
    public abstract IRatableAnecdoteRepository getAnecdoteRepository();

}
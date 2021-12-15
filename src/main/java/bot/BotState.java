package bot;

public enum BotState {
    Default,
    AnecdoteTold,
    UnfinishedAnecdoteTold,
    UserAnecdoteTold,
    Deactivated;

    /**
     * Указывает активен ли бот, т.е. ожидает ли он
     * следующего ввода пользователем сообщения.
     * @return true, если бот активен, иначе false.
     */
    public boolean isActive() {
        return this != BotState.Deactivated;
    }

    /**
     * Указывает, был ли рассказан анекдот.
     * @return true, если анекдот был рассказан, иначе false.
     */
    public boolean wasAnecdoteTold() {
        return this == AnecdoteTold || this == UserAnecdoteTold;
    }

    /**
     * Указывает, был ли рассказан неоконченный анекдот.
     * @return true, если неоконченный анекдот был рассказан, иначе false.
     */
    public boolean wasUnfinishedAnecdoteTold() {
        return this == UnfinishedAnecdoteTold;
    }

}

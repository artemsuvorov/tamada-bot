package bot;

/**
 * Представляет собой конфигурацию, содержащую имя бота и все его сообщения.
 */
public class BotConfiguration {

    /**
     * Имя бота
      */
    public String BotName;

    /**
     * Сообщение бота при старте диалога
      */
    public String ConversationStart;
    /**
     * Сообщение, содержащее справку бота
      */
    public String HelpMessage;
    /**
     * Сообщение, в котором бот представляется
      */
    public String Introduction;

    /**
     * Приветствия бота
      */
    public String[] Greetings;

    /**
     * Ответы бота на вопрос "как дела"
      */
    public String[] OnHowAreYouMessages;

    /**
     * Фразы бота, предваряющие анекдот
      */
    public String[] AnecdoteStarters;
    /**
     * Массив анекдотов бота
      */
    public String[] Anecdotes;

    /**
     * Сообщение бота с предложением оценить анекдот
      */
    public String RateAnecdoteInvitation;
    /**
     * Сообщение бота, когда нет анекдота для оценки
     * (когда пользователь хочет оценить анекдот,
     * когда анекдот не был рассказан)
     */
    public String OnNoAnecdotesToRateMessage;
    /**
     * Сообщение бота, когда анекдот не может быть оценен
     * (например, анекдот не поддерживает оценивания)
     */
    public String OnCannotRateAnecdoteMessage;
    /**
     * Сообщения бота, являющееся реакцией на положительную оценку пользователя
     */
    public String[] OnLikeRatingMessages;
    /**
     * Сообщение бота, являющееся реакцией на отрицательную оценку пользователя
     */
    public String[] OnDislikeRatingMessages;
    /**
     * Сообщения бота, являющееся реакцией на все остальные оценки пользователя
     */
    public String[] OnAnyRatingMessages;
    /**
     * Сообщения бота, когда пользователь передумал оценивать анекдот
     */
    public String[] OnCancelRatingMessages;
    /**
     * Сообщение бота, когда пользователь не указал оценку при оценивании анекдота
     */
    public String OnRateNoRatingProvided;
    /**
     * Сообщение бота, когда пользователь указал некорректную оценку при оценивании анекдота
     */
    public String OnRateInvalidRatingProvided;

    /**
     * Сообщение бота, когда пользователь не указал оценку при
     * просьбе отобразить анекдоты с некоторой оценкой
     */
    public String OnShowNoRatingProvided;
    /**
     * Сообщение бота, когда пользователь указал некорректную
     * оценку при просьбе отобразить анекдоты с некоторой оценкой
     */
    public String OnShowInvalidRatingProvided;
    /**
     * Сообщение бота, когда список запрошенных анекдотов пуст
     */
    public String OnAnecdotesEmptyMessage;
    /**
     * Сообщения бота, когда был запрошен список анекдотов
     */
    public String[] OnShowAnecdotesMessages;

    /**
     * Сообщения бота, являющееся реакцией на то, что пользователь похвалил анекдот
     */
    public String[] OnLikedMessages;
    /**
     * Сообщения бота, являющееся реакцией на то, что пользователь посмеялся ("хаха")
     */
    public String[] OnLaughMessages;

    /**
     * Сообщения бота, когда пользователь неизвестную команду
     */
    public String[] NotUnderstandMessages;
    /**
     * Сообщения бота при окончании диалога
     */
    public String[] StopChatMessages;

    /**
     * Сообщение бота, когда массив анекдотов пуст
     */
    public String OnNoAnecdotesMessage;

}
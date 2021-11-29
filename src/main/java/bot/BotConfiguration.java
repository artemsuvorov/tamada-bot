package bot;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

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
     * Сообщение бота, когда пользователь просит начать диалог, но диалог уже был начат.
     */
    public String OnAlreadyStarted;
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
     * Сообщение бота, предваряющее неоконченный анекдот.
     */
    public String OnTellUnfinishedAnecdote;

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
     * Сообщения бота, когда была успешно предложена концовка к предыдущему анекдоту
     */
    public String[] OnEndingSuggested;
    /**
     * Сообщение бота, когда нет анекдота, к которому можно было бы добавить концовку
     */
    public String OnNoAnecdotesToSuggest;
    /**
     * Сообщение бота, когда пользователь ввел некорректную концовку
     */
    public String OnInvalidSuggestion;

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

    /**
     * Парсит файл Bot-Config.json и возвращает полученную конфигурацию бота BotConfiguration.
     * @return Конфигурация бота полученная из файла Bot-Config.json.
     * @throws com.google.gson.JsonSyntaxException
     */
    public static BotConfiguration deserializeBotConfig(File configFile, Charset encoding) {
        var json = readBotConfigFile(configFile, encoding);
        var gson = new Gson();
        return gson.fromJson(json, BotConfiguration.class);
    }

    /**
     * Читает Bot-Config.json и возвращает его содержание в виде строки,
     * если операция чтения прошла успешно, иначе null.
     * @return Содержание файла Bot-Config.json в виде строки.
     */
    private static String readBotConfigFile(File configFile, Charset encoding) {
        try (var fileStream = new FileInputStream(configFile)) {
            var data = new byte[(int)configFile.length()];
            fileStream.read(data);
            return new String(data, encoding);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
package anecdote;

import com.google.gson.GsonBuilder;
import event.EventHandler;

import java.util.ArrayList;

/**
 * Представляет собой список анекдотов, которые является общими для всех пользователей.
 * Реализует интерфейс синглтона.
 */
public final class CommonAnecdoteList {

    private ArrayList<Anecdote> commonAnecdotes;

    private final CommonAnecdoteListSerializer serializer = new CommonAnecdoteListSerializer();
    private final CommonAnecdoteListDeserializer deserializer = new CommonAnecdoteListDeserializer();

    private static CommonAnecdoteList instance;

    private CommonAnecdoteList() {
        commonAnecdotes = new ArrayList<>();
    }

    /**
     * Возвращает ссылку на единственный экземпляр этого класса в программе.
     * @return Единственный на всю программу экземпляр класса {@link CommonAnecdoteList}.
     */
    public static CommonAnecdoteList get() {
        if (instance == null)
            instance = new CommonAnecdoteList();
        return instance;
    }

    /**
     * Возвращает список анекдотов, общих для всех пользователей, типа {@link ArrayList<Anecdote>}.
     * @return Список анекдотов {@link ArrayList<Anecdote>}.
     */
    public ArrayList<Anecdote> getAnecdotes() {
        return commonAnecdotes;
    }

    /**
     * Добавляет указанный анекдот в список общих для всех пользователей.
     * @param anecdote анекдот, который будет добавлен анекдотов, в список общих для всех пользователей.
     */
    public void add(Anecdote anecdote) {
        commonAnecdotes.add(anecdote);
    }

    /**
     * Добавляет переданные анекдоты в список анекдотов, общих для всех пользователей.
     * @param anecdotes анекдоты, который будут добавлены в список анекдотов, общих для всех пользователей.
     */
    public void addAll(ArrayList<Anecdote> anecdotes) {
        commonAnecdotes.addAll(anecdotes);
    }

    /**
     * Удаляет все анекдоты из этого списка анекдотов, общих для всех пользователей.
     */
    public void clear() {
        commonAnecdotes.clear();
    }

    /**
     * Сериализует этот список анекдотов, общих для всех пользователей,
     * в Json формат в строку String и возвращает ее.
     * @return Строка String, содержащая сериализованный список общих анекдотов,
     * представленный в формате Json.
     */
    public String serializeCommonAnecdotes() {
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), serializer)
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    /**
     * Десериализует список анекдотов, общих для всех пользователей,
     * из указанной строки String, содержащей данные в формате Json,
     * и возвращает полученный список общих анекдотов {@link CommonAnecdoteList}.
     * @return Список общих анекдотов, десериализованный из Json строки String.
     */
    public void deserializeCommonAnecdotes(String json) {
        if (json == null) return;
        var gson = new GsonBuilder()
                .registerTypeAdapter(this.getClass(), deserializer)
                .create();
        CommonAnecdoteList list = gson.fromJson(json, this.getClass());
        this.commonAnecdotes = list.commonAnecdotes;
    }

}
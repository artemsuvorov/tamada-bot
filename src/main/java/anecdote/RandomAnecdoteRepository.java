package anecdote;

import utils.Randomizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Представляет собой класс репозитория анекдотов,
 * который выдает анекдот один за другим в случайной неповторяющейся последовательности.
 */
public class RandomAnecdoteRepository implements IAnecdoteRepository {

    private ArrayList<IAnecdote> anecdotes;
    protected ArrayList<IAnecdote> toldAnecdotes;

    public RandomAnecdoteRepository(String[] anecdotes) {
        // converts string array of anecdotes to list of IAnecdotes
        this((ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
            .stream().map(x->(IAnecdote)new Anecdote(x)).collect(Collectors.toList()));
    }

    public RandomAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        if (anecdotes == null || anecdotes.isEmpty())
            throw new IllegalArgumentException("Anecdotes collection cannot be null or empty.");

        this.anecdotes = anecdotes;
        toldAnecdotes = new ArrayList<IAnecdote>();
    }

    /**
     * Возвращает количество анекдотов, которые содержатся в этом репозитории.
     * @return количество анекдотов, которые содержатся в этом репозитории.
     */
    @Override
    public int getCount() {
        return anecdotes.size() + toldAnecdotes.size();
    }

    /**
     * Указывает, является ли этот репозиторий пустым.
     * @return true, если репозиторий пуст, иначе false.
     */
    @Override
    public boolean hasAnecdotes() {
        return !anecdotes.isEmpty() || !toldAnecdotes.isEmpty();
    }

    /**
     * Выбирает и возвращает случайный анекдот из набора еще не рассказанных анекдотов.
     * Когда все анекдоты этого репозитория рассказаны, принимает рассказанные анекдоты
     * как еще не рассказанные.
     * @return Случайный анекдот из набора еще не рассказанных анекдотов.
     */
    @Override
    public IAnecdote getNextAnecdote() {
        if (anecdotes.isEmpty() && toldAnecdotes.isEmpty())
            throw new IllegalArgumentException("There are no anecdotes In repository!");

        if (anecdotes.isEmpty()) {
            anecdotes = toldAnecdotes;
            toldAnecdotes = new ArrayList<IAnecdote>();
        }

        return getRandomAnecdote();
    }

    /**
     * Возвращает случайный анекдот из набора еще не рассказанных анекдотов
     * и удаляет его из этого набора.
     * @return Случайный анекдот из набора еще не рассказанных анекдотов.
     */
    protected IAnecdote getRandomAnecdote() {
        var index = Randomizer.getRandomNumber(anecdotes.size());
        var anecdote = anecdotes.get(index);
        anecdotes.remove(index);
        toldAnecdotes.add(anecdote);
        return anecdote;
    }

}
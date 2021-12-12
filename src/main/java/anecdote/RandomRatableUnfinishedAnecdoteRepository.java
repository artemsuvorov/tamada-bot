package anecdote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Представляет собой репозиторий, который состоит из анекдотов
 * и который выдает их один за другим в случайной неповторяющейся последовательности.
 * Анекдоты такого репозитория поддерживают оценивание и добавление концовки.
 */
public class RandomRatableUnfinishedAnecdoteRepository extends RandomRatableAnecdoteRepository {

    public RandomRatableUnfinishedAnecdoteRepository(ArrayList<Anecdote> anecdotes, ArrayList<Anecdote> toldAnecdotes,
        ArrayList<Anecdote> bannedAnecdotes, Map<Rating, ArrayList<Anecdote>> ratedAnecdotes) {
        super(anecdotes, toldAnecdotes, bannedAnecdotes, ratedAnecdotes);
    }

    public RandomRatableUnfinishedAnecdoteRepository(AnecdotesConfiguration configuration) {
        this(configuration.Anecdotes, configuration.UnfinishedAnecdotes);
    }

    public RandomRatableUnfinishedAnecdoteRepository(String[] anecdotes, String[] unfinishedAnecdotes) {
        this(concatenateAnecdotes(anecdotes, unfinishedAnecdotes));
    }

    public RandomRatableUnfinishedAnecdoteRepository(ArrayList<Anecdote> anecdotes) {
        super(anecdotes);
    }

    /**
     * Объединяет массивы текстов обычных анекдотов и неоконченных анекдотов
     * в общий список анекдотов IAnecdote и возвращает его.
     * @param anecdotes массив анекдотов, подлежащих объединению.
     * @param unfinishedAnecdotes массив неоконченных анекдотов, подлежащих объединению.
     * @return Общий список из анекдотов IAnecdote.
     */
    private static ArrayList<Anecdote> concatenateAnecdotes(String[] anecdotes, String[] unfinishedAnecdotes) {
        var ratable = (ArrayList<Anecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
                .stream().map(x->(Anecdote)new RatableAnecdote(x)).collect(Collectors.toList());
        var unfinished = (ArrayList<Anecdote>)new ArrayList<String>(Arrays.asList(unfinishedAnecdotes))
                .stream().map(x->(Anecdote)new UnfinishedAnecdote(x)).collect(Collectors.toList());
        ratable.addAll(unfinished);
        return ratable;
    }

}
package anecdote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Представляет собой репозиторий, который состоит из анекдотов
 * и который выдает их один за другим в случайной неповторяющейся последовательности.
 * Анекдоты такого репозитория поддерживают оценивание и добавление концовки.
 */
public class RandomRatableUnfinishedAnecdoteRepository extends RandomRatableAnecdoteRepository {

    public RandomRatableUnfinishedAnecdoteRepository(AnecdotesConfiguration configuration) {
        this(configuration.Anecdotes, configuration.UnfinishedAnecdotes);
    }

    public RandomRatableUnfinishedAnecdoteRepository(String[] anecdotes, String[] unfinishedAnecdotes) {
        this(concatenateAnecdotes(anecdotes, unfinishedAnecdotes));
    }

    public RandomRatableUnfinishedAnecdoteRepository(ArrayList<IAnecdote> anecdotes) {
        super(anecdotes);
    }

    /**
     * Объединяет массивы текстов обычных анекдотов и неоконченных анекдотов
     * в общий список анекдотов IAnecdote и возвращает его.
     * @param anecdotes массив анекдотов, подлежащих объединению.
     * @param unfinishedAnecdotes массив неоконченных анекдотов, подлежащих объединению.
     * @return Общий список из анекдотов IAnecdote.
     */
    private static ArrayList<IAnecdote> concatenateAnecdotes(String[] anecdotes, String[] unfinishedAnecdotes) {
        var ratable = (ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(anecdotes))
                .stream().map(x->(IAnecdote)new RatableAnecdote(x)).collect(Collectors.toList());
        var unfinished = (ArrayList<IAnecdote>)new ArrayList<String>(Arrays.asList(unfinishedAnecdotes))
                .stream().map(x->(IAnecdote)new UnfinishedAnecdote(x)).collect(Collectors.toList());
        ratable.addAll(unfinished);
        return ratable;
    }

}
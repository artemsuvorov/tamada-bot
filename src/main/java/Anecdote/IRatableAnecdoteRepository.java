package Anecdote;

import java.util.ArrayList;

public interface IRatableAnecdoteRepository extends IAnecdoteRepository {

    ArrayList<IAnecdote> getFavorites();

}
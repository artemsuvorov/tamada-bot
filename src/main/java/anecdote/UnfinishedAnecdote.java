package anecdote;

/**
 * Представляет собой класс неоконченного анекдота,
 * который поддерживает возможность дописать ему концовку.
 */
public class UnfinishedAnecdote extends RatableAnecdote {

    private final static String gapIndicator = ".............";

    private String ending = "";

    public UnfinishedAnecdote(String anecdote, Rating rating, String ending) {
        super(anecdote, rating);
        this.ending = ending;
    }

    public UnfinishedAnecdote(String anecdote) {
        this(anecdote, Rating.None, null);
    }

    /**
     * Указывает, была ли уже дописана концовка этому анекдоту.
     * @return true, если анекдот имеет концовку, иначе false.
     */
    public boolean hasEnding() {
        return ending != null && !ending.isBlank();
    }

    /**
     * Возвращает дописанную концовку анекдота, если таковая есть.
     * По умолчанию, концовка анекдота - это пустая строка.
     * @return Возвращает концовку анекдота.
     */
    public String getEnding() {
        return this.ending;
    }

    /**
     * Дописывает указанную концовку этому анекдоту.
     * @param ending концовка, которая будет дописана анекдоту.
     */
    public void setEnding(String ending) {
        this.ending = ending;
    }

    /**
     * Возвращает содержание анекдота с его концовкой, если она есть.
     * @return Содержание анекдота в виде строки.
     */
    @Override
    public String getText() {
        if (hasEnding())
            return super.getText() + " " + ending;
        else
            return super.getText() + " " + gapIndicator;
    }

}
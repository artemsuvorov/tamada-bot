package anecdote;

import java.util.Objects;

/**
 * Представляет собой класс неоконченного анекдота,
 * который поддерживает возможность дописать ему концовку.
 */
public class UnfinishedAnecdote extends RatableAnecdote {

    private final static String gapIndicator = ".............";

    private long authorId;
    private String ending;

    private final TotalRating totalRating;

    public UnfinishedAnecdote(String anecdote, Rating rating,
        TotalRating totalRating, long authorId, String ending) {
        super(anecdote, rating);
        this.totalRating = totalRating;
        this.authorId = authorId;
        this.ending = ending;
    }

    public UnfinishedAnecdote(String anecdote) {
        this(anecdote, Rating.None, new TotalRating(), 0, null);
    }

    /**
     * Указывает, была ли уже дописана концовка этому анекдоту.
     * @return true, если анекдот имеет концовку, иначе false.
     */
    public boolean hasEnding() {
        return ending != null && !ending.isBlank();
    }

    // todo: add javadoc
    public long getAuthorId() {
        return authorId;
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
    public void setEnding(long authorId, String ending) {
        this.authorId = authorId;
        this.ending = ending;
    }

    // todo: add javadoc
    public double getTotalRating() {
        return totalRating.getAverageRating();
    }

    // todo: add javadoc
    @Override
    public void setRating(long senderId, Rating rating) {
        super.setRating(senderId, rating);
        totalRating.addUserRating(rating);
    }

    public String getTextWithoutEnding() {
        return super.getText();
    }

    /**
     * Возвращает содержание анекдота с его концовкой, если она есть.
     * @return Содержание анекдота в виде строки.
     */
    @Override
    public String getText() {
        if (hasEnding())
            return getTextWithoutEnding() + " " + ending;
        else
            return getTextWithoutEnding() + " " + gapIndicator;
    }

    // todo: add javadoc
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;
        UnfinishedAnecdote that = (UnfinishedAnecdote) other;
        return Objects.equals(ending, that.ending);
    }

    // todo: add javadoc
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authorId, ending);
    }

}
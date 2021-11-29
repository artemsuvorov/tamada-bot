package anecdote;

/**
 * Представляет собой класс неоконченного анекдота,
 * который поддерживает возможность дописать ему концовку.
 */
public class UnfinishedAnecdote extends RatableAnecdote {

    private String ending;
    private final String gapIndicator = ".............";

    public UnfinishedAnecdote(String anecdote) {
        super(anecdote);
    }

    /**
     * Указывает, была ли уже дописана концовка этому анекдоту.
     * @return true, если анекдот имеет концовку, иначе false.
     */
    public boolean hasEnding() {
        return ending != null && !ending.isBlank();
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
package anecdote;

public class TotalRating {

    private double ratingSum;
    private double ratingCount;

    public TotalRating(double sum, double count) {
        this.ratingSum = sum;
        this.ratingCount = count;
    }

    public TotalRating() {
        this(0,0);
    }

    public void addUserRating(Rating rating) {
        ratingSum += rating.getValue();
        ratingCount++;
    }

    public double getAverageRating() {
        return ratingSum / ratingCount;
    }

}

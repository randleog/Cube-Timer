import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * A score holds the number of points someone achieved and the time they did
 * it in. it allows the use of comparisons for the time and the points so
 * they can be sorted in the right order.
 *
 * @author William Randle
 */
public class Solve implements Comparable {
    public static final int PRECISION = 1000;

    private String scramble;
    private double time;
    private double timeOfCompletion;



    private String penalty;

    private static final String NO_PENALTY = "N/A";

    private static final String TIME_PENALTY = "+2";



    /**
     * constructs a score
     *
     * @param scramble String parsed username for the profile which got the time
     * @param timeOfCompletion   int number of points achieved
     */
    public Solve(String scramble, double timeOfCompletion, double time, String penalty) {
        scramble = scramble.replaceAll(" ", "_");
        this.scramble = scramble;
        this.timeOfCompletion = timeOfCompletion;
        this.time = time;

        this.penalty = penalty;
    }



    /**
     * @return int the number of time at which it was achieved for the score
     */
    public double getTimeOfCompletion() {
        return timeOfCompletion;
    }

    /**
     * @return double the time the score took to complete
     */
    public double getTime() {
        return time;
    }

    public boolean isEqual(Solve solve) {
        if (solve.getTime() == getTime() && solve.scramble.equals(scramble)) {
            return true;
        } else {
            return false;
        }
    }

    public void setPenalty(String penalty) {

        if (penalty.equals(TIME_PENALTY)) {
            if (!(this.penalty.equals(TIME_PENALTY))) {
                time = time+2;
            }
        }

        if (this.penalty.equals(TIME_PENALTY)) {
            if (!(penalty.equals(TIME_PENALTY))) {
                time = time-2;
            }
        }
        this.penalty = penalty;
    }

    public String getScramble() {
        String formatScramble = scramble.replaceAll("_", " ");

        return formatScramble;
    }


    /**
     * @return string the data for a score formatted in a presentable way
     */
    public String toString() {
        int lettersUnderLimit = 10 - scramble.length();
        String padding = "";
        for (int i = 0; i < lettersUnderLimit; i++) {
            padding = padding + " ";
        }
        BigDecimal accurateTime = new BigDecimal(timeOfCompletion);
        return String.format("%.3f", time) + " " + scramble + " " + accurateTime + " " + penalty;
    }

    /**
     * @return string the data for a score formatted in a presentable way
     */
    public String displayString() {
        int lettersUnderLimit = 10 - scramble.length();
        String padding = "";
        for (int i = 0; i < lettersUnderLimit; i++) {
            padding = padding + " ";
        }

        Date date = new Date((long) timeOfCompletion);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (penalty.equals(NO_PENALTY)) {
            return String.format("%.3f", time) + " | "
                    + getTimeFormat(calendar);
        } else {

            return String.format("%.3f", time) + " " + penalty + " | "
                    + getTimeFormat(calendar);
        }

    }

    public String getPenalty() {
           return penalty;
    }

    private String getTimeFormat(Calendar calendar) {
        return + calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + " "
                + calendar.get(Calendar.DAY_OF_MONTH)
                + "/" + calendar.get(Calendar.MONTH)
                + "/" + calendar.get(Calendar.YEAR);
    }

    public Calendar getCalendar() {
        Date date = new Date((long) timeOfCompletion);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    /**
     * @param score the score comparing against
     * @return int result points, sorting in ascending order of points
     */
    @Override
    public int compareTo(Object score) {
        Solve score1 = (Solve) score;
        return (int)(score1.getTimeOfCompletion() - this.timeOfCompletion);
    }

    /**
     * allows comparison for times in descending order
     */
    public static Comparator<Solve> ScoreTimeComparator

            = new Comparator<Solve>() {

        /**
         * compares two times together and gives comparison in descending order
         * @param score1 first score to compare against
         * @param score2 second score to compare agianst
         * @return intcomparison result time sorting in descending order
         */
        public int compare(Solve score1, Solve score2) {


            return (int) (score1.getTime() * Solve.PRECISION - score2.getTime()
                    * Solve.PRECISION);

        }

    };
}

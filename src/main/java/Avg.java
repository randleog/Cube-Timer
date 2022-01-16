
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Avg {

    private static final int LARGE_NUMBER = 999999999;
    public ArrayList<Solve> solves = new ArrayList<>();

    private Calendar time;


    public Avg() {

    }



    public void setTime(Calendar time) {
        this.time =time;
    }

    public Calendar getTime() {
        return time;
    }

    public boolean isEqual (Avg comp) {
        ArrayList<String> scrambles = new ArrayList<>();
        for (Solve solve : solves) {
            scrambles.add( solve.getScramble());
        }
        for (Solve solve : comp.solves) {
            if (scrambles.contains(solve.getScramble())) {
                return true;
            }

        }
        return false;
    }

    public double getAverage() {


        if (solves.size() >= 3) {
            double total = 0;
            double smallest = LARGE_NUMBER;
            double largest = 0;

            for (Solve solve : solves) {
                if (solve.getTime() < smallest) {
                    smallest = solve.getTime();
                }

                if (solve.getTime() > largest) {
                    largest = solve.getTime();
                }
                total = total + solve.getTime();
            }
            total = total - smallest;
            total = total - largest;

            return total / (solves.size()-2);
        } else {
            return 0;
        }

    }

    public String toString() {
        String times= "";



        for(Solve solve : solves) {
            if (solve.isEqual(Collections.max(solves, Solve.ScoreTimeComparator))) {
                times += "(" + String.format("%.3f", solve.getTime()) + "), ";
            } else if (solve.isEqual(Collections.min(solves, Solve.ScoreTimeComparator))) {
                times += "(" + String.format("%.3f", solve.getTime()) + "), ";
            } else {

                times += String.format("%.3f", solve.getTime()) + ", ";
            }
        }

        return times;
    }






}

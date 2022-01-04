import java.util.ArrayList;
import java.util.Calendar;

public class Avg implements Comparable {

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


    @Override
    public int compareTo(Object avg) {
        Avg avg1 = (Avg) avg;
        return (int)(avg1.getAverage() - this.getAverage());
    }

}
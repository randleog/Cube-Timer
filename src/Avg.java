import java.util.ArrayList;

public class Avg implements Comparable {

    private static final int LARGE_NUMBER = 999999999;
    public ArrayList<Double> solves = new ArrayList<>();


    public Avg() {

    }

    public double getAverage() {


        if (solves.size() >= 3) {
            double total = 0;
            double smallest = LARGE_NUMBER;
            double largest = 0;

            for (double solve : solves) {
                if (solve < smallest) {
                    smallest = solve;
                }

                if (solve > largest) {
                    largest = solve;
                }
                total = total + solve;
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

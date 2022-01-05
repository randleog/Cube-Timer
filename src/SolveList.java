import javafx.scene.text.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * this class allows access to and updating of the top 10 leaderboards
 * for points and for time of completion for each level.
 *
 * @author William Randle
 */
public class SolveList {
    private static final String OPEN_FILE_ERROR = "Could not find ";
    private static final String LEADER_BOARD_PATH = "res\\";

    private static final int LARGE_NUMBER = 999999999;

    private static ArrayList<Solve> solves= new ArrayList<>();


    private static double dailyAvg = 0;
    private static double weeklyAvg = 0;
    private static double monthlyAvg = 0;



    private static ArrayList<Avg> ao5s= new ArrayList<>();

    private static ArrayList<Avg> ao12s= new ArrayList<>();

    private static ArrayList<Avg> ao50s= new ArrayList<>();

    private static ArrayList<Solve> pbs= new ArrayList<>();

    private static ArrayList<Solve> ao5pbs= new ArrayList<>();

    private static ArrayList<Solve> ao12pbs= new ArrayList<>();

    public static void loadSolves() {
        updateTable(LEADER_BOARD_PATH + "solves.txt", getSolves("solves.txt"));

        loadMoreStats();
    }

    public static void loadMoreStats() {
        loadPbsAo12();
        loadPbsAo5();
        loadPbs();
    }

    public static ArrayList<Solve> getao5s() {
        ArrayList<Solve> tempao5s = new ArrayList<>();

        for (Avg avg : ao5s) {
            tempao5s.add(new Solve (avg.toString(), avg.getTime().getTimeInMillis(), avg.getAverage(), "N/A"));
        }



        return tempao5s;
    }

    public static ArrayList<Solve> getao12s() {
        ArrayList<Solve> tempao12s = new ArrayList<>();

        for (Avg avg : ao12s) {
            tempao12s.add(new Solve (avg.toString(), avg.getTime().getTimeInMillis(), avg.getAverage(), "N/A"));
        }


        return tempao12s;
    }

    public static Avg getpbAo5() {


        double smallestVal = LARGE_NUMBER;
        Avg smallest = new Avg();



        for (Avg avg : ao5s) {
            if (avg.getAverage() < smallestVal) {
                smallestVal = avg.getAverage();
                smallest = avg;
            }
        }
        return smallest;

    }

    public static Avg getpbAo12() {


        double smallestVal = LARGE_NUMBER;
        Avg smallest = new Avg();



        for (Avg avg : ao12s) {
            if (avg.getAverage() < smallestVal) {
                smallestVal = avg.getAverage();
                smallest = avg;
            }
        }
        return smallest;

    }

    public static double getpbAo50() {


        double smallest = LARGE_NUMBER;


        for (Avg avg : ao50s) {
            if (avg.getAverage() < smallest) {
                smallest = avg.getAverage();
            }
        }
        return smallest;

    }

    /**
     * if the score is in the top 10 of the leaderboard for times or points,
     * add it to that leaderboard.
     *
     * @param newSolve  Score to add to the leaderboard/s
     * @param tableName the level the score was achieved on
     */
    public static void addSolve(String tableName, Solve newSolve) {
        createFileIfNotExists(tableName);

        //now add to the two tables for time and score top 10:

        //check if the score is in the top 10 for time
        addSolve(newSolve, tableName);


    }

    public static int getSolveCount() {
        return solves.size();
    }


    /**
     * adds a score to the top 10 leaderboard of times
     * (if they are in the top 10)
     *
     * @param newScore  Score to add to the leaderboard
     * @param tableName level the score was achieved on
     */
    private static void addSolve(Solve newScore, String tableName) {
        String timeBoardFile = LEADER_BOARD_PATH + tableName;
        ArrayList<Solve> scores = new ArrayList<>();
        scores.add(newScore);
        scores.addAll(getSolves(tableName));

        updateTable(timeBoardFile, scores);
        //appendTable(timeBoardFile, newScore);

    }


    private static void appendTable(String tableName, Solve solve) {
        try {


            Scanner scanner = new Scanner( new File(tableName) );
            String text = scanner.useDelimiter("\\A").nextLine();
            scanner.close();

            FileWriter writer = new FileWriter(tableName);
            writer.write(solve.toString() + "\n" + text);


            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read table.");
            e.printStackTrace();
        }

    }

    /**
     * Updates the table to show the current scores for that table.
     *
     * @param tableName String the file path for the table to update
     * @param scores    Arraylist of scores to set the table to
     */
    private static void updateTable(String tableName, ArrayList<Solve> scores) {
        SolveList.solves = scores;
        int i = 0;
        Avg bufferAo5 = new Avg();
        Avg bufferAo12 = new Avg();
        Avg bufferAo50 = new Avg();



        try {
            FileWriter writer = new FileWriter(tableName);
            for (Solve score : scores) {
                if (score.getTime() < 0.5) {

                } else {
                    writer.append(score.toString() + "\n");

                    i++;

                    bufferAo5.solves.add(score);
                    if (bufferAo5.solves.size() > 5) {
                        bufferAo5.solves.remove(0);
                    }

                    if (i > 4) {
                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo5.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao5s.add(tempAvg);
                    }


                    bufferAo12.solves.add(score);
                    if (bufferAo12.solves.size() > 12) {
                        bufferAo12.solves.remove(0);
                    }

                    if (i > 11) {
                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo12.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao12s.add(tempAvg);
                    }

                    bufferAo50.solves.add(score);
                    if (bufferAo50.solves.size() > 50) {
                        bufferAo50.solves.remove(0);
                    }

                    if (i > 49) {
                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo50.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao50s.add(tempAvg);
                    }
                }

            }



            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read table.");
            e.printStackTrace();
        }

    }

    private static void loadPbs() {
        pbs = new ArrayList<>();

        double lastPb = LARGE_NUMBER;


        if (solves.size() > 0) {
            for (int i = solves.size() - 1; i > 0; i--) {
                if (solves.get(i).getTime() < lastPb) {
                    lastPb = solves.get(i).getTime();
                    pbs.add(solves.get(i));
                }

            }
        }

        Collections.reverse(pbs);
    }

    public static ArrayList<Solve> getPbs() {


        return pbs;
    }

    private static void loadPbsAo5() {
        ao5pbs = new ArrayList<>();

        double lastPb = LARGE_NUMBER;


        if (ao5s.size() > 0) {
            for (int i = ao5s.size() - 1; i > 0; i--) {
                if (ao5s.get(i).getAverage() < lastPb) {
                    lastPb = ao5s.get(i).getAverage();
                    ao5pbs.add(new Solve (ao5s.get(i).toString(), ao5s.get(i).getTime().getTimeInMillis(), ao5s.get(i).getAverage(), "N/A"));
                }

            }
        }

        Collections.reverse(ao5pbs);
    }

    public static ArrayList<Solve> getPbsAo5() {

        return ao5pbs;
    }

    private static void loadPbsAo12() {
        ao12pbs = new ArrayList<>();

        double lastPb = LARGE_NUMBER;


        if (ao12s.size() > 0) {
            for (int i = ao12s.size() - 1; i > 0; i--) {
                if (ao12s.get(i).getAverage() < lastPb) {
                    lastPb = ao12s.get(i).getAverage();
                    ao12pbs.add(new Solve (ao12s.get(i).toString(), ao12s.get(i).getTime().getTimeInMillis(), ao12s.get(i).getAverage(), "N/A"));
                }

            }
        }

        Collections.reverse(ao12pbs);
    }

    public static ArrayList<Solve> getPbsAo12() {

        return ao12pbs;
    }



    private static ArrayList<Solve> sortByTime(ArrayList<Solve> scores) {

        Collections.sort(scores, Solve.ScoreTimeComparator);

        return scores;
    }

    /**
     * returns an arraylist of scores sorted in descending order of time
     * should only return the top 10
     *
     * @param scores Arraylist of scores to be sorted
     * @return Arraylist of sorted scores
     */
    private static ArrayList<Solve> sortTimes(ArrayList<Solve> scores) {


        Collections.sort(scores);


        return scores;
    }


    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getSolves(String levelName) {


        ArrayList<Solve> solves = new ArrayList<>();
        try {
            Scanner in = openLeaderboard(levelName);


            while (in.hasNextLine()) {
                double time = in.nextDouble();
                String scramble = in.next();
                double timeOfCompletion = in.nextLong();
                String penalty = in.next();
                Solve score = new Solve(scramble, timeOfCompletion, time, penalty);

                score.setPenalty(penalty);

                solves.add(score);
                in.nextLine();
            }
            in.close();

                return sortTimes(solves);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to parse leaderboard " + levelName);
            return null;
        }

    }



    /**
     * creates a file if one doesnt already exist, so there will always be
     * highscore tables for every level
     *
     * @param level String the level for which a table needs to be made
     */
    private static void createFileIfNotExists(String level) {

        try {
            File file = new File(LEADER_BOARD_PATH + level);
            if (file.createNewFile()) {
                System.out.println("creating file " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getAverage(int number, String levelName) {

        ArrayList<Solve> solves = SolveList.solves;
        if (solves.size() >= number) {
            double total = 0;
            double smallest = LARGE_NUMBER;
            double largest = 0;

            for (int i = 0; i < number; i++) {
                if (solves.get(i).getTime() < smallest) {
                    smallest = solves.get(i).getTime();
                }

                if (solves.get(i).getTime() > largest) {
                    largest = solves.get(i).getTime();
                }
                total = total + solves.get(i).getTime();
            }
            total = total - smallest;
            total = total - largest;

            return total / (number-2);
        } else {
            return 0;
        }

    }



    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getDaily(String levelName) {
        ArrayList<Solve> solvesOld = SolveList.solves;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if ((calendar.get(Calendar.DAY_OF_YEAR) == score.getCalendar().get(Calendar.DAY_OF_YEAR))
                    && (calendar.get(Calendar.YEAR) == score.getCalendar().get(Calendar.YEAR))) {
                solves.add(score);
            }
        }

        return sortByTime(solves);

    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getWeekly(String levelName) {


        ArrayList<Solve> solvesOld = SolveList.solves;;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if ((calendar.get(Calendar.WEEK_OF_YEAR) == score.getCalendar().get(Calendar.WEEK_OF_YEAR))
                    && (calendar.get(Calendar.YEAR) == score.getCalendar().get(Calendar.YEAR))) {
                solves.add(score);
            }
        }

        return sortByTime(solves);


        }




    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getMonthly(String levelName) {

        ArrayList<Solve> solvesOld = SolveList.solves;;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if ((calendar.get(Calendar.MONTH) == score.getCalendar().get(Calendar.MONTH))
                    && (calendar.get(Calendar.YEAR) == score.getCalendar().get(Calendar.YEAR))) {
                solves.add(score);
            }
        }

        return sortByTime(solves);

    }


    public static ArrayList<Solve> getAllTime() {

        ArrayList<Solve> solvesOld = SolveList.solves;;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {

            solves.add(score);

        }
        return sortByTime(solves);

    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void delete(Solve solve, String levelName) {
        ArrayList<Solve> solvesOld = SolveList.solves;;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {
            if (score.isEqual(solve)) {

            } else {
                solves.add(score);
            }
        }

        updateTable(LEADER_BOARD_PATH + levelName , solves);


    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void setPenalty(Solve solve, String penalty, String levelName) {
        ArrayList<Solve> solvesOld = SolveList.solves;;
        ArrayList<Solve> solves = new ArrayList();
        for (Solve score : solvesOld) {
            if (score.isEqual(solve)) {
                if (score.getPenalty().equals(penalty)) {
                    score.setPenalty("N/A");
                } else {
                    score.setPenalty(penalty);
                }

            }
            solves.add(score);
        }

        updateTable(LEADER_BOARD_PATH + levelName , solves);

    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getLastN(String levelName, int lower, int n) {
        ArrayList<Solve> solvesOld = SolveList.solves;
        ArrayList<Solve> solves = new ArrayList();

        for (int i = lower; i < n; i++) {
            solves.add(solvesOld.get(i));


        }
        return sortTimes(solves);
    }

    /**
     * gets the scanner for the table required, so it can be read from.
     *
     * @param level String name of the levels highscores we are reading
     * @return
     */
    private static Scanner openLeaderboard(String level) {
        File inputFile = new File(LEADER_BOARD_PATH + level);


        createFileIfNotExists(level);

        Scanner in = null;
        try {
            in = new Scanner(inputFile);

        } catch (FileNotFoundException e) {
            System.out.println(OPEN_FILE_ERROR + level);
            System.exit(0);
        }
        return in;
    }


}

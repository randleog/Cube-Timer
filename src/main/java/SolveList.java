
import java.io.*;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * todo: make a new json file for each day.
 * then, have a main json file which controlls the list of all days accessable.
 * for instance: you would store the day in which you got a certain pb.
 *
 * at run, all the days are compiled to account for pbs and things, however if
 * a pbs file exists, it is assumed that the calculation only needs to be
 * tweaked, and thus not everything is compiled.
 *
 * todo: leaderboard for beast each day
 *
 * todo: for each solve, hold the last 5 solves in it, and an additional comparator for averages.
 *
 * todo: add standard deviation,
 *
 * todo: line graph representing all time progression,
 * and a bar graph showing all solves for a given time period
 *
 * todo: colour coded based on the number of solves per day (in the vain of streaks)
 *
 * todo: canvas within the vboxes!
 *
 * @author William Randle
 */
public class SolveList {
    private static final String OPEN_FILE_ERROR = "Could not find ";
    private static final String LEADER_BOARD_PATH = "res\\";

    private static final int LARGE_NUMBER = 999999999;

    private static JSONArray solvesJ = new JSONArray();

    private static double dailyAvg = 0;
    private static double weeklyAvg = 0;
    private static double monthlyAvg = 0;

    private static int dailyN = 0;
    private static int weeklyN = 0;
    private static int monthlyN = 0;

    public static final int streakCount = 50;
    public static final int streakThreshold = 10;
    public static int streakDays = 0;

    private static int currentStreak = 0;
    private static int bestStreak = 0;

    private static int shortestScramble = 99999999;

    private static ArrayList<Avg> ao5s = new ArrayList<>();

    private static ArrayList<Avg> ao12s = new ArrayList<>();

    private static ArrayList<Avg> ao50s = new ArrayList<>();

    private static ArrayList<Solve> pbs = new ArrayList<>();

    private static ArrayList<Solve> ao5pbs = new ArrayList<>();

    private static ArrayList<Solve> ao12pbs = new ArrayList<>();

    private static ArrayList<Solve> ao50pbs = new ArrayList<>();


    private static ArrayList<Solve> monthly = new ArrayList<>();

    private static ArrayList<Solve> weekly = new ArrayList<>();

    private static ArrayList<Solve> daily = new ArrayList<>();

    private static ArrayList<Solve> days= new ArrayList<>();
    private static ArrayList<Solve> weeks= new ArrayList<>();

    private static ArrayList<Solve> monthDensity = new ArrayList<>();

    private static double total = 0;

    private static int mostInDay = 0;

    private static int mostInHour = 0;

    private static long longestTimeWithout = 0;

    private static Calendar bestStreakTime = Calendar.getInstance();

    private static Calendar firstSolve = Calendar.getInstance();

    private static Calendar longestTime = Calendar.getInstance();

    private static Calendar mostDay = Calendar.getInstance();
    private static Calendar mostHour = Calendar.getInstance();

    public static int getStreakDays() {
        return streakDays;
    }

    public static int getMostInDay() {
        return mostInDay;
    }

    public static int getMostInHour() {
        return mostInHour;
    }

    public static Calendar getMostDay() {
        return mostDay;
    }

    public static Calendar getMostHour() {
        return mostHour;
    }

    public static ArrayList<Solve> getWeeks() {
        return weeks;
    }

    public static void loadSolves() {
        loadSolves("solves");
        loadPbsAo12();
        loadPbsAo5();
        loadPbsAo50();
        loadPbs();

        loadMonthly("solves");
    }


    public static void loadMoreStats() {
        loadPbsAo12();
        loadPbsAo5();
        loadPbs();
    }


    public static Solve getpbAo5() {

     
        if (ao5pbs.size() < 1) {
            return new Solve( "no solves", 0, 0, "N/A");
        }
        return ao5pbs.get(0);

    }

    public static Solve getpbAo12() {


        if (ao12pbs.size() < 1) {
            return new Solve( "no solves", 0, 0, "N/A");
        }
        return ao12pbs.get(0);

    }

    public static Solve getpbAo50() {


        if (ao50pbs.size() < 1) {
            return new Solve( "no solves", 0, 0, "N/A");
        }
        return ao50pbs.get(0);


    }

    public static int getMonthlyD(){
        return monthDensity.size();
    }

    public static double getAverage() {
        return total / solvesJ.size();
    }

    public static double getTotal() {
        return total;
    }

    public static int getDailyN(){
        return dailyN;
    }
    public static int getWeeklyN(){
        return weeklyN;
    }
    public static int getMonthlyN(){
        return monthlyN;
    }
    public static void addSolve(Solve solve) {
        JSONArray jsonSolve = new JSONArray();
        jsonSolve.add(getSolveCount());
        jsonSolve.add(solve.getTime());
        jsonSolve.add(solve.getScramble());
        jsonSolve.add(solve.getCalendar().getTimeInMillis());
        jsonSolve.add(solve.getPenalty());
        jsonSolve.add(solve.getaverageN());


        solvesJ.add(jsonSolve);

        update();
    }

    private static void update() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "solve");


        jsonObject.put("solves", solvesJ);

        try {
            FileWriter file = new FileWriter("solves.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }
    public static int getSolveCount() {
        return solvesJ.size();
    }



    private static void loadPbs() {
        pbs = new ArrayList<>();

        double lastPb = LARGE_NUMBER;


        if (solvesJ.size() > 0) {
            for (int i = 0; i < solvesJ.size(); i++) {
                if (getTime((JSONArray) solvesJ.get(i)) < lastPb) {
                    lastPb = getTime((JSONArray) solvesJ.get(i));
                    pbs.add(getSolve((JSONArray) solvesJ.get(i)));
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
            for (int i = 0; i < ao5s.size(); i++) {
                if (ao5s.get(i).getAverage() < lastPb) {
                    lastPb = ao5s.get(i).getAverage();
                    ao5pbs.add(new Solve( ao5s.get(i).toString(), ao5s.get(i).getTime().getTimeInMillis(), ao5s.get(i).getAverage(), "N/A"));
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
            for (int i = 0; i < ao12s.size(); i++) {
                if (ao12s.get(i).getAverage() < lastPb) {
                    lastPb = ao12s.get(i).getAverage();
                    ao12pbs.add(new Solve( ao12s.get(i).toString(), ao12s.get(i).getTime().getTimeInMillis(), ao12s.get(i).getAverage(), "N/A"));
                }

            }
        }
        Collections.reverse(ao12pbs);

    }

    private static void loadPbsAo50() {
        ao50pbs = new ArrayList<>();

        double lastPb = LARGE_NUMBER;


        if (ao50s.size() > 0) {
            for (int i = 0; i < ao50s.size(); i++) {
                if (ao50s.get(i).getAverage() < lastPb) {
                    lastPb = ao50s.get(i).getAverage();
                    ao50pbs.add(new Solve( ao50s.get(i).toString(), ao50s.get(i).getTime().getTimeInMillis(), ao50s.get(i).getAverage(), "N/A"));
                }

            }
        }
        Collections.reverse(ao50pbs);

    }

    public static ArrayList<Solve> getPbsAo12() {

        return ao12pbs;
    }

    public static ArrayList<Solve> getPbsAo50() {

        return ao50pbs;
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


    public static ArrayList<Solve> getMonthDensity() {



        return monthDensity;
    }


    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getSolves(String levelName) {
        total = 0;
        Avg bufferAo5 = new Avg();
        Avg bufferAo12 = new Avg();
        Avg bufferAo50 = new Avg();
        ArrayList<Solve> solves = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("solves.json"));

            JSONArray solvesJ = (JSONArray) (jsonObject.get("solves"));


            for (int i = 0; i < solvesJ.size(); i++) {
                JSONArray solve = (JSONArray) (solvesJ.get(i));

                double time = (double) solve.get(1);
                total = total + time;

                String scramble = (String) solve.get(2);
                long timeOfCompletion = (long) solve.get(3);

                String penalty = (String) solve.get(4);

                String average = (String) solve.get(5);


                Solve score = new Solve(scramble, timeOfCompletion, time, penalty);

                bufferAo5.solves.add(score);
                if (average.contains(";5.")) {
                    score.setFirstN(5, true);
                    if (bufferAo5.solves.size() >= 5) {

                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo5.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao5s.add(tempAvg);

                    } else {
                        System.out.println(score.toString()+ " no 5");

                    }
                    bufferAo5 = new Avg();

                }


                bufferAo12.solves.add(score);
                if (average.contains(";12.")) {
                    score.setFirstN(12, true);
                    if (bufferAo12.solves.size() >= 12) {

                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo12.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao12s.add(tempAvg);

                    } else {
                        System.out.println(score.toString()+ " no 12");

                    }
                    bufferAo12 = new Avg();

                }


                bufferAo50.solves.add(score);
                if (average.contains(";50.")) {
                    score.setFirstN(50, true);
                    if (bufferAo50.solves.size() >= 50) {

                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo50.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao50s.add(tempAvg);

                    } else {
                        System.out.println(score.toString() + " no 50");

                    }
                    bufferAo5 = new Avg();

                }



                solves.add(score);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



           // createJSON(solves);

            return sortTimes(solves);

    }

    public static ArrayList<Solve> getdays() {
        return days;
    }

    public static long getLongestTimeWithout() {
        return longestTimeWithout;
    }

    public static Calendar getLongestTime() {
        return longestTime;
    }

    public static Calendar getFirstSolve() {
        return firstSolve;
    }

    public static Calendar getBestStreakTime() {
        return bestStreakTime;
    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void loadSolvesOld(String levelName) {
        total = 0;

        double monthTotal = 0.01;

        double weekTotal =0.01;
        int currentWeek = 0;
        int currentMonth = 0;

        long daysWithout = 0;

        int dailyMost = 0;
        int hourlyMost = 0;

        int streak = 0;

        double totalDayTime = 0;

        Calendar lastSolveC = Calendar.getInstance();

        Avg bufferAo5 = new Avg();
        Avg bufferAo12 = new Avg();
        Avg bufferAo50 = new Avg();
        ArrayList<Solve> solves = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("solves.json"));

            JSONArray solvesJ = (JSONArray) (jsonObject.get("solves"));


            for (int i = 0; i < solvesJ.size(); i++) {
                JSONArray solve = (JSONArray) (solvesJ.get(i));

                double time = (double) solve.get(1);


                total = total + time;
                String scramble = (String) solve.get(2);
                String scrambleLength = scramble.replaceAll("2", "").replaceAll(" ", "").replaceAll("_", "").replaceAll("'", "");
                if (scrambleLength.length() < shortestScramble) {

                    shortestScramble = scrambleLength.length();
                }

                long timeOfCompletion = (long) solve.get(3);

                String penalty = (String) solve.get(4);

                String average = (String) solve.get(5);


                Solve score = new Solve(scramble, timeOfCompletion, time, penalty);

                if (solves.size() == 0) {
                    firstSolve = score.getCalendar();
                }


                if ((score.getCalendar().get(Calendar.MONTH) == lastSolveC.get(Calendar.MONTH))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {
                    currentMonth++;
                    monthTotal+=score.getTime();

                } else {
                    monthDensity.add(new Solve("average: " + String.format("%.3f"
                            , monthTotal/currentMonth), score.getCalendar().getTimeInMillis(), currentMonth, "-"));
                    currentMonth= 1;
                    monthTotal = 0.01;
                }

                if ((score.getCalendar().get(Calendar.WEEK_OF_YEAR) == lastSolveC.get(Calendar.WEEK_OF_YEAR))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {
                    currentWeek++;
                    weekTotal+=score.getTime();

                } else {

                    if (monthTotal/currentMonth > 1) {
                        weeks.add(new Solve("weekly ", score.getCalendar().getTimeInMillis(), monthTotal / currentMonth, "-"));
                    }
                    currentMonth= 1;
                    monthTotal = 0.01;
                }


                daysWithout = score.getCalendar().getTimeInMillis()-lastSolveC.getTimeInMillis();
                if (daysWithout > longestTimeWithout) {
                    longestTimeWithout = daysWithout;
                    longestTime = lastSolveC;
                }


                if ((score.getCalendar().get(Calendar.DAY_OF_YEAR) == lastSolveC.get(Calendar.DAY_OF_YEAR))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {

                    if (dailyMost > mostInDay) {
                        mostInDay = dailyMost;
                        mostDay = score.getCalendar();
                    }

                    totalDayTime+=score.getTime();

                    if (dailyMost == streakCount) {
                        streak++;
                        streakDays++;
                        if (streak > bestStreak) {
                            bestStreak = streak;
                            bestStreakTime = score.getCalendar();
                        }
                    }
                    dailyMost++;
                } else {
                    if (totalDayTime > 1) {
                        if (dailyMost < streakCount) {
                            System.out.println(totalDayTime);
                            days.add(new Solve("day", score.getCalendar().getTimeInMillis(), totalDayTime / (dailyMost + 1), "-"));
                            streak = 0;
                        }
                    }
                    totalDayTime = 0.1;
                    dailyMost = 1;

                }

                if ((score.getCalendar().get(Calendar.HOUR_OF_DAY) == lastSolveC.get(Calendar.HOUR_OF_DAY))
                        && (score.getCalendar().get(Calendar.DAY_OF_YEAR) ==lastSolveC.get(Calendar.DAY_OF_YEAR))) {
                    hourlyMost++;
                    if (hourlyMost > mostInHour) {
                        mostInHour = hourlyMost;
                        mostHour = score.getCalendar();
                    }
                } else {
                    hourlyMost = 0;
                }

                lastSolveC = score.getCalendar();

                bufferAo5.solves.add(score);
                if (bufferAo5.solves.size() > 4) {
                    score.setFirstN(5, true);
                    if (bufferAo5.solves.size() >= 5) {


                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo5.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao5s.add(tempAvg);

                    } else {
                        System.out.println(score.toString()+ " no 5");

                    }
                    bufferAo5 = new Avg();

                }


                bufferAo12.solves.add(score);
                if (bufferAo12.solves.size() > 11) {
                    score.setFirstN(12, true);
                    if (bufferAo12.solves.size() >= 12) {

                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo12.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao12s.add(tempAvg);

                    } else {
                        System.out.println(score.toString()+ " no 12");

                    }
                    bufferAo12 = new Avg();

                }


                bufferAo50.solves.add(score);
                if (bufferAo50.solves.size() > 49) {
                    score.setFirstN(50, true);
                    if (bufferAo50.solves.size() >= 50) {

                        Avg tempAvg = new Avg();
                        tempAvg.solves.addAll(bufferAo50.solves);
                        tempAvg.setTime(score.getCalendar());
                        ao50s.add(tempAvg);

                    } else {
                        System.out.println(score.toString() + " no 50");

                    }
                    bufferAo50 = new Avg();

                }


                solves.add(score);
            }

            currentStreak = streak;

            SolveList.solvesJ = arrayToJson(sortTimes(solves));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void loadSolves(String levelName) {
        total = 0;

        double monthTotal = 0.01;

        double weekTotal =0.01;
        int currentWeek = 0;
        int currentMonth = 0;

        long daysWithout = 0;

        int dailyMost = 0;
        int hourlyMost = 0;

        int streak = 0;

        double totalDayTime = 0;

        Calendar lastSolveC = Calendar.getInstance();

        Avg bufferAo5 = new Avg();
        Avg bufferAo12 = new Avg();
        Avg bufferAo50 = new Avg();
        ArrayList<Solve> solves = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("solves.json"));

            JSONArray solvesJ = (JSONArray) (jsonObject.get("solves"));


            for (int i = 0; i < solvesJ.size(); i++) {
                JSONArray solve = (JSONArray) (solvesJ.get(i));

                double time = (double) solve.get(1);


                total = total + time;
                String scramble = (String) solve.get(2);
                String scrambleLength = scramble.replaceAll("2", "").replaceAll(" ", "").replaceAll("_", "").replaceAll("'", "");
                if (scrambleLength.length() < shortestScramble) {

                    shortestScramble = scrambleLength.length();
                }

                long timeOfCompletion = (long) solve.get(3);

                String penalty = (String) solve.get(4);

                String average = (String) solve.get(5);


                Solve score = new Solve(scramble, timeOfCompletion, time, penalty);

                if (solves.size() == 0) {
                    firstSolve = score.getCalendar();
                }


                if ((score.getCalendar().get(Calendar.MONTH) == lastSolveC.get(Calendar.MONTH))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {
                    currentMonth++;
                    monthTotal+=score.getTime();

                } else {
                    monthDensity.add(new Solve("average: " + String.format("%.3f"
                            , monthTotal/currentMonth), score.getCalendar().getTimeInMillis(), currentMonth, "-"));
                    currentMonth= 1;
                    monthTotal = 0.01;
                }

                if ((score.getCalendar().get(Calendar.WEEK_OF_YEAR) == lastSolveC.get(Calendar.WEEK_OF_YEAR))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {
                    currentWeek++;
                    weekTotal+=score.getTime();

                } else {

                    if (monthTotal/currentMonth > 1) {
                        weeks.add(new Solve("weekly ", score.getCalendar().getTimeInMillis(), monthTotal / currentMonth, "-"));
                    }
                    currentMonth= 1;
                    monthTotal = 0.01;
                }


                daysWithout = score.getCalendar().getTimeInMillis()-lastSolveC.getTimeInMillis();
                if (daysWithout > longestTimeWithout) {
                    longestTimeWithout = daysWithout;
                    longestTime = lastSolveC;
                }


                if ((score.getCalendar().get(Calendar.DAY_OF_YEAR) == lastSolveC.get(Calendar.DAY_OF_YEAR))
                        && (score.getCalendar().get(Calendar.YEAR) ==lastSolveC.get(Calendar.YEAR))) {

                    if (dailyMost > mostInDay) {
                        mostInDay = dailyMost;
                        mostDay = score.getCalendar();
                    }

                    totalDayTime+=score.getTime();

                    if (dailyMost == streakCount) {
                        streak++;
                        streakDays++;
                        if (streak > bestStreak) {
                            bestStreak = streak;
                            bestStreakTime = score.getCalendar();
                        }
                    }
                    dailyMost++;
                } else {
                    if (totalDayTime > 1) {
                        if (dailyMost < streakCount) {
                            System.out.println(totalDayTime);
                            days.add(new Solve("day", score.getCalendar().getTimeInMillis(), totalDayTime / (dailyMost + 1), "-"));
                            streak = 0;
                        }
                    }
                    totalDayTime = 0.1;
                    dailyMost = 1;

                }

                if ((score.getCalendar().get(Calendar.HOUR_OF_DAY) == lastSolveC.get(Calendar.HOUR_OF_DAY))
                        && (score.getCalendar().get(Calendar.DAY_OF_YEAR) ==lastSolveC.get(Calendar.DAY_OF_YEAR))) {
                    hourlyMost++;
                    if (hourlyMost > mostInHour) {
                        mostInHour = hourlyMost;
                        mostHour = score.getCalendar();
                    }
                } else {
                    hourlyMost = 0;
                }

                lastSolveC = score.getCalendar();

                if (solves.size() > 4) {
                    Avg ao5 = getAverageConcurrent(5, solves);
                    ao5.setTime(score.getCalendar());
                    if (!(ao5.getTime() == null)) {
                        ao5s.add(ao5);
                    }
                }

                if (solves.size() > 11) {
                    Avg ao12 = getAverageConcurrent(12, solves);
                    ao12.setTime(score.getCalendar());
                    if (!(ao12.getTime() == null)) {
                        ao12s.add(ao12);
                    }
                }

                if (solves.size() > 49) {
                    Avg ao50 = getAverageConcurrent(50, solves);
                    ao50.setTime(score.getCalendar());
                    if (!(ao50.getTime() == null)) {
                        ao50s.add(ao50);
                    }
                }

                solves.add(score);
            }

            currentStreak = streak;

            SolveList.solvesJ = arrayToJson(sortTimes(solves));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static JSONArray arrayToJson(ArrayList<Solve> solves) {
        JSONArray jsonArray = new JSONArray();
        for (int i =0; i < solves.size(); i++) {
            JSONArray jsonSolve = new JSONArray();
            jsonSolve.add(i);
            jsonSolve.add(solves.get(i).getTime());
            jsonSolve.add(solves.get(i).getScramble());
            jsonSolve.add(solves.get(i).getCalendar().getTimeInMillis());
            jsonSolve.add(solves.get(i).getPenalty());
            jsonSolve.add(solves.get(i).getaverageN());

            jsonArray.add(jsonSolve);
        }

        return  jsonArray;
    }

    private static JSONArray solveToJSON(Solve solve) {
        int j =0;
        for (int i = 0; i < solvesJ.size(); i++) {
            if (getSolve((JSONArray) solvesJ.get(i)).isEqual(solve)) {
                j=i;
            }
        }

            JSONArray jsonSolve = new JSONArray();

            jsonSolve.add(j);
            jsonSolve.add(solve.getTime());
            jsonSolve.add(solve.getScramble());
            jsonSolve.add(solve.getCalendar().getTimeInMillis());
            jsonSolve.add(solve.getPenalty());
            jsonSolve.add(solve.getaverageN());




        return  jsonSolve;
    }


    private static Solve getSolve(JSONArray solve) {
        int index = (int) solve.get(0);
        double time = (double) solve.get(1);
        String scramble = (String) solve.get(2);
        long timeOfCompletion = (long) solve.get(3);

        String penalty = (String) solve.get(4);

        String average = (String) solve.get(5);


        Solve score = new Solve( scramble, timeOfCompletion, time, penalty);


        return score;

    }

    private static double getTime(JSONArray solve) {

        double time = (double) solve.get(1);


        return time;

    }

    private static String getScramble(JSONArray solve) {

        String scramble = (String) solve.get(2);


        return scramble;

    }


    public static int getSmallestScramble() {



        return shortestScramble;

    }



    public static double getAverage(int number, String levelName) {

     //   if (number == 5) {
     //       if (ao5s.size() > 0) {
      //          return ao5s.get(ao5s.size()-1).getAverage();
      //      }
     //   }

       // if (number == 12) {
       //     if (ao12s.size() > 0) {
      //          return ao12s.get(ao12s.size()-1).getAverage();
      //      }
     //   }

      //  if (number == 50) {
      //      if (ao50s.size() > 0) {
      //          return ao50s.get(ao50s.size()-1).getAverage();
     //       }
     //   }

        if (solvesJ.size() >= number) {
            double total = 0;
            double smallest = LARGE_NUMBER;
            double largest = 0;

            for (int i = solvesJ.size()-1; i >= solvesJ.size()-number; i--) {
                if (getTime((JSONArray) solvesJ.get(i)) < smallest) {
                    smallest = getTime((JSONArray) solvesJ.get(i));
                }

                if (getTime((JSONArray) solvesJ.get(i)) > largest) {
                    largest = getTime((JSONArray) solvesJ.get(i));
                }
                total = total + getTime((JSONArray) solvesJ.get(i));
            }
            total = total - smallest;
            total = total - largest;

            return total / (number - 2);
        } else {
            return 0;
        }

    }

    public static Avg getAverageConcurrent(int number, ArrayList<Solve> solves) {

        //   if (number == 5) {
        //       if (ao5s.size() > 0) {
        //          return ao5s.get(ao5s.size()-1).getAverage();
        //      }
        //   }

        // if (number == 12) {
        //     if (ao12s.size() > 0) {
        //          return ao12s.get(ao12s.size()-1).getAverage();
        //      }
        //   }

        //  if (number == 50) {
        //      if (ao50s.size() > 0) {
        //          return ao50s.get(ao50s.size()-1).getAverage();
        //       }
        //   }
        Avg ao5 = new Avg();

        if (solves.size() >= number) {


            for (int i = solves.size()-1; i >= solves.size()-number; i--) {

                ao5.solves.add(solves.get(i));

            }


        }
        return ao5;

    }


    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getDaily(String levelName) {

        ArrayList<Solve> solves = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar;

        boolean nolongerTime = false;

        for (int i = solvesJ.size()-1; i >= 0; i--) {
            if (!nolongerTime) {

                calendar = Calendar.getInstance();
                calendar.setTime(date);

                if ((calendar.get(Calendar.DAY_OF_YEAR) == getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.DAY_OF_YEAR))
                        && (calendar.get(Calendar.YEAR) ==getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.YEAR))) {
                    solves.add(getSolve((JSONArray)solvesJ.get(i)));

                } else {
                    nolongerTime = true;
                }
            }
        }
        dailyN = solves.size();
        if (solves.size() < 1) {
            return new ArrayList<>();
        }

        if (solves.size() < MainMenuController.VISIBILE_LIMIT*2 ) {
            return sortByTime(solves);
        } else {

            return sortN(solves, MainMenuController.VISIBILE_LIMIT);
        }

    }

    public static ArrayList<Solve> sortN(ArrayList<Solve> solves, int n) {


        ArrayList<Solve> nSolves = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            nSolves.add(Collections.min(solves, Solve.ScoreTimeComparator));
            solves.remove(Collections.min(solves, Solve.ScoreTimeComparator));
        }

        return nSolves;

    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getWeekly(String levelName) {

        ArrayList<Solve> solves = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar;

        boolean nolongerTime = false;

        for (int i = solvesJ.size()-1; i >= 0; i--) {
            if (!nolongerTime) {

                calendar = Calendar.getInstance();
                calendar.setTime(date);

                if ((calendar.get(Calendar.WEEK_OF_YEAR) == getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.WEEK_OF_YEAR))
                        && (calendar.get(Calendar.YEAR) ==getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.YEAR))) {
                    solves.add(getSolve((JSONArray)solvesJ.get(i)));

                } else {
                    nolongerTime = true;
                }
            }
        }
        weeklyN = solves.size();
        if (solves.size() < 1) {
            return new ArrayList<>();
        }

        if (solves.size() < MainMenuController.VISIBILE_LIMIT*2 ) {
            return sortByTime(solves);
        } else {

            return sortN(solves, MainMenuController.VISIBILE_LIMIT);
        }




    }
    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void loadMonthly(String levelName) {

        ArrayList<Solve> solves = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        boolean nolongerTime = false;

        for (int i = solvesJ.size()-1; i >= 0; i--) {
            if (!nolongerTime) {
                if ((calendar.get(Calendar.MONTH) == getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.MONTH))
                        && (calendar.get(Calendar.YEAR) ==getSolve((JSONArray)solvesJ.get(i)).getCalendar().get(Calendar.YEAR))) {
                    solves.add(getSolve((JSONArray)solvesJ.get(i)));

                } else {
                    nolongerTime = true;
                }
            }
        }

        monthlyN = solves.size();
        if (solves.size() < 1) {
            monthly =  new ArrayList<>();
        }

        if (solves.size() < MainMenuController.VISIBILE_LIMIT*2 ) {
            monthly =  sortByTime(solves);
        } else {
            System.out.println("sortN");
            monthly =  sortN(solves, MainMenuController.VISIBILE_LIMIT);
        }


    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getMonthly(String levelName) {


        return monthly;

    }

    public static Solve getMonthlyIndex(int n) {
        return monthly.get(n-1);
    }

    public static int getMonthlySize() {
        return monthly.size();
    }

    public static Solve getWeeklyIndex(int n) {
        return weekly.get(n);
    }

    public static int getWeeklySize() {
        return weekly.size();
    }


    public static Solve getDailyIndex(int n) {
        return daily.get(n);
    }
    public static int getDailySize() {
        return daily.size();
    }


    public static ArrayList<Solve> getAllTime() {



        ArrayList<Solve> solves = new ArrayList<>();

        for (int i = 0; i < solvesJ.size(); i++) {


            solves.add(getSolve((JSONArray)solvesJ.get(i)));

        }



        if (solves.size() < 1) {
            return new ArrayList<>();
        }

        if (solves.size() < MainMenuController.VISIBILE_LIMIT*2 ) {
            return sortByTime(solves);
        } else {

            return sortN(solves, MainMenuController.VISIBILE_LIMIT);
        }

    }

    public static ArrayList<Avg> getAo5s() {
        return ao5s;
    }

    public static ArrayList<Avg> getAo12s() {
        return ao12s;
    }

    public static ArrayList<Avg> getAo50s() {
        return ao50s;
    }


    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void delete(Solve solve, String levelName) {
        boolean isDeleted = false;
        int i = 0;
        while (!isDeleted && i < solvesJ.size()) {
            if (getSolve((JSONArray) solvesJ.get(i)).isEqual(solve)) {
                solvesJ.remove(i);
                isDeleted = true;
            }
            i++;
        }


    }

    public static int getCurrentStreak() {
        return currentStreak;
    }

    public static int getBestStreak() {
        return bestStreak;
    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void setPenalty(Solve solve, String penalty, String levelName) {


        if (penalty.equals(Solve.TIME_PENALTY)) {
            if (!(solve.getPenalty().equals(Solve.TIME_PENALTY))) {
                solve.setTime(solve.getTime()+2);
            }
        }

        if (solve.getPenalty().equals(Solve.TIME_PENALTY)) {
            if (!(penalty.equals(Solve.TIME_PENALTY))) {
                solve.setTime(solve.getTime()-2);
            }
        }

        if (solve.getPenalty().equals(Solve.TIME_PENALTY)) {
            if (!(penalty.equals(Solve.TIME_PENALTY))) {
                solve.setTime(solve.getTime()-2);
            }
        }

        solve.setPenalty(penalty);
        for (int i = 0; i < solvesJ.size(); i++) {
            if (getSolve((JSONArray) solvesJ.get(i)).isEqual(solve)) {
                solvesJ.set(i, solveToJSON(solve));
            }
        }




    }

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static ArrayList<Solve> getLastN(String levelName, int lower, int n) {

        ArrayList<Solve> solves = new ArrayList();



        if (solvesJ.size() >= n) {
            for (int i = solvesJ.size()-1-lower; i >= solvesJ.size()-n; i--) {

                solves.add(getSolve((JSONArray) solvesJ.get(i)));


            }
        } else {
            for (int i = solvesJ.size()-1; i >= 0; i--) {
                solves.add(getSolve((JSONArray) solvesJ.get(i)));


            }
        }
        return solves;
    }

    public static ArrayList<Solve> solveLastN(ArrayList<Solve> solves, int lower, int n) {


        ArrayList<Solve> newSolves = new ArrayList<>();

        if (solves.size() >= n) {
            for (int i = solves.size()-1-lower; i >= solves.size()-n; i--) {

                newSolves.add(solves.get(i));


            }
        } else {
            for (int i = solves.size()-1; i >= 0; i--) {
                newSolves.add(solves.get(i));


            }
        }
        return newSolves;
    }




}

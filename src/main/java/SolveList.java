
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


    private static ArrayList<Avg> ao5s = new ArrayList<>();

    private static ArrayList<Avg> ao12s = new ArrayList<>();

    private static ArrayList<Avg> ao50s = new ArrayList<>();

    private static ArrayList<Solve> pbs = new ArrayList<>();

    private static ArrayList<Solve> ao5pbs = new ArrayList<>();

    private static ArrayList<Solve> ao12pbs = new ArrayList<>();


    private static ArrayList<Solve> monthly = new ArrayList<>();

    private static ArrayList<Solve> weekly = new ArrayList<>();

    private static ArrayList<Solve> daily = new ArrayList<>();

    public static void loadSolves() {
        loadSolves("solves");
        loadPbsAo12();
        loadPbsAo5();
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

    public static double getpbAo50() {


        double smallest = LARGE_NUMBER;


        for (Avg avg : ao50s) {
            if (avg.getAverage() < smallest) {
                smallest = avg.getAverage();
            }
        }
        return smallest;

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

    /**
     * gets the scores from the given table based on the parsed type.
     *
     * @param levelName String name of the level for the scores
     * @return Arraylist of scores in the top 10 of the specified table
     */
    public static void loadSolves(String levelName) {

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
                String scramble = (String) solve.get(2);
                long timeOfCompletion = (long) solve.get(3);

                String penalty = (String) solve.get(4);

                String average = (String) solve.get(5);


                Solve score = new Solve(scramble, timeOfCompletion, time, penalty);

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
                if (bufferAo5.solves.size() > 11) {
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
                if (bufferAo5.solves.size() > 49) {
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




    public static double getAverage(int number, String levelName) {

        if (number == 5) {
            if (ao5s.size() > 0) {
                return ao5s.get(0).getAverage();
            }
        }

        if (number == 12) {
            if (ao12s.size() > 0) {
                return ao12s.get(0).getAverage();
            }
        }

        if (solvesJ.size() >= number) {
            double total = 0;
            double smallest = LARGE_NUMBER;
            double largest = 0;

            for (int i = 0; i < number; i++) {
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


        return sortByTime(solves);

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

        for (int i = 0; i < solvesJ.size(); i++) {
            if (getSolve((JSONArray) solvesJ.get(i)).isEqual(solve)) {
                solvesJ.set(i, solveToJSON(solve));
            }
        }
        solve.setPenalty(penalty);



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




}

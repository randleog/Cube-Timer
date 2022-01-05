import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class FileConverterSolves {

    public static void main(String[] args) {
        create("res\\newtxt.txt", convertFile2("res\\og\\wrongFormat.txt"));
    }

    public static ArrayList<Solve> convertFile(String fileName) {
        ArrayList<Solve> solves = SolveList.getSolves("solves.txt");
        try {
            Scanner in = new Scanner( new File(fileName));

            while(in.hasNextLine()) {
                String currentLine = in.nextLine();

                if (currentLine.charAt(0) == '/') {

                } else {
                    if (solveFromString(currentLine).getTime() == 0.0) {

                    } else {

                        solves.add(solveFromString(currentLine));
                    }
                }

            }


            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return solves;

    }


    public static ArrayList<Solve> convertFile2(String fileName) {
        ArrayList<Solve> solves = SolveList.getSolves("solves.txt");
        try {
            Scanner in = new Scanner( new File(fileName));

            while(in.hasNextLine()) {
                String currentLine = in.nextLine();

                if (currentLine.charAt(0) == '/') {

                } else {
                    if (solveFromString2(currentLine).getTime() == 0.0) {

                    } else {

                        solves.add(solveFromString2(currentLine));
                    }
                }

            }


            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return solves;

    }

    public static Solve solveFromString(String solveS) {


        Scanner scanSolve = new Scanner(solveS);

        scanSolve.next();
        double time = 0;
        String timeCheck = scanSolve.next();


        if (timeCheck.contains("DNF")) {

        } else {
            time = Double.parseDouble(timeCheck);
        }




        String lastWord = "";

    String scramble = "";
        while(!(lastWord).contains("@")) {
            scramble = scramble + lastWord;
            lastWord = scanSolve.next();


        }

        String date = lastWord;

        String dateTime = scanSolve.next();

        Solve solve = new Solve(scramble, getCalendar(date, dateTime), time, "N/A");


        scanSolve.close();


        return solve;
    }

    public static double getTime(String solveTime) {
        double timeTotal;
        Scanner in = new Scanner(solveTime);

        in.useDelimiter(":");
        String test = in.next();



        timeTotal = Integer.parseInt(test)*60.0;
        timeTotal = timeTotal + in.nextDouble();

        in.close();

        return timeTotal;
    }


    public static Solve solveFromString2(String solveS) {
       // System.out.println(solveS);

        Scanner scanSolve = new Scanner(solveS);


        double time = 0;
        String timeCheck = scanSolve.next();
        time = getTime(timeCheck);




        String lastWord = "";

        String scramble = "";
        while(!(lastWord).contains("-")) {
            scramble = scramble + lastWord;
            lastWord = scanSolve.next();


        }

        String date = lastWord;

        String dateTime = scanSolve.next();

        Solve solve = new Solve(scramble, getCalendar(date, dateTime), time, "N/A");


        scanSolve.close();


        return solve;
    }

    private static void create(String tableName, ArrayList<Solve> scores) {

        try {
            FileWriter writer = new FileWriter(tableName);
            for (Solve score : scores) {
                writer.append(score.toString() + "\n");

            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read table.");
            e.printStackTrace();
        }

    }

    private static long getCalendar(String date, String time) {


        date = date.replaceAll("@", "");
        date = date.replaceAll("-", " ");



        Scanner dateScan = new Scanner(date);

        int year = dateScan.nextInt();
        int month = dateScan.nextInt();
        int day = dateScan.nextInt();

        dateScan.close();


        time = time.replaceAll(":", " ");


        Scanner timeScan = new Scanner(time);

        int hour = timeScan.nextInt();
        int minute = timeScan.nextInt();
     //   int second = timeScan.nextInt();

        timeScan.close();


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day, hour, minute, 0);



        return calendar.getTimeInMillis();
    }





}

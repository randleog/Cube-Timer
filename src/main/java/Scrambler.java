import java.util.ArrayList;


public class Scrambler {
    public static int scrambleLength = 30;

    private static ArrayList<String> availableChars = new ArrayList<>();
    private static ArrayList<String> additionalScramble = new ArrayList<>();


    public static String findShorterSolutions(String scrambledCube) {
        //Find shorter solutions (try more probes even a solution has already been found)
        //In this example, we try AT LEAST 10000 phase2 probes to find shorter solutions.
        String result = new Search().solution(scrambledCube, 21, 100000000, 10000, Search.INVERSE_SOLUTION);
        return result;
        // L2 U  D2 R' B  U2 L  F  U  R2 D2 F2 U' L2 U  B  D  R'
    }

    public static String continueSearch(String scrambledCube) {
        //Continue to find shorter solutions
        Search searchObj = new Search();
        String result = searchObj.solution(scrambledCube, 21, 500, 0, 0);
        if (!result.contains("Error")) {
            return scrambledCube;
        }

        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R'

        for (int i = 0; i < 10; i++) {
            result = searchObj.next(500, 0, 0);
            if (result.length()< scrambledCube.length()) {
                if (!result.contains("Error")) {
                    scrambledCube = result;
                }
            }

        }
        return scrambledCube;
    }

    public static String generateScramble() {
        String scramble = "";
        ArrayList<String> letters = new ArrayList<>();
        while (letters.size() < scrambleLength) {
            letters.add(getScrambleLetter(letters));
        }

        for (String letter : letters) {
            scramble = scramble + letter + additionalScramble.get(Main.random.nextInt(additionalScramble.size())) + " ";
        }
        System.out.println(scramble);

        String scrambledCube = Tools.fromScramble(scramble);

        //scramble = findShorterSolutions(continueSearch(scrambledCube));
        scramble = findShorterSolutions(scrambledCube);
        scramble = scramble.replaceAll("  ", " ");
        scramble =  scramble.replaceAll("  ", " ");

        System.out.println(scramble);
        return scramble;
    }

    private static boolean opposite(String moves1, String move2) {
        switch(moves1) {
            case "RL":
                return move2.equals("R");
            case "LR":
                return move2.equals("L");
            case "FB":
                return move2.equals("F");
            case "BF":
                return move2.equals("B");
            case "UD":
                return move2.equals("U");
            case "DU":
                return move2.equals("D");
            default:
                return false;
        }

    }



    private static String getScrambleLetter(ArrayList<String> lastMoves) {
        String newMove = "";
        if (lastMoves.size() == 0) {
            newMove = availableChars.get(Main.random.nextInt(availableChars.size()));
        } else {

            ArrayList<String> tempMoves = getAllowedMoves(lastMoves);
            newMove = tempMoves.get(Main.random.nextInt(tempMoves.size()));
        }
        return newMove;

    }

    private static ArrayList<String> getAllowedMoves(ArrayList<String> lastMoves) {
        ArrayList<String> tempMoves = new ArrayList<>();
        tempMoves.addAll(availableChars);
        if (lastMoves.size()>=1) {
            tempMoves.removeIf(s -> (s.equals(lastMoves.get(lastMoves.size() - 1))));
        }
        if (lastMoves.size()>=2) {
            tempMoves.removeIf(s -> (opposite(lastMoves.get(lastMoves.size() - 2) + lastMoves.get(lastMoves.size() - 1), s)));
        }
        return tempMoves;
    }


    public static void initializeScrambleLegal() {

        availableChars = new ArrayList<>();
        additionalScramble = new ArrayList<>();

        availableChars.add("R");
        availableChars.add("U");
        availableChars.add("L");
        availableChars.add("D");
       availableChars.add("B");
      //  availableChars.add("F");


        additionalScramble.add("'");
        additionalScramble.add("2");
        additionalScramble.add("");

    }
}

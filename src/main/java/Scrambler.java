import java.util.ArrayList;

public class Scrambler {
    public static int scrambleLength = 25;

    private static ArrayList<String> availableChars = new ArrayList<>();
    private static ArrayList<String> additionalScramble = new ArrayList<>();


    public static String generateScramble() {
        String scramble = "";
        ArrayList<String> letters = new ArrayList<>();
        while (letters.size() < scrambleLength) {
            letters.add(getScrambleLetter(letters));
        }

        for (String letter : letters) {
            scramble = scramble + letter + additionalScramble.get(Main.random.nextInt(additionalScramble.size())) + " ";
        }

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
        availableChars.add("F");


        additionalScramble.add("'");
        additionalScramble.add("2");
        additionalScramble.add("");

    }
}

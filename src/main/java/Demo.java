
public class Demo {

    public static void simpleSolve(String scrambledCube) {
        String result = new Search().solution(scrambledCube, 21, 100000000, 0, 0);
        System.out.println(result);
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R'
    }

    public static void outputControl(String scrambledCube) {
        String result = new Search().solution(scrambledCube, 21, 100000000, 0, Search.APPEND_LENGTH);
        System.out.println(result);
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R' (21f)

        result = new Search().solution(scrambledCube, 21, 100000000, 0, Search.USE_SEPARATOR | Search.INVERSE_SOLUTION);
        System.out.println(result);
        // R  L2 D  R  F  U2 F' L  F' .  B2 D' R2 B2 R2 L2 U  F2 L2 B2 U2 R2
    }

    public static String findShorterSolutions(String scrambledCube) {
        //Find shorter solutions (try more probes even a solution has already been found)
        //In this example, we try AT LEAST 10000 phase2 probes to find shorter solutions.
        String result = new Search().solution(scrambledCube, 21, 100000000, 10000, 0);
        return result;
        // L2 U  D2 R' B  U2 L  F  U  R2 D2 F2 U' L2 U  B  D  R'
    }

    public static String continueSearch(String scrambledCube) {
        //Continue to find shorter solutions
        Search searchObj = new Search();
        String result = searchObj.solution(scrambledCube, 21, 500, 0, 0);

        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R'

        for (int i = 0; i < 3; i++) {
            if (result.length()< scrambledCube.length()) {


                scrambledCube = result;
            }
            result = searchObj.next(500, 0, 0);
        }
    return scrambledCube;
    }

    public static void main(String[] args) {
        // Full initialization, which will take about 200ms.
        // The solver will be about 5x~10x slower without full initialization.
        long startTime = System.nanoTime();
        Search.init();
        System.out.println("Init time: " + (System.nanoTime() - startTime) / 1.0E6 + " ms");

        /** prepare scrambledCube as
         *
         *             |************|
         *             |*U1**U2**U3*|
         *             |************|
         *             |*U4**U5**U6*|
         *             |************|
         *             |*U7**U8**U9*|
         *             |************|
         * ************|************|************|************|
         * *L1**L2**L3*|*F1**F2**F3*|*R1**R2**R3*|*B1**B2**B3*|
         * ************|************|************|************|
         * *L4**L5**L6*|*F4**F5**F6*|*R4**R5**R6*|*B4**B5**B6*|
         * ************|************|************|************|
         * *L7**L8**L9*|*F7**F8**F9*|*R7**R8**R9*|*B7**B8**B9*|
         * ************|************|************|************|
         *             |************|
         *             |*D1**D2**D3*|
         *             |************|
         *             |*D4**D5**D6*|
         *             |************|
         *             |*D7**D8**D9*|
         *             |************|
         *
         * -> U1 U2 ... U9 R1 ... R9 F1 ... F9 D1 ... D9 L1 ... L9 B1 ... B9
         */
        String scrambledCube = "DUUBULDBFRBFRRULLLBRDFFFBLURDBFDFDRFRULBLUFDURRBLBDUDL";
        // scrambledCube can also be obtained by specific moves
        scrambledCube = Tools.fromScramble("R L2 D R F U2 F' L F' B2 D' R2 B2 R2 L2 U F2 L2 B2 U2 R2");
        System.out.println(scrambledCube);

     //   simpleSolve(scrambledCube);
        //outputControl(scrambledCube);
        System.out.println(findShorterSolutions(scrambledCube));
        System.out.println(findShorterSolutions(continueSearch(scrambledCube)));
    }
}

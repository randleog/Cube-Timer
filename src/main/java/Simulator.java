import java.util.Calendar;

public class Simulator {
    public String[] g = {"0G","1G","2G","3G","4G","5G","6G","7G", "G"};


    String[] b = {"0B","1B","2B","3B","4B","5B","6B","7B", "B"};

    String[] r = {"0R","1R","2R","3R","4R","5R","6R","7R", "R"};

    String[] o = {"0O","1O","2O","3O","4O","5O","6O","7O", "O"};

    String[] y = {"0Y","1Y","2Y","3Y","4Y","5Y","6Y","7Y", "Y"};

    String[] w = {"0W","1W","2W","3W","4W","5W","6W","7W", "W"};

    String[][] gr = new String[4][3];

    String[][] rr = new String[4][3];

    String[][] or = new String[4][3];

    String[][] wr = new String[4][3];

    String[][] yr = new String[4][3];

    String[][] br = new String[4][3];

    public static void main(String[] args) {
        System.out.println("rubiks simulator");

        Simulator simulator = new Simulator();


        System.out.println(simulator.getCube());
    }

    public void scramble(String scramble) {
        System.out.println(scramble);
        scramble= scramble.replaceAll("R2", "RR");
        scramble= scramble.replaceAll("L2", "LL");
        scramble= scramble.replaceAll("U2", "UU");
        scramble= scramble.replaceAll("D2", "DD");
        scramble= scramble.replaceAll("B2", "BB");
        scramble= scramble.replaceAll("F2", "FF");

        scramble= scramble.replaceAll("R'", "RRR");
        scramble= scramble.replaceAll("L'", "LLL");
        scramble= scramble.replaceAll("U'", "UUU");
        scramble= scramble.replaceAll("D'", "DDD");
        scramble= scramble.replaceAll("B'", "BBB");
        scramble= scramble.replaceAll("F'", "FFF");

        scramble=scramble.replaceAll(" ", "");

        for (int i = 0; i < scramble.length(); i++) {
            move(scramble.charAt(i));
        }
        System.out.println(scramble);
        System.out.println(getCube());
    }

    public String[] getG() {
        return g;
    }
    public String[] getB() {
        return b;
    }

    public String[] getR() {
        return r;
    }

    public String[] getO() {
        return o;
    }

    public String[] getY() {
        return y;
    }

    public String[] getW() {
        return w;
    }


    private void updateGR() {
        String[] w1 = new String[4];
        w1[0] = w[5];
        w1[1] = w[6];
        w1[2] = w[7];

        gr[0] = w1;

        String[] r1 = new String[4];
        r1[0] = r[0];
        r1[1] = r[3];
        r1[2] = r[5];

        gr[1] = r1;


        String[] y1 = new String[4];
        y1[0] = y[2];
        y1[1] = y[1];
        y1[2] = y[0];

        gr[2] = y1;

        String[] o1 = new String[4];
        o1[0] = o[7];
        o1[1] = o[4];
        o1[2] = o[2];

        gr[3] = o1;



    }

    private void setGR() {

        w[5] = gr[0][0];
        w[6] = gr[0][1];
        w[7] = gr[0][2];

        r[0] = gr[1][0];
        r[3] = gr[1][1];
        r[5] = gr[1][2];

        y[2] = gr[2][0];
        y[1] = gr[2][1];
        y[0] = gr[2][2];

        o[7] = gr[3][0];
        o[4] = gr[3][1];
        o[2] = gr[3][2];


    }

    private void updateRR() {
        String[] w1 = new String[4];
        w1[0] = w[7];
        w1[1] = w[4];
        w1[2] = w[2];

        rr[0] = w1;

        String[] b1 = new String[4];
        b1[0] = b[0];
        b1[1] = b[3];
        b1[2] = b[5];

        rr[1] = b1;


        String[] y1 = new String[4];
        y1[0] = y[7];
        y1[1] = y[4];
        y1[2] = y[2];

        rr[2] = y1;

        String[] g1 = new String[4];
        g1[0] = g[7];
        g1[1] = g[4];
        g1[2] = g[2];

        rr[3] = g1;



    }

    private void setRR() {

        w[7] = rr[0][0];
        w[4] = rr[0][1];
        w[2] = rr[0][2];

        b[0] = rr[1][0];
        b[3] = rr[1][1];
        b[5] = rr[1][2];

        y[7] = rr[2][0];
        y[4] = rr[2][1];
        y[2] = rr[2][2];

        g[7] = rr[3][0];
        g[4] = rr[3][1];
        g[2] = rr[3][2];


    }

    private void updateOR() {
        String[] w1 = new String[4];
        w1[0] = w[0];
        w1[1] = w[3];
        w1[2] = w[5];

        or[0] = w1;

        String[] g1 = new String[4];
        g1[0] = g[0];
        g1[1] = g[3];
        g1[2] = g[5];

        or[1] = g1;


        String[] y1 = new String[4];
        y1[0] = y[0];
        y1[1] = y[3];
        y1[2] = y[5];

        or[2] = y1;

        String[] b1 = new String[4];
        b1[0] = b[7];
        b1[1] = b[4];
        b1[2] = b[2];

        or[3] = b1;



    }

    private void setOR() {

        w[0] = or[0][0];
        w[3] = or[0][1];
        w[5] = or[0][2];

        g[0] = or[1][0];
        g[3] = or[1][1];
        g[5] = or[1][2];

        y[0] = or[2][0];
        y[3] = or[2][1];
        y[5] = or[2][2];

        b[7] = or[3][0];
        b[4] = or[3][1];
        b[2] = or[3][2];


    }

    private void updateWR() {
        String[] b1 = new String[4];
        b1[0] = b[2];
        b1[1] = b[1];
        b1[2] = b[0];

        wr[0] = b1;

        String[] r1 = new String[4];
        r1[0] = r[2];
        r1[1] = r[1];
        r1[2] = r[0];

        wr[1] = r1;


        String[] g1 = new String[4];
        g1[0] = g[2];
        g1[1] = g[1];
        g1[2] = g[0];

        wr[2] = g1;

        String[] o1 = new String[4];
        o1[0] = o[2];
        o1[1] = o[1];
        o1[2] = o[0];

        wr[3] = o1;



    }

    private void setWR() {

        b[2] = wr[0][0];
        b[1] = wr[0][1];
        b[0] = wr[0][2];

        r[2] = wr[1][0];
        r[1] = wr[1][1];
        r[0] = wr[1][2];

        g[2] = wr[2][0];
        g[1] = wr[2][1];
        g[0] = wr[2][2];

        o[2] = wr[3][0];
        o[1] =  wr[3][1];
        o[0] = wr[3][2];


    }

    private void updateYR() {
        String[] b1 = new String[4];
        b1[0] = b[5];
        b1[1] = b[6];
        b1[2] = b[7];

        yr[0] = b1;

        String[] o1 = new String[4];
        o1[0] = o[5];
        o1[1] = o[6];
        o1[2] = o[7];

        yr[1] = o1;

        String[] g1 = new String[4];
        g1[0] = g[5];
        g1[1] = g[6];
        g1[2] = g[7];

        yr[2] = g1;



        String[] r1 = new String[4];
        r1[0] = r[5];
        r1[1] = r[6];
        r1[2] = r[7];

        yr[3] = r1;


    }

    private void setYR() {

        b[5] = yr[0][0];
        b[6] = yr[0][1];
        b[7] = yr[0][2];

        o[5] = yr[1][0];
        o[6] =  yr[1][1];
        o[7] = yr[1][2];

        g[5] = yr[2][0];
        g[6] = yr[2][1];
        g[7] = yr[2][2];


        r[5] = yr[3][0];
        r[6] = yr[3][1];
        r[7] = yr[3][2];



    }

    private void updateBR() {
        String[] w1 = new String[4];
        w1[0] = w[2];
        w1[1] = w[1];
        w1[2] = w[0];

        br[0] = w1;

        String[] o1 = new String[4];
        o1[0] = o[0];
        o1[1] = o[3];
        o1[2] = o[5];

        br[1] = o1;

        String[] y1 = new String[4];
        y1[0] = y[5];
        y1[1] = y[6];
        y1[2] = y[7];

        br[2] = y1;



        String[] r1 = new String[4];
        r1[0] = r[2];
        r1[1] = r[4];
        r1[2] = r[7];

        br[3] = r1;


    }

    private void setBR() {

        w[2] = br[0][0];
        w[1] = br[0][1];
        w[0] = br[0][2];

        o[0] = br[1][0];
        o[3] =  br[1][1];
        o[5] = br[1][2];

        y[5] = br[2][0];
        y[6] = br[2][1];
        y[7] = br[2][2];


        r[2] = br[3][0];
        r[4] = br[3][1];
        r[7] = br[3][2];



    }

    public String getCube() {
        String cube = "";
        cube = cube + "\n               ";

        int i = 0;
        for (String string : w) {
            i++;
            cube = cube + string + "| ";
            if (i % 3 == 0) {
                cube = cube + "\n               ";
            } else if (i == 4) {
                cube = cube + "W " + "| ";
                i++;
            }



        }

        cube = cube + "\n";

        cube = cube + o[0] + "| " + o[1] + "| " + o[2];
        cube = cube + "|   |";
        cube = cube + g[0] + "| " + g[1] + "| " + g[2];
        cube = cube + "|   |";
        cube = cube + r[0] + "| " + r[1] + "| " + r[2];
        cube = cube + "|   |";
        cube = cube + b[0] + "| " + b[1] + "| " + b[2];

        cube = cube + "\n";

        cube = cube + o[3] + "| " + "O " + "| " + o[4];
        cube = cube + "|   |";
        cube = cube + g[3] + "| " + "G " + "| " + g[4];
        cube = cube + "|   |";
        cube = cube + r[3] + "| " + "R " + "| " + r[4];
        cube = cube + "|   |";
        cube = cube + b[3] + "| " + "B " + "| " + b[4];


        cube = cube + "\n";
        cube = cube + o[5] + "| " + o[6] + "| " + o[7];
        cube = cube + "|   |";
        cube = cube + g[5] + "| " + g[6] + "| " + g[7];
        cube = cube + "|   |";
        cube = cube + r[5] + "| " + r[6] + "| " + r[7];
        cube = cube + "|   |";
        cube = cube + b[5] + "| " + b[6] + "| " + b[7];


        cube = cube + "\n";





        cube = cube + "\n               ";
        i = 0;
        for (String string : y) {
            i++;
            cube = cube + string + "| ";
            if (i % 3 == 0) {
                cube = cube + "\n               ";
            } else if (i == 4) {
                cube = cube + "Y " + "| ";
                i++;
            }
        }
        return cube;
    }

    public void move(char move) {

        switch (move) {
            case 'R' -> RMove();
            case 'L' -> LMove();
            case 'F' -> FMove();
            case 'D' -> DMove();
            case 'B' -> BMove();
            case 'U' -> UMove();
        }


    }

    public void FMove() {
        updateGR();


        moveJust(g);
        moveJust(gr);
        setGR();

    }

    public void RMove() {
        updateRR();


        moveJust(r);
        moveJust(rr);
        setRR();

    }

    public void LMove() {
        updateOR();


        moveJust(o);
        moveJust(or);
        setOR();

    }

    public void UMove() {
        updateWR();


        moveJust(w);
        moveJust(wr);
        setWR();

    }


    public void DMove() {
        updateYR();


        moveJust(y);
        moveJust(yr);
        setYR();

    }

    public void BMove() {
        updateBR();


        moveJust(b);
        moveJust(br);
        setBR();

    }

    private void moveJust(String[][] g) {
        String[] buffer = g[3];
       g[3] = g[2];
        g[2]=g[1];
        g[1]=g[0];
        g[0]=buffer;


    }

    private void moveJust(String[] g) {
        String G0 = g[0];
        String G2 = g[2];
        String G7 = g[7];
        String G5 = g[5];

        g[0] = G5;
        g[5] = G7;
        g[7] = G2;
        g[2] = G0;

        String G1 = g[1];
        String G4 = g[4];
        String G6 = g[6];
        String G3 = g[3];

        g[1] = G3;
        g[4] = G1;
        g[6] = G4;
        g[3] = G6;

    }



}

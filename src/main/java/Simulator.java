import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * a simulated rubiks cube
 *
 * @author William Randle (randleog)
 */
public class Simulator {
    public String[] g = {"0G", "1G", "2G", "3G", "4G", "5G", "6G", "7G", "G"};


    String[] b = {"0B", "1B", "2B", "3B", "4B", "5B", "6B", "7B", "B"};

    String[] r = {"0R", "1R", "2R", "3R", "4R", "5R", "6R", "7R", "R"};

    String[] o = {"0O", "1O", "2O", "3O", "4O", "5O", "6O", "7O", "O"};

    String[] y = {"0Y", "1Y", "2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "Y"};

    String[] w = {"0W", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "W"};

    String[][] gr = new String[4][3];

    String[][] rr = new String[4][3];

    String[][] or = new String[4][3];

    String[][] wr = new String[4][3];

    String[][] yr = new String[4][3];

    String[][] br = new String[4][3];

    private String[] faces = {"G", "R", "B", "O"};

    private int up = 0;

    int currentface = 0;

    public void rotate(String dir) {
        if (dir.equals("R")) {
            if (up == 0) {
                clockwise();
            } else {
                antiClockwise();
            }
        } else if (dir.equals("L")) {
            if (up == 0) {
                antiClockwise();
            } else {
                clockwise();
            }
        }

    }

    private void clockwise() {
        currentface++;
        if (currentface > 3) {
            currentface=0;
        }
    }

    private void antiClockwise() {
        currentface--;
        if (currentface < 0) {
            currentface=3;
        }
    }

    public static void main(String[] args) {
        //  System.out.println("rubiks simulator");

        Simulator simulator = new Simulator();
        simulator.scramble("R U B R B"); // R' U2 L U B' L2 B2 L2 U2 R' U' R L2 B U' L B2 R B L D' R' B' ");

        //  System.out.println(simulator.getCube());
    }

    public void scramble(String scramble) {
        //System.out.println(isSolved());
        //   System.out.println(scramble);
        scramble = scramble.replaceAll("R2", "RR");
        scramble = scramble.replaceAll("L2", "LL");
        scramble = scramble.replaceAll("U2", "UU");
        scramble = scramble.replaceAll("D2", "DD");
        scramble = scramble.replaceAll("B2", "BB");
        scramble = scramble.replaceAll("F2", "FF");

        scramble = scramble.replaceAll("R'", "RRR");
        scramble = scramble.replaceAll("L'", "LLL");
        scramble = scramble.replaceAll("U'", "UUU");
        scramble = scramble.replaceAll("D'", "DDD");
        scramble = scramble.replaceAll("B'", "BBB");
        scramble = scramble.replaceAll("F'", "FFF");

        scramble = scramble.replaceAll(" ", "");

        for (int i = 0; i < scramble.length(); i++) {

            move(scramble.charAt(i));
        }
        //     System.out.println(isSolved());
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
        o[1] = wr[3][1];
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
        o[6] = yr[1][1];
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

        w[0] = br[0][0];
        w[1] = br[0][1];
        w[2] = br[0][2];

        o[0] = br[1][0];
        o[3] = br[1][1];
        o[5] = br[1][2];

        y[5] = br[2][0];
        y[6] = br[2][1];
        y[7] = br[2][2];


        r[7] = br[3][0];
        r[4] = br[3][1];
        r[2] = br[3][2];


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

    public boolean isSolved() {


        String[] g2 = {"0G", "1G", "2G", "3G", "4G", "5G", "6G", "7G", "G"};
        String[] b2 = {"0B", "1B", "2B", "3B", "4B", "5B", "6B", "7B", "B"};
        String[] r2 = {"0R", "1R", "2R", "3R", "4R", "5R", "6R", "7R", "R"};
        String[] o2 = {"0O", "1O", "2O", "3O", "4O", "5O", "6O", "7O", "O"};
        String[] y2 = {"0Y", "1Y", "2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "Y"};
        String[] w2 = {"0W", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "W"};


        if (!Arrays.equals(g, g2)) {
            return false;
        }


        if (!Arrays.equals(b, b2)) {
            return false;
        }
        if (!Arrays.equals(r, r2)) {
            return false;
        }
        if (!Arrays.equals(o, o2)) {
            return false;
        }

        if (!Arrays.equals(y, y2)) {
            return false;
        }

        if (!Arrays.equals(w, w2)) {
            return false;
        }

        return true;
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

    public void move(String move) {

        switch (move) {
            case "R" -> RMove();
            case "O" -> LMove();
            case "G" -> FMove();
            case "Y" -> DMove();
            case "B" -> BMove();
            case "W" -> UMove();
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
        g[2] = g[1];
        g[1] = g[0];
        g[0] = buffer;


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

    Color red = Color.rgb(255, 0, 0);
    Color green = Color.rgb(0, 255, 0);
    Color blue = Color.rgb(0, 50, 255);
    Color orange = Color.rgb(255, 125, 0);
    Color white = Color.rgb(255, 255, 255);
    Color yellow = Color.rgb(255, 210, 0);

    public Color getColor(String color) {
        if (color.contains("B")) {
            return blue;
        } else if (color.contains("O")) {
            return orange;
        } else if (color.contains("G")) {
            return green;
        } else if (color.contains("R")) {
            return red;
        } else if (color.contains("W")) {
            return white;
        } else if (color.contains("Y")) {
            return yellow;
        }

        return white;

    }

    public int getHeight(int pos) {
        return 1;
    }
    double angle = 2;

    double angleC = 1;


    double sideAngle = 1;

    int width = 20;
    int height = 20;

    private void drawSides(GraphicsContext g2d, String[] g, String[] w, String[] r, String[] alt, double startX, double startY) {
      //  sideAngle = Math.pow(sideAngle, 1.5);


        if ( sideAngle < 0) {
           // moveJust(alt);
            drawH2(g2d, startX-width*3, startY, alt);
        } else {
            drawH1(g2d, startX, startY, r);
        }

        int i = 0;
        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX, startY, width, height);

        i++;


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX + width, startY, width, height);
        i++;


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX + width * 2, startY, width, height);
        i++;

        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX, startY + height, width, height);
        i++;

        g2d.setFill(getColor(g[g.length - 1]));
        g2d.fillRect(startX + width, startY + height, width, height);


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX + width * 2, startY + height, width, height);
        i++;


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX, startY + height * 2, width, height);
        i++;


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX + width, startY + height * 2, width, height);
        i++;


        g2d.setFill(getColor(g[i]));
        g2d.fillRect(startX + width * 2, startY + height * 2, width, height);
        i++;


        i = 0;


        /////now handling the top

        double[] widths = {startX + (width / (angle * sideAngle)), startX + width + (width / (angle * sideAngle)), startX + width, startX};
        double[] widths2 = {startX + (width / (angle * sideAngle)) + width, startX + width + (width / (angle * sideAngle)) + width, startX + width * 2, startX + width};
        double[] widths3 = {startX + (width / (angle * sideAngle)) + width * 2, startX + width + (width / (angle * sideAngle)) + width * 2, startX + width * 3, startX + width * 2};


        double widthV = (width / (angle * sideAngle));
        double v4 = startX + width + widthV * 2;


        double widthV2 = startX + widthV * 2;
        double widthV3 = startX + widthV * 3;

        double[] widths21 = {widthV2, v4, startX + width + widthV, startX + widthV};
        double[] widths22 = {widthV2 + width, startX + width + widthV + width + widthV, startX + width * 2 + widthV, startX + width + widthV};
        double[] widths23 = {widthV2 + width * 2, widthV2 + width * 3, startX + width * 3 + widthV, startX + width * 2 + widthV};

        double[] widths31 = {widthV3, startX + width + widthV * 4, startX + width + widthV * 3, startX + widthV * 2};
        double[] widths32 = {widthV3 + width , startX + width*2 + widthV *3, startX + width * 2 + widthV*2, v4};
        double[] widths33 = {widthV3+ width * 2, startX + width * 3 + widthV*3, startX + width * 3 + widthV*2, startX + width * 2 + widthV*2};


        double v1 = startY - height / (angle * angleC) * 3;
        double v2 = startY - height / (angle * angleC) * 2;
        double v3 = startY - height / (angle * angleC) * 1;

        double[] heights = {v1, v1, v2, v2};
        double[] heights2 = {v2, v2, v3, v3};
        double[] heights3 = {v3, v3, startY, startY};



        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths31, heights, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths32, heights, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths33, heights, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths21, heights2, 4);
        i++;

        g2d.setFill(getColor(w[w.length - 1]));
        g2d.fillPolygon(widths22, heights2, 4);


        //this
        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths23, heights2, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths, heights3, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths2, heights3, 4);
        i++;

        g2d.setFill(getColor(w[i]));
        g2d.fillPolygon(widths3, heights3, 4);
        i++;





    }


    private void drawH1(GraphicsContext g2d, double startX, double startY, String[] alt) {

        int i = 0;
        double widthV = (width / (angle * sideAngle));
        double v4 = startX + width + widthV * 2;


        double widthV2 = startX + widthV * 2;
        double widthV3 = startX + widthV * 3;

        /////now handling the top



        double[] widths = {startX+width*3, startX + width * 3 + widthV, startX + width * 3 + widthV, startX+width*3};

        double[] widths2 = {startX+width*3 + widthV, startX + width * 3 + widthV*2, startX + width * 3 + widthV*2, startX+width*3+ widthV};

        double[] widths3 = {startX+width*3 + widthV*2, startX + width * 3 + widthV*3, startX + width * 3 + widthV*3, startX+width*3+ widthV*2};

        double v1 = startY-height / (angle * angleC);
        double v2 = startY+height-height / (angle * angleC);


        double heightV = height / (angle * angleC);

        double[] heights = {startY, v1  , v2, startY+height};
        double[] heights11 = {startY- heightV, v1- heightV, v2- heightV, startY+height- heightV};
        double[] heights12 = {startY- heightV*2, v1- heightV*2, v2- heightV*2, startY+height- heightV*2};


        double[] heights20 = {startY+height, v1+height  , v2+height, startY+height+height};
        double[] heights21 = {startY- heightV+height, v1- heightV+height, v2- heightV+height, startY+height- heightV+height};
        double[] heights22 = {startY- heightV*2+height, v1- heightV*2+height, v2- heightV*2+height, startY+height- heightV*2+height};

        double[] heights30 = {startY+height+height, v1+height+height  , v2+height+height, startY+height+height+height};
        double[] heights31 = {startY- heightV+height+height, v1- heightV+height+height, v2- heightV+height+height, startY+height- heightV+height+height};
        double[] heights32 = {startY- heightV*2+height+height, v1- heightV*2+height+height, v2- heightV*2+height+height, startY+height- heightV*2+height+height};

        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths, heights, 4);
        i++;


        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths2, heights11, 4);
        i++;


        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths3, heights12, 4);
        i++;

        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths, heights20, 4);
        i++;


        g2d.setFill(getColor(alt[alt.length-1]));
        g2d.fillPolygon(widths2, heights21, 4);



        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths3, heights22, 4);
        i++;

        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths, heights30, 4);
        i++;


        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths2, heights31, 4);
        i++;


        g2d.setFill(getColor(alt[i]));
        g2d.fillPolygon(widths3, heights32, 4);
        i++;
    }

    private void drawH2(GraphicsContext g2d, double startX, double startY, String[] alt) {

        int i = 0;
        double widthV = (width / (angle * sideAngle));
        double v4 = startX + width + widthV * 2;


        double widthV2 = startX + widthV * 2;
        double widthV3 = startX + widthV * 3;

        /////now handling the top



        double[] widths = {startX+width*3, startX + width * 3 + widthV, startX + width * 3 + widthV, startX+width*3};

        double[] widths2 = {startX+width*3 + widthV, startX + width * 3 + widthV*2, startX + width * 3 + widthV*2, startX+width*3+ widthV};

        double[] widths3 = {startX+width*3 + widthV*2, startX + width * 3 + widthV*3, startX + width * 3 + widthV*3, startX+width*3+ widthV*2};

        double v1 = startY-height / (angle * angleC);
        double v2 = startY+height-height / (angle * angleC);


        double heightV = height / (angle * angleC);

        double[] heights = {startY, v1  , v2, startY+height};
        double[] heights11 = {startY- heightV, v1- heightV, v2- heightV, startY+height- heightV};
        double[] heights12 = {startY- heightV*2, v1- heightV*2, v2- heightV*2, startY+height- heightV*2};


        double[] heights20 = {startY+height, v1+height  , v2+height, startY+height+height};
        double[] heights21 = {startY- heightV+height, v1- heightV+height, v2- heightV+height, startY+height- heightV+height};
        double[] heights22 = {startY- heightV*2+height, v1- heightV*2+height, v2- heightV*2+height, startY+height- heightV*2+height};

        double[] heights30 = {startY+height+height, v1+height+height  , v2+height+height, startY+height+height+height};
        double[] heights31 = {startY- heightV+height+height, v1- heightV+height+height, v2- heightV+height+height, startY+height- heightV+height+height};
        double[] heights32 = {startY- heightV*2+height+height, v1- heightV*2+height+height, v2- heightV*2+height+height, startY+height- heightV*2+height+height};

        g2d.setFill(getColor(alt[2]));
        g2d.fillPolygon(widths, heights, 4);
        i++;


        g2d.setFill(getColor(alt[1]));
        g2d.fillPolygon(widths2, heights11, 4);
        i++;


        g2d.setFill(getColor(alt[0]));
        g2d.fillPolygon(widths3, heights12, 4);
        i++;

        g2d.setFill(getColor(alt[4]));
        g2d.fillPolygon(widths, heights20, 4);
        i++;


        g2d.setFill(getColor(alt[alt.length-1]));
        g2d.fillPolygon(widths2, heights21, 4);



        g2d.setFill(getColor(alt[3]));
        g2d.fillPolygon(widths3, heights22, 4);
        i++;

        g2d.setFill(getColor(alt[7]));
        g2d.fillPolygon(widths, heights30, 4);
        i++;


        g2d.setFill(getColor(alt[6]));
        g2d.fillPolygon(widths2, heights31, 4);
        i++;


        g2d.setFill(getColor(alt[5]));
        g2d.fillPolygon(widths3, heights32, 4);
        i++;
    }

    private void drawCube(GraphicsContext g2d, String col) {
        g2d.setFill(Color.rgb(35,35,35));
        g2d.fillRect(0,0,g2d.getCanvas().getWidth(),g2d.getCanvas().getHeight());

        double startY = g2d.getCanvas().getWidth() / 3.0;

        double startX = 30;

        if (col.contains("GW")) {
            drawSides(g2d, g, w, r, o,startX, startY);
        } else if (col.contains("RW")) {
            moveJust(w);
            drawSides(g2d, r, w, b,g, startX, startY);
            moveJust(w);
            moveJust(w);
            moveJust(w);
        }else if (col.contains("BW")) {
            moveJust(w);
            moveJust(w);
            drawSides(g2d, b, w, o,r, startX, startY);
            moveJust(w);
            moveJust(w);
        }else if (col.contains("OW")) {
            moveJust(w);
            moveJust(w);
            moveJust(w);
            drawSides(g2d, o, w, g,b, startX, startY);
            moveJust(w);
        }

        if (col.contains("GY")) {
            moveJust(g);
            moveJust(g);
            moveJust(o);
            moveJust(o);
            moveJust(y);
            moveJust(y);
            drawSides(g2d, g, y, o,r, startX, startY);
            moveJust(y);
            moveJust(y);
            moveJust(g);
            moveJust(g);
            moveJust(o);
            moveJust(o);
        } else if (col.contains("RY")) {
            moveJust(g);
            moveJust(g);
            moveJust(r);
            moveJust(r);
            moveJust(y);
            drawSides(g2d, r, y, g, b,startX, startY);
            moveJust(y);
            moveJust(y);
            moveJust(y);
            moveJust(g);
            moveJust(g);
            moveJust(r);
            moveJust(r);
        }else if (col.contains("BY")) {
            moveJust(b);
            moveJust(b);
            moveJust(r);
            moveJust(r);
            drawSides(g2d, b, y, r,o, startX, startY);
            moveJust(b);
            moveJust(b);
            moveJust(r);
            moveJust(r);
        }else if (col.contains("OY")) {
            moveJust(o);
            moveJust(o);
            moveJust(b);
            moveJust(b);
            moveJust(y);
            moveJust(y);
            moveJust(y);
            drawSides(g2d, o, y, b, g,startX, startY);
            moveJust(y);
            moveJust(o);
            moveJust(o);
            moveJust(b);
            moveJust(b);
        }
    }


    public void draw3d(GraphicsContext g2d, String col) {
        double startY = g2d.getCanvas().getWidth() / 3.0;


        if (col.contains("GW")) {
            if (up == 0) {
                drawCube(g2d, faces[currentface]+"W");
            }

        } else if (col.contains("RW")) {
            moveJust(w);
            drawSides(g2d, r, w, b,g, 0, startY);
            moveJust(w);
            moveJust(w);
            moveJust(w);
        }else if (col.contains("BW")) {
            moveJust(w);
            moveJust(w);
            drawSides(g2d, b, w, o, r,0, startY);
            moveJust(w);
            moveJust(w);
        }else if (col.contains("OW")) {
            moveJust(w);
            moveJust(w);
            moveJust(w);
            drawSides(g2d, o, w, g,b, 0, startY);
            moveJust(w);
        }

        if (col.contains("GY")) {
            moveJust(g);
            moveJust(g);
            moveJust(o);
            moveJust(o);
            moveJust(y);
            moveJust(y);
            drawSides(g2d, g, y, o,r, 20, startY);
            moveJust(y);
            moveJust(y);
            moveJust(g);
            moveJust(g);
            moveJust(o);
            moveJust(o);
        } else if (col.contains("RY")) {
            moveJust(g);
            moveJust(g);
            moveJust(r);
            moveJust(r);
            moveJust(y);
            drawSides(g2d, r, y, g,b, 0, startY);
            moveJust(y);
            moveJust(y);
            moveJust(y);
            moveJust(g);
            moveJust(g);
            moveJust(r);
            moveJust(r);
        }else if (col.contains("BY")) {
            moveJust(b);
            moveJust(b);
            moveJust(r);
            moveJust(r);
            drawSides(g2d, b, y, r, o,0, startY);
            moveJust(b);
            moveJust(b);
            moveJust(r);
            moveJust(r);
        }else if (col.contains("OY")) {
            moveJust(o);
            moveJust(o);
            moveJust(b);
            moveJust(b);
            moveJust(y);
            moveJust(y);
            moveJust(y);
            drawSides(g2d, o, y, b, g,0, startY);
            moveJust(y);
            moveJust(o);
            moveJust(o);
            moveJust(b);
            moveJust(b);
        }
    }


}

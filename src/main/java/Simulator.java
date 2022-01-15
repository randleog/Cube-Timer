public class Simulator {
    public String[] g = {"0G","1G","2G","3G","4G","5G","6G","7G"};

    String[] b = {"0B","1B","2B","3B","4B","5B","6B","7B"};

    String[] r = {"0R","1R","2R","3R","4R","5R","6R","7R"};

    String[] o = {"0O","1O","2O","3O","4O","5O","6O","7O"};

    String[] y = {"0Y","1Y","2Y","3Y","4Y","5Y","6Y","7Y"};

    String[] w = {"0W","1W","2W","3W","4W","5W","6W","7W"};

    public static void main(String[] args) {
        System.out.println("rubiks simulator");

        Simulator simulator = new Simulator();
        System.out.println(simulator.getCube());
    }

    public String getCube() {
        String cube = "";


        int i = 0;
        for (String string : w) {
            i++;
            cube = cube + string + "| ";
            if (i % 3 == 0) {
                cube = cube + "\n";
            } else if (i == 4) {
                cube = cube + "G" + "| ";
                i++;
            }



        }

        cube = cube + "\n";



        for (i = 0; i < 8; i++) {
            i++;
            cube = cube + o[i] + "| " + g[i] + "| " + r[i]+ "| " + b[i];
            if (i % 3 == 0) {
                cube = cube + "\n";
            } else if (i == 4) {
                cube = cube + "G" + "| ";
                i++;
            }



        }



        cube = cube + "\n";
        i = 0;
        for (String string : y) {
            i++;
            cube = cube + string + "| ";
            if (i % 3 == 0) {
                cube = cube + "\n";
            } else if (i == 4) {
                cube = cube + "W" + "| ";
                i++;
            }
        }
        return cube;
    }



}

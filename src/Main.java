import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

class Main {
    public static void main(String[] args) throws Exception {
        String[] filenames = {"beeSetup1.txt", "beeSetup2.txt", "beeSetup3.txt"};
        int finalmoves = 0;
        for(int a = 0; a < filenames.length; a++) {
            BufferedReader brTest = new BufferedReader(new FileReader(new File(filenames[a])));

            String[] cubeSize = brTest.readLine().split(",");
            int[][] bees = new int[15][3];
            int[][] homes = new int[15][4];
            boolean[][][] cube = new boolean[Integer.parseInt(cubeSize[0])][Integer.parseInt(cubeSize[1])][Integer.parseInt(cubeSize[2])];

            // Creating Hives based on txt file and adding them to an Arraylist
            for (int i = 0; i < 15; i++) {
                String[] temp = brTest.readLine().split(",");
                homes[i][0] = Integer.parseInt(temp[0]);
                homes[i][1] = Integer.parseInt(temp[1]);
                homes[i][2] = Integer.parseInt(temp[2]);
                homes[i][3] = 0;
            }
            // Creating Bees based on txt file and adding them to an ArrayList
            for (int i = 0; i < 15; i++) {
                String[] temp = brTest.readLine().split(",");
                bees[i][0] = Integer.parseInt(temp[0]);
                bees[i][1] = Integer.parseInt(temp[1]);
                bees[i][2] = Integer.parseInt(temp[2]);
            }

            // Getting the number of obstacles from txt file
            int numObstacles = Integer.parseInt(brTest.readLine());

            // Creating Obstacles based on txt file and adding them to an ArrayList
            for (int k = 0; k < numObstacles; k++) {
                String[] temp = brTest.readLine().split(",");
                //System.out.println(Arrays.toString(temp));
                cube[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])][Integer.parseInt(temp[2])] = true; // True = obstacle
            }
            int totalmoves = 0;
            int bounds = Integer.parseInt(cubeSize[0]);

            for (int i = 0; i < 15; i++) {
                int[] temparray = {bees[i][0], bees[i][0], bees[i][0]};
                Node start = new Node(null, temparray, 0);
                int lowest = 1000;
                int lowesthive = 0;
                for (int y = 0; y < 15; y++) {
                    if (homes[i][3] == 0) {
                        int[] temparray2 = {homes[y][0], homes[y][1], homes[y][2]};
                        if (lowest > heuristic(start, temparray2)) {
                            lowest = heuristic(start, temparray2);
                            lowesthive = y;
                        }
                    }
                }
                int[] endcoords = {homes[lowesthive][0], homes[lowesthive][1], homes[lowesthive][2]};
                ArrayList<Node> ret = astar(start, endcoords, cube);
                if (ret != null) {
                    //printout(ret);
                    //System.out.println(ret.size()-1);
                    //System.out.println("next");
                    totalmoves += (ret.size() - 1);
                }
            }
            System.out.println(filenames[a] + " took " + totalmoves + " moves");
            finalmoves += totalmoves;
        }
        System.out.println("the total moves is: " + finalmoves);
    }

    public static int[][] get_neighbors(boolean[][][] grid, Node node, int bounds) {

        int[] coords = node.getPosition();
        int[][] neighbors = new int[12][3];

        int[] temparray0 = {coords[0] + 1, coords[1], coords[2]};
        neighbors[0] = temparray0;
        int[] temparray1 = {coords[0], coords[1] + 1, coords[2]};
        neighbors[1] = temparray1;
        int[] temparray2 = {coords[0], coords[1], coords[2] + 1};
        neighbors[2] = temparray2;
        int[] temparray3 = {coords[0] + 1, coords[1] + 1, coords[2]};
        neighbors[3] = temparray3;
        int[] temparray4 = {coords[0] + 1, coords[1], coords[2] + 1};
        neighbors[4] = temparray4;
        int[] temparray5 = {coords[0], coords[1] + 1, coords[2] + 1};
        neighbors[5] = temparray5;
        // Separates the minuses from the plusses
        int[] temparray6 = {coords[0] - 1, coords[1], coords[2]};
        neighbors[6] = temparray6;
        int[] temparray7 = {coords[0], coords[1] - 1, coords[2]};
        neighbors[7] = temparray7;
        int[] temparray8 = {coords[0], coords[1], coords[2] - 1};
        neighbors[8] = temparray8;
        int[] temparray9 = {coords[0] - 1, coords[1] - 1, coords[2]};
        neighbors[9] = temparray9;
        int[] temparray10 = {coords[0] - 1, coords[1], coords[2] - 1};
        neighbors[10] = temparray10;
        int[] temparray11 = {coords[0], coords[1] - 1, coords[2] - 1};
        neighbors[11] = temparray11;

        temparray0 = null;
        temparray1 = null;
        temparray2 = null;
        temparray3 = null;
        temparray4 = null;
        temparray5 = null;
        temparray6 = null;
        temparray7 = null;
        temparray8 = null;
        temparray9 = null;
        temparray10 = null;
        temparray11 = null;

        int i = 0;
        int els = 12;
        for (int[] neighbor : neighbors) {
            boolean bpoint = false;
            for (int num : neighbor) {
                if ((num >= bounds || num < 0)) {
                    neighbors[i] = null;
                    bpoint = true;
                    els -= 1;
                    break;
                }
            }
            if (!bpoint) {
                if (grid[neighbor[0]][neighbor[1]][neighbor[2]]) {
                    neighbors[i] = null;
                    els -= 1;
                }
            }
            i++;
        }
        int counter = 0;
        int[][] newneighbors = new int[els][3];
        for(int[] neighbor : neighbors) {
            if(neighbor != null) {
                newneighbors[counter] = neighbor;
                counter += 1;
            }
        }
        return (newneighbors);
    }

    public static int heuristic(Node start, int[] end) {
        int[] startcoords = start.getPosition();
        return (Math.abs(startcoords[0] - end[0] + Math.abs(startcoords[1] - end[1]) + Math.abs(startcoords[2] - end[2])));
    }

    public static ArrayList<Node> astar(Node start, int[] end, boolean[][][] grid) {
        ArrayList<Node> open_set = new ArrayList<Node>();
        open_set.add(start);
        ArrayList<Node> closed_set = new ArrayList<Node>();
        Node temp = null;
        while(open_set.size() > 0) {
            int lowest = 99999999;
            for(Node node : open_set) {
                if(node.getH() + node.getG() < lowest) {
                    lowest = node.getG() + node.getH();
                    temp = node;
                }
            }
            Node current = temp;
            if(current.lsequality(end)) {
                ArrayList<Node> path = new ArrayList<Node>();
                while(current.getParent() != null) {
                    path.add(current);
                    current = current.getParent();
                }
                path.add(current);
                Collections.reverse(path);
                return path;
            }

            open_set.remove(current);
            closed_set.add(current);
            int[][] nodeList = get_neighbors(grid, current, grid.length);
            ArrayList<Node> newnodeList = new ArrayList<Node>();
            for(int[] node : nodeList) {
                Node a = new Node(current, node, 10000);
                newnodeList.add(a);
            }
            for(Node node : newnodeList) {
                boolean inopen = false;
                boolean inclosed = false;

                for(Node othnode: closed_set) {
                    if(node.nodeequality(othnode)) {
                        inclosed = true;
                    }
                }
                if(!inclosed) {
                    for(Node othnode : open_set) {
                        if(node.nodeequality(othnode)) {
                            int new_g = current.getG() + 1;
                            inopen = true;
                            if(othnode.getG() > new_g) {
                                othnode.setG(new_g);
                                othnode.setParent(current);
                            }
                        }
                    }
                }
                if(!inclosed && !inopen) {
                    node.setG(current.getG() + 1);
                    node.setH(heuristic(node, end));
                    node.setParent(current);
                    open_set.add(node);
                }

            }
        }
        System.out.println("path not found");
        return null;
    }

    public static void printout(ArrayList<Node> nlist) {
        for (Node node : nlist) {
            System.out.println(Arrays.toString(node.getPosition()));
        }
    }
}

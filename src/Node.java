// Node is point where the bee can go - imaginary point
// Class gets the distance from the starting point and the distance to the end
public class Node {
    Node theparent; // Where the bee would go before this point
    int[] theposition;
    int theovr=100000;
    int g; // Distance from the starting point
    int h; // Heuristic value - estimated distance from the point to the end goal

    public Node(Node parent, int[] position, int ovr) {
        theparent = parent;
        theposition = position;
        theovr = ovr;
        g = ovr;
        h = ovr;
    }
    // Setting everything
    public void setParent(Node parent){
        theparent = parent;
    }

    public void setPosition(int[] position) {
        theposition = position;
    }

    public void setG (int g) {
        this.g = g;
    }

    public void setH ( int h) {
        this.h = h;
    }

    public int getG () {
        return g;
    }

    public Node getParent () {
        return theparent;
    }

    public int getH () {
        return h;
    }

    public int[] getPosition () {
        return theposition;
    }
    // If any node equals to another node in terms of coordinates; helps with deleting duplicate points and optimizing the code; this and the method below are just meant to help when working with the astar algorithm
// LSequality serves the same purpose it just takes in a list of coordinates as opposed to a node point as an input - again its just something that helped in the code of astar
    public boolean nodeequality(Node other) {
        return theposition[0] == other.getPosition()[0] && theposition[1] == other.getPosition()[1] && theposition[2] == other.getPosition()[2];
    }

    public boolean lsequality(int[] other) {
        return theposition[0] == other[0] && theposition[1] == other[1] && theposition[2] == other[2];
    }
}

/**
 * Created by Ákos Nikházy 2019
 *
 * Based on this
 * https://medium.com/@ssaurel/calculate-shortest-paths-in-java-by-implementing-dijkstras-algorithm-5c1db06b6541
 *
 * Added way to save path and have start and end nodes
 * thanks for stackoverflow community:
 * https://stackoverflow.com/questions/57347731/shortest-path-with-dijkstra/57354735
 */

package yzahk.in.pathfindermap;

import java.util.ArrayList;

// now we must create graph object and implement dijkstra algorithm
public class Graph {
    private Node[] nodes;
    private Edge[] edges;

    private int noOfNodes;
    private int noOfEdges;

    private int startAt;
    private int endAt;

    private ArrayList<Integer> path = new ArrayList<Integer>();
    private ArrayList<Integer> edgePath = new ArrayList<Integer>();

    public Graph(Edge[] edges) {
        this.edges = edges;
        // create all nodes ready to be updated with the edges
        this.noOfNodes = calculateNoOfNodes(edges);
        this.nodes = new Node[this.noOfNodes];


        for (int n = 0; n < this.noOfNodes; n++) {
            this.nodes[n] = new Node();
        }
        // add all the edges to the nodes, each edge added to two nodes (to and from)
        this.noOfEdges = edges.length;
        for (int edgeToAdd = 0; edgeToAdd < this.noOfEdges; edgeToAdd++) {
            this.nodes[edges[edgeToAdd].getFromNodeIndex()].getEdges().add(edges[edgeToAdd]);
            this.nodes[edges[edgeToAdd].getToNodeIndex()].getEdges().add(edges[edgeToAdd]);
        }

        // add all the edges to the nodes, each edge added to two nodes (to and from)
        this.noOfEdges = edges.length;

    }

    private static int calculateNoOfNodes(Edge[] edges) {
        int noOfNodes = 0;
        for (Edge e : edges) {
            if (e.getToNodeIndex() > noOfNodes)
                noOfNodes = e.getToNodeIndex();
            if (e.getFromNodeIndex() > noOfNodes)
                noOfNodes = e.getFromNodeIndex();
        }
        noOfNodes++;
        return noOfNodes;
    }

    public void setUpEdgeLengths(ArrayList<String> edgeData) {
        // needs a list of edge id-s that will be set to Edge.maxEdgeLength in the edge object.
        // We can't delete edges but length of Edge.maxEdgeLengthmakes makes sure it will be
        // avoided if possible. See comment in Edge.java for further info
        for (int i = 0; i < this.edges.length; i++) {

            if (edgeData.contains(String.valueOf(edges[i].getEdgeId()))) {
                this.edges[i].setLength();
            }
        }
    }

    public void calculatePath() {
        int nodeNow = endAt;
        this.path.add(endAt);
        while (nodeNow != startAt) {
            path.add(nodes[nodeNow].getPredecessor());
            nodeNow = nodes[nodeNow].getPredecessor();

        }

    }

    public void calculateEdgesPath() {
        int nodeNow = endAt;

        while (nodeNow != startAt) {
            edgePath.add(nodes[nodeNow].getPredecessorEdge());
            nodeNow = nodes[nodeNow].getPredecessor();
        }
    }

    public ArrayList<Integer> getPath() {

        return path;

    }

    public ArrayList<Integer> getEdgePath() {

        return edgePath;

    }

    /**
     * Calculates the shortest distance between two points
     * @param startpoint
     * @param endpoint
     */
    public void calculateShortestDistances(int startpoint, int endpoint) {

        startAt = startpoint;
        endAt = endpoint;

        this.nodes[startpoint].setDistanceFromSource(0);
        int nextNode = startpoint;

        // visit every node
        for (int i = 0; i < this.nodes.length; i++) {
            // loop around the edges of current node

            ArrayList<Edge> currentNodeEdges = this.nodes[nextNode].getEdges();

            for (int joinedEdge = 0; joinedEdge < currentNodeEdges.size(); joinedEdge++) {

                int neighbourIndex = currentNodeEdges.get(joinedEdge).getNeighbourIndex(nextNode);

                if (!this.nodes[neighbourIndex].isVisited()) {
                    int tentative = this.nodes[nextNode].getDistanceFromSource() + currentNodeEdges.get(joinedEdge).getLength();

                    if (tentative < nodes[neighbourIndex].getDistanceFromSource()) {
                        nodes[neighbourIndex].setDistanceFromSource(tentative);
                        nodes[neighbourIndex].setPredecessor(nextNode);
                        nodes[neighbourIndex].setPredecessorEdge(currentNodeEdges.get(joinedEdge).getEdgeId());
                    }
                }
            }

            nodes[nextNode].setVisited(true);

            nextNode = getNodeShortestDistanced();

        }

    }

    private int getNodeShortestDistanced() {
        int storedNodeIndex = 0;
        int storedDist = Integer.MAX_VALUE;
        for (int i = 0; i < this.nodes.length; i++) {
            int currentDist = this.nodes[i].getDistanceFromSource();
            if (!this.nodes[i].isVisited() && currentDist < storedDist) {
                storedDist = currentDist;
                storedNodeIndex = i;

            }
        }
        return storedNodeIndex;
    }
}
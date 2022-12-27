package g54915.model.util.algo;

import g54915.model.dto.StationDto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {

    private final StationDto station;
    private final Map<Node, Integer> adjacentNodes = new HashMap<>();
    private Integer distance = Integer.MAX_VALUE;
    private List<Node> shortestPath = new LinkedList<>();

    public Node(StationDto stop) {
        this.station = stop;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public StationDto getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Node> newShortestPath) {
        shortestPath = newShortestPath;
    }
}
package g54915.model;

import g54915.model.dto.FavoriteDto;
import g54915.model.dto.StationDto;
import g54915.model.dto.StopDto;
import g54915.model.observer.Observable;
import g54915.model.observer.Observer;
import g54915.model.repository.FavoriteRepository;
import g54915.model.repository.StopDutchRepository;
import g54915.model.repository.StopRepository;
import g54915.model.util.algo.Dijkstra;
import g54915.model.util.exception.RepositoryException;
import g54915.model.util.algo.Graph;
import g54915.model.util.algo.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.*;
import java.util.stream.Collectors;

//change stations to dutch
public class Model extends Observable {
    public static final String FAVORITE_INVALID_DATA = "invalidData";
    public static final String FAVORITE_DELETE_FAILED = "deleteFail";
    public static final String FAVORITE_ADD_FAILED_MAX = "maxFavorite";
    public static final String FAVORITE_REPOSITORY_ERROR = "repoError";
    public static final String FAVORITE_UPDATE_NO_CHANGES = "updateNoChanges";

    private final ObservableList<StationDto> pathStations = FXCollections.observableArrayList();
    private final ObservableSet<StationDto> allStations = FXCollections.observableSet();
    private final ObservableSet<FavoriteDto> allFavorites = FXCollections.observableSet();
    private final StopRepository repository = new StopRepository();
    private final StopDutchRepository dutchRepository = new StopDutchRepository();
    private final FavoriteRepository favoriteRepository = new FavoriteRepository();
    private StationDto src = null;
    private StationDto dest = null;
    private final List<String> changeStation = new ArrayList<>();
    private final HashMap<Integer, Node> allNodeStations = new HashMap<>();
    private Graph graph;
    private boolean isSearchDone = false;
    private String msgFavorite = null;

    public Model() throws RepositoryException {
        repository.getAll().forEach(dto -> allStations.add(dto.getStation()));
        resetFavorite();
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    private void makeGraph() throws RepositoryException {
        graph = new Graph();
        List<StopDto> allStops;
        List<StopDto> adjStops;

        allStations.forEach(station -> {
            allNodeStations.put(station.getKey(), new Node(station));
        });

        for (var station : allStations) {
            allStops = repository.getSame(station.getKey());
            Set<Integer> stopLines = allStops.stream()
                    .map(StopDto::getLine)
                    .collect(Collectors.toSet());

            station.setLines(stopLines);
            adjStops = repository.getAdj(allStops);
            adjStops.forEach(stops -> allNodeStations.get(station.getKey())
                    .addDestination(allNodeStations.get(stops.getStation().getKey()), 1));
        }
        allNodeStations.forEach((key, value) -> graph.addNode(value));
    }

    public void search(StationDto source, StationDto destination) throws RepositoryException {
        src = source;
        dest = destination;
        if (source == null || destination == null) {
            isSearchDone = false;
            notifyObservers();
            return;
        }
        makeGraph();
        pathStations.clear();
        Graph shortestGraph = Dijkstra.calculateShortestPathFromSource(graph, allNodeStations.get(source.getKey()));
        shortestGraph.getNodes().stream()
                .filter(node -> node.getStation().getKey().equals(destination.getKey()))
                .flatMap(node -> node.getShortestPath().stream())
                .map(Node::getStation)
                .forEachOrdered(pathStations::add);
        pathStations.add(destination);

        changeStation.clear();
        Map<Integer, Integer> previousLine = new HashMap<>();
        pathStations.get(0).getLines().forEach(line -> {
            previousLine.put(line, line);
        });

        boolean changeLine;
        for (int i = 0; i < pathStations.size(); i++) {
            changeLine = true;
            for (var line : pathStations.get(i).getLines()) {
                if (previousLine.containsKey(line)) {
                    changeLine = false;
                    break;
                }
            }

            if (changeLine) {
                previousLine.clear();
                changeStation.add(pathStations.get(i - 1).getName());
                for (var line : pathStations.get(i).getLines()) {
                    previousLine.put(line, line);
                }
            }
        }
        isSearchDone = true;
        notifyObservers();
    }

    private void resetFavorite() throws RepositoryException {
        allFavorites.clear();
        allFavorites.addAll(favoriteRepository.getAll());
    }

    public void deleteFavorite(StationDto source, StationDto destination, String name) throws RepositoryException {
        if (source == null || destination == null) {
            msgFavorite = FAVORITE_INVALID_DATA;
            notifyObservers();
            return;
        }
        var favorite = favoriteRepository.get(name);
        if (favorite != null) {
            favoriteRepository.remove(name);
        } else {
            msgFavorite = FAVORITE_DELETE_FAILED;
        }
        resetFavorite();
        notifyObservers();
    }

    public void addFavorite(StationDto source, StationDto destination, String name) throws RepositoryException {
        if (source == null || destination == null) {
            msgFavorite = FAVORITE_INVALID_DATA;
            notifyObservers();
            return;
        }
        if (allFavorites.size() > 5) {
            msgFavorite = FAVORITE_ADD_FAILED_MAX;
            notifyObservers();
            return;
        }
        var favorite = favoriteRepository.get(name);
        var newFavorite = new FavoriteDto(name, source, destination);
        if (favorite == null) {
            favoriteRepository.add(newFavorite);
        } else if (!favorite.equals(newFavorite)) {
            favoriteRepository.update(newFavorite);
        } else {
            msgFavorite = FAVORITE_UPDATE_NO_CHANGES;
        }
        resetFavorite();
        notifyObservers();
    }

    public ObservableList<StationDto> getPathStations() {
        return pathStations;
    }

    public ObservableSet<StationDto> getAllStations() {
        return allStations;
    }

    public ObservableSet<FavoriteDto> getAllFavorites() {
        return allFavorites;
    }

    public boolean isSearchDone() {
        return isSearchDone;
    }

    public String getMsgFavorite() {
        return msgFavorite;
    }

    public List<String> getChangeStation() {
        return changeStation;
    }

    public void setMsgFavorite(String msgFavorite) {
        this.msgFavorite = msgFavorite;
    }

    //change stations to dutch
    public void applyDutchChanges() throws RepositoryException {
        pathStations.clear();
        allStations.clear();
        dutchRepository.getAll().forEach(dto -> allStations.add(dto.getStation()));
        search(src,dest);
    }
}
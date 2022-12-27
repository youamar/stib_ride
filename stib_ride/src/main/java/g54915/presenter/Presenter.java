package g54915.presenter;

import g54915.model.Model;
import g54915.model.dto.StationDto;
import g54915.model.observer.Observable;
import g54915.model.observer.Observer;
import g54915.model.util.exception.RepositoryException;
import g54915.view.View;
import javafx.collections.ObservableSet;

import java.io.IOException;

//change stations to dutch
public class Presenter implements Observer {

    private final Model model;
    private final View view;

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void initialize() throws RepositoryException {
        view.initialize(model.getAllStations(), model.getAllFavorites());
    }

    @Override
    public void update(Observable observable, Object arg) {
        view.setMenuFavorite(model.getAllFavorites());
        view.setPathStations(model.getPathStations());
        view.setNbStations(model.getPathStations().size());
        view.setSearchStatus(model.isSearchDone());
        view.setMsgFavorite(model.getMsgFavorite());
        view.setChangeStation(model.getChangeStation());
    }

    public void search(StationDto source, StationDto destination) {
        model.setMsgFavorite(null);
        try {
            view.closeFavorite();
            model.search(source, destination);
        } catch (RepositoryException e) {
            throw new IllegalArgumentException("Impossible to search that data.");
        }
    }

    public void addFavorite(StationDto source, StationDto destination, String name) {
        model.setMsgFavorite(null);
        try {
            model.addFavorite(source, destination, name);
        } catch (RepositoryException e) {
            model.setMsgFavorite(Model.FAVORITE_REPOSITORY_ERROR);
            model.notifyObservers();
            return;
        }
        if (model.getMsgFavorite() == null) {
            view.closeFavorite();
        }
    }

    public void deleteFavorite(StationDto source, StationDto destination, String name) {
        model.setMsgFavorite(null);
        try {
            model.deleteFavorite(source, destination, name);
        } catch (RepositoryException e) {
            model.setMsgFavorite(Model.FAVORITE_REPOSITORY_ERROR);
            model.notifyObservers();
            return;
        }
        if (model.getMsgFavorite() == null) {
            view.closeFavorite();
        }
    }

    public void showFavoriteStage(StationDto source, StationDto destination, String name) {
        model.setMsgFavorite(null);
        try {
            view.showFavorite(source, destination, name);
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to load the FXML Controller for popup.");
        }
    }

    //change stations to dutch

    public void setFavoriteSearchableBoxes(ObservableSet<StationDto> dutchSt) {
        view.getFavoriteCtrl().initialize(dutchSt);
    }

    //change stations to dutch

    public void changeLanguageToDutch() throws RepositoryException {
        model.applyDutchChanges();
    }

    public ObservableSet<StationDto> getAllStations() {
        return model.getAllStations();
    }
}

package g54915.view;

import g54915.model.dto.FavoriteDto;
import g54915.model.dto.StationDto;
import g54915.model.util.exception.RepositoryException;
import g54915.presenter.Presenter;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class View {

    private final FxmlController ctrl;
    private FxmlControllerFavorite favoriteCtrl;
    private Stage favoriteStage;

    public View(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stib.fxml"));
        loader.setController(new FxmlController());
        Pane root = loader.load();
        ctrl = loader.getController();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        stage.setTitle("HE2B ESI - STIB Planner");
        stage.setScene(scene);
        stage.show();
        makeFavoriteStage(stage);
    }

    public void addPresenter(Presenter presenter) {
        ctrl.setPresenter(presenter);
        favoriteCtrl.setPresenter(presenter);
    }

    public void initialize(ObservableSet<StationDto> allStations, ObservableSet<FavoriteDto> allFavorites) throws RepositoryException {
        ctrl.initialize(allStations, allFavorites);
        favoriteCtrl.initialize(allStations);
    }

    public void showFavorite(StationDto source, StationDto destination, String name) throws IOException {
        favoriteStage.show();
        favoriteCtrl.setComboBox(source, destination);
        favoriteCtrl.setName(name);
    }

    private void makeFavoriteStage(Stage stage) throws IOException {
        favoriteStage = new Stage();
        favoriteStage.initModality(Modality.APPLICATION_MODAL);
        favoriteStage.initOwner(stage);

        FXMLLoader loaderFavorite = new FXMLLoader(getClass().getResource("/fxml/favorite.fxml"));
        loaderFavorite.setController(new FxmlControllerFavorite());
        Pane rootFavorite = loaderFavorite.load();
        Scene sceneFavorite = new Scene(rootFavorite);

        favoriteCtrl = loaderFavorite.getController();
        favoriteStage.setResizable(false);
        favoriteStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        favoriteStage.setTitle("HE2B ESI - STIB Planner (Favorites)");
        favoriteStage.setScene(sceneFavorite);
    }

    public void setSearchStatus(boolean isFinished) {
        ctrl.getSearchStatus().setText(isFinished ? "Search finished" : "an error occurred, please try again.");
    }

    public void setNbStations(int nb) {
        ctrl.getNbStations().setText("Number of stations : " + nb);
    }

    public void setChangeStation(List<String> listStation) {
        ctrl.setChangeStation(listStation);
    }

    public void setPathStations(ObservableList<StationDto> newList) {
        ctrl.setPathStations(newList);
    }

    public void closeFavorite() {
        favoriteStage.close();
    }

    public void setMenuFavorite(ObservableSet<FavoriteDto> allFavorites) {
        ctrl.setMenuFavorite(allFavorites);
    }

    public void setMsgFavorite(String msg) {
        favoriteCtrl.setMsgFavorite(msg);
    }

    //change stations to dutch
    public FxmlControllerFavorite getFavoriteCtrl(){
        return favoriteCtrl;
    }

}

package g54915.view;

import g54915.model.dto.FavoriteDto;
import g54915.model.dto.StationDto;
import g54915.model.util.exception.RepositoryException;
import g54915.presenter.Presenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;

//change stations to dutch
public class FxmlController {

    @FXML
    private TableView<StationDto> table;

    @FXML
    private TableColumn<StationDto, String> lineCol;

    @FXML
    private TableColumn<StationDto, String> stationCol;

    @FXML
    private Label nbStations;

    @FXML
    private Label searchStatus;

    @FXML
    private Label changeLine;

    @FXML
    private Button searchBt;

    @FXML
    private Button favoriteBt;

    @FXML
    private Menu favoriteRoutesMenu;

    @FXML
    private MenuItem quit;

    //change stations to dutch

    @FXML
    private Menu languagesMenu;

    //change stations to dutch

    @FXML
    private MenuItem dutchStations;

    @FXML
    private SearchableComboBox<StationDto> searchOrigin;

    @FXML
    private SearchableComboBox<StationDto> searchDestination;
    private ObservableList<StationDto> pathStations;
    private Presenter presenter;
    ObservableSet<StationDto> dutchSt;

    @FXML
    private void search(ActionEvent event) {
        presenter.search(searchOrigin.getValue(), searchDestination.getValue());
    }

    @FXML
    private void favorite(ActionEvent event) {
        presenter.showFavoriteStage(searchOrigin.getValue(), searchDestination.getValue(), "");
    }

    //change stations to dutch

    @FXML
    private void changeToDutch(ActionEvent event) throws RepositoryException {
        changeComboBoxesContentToDutch();
        presenter.setFavoriteSearchableBoxes(dutchSt);
    }

    //change stations to dutch

    private void changeComboBoxesContentToDutch() throws RepositoryException {

        searchOrigin.getSelectionModel().clearSelection();
        searchDestination.getSelectionModel().clearSelection();
        presenter.changeLanguageToDutch();
        dutchSt = presenter.getAllStations();
        
        searchOrigin.getItems().clear();
        searchDestination.getItems().clear();

        searchOrigin.setItems(FXCollections.observableArrayList(dutchSt));
        searchDestination.setItems(FXCollections.observableArrayList(dutchSt));
        searchOrigin.getSelectionModel().select(12);
        searchDestination.getSelectionModel().select(35);
    }

    public void initialize(ObservableSet<StationDto> allStations, ObservableSet<FavoriteDto> allFavorites) {
        pathStations = FXCollections.observableArrayList();
        stationCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lineCol.setCellValueFactory(new PropertyValueFactory<>("linesToString"));
        table.setItems(pathStations);
        searchOrigin.setItems(FXCollections.observableArrayList(allStations));
        searchDestination.setItems(FXCollections.observableArrayList(allStations));

        searchOrigin.getSelectionModel().select(12);
        searchDestination.getSelectionModel().select(35);

        setMenuFavorite(allFavorites);
        quit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public Label getSearchStatus() {
        return searchStatus;
    }

    public Label getNbStations() {
        return nbStations;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setChangeStation(List<String> changeStation) {
        StringBuilder changes = new StringBuilder("Line change : ");
        if (changeStation.isEmpty()) {
            changes.append("none");
            changeLine.setText(changes.toString());
            return;
        }
        changeStation.forEach(station -> {
            changes.append(station).append(", ");
        });
        changeLine.setText(changes.substring(0, changes.length() - 2));
    }

    public void setPathStations(ObservableList<StationDto> newPath) {
        pathStations.setAll(newPath);
    }

    public void setMenuFavorite(ObservableSet<FavoriteDto> allFavorites) {
        favoriteRoutesMenu.getItems().clear();
        if (allFavorites.isEmpty()) {
            MenuItem menuItem = new MenuItem("no favorites at the moment.");
            favoriteRoutesMenu.getItems().add(menuItem);
            menuItem.setDisable(true);
        } else {
            allFavorites.forEach(v -> {
                var item = new MenuItem(v.getKey());
                item.setOnAction(e -> presenter.showFavoriteStage(v.getSource(), v.getDestination(), v.getKey()));
                favoriteRoutesMenu.getItems().add(item);
            });
        }
    }
}

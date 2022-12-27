package g54915.view;

import g54915.model.Model;
import g54915.model.dto.StationDto;
import g54915.presenter.Presenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

//change stations to dutch
public class FxmlControllerFavorite {

    @FXML
    private SearchableComboBox<StationDto> searchOrigin;

    @FXML
    private SearchableComboBox<StationDto> searchDestination;

    @FXML
    private TextField routeName;

    @FXML
    private Button saveBt;

    @FXML
    private Button deleteBt;

    @FXML
    private Button searchBt;

    @FXML
    private Text message;
    private Presenter presenter;

    @FXML
    private void search(ActionEvent event) {
        presenter.search(searchOrigin.getValue(), searchDestination.getValue());
    }

    @FXML
    private void saveFavorite(ActionEvent event) {
        presenter.addFavorite(searchOrigin.getValue(), searchDestination.getValue(), routeName.getText());
    }

    @FXML
    private void deleteFavorite(ActionEvent event) {
        presenter.deleteFavorite(searchOrigin.getValue(), searchDestination.getValue(), routeName.getText());
    }

    public void initialize(ObservableSet<StationDto> allStations) {
        searchOrigin.setItems(FXCollections.observableArrayList(allStations));
        searchDestination.setItems(FXCollections.observableArrayList(allStations));
        searchOrigin.getSelectionModel().selectFirst();
        searchDestination.getSelectionModel().selectLast();
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setName(String name) {
        this.routeName.setText(name);
    }

    public void setMsgFavorite(String message) {
        if (message == null) {
            this.message.setText("");
            return;
        }
        String fMsg;
        switch (message) {
            case Model.FAVORITE_ADD_FAILED_MAX:
                fMsg = "You've reached your routes limit, please delete some of them.";
                break;
            case Model.FAVORITE_REPOSITORY_ERROR:
                fMsg = "An error occurred, please check what you've typed.";
                break;
            case Model.FAVORITE_DELETE_FAILED:
                fMsg = "Impossible to delete this route.";
                break;
            case Model.FAVORITE_INVALID_DATA:
                fMsg = "The given data is invalid. Please try again.";
                break;
            case Model.FAVORITE_UPDATE_NO_CHANGES:
                fMsg = "No changes to update.";
                break;
            default:
                fMsg = "";
        }
        this.message.setText(fMsg);
    }

    public void setComboBox(StationDto source, StationDto destination) {
        searchOrigin.getSelectionModel().select(source);
        searchDestination.getSelectionModel().select(destination);
    }

}

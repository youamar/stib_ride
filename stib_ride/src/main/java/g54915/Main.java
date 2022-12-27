package g54915;

import g54915.model.Model;
import g54915.model.util.config.ConfigManager;
import g54915.presenter.Presenter;
import g54915.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Entry points to the <code> StibRide </code> application...
     *
     * @param args no arguments needed.
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Main() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConfigManager.getInstance().load();
        Model model = new Model();
        View view = new View(stage);
        Presenter presenter = new Presenter(model, view);
        presenter.initialize();
        view.addPresenter(presenter);
        model.addObserver(presenter);
    }

}

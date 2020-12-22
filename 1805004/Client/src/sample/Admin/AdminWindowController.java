package sample.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import sample.MainController;

public class AdminWindowController {

    @FXML
    public DialogPane admin;
    @FXML
    public Button create;
    @FXML
    public Button delete;

    public void initialize() {
        create.setOnAction(event -> MainController.getInstance().register());

        delete.setOnAction(event -> MainController.getInstance().deleteManufacturer());
    }
}

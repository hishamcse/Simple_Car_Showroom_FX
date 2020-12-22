package sample.Viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.Common.Car;
import sample.Common.TransferPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RegSearchController {

    @FXML
    public TextField reg;
    @FXML
    public Button search;

    public void initialize() {
        search.setOnAction(event -> {
            if (reg.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill up required field");
                alert.showAndWait();
                return;
            }
            try {
                search_By_Reg();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void search_By_Reg() {
        TransferPackage tr = new TransferPackage("searchByReg", reg.getText());
        Socket socket;
        Car car = new Car();
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            if (s.equals("Error")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("This reg no doesn't exist");
                alert.showAndWait();
            }

            Task<ObservableList<Car>> task = new Task<>() {
                @Override
                protected ObservableList<Car> call() {
                    return FXCollections.observableArrayList(car.getAllCars(s, "searchByReg"));
                }
            };
            ViewerController.getInstance().tableView.setDisable(false);
            ViewerController.getInstance().tableView.itemsProperty().bind(task.valueProperty());
            ViewerController.getInstance().progressbar.progressProperty().bind(task.progressProperty());
            ViewerController.getInstance().progressbar.setVisible(true);

            task.setOnSucceeded(event -> ViewerController.getInstance().progressbar.setVisible(false));
            task.setOnFailed(event -> ViewerController.getInstance().progressbar.setVisible(false));

            new Thread(task).start();

            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

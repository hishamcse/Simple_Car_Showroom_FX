package sample.Viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import sample.Common.Car;
import sample.Common.TransferPackage;
import sample.MainController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ViewerController {

    @FXML
    public ImageView imgView;
    @FXML
    public Button viewAll;
    @FXML
    public Button searchByReg;
    @FXML
    public Button searchByMakeModel;
    @FXML
    public Button buyCar;
    @FXML
    public TableView<Car> tableView;
    @FXML
    public ProgressBar progressbar;
    @FXML
    public TableColumn<Car, Button> op;

    private static ViewerController viewerController;

    public ViewerController() {
        viewerController = this;
    }

    public static ViewerController getInstance() {
        return viewerController;
    }

    public void initialize() {
        Circle circle = new Circle(imgView.getFitWidth() / 2,
                imgView.getFitHeight() / 2, 40);
        imgView.setClip(circle);

        viewAll.setOnAction(event -> {
            tableView.setDisable(false);
            updateTableView("viewAll");
        });

        searchByReg.setOnAction(event -> {
            tableView.setDisable(true);
            search_By_Reg();
        });

        searchByMakeModel.setOnAction(event -> {
            tableView.setDisable(true);
            search_By_MakeModel();
        });

        buyCar.setOnAction(event -> {
            tableView.setDisable(false);
            updateTableView("buy");
        });
    }

    public void updateTableView(String state) {
        TransferPackage tr = new TransferPackage("viewAll", " ");
        Socket socket;
        Car car = new Car();
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            Task<ObservableList<Car>> task = new Task<>() {
                @Override
                protected ObservableList<Car> call() {
                    return FXCollections.observableArrayList(car.getAllCars(s, state));
                }
            };
            tableView.itemsProperty().bind(task.valueProperty());
            progressbar.progressProperty().bind(task.progressProperty());
            progressbar.setVisible(true);

            task.setOnSucceeded(event -> progressbar.setVisible(false));
            task.setOnFailed(event -> progressbar.setVisible(false));

            new Thread(task).start();
            oos.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void search_By_Reg() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(MainController.getInstance().borderPane.getScene().getWindow());
        dialog.setTitle("Search Car By Registration No");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("regSearch.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            System.out.println("ok button pressed");
        } else {
            System.out.println("cancel");
        }
    }

    public void search_By_MakeModel() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(MainController.getInstance().borderPane.getScene().getWindow());
        dialog.setTitle("Search Car By Make & Model");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("makeModelSearch.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            System.out.println("ok button pressed");
        } else {
            System.out.println("cancel");
        }
    }

    public void buyACar() {
        Car c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Are you sure you want to buy this car?" + c.getRegNo());
            Optional<ButtonType> o = alert.showAndWait();
            if (o.isPresent() && o.get() == ButtonType.OK) {
                TransferPackage tr = new TransferPackage("buy", c.getRegNo());
                Socket socket;
                try {
                    socket = new Socket("localhost", 5000);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(tr);
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    String s = (String) ois.readObject();
                    System.out.println(s);
                    if (s.equals("Error")) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Warning");
                        alert.setContentText("Car is out of stock now!!");
                        alert.showAndWait();
                    } else {
                        ViewerController.getInstance().updateTableView("viewAll");
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Congrats!!");
                        alert.setContentText("You have successfully bought the car");
                        alert.showAndWait();
                    }
                    ois.close();
                    oos.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
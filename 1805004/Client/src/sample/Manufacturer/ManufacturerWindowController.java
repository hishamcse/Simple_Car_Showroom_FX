package sample.Manufacturer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import sample.Common.Car;
import sample.Common.TransferPackage;
import sample.LoginRegister.LoginController;
import sample.MainController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ManufacturerWindowController {

    @FXML
    public ImageView imgView;
    @FXML
    public Label label;
    @FXML
    public Button viewAll;
    @FXML
    public Button addCar;
    @FXML
    public Button edit;
    @FXML
    public Button delete;
    @FXML
    public TableView<Car> tableView;
    @FXML
    public ProgressBar progressbar;
    @FXML
    public TableColumn<Car, Button> op;

    public String edited;

    private static ManufacturerWindowController manufacturerWindowController;

    public ManufacturerWindowController() {
        manufacturerWindowController = this;
    }

    public static ManufacturerWindowController getInstance() {
        return manufacturerWindowController;
    }

    public void initialize() {
        Circle circle = new Circle(imgView.getFitWidth() / 2,
                imgView.getFitHeight() / 2, 40);
        imgView.setClip(circle);
        loginWindow();

        viewAll.setOnAction(event -> {
            tableView.setDisable(false);
            updateTableView("viewAll");
        });

        addCar.setOnAction(event -> {
            tableView.setDisable(true);
            addCarToDataBase();
        });

        edit.setOnAction(event -> {
            tableView.setDisable(false);
            updateTableView("edit");
        });

        delete.setOnAction(event -> {
            tableView.setDisable(false);
            updateTableView("delete");
        });
    }

    public void loginWindow() {
        String[] splits = LoginController.getInstance().string.split(",");
        String name = splits[0];
        String img = null;
        if (splits.length > 1) {
            img = splits[1];
        }
        System.out.println("login");
        if (img != null) {
            imgView.setImage(new Image(img));
        }
        label.setText(name);
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

            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addCarToDataBase() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(MainController.getInstance().borderPane.getScene().getWindow());
        dialog.setTitle("Add A Car");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addCar.fxml"));
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

    public void editCar() {
        Car c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            edited = c.getRegNo() + "," + c.getYear() + "," + c.getColor1() + "," + c.getColor2() + "," + c.getColor3() + ","
                    + c.getCarMake() + "," + c.getCarModel() + "," + c.getPrice() + "," + c.getQuantity() + ",";
            if (c.getCarImage().getImage() != null) {
                edited = edited + c.getCarImage().getImage().getUrl();
            } else {
                edited = edited + " ";
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(MainController.getInstance().borderPane.getScene().getWindow());
            dialog.setTitle("Edit Car");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editCar.fxml"));
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
    }

    public void deleteCar() {
        Car c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            edited = c.getRegNo();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Delete the car with this registration no '" + c.getRegNo() + "'?");
            Optional<ButtonType> o = alert.showAndWait();
            if (o.isPresent() && o.get() == ButtonType.OK) {
                TransferPackage tr = new TransferPackage("deleteCar", c.getRegNo());
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
                        alert.setContentText("Car with this registration no doesn't exist");
                        alert.showAndWait();
                    } else {
                        ManufacturerWindowController.getInstance().updateTableView("delete");
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Congrats!!");
                        alert.setContentText("The car has been successfully deleted");
                        alert.showAndWait();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
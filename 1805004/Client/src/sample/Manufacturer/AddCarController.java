package sample.Manufacturer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import sample.Common.TransferPackage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddCarController {

    @FXML
    public DialogPane dialog;
    @FXML
    public ImageView imgView;
    @FXML
    public TextField reg, year, Color1, Color2, Color3, make, model, price, quantity;
    @FXML
    public Button addButton;

    public void initialize() {
        Circle circle = new Circle(imgView.getFitWidth() / 2,
                imgView.getFitHeight() / 2, 50);
        imgView.setClip(circle);

        imgView.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("hello");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("images", "*.png", "*.jpg", "*.bmp"));
            File file = fileChooser.showOpenDialog(dialog.getScene().getWindow());
            imgView.setImage(new Image(file.toURI().toString()));
        });

        addButton.setOnAction(event -> {
            if (reg.getText().isBlank() || year.getText().isBlank() || make.getText().isBlank() ||
                    model.getText().isBlank() || price.getText().isBlank() || quantity.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill up required fields");
                alert.showAndWait();
                return;
            } else {
                try {
                    Integer.parseInt(year.getText());
                    Integer.parseInt(quantity.getText());
                    Double.parseDouble(price.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Warning");
                    alert.setContentText("Year or quantity or price field is invalid");
                    alert.showAndWait();
                    return;
                }
            }
            try {
                newCar();
            } catch (Exception w) {
                System.err.println("error");
            }
        });
    }

    public void newCar() {
        String sb = reg.getText() + "," + year.getText() + "," + Color1.getText() + "," + Color2.getText() + "," +
                Color3.getText() + "," + make.getText() + "," + model.getText() + "," + price.getText() + "," +
                quantity.getText() + "," + imgView.getImage().getUrl();
        TransferPackage tr = new TransferPackage("addCar", sb);
        Socket socket;
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            System.out.println(s);
            if (s.equals("Successful")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Car Info");
                alert.setHeaderText("Congrats!!");
                alert.setContentText("Adding Car Info successful");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("There already exists car with this registration no");
                alert.showAndWait();
            }
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
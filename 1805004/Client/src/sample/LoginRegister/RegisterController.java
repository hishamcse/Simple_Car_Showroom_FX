package sample.LoginRegister;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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
import java.util.Optional;

public class RegisterController {

    @FXML
    private DialogPane rego;
    @FXML
    public Button regButton;
    @FXML
    public TextField name;
    @FXML
    public PasswordField password;
    @FXML
    public ImageView imgView;

    public void initialize() {
        Circle circle = new Circle(imgView.getFitWidth() / 2,
                imgView.getFitHeight() / 2, 50);
        imgView.setClip(circle);

        regButton.setOnAction(event -> {
            if (name.getText().equals("") || password.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill up all");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Submit");
            alert.setContentText("Are you sure to continue");
            Optional<ButtonType> o = alert.showAndWait();
            if (o.isPresent() && o.get() == ButtonType.OK) {
                try {
                    newAccount();
                } catch (Exception w) {
                    System.err.println("error");
                }
            }
        });

        imgView.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("hello");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("images", "*.png", "*.jpg", "*.bmp"));
            File file = fileChooser.showOpenDialog(rego.getScene().getWindow());
            imgView.setImage(new Image(file.toURI().toString()));
        });
    }

    @FXML
    private void newAccount() {
        String newName = name.getText().trim();
        String newPassword = password.getText().trim();
        String image = " ";
        if (imgView.getImage() != null) {
            image = imgView.getImage().getUrl();
            System.out.println(image);
        }

        String str = newName + "," + newPassword + "," + image;
        TransferPackage tr = new TransferPackage("create", str);
        Socket socket;
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            System.out.println(s);
            if (!s.equals("Successful")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("There already exists Manufacturer account with this name");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Creation");
                alert.setHeaderText("Congrats!!");
                alert.setContentText("Account creation successful");
                alert.showAndWait();
            }
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
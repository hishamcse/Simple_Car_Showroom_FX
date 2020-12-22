package sample.LoginRegister;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Common.TransferPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class DeleteAccController {

    @FXML
    public Button delButton;
    @FXML
    public TextField name;
    @FXML
    public PasswordField password;

    public void initialize() {

        delButton.setOnAction(event -> {
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
            alert.setContentText("Are you sure to delete this manufacturer?");
            Optional<ButtonType> o = alert.showAndWait();
            if (o.isPresent() && o.get() == ButtonType.OK) {
                try {
                    deleteAccount();
                } catch (Exception w) {
                    System.err.println("error");
                }
            }
        });
    }

    @FXML
    private void deleteAccount() {
        String newName = name.getText().trim();
        String newPassword = password.getText().trim();
        String str = newName + "," + newPassword;
        TransferPackage tr = new TransferPackage("delete", str);
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
                alert.setContentText("There doesn't exist such manufacturer");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Creation");
                alert.setHeaderText("Congrats!!");
                alert.setContentText("Account deletion successful");
                alert.showAndWait();
            }
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
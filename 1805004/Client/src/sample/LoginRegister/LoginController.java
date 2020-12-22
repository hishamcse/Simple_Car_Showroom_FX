package sample.LoginRegister;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.Common.TransferPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginController {

    @FXML
    public Button btn;
    @FXML
    public Text welcome;
    @FXML
    public DialogPane dialogPane;
    @FXML
    public TextField textField;
    @FXML
    public PasswordField passWord;
    @FXML
    public Text actionTarget;

    private static LoginController loginController;
    public String string = null;

    public LoginController() {
        loginController = this;
    }

    public static LoginController getInstance() {
        return loginController;
    }

    public void initialize() {

        btn.setOnAction(e -> {
            if (textField.getText().equals("") && passWord.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("No Entry detected");
                alert.showAndWait();
                return;
            } else if (textField.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("No Entry for UserName");
                alert.showAndWait();
                return;
            } else if (passWord.getText().equals("") && !textField.getText().equalsIgnoreCase("viewer")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("No Entry for Password");
                alert.showAndWait();
                return;
            }
            actionTarget.setFill(Color.FIREBRICK);
            actionTarget.setText("Sign in button pressed");
            try {
                logIn();
            } catch (Exception ignored) {

            }
        });
    }

    private void logIn() {
        String name = textField.getText().trim();
        String pass = passWord.getText().trim();
        System.out.println("login");
        String str = name + "," + pass;
        TransferPackage tr = new TransferPackage("login", str);
        Socket socket;
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            System.out.println(s);
            if (s.equals("Unsuccessful")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("No account with this username or password");
                alert.showAndWait();
            } else {
                System.out.println(s);
                string = s;
            }
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
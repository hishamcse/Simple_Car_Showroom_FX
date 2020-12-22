package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import sample.LoginRegister.LoginController;

import java.io.*;
import java.util.Optional;

public class MainController {

    @FXML
    public BorderPane borderPane;
    @FXML
    public AnchorPane anchorPane;

    private static MainController mainController;

    public MainController() {
        mainController = this;
    }

    public static MainController getInstance() {
        return mainController;
    }

    public void initialize() {

    }

    @FXML
    public void admin() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Admin Panel");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Admin/adminLogin.fxml"));
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

    @FXML
    public void register() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Register");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("LoginRegister/register.fxml"));
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

    public void deleteManufacturer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Delete Manufacturer");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("LoginRegister/deleteAcc.fxml"));
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

    @FXML
    public void login() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Login");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("LoginRegister/login.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK &&
                !LoginController.getInstance().textField.getText().equalsIgnoreCase("viewer")) {
            manufacturerWindow();
        } else if (buttonType.isPresent() && buttonType.get() == ButtonType.OK &&
                LoginController.getInstance().textField.getText().equalsIgnoreCase("viewer")) {
            viewerWindow();
        } else {
            System.out.println("cancel");
        }
    }

    public void manufacturerWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Manufacturer/manufacturerWindow.fxml"));
        try {
            borderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewerWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Viewer/viewer.fxml"));
        try {
            borderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
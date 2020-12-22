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

public class EditCarController {

    @FXML
    public DialogPane dialog;
    @FXML
    public ImageView imgView;
    @FXML
    public TextField reg, year, Color1, Color2, Color3, make, model, price, quantity;
    @FXML
    public Button addButton;

    public void initialize() {
        Circle circle = new Circle(imgView.getFitWidth() / 2, imgView.getFitHeight() / 2, 50);
        imgView.setClip(circle);

        String[] strings = ManufacturerWindowController.getInstance().edited.split(",");
        reg.setText(strings[0]);
        year.setText(strings[1]);
        Color1.setText(strings[2]);
        Color2.setText(strings[3]);
        Color3.setText(strings[4]);
        make.setText(strings[5]);
        model.setText(strings[6]);
        price.setText(strings[7]);
        quantity.setText(strings[8]);
        if (strings[9].split(":")[0].equals("file")) {
            imgView.setImage(new Image(strings[9]));
        } else {
            imgView.setImage(new Image("file:F:\\HISHAM_CSE  ASUS\\CSE L-1,T-2\\CSE 108 SESSIONAL\\CSE 108 SESSIONAL\\Week 10\\1805004\\Client\\src\\sample\\Images\\demo.jpg"));
        }

        reg.setEditable(false);

        imgView.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Image");
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
                editCar();
            } catch (Exception w) {
                System.err.println("error");
            }
        });
    }

    public void editCar() {
        String sb = reg.getText() + "," + reg.getText() + "," + year.getText() + "," + Color1.getText() + "," + Color2.getText() + "," +
                Color3.getText() + "," + make.getText() + "," + model.getText() + "," + price.getText() + "," +
                quantity.getText() + "," + imgView.getImage().getUrl();
        TransferPackage tr = new TransferPackage("editCar", sb);
        Socket socket;
        try {
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(tr);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String s = (String) ois.readObject();
            System.out.println(s);
            if (s.equals("Error")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Warning");
                alert.setContentText("Car with this registration no doesn't exist");
                alert.showAndWait();
            } else {
                ManufacturerWindowController.getInstance().updateTableView("edit");
            }
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
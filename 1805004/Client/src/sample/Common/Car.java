package sample.Common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Manufacturer.ManufacturerWindowController;
import sample.Viewer.ViewerController;

public class Car {

    private String regNo;
    private String year;
    private String color1, color2, color3;
    private String carMake;
    private String carModel;
    private String price;
    private String quantity;
    private ImageView carImage = new ImageView();
    private Button option;
    private ObservableList<Car> observableList;

    public Car(){
        observableList = FXCollections.observableArrayList();
    }

    public Car(String regNo, String year, String color1, String color2, String color3, String carMake, String carModel, String price, String quantity, String carImage, String option) {
        this.regNo = regNo;
        this.year = year;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.carMake = carMake;
        this.carModel = carModel;
        this.price = price;
        this.quantity = quantity;
        if(carImage.split(":")[0].equals("file")) {
            System.out.println("ok");
            this.carImage.setImage(new Image(carImage));
            this.carImage.setFitWidth(75);
            this.carImage.setFitHeight(55);
        }else{
            this.carImage.setImage(null);
        }
        this.option = new Button(option);
        switch (option) {
            case "edit":
                this.option.setVisible(true);
                this.option.setStyle("-fx-text-fill: #5adadc");
                this.option.setOnAction(event -> ManufacturerWindowController.getInstance().editCar());
                break;
            case "delete":
                this.option.setVisible(true);
                this.option.setStyle("-fx-text-fill: #ffccaf");
                this.option.setOnAction(event -> ManufacturerWindowController.getInstance().deleteCar());
                break;
            case "buy":
                this.option.setVisible(true);
                this.option.setStyle("-fx-text-fill: white");
                this.option.setOnAction(event -> ViewerController.getInstance().buyACar());
                break;
            default:
                this.option.setVisible(false);
                break;
        }
    }

    public String getRegNo() {
        return regNo;
    }

    public String getYear() {
        return year;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public String getColor3() {
        return color3;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public ImageView getCarImage() {
        return carImage;
    }

    public Button getOption() {
        return option;
    }

    public ObservableList<Car> getAllCars(String str, String state){
        if(state.equals("viewAll")){
            state = "Show";
        }else if(state.equals("searchByReg") || state.equals("searchByMakeModel")){
            state = " ";
        }
        String[] splits = str.split("\n");
        for (String sp : splits) {
            String[] strings = sp.split(",");
            observableList.addAll(new Car(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6],
                    strings[7], strings[8], strings[9], state));
        }
        System.out.println(observableList);
        return observableList;
    }

    @Override
    public String toString() {
        return regNo + ',' + year + ',' + color1 + ',' + color2 + ',' + color3 + ',' + carMake + ',' + carModel + ',' + price;
    }
}
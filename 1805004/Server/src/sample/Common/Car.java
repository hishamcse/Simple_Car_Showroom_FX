package sample.Common;

public class Car {

    private final String regNo;
    private final int year;
    private final String color1, color2, color3;
    private final String carMake;
    private final String carModel;
    private final double price;
    private int quantity;
    private String image;

    public Car(String regNo, int year, String color1, String color2, String color3, String carMake, String carModel, double price, int quantity, String image) {
        this.regNo = regNo;
        this.year = year;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.carMake = carMake;
        this.carModel = carModel;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return regNo + ',' + year + ',' + color1 + ',' + color2 + ',' + color3 + ',' + carMake + ',' + carModel + ',' + price;
    }
}
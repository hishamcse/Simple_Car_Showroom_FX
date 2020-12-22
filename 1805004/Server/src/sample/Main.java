package sample;

import sample.AdminPanel.Admin;
import sample.Common.TransferPackage;
import sample.Manufacturer.CarWareHouseManufacturer;
import sample.Manufacturer.Manufacturer;
import sample.Viewer.CarWareHouseViewer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main {

    private static final Admin admin = new Admin();
    private static final Manufacturer account = new Manufacturer();
    private static final CarWareHouseManufacturer manufacturers = new CarWareHouseManufacturer();
    private static final CarWareHouseViewer viewers = new CarWareHouseViewer();

    public static void main(String[] args) {

        new Thread(() -> {
            System.out.println("thread");
            try (ServerSocket serverSocket = new ServerSocket(5000)) {
                System.out.println("server Thread");
                while (true) {

                    if (!admin.open()) {
                        System.out.println("can't open");
                        return;
                    }
                    if (!account.open()) {
                        System.out.println("can't open");
                        return;
                    }
                    if (!manufacturers.open()) {
                        System.out.println("can't open");
                        return;
                    }
                    if (!viewers.open()) {
                        System.out.println("can't open");
                        return;
                    }
                    System.out.println("waiting for socket");
                    Socket socket = serverSocket.accept();
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    TransferPackage tr = (TransferPackage) objectInputStream.readObject();
                    System.out.println("connected to client");
                    String s = null;
                    switch (tr.getOperation()) {
                        case "admin":
                            s = admin.adminLogin(tr.getContent());
                            break;
                        case "create":
                            if (account.addManufacturer(tr.getContent())) {
                                s = "Successful";
                            } else {
                                s = "Unsuccessful";
                            }
                            break;
                        case "delete":
                            if (account.deleteManufacturer(tr.getContent())) {
                                s = "Successful";
                            } else {
                                s = "Unsuccessful";
                            }
                            break;
                        case "login":
                            if (tr.getContent().split(",")[0].equalsIgnoreCase("viewer")) {
                                s = "Success";
                            } else {
                                s = account.manufacturerLogin(tr.getContent());
                            }
                            break;
                        case "viewAll":
                            s = manufacturers.view_all_cars();
                            break;
                        case "addCar":
                            s = manufacturers.addCar(tr.getContent());
                            break;
                        case "editCar":
                            s = manufacturers.updateCar(tr.getContent());
                            break;
                        case "deleteCar":
                            s = manufacturers.deleteCar(tr.getContent());
                            break;
                        case "searchByReg":
                            s = viewers.Car_By_Reg(tr.getContent());
                            break;
                        case "searchByMakeModel":
                            s = viewers.Cars_By_MakeAndModel(tr.getContent());
                            break;
                        case "buy":
                            s = viewers.buy_A_Car(tr.getContent());
                            break;
                    }
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(s);
                    objectOutputStream.close();
                    objectInputStream.close();
                    admin.close();
                    account.close();
                    manufacturers.close();
                    viewers.close();
                }
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
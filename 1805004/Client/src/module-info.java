module Client {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires AnimateFX;

    opens sample;
    opens sample.Common;
    opens sample.LoginRegister;
    opens sample.Manufacturer;
    opens sample.Viewer;
    opens sample.Admin;
}
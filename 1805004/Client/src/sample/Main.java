package sample;

import animatefx.animation.FadeIn;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    public static final String text = "Car Showroom";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Car Showroom");
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        Text anim = new Text();
        VBox root2 = new VBox(anim);
        root2.setId("background");
        root2.setAlignment(Pos.CENTER);
        primaryStage.setTitle("Car Showroom");
        Image image = new Image(getClass().getResource("/sample/Images/icon2.jpg").toExternalForm());
        primaryStage.getIcons().add(image);
        Scene scene = new Scene(root2, 1150, 750);
        scene.getStylesheets().addAll(getClass().getResource("file.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        new FadeIn(scene.getRoot()).play();

        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(150),
                event -> {
                    if (i.get() > text.length()) {
                        timeline.stop();
                        primaryStage.setScene(new Scene(root, 1150, 750));
                        primaryStage.show();
                    } else {
                        anim.setText(text.substring(0, i.get()));
                        anim.setFont(Font.font(50));
                        anim.setFill(Color.WHEAT);
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.satispay.instore.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * --> Created by domenicovisconti on 15/09/16.
 */
public class InStoreApiClientApplication extends Application {

    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getClassLoader().getResource("layouts/home_page.fxml");

        if (resource != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.strings", Locale.getDefault()));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setMinWidth(640);
            stage.setMinHeight(400);
            stage.setScene(new Scene(root, 640, 400));
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

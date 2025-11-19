package com.future.wordstats;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1035, 750);
        stage.setTitle("Word Statistics");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
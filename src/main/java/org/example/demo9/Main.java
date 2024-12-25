package org.example.demo9;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.demo9.ui.MainMenu;
import javafx.scene.image.Image;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage);

        StackPane root = new StackPane();
        root.getChildren().addAll(mainMenu.getRoot());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Лисы и Куры");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
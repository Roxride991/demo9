package org.example.demo9.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class SettingsScreen {
    private VBox root;
    private Stage stage;

    public SettingsScreen(Stage stage) {
        this.stage = stage;
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("Настройки");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Label difficultyLabel = new Label("Сложность игры:");
        ComboBox<String> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Легкая", "Средняя", "Сложная");
        difficultyComboBox.setValue("Средняя");

        // Back button
        Button backButton = new Button("Выйти в главное меню");
        backButton.setStyle("-fx-font-size: 16;");
        backButton.setOnAction(e -> returnToMainMenu());

        root.getChildren().addAll(titleLabel, difficultyLabel, difficultyComboBox, backButton);
    }

    private void returnToMainMenu() {
        MainMenu mainMenu = new MainMenu(stage);
        Scene mainMenuScene = new Scene(mainMenu.getRoot(), 800, 600);
        stage.setScene(mainMenuScene);
    }

    public VBox getRoot() {
        return root;
    }
}

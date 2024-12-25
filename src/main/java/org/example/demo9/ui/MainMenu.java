package org.example.demo9.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import org.example.demo9.ai.Bot;
import org.example.demo9.ai.MediumBot;

public class MainMenu {
    private VBox root;
    private Stage stage;

    public MainMenu(Stage stage) {
        this.stage = stage;
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("Лисы и куры");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        // Buttons
        Button startGameBtn = createMenuButton("Начать игру");
        Button settingsBtn = createMenuButton("Настройки");
        Button rulesBtn = createMenuButton("Правила");
        Button exitBtn = createMenuButton("Выход");

        // Add event handlers
        startGameBtn.setOnAction(e -> startGame());
        settingsBtn.setOnAction(e -> showSettings());
        rulesBtn.setOnAction(e -> showRules());
        exitBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(titleLabel, startGameBtn, settingsBtn, rulesBtn, exitBtn);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 18; -fx-min-width: 200;");
        return button;
    }

    private void startGame() {
        Bot bot = new MediumBot();
        GameBoardUI gameBoardUI = new GameBoardUI(bot);
        Scene gameScene = new Scene(gameBoardUI, 800, 600);
        stage.setScene(gameScene);
        stage.setResizable(false); // Окно нельзя изменять по размеру

    }

    private void showSettings() {
        SettingsScreen settings = new SettingsScreen(stage);
        Scene settingsScene = new Scene(settings.getRoot(), 800, 600);
        stage.setScene(settingsScene);
        stage.setResizable(false); // Окно нельзя изменять по размеру

    }

    private void showRules() {
        RulesScreen rules = new RulesScreen(stage);
        Scene rulesScene = new Scene(rules.getRoot(), 800, 600);
        stage.setScene(rulesScene);
        stage.setResizable(false); // Окно нельзя изменять по размеру

    }

    public VBox getRoot() {
        return root;
    }
}

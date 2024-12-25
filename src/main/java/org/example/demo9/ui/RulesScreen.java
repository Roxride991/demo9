package org.example.demo9.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.TextAlignment;

public class RulesScreen {
    private VBox root;
    private Stage stage;

    public RulesScreen(Stage stage) {
        this.stage = stage;
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("Правила Игры");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        VBox rulesContent = new VBox(10);
        rulesContent.setAlignment(Pos.CENTER_LEFT);

        String[] rules = {
                "• Игра ведется на доске размером 7х7 с некоторыми недопустимыми позициями по углам",
                "• 2 Лисы против 20 куп",
                "• Лисы начинают в середине, куры - снизу",
                "",
                "Правила передвижения:",
                "• Куры могут перемещаться вверх, влево или вправо на соседние пустые клетки",
                "• Лисы могут перемещаться в любом направлении (вверх, вниз, влево, вправо) на соседние пустые клетки",
                "• Лисы могут съедать кур, перепрыгивая через них на пустую клетку",
                "",
                "Условия выигрыша:",
                "• Куры выигрывают, если 9 кур попадают в три верхних ряда",
                "• Лисы выигрывают, если захватывают достаточное количество кур, чтобы куры не могли победить (12 или более захватов)"
        };

        for (String rule : rules) {
            Label ruleLabel = new Label(rule);
            ruleLabel.setStyle("-fx-font-size: 14;");
            ruleLabel.setWrapText(true);
            ruleLabel.setTextAlignment(TextAlignment.LEFT);
            rulesContent.getChildren().add(ruleLabel);
        }

        ScrollPane scrollPane = new ScrollPane(rulesContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f0f0; -fx-background-color: #f0f0f0;");

        Button backButton = new Button("Выйти в главное меню");
        backButton.setStyle("-fx-font-size: 16;");
        backButton.setOnAction(e -> returnToMainMenu());

        root.getChildren().addAll(titleLabel, scrollPane, backButton);
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

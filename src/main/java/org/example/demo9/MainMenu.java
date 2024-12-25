package org.example.demo9;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo9.model.GameBoard;
import org.example.demo9.model.Move;
import org.example.demo9.model.Piece;
import org.example.demo9.model.Fox;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.List;

public class MainMenu {
    private VBox root;
    private GameBoard gameBoard;
    private GridPane boardUI;
    private Stage primaryStage;
    private boolean gameEnded = false;
    private Button restartButton;
    private Piece selectedPiece = null;

    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        // Создаем кнопки
        restartButton = new Button("Начать заново");
        restartButton.setOnAction(e -> restartGame());
        restartButton.setVisible(false);

        Button exitButton = new Button("Выйти из игры");
        exitButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Выход из игры");
            alert.setContentText("Вы действительно хотите выйти из игры?");

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    primaryStage.close();
                }
            });
        });

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(restartButton, exitButton);

        initializeGame();
        root.getChildren().add(buttonBox);
    }

    private void initializeGame() {
        gameBoard = new GameBoard();
        gameEnded = false;
        selectedPiece = null;

        initializeBoard();
        updateBoard();

        if (!root.getChildren().contains(restartButton)) {
            root.getChildren().add(restartButton);
        }
        restartButton.setVisible(false);
    }

    private void initializeBoard() {
        root.getChildren().removeIf(node -> node instanceof GridPane);

        boardUI = new GridPane();
        boardUI.setHgap(10);
        boardUI.setVgap(10);
        boardUI.setAlignment(Pos.CENTER);

        for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < GameBoard.BOARD_SIZE; j++) {
                if (gameBoard.isValidPosition(i, j)) {
                    Circle cell = new Circle(20);
                    cell.setFill(Color.LIGHTGRAY);
                    cell.setStroke(Color.BLACK);

                    final int row = i;
                    final int col = j;
                    cell.setOnMouseClicked(e -> handleCellClick(row, col));

                    boardUI.add(cell, j, i);
                }
            }
        }

        root.getChildren().add(0, boardUI);
    }

    private void handleCellClick(int row, int col) {
        if (gameEnded) {
            showMessage("Игра окончена", "Нажмите 'Начать заново' для новой игры");
            return;
        }

        Piece clickedPiece = gameBoard.getPiece(row, col);

        if (selectedPiece == null) {
            // First click - select piece
            if (clickedPiece instanceof Fox) {
                selectedPiece = clickedPiece;
                highlightValidMoves(selectedPiece);
            }
        } else {
            // Second click - try to move
            List<Move> validMoves = gameBoard.getValidMoves(selectedPiece);
            Move selectedMove = null;

            // Find if this is a valid move
            for (Move move : validMoves) {
                if (move.getNewRow() == row && move.getNewCol() == col) {
                    selectedMove = move;
                    break;
                }
            }

            if (selectedMove != null) {

                gameBoard.makeMove(selectedMove);
                updateBoard();

                // Отладочная информация
                System.out.println("Количество оставшихся кур: " + gameBoard.getChickens().size());
                System.out.println("Проверка условия победы лис: " + gameBoard.isFoxWin());

                // Проверка условий победы
                if (gameBoard.isFoxWin()) {
                    System.out.println("Лисы победили!");
                    gameEnded = true;
                    showVictoryPopup("Победа лис!",
                            "Лисы поймали " + (20 - gameBoard.getChickens().size()) +
                                    " кур! (Осталось: " + gameBoard.getChickens().size() + " кур)");
                    restartButton.setVisible(true);
                } else if (isChickenWin()) {
                    System.out.println("Куры победили!");
                    gameEnded = true;
                    showVictoryPopup("Победа кур!", "Девять кур успешно добрались до верхних рядов!");
                    restartButton.setVisible(true);
                }
            }

            selectedPiece = null;
            unhighlightAllCells();
        }
    }

    private boolean isChickenWin() {
        int chickensInTopRows = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < GameBoard.BOARD_SIZE; col++) {
                if (gameBoard.isValidPosition(row, col)) {
                    Piece piece = gameBoard.getPiece(row, col);
                    if (piece instanceof org.example.demo9.model.Chicken) {
                        chickensInTopRows++;
                        if (chickensInTopRows >= 9) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void highlightValidMoves(Piece piece) {
        List<Move> validMoves = gameBoard.getValidMoves(piece);
        for (Move move : validMoves) {
            Circle cell = (Circle) getNodeFromGridPane(boardUI, move.getNewCol(), move.getNewRow());
            if (cell != null) {
                cell.setStroke(Color.GREEN);
                cell.setStrokeWidth(3);
            }
        }
    }

    private void unhighlightAllCells() {
        for (javafx.scene.Node node : boardUI.getChildren()) {
            if (node instanceof Circle) {
                Circle cell = (Circle) node;
                cell.setStroke(Color.BLACK);
                cell.setStrokeWidth(1);
            }
        }
    }

    private void showVictoryPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Конец игры!");
        alert.setHeaderText(title);
        alert.setContentText(message);

        Button okButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.setText("Понятно");

        alert.showAndWait();
    }

    private void restartGame() {
        initializeGame();
    }

    private void updateBoard() {

        for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < GameBoard.BOARD_SIZE; j++) {
                if (gameBoard.isValidPosition(i, j)) {
                    Circle cell = (Circle) getNodeFromGridPane(boardUI, j, i);
                    if (cell != null) {
                        updateCellAppearance(cell, gameBoard.getPiece(i, j));
                    }
                }
            }
        }
    }

    private void updateCellAppearance(Circle cell, Piece piece) {
        if (piece == null) {
            cell.setFill(Color.LIGHTGRAY);
        } else if (piece instanceof Fox) {
            cell.setFill(Color.RED);
        } else if (piece instanceof org.example.demo9.model.Chicken) {
            cell.setFill(Color.YELLOW);
        }
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Button okButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        okButton.setText("Ок");

        alert.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}

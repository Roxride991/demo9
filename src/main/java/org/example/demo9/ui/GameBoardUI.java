package org.example.demo9.ui;

import javafx.geometry.Pos;
import org.example.demo9.model.*;
import org.example.demo9.ai.Bot;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class GameBoardUI extends GridPane {
    private static final int CELL_SIZE = 60;
    private GameBoard gameBoard;
    private StackPane[][] cells;
    private Piece selectedPiece;
    private boolean isPlayerTurn;
    private Bot bot;

    public GameBoardUI(Bot bot) {
        this.bot = bot;
        this.gameBoard = new GameBoard();
        this.cells = new StackPane[GameBoard.BOARD_SIZE][GameBoard.BOARD_SIZE];
        this.isPlayerTurn = true;
        initializeBoard();
        setAlignment(Pos.CENTER);
    }

    private void initializeBoard() {
        for (int row = 0; row < GameBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < GameBoard.BOARD_SIZE; col++) {
                if (gameBoard.isValidPosition(row, col)) {
                    StackPane cell = createCell(row, col);
                    cells[row][col] = cell;
                    add(cell, col, row);
                } else {
                    Rectangle invalid = new Rectangle(CELL_SIZE, CELL_SIZE);
                    invalid.setFill(Color.GRAY);
                    add(invalid, col, row);
                }
            }
        }
        updateBoard();
    }

    private StackPane createCell(int row, int col) {
        Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);

        StackPane cell = new StackPane(background);
        cell.setOnMouseClicked(e -> handleCellClick(row, col));
        return cell;
    }

    private void handleCellClick(int row, int col) {
        if (!isPlayerTurn) {
            return;
        }

        Piece piece = gameBoard.getPiece(row, col);

        if (selectedPiece == null) {
            if (piece instanceof Chicken) {
                selectedPiece = piece;
                highlightValidMoves(piece);
            }
        } else {
            if (piece == null) {
                // Try to move to empty cell
                Move move = findMove(selectedPiece, row, col);
                if (move != null) {
                    makeMove(move);
                    selectedPiece = null;
                    clearHighlights();
                    isPlayerTurn = false;
                    makeBotMove();
                }
            } else if (piece instanceof Chicken) {
                // Select different chicken
                selectedPiece = piece;
                clearHighlights();
                highlightValidMoves(piece);
            }
        }
    }

    private Move findMove(Piece piece, int targetRow, int targetCol) {
        List<Move> validMoves = gameBoard.getValidMoves(piece);
        for (Move move : validMoves) {
            if (move.getNewRow() == targetRow && move.getNewCol() == targetCol) {
                return move;
            }
        }
        return null;
    }

    private void highlightValidMoves(Piece piece) {
        List<Move> validMoves = gameBoard.getValidMoves(piece);
        for (Move move : validMoves) {
            Rectangle highlight = new Rectangle(CELL_SIZE, CELL_SIZE);
            highlight.setFill(Color.YELLOW);
            highlight.setOpacity(0.3);
            cells[move.getNewRow()][move.getNewCol()].getChildren().add(highlight);
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < GameBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < GameBoard.BOARD_SIZE; col++) {
                if (cells[row][col] != null) {
                    cells[row][col].getChildren().removeIf(node ->
                            node instanceof Rectangle && ((Rectangle)node).getFill() == Color.YELLOW);
                }
            }
        }
    }

    private void makeBotMove() {
        Move botMove = bot.getNextMove(gameBoard);
        if (botMove != null) {
            makeMove(botMove);
            isPlayerTurn = true;
        }
        checkGameEnd();
    }

    private void makeMove(Move move) {
        gameBoard.makeMove(move);
        updateBoard();
    }

    private void updateBoard() {
        for (int row = 0; row < GameBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < GameBoard.BOARD_SIZE; col++) {
                if (cells[row][col] != null) {
                    cells[row][col].getChildren().removeIf(node -> node instanceof Circle);

                    Piece piece = gameBoard.getPiece(row, col);
                    if (piece != null) {
                        Circle circle = new Circle(CELL_SIZE / 3);
                        if (piece instanceof Fox) {
                            circle.setFill(Color.RED);
                        } else {
                            circle.setFill(Color.GREEN);
                        }
                        cells[row][col].getChildren().add(circle);
                    }
                }
            }
        }
    }

    private void checkGameEnd() {
        if (gameBoard.isChickenWin()) {

            showGameEndDialog("Куры Выиграли!");
        } else if (gameBoard.isFoxWin()) {

            showGameEndDialog("Лисы Выиграли!");
        }
    }

    private void showGameEndDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getButtonTypes().clear();

        ButtonType newGameButton = new ButtonType("Новая игра");
        ButtonType exitButton = new ButtonType("Выход");
        alert.getButtonTypes().addAll(newGameButton, exitButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == newGameButton) {
                // Reset the game board
                gameBoard = new GameBoard();
                isPlayerTurn = true;
                selectedPiece = null;
                initializeBoard();
            } else if (buttonType == exitButton) {

                javafx.application.Platform.exit();
            }
        });
    }
}

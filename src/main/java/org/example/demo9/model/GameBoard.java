package org.example.demo9.model;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    public static final int BOARD_SIZE = 7;
    private Piece[][] board;
    private List<Fox> foxes;
    private List<Chicken> chickens;

    public GameBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        foxes = new ArrayList<>();
        chickens = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if ((i < 2 || i > 4) && (j < 2 || j > 4)) {
                    board[i][j] = null; // Invalid position
                } else {
                    board[i][j] = null; // Valid empty position
                }
            }
        }

        placePiece(new Fox(2, 2));
        placePiece(new Fox(2, 4));

        // Place chickens
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isValidPosition(i, j)) {
                    placePiece(new Chicken(i, j));
                }
            }
        }
    }

    public boolean isValidPosition(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        return !((row < 2 || row > 4) && (col < 2 || col > 4));
    }

    public boolean placePiece(Piece piece) {
        if (!isValidPosition(piece.getRow(), piece.getCol())) {
            return false;
        }
        board[piece.getRow()][piece.getCol()] = piece;
        if (piece instanceof Fox) {
            foxes.add((Fox) piece);
        } else if (piece instanceof Chicken) {
            chickens.add((Chicken) piece);
        }
        return true;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public boolean isChickenWin() {
        int chickensInTopSquare = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j < 5; j++) {
                if (board[i][j] instanceof Chicken) {
                    chickensInTopSquare++;
                }
            }
        }
        return chickensInTopSquare >= 9;
    }

    public boolean isFoxWin() {
        return chickens.size() <= 8;
    }

    public List<Move> getValidMoves(Piece piece) {
        List<Move> moves = new ArrayList<>();
        if (piece instanceof Fox) {
            moves.addAll(getFoxMoves((Fox) piece));
        } else if (piece instanceof Chicken) {
            moves.addAll(getChickenMoves((Chicken) piece));
        }
        return moves;
    }

    private List<Move> getFoxMoves(Fox fox) {
        List<Move> moves = new ArrayList<>();

        List<Move> captureMoves = getCaptureMoves(fox, new ArrayList<>());
        if (captureMoves.isEmpty()) {

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newRow = fox.getRow() + dir[0];
                int newCol = fox.getCol() + dir[1];
                if (isValidPosition(newRow, newCol) && board[newRow][newCol] == null) {
                    moves.add(new Move(fox, newRow, newCol));
                }
            }
        } else {
            moves.addAll(captureMoves);
        }

        return moves;
    }

    private List<Move> getCaptureMoves(Fox fox, List<int[]> capturedPositions) {
        List<Move> captureMoves = new ArrayList<>();
        int[][] captureDirections = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};

        for (int[] dir : captureDirections) {
            int jumpRow = fox.getRow() + dir[0];
            int jumpCol = fox.getCol() + dir[1];

            if (isValidPosition(jumpRow, jumpCol) && board[jumpRow][jumpCol] == null) {
                int midRow = fox.getRow() + dir[0]/2;
                int midCol = fox.getCol() + dir[1]/2;

                if (isValidPosition(midRow, midCol) &&
                        board[midRow][midCol] instanceof Chicken &&
                        !isPositionCaptured(midRow, midCol, capturedPositions)) {

                    Move move = new Move(fox, jumpRow, jumpCol);
                    move.addCapture(board[midRow][midCol], jumpRow, jumpCol);

                    int originalRow = fox.getRow();
                    int originalCol = fox.getCol();

                    fox.setPosition(jumpRow, jumpCol);
                    List<int[]> newCapturedPositions = new ArrayList<>(capturedPositions);
                    newCapturedPositions.add(new int[]{midRow, midCol});

                    // Recursively find additional captures
                    List<Move> additionalCaptures = getCaptureMoves(fox, newCapturedPositions);

                    // If there are additional captures, add them to the current move
                    for (Move additionalMove : additionalCaptures) {
                        Move combinedMove = new Move(fox, jumpRow, jumpCol);
                        combinedMove.addCapture(board[midRow][midCol], jumpRow, jumpCol);
                        for (Piece capturedPiece : additionalMove.getCapturedPieces()) {
                            combinedMove.addCapture(capturedPiece,
                                    additionalMove.getNewRow(),
                                    additionalMove.getNewCol());
                        }
                        combinedMove.updateFinalPosition(
                                additionalMove.getNewRow(),
                                additionalMove.getNewCol());
                        captureMoves.add(combinedMove);
                    }

                    if (additionalCaptures.isEmpty()) {
                        captureMoves.add(move);
                    }

                    fox.setPosition(originalRow, originalCol);
                }
            }
        }

        return captureMoves;
    }

    private boolean isPositionCaptured(int row, int col, List<int[]> capturedPositions) {
        for (int[] pos : capturedPositions) {
            if (pos[0] == row && pos[1] == col) {
                return true;
            }
        }
        return false;
    }

    private List<Move> getChickenMoves(Chicken chicken) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {0, -1}, {0, 1}}; // Up, Left, Right

        for (int[] dir : directions) {
            int newRow = chicken.getRow() + dir[0];
            int newCol = chicken.getCol() + dir[1];
            if (isValidPosition(newRow, newCol) && board[newRow][newCol] == null) {
                moves.add(new Move(chicken, newRow, newCol));
            }
        }

        return moves;
    }

    public void makeMove(Move move) {
        System.out.println("Выполняется ход. Количество кур до хода: " + chickens.size());
        System.out.println("Количество захваченных фигур: " + move.getCapturedPieces().size());

        for (Piece capturedPiece : move.getCapturedPieces()) {
            if (capturedPiece instanceof Chicken) {
                chickens.remove(capturedPiece);
                board[capturedPiece.getRow()][capturedPiece.getCol()] = null;
                System.out.println("Курица удалена. Осталось кур: " + chickens.size());
            }
        }

        Piece piece = move.getPiece();
        board[piece.getRow()][piece.getCol()] = null;
        piece.setPosition(move.getNewRow(), move.getNewCol());
        board[move.getNewRow()][move.getNewCol()] = piece;
    }

    public List<Fox> getFoxes() {
        return foxes;
    }

    public List<Chicken> getChickens() {
        return chickens;
    }
}

package org.example.demo9.model;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private Piece piece;
    private int newRow;
    private int newCol;
    private List<Piece> capturedPieces;
    private List<int[]> moveSequence;

    public Move(Piece piece, int newRow, int newCol) {
        this.piece = piece;
        this.newRow = newRow;
        this.newCol = newCol;
        this.capturedPieces = new ArrayList<>();
        this.moveSequence = new ArrayList<>();
        this.moveSequence.add(new int[]{newRow, newCol});
    }

    public void addCapture(Piece capturedPiece, int row, int col) {
        capturedPieces.add(capturedPiece);
        moveSequence.add(new int[]{row, col});
    }

    public Piece getPiece() {
        return piece;
    }

    public int getNewRow() {
        return newRow;
    }

    public int getNewCol() {
        return newCol;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public List<int[]> getMoveSequence() {
        return moveSequence;
    }

    public boolean isCapture() {
        return !capturedPieces.isEmpty();
    }

    public int getCaptureCount() {
        return capturedPieces.size();
    }

    public void updateFinalPosition(int row, int col) {
        this.newRow = row;
        this.newCol = col;
    }
}

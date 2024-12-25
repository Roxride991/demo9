package org.example.demo9.ai;

import org.example.demo9.model.GameBoard;
import org.example.demo9.model.Move;
import org.example.demo9.model.Fox;
import org.example.demo9.model.Piece;
import java.util.List;
import java.util.Random;

public class MediumBot implements Bot {
    private Random random = new Random();

    @Override
    public Move getNextMove(GameBoard board) {
        List<Fox> foxes = board.getFoxes();

        for (Fox fox : foxes) {
            List<Move> moves = board.getValidMoves(fox);
            for (Move move : moves) {
                if (move.isCapture()) {
                    return move;
                }
            }
        }

        List<Move> allMoves = getAllPossibleMoves(board, foxes);
        if (!allMoves.isEmpty()) {
            return allMoves.get(random.nextInt(allMoves.size()));
        }

        return null;
    }

    private List<Move> getAllPossibleMoves(GameBoard board, List<Fox> foxes) {
        List<Move> allMoves = new java.util.ArrayList<>();
        for (Fox fox : foxes) {
            allMoves.addAll(board.getValidMoves(fox));
        }
        return allMoves;
    }
}

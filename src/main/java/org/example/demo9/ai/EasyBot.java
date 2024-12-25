package org.example.demo9.ai;

import org.example.demo9.model.GameBoard;
import org.example.demo9.model.Move;
import org.example.demo9.model.Fox;
import java.util.List;
import java.util.Random;

public class EasyBot implements Bot {
    private Random random = new Random();

    @Override
    public Move getNextMove(GameBoard board) {
        List<Fox> foxes = board.getFoxes();
        List<Move> allMoves = getAllPossibleMoves(board, foxes);

        if (!allMoves.isEmpty()) {
            // Just pick a random move
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

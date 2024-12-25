package org.example.demo9.ai;

import org.example.demo9.model.GameBoard;
import org.example.demo9.model.Move;
import org.example.demo9.model.Fox;
import org.example.demo9.model.Chicken;
import java.util.List;

public class HardBot implements Bot {
    private static final int MAX_DEPTH = 20;
    private static final int CAPTURE_VALUE = 100;
    private static final int POSITION_VALUE = 10;

    @Override
    public Move getNextMove(GameBoard board) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        List<Fox> foxes = board.getFoxes();
        for (Fox fox : foxes) {
            List<Move> moves = board.getValidMoves(fox);
            for (Move move : moves) {
                if (move.isCapture()) {
                    return move;
                }

                GameBoard simulatedBoard = new GameBoard();
                simulatedBoard.makeMove(move);
                int moveValue = alphaBeta(simulatedBoard, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private int alphaBeta(GameBoard board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || board.isFoxWin() || board.isChickenWin()) {
            return evaluatePosition(board);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            List<Fox> foxes = board.getFoxes();
            for (Fox fox : foxes) {
                List<Move> moves = board.getValidMoves(fox);
                for (Move move : moves) {
                    GameBoard simulatedBoard = new GameBoard();
                    simulatedBoard.makeMove(move);
                    int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, false);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            List<Chicken> chickens = board.getChickens();
            for (Chicken chicken : chickens) {
                List<Move> moves = board.getValidMoves(chicken);
                for (Move move : moves) {
                    GameBoard simulatedBoard = new GameBoard();
                    simulatedBoard.makeMove(move);
                    int eval = alphaBeta(simulatedBoard, depth - 1, alpha, beta, true);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    private int evaluatePosition(GameBoard board) {
        int score = 0;


        score += (20 - board.getChickens().size()) * CAPTURE_VALUE;


        for (Fox fox : board.getFoxes()) {
            int distanceFromCenter = Math.abs(fox.getRow() - 3) + Math.abs(fox.getCol() - 3);
            score += (6 - distanceFromCenter) * POSITION_VALUE;
        }


        int chickensInTopRows = 0;
        for (Chicken chicken : board.getChickens()) {
            if (chicken.getRow() < 3) {
                chickensInTopRows++;
            }
        }
        score -= chickensInTopRows * POSITION_VALUE * 2;

        return score;
    }
}

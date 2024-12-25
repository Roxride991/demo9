package org.example.demo9.ai;

import org.example.demo9.model.GameBoard;
import org.example.demo9.model.Move;

public interface Bot {
    Move getNextMove(GameBoard board);
}


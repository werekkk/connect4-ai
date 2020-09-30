package ai;

import model.Board;
import model.Color;
import model.Move;
import view.GameStats;

public interface MoveAlgorithm {
    Move suggestMove(Board board, Color player, AiConfig config, GameStats stats);
}

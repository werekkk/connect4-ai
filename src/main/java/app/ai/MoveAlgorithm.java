package app.ai;

import app.model.Board;
import app.model.Color;
import app.model.Move;
import app.view.GameStats;

public interface MoveAlgorithm {
    Move suggestMove(Board board, Color player, AiConfig config, GameStats stats);
}

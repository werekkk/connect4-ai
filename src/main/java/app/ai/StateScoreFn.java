package app.ai;

import app.model.Board;
import app.model.Color;

public interface StateScoreFn {
    int evaluate(Board board, Color color);
}

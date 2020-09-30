package ai;

import model.Board;
import model.Color;

public interface StateScoreFn {
    int evaluate(Board board, Color color);
}

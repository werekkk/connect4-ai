package ai;

import model.Board;
import model.Color;
import model.Move;
import view.GameStats;

public class Ai {

    private AiConfig config;

    MoveAlgorithm yellowAlg;
    MoveAlgorithm redAlg;

    public void newGame(AiConfig config) {
        this.config = config;
        yellowAlg = config.yellowAlg.getAlgorithm();
        redAlg = config.redAlg.getAlgorithm();
    }

    public boolean isHuman(Color player) {
        return switch (player) {
            case YELLOW -> !config.yellowIsAi;
            case RED -> !config.redIsAi;
        };
    }

    public Move suggestMove(Board board, Color player, GameStats stats) {
        return switch (player) {
            case YELLOW -> yellowAlg.suggestMove(board, Color.YELLOW, config, stats);
            case RED -> redAlg.suggestMove(board, Color.RED, config, stats);
        };
    }

}

package ai;

import model.Color;

public class AiConfig {

    public boolean yellowIsAi;
    public boolean redIsAi;

    public AiAlgorithm yellowAlg;
    public AiAlgorithm redAlg;

    public ScoreHeuristic yellowScoreHeuristic;
    public ScoreHeuristic redScoreHeuristic;

    public int yellowDepth;
    public int redDepth;

    public StateScoreFn getScoreFn(Color color) {
        return switch (color) {
            case YELLOW -> yellowScoreHeuristic.getStateScoreFn();
            case RED -> redScoreHeuristic.getStateScoreFn();
        };
    }

    public enum AiAlgorithm {
        MINIMAX,
        ALPHABETA;

        public MoveAlgorithm getAlgorithm() {
            return switch (this) {
                case MINIMAX ->
                    ((board, player, config, stats) -> {
                        AiAlgorithms ai = new AiAlgorithms(board, player, config);
                        return ai.minimax(stats);
                    });
                case ALPHABETA ->
                    ((board, player, config, stats) -> {
                        AiAlgorithms ai = new AiAlgorithms(board, player, config);
                        return ai.alphaBeta(stats);
                    });
            };
        }

    }

    public enum ScoreHeuristic {
        SIMPLE,
        CURRENT_STATE,
        CURRENT_POSSIBILITIES;

        public StateScoreFn getStateScoreFn() {
            return switch (this) {
                case SIMPLE -> StateScore::simpleScore;
                case CURRENT_STATE -> StateScore::currentStateScore;
                case CURRENT_POSSIBILITIES -> StateScore::currentPossibilitiesScore;
            };
        }


        @Override
        public String toString() {
            return switch (this) {
                case SIMPLE -> "P";
                case CURRENT_STATE -> "OS";
                case CURRENT_POSSIBILITIES -> "OM";
            };
        }
    }

}

package ai;

import model.Board;
import model.Color;

public class StateScore {

    public static final int INF = 1000000000;

    private static final int CS_W1 = 1;
    private static final int CS_W2 = 2;
    private static final int CS_W3 = 5;

    private static final int CP_W1 = 1;
    private static final int CP_W2 = 2;
    private static final int CP_W3 = 3;
    private static final int CP_W4 = 4;

    public static int simpleScore(Board board, Color color) {
        Color winner = board.findWinner();
        if (winner != null) {
            if (winner == color)
                return INF;
            else
                return -INF;
        }
        return 0;
    }

    public static int currentPossibilitiesScore(Board board, Color color) {
        int simpleScore = simpleScore(board, color);
        if (simpleScore != 0)
            return simpleScore;
        else
            return CP_W1 * BoardUtils.possibleMoves(board) +
                    CP_W2 * BoardUtils.possibleNs(board, color, 2) +
                    CP_W3 * BoardUtils.possibleNs(board, color, 3) +
                    CP_W4 * BoardUtils.possibleNs(board, color, 4);
    }

    public static int currentStateScore(Board board, Color color) {
        int simpleScore = simpleScore(board, color);
        if (simpleScore != 0)
            return simpleScore;
        else
            return CS_W1 * BoardUtils.countFields(board, color) +
                    CS_W2 * BoardUtils.countAllNLengths(board, color, 2) +
                    CS_W3 * BoardUtils.countAllNLengths(board, color, 3);
    }

}

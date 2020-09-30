package ai;

import model.Move;

import java.util.ArrayList;
import java.util.Random;

public class Moves {

    private final static Random random = new Random();

    private final ArrayList<Move> bestMoves = new ArrayList<>(3);
    private final ArrayList<Move> worstMoves = new ArrayList<>(3);

    public void add(Move move) {
        if (bestMoves.isEmpty() || bestMoves.get(0).score == move.score) {
            bestMoves.add(move);
        } else if (move.score > bestMoves.get(0).score) {
            bestMoves.clear();
            bestMoves.add(move);
        }
        if (worstMoves.isEmpty() || worstMoves.get(0).score == move.score) {
            worstMoves.add(move);
        } else if (move.score < worstMoves.get(0).score) {
            worstMoves.clear();
            worstMoves.add(move);
        }

    }

    public Move bestMove() {
        if (bestMoves.isEmpty()) {
            return null;
        } else {
//            return bestMoves.get(random.nextInt(bestMoves.size()));
            return bestMoves.get(0);
        }
    }

    public Move worstMove() {
        if (worstMoves.isEmpty()) {
            return null;
        } else {
//            return worstMoves.get(random.nextInt(worstMoves.size()));
            return worstMoves.get(0);
        }
    }

}

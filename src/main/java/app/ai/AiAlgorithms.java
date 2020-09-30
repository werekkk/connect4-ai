package app.ai;

import app.model.Board;
import app.model.Color;
import app.model.Move;
import app.view.GameStats;

import java.util.ArrayList;

public class AiAlgorithms {

    private final Board board;
    private final Color player;
    private final AiConfig config;
    private final StateScoreFn scoreFn;

    public AiAlgorithms(Board board, Color player, AiConfig config) {
        this.board = board;
        this.player = player;
        this.config = config;
        this.scoreFn = config.getScoreFn(player);
    }

    public Move minimax(GameStats stats) {
        int depth = (player == Color.YELLOW) ? config.yellowDepth : config.redDepth;
        stats.nodeVisited(player);
        return minimax(depth - 1, stats, MinimaxSelection.MAXIMIZE);
    }

    private Move minimax(int depth, GameStats stats, MinimaxSelection selection) {
        Moves moves = new Moves();
        ArrayList<Move> possibleMoves = board.getPossibleMoves(selection == MinimaxSelection.MAXIMIZE ? player : player.opposite());
        for (Move move : possibleMoves) {
            board.apply(move);
            stats.nodeVisited(player);
            if (depth == 0 || board.findWinner() != null || board.isFull()) {
                move.score = scoreFn.evaluate(board, player) + (selection == MinimaxSelection.MAXIMIZE ? depth : -depth);
            } else {
                move.score = minimax(depth - 1, stats, selection.opposite()).score;
            }
            moves.add(move);
            board.revert(move);
        }
        return selection == MinimaxSelection.MAXIMIZE ? moves.bestMove() : moves.worstMove();
    }

    public Move alphaBeta(GameStats stats) {
        int depth = (player == Color.YELLOW) ? config.yellowDepth : config.redDepth;
        stats.nodeVisited(player);
        return alphaBeta(depth - 1, -StateScore.INF, StateScore.INF, stats, MinimaxSelection.MAXIMIZE);
    }

    private Move alphaBeta(int depth, int alpha, int beta, GameStats stats, MinimaxSelection selection) {
        Moves moves = new Moves();
        ArrayList<Move> possibleMoves = board.getPossibleMoves(selection == MinimaxSelection.MAXIMIZE ? player : player.opposite());
        for (Move move : possibleMoves) {
            board.apply(move);
            stats.nodeVisited(player);
            if (depth == 0 || board.findWinner() != null || board.isFull()) {
                move.score = scoreFn.evaluate(board, player) + (selection == MinimaxSelection.MAXIMIZE ? depth : -depth);
            } else {
                move.score = alphaBeta(depth - 1, alpha, beta, stats, selection.opposite()).score;
                if (selection == MinimaxSelection.MAXIMIZE) {
                    alpha = Math.max(alpha, move.score);
                } else if (selection == MinimaxSelection.MINIMIZE) {
                    beta = Math.min(beta, move.score);
                }
                if (alpha > beta) {
                    stats.prune(player);
                    board.revert(move);
                    return move;
                }
            }
            moves.add(move);
            board.revert(move);
        }
        return selection == MinimaxSelection.MAXIMIZE ? moves.bestMove() : moves.worstMove();
    }

    private enum MinimaxSelection {
        MAXIMIZE,
        MINIMIZE;

        public MinimaxSelection opposite() {
            return switch (this) {
                case MAXIMIZE -> MINIMIZE;
                case MINIMIZE -> MAXIMIZE;
            };
        }

    }

}

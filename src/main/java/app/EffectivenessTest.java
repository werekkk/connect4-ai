package app;

import app.ai.AiAlgorithms;
import app.ai.AiConfig;
import app.model.Board;
import app.model.Color;
import app.model.Move;
import app.view.GameStats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EffectivenessTest {

    public static void testHeuristics(String filename) {
        Box<String> SvsCS = new Box<>();
        Box<String> SvsCP = new Box<>();
        Box<String> CSvsCP = new Box<>();

        Thread t1 = new Thread(() -> SvsCS.set(performTests(AiConfig.ScoreHeuristic.SIMPLE, AiConfig.ScoreHeuristic.CURRENT_STATE)));
        Thread t2 = new Thread(() -> SvsCP.set(performTests(AiConfig.ScoreHeuristic.SIMPLE, AiConfig.ScoreHeuristic.CURRENT_POSSIBILITIES)));
        Thread t3 = new Thread(() -> CSvsCP.set(performTests(AiConfig.ScoreHeuristic.CURRENT_STATE, AiConfig.ScoreHeuristic.CURRENT_POSSIBILITIES)));

        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try (BufferedWriter br = new BufferedWriter(new FileWriter(new File(filename)))) {
            br.write(SvsCS.val);
            br.newLine();
            br.write(SvsCP.val);
            br.newLine();
            br.write(CSvsCP.val);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Zakończono test efektywności heurystyk.");
    }

    private static String performTests(AiConfig.ScoreHeuristic h1, AiConfig.ScoreHeuristic h2) {
        StringBuilder sb = new StringBuilder();
        sb.append(h1.toString() + "\\" + h2.toString());
        sb.append(";1;2;3;4;5;6;7;8\n");
        for (int h1depth = 1; h1depth <= 8; h1depth++) {
            sb.append(h1depth);
            for (int h2depth = 1; h2depth <= 8; h2depth++) {
                System.out.println(h1.toString() + "(" + h1depth + ") vs " + h2.toString() + "(" + h2depth + ")");
                sb.append(";");
                sb.append(testGame(h1, h2, h1depth, h2depth));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static String testGame(AiConfig.ScoreHeuristic h1, AiConfig.ScoreHeuristic h2, int depth1, int depth2) {
        Box<Color> firstWinner = new Box<>();
        Box<Color> secondWinner = new Box<>();
        Thread t1 = new Thread(() -> firstWinner.set(testGameColoured(h1, h2, depth1, depth2)));
        Thread t2 = new Thread(() -> secondWinner.set(testGameColoured(h2, h1, depth2, depth1)));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (firstWinner.val == Color.YELLOW ? h1.toString() : (firstWinner.val == Color.RED ? h2.toString() : "R")) + " " +
                (secondWinner.val == Color.YELLOW ? h2.toString() : (secondWinner.val == Color.RED ? h1.toString() : "R"));
    }

    private static Color testGameColoured(AiConfig.ScoreHeuristic yellow, AiConfig.ScoreHeuristic red, int yellowDepth, int redDepth) {
        Board board = new Board(6, 7);
        board.apply(new Move(4, Color.YELLOW));
        AiConfig aiConfig = new AiConfig();
        aiConfig.yellowDepth = yellowDepth;
        aiConfig.redDepth = redDepth;
        aiConfig.yellowAlg = AiConfig.AiAlgorithm.ALPHABETA;
        aiConfig.redAlg = AiConfig.AiAlgorithm.ALPHABETA;
        aiConfig.yellowScoreHeuristic = yellow;
        aiConfig.redScoreHeuristic = red;
        GameStats stats = new GameStats();
        Color currentColor = Color.RED;
        while (!board.isFull() && board.findWinner() == null) {
            Move move = new AiAlgorithms(board, currentColor, aiConfig).alphaBeta(stats);
            board.apply(move);
            currentColor = currentColor.opposite();
        }
        return board.findWinner();
    }

    private static class Box<T> {
        T val;
        public void set(T val) {
            this.val = val;
        }
    }
}



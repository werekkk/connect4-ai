package app.view;

import app.ai.BoardUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import app.model.Board;
import app.model.Color;

public class Dialogs {

    public static void showFinishDialog(Board board, GameStats stats) {
        Color winner = BoardUtils.findWinner(board);
        if (winner != null) {
            winnerDialog(winner, stats);
        } else if (board.isFull()) {
            tieDialog(stats);
        }
    }

    private static void winnerDialog(Color color, GameStats stats) {
        showDialog("Koniec gry", "Wygrał " + color.toString() + "!\n\n" + stats.toString());
    }

    private static void tieDialog(GameStats stats) {
        showDialog("Remis", "Nikt nie wygrał!\n" + stats.toString());
    }

    private static void showDialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}

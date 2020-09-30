package app.ai;

import app.model.Board;
import app.model.Color;
import app.model.Move;

public class BoardUtils {

    public static Color findWinner(Board board) {
        Color winner;
        Color[][] fields = board.fields;
        // rows
        for (int i = 0; i < board.rows; i++) {
            for (int j = 0; j < board.columns - 3; j++) {
                winner = row(i, j, fields, 4);
                if (winner != null)
                    return winner;
            }
        }
        // columns
        for (int i = 0; i < board.rows - 3; i++) {
            for (int j = 0; j < board.columns; j++) {
                winner = column(i, j, fields, 4);
                if (winner != null)
                    return winner;
            }
        }
        // diagonal
        for (int i = 0; i < board.rows - 3; i++) {
            for (int j = 0; j < board.columns - 3; j++) {
                winner = diagonalLeft(i, j, fields, 4);
                if (winner != null)
                    return winner;
                winner = diagonalRight(i + 3, j, fields, 4);
                if (winner != null)
                    return winner;
            }
        }
        return null;
    }

    public static int countFields(Board board, Color color) {
        int count = 0;
        for (int i = 0; i < board.rows; i++) {
            for (int j = 0; j < board.columns; j++) {
                count += board.fields[i][j] == color ? 1 : 0;
            }
        }
        return count;
    }

    public static int possibleMoves(Board board) {
        int moves = 0;
        for (int i = 0; i < board.columns; i++) {
            if (!board.isColumnFull(i))
                moves++;
        }
        return moves;
    }

    public static int possibleNs(Board board, Color color, int n) {
        int current = countAllNLengths(board, color, n);
        int increase = 0;
        for (int i = 0; i < board.columns; i++) {
            if (!board.isColumnFull(i)) {
                Move move = new Move(i, color);
                board.apply(move);
                increase += countAllNLengths(board, color, n) - current;
                board.revert(move);
            }
        }
        return increase;
    }

    public static int countAllNLengths(Board board, Color color, int n) {
        int count = 0;
        Color[][] fields = board.fields;
        // rows
        for (int i = 0; i < board.rows; i++) {
            for (int j = 0; j < board.columns - (n - 1); j++) {
                boolean correct = true;
                for (int k = 0; k < n; k++) {
                    if (fields[i][j + k] != color) {
                        correct = false;
                        break;
                    }
                }
                count += correct ? 1 : 0;
            }
        }
        // columns
        for (int i = 0; i < board.rows - (n - 1); i++) {
            for (int j = 0; j < board.columns; j++) {
                boolean correct = true;
                for (int k = 0; k < n; k++) {
                    if (fields[i + k][j] != color) {
                        correct = false;
                        break;
                    }
                }
                count += correct ? 1 : 0;
            }
        }
        // diagonal
        for (int i = 0; i < board.rows - (n - 1); i++) {
            for (int j = 0; j < board.columns - (n - 1); j++) {
                boolean correct = true;
                for (int k = 0; k < n; k++) {
                    if (fields[i + k][j + k] != color) {
                        correct = false;
                        break;
                    }
                }
                count += correct ? 1 : 0;
                correct = true;
                for (int k = 0; k < n; k++) {
                    if (fields[i + (n - 1) - k][j + k] != color) {
                        correct = false;
                        break;
                    }
                }
                count += correct ? 1 : 0;
            }
        }
        return count;
    }

    private static Color row(int row, int col, Color[][] fields, int n) {         // right
        Color c = fields[row][col];
        for (int i = 1; i < n; i++) {
            if (fields[row][col + i] != c) {
                return null;
            }
        }
        return c;
    }

    private static Color column(int row, int col, Color[][] fields, int n) {      // down
        Color c = fields[row][col];
        for (int i = 1; i < n; i++) {
            if (fields[row + i][col] != c) {
                return null;
            }
        }
        return c;
    }

    private static Color diagonalLeft(int row, int col, Color[][] fields, int n) {// down, direction: \
        Color c = fields[row][col];
        for (int i = 1; i < n; i++) {
            if (fields[row + i][col + i] != c) {
                return null;
            }
        }
        return c;
    }

    private static Color diagonalRight(int row, int col, Color[][] fields, int n) {// up, direction: /
        Color c = fields[row][col];
        for (int i = 1; i < n; i++) {
            if (fields[row - i][col + i] != c) {
                return null;
            }
        }
        return c;
    }

}

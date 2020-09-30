package model;

import ai.BoardUtils;
import javafx.beans.property.ObjectProperty;

import java.util.ArrayList;

public class Board {

    public int rows;
    public int columns;
    public Color[][] fields;
    private final int[] lowestRow;

    public Board(int row, int col) {
        this.rows = row;
        this.columns = col;
        fields = new Color[row][col];
        lowestRow = new int[col];
        for (int i = 0; i < col; i++) {
            lowestRow[i] = row - 1;
        }
    }

    public Board(int row, int col, ObjectProperty<Color>[][] fields) {
        this(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.fields[i][j] = fields[i][j].getValue();
            }
        }
        for (int i = 0; i < col; i++) {
            lowestRow[i] = -1;
            for (int j = 0; j < row; j++) {
                if (this.fields[j][i] == null)
                    lowestRow[i] = j;
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < columns; i++) {
            if (!isColumnFull(i))
                return false;
        }
        return true;
    }

    public boolean isColumnFull(int col) {
        return lowestRow[col] == -1;
    }

    public void apply(Move move) {
        fields[lowestRow[move.column]][move.column] = move.color;
        lowestRow[move.column]--;
    }

    public void revert(Move move) {
        lowestRow[move.column]++;
        fields[lowestRow[move.column]][move.column] = null;
    }

    public Color findWinner() {
        return BoardUtils.findWinner(this);
    }

    public ArrayList<Move> getPossibleMoves(Color color) {
        ArrayList<Move> possibleMoves = new ArrayList<>(columns);
        for (int i = 0; i < columns; i++) {
            if (!isColumnFull(i)) {
                possibleMoves.add(new Move(i, color));
            }
        }
        return possibleMoves;
    }

}

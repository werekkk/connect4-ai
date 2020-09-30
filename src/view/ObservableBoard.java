package view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Board;
import model.Color;
import model.Move;

public class ObservableBoard {

    private final int rows;
    private final int columns;
    private final ObjectProperty<Color>[][] fields;

    public ObservableBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        fields = new ObjectProperty[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                fields[i][j] = new SimpleObjectProperty<>(null);
            }
        }
    }

    public void cast(Board board) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                fields[i][j].setValue(board.fields[i][j]);
            }
        }
    }

    public Board toBoard() {
        return new Board(rows, columns, fields);
    }

    public boolean canPlace(int column) {
        return findLowestEmptyRow(column) != -1;
    }

    public int findLowestEmptyRow(int column) {
        int lowestRow = -1;
        for (int i = 0; i < rows; i++) {
            if (fields[i][column].getValue() == null)
                lowestRow = i;
        }
        return lowestRow;
    }

    public void apply(Move move) {
        int lowestEmptyRow = findLowestEmptyRow(move.column);
        fields[lowestEmptyRow][move.column].setValue(move.color);
    }

    public void clear() {
        for (ObjectProperty<Color>[] row : fields) {
            for (ObjectProperty<Color> field : row) {
                field.setValue(null);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ObjectProperty<Color>[][] getFields() {
        return fields;
    }

}

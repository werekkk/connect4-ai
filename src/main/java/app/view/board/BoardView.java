package app.view.board;

import app.view.ObservableBoard;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import app.model.Color;

import java.io.IOException;

public class BoardView extends VBox {

    @FXML private GridPane gridPane;

    private FieldView[][] fields;

    private FieldClickedHandler fieldClickedHandler;

    public BoardView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/board_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setBoard(ObservableBoard board) {

        gridPane.getChildren().removeAll();

        int rows = board.getRows();
        int columns = board.getColumns();
        ObjectProperty<Color>[][] fields = board.getFields();
        this.fields = new FieldView[rows][columns];

        for (int i = 0; i < rows; i++) {
            gridPane.addRow(i);
        }
        for (int i = 0; i < columns; i++) {
            gridPane.addColumn(i);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                FieldView fieldView = new FieldView(i, j);
                fieldView.setObservable(fields[i][j]);
                fieldView.setOnMouseEntered(event -> highlightColumn(fieldView.getColumn()));
                fieldView.setOnMouseExited(event -> clearHighlight());
                fieldView.setOnMouseClicked(event -> handleFieldClicked(fieldView));
                this.fields[i][j] = fieldView;
                gridPane.add(fieldView, j, i);
            }
        }
    }

    public void highlightColumn(int column) {
        clearHighlight();
        for (FieldView[] field : fields) {
            field[column].setHighlighted(true);
        }
    }

    public void clearHighlight() {
        for (FieldView[] row : fields) {
            for (FieldView field : row) {
                field.setHighlighted(false);
            }
        }
    }

    private void handleFieldClicked(FieldView fieldView) {
        if (fieldClickedHandler != null) {
            fieldClickedHandler.handle(fieldView);
        }
    }

    public void setFieldClickedHandler(FieldClickedHandler fieldClickedHandler) {
        this.fieldClickedHandler = fieldClickedHandler;
    }

    public interface FieldClickedHandler {
        void handle(FieldView field);
    }

    public void clear() {
        for (FieldView[] row : fields) {
            for (FieldView field : row) {
                field.clear();
            }
        }
    }

    public void setNumber(int row, int col, int number) {
        fields[row][col].setNumber(number);
    }
}

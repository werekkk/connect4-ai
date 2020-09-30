package view.board;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Color;

import java.io.IOException;

public class FieldView extends Pane {

    private static final Paint WHITE = Paint.valueOf("#ffffff");
    private static final Paint GRAY = Paint.valueOf("#bbbbbb");

    @FXML private Rectangle background;
    @FXML private Circle field;
    @FXML private Label moveCountLabel;

    private final int row;
    private final int column;

    public FieldView(int row, int column) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("field_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        this.row = row;
        this.column = column;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setObservable(ObjectProperty<Color> observable) {
        applyColor(observable.getValue());
        observable.addListener((obs, oldValue, newValue) -> applyColor(newValue));
    }

    private void applyColor(Color color) {
        field.setFill(Paint.valueOf(Color.colorString(color)));
    }

    public void setHighlighted(boolean highlighted) {
        background.setFill((highlighted) ? GRAY : WHITE);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setNumber(int number) {
        Platform.runLater(() -> moveCountLabel.setText(number + ""));
    }

    public void clear() {
        applyColor(null);
        setHighlighted(false);
        Platform.runLater(() -> moveCountLabel.setText(""));
    }
}

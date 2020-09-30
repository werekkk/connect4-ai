package app.view;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;

public class ControlUtils {

    public static void initIntegerTextField(TextField textField, int defaultValue, int minValue, int maxValue) {
        textField.setText(defaultValue + "");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (textField.getText().length() > 4 && change.getText().length() > 0) {
                return null;
            }
            String newText = change.getText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> validateIntegerTextField(textField, minValue, maxValue));
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateIntegerTextField(textField, minValue, maxValue);
            }
        });
    }

    private static void validateIntegerTextField(TextField textField, int minValue, int maxValue) {
        if (textField.getText() == null || textField.getText().length() == 0) {
            textField.clear();
            textField.setText(minValue + "");
            return;
        }
        int value = Integer.parseInt(textField.getText());
        if (value < minValue) {
            textField.clear();
            textField.setText(minValue + "");
        } else if (value > maxValue) {
            textField.clear();
            textField.setText(maxValue + "");
        }
    }
}

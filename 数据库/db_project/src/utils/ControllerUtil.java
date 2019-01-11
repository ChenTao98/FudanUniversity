package utils;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class ControllerUtil {
    public static ComboBox<String> getComboBox(String[] strings) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(strings));
        comboBox.getSelectionModel().select(0);
        comboBox.setEditable(false);
        comboBox.setVisibleRowCount(15);
        return comboBox;
    }

    public static void showAlert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        String title = "";
        switch (alertType) {
            case INFORMATION:
                title = "成功";
                break;
            case WARNING:
                title = "警告";
                break;
            case ERROR:
                title = "错误";
                break;
        }
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.show();
    }

    public static String getSelectString(ComboBox<String> comboBox) {
        return comboBox.getSelectionModel().getSelectedItem();
    }
}

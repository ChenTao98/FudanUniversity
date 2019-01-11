package pane;

import dao.ParkInsertDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static java.lang.Integer.parseInt;
import static utils.ControllerUtil.getSelectString;
import static utils.ControllerUtil.showAlert;
import static utils.ControllerUtil.getComboBox;

public class ParkInsertPane extends BorderPane {
    private HBox parkTemp = new HBox();
    private HBox parkRent = new HBox();
    private HBox parkBuy = new HBox();
    private String[] community = {"A", "B", "C"};
    private ParkInsertDao parkInsertDao = new ParkInsertDao();

    public ParkInsertPane() {
        Label labelText = new Label("停车信息插入");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initParkTemp();
        initParkRent();
        initParkBuy();
        parkRent.setMinHeight(100);
        parkBuy.setMinHeight(100);
        parkTemp.setMinHeight(100);
        VBox vBoxCenter = new VBox();
        vBoxCenter.getChildren().addAll(parkTemp, parkRent, parkBuy);
        setCenter(vBoxCenter);
        Insets insets = new Insets(20, 0, 0, 20);
        setMargin(vBoxCenter, insets);
    }

    private void initParkTemp() {
        Label labelTemp = new Label("临时停车：");
        labelTemp.setFont(Font.font("BOLD", 20));

        Label labelCommunity = new Label("小区：");
        ComboBox<String> comboBoxCommunity = getComboBox(community);
//        comboBoxCommunity.setMaxWidth(80);
        labelCommunity.setLabelFor(comboBoxCommunity);

        Label labelNum = new Label("车位号");
        String[] nums = new String[200];
        for (int i = 0; i < 200; i++) {
            nums[i] = (i + 1) + "";
        }
        ComboBox<String> comboBoxNum = getComboBox(nums);
//        comboBoxNum.setMaxWidth(80);
        labelNum.setLabelFor(comboBoxNum);

        TextField textCarNum = new TextField("");
        textCarNum.setPromptText("车牌号");

        TextField textAmount = new TextField();
        textAmount.setPromptText("金额");
//        textAmount.setMaxWidth(80);

        Button button = new Button("提交");
        parkTemp.getChildren().addAll(labelTemp, labelCommunity, comboBoxCommunity, labelNum, comboBoxNum, textCarNum, textAmount, button);
        parkTemp.setSpacing(10);

        button.setOnAction(event -> {
            String amount = textAmount.getText();
            String carNum = textCarNum.getText();
            if (amount.matches("^[0-9]+[.]?[0-9]+$") && !carNum.equals("")) {
                boolean isSuccess = parkInsertDao.insertTemp(getSelectString(comboBoxCommunity), carNum,
                        parseInt(getSelectString(comboBoxNum)), Double.parseDouble(amount));
                if (isSuccess) {
                    showAlert(Alert.AlertType.INFORMATION, "成功插入数据");
                } else {
                    showAlert(Alert.AlertType.WARNING, "插入失败，车位已被购买或者租用");
                }
            } else {
                textAmount.setText("");
                textAmount.setPromptText("请输入整数或者小数");
            }
        });
    }

    private void initParkRent() {
        Label labelRent = new Label("租用车位：");
        labelRent.setFont(Font.font("BOLD", 20));

        Label labelCommunity = new Label("小区：");
        ComboBox<String> comboBoxCommunity = getComboBox(community);
//        comboBoxCommunity.setMaxWidth(80);
        labelCommunity.setLabelFor(comboBoxCommunity);

        Label labelNum = new Label("车位号");
        String[] nums = new String[200];
        for (int i = 0; i < 200; i++) {
            nums[i] = (i + 1) + "";
        }
        ComboBox<String> comboBoxNum = getComboBox(nums);
//        comboBoxNum.setMaxWidth(80);
        labelNum.setLabelFor(comboBoxNum);

        Label labelYear = new Label("年");
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = i + "";
        }
        ComboBox<String> comboBoxYear = getComboBox(years);
        labelYear.setLabelFor(comboBoxYear);


        Label labelMonth = new Label("月");
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = i + "";
        }
        ComboBox<String> comboBoxMonth = getComboBox(months);
        labelMonth.setLabelFor(comboBoxMonth);

        Label labelId = new Label("户主id");
        TextField textFieldId = new TextField();
        labelId.setLabelFor(textFieldId);

        Button button = new Button("提交");
        button.setOnAction(event -> {
            int year = parseInt(getSelectString(comboBoxYear));
            int month = parseInt(getSelectString(comboBoxMonth));
            String id = textFieldId.getText();
            if (id.matches("^[0-9]+$") && (year != 0 || month != 0)) {
                int result = parkInsertDao.insertRent(getSelectString(comboBoxCommunity), parseInt(getSelectString(comboBoxNum)),
                        year, month, parseInt(id));
                showResult(result);
            } else {
                textFieldId.setText("");
                textFieldId.setPromptText("请输入数字id");
            }
        });

        parkRent.getChildren().addAll(labelRent, labelCommunity, comboBoxCommunity, labelNum, comboBoxNum,
                comboBoxYear, labelYear, comboBoxMonth, labelMonth, labelId, textFieldId, button);
        parkRent.setSpacing(10);

    }

    private void initParkBuy() {
        Label labelBuy = new Label("购买车位：");
        labelBuy.setFont(Font.font("BOLD", 20));

        Label labelCommunity = new Label("小区：");
        ComboBox<String> comboBoxCommunity = getComboBox(community);
//        comboBoxCommunity.setMaxWidth(80);
        labelCommunity.setLabelFor(comboBoxCommunity);

        Label labelNum = new Label("车位号");
        String[] nums = new String[200];
        for (int i = 0; i < 200; i++) {
            nums[i] = (i + 1) + "";
        }
        ComboBox<String> comboBoxNum = getComboBox(nums);
//        comboBoxNum.setMaxWidth(80);
        labelNum.setLabelFor(comboBoxNum);

        Label labelId = new Label("户主id");
        TextField textFieldId = new TextField();
        labelId.setLabelFor(textFieldId);

        Button button = new Button("提交");
        button.setOnAction(event -> {
            String id = textFieldId.getText();
            if (id.matches("^[0-9]+$")) {
                int result = parkInsertDao.insertBuy(getSelectString(comboBoxCommunity), parseInt(getSelectString(comboBoxNum)), parseInt(id));
                showResult(result);
            } else {
                textFieldId.setText("");
                textFieldId.setPromptText("请输入数字id");
            }
        });
        parkBuy.getChildren().addAll(labelBuy, labelCommunity, comboBoxCommunity, labelNum, comboBoxNum, labelId, textFieldId, button);
        parkBuy.setSpacing(10);
    }

    private void showResult(int result) {
        switch (result) {
            case 0:
                showAlert(Alert.AlertType.INFORMATION, "成功插入数据");
                break;
            case 1:
                showAlert(Alert.AlertType.WARNING, "该车位已被购买或者租用");
                break;
            case 2:
                showAlert(Alert.AlertType.ERROR, "该住户不存在");
                break;
        }
    }
}

package pane;

import dao.CheckDao;
import entity.CheckRecordEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ControllerUtil;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.ControllerUtil.getComboBox;
import static utils.ControllerUtil.getSelectString;
import static utils.ControllerUtil.showAlert;
import static utils.DateUtil.getStringByLength;
import static utils.DateUtil.yearMonthToFirstDayMonth;
import static utils.DateUtil.yearMonthToLastDayMonth;

public class CheckPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxInsertService = new HBox();
    private HBox hBoxInsert = new HBox();
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent;
    private CheckDao checkDao = new CheckDao();

    public CheckPane() {
        Label labelText = new Label("排查记录管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxInsertService();
        initHBoxInsert();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxInsertService, hBoxInsert, scrollPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBoxButton() {
        String[] year = new String[100];
        for (int i = 0; i < 100; i++) {
            year[i] = (i + 2018) + "";
        }
        String[] month = new String[12];
        for (int i = 0; i < 12; i++) {
            month[i] = (i + 1) + "";
        }
        Label labelStart = new Label("开始月份");
        Label labelStartYear = new Label("年");
        ComboBox<String> comboBoxStartYear = ControllerUtil.getComboBox(year);
        Label labelStartMonth = new Label("月");
        ComboBox<String> comboBoxStartMonth = ControllerUtil.getComboBox(month);
        Label labelSEnd = new Label("结束月份");
        Label labelEndYear = new Label("年");
        ComboBox<String> comboBoxEndYear = ControllerUtil.getComboBox(year);
        Label labelEndMonth = new Label("月");
        ComboBox<String> comboBoxEndMonth = ControllerUtil.getComboBox(month);
        Button button = new Button("查询");
        button.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            initRecord(start, end);
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, button);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initHBoxInsert() {
        RadioButton radioButtonIndoor = new RadioButton("室内");
        radioButtonIndoor.setUserData("室内");
        RadioButton radioButtonOutdoor = new RadioButton("室外");
        radioButtonOutdoor.setUserData("室外");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(radioButtonIndoor, radioButtonOutdoor);
        toggleGroup.selectToggle(radioButtonIndoor);
        CheckBox checkBoxIsNeedRepair = new CheckBox("需要维修");
        Button button = new Button("插入排查记录");
        button.setOnAction(event -> {
            String type = toggleGroup.getSelectedToggle().getUserData().toString();
            String isNeed = checkBoxIsNeedRepair.isSelected() ? "1" : "0";
            insert(type, isNeed);
        });
        hBoxInsert.getChildren().addAll(radioButtonIndoor, radioButtonOutdoor, checkBoxIsNeedRepair, button);
        hBoxInsert.setAlignment(Pos.CENTER);
        hBoxInsert.setSpacing(20);
    }

    private void initHBoxInsertService() {
//        String[] community = {"A", "B", "C"};
//        String[] unit = {"1", "2", "3", "4", "5", "6"};
//        String[] room = new String[36];
//        int count = 0;
//        for (int i = 1; i <= 9; i++) {
//            for (int j = 1; j <= 4; j++) {
//                room[count] = i + "0" + j;
//                count++;
//            }
//        }
//        Label labelCommunity = new Label("小区：");
//        ComboBox<String> comboBoxCommunity = getComboBox(community);
//        labelCommunity.setLabelFor(comboBoxCommunity);
//
//        Label labelUnit = new Label("单元：");
//        ComboBox<String> comboBoxUnit = getComboBox(unit);
//        labelUnit.setLabelFor(comboBoxUnit);
//
//        Label labelRoom = new Label("房间号：");
//        ComboBox<String> comboBoxRoom = getComboBox(room);
//        labelRoom.setLabelFor(comboBoxRoom);

        String[] device = {"电梯", "楼道灯", "门禁", "下水道", "消防栓", "健身器材", "公告牌", "照明灯", "草坪"};
        Label labelType = new Label("维修项目");
        ComboBox<String> comboBoxDevice = getComboBox(device);
        labelType.setLabelFor(comboBoxDevice);
        TextField checkId = new TextField();
        checkId.setPromptText("排查记录id");

        Button button = new Button("插入维修记录");
        button.setOnAction(event -> {
            String inputCheckId = checkId.getText();
            if (inputCheckId.equals("") || !inputCheckId.matches("^[0-9]+$")) {
                checkId.setPromptText("请输入数字id");
            } else {
                insertService(getSelectString(comboBoxDevice), parseInt(inputCheckId));
            }
        });
        hBoxInsertService.getChildren().addAll(labelType, comboBoxDevice, checkId, button);
        hBoxInsertService.setAlignment(Pos.CENTER);
        hBoxInsertService.setSpacing(20);
    }

    private void initRecord(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<CheckRecordEntity> arrayList = checkDao.query(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("排查时间", 30) + "\t" + getStringByLength("排查id", 10)
                + "\t" + getStringByLength("排查范围", 10) + "\t" + getStringByLength("是否需要维修", 10)));
        vBoxContent.getChildren().add(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        for (int i = 0; i < arrayList.size(); i++) {
            CheckRecordEntity recordEntity = arrayList.get(i);
            String indoor = recordEntity.getIsIndoor().equals("1") ? "室内" : "室外";
            String isNeed = recordEntity.getIsNeedService().equals("1") ? "需要" : "不需要";
            hBox = new HBox();
            hBox.getChildren().addAll(new Text(getStringByLength(recordEntity.getCheckTime().toString(), 30) + "\t" +
                    getStringByLength(recordEntity.getCheckId() + "", 10)
                    + "\t" + getStringByLength(indoor, 10) + "\t" + getStringByLength(isNeed, 10)));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
    }

    private void insert(String type, String isNeed) {
        boolean result = checkDao.insert(type, isNeed);
        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "插入成功");
        } else {
            showAlert(Alert.AlertType.WARNING, "插入失败");
        }
    }

    private void insertService(String type, int checkId) {
        int result = checkDao.insertService(type, checkId);
        switch (result) {
            case 0:
                showAlert(Alert.AlertType.INFORMATION, "成功插入");
                break;
            case 1:
                showAlert(Alert.AlertType.WARNING, "没有对应的排查id或不需要维修");
                break;
            case 2:
                showAlert(Alert.AlertType.WARNING, "设备类型与排查范围不对应");
                break;
            case 3:
                showAlert(Alert.AlertType.ERROR, "插入失败");
                break;
        }
    }
}

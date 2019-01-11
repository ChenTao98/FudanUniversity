package pane;

import dao.RepairDao;
import entity.RepairByHouseEntity;
import entity.RepairByTypeEntity;
import entity.RepairRecordEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ControllerUtil;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.ControllerUtil.getComboBox;
import static utils.ControllerUtil.showAlert;
import static utils.DateUtil.getStringByLength;
import static utils.DateUtil.yearMonthToFirstDayMonth;
import static utils.DateUtil.yearMonthToLastDayMonth;

public class RepairPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxInsert = new HBox();
    private HBox hBoxText = new HBox();
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent;
    private Text textAll;
    private Text textInValid;
    private RepairDao repairDao = new RepairDao();
    private String[] device = {"电梯", "楼道灯", "门禁", "下水道", "消防栓"};

    public RepairPane() {
        Label labelText = new Label("报修信息管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxInsert();
        initHBoxText();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxInsert, hBoxText, scrollPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBoxText() {
        textAll = new Text("期间总报修数");
        textInValid = new Text("未维修数");
        hBoxText.getChildren().addAll(textAll, textInValid);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
    }

    private void setText(String total, String need) {
        textAll.setText(total);
        textInValid.setText(need);
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
        Button buttonNeedService = new Button("报修记录");
        buttonNeedService.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            queryRepair(start, end);
        });
        Button buttonHouse = new Button("按户查询");
        buttonHouse.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            queryByHouse(start, end);
        });
        Button buttonType = new Button("按类型查询");
        buttonType.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            queryByType(start, end);
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, buttonNeedService, buttonHouse, buttonType);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initHBoxInsert() {
        String[] community = {"A", "B", "C"};
        String[] unit = {"1", "2", "3", "4", "5", "6"};
        String[] room = new String[36];
        int count = 0;
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 4; j++) {
                room[count] = i + "0" + j;
                count++;
            }
        }
        Label labelCommunity = new Label("小区：");
        ComboBox<String> comboBoxCommunity = getComboBox(community);
        labelCommunity.setLabelFor(comboBoxCommunity);

        Label labelUnit = new Label("单元：");
        ComboBox<String> comboBoxUnit = getComboBox(unit);
        labelUnit.setLabelFor(comboBoxUnit);

        Label labelRoom = new Label("房间号：");
        ComboBox<String> comboBoxRoom = getComboBox(room);
        labelRoom.setLabelFor(comboBoxRoom);


        Label labelType = new Label("报修项目");
        ComboBox<String> comboBoxDevice = getComboBox(device);
        labelType.setLabelFor(comboBoxDevice);
        TextField repairReason = new TextField();
        repairReason.setPromptText("报修原因");

        Button button = new Button("报修");
        button.setOnAction(event -> {
            String reason = repairReason.getText();
            String inputCommunity = comboBoxCommunity.getSelectionModel().getSelectedItem();
            String inputUnit = comboBoxUnit.getSelectionModel().getSelectedItem();
            String roomNum = comboBoxRoom.getSelectionModel().getSelectedItem();
            String type = comboBoxDevice.getSelectionModel().getSelectedItem();
            if (!reason.equals("")) {
                boolean result = repairDao.insert(inputCommunity, parseInt(inputUnit), parseInt(roomNum), type, reason);
                if (result) {
                    showAlert(Alert.AlertType.INFORMATION, "成功报修");
                } else {
                    showAlert(Alert.AlertType.WARNING, "报修失败，该房间未出售");
                }
            } else {
                repairReason.setPromptText("请输入报修原因");
            }
        });
        hBoxInsert.getChildren().addAll(labelCommunity, comboBoxCommunity, labelUnit, comboBoxUnit, labelRoom, comboBoxRoom, labelType, comboBoxDevice, repairReason, button);
        hBoxInsert.setAlignment(Pos.CENTER);
        hBoxInsert.setSpacing(20);
    }

    private void queryRepair(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<RepairRecordEntity> arrayList = repairDao.queryRepair(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("报修时间", 30) + "\t" + getStringByLength("报修id", 10) + "\t" + getStringByLength("报修类型", 10) + "\t" +
                getStringByLength("报修原因", 10) + "\t" + getStringByLength("房间id", 10) + "\t" + "报修状态"));
        vBoxContent.getChildren().add(hBox);
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            RepairRecordEntity recordEntity = arrayList.get(i);
            hBox = new HBox();
            Button button = new Button("已维修");
            hBox.getChildren().addAll(new Text(getStringByLength(recordEntity.getRepairTime().toString(), 20) + "\t" + getStringByLength(recordEntity.getRepairId() + "", 10) + "\t" +
                    getStringByLength(device[recordEntity.getDeviceId() - 1], 15) + "\t" + getStringByLength(recordEntity.getRepairReason(), 15) + "\t" + getStringByLength(recordEntity.getRoomId() + "", 10)), button);
            vBoxContent.getChildren().add(hBox);
            hBox.setSpacing(10);
            if (recordEntity.getIsService().equals("0")) {
                count++;
                button.setOnAction(event -> {
                    boolean result = repairDao.insertService(recordEntity.getDeviceId(), recordEntity.getRepairId());
                    if (result) {
                        showAlert(Alert.AlertType.INFORMATION, "成功维修");
                        button.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "维修失败");
                    }
                });
            } else {
                button.setDisable(true);
            }

            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
        setText("期间总维修数" + arrayList.size(), "未维修数" + count);
    }

    private void queryByHouse(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<RepairByHouseEntity> arrayList = repairDao.queryByHouse(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("小区", 10) + "\t" + getStringByLength("单元号", 10) + "\t" +
                getStringByLength("房间号", 10) + "\t" + getStringByLength("报修次数", 10)));
        vBoxContent.getChildren().add(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        for (int i = 0; i < arrayList.size(); i++) {
            RepairByHouseEntity repair = arrayList.get(i);
            hBox = new HBox();
            hBox.getChildren().addAll(new Text(getStringByLength(repair.getCommunity(), 10) + "\t" + getStringByLength(repair.getUnit() + "", 20) + "\t" +
                    getStringByLength(repair.getRoomNum() + "", 15) + "\t" + getStringByLength(repair.getCount() + "", 15)));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
    }

    private void queryByType(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<RepairByTypeEntity> arrayList = repairDao.queryByType(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("类型id", 10) + "\t" + getStringByLength("报修类型", 10) + "\t" +
                getStringByLength("报修次数", 10)));
        vBoxContent.getChildren().add(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        for (int i = 0; i < arrayList.size(); i++) {
            RepairByTypeEntity repair = arrayList.get(i);
            hBox = new HBox();
            hBox.getChildren().addAll(new Text(getStringByLength(repair.getDeviceType() + "", 10) + "\t" + getStringByLength(repair.getDeviceName(), 15) + "\t" +
                    getStringByLength(repair.getCount() + "", 10)));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
    }
}

package pane;

import dao.ComplaintDao;
import entity.ComplaintRecordEntity;
import entity.RepairByHouseEntity;
import entity.RepairByTypeEntity;
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

public class ComplaintPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxInsert = new HBox();
    private HBox hBoxText = new HBox();
    private HBox hBoxUpdate = new HBox();
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent;
    private Text textAll;
    private Text textInValid;
    private ComplaintDao complaintDao = new ComplaintDao();

    public ComplaintPane() {
        Label labelText = new Label("投诉信息管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxInsert();
        initHBoxText();
        initHBoxUpdate();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxInsert, hBoxUpdate, hBoxText, scrollPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBoxText() {
        textAll = new Text("期间总投诉数");
        textInValid = new Text("未处理数");
        hBoxText.getChildren().addAll(textAll, textInValid);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
    }

    private void initHBoxUpdate() {
        TextField textId = new TextField();
        textId.setPromptText("投诉id");
        TextField textResult = new TextField();
        textResult.setPromptText("处理结果");
        TextField textProcess = new TextField();
        textProcess.setPromptText("处理过程");
        Button button = new Button("更新处理结果");
        button.setOnAction(event -> {
            String id = textId.getText();
            String result = textResult.getText();
            String process = textProcess.getText();
            if (id.equals("") || !id.matches("^[0-9]+$") || result.equals("") || process.equals("")) {
                showAlert(Alert.AlertType.WARNING, "请输入正确信息");
            } else {
                update(parseInt(textId.getText()), textResult.getText(), textProcess.getText());
            }
        });
        hBoxUpdate.getChildren().addAll(textId, textResult, textProcess, button);
        hBoxUpdate.setAlignment(Pos.CENTER);
        hBoxUpdate.setSpacing(20);
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
        Button buttonNeed = new Button("投诉记录");
        buttonNeed.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            initComplaint(start, end);
        });
        Button buttonQuery = new Button("按户查询");
        buttonQuery.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            initByHouse(start, end);
        });
        Button buttonType = new Button("统计记录");
        buttonType.setOnAction(event -> {
            String startYear = comboBoxStartYear.getSelectionModel().getSelectedItem();
            String startMonth = comboBoxStartMonth.getSelectionModel().getSelectedItem();
            String endYear = comboBoxEndYear.getSelectionModel().getSelectedItem();
            String endMonth = comboBoxEndMonth.getSelectionModel().getSelectedItem();
            String start = yearMonthToFirstDayMonth(parseInt(startYear), parseInt(startMonth));
            String end = yearMonthToLastDayMonth(parseInt(endYear), parseInt(endMonth));
            initByType(start, end);
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, buttonNeed, buttonQuery, buttonType);
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

        String[] type = {"住户问题", "设备问题", "安保问题", "费用问题", "其他问题"};
        Label labelType = new Label("投诉类型");
        ComboBox<String> comboBoxDevice = getComboBox(type);
        labelType.setLabelFor(comboBoxDevice);
        TextField complaintReason = new TextField();
        complaintReason.setPromptText("投诉内容");

        Button button = new Button("投诉");
        button.setOnAction(event -> {
            String reason = complaintReason.getText();
            if (!reason.equals("")) {
                insert(getSelectString(comboBoxCommunity), parseInt(getSelectString(comboBoxUnit)), parseInt(getSelectString(comboBoxRoom)),
                        getSelectString(comboBoxDevice), reason);
            } else {
                complaintReason.setPromptText("请输入报修原因");
            }
        });
        hBoxInsert.getChildren().addAll(labelCommunity, comboBoxCommunity, labelUnit, comboBoxUnit, labelRoom, comboBoxRoom, labelType, comboBoxDevice, complaintReason, button);
        hBoxInsert.setAlignment(Pos.CENTER);
        hBoxInsert.setSpacing(20);
    }

    private void insert(String community, int unit, int roomNum, String type, String content) {
        boolean result = complaintDao.insert(community, unit, roomNum, type, content);
        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "成功投诉");
        } else {
            showAlert(Alert.AlertType.WARNING, "投诉失败，该房间未出售");
        }
    }

    private void update(int complaintId, String process_result, String process) {
        boolean result = complaintDao.update(complaintId, process_result, process);
        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "更新成功");
        } else {
            showAlert(Alert.AlertType.WARNING, "更新失败,该记录已更新过");
        }
    }

    private void initComplaint(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<ComplaintRecordEntity> arrayList = complaintDao.queryComplaint(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("投诉时间", 35) + "\t" + getStringByLength("投诉id", 10) + "\t" +
                getStringByLength("投诉类型", 10) + "\t" + getStringByLength("投诉原因", 10) + "\t" +
                getStringByLength("房间id", 10) + "\t" + getStringByLength("投诉状态", 10) + "\t" +
                getStringByLength("处理结果", 10) + "\t" + "处理过程"));
        vBoxContent.getChildren().add(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            ComplaintRecordEntity recordEntity = arrayList.get(i);
            String state = recordEntity.getIsProcess().equals("1") ? "已处理" : "未处理";
            if (recordEntity.getIsProcess().equals("0")) {
                count++;
            }
            String result = recordEntity.getProcessResult() == null ? "" : recordEntity.getProcessResult();
            String how = recordEntity.getHowProcess() == null ? "" : recordEntity.getHowProcess();
            hBox = new HBox();
            hBox.getChildren().addAll(new Text(getStringByLength(recordEntity.getComplaintTime().toString(), 30) + "\t" +
                    getStringByLength(recordEntity.getComplaintId() + "", 10) + "\t" + getStringByLength(recordEntity.getComplaintType(), 10)
                    + "\t" + getStringByLength(recordEntity.getComplaintContent(), 12) + "\t" +
                    getStringByLength(recordEntity.getRoomId() + "", 10) + "\t" + getStringByLength(state, 10) + "\t" +
                    getStringByLength(result, 10) + "\t" + how));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
        updateText(arrayList.size(), count);
    }

    private void initByHouse(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<RepairByHouseEntity> arrayList = complaintDao.queryHouse(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("小区", 10) + "\t" + getStringByLength("单元号", 10) + "\t" +
                getStringByLength("房间号", 10) + "\t" + getStringByLength("投诉次数", 10)));
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

    private void initByType(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<RepairByTypeEntity> arrayList = complaintDao.queryType(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Text(getStringByLength("投诉类型", 10) + "\t" +
                getStringByLength("投诉次数", 10)));
        vBoxContent.getChildren().add(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        for (int i = 0; i < arrayList.size(); i++) {
            RepairByTypeEntity repair = arrayList.get(i);
            hBox = new HBox();
            hBox.getChildren().addAll(new Text(getStringByLength(repair.getDeviceName(), 15) + "\t" +
                    getStringByLength(repair.getCount() + "", 10)));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
    }

    private void updateText(int total, int need) {
        textAll.setText("期间总投诉数：" + total);
        textInValid.setText("未处理数：" + need);
    }
}

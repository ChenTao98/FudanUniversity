package pane;

import dao.ServiceDao;
import entity.ServiceRowEntity;
import entity.ServiceTypeEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.ControllerUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.DateUtil.getStringByLength;
import static utils.DateUtil.yearMonthToFirstDayMonth;
import static utils.DateUtil.yearMonthToLastDayMonth;

public class ServicePane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxText = new HBox();
    private GridPane gridPane = new GridPane();
    private Text textAll;
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent = new VBox();
    private Text textAmount;
    private ServiceDao serviceDao = new ServiceDao();
    private DecimalFormat df = new DecimalFormat("0.00");

    public ServicePane() {
        Label labelText = new Label("维修记录管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxText();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxText, gridPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBoxText() {
        textAll = new Text("期间总维修数 ");
        textAmount = new Text("维修总费用 ");
        hBoxText.getChildren().addAll(textAll, textAmount);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
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
        Button buttonHouse = new Button("查询");
        buttonHouse.setOnAction(event -> {
            String startTime = yearMonthToFirstDayMonth(parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem()));
            String endTime = yearMonthToLastDayMonth(parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem()));
            query(startTime, endTime);
        });
        Button buttonType = new Button("按类型查询");
        buttonType.setOnAction(event -> {
            String startTime = yearMonthToFirstDayMonth(parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem()));
            String endTime = yearMonthToLastDayMonth(parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem()));
            queryType(startTime, endTime);
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, buttonHouse, buttonType);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void query(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<ServiceRowEntity> arrayList = serviceDao.getService(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Text(getStringByLength("维修时间", 30) + "\t" + getStringByLength("维修类型", 10)
                + "\t" + getStringByLength("维修金额", 10) + "\t" + getStringByLength("排查id", 10) + "\t" + "报修id"));
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        double amount = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            hBox = new HBox();
            ServiceRowEntity rowEntity = arrayList.get(i);
            amount += rowEntity.getAmount();
            String repairId = rowEntity.getRepairId() == 0 ? "" : "" + rowEntity.getRepairId();
            String checkId = rowEntity.getCheckId() == 0 ? "" : "" + rowEntity.getCheckId();
            hBox.getChildren().add(new Text(getStringByLength(rowEntity.getServiceTime().toString(), 10) + "\t" +
                    getStringByLength(rowEntity.getDeviceName(), 15) + "\t" + getStringByLength(df.format(rowEntity.getAmount()), 15)
                    + "\t" + getStringByLength(checkId + "", 10) + "\t" + repairId));
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            vBoxContent.getChildren().add(hBox);
        }
        setText(arrayList.size(), amount);
    }

    private void queryType(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<ServiceTypeEntity> arrayList = serviceDao.getTypeEntity(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Text(getStringByLength("类型id", 10) + "\t" +
                getStringByLength("维修类型", 10) + "\t" + getStringByLength("维修数量", 10)
                + "\t" + getStringByLength("维修金额", 10)));
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        int count = 0;
        double amount = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            ServiceTypeEntity typeEntity = arrayList.get(i);
            hBox = new HBox();
            count += typeEntity.getCount();
            amount += typeEntity.getCount() * typeEntity.getAmount();
            hBox.getChildren().add(new Text(getStringByLength(typeEntity.getDeviceId() + "", 10) + "\t" +
                    getStringByLength(typeEntity.getDeviceName(), 15) + "\t" + getStringByLength(typeEntity.getCount() + "", 20)
                    + "\t" + getStringByLength(df.format(typeEntity.getAmount() * typeEntity.getCount()), 10)));
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            vBoxContent.getChildren().add(hBox);
        }
        setText(count, amount);
    }

    private void setText(int count, double amount) {
        textAll.setText("期间总维修数：" + count);
        textAmount.setText("维修总金额：" + amount);
    }
}

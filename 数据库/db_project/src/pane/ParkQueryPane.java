package pane;

import dao.ParkQueryDao;
import entity.ParkBuyEntity;
import entity.ParkRentEntity;
import entity.ParkTempEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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

public class ParkQueryPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxTime = new HBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxText = new HBox();
    private VBox vBoxContent;
    private ScrollPane scrollPane = new ScrollPane();
    private ArrayList<ParkTempEntity> arrayListTemp;
    private ArrayList<ParkRentEntity> arrayListRent;
    private ArrayList<ParkBuyEntity> arrayListBuy;
    private Text textAll;
    private Text textTemp;
    private Text textRent;
    private Text textBuy;
    private ParkQueryDao parkQueryDao = new ParkQueryDao();

    public ParkQueryPane() {
        Label labelText = new Label("停车收费查询");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxText();

        vBoxCenter.getChildren().addAll(hBoxTime, hBoxButton, hBoxText, scrollPane);
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
        hBoxTime.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd, comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth);
        hBoxTime.setSpacing(20);
        hBoxTime.setAlignment(Pos.CENTER);
        RadioButton radioButtonTemp = new RadioButton("临时停车收费");
        radioButtonTemp.setUserData("临时车位");
        RadioButton radioButtonRent = new RadioButton("租用车位收费");
        radioButtonRent.setUserData("已租车位");
        RadioButton radioButtonBuy = new RadioButton("购买车位收费");
        radioButtonBuy.setUserData("已购车位");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(radioButtonBuy, radioButtonTemp, radioButtonRent);
        toggleGroup.selectToggle(radioButtonTemp);
        Button button = new Button("查询");
        button.setOnAction(event -> {
            int startYear = parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem());
            int startMonth = parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem());
            int endYear = parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem());
            int endMonth = parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem());
            String startTime = yearMonthToFirstDayMonth(startYear, startMonth);
            String endTime = yearMonthToLastDayMonth(endYear, endMonth);
            String type = (String) toggleGroup.getSelectedToggle().getUserData();
            setText(parkQueryDao.getAmount(startTime, endTime));
            switch (type) {
                case "临时车位":
                    arrayListTemp = parkQueryDao.queryTemp(startTime, endTime);
                    initTemp(0, 10);
                    break;
                case "已租车位":
                    arrayListRent = parkQueryDao.queryRent(startTime, endTime);
                    initRent();
                    break;
                case "已购车位":
                    arrayListBuy = parkQueryDao.queryBuy(startTime, endTime);
                    initBuy();
                    break;
            }
        });
        hBoxButton.getChildren().addAll(radioButtonTemp, radioButtonRent, radioButtonBuy, button);
        hBoxButton.setSpacing(20);
        hBoxButton.setAlignment(Pos.CENTER);
    }

    private void initHBoxText() {
        textAll = new Text("期间总共收费：");
        textTemp = new Text("临时收费：");
        textRent = new Text("租用收费：");
        textBuy = new Text("购买收费：");
        hBoxText.getChildren().addAll(textAll, textTemp, textBuy, textRent);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
    }

    private void initTemp(int from, int to) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textTime;
        Text textCarNum;
        Text textCharge;
        Text textID;
        textTime = new Text(getStringByLength("时间", 30));
        textCarNum = new Text(getStringByLength("车牌号", 10));
        textCharge = new Text(getStringByLength("收费", 6));
        textID = new Text(getStringByLength("车位id", 10));
        hBox.getChildren().addAll(textTime, textCarNum, textCharge, textID);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-background-color:#E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        for (int i = 0; i < arrayListTemp.size(); i++) {
            ParkTempEntity parkTempEntity = arrayListTemp.get(i);
            hBox = new HBox();
            Text text = new Text(parkTempEntity.getParkTime().toString() + "\t" + getStringByLength(parkTempEntity.getLicenseNum(), 10) + "\t" + getStringByLength(parkTempEntity.getCharge() + "", 10) + "\t" + parkTempEntity.getParkId());

            hBox.getChildren().addAll(text);
            hBox.setSpacing(20);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color:#E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            vBoxContent.getChildren().add(hBox);
        }
    }

    private void initRent() {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textStart = new Text(getStringByLength("起始时间", 30));
        Text textEnd = new Text(getStringByLength("终止时间", 30));
        Text textCharge = new Text(getStringByLength("收费", 10));
        Text textID = new Text(getStringByLength("车位id", 10));
        Text textHouseID = new Text(getStringByLength("户主id", 10));
        hBox.getChildren().addAll(textStart, textEnd, textCharge, textID, textHouseID);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        for (int i = 0; i < arrayListRent.size(); i++) {
            ParkRentEntity parkRentEntity = arrayListRent.get(i);
            hBox = new HBox();
            Text text = new Text(parkRentEntity.getRentStartTime() + "\t" + parkRentEntity.getRentEndTime() + "\t" + getStringByLength(parkRentEntity.getCharge() + "", 10)
                    + "\t" + getStringByLength(parkRentEntity.getParkId() + "", 10) + "\t" + parkRentEntity.getHouseholdId());
            hBox.getChildren().addAll(text);
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color:#E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
    }

    private void initBuy() {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textStart = new Text(getStringByLength("购买时间", 30));
        Text textCharge = new Text(getStringByLength("收费", 15));
        Text textID = new Text(getStringByLength("车位id", 10));
        Text textHouseID = new Text(getStringByLength("户主id", 10));
        hBox.getChildren().addAll(textStart, textCharge, textID, textHouseID);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        for (int i = 0; i < arrayListBuy.size(); i++) {
            ParkBuyEntity park = arrayListBuy.get(i);
            hBox = new HBox();
            Text text = new Text(park.getBuyTime() + "\t" + getStringByLength(park.getCharge() + "", 10) + "\t" + getStringByLength(park.getParkId() + "", 10)
                    + "\t" + park.getHouseholdId());
            hBox.getChildren().addAll(text);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            vBoxContent.getChildren().add(hBox);
        }
    }

    private void setText(double[] doubles) {
        DecimalFormat df = new DecimalFormat("0.00");
        textAll.setText("期间总共收费：" + df.format(doubles[0] + doubles[1] + doubles[2]));
        textTemp.setText("临时收费：" + df.format(doubles[0]));
        textRent.setText("租用收费：" + df.format(doubles[1]));
        textBuy.setText("购买收费：" + df.format(doubles[2]));
    }

}

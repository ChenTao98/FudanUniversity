package pane;

import dao.PropertyProfitQueryDao;
import entity.ParkInfoEntity;
import entity.PropertyIncomeEntity;
import entity.PropertyOutcomeEntity;
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

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.DateUtil.formatDouble;
import static utils.DateUtil.yearMonthToFirstDayMonth;
import static utils.DateUtil.yearMonthToLastDayMonth;

public class PropertyProfitPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private VBox vBoxContent;
    private HBox hBoxButton = new HBox();
    private HBox hBoxText = new HBox();
    private Text textAll;
    private ScrollPane scrollPane = new ScrollPane();
    private ArrayList<PropertyIncomeEntity> arrayListPropertyIncome;
    private ArrayList<PropertyOutcomeEntity> arrayListPropertyOutcome;
    private PropertyProfitQueryDao propertyProfitQueryDao = new PropertyProfitQueryDao();
    private Text textIncome;
    private Text textOutcome;

    public PropertyProfitPane() {
        Label labelText = new Label("物业收支汇总");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBox();
        initText();

        vBoxCenter.getChildren().addAll(hBoxButton, hBoxText);
        vBoxCenter.setSpacing(20);
        setCenter(vBoxCenter);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
    }

    private void initHBox() {
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
            int startYear = parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem());
            int startMonth = parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem());
            int endYear = parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem());
            int endMonth = parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem());
            String startTime = yearMonthToFirstDayMonth(startYear, startMonth);
            String endTime = yearMonthToLastDayMonth(endYear, endMonth);
            arrayListPropertyIncome = propertyProfitQueryDao.queryIncome(startTime, endTime);
            arrayListPropertyOutcome = propertyProfitQueryDao.queryOutcome(startTime, endTime);
            setText(propertyProfitQueryDao.queryProfit(startTime, endTime));
            initHBoxButton();

            System.out.println(comboBoxStartYear.getSelectionModel().getSelectedItem() + " " + comboBoxStartMonth.getSelectionModel().getSelectedItem());
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, button);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initText() {
        textIncome = new Text("期间总收入");
        textOutcome = new Text("期间总支出");
        textAll = new Text("期间总盈利");
        hBoxText.getChildren().addAll(textIncome, textOutcome, textAll);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
    }

    private void initHBoxButton() {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textTime;
        Text textIncome;
        Text textOutcome;
        Text textProfit;
        textTime = new Text(getStringByLength("时间", 35));
        textIncome = new Text(getStringByLength("收入", 10));
        textOutcome = new Text(getStringByLength("支出", 10));
        textProfit = new Text(getStringByLength("盈利", 10));
        hBox.getChildren().addAll(textTime, textIncome, textOutcome, textProfit);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        int lengthIn = arrayListPropertyIncome.size();
        int lengthOut = arrayListPropertyOutcome.size();
        int lengthMax = lengthIn < lengthOut ? lengthOut : lengthIn;
        int lengthMin = lengthIn < lengthOut ? lengthIn : lengthOut;
        boolean flag = lengthIn < lengthOut;
        for (int i = 0; i < lengthMax; i++) {
            Timestamp timestamp;
            double income = 0;
            double outcome = 0;
            if (i >= lengthMin) {
                if (flag) {
                    PropertyOutcomeEntity propertyOutcomeEntity = arrayListPropertyOutcome.get(i);
                    timestamp = propertyOutcomeEntity.getOutcomeTime();
                    outcome = propertyOutcomeEntity.getOutcomeAmount();
                } else {
                    PropertyIncomeEntity propertyIncomeEntity = arrayListPropertyIncome.get(i);
                    timestamp = propertyIncomeEntity.getIncomeTime();
                    income = propertyIncomeEntity.getIncomeAmount();
                }
            } else {
                PropertyIncomeEntity propertyIncomeEntity = arrayListPropertyIncome.get(i);
                PropertyOutcomeEntity propertyOutcomeEntity = arrayListPropertyOutcome.get(i);
                timestamp = propertyIncomeEntity.getIncomeTime();
                income = propertyIncomeEntity.getIncomeAmount();
                outcome = propertyOutcomeEntity.getOutcomeAmount();
            }

            hBox = new HBox();
            Text text = new Text(getStringByLength(timestamp.toString() + "", 15) + "\t"
                    + getStringByLength(formatDouble(income) + "", 15) + "\t" +
                    getStringByLength(formatDouble(outcome) + "", 10) + "\t" +
                    getStringByLength(formatDouble(income - outcome) + "", 15));
            hBox.getChildren().addAll(text);
            hBox.setSpacing(20);
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
        textIncome.setText("期间总收入：" + df.format(doubles[0]));
        textOutcome.setText("期间总支出：" + df.format(doubles[1]));
        textAll.setText("期间总盈利：" + df.format(doubles[2]));
    }

    private String getStringByLength(String string, int length) {
        for (int i = string.length(); i < length; i++) {
            string += " ";
        }
        return string;
    }
}

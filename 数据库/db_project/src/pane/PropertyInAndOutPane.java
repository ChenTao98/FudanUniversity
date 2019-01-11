package pane;

import dao.PropertyInAndOutDao;
import entity.PropertyIncomeEntity;
import entity.PropertyOutcomeEntity;
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

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static utils.ControllerUtil.getComboBox;
import static utils.ControllerUtil.showAlert;
import static utils.DateUtil.getStringByLength;
import static utils.DateUtil.yearMonthToFirstDayMonth;
import static utils.DateUtil.yearMonthToLastDayMonth;

public class PropertyInAndOutPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxInsert = new HBox();
    private HBox hBoxText = new HBox();
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent = new VBox();
    private Text textAll;
    private Text textPro;
    private Text textPark;
    private Text textAd;
    private Text textElse;
    private PropertyInAndOutDao propertyInAndOutDao = new PropertyInAndOutDao();
    private DecimalFormat df = new DecimalFormat("0.00");

    public PropertyInAndOutPane() {
        Label labelText = new Label("物业收支查询");
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
        textAll = new Text("期间总收入");
        textPark = new Text("停车总收入");
        textPro = new Text("物业总收入");
        textAd = new Text("广告总收入");
        textElse = new Text("其他总收入");
        hBoxText.getChildren().addAll(textAll, textPark, textPro, textAd, textElse);
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
        Button buttonIncome = new Button("查询收入");
        buttonIncome.setOnAction(event -> {
            String startTime = yearMonthToFirstDayMonth(parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem()));
            String endTime = yearMonthToLastDayMonth(parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem()));
            initIncome(startTime, endTime);
        });
        Button buttonOutcome = new Button("查询支出");
        buttonOutcome.setOnAction(event -> {
            String startTime = yearMonthToFirstDayMonth(parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxStartMonth.getSelectionModel().getSelectedItem()));
            String endTime = yearMonthToLastDayMonth(parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem()), parseInt(comboBoxEndMonth.getSelectionModel().getSelectedItem()));
            initOutcome(startTime, endTime);
        });
        hBoxButton.getChildren().addAll(labelStart, comboBoxStartYear, labelStartYear, comboBoxStartMonth, labelStartMonth, labelSEnd,
                comboBoxEndYear, labelEndYear, comboBoxEndMonth, labelEndMonth, buttonIncome, buttonOutcome);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initHBoxInsert() {
        String[] incomeType = {"广告收入", "其他收入"};
        Label labelType = new Label("收入类型");
        ComboBox<String> comboBoxIncomeType = getComboBox(incomeType);
        labelType.setLabelFor(comboBoxIncomeType);
        TextField incomeAmount = new TextField();
        incomeAmount.setPromptText("收入金额");

        Button button = new Button("插入收入记录");
        button.setOnAction(event -> {
            String amount = incomeAmount.getText();
            if (amount.matches("^[0-9]+[.]{0,1}[0-9]+$")) {
                boolean result = propertyInAndOutDao.insert(comboBoxIncomeType.getSelectionModel().getSelectedItem(), parseDouble(amount));
                if (result) {
                    showAlert(Alert.AlertType.INFORMATION, "插入成功");
                } else {
                    showAlert(Alert.AlertType.WARNING, "插入失败");
                }
            } else {
                incomeAmount.setText("");
                incomeAmount.setPromptText("请输入金额");
            }
        });
        hBoxInsert.getChildren().addAll(labelType, comboBoxIncomeType, incomeAmount, button);
        hBoxInsert.setAlignment(Pos.CENTER);
        hBoxInsert.setSpacing(20);
    }

    private void initIncome(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<PropertyIncomeEntity> arrayList = propertyInAndOutDao.getIncome(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Text(getStringByLength("收入时间", 30) + "\t" + getStringByLength("收入类型", 10) + "\t" + "收入金额"));
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        for (int i = 0; i < arrayList.size(); i++) {
            PropertyIncomeEntity in = arrayList.get(i);
            hBox = new HBox();
            hBox.getChildren().add(new Text(getStringByLength(in.getIncomeTime().toString(), 10) + "\t" + getStringByLength(in.getIncomeType(), 10)
                    + "\t" + df.format(in.getIncomeAmount())));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
        setIncomeText(start, end);
    }

    private void initOutcome(String start, String end) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        ArrayList<PropertyOutcomeEntity> arrayList = propertyInAndOutDao.getOutcome(start, end);
        HBox hBox = new HBox();
        hBox.getChildren().add(new Text(getStringByLength("支出时间", 30) + "\t" + getStringByLength("支出类型", 10) + "\t" + "支出金额"));
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);

        for (int i = 0; i < arrayList.size(); i++) {
            PropertyOutcomeEntity out = arrayList.get(i);
            hBox = new HBox();
            hBox.getChildren().add(new Text(getStringByLength(out.getOutcomeTime().toString(), 10) + "\t" + getStringByLength(out.getOutcomeType(), 10)
                    + "\t" + df.format(out.getOutcomeAmount())));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
        setOutText(start, end);
    }

    private void setOutText(String start, String end) {
        double out = propertyInAndOutDao.getOutcomeType(start, end);
        textAll.setText("总支出" + df.format(out));
        textElse.setText("");
        textAd.setText("");
        textPark.setText("");
        textPro.setText("");
    }

    private void setIncomeText(String start, String end) {
        double[] out = propertyInAndOutDao.getIncomeType(start, end);
        textAll.setText("总收入" + df.format(out[0] + out[1] + out[2] + out[3]));
        textPark.setText("停车收入" + df.format(out[0]));
        textPro.setText("物业收入" + df.format(out[1]));
        textAd.setText("广告收入" + df.format(out[2]));
        textElse.setText("其他收入" + df.format(out[3]));
    }
}

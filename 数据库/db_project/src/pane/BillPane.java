package pane;

import dao.BillDao;
import entity.HouseholdBillEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static utils.ControllerUtil.showAlert;
import static utils.DateUtil.getStringByLength;

public class BillPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxText = new HBox();
    private ScrollPane scrollPane = new ScrollPane();
    private VBox vBoxContent;
    private Text textAll;
    private BillDao billDao = new BillDao();
    private DecimalFormat df = new DecimalFormat("0.00");

    public BillPane() {
        Label labelText = new Label("住户账单管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBoxButton();
        initHBoxText();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxText, scrollPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBoxButton() {
        RadioButton radioButtonPro = new RadioButton("物业费全缴");
        radioButtonPro.setUserData("物业费全缴");
        RadioButton radioButtonPark = new RadioButton("车位管理费全缴");
        radioButtonPark.setUserData("车位管理费全缴");
        RadioButton radioButtonNewBill = new RadioButton("生成本月账单");
        radioButtonNewBill.setUserData("生成本月账单");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(radioButtonPro, radioButtonPark, radioButtonNewBill);
        toggleGroup.selectToggle(radioButtonPro);

        Button button = new Button("执行");
        Button buttonFresh = new Button("刷新");
        buttonFresh.setOnAction(event -> initBill());
        button.setOnAction(event -> {
            String type = toggleGroup.getSelectedToggle().getUserData().toString();
            switch (type) {
                case "物业费全缴":
                    setProAll();
                    break;
                case "车位管理费全缴":
                    setParkAll();
                    break;
                case "生成本月账单":
                    if (billDao.initBill()) {
                        showAlert(Alert.AlertType.INFORMATION, "成功生成账单");
                        initBill();
                    } else {
                        showAlert(Alert.AlertType.WARNING, "生成账单失败");
                    }
                    break;
            }
        });


        hBoxButton.getChildren().addAll(radioButtonPro, radioButtonPark, radioButtonNewBill, button, buttonFresh);
        hBoxButton.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initBill() {
        ArrayList<HouseholdBillEntity> billEntities = billDao.getBill();
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        HBox hBox = new HBox();

        hBox.getChildren().addAll(new Text(getStringByLength("时间", 30) + "\t" + getStringByLength("用户id", 10) + "\t"
                + getStringByLength("物业费", 10) + "\t" + getStringByLength("物业费状态", 10) + "\t" + getStringByLength("车位费", 10)
                + "\t" + "车位费状态"));
        hBox.setSpacing(20);
        vBoxContent.getChildren().addAll(hBox);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        setText(billEntities.size());
        for (int i = 0; i < billEntities.size(); i++) {
            HouseholdBillEntity billEntity = billEntities.get(i);
            hBox = new HBox();
            vBoxContent.getChildren().add(hBox);
            Button buttonPro = new Button("物业费已交");
            Button buttonPark = new Button("车位费已交");
            hBox.getChildren().addAll(new Text(getStringByLength(billEntity.getBillTime().toString(), 20) + "\t" + getStringByLength(billEntity.getHouseholdId() + "", 10) + "\t"
                    + getStringByLength(df.format(billEntity.getPropertyCharge()), 10)), buttonPro, new Text(getStringByLength(df.format(billEntity.getParkCharge()), 15)), buttonPark);
            hBox.setSpacing(20);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            if (billEntity.getIsProChargePay().equals("0")) {
                buttonPro.setOnAction(event -> {
                    boolean result = billDao.setPro(billEntity.getHouseholdId(), billEntity.getPropertyCharge(), billEntity.getBillTime().toString());
                    if (result) {
                        showAlert(Alert.AlertType.INFORMATION, "物业费成功提交");
                        buttonPro.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "物业费提交失败");
                    }
                });
            } else {
                buttonPro.setDisable(true);
            }
            if (billEntity.getIsParkChargePay().equals("0")) {
                buttonPark.setOnAction(event -> {
                    boolean result = billDao.setPark(billEntity.getHouseholdId(), billEntity.getParkCharge(), billEntity.getBillTime().toString());
                    if (result) {
                        showAlert(Alert.AlertType.INFORMATION, "车位费成功提交");
                        buttonPark.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "车位费提交失败");
                    }
                });
            } else {
                buttonPark.setDisable(true);
            }
        }
    }

    private void setProAll() {
        if (billDao.setProAll()) {
            showAlert(Alert.AlertType.INFORMATION, "物业费成功提交");
        } else {
            showAlert(Alert.AlertType.WARNING, "物业费提交失败");
        }
        initBill();
    }

    private void setParkAll() {
        if (billDao.setParkAll()) {
            showAlert(Alert.AlertType.INFORMATION, "车位费成功提交");
        } else {
            showAlert(Alert.AlertType.WARNING, "车位费提交失败");
        }
        initBill();
    }

    private void initHBoxText() {
        textAll = new Text("总共有？？位未缴费");
        hBoxText.getChildren().addAll(textAll);
        hBoxText.setAlignment(Pos.CENTER);
    }

    private void setText(int num) {
        textAll.setText("总共有" + num + "位未缴费");
    }
}

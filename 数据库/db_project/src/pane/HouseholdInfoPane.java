package pane;

import dao.HouseholdInfoDao;
import entity.HouseholdInfoEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static utils.ControllerUtil.showAlert;
import static utils.DateUtil.getStringByLength;

public class HouseholdInfoPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxDelete = new HBox();
    private HBox hBoxText = new HBox();
    private VBox vBoxContent;
    private ScrollPane scrollPane = new ScrollPane();
    private Text textAll;
    private Text textValid;
    private Text textInValid;
    private HouseholdInfoDao householdInfoDao = new HouseholdInfoDao();

    public HouseholdInfoPane() {
        Label labelText = new Label("住户信息管理");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        textAll = new Text();
        textValid = new Text();
        textInValid = new Text();
        hBoxText.getChildren().addAll(textAll, textValid, textInValid);
        hBoxText.setSpacing(20);
        hBoxText.setAlignment(Pos.CENTER);

        initHBoxButton();
        initHboxDelete();


        vBoxCenter.getChildren().addAll(hBoxButton, hBoxDelete, hBoxText, scrollPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
        initInfo();
    }

    private void initHBoxButton() {
        TextField idCard = new TextField();
        idCard.setPromptText("身份证号");
        TextField name = new TextField();
        name.setPromptText("姓名");
        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("电话号码");
        Button buttonInsert = new Button("插入新住户");
        buttonInsert.setOnAction(event -> {
            String inputIDCard = idCard.getText();
            String inputName = name.getText();
            String inputPhone = phoneNumber.getText();
            if (inputIDCard.equals("") || inputName.equals("") || inputPhone.equals("") || !inputPhone.matches("^[0-9]{1,11}$") || inputIDCard.length() > 18 || inputName.length() > 30) {
                showAlert(Alert.AlertType.WARNING, "请输入信息");

            } else {
                int result = householdInfoDao.insertHousehold(inputIDCard, inputName, inputPhone);
                if (result == 0) {
                    showAlert(Alert.AlertType.WARNING, "插入失败,电话或者身份证已存在");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "成功插入，插入id为：" + result);
                    initInfo();
                }
            }
        });
        hBoxButton.getChildren().addAll(idCard, name, phoneNumber, buttonInsert);
        hBoxButton.setSpacing(20);
        hBoxButton.setAlignment(Pos.CENTER);
    }

    private void initHboxDelete() {
        TextField idText = new TextField();
        idText.setPromptText("删除的用户id");
        Button button = new Button("删除用户");
        button.setOnAction(event -> {
            String idHousehold = idText.getText();
            if (idHousehold.matches("^[0-9]+$")) {
                if (householdInfoDao.delete(parseInt(idHousehold))) {
                    showAlert(Alert.AlertType.INFORMATION, "删除成功");
                    initInfo();
                } else {
                    showAlert(Alert.AlertType.WARNING, "删除失败");
                }
            } else {
                idText.setText("");
                idText.setPromptText("请输入数字id");
            }
        });
        hBoxDelete.getChildren().addAll(idText, button);
        hBoxDelete.setAlignment(Pos.CENTER);
        hBoxButton.setSpacing(20);
    }

    private void initInfo() {
        ArrayList<HouseholdInfoEntity> arrayList = householdInfoDao.getHouseInfo();
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);

        HBox hBox = new HBox();
        Text text = new Text(getStringByLength("用户id", 10) + "\t" + getStringByLength("身份证", 25) + "\t" +
                getStringByLength("姓名", 15) + "\t" + getStringByLength("电话", 20) + "\t" + "是否有效");
        hBox.getChildren().add(text);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        scrollPane.setContent(vBoxContent);
        int valid=0;
        for (int i = 0; i < arrayList.size(); i++) {
            HouseholdInfoEntity info = arrayList.get(i);
            hBox = new HBox();
            if(info.getIsValid().equals("1")){
                valid++;
            }
            hBox.getChildren().add(new Text(getStringByLength(info.getHouseholdId() + "", 10) + "\t" + getStringByLength(info.getHouseholdIdCard(), 20) + "\t" +
                    getStringByLength(info.getHouseholdName(), 15) + "\t" + getStringByLength(info.getHouseholdPhone(), 15) + "\t" + getValid(info.getIsValid())));
            vBoxContent.getChildren().add(hBox);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
        }
        initHBoxText(arrayList.size(),valid);
    }

    private String getValid(String isValid) {
        return isValid.equals("1") ? "有效" : "无效";
    }

    private void initHBoxText(int total,int valid) {
        textAll.setText("总共" + total+ "位住户");
        textValid.setText(valid + "位有效");
        textInValid.setText((total-valid) + "位无效");
    }
}

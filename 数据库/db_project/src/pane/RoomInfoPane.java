package pane;

import dao.RoomInfoInsertDao;
import entity.RoomInfoEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static utils.ControllerUtil.showAlert;

public class RoomInfoPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxDelete = new HBox();
    private HBox hBoxText = new HBox();
    private VBox vBoxContent;
    private ArrayList<RoomInfoEntity> arrayListRoomInfo;
    private ScrollPane scrollPane = new ScrollPane();
    private GridPane gridPane = new GridPane();
    private RoomInfoInsertDao roomInfoInsertDao = new RoomInfoInsertDao();
    private Text textAll;
    private Text textValid;
    private Text textInValid;

    public RoomInfoPane() {
        Label labelText = new Label("房屋信息管理");
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
        initRoomInfo();
    }

    private void initHBoxButton() {
        TextField roomId = new TextField();
        roomId.setPromptText("房屋id");
        TextField householdId = new TextField();
        householdId.setPromptText("户主id");
        Button buttonInsert = new Button("插入新信息");
        buttonInsert.setOnAction(event -> {
            String inputRoomId = roomId.getText();
            String inputHouseholdId = householdId.getText();

            if (inputRoomId.matches("^[0-9]+$") && inputHouseholdId.matches("^[0-9]+$") &&
                    roomInfoInsertDao.exist(Integer.parseInt(inputHouseholdId))) {
                int result = roomInfoInsertDao.insertRoomInfo(Integer.parseInt(inputRoomId), Integer.parseInt(inputHouseholdId));
                if (result == 0) {
                    roomInfoInsertDao.insertRoomInfo(Integer.parseInt(inputRoomId), Integer.parseInt(inputHouseholdId));
                    showAlert(Alert.AlertType.INFORMATION, "成功插入");
                    initRoomInfo();
                } else {
                    showAlert(Alert.AlertType.WARNING, "已经出售");
                }
            } else if (!roomInfoInsertDao.exist(Integer.parseInt(inputHouseholdId))) {
                showAlert(Alert.AlertType.WARNING, "用户不存在");
            } else {
                roomId.setText("");
                roomId.setPromptText("房屋id:请输入数字id");
                householdId.setText("");
                householdId.setPromptText("户主id:请输入数字id");
            }
        });
        hBoxButton.getChildren().addAll(roomId, householdId, buttonInsert);
        hBoxButton.setSpacing(20);
        hBoxButton.setAlignment(Pos.CENTER);
    }

    private void initHBoxText() {
        textAll = new Text("总共???间房屋");
        textValid = new Text("???间空闲");
        textInValid = new Text("???间已出售");
        hBoxText.getChildren().addAll(textAll, textValid, textInValid);
        hBoxText.setSpacing(20);
        hBoxText.setAlignment(Pos.CENTER);
    }

    private void initRoomInfo() {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textRoomId = new Text(getStringByLength("房间Id", 10));
        Text textCommunity = new Text(getStringByLength("小区", 10));
        Text textUnitNum = new Text(getStringByLength("单元号", 10));
        Text textRoomNum = new Text(getStringByLength("房间号", 10));
        Text textRoomArea = new Text(getStringByLength("房间面积", 10));
        Text textPricePerM2 = new Text(getStringByLength("每平方米价格", 10));
        Text textIsSold = new Text(getStringByLength("是否空闲", 10));
        Text textHouseholdId = new Text(getStringByLength("户主id", 10));
        hBox.getChildren().addAll(textRoomId, textCommunity, textUnitNum, textRoomNum, textRoomArea, textPricePerM2, textIsSold, textHouseholdId);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        arrayListRoomInfo = roomInfoInsertDao.queryRoomInfo();
        vBoxContent.getChildren().add(hBox);
        int count = 0;
        for (int i = 0; i < arrayListRoomInfo.size(); i++) {
            RoomInfoEntity roomInfo = arrayListRoomInfo.get(i);
            hBox = new HBox();
            String isSold = "否";
            if (roomInfo.getHouseholdId() == 0) {
                isSold = "是";
                count++;
            }
            Text text = new Text(getStringByLength(roomInfo.getRoomId() + "", 10) + "\t" + getStringByLength(roomInfo.getCommunity() + "", 10) + "\t" + getStringByLength(roomInfo.getUnitNum() + "", 10)
                    + "\t" + getStringByLength(roomInfo.getRoomNum() + "", 15) + "\t" + getStringByLength(roomInfo.getRoomArea() + "", 20) + "\t" + getStringByLength(roomInfo.getPricePerM2() + "", 13) + "\t" + getStringByLength(isSold, 18) + "\t" + getStringByLength(roomInfo.getHouseholdId() + "", 15));
            hBox.getChildren().addAll(text);
            if (i % 2 == 0) {
                hBox.setStyle("-fx-background-color: #E6E6FA;");
            } else {
                hBox.setStyle("-fx-background-color: #E1FFFF;");
            }
            vBoxContent.getChildren().add(hBox);
        }
        setText(arrayListRoomInfo.size(), count);
    }

    private String getStringByLength(String string, int length) {
        for (int i = string.length(); i < length; i++) {
            string += " ";
        }
        return string;
    }

    private void setText(int total, int free) {
        textAll.setText("总共" + total + "间房屋");
        textValid.setText((total - free) + "间已售");
        textInValid.setText(free + "间空闲");
    }
}

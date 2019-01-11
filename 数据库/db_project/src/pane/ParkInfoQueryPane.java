package pane;

import dao.ParkInfoQueryDao;
import entity.ParkInfoEntity;
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


public class ParkInfoQueryPane extends BorderPane {
    private VBox vBoxCenter = new VBox();
    private HBox hBoxButton = new HBox();
    private HBox hBoxText = new HBox();
    private VBox vBoxContent;
    private GridPane gridPane = new GridPane();
    private ScrollPane scrollPane = new ScrollPane();
    private Text textAll;
    private Text textTemp;
    private Text textRent;
    private Text textBuy;
    private ArrayList<ParkInfoEntity> arrayListParkInfo;
    private ParkInfoQueryDao parkInfoQueryDao = new ParkInfoQueryDao();

    public ParkInfoQueryPane() {
        Label labelText = new Label("车位信息查询");
        labelText.setFont(Font.font("BOLD", 30));
        setTop(labelText);
        setAlignment(labelText, Pos.TOP_CENTER);

        initHBox();
        initHBoxText();
        vBoxCenter.getChildren().addAll(hBoxButton, hBoxText, gridPane);
        vBoxCenter.setSpacing(20);
        Insets insets = new Insets(20, 0, 0, 0);
        setMargin(vBoxCenter, insets);
        setCenter(vBoxCenter);
    }

    private void initHBox() {
        RadioButton radioButtonTemp = new RadioButton("临时车位");
        radioButtonTemp.setUserData("临时车位");
        RadioButton radioButtonRent = new RadioButton("已租车位");
        radioButtonRent.setUserData("已租车位");
        RadioButton radioButtonBuy = new RadioButton("已购车位");
        radioButtonBuy.setUserData("已购车位");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(radioButtonBuy, radioButtonTemp, radioButtonRent);
        toggleGroup.selectToggle(radioButtonTemp);
        Button button = new Button("查询");
        button.setOnAction(event -> {
            initHBoxButton(toggleGroup.getSelectedToggle().getUserData().toString());
            setText(parkInfoQueryDao.getAmount(toggleGroup.getSelectedToggle().getUserData().toString()));
        });
        hBoxButton.getChildren().addAll(radioButtonTemp, radioButtonRent, radioButtonBuy, button);
        hBoxButton.setSpacing(20);
        hBoxButton.setAlignment(Pos.CENTER);
    }

    private void initHBoxText() {
        textAll = new Text("总共车位：");
        textTemp = new Text("临时车位：");
        textRent = new Text("已租车位：");
        textBuy = new Text("已购车位：");
        hBoxText.getChildren().addAll(textAll, textTemp, textRent, textBuy);
        hBoxText.setAlignment(Pos.CENTER);
        hBoxText.setSpacing(20);
    }

    private void initHBoxButton(String type) {
        vBoxCenter.getChildren().remove(scrollPane);
        scrollPane = new ScrollPane();
        vBoxCenter.getChildren().add(scrollPane);
        vBoxContent = new VBox();
        scrollPane.setContent(vBoxContent);
        vBoxContent.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Text textParkId;
        Text textCommunity;
        Text textParkNum;
        Text textParkType;
        Text textPriceRent;
        Text textPriceBuy;
        Text textRentStartTime;
        Text textRentEndTime;
        textParkId = new Text(getStringByLength("车位id", 10));
        textCommunity = new Text(getStringByLength("小区", 10));
        textParkNum = new Text(getStringByLength("小区车位号", 9));
        textParkType = new Text(getStringByLength("车位类型", 10));
        textPriceRent = new Text(getStringByLength("租用价格", 14));
        textPriceBuy = new Text(getStringByLength("购买价格", 10));
        textRentStartTime = new Text(getStringByLength("租用开始时间", 10));
        textRentEndTime = new Text(getStringByLength("租用结束时间", 10));
        hBox.getChildren().addAll(textParkId, textCommunity, textParkNum, textParkType, textPriceRent, textPriceBuy, textRentStartTime, textRentEndTime);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-background-color: #E1FFFF;");
        vBoxContent.getChildren().add(hBox);
        arrayListParkInfo = parkInfoQueryDao.query(type);
        for (int i = 0; i < arrayListParkInfo.size(); i++) {
            ParkInfoEntity parkInfoEntity = arrayListParkInfo.get(i);
            hBox = new HBox();
            String rentStartTime;
            String rentEndTime;
            if (parkInfoEntity.getRentStartTime() == null) {
                rentStartTime = " ";
            } else {
                rentStartTime = parkInfoEntity.getRentStartTime() + " ";
            }
            if (parkInfoEntity.getRentEndTime() == null) {
                rentEndTime = " ";
            } else {
                rentEndTime = parkInfoEntity.getRentEndTime() + " ";
            }
            Text text = new Text(getStringByLength(parkInfoEntity.getParkId() + "", 20) + "\t" + getStringByLength(parkInfoEntity.getCommunity(), 15) + "\t" + getStringByLength(parkInfoEntity.getParkNum() + "", 14) + "\t" + getStringByLength(parkInfoEntity.getParkType(), 15)
                    + "\t" + getStringByLength(parkInfoEntity.getPriceRent() + "", 14) + "\t" + getStringByLength(parkInfoEntity.getPriceBuy() + "", 10) + "\t" + getStringByLength(rentStartTime, 10) + "\t" + getStringByLength(rentEndTime, 10));
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

    private void setText(int[] nums) {
        textAll.setText("总共车位：" + (nums[0] + nums[1] + nums[2]));
        textTemp.setText("临时车位：" + nums[0]);
        textRent.setText("已租车位：" + nums[1]);
        textBuy.setText("已购车位：" + nums[2]);
    }

    private String getStringByLength(String string, int length) {
        for (int i = string.length(); i < length; i++) {
            string += " ";
        }
        return string;
    }
}

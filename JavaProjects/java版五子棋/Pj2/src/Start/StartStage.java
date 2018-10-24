package Start;

import Game.GameStage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class StartStage extends Application {
    //创建开始背景与开始按钮的对象，并创建必要的面板、组件。
    StartBackground startBackground = new StartBackground();
    StartStageButton startStageButton = new StartStageButton();
    private HBox hBoxAddStartButton = new HBox();
    private BorderPane borderPane = new BorderPane();
    private MediaPlayer mpStart = new MediaPlayer(new Media(StartStage.class.getResource("image/月光.mp3").toString()));

    //注册开始的Stage，并添加组件。
    public void start(Stage primarystage) {
        setBtContinue(primarystage);
        setBtStart(primarystage);
        //从startStageButton获得按钮并加入HBox
        hBoxAddStartButton.getChildren().addAll(startStageButton.getBtStart(), startStageButton.getBtContinue(), startStageButton.getBtExit());
        borderPane.setBottom(hBoxAddStartButton);
        //从startButton获得stackPane并加入
        borderPane.setCenter(startBackground.getStackPane());
        mpStart.play();
        primarystage.setScene(new Scene(borderPane, 790, 650));
        primarystage.setTitle("软工五子棋");
        primarystage.setResizable(false);
        primarystage.show();
    }

    //设置开始按钮事件
    private void setBtStart(Stage primarystage) {
        startStageButton.getBtStart().setOnAction(event -> {
            mpStart.pause();
            primarystage.close();
            GameStage gameStage = new GameStage();
        });
    }

    //设置继续按钮的事件
    private void setBtContinue(Stage primarystage) {
        startStageButton.getBtContinue().setOnAction(event -> {
            //将存档中的数据读入数组
            int[] record = new int[510];
            java.util.Arrays.fill(record, -1);
            int value;
            int total = 0;
            int totalSecond;
            try {
                totalSecond = new FileInputStream("Time.txt").read();
                FileInputStream fileInputStream = new FileInputStream("recordArray.txt");
                while ((value = fileInputStream.read()) != -1) {
                    record[total] = value;
                    total++;
                }
                mpStart.pause();
                primarystage.close();
                GameStage gameStage = new GameStage(record, totalSecond);
            } catch (IOException ex) {
                //对事件异常的处理，设置并弹出异常窗口
                //注册按钮、文本等
                Button btIOExceptionReturn = new Button("返回");
                btIOExceptionReturn.setPrefSize(80, 40);
                btIOExceptionReturn.setOpacity(0.4);
                btIOExceptionReturn.setFont(Font.font("返回", FontWeight.BOLD, FontPosture.ITALIC, 20));
                btIOExceptionReturn.setStyle("-fx-base:lightblue");
                Text textSorry = new Text("Oh,No");
                textSorry.setFont(Font.font("Oh,No", FontWeight.BOLD, FontPosture.ITALIC, 40));
                Text textFileNotFound = new Text("存档丢失");
                textFileNotFound.setFont(Font.font("存档丢失", FontWeight.BOLD, FontPosture.ITALIC, 40));
                //将组件并加入面板中
                GridPane gridPane = new GridPane();
                gridPane.add(textSorry, 0, 0);
                gridPane.add(textFileNotFound, 0, 1);
                BorderPane borderPane = new BorderPane();
                borderPane.setCenter(gridPane);
                gridPane.setAlignment(Pos.CENTER);
                HBox hBox = new HBox();
                hBox.getChildren().addAll(btIOExceptionReturn);
                borderPane.setBottom(hBox);
                hBox.setAlignment(Pos.BOTTOM_CENTER);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(new ImageView(new Image(getClass().getResource("image/古风6.jpeg").toString())), borderPane);
                Stage stageException = new Stage();
                stageException.setScene(new Scene(stackPane));
                stageException.show();
                btIOExceptionReturn.setOnAction(event1 -> {
                    stageException.close();
                });
            }
        });
    }
}
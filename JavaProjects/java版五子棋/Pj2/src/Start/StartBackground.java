package Start;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class StartBackground {
    private BorderPane borderPane = new BorderPane();
    private StackPane stackPane = new StackPane();
    private Label lbTile = new Label("          欢迎来到软工五子棋");
    private int num = 1;

    //创建第一张图片，并调用函数
    StartBackground() {
        borderPane.setCenter(new ImageView(new Image(getClass().getResource("image/古风0.jpg").toString())));
        callImage1();
    }

    //设置Timeline事件，通过不断循环更改图片
    private void callImage1() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4000), eventHandler1));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //动画事件处理
    EventHandler<ActionEvent> eventHandler1 = event -> {
        //更改图片并添加图片
        String s = "image/古风" + (num % 4) + ".jpg";
        ImageView imageView = new ImageView(new Image(getClass().getResource(s).toString()));
        borderPane.setCenter(imageView);
        //用FadeTransition，设置为周期4秒
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), imageView);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.play();
        num++;
    };

    //设置面板，将背景等加入面板，作为开始背景，并设置为public，方便StartStage获得该面板
    public StackPane getStackPane() {
        lbTile.setFont(Font.font("欢迎来到软工五子棋", FontWeight.BOLD, FontPosture.ITALIC, 50));
        lbTile.setPrefSize(800, 20);
        stackPane.getChildren().addAll(borderPane, lbTile);
        return stackPane;
    }
}

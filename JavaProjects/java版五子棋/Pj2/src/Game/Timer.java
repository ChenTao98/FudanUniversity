package Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Timer {

    private Text timeText = new Text("00:00:00");
    private int totalSecond = 0;
    private Timeline timeline;

    //初始化时间文本，并设置TimeLine事件
    private void setTimeText() {
        timeText.setFont(Font.font("00:00:00", FontWeight.BOLD, FontPosture.ITALIC, 20));
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //TimeLine事件处理
    EventHandler<ActionEvent> eventHandler = event -> {
        //将当前时间转化，并更新时间文本
        String stringHour = "", stringMinute = "", stringScond = "";
        int second = totalSecond;
        int hour = second / 3600;
        if (hour < 10) {
            stringHour = "0" + hour;
        } else {
            stringHour += hour;
        }
        second %= 3600;
        int minute = second / 60;
        if (minute < 10) {
            stringMinute = "0" + minute;
        } else {
            stringMinute += minute;
        }
        second %= 60;
        if (second < 10) {
            stringScond = "0" + second;
        } else {
            stringScond += second;
        }
        timeText.setText(stringHour + ":" + stringMinute + ":" + stringScond);
        timeText.setFont(Font.font(stringHour + ":" + stringMinute + ":" + stringScond, FontWeight.BOLD, FontPosture.ITALIC, 20));
        totalSecond++;
    };

    //组件的获取与设置函数
    public Text getTimeText() {
        setTimeText();
        return timeText;
    }

    public void timePause() {
        timeline.stop();
    }

    public void timePlay() {
        timeline.play();
    }

    public void time() {
        totalSecond = 0;
    }

    public void setTotalSecond(int totalSecond) {
        this.totalSecond = totalSecond;
    }

    public int getTotalSecond() {
        return totalSecond;
    }
}

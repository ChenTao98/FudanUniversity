package Start;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class StartStageButton {
    private Button btExit = new Button("退出");
    private Button btStart = new Button("新游戏");
    private Button btContinue = new Button("继续");

    //设置三个按钮样式，并为public，方便StartStage获得
    public Button getBtExit() {
        btExit.setOnAction(event -> System.exit(0));
        btExit.setFont(Font.font("退出", FontWeight.BOLD, FontPosture.ITALIC, 30));
        btExit.setPrefSize(270, 60);
        btExit.setStyle("-fx-base:lightblue");
        return btExit;
    }

    public Button getBtStart() {
        btStart.setPrefSize(266, 60);
        btStart.setFont(Font.font("新游戏", FontWeight.BOLD, FontPosture.ITALIC, 30));
        btStart.setStyle("-fx-base:lightblue");
        return btStart;
    }

    public Button getBtContinue() {
        btContinue.setPrefSize(266, 60);
        btContinue.setFont(Font.font("继续", FontWeight.BOLD, FontPosture.ITALIC, 30));
        btContinue.setStyle("-fx-base:lightblue");
        return btContinue;
    }
}

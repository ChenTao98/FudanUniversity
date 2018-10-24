package Game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutGobang extends Application {
    private ImageView imageView = new ImageView(new Image(getClass().getResource("image/about.jpeg").toString()));
    private Button btAbout = new Button("关于");
    private Text textAbout = new Text("关于五子棋");
    private Text textAuthor = new Text("作者：陈涛");
    private Text textEdition = new Text("版本：98版");
    private GridPane gridPane = new GridPane();
    private StackPane stackPane = new StackPane();
    private Button btReturn = new Button("返回");

    //设置关于界面
    public void start(Stage stageAbout) {
        setText();
        //将文本与按钮添加入gridPane
        gridPane.getChildren().clear();
        gridPane.setColumnSpan(textAuthor, 4);
        gridPane.setColumnSpan(textAbout, 4);
        gridPane.add(textAbout, 0, 0);
        gridPane.setColumnSpan(textEdition, 4);
        gridPane.add(textAuthor, 0, 1);
        gridPane.add(textEdition, 0, 2);
        gridPane.add(btReturn, 2, 4);
        //将图片与gridPane加入stackPane中，并加入stageAbout
        stackPane.getChildren().clear();
        stackPane = new StackPane();
        stackPane.getChildren().add(imageView);
        stackPane.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        stageAbout.setScene(new Scene(stackPane));
        stageAbout.setResizable(false);
        stageAbout.show();
    }

    //对文本进行样式设置
    private void setText() {
        textAbout.setFont(Font.font("关于五子棋", FontWeight.BOLD, FontPosture.ITALIC, 40));
        textAuthor.setFont(Font.font("作者：陈涛", FontWeight.BOLD, FontPosture.ITALIC, 40));
        textEdition.setFont(Font.font("版本：98版", FontWeight.BOLD, FontPosture.ITALIC, 40));
        btReturn.setFont(Font.font("返回", FontWeight.BOLD, FontPosture.ITALIC, 20));
        btReturn.setOpacity(0.3);
    }

    public Button getBtAbout() {
        return btAbout;
    }

    public Button getBtReturn() {
        return btReturn;
    }
}

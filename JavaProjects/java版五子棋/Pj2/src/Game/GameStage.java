package Game;

import Start.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;

public class GameStage extends Application {
    GameMenu gameMenu = new GameMenu();
    Stage stageAbout = new Stage();
    Stage stageAboutCopy = new Stage();
    Chessboard chessboard = new Chessboard();
    GameMedia gameMedia = new GameMedia();
    Timer timer = new Timer();
    AboutGobang aboutGobang = new AboutGobang();
    GridPane gridPane = new GridPane();
    GameStyle gameStyle = new GameStyle();
    static Stage primarystage;
    private HBox hBox1 = new HBox();
    private HBox hBox2 = new HBox();
    private BorderPane borderPane = new BorderPane();

    //构造函数，新游戏按钮调用
    public GameStage() {
        primarystage = new Stage();
        timer.time();
        start(primarystage);
    }

    //构造函数，继续按钮调用
    public GameStage(int[] record, int totalSecond) {
        this.primarystage = new Stage();
        timer.time();
        //设置存档时的时间
        timer.setTotalSecond(totalSecond);
        start(primarystage);
        setChessboard(record);
    }

    //构建游戏界面的Stage，并将对应组件加入
    public void start(Stage primaryStage) {
        setStyleOne();
        setStyleTwo();
        setBtAbout();
        setBtReturn();
        setItNewGame();
        setItSave();
        //将菜单、音乐、风格加入面板
        hBox1.getChildren().addAll(gameMenu.getMenu(), gameMedia.getMenuBar(), gameStyle.getMenuBar(), aboutGobang.getBtAbout());
        hBox1.setSpacing(40);
        hBox2.getChildren().addAll(timer.getTimeText(), chessboard.getTextBlack(), chessboard.getTextWhite());
        hBox2.setSpacing(40);
        gridPane.add(hBox1, 0, 1);
        gridPane.add(hBox2, 0, 2);
        hBox2.setAlignment(Pos.CENTER);
        borderPane.setTop(gridPane);
        borderPane.setCenter(chessboard.getStackPane());
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //设置关于按钮的事件
    private void setBtAbout() {
        aboutGobang.getBtAbout().setOnAction(event -> {
            timer.timePause();
            aboutGobang.start(stageAbout);
            stageAboutCopy = stageAbout;
            stageAbout = new Stage();
        });
    }

    //设置返回按钮的事件
    private void setBtReturn() {
        aboutGobang.getBtReturn().setOnAction(event -> {
            stageAboutCopy.close();
            timer.timePlay();
        });
    }

    //设置重新开始的事件
    private void setItNewGame() {
        gameMenu.getItNewGame().setOnAction(event -> {
            timer.time();
            timer.timePause();
            //将面包板中的组件清空
            hBox2.getChildren().clear();
            borderPane.getChildren().removeAll(chessboard.getStackPane());
            //重新创建组件并加入面板
            chessboard = new Chessboard();
            timer = new Timer();
            hBox2.getChildren().addAll(timer.getTimeText(), chessboard.getTextBlack(), chessboard.getTextWhite());
            gameMenu.getItNewGame().setSelected(false);
            borderPane.setCenter(chessboard.getStackPane());
        });
    }

    //设置风格1
    private void setStyleOne() {
        gameStyle.getStyleOne().setOnAction(event -> {
            //清空面板
            chessboard.getStackPane().getChildren().clear();
            //更换组件，做到风格更换
            chessboard.getStackPane().setPrefSize(700, 700);
            chessboard.getStackPane().getChildren().add(new ImageView(new Image(getClass().getResource("image/古风背景.jpeg").toString())));
            chessboard.getStackPane().getChildren().add(chessboard.getPane());
            chessboard.getStackPane().getChildren().add(chessboard.getGridPane());
            chessboard.getGridPane().setAlignment(Pos.CENTER);
            //更换音乐菜单
            RadioMenuItem[] menuItems = gameMedia.getMuneItems();
            menuItems[0].setText("步步为营");
            menuItems[1].setText("天行九歌");
            menuItems[2].setText("上邪");
            menuItems[3].setText("以剑之名");
        });
    }

    //设置风格2
    private void setStyleTwo() {
        gameStyle.getStyleTwo().setOnAction(event -> {
            //清空面板
            chessboard.getStackPane().getChildren().clear();
            //更换组件，做到风格更换
            chessboard.getStackPane().setPrefSize(700, 700);
            chessboard.getStackPane().getChildren().add(new ImageView(new Image(getClass().getResource("image/动漫背景.jpg").toString())));
            chessboard.getStackPane().getChildren().add(chessboard.getPane());
            chessboard.getStackPane().getChildren().add(chessboard.getGridPane());
            chessboard.getGridPane().setAlignment(Pos.CENTER);
            //更换音乐菜单
            RadioMenuItem[] menuItems = gameMedia.getMuneItems();
            menuItems[0].setText("ButterFly");
            menuItems[1].setText("花舞う街で~");
            menuItems[2].setText("月光の雲海");
            menuItems[3].setText("千与千寻");
        });
    }

    //设置存档按钮事件
    private void setItSave() {
        //设置快捷键
        gameMenu.getItSave().setAccelerator(KeyCombination.valueOf("S"));
        gameMenu.getItSave().setOnAction(event -> {
            try {
                gameMenu.getItSave().setSelected(false);
                int[][] recordArray = chessboard.getRecordArray();
                int totalStep = chessboard.getTotalStep();
                //将下棋的记录数组与当前时间存到两个txt中
                FileOutputStream outputStream = new FileOutputStream("recordArray.txt");
                FileOutputStream outputTime = new FileOutputStream("Time.txt");
                for (int i = 0; i < totalStep; i++) {
                    for (int j = 0; j < 2; j++) {
                        outputStream.write(recordArray[i][j]);
                    }
                }
                outputTime.write(timer.getTotalSecond());
            } catch (IOException ex) {
            }
        });
    }

    //设置读档时棋盘恢复
    private void setChessboard(int[] record) {
        //创建数组等，并指向Chessboard中创建的数组，以更改其元素
        ImageView[][] imageViews = chessboard.getImageViews();
        GridPane gridPaneOfChessboard = chessboard.getGridPane();
        Button[][] btArray = chessboard.getBtArray();
        int[][] recordArray = chessboard.getRecordArray();
        int[][] blackRecord = chessboard.getBlackRecord();
        int[][] whiteRecord = chessboard.getWhiteRecord();
        int totalStep = 0, blackStep = 0, whiteStep = 0;
        int index = 0;
        //将读出的数据转化为记录数组
        for (int i = 0; record[index] != -1; i++) {
            for (int j = 0; j < 2; j++) {
                if (totalStep % 2 == 0) {
                    recordArray[totalStep][j] = blackRecord[totalStep / 2][j] = record[index];
                    index++;
                } else {
                    recordArray[totalStep][j] = whiteRecord[(totalStep - 1) / 2][j] = record[index];
                    index++;
                }
            }
            totalStep++;
        }
        //恢复存档时黑白棋的步数
        if (totalStep % 2 == 0) {
            blackStep = totalStep / 2;
            whiteStep = totalStep / 2;
        } else {
            blackStep = (totalStep + 1) / 2;
            whiteStep = totalStep / 2;
        }
        //通过读取记录数组，将对应位置的按钮改为图片
        for (int i = 0; i < totalStep; i++) {
            int numberX = recordArray[i][1];
            int numberY = recordArray[i][0];
            if (i % 2 == 0) {
                imageViews[numberX][numberY] = new ImageView(new Image(getClass().getResource("image/黑.png").toString()));
            } else {
                imageViews[numberX][numberY] = new ImageView(new Image(getClass().getResource("image/白.png").toString()));
            }
            gridPaneOfChessboard.getChildren().remove(btArray[numberX][numberY]);
            gridPaneOfChessboard.add(imageViews[numberX][numberY], numberX, numberY);
        }
        chessboard.setStep(totalStep, blackStep, whiteStep);
        chessboard.setBothText(blackStep, whiteStep);
    }

    public static Stage getStage() {
        return primarystage;
    }
}

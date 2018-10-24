package Game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Chessboard {
    private Button[][] btArray = new Button[15][15];
    //创建放置按钮的面板
    private GridPane gridPaneOfButton = new GridPane();
    //创建pane，用于存储棋盘的Line
    private Pane pane = new Pane();
    private StackPane stackPaneOfChessboard = new StackPane();
    private ImageView imageViewOfBackground = new ImageView(getClass().getResource("image/古风背景.jpeg").toString());
    //用于记录步数
    private int totalStep = 0;
    private int blackStep = 0, whiteStep = 0;
    //用于记录棋子位置的数组
    private int[][] recordArray = new int[225][2];
    private int[][] blackRecord = new int[225][2];
    private int[][] whiteRecord = new int[225][2];
    //创建imageView数组，用于存储下棋过程的棋子图片
    private ImageView[][] imageViewsChessman = new ImageView[15][15];
    private Text textBlackStep = new Text("黑方： 0 步");
    private Text textWhiteStep = new Text("白方： 0 步");

    //在构造方法中调用函数，设置必要组件的样式、事件；
    Chessboard() {
        setItReturn();
        setBtArray();
        setGridPane();
        setPane();
        setStackPane(imageViewOfBackground);
    }

    //初始化棋盘按钮数组，并设置事件
    private void setBtArray() {
        //初始化黑白两方步数文本
        setFonts(textBlackStep);
        setFonts(textWhiteStep);
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                //初始化按钮数组
                int numberX = x, numberY = y;
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setOpacity(0);
                //设置按钮事件
                button.setOnAction(event -> {
                    if (totalStep % 2 == 0) {
                        //黑方回合，新建黑棋图片，存入图片数组
                        imageViewsChessman[numberX][numberY] = new ImageView(new Image(getClass().getResource("image/黑.png").toString()));
                        //将棋子位置记录数组
                        recordArray[totalStep][0] = blackRecord[blackStep][0] = numberY;
                        recordArray[totalStep][1] = blackRecord[blackStep][1] = numberX;
                        //刷新步数，并进行胜负判断
                        blackStep++;
                        textBlackStep.setText("黑方  " + blackStep + " 步");
                        setFonts(textBlackStep);
                        WinOrLose winOrLose = new WinOrLose(blackRecord, whiteRecord, blackStep, whiteStep, totalStep);
                    } else {
                        //白方回合，新建白棋图片，存入图片数组
                        imageViewsChessman[numberX][numberY] = new ImageView(new Image(getClass().getResource("image/白.png").toString()));
                        //将棋子位置记录数组
                        recordArray[totalStep][0] = whiteRecord[whiteStep][0] = numberY;
                        recordArray[totalStep][1] = whiteRecord[whiteStep][1] = numberX;
                        //刷新步数，并进行胜负判断
                        whiteStep++;
                        textWhiteStep.setText("白方  " + whiteStep + " 步");
                        setFonts(textWhiteStep);
                        WinOrLose winOrLose = new WinOrLose(blackRecord, whiteRecord, blackStep, whiteStep, totalStep);
                    }
                    totalStep++;
                    //将griPane 中的按钮移除，添加棋子图片，达到下棋
                    gridPaneOfButton.getChildren().remove(btArray[numberX][numberY]);
                    gridPaneOfButton.add(imageViewsChessman[numberX][numberY], numberX, numberY);
                });
                btArray[x][y] = button;
            }
        }
    }

    //初始化gridPaneOfButton，将按钮加入
    private void setGridPane() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                gridPaneOfButton.add(btArray[x][y], x, y);
            }
        }
        for (int i = 0; i < 225; i++) {
            for (int j = 0; j < 2; j++) {
                blackRecord[i][j] = -1;
                whiteRecord[i][j] = -1;
                recordArray[i][j] = -1;
            }
        }
    }

    //初始化pane，使用Line画出棋盘
    private void setPane() {
        for (int x = 70; x <= 630; x += 40) {
            Line line = new Line(x, 70, x, 630);
            pane.getChildren().add(line);
        }
        for (int y = 70; y <= 630; y += 40) {
            Line line = new Line(70, y, 630, y);
            pane.getChildren().add(line);
        }
    }

    //初始化棋盘stackPane，将背景图片、棋盘、按钮面板加入
    private void setStackPane(ImageView imageView) {
        stackPaneOfChessboard.setPrefSize(700, 700);
        stackPaneOfChessboard.getChildren().add(imageView);
        stackPaneOfChessboard.getChildren().add(pane);
        stackPaneOfChessboard.getChildren().add(gridPaneOfButton);
        gridPaneOfButton.setAlignment(Pos.CENTER);
    }

    //stackPane的获取函数
    public StackPane getStackPane() {
        return stackPaneOfChessboard;
    }

    //设置悔棋按钮事件
    private void setItReturn() {
        //设置悔棋按钮快捷键
        GameMenu.getItReturn().setAccelerator(KeyCombination.valueOf("R"));
        //设置悔棋按钮事件
        GameMenu.getItReturn().setOnAction(event -> {
            //进行步数判断
            if (totalStep > 0) {
                //获取上一步棋子坐标
                int numberX = recordArray[totalStep - 1][1];
                int numberY = recordArray[totalStep - 1][0];
                //移除上一步所下的棋子，并将按钮重新加入面板
                gridPaneOfButton.getChildren().remove(imageViewsChessman[numberX][numberY]);
                gridPaneOfButton.add(btArray[numberX][numberY], numberX, numberY);
                totalStep--;
                //刷新步数
                if (totalStep % 2 == 0) {
                    blackStep--;
                    textBlackStep.setText("黑方  " + blackStep + " 步");
                    setFonts(textBlackStep);

                } else {
                    whiteStep--;
                    textWhiteStep.setText("白方  " + whiteStep + " 步");
                    setFonts(textWhiteStep);
                }
                GameMenu.getItReturn().setSelected(false);
            } else
                GameMenu.getItReturn().setSelected(false);
        });
    }

    //各个组件的获取函数
    public Text getTextBlack() {
        return textBlackStep;
    }

    public Text getTextWhite() {
        return textWhiteStep;
    }

    public Pane getPane() {
        return pane;
    }

    public GridPane getGridPane() {
        return gridPaneOfButton;
    }

    public int[][] getRecordArray() {
        return recordArray;
    }

    public int[][] getBlackRecord() {
        return blackRecord;
    }

    public int[][] getWhiteRecord() {
        return whiteRecord;
    }

    public int getTotalStep() {
        return totalStep;
    }

    public ImageView[][] getImageViews() {
        return imageViewsChessman;
    }

    public Button[][] getBtArray() {
        return btArray;
    }

    //读取存档时设置步数
    public void setStep(int totalStep, int blackStep, int whiteStep) {
        this.totalStep = totalStep;
        this.blackStep = blackStep;
        this.whiteStep = whiteStep;
    }

    //读取存档时设置显示步数
    public void setBothText(int blackStep, int whiteStep) {
        textBlackStep.setText("黑方  " + blackStep + " 步");
        setFonts(textBlackStep);
        textWhiteStep.setText("白方  " + whiteStep + " 步");
        setFonts(textWhiteStep);
    }

    //设置文本样式
    private void setFonts(Text text) {
        text.setFont(Font.font(text.getText(), FontWeight.BOLD, FontPosture.ITALIC, 20));
    }
}
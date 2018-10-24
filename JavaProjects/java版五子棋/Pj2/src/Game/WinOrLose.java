package Game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WinOrLose {
    private ImageView imageView = new ImageView(new Image(getClass().getResource("image/result2.jpg").toString()));
    private Stage stage = new Stage();
    private Text textResult = new Text();
    private Text textIsNewGame = new Text("是否开始新游戏");
    private Button btNewGame = new Button("新游戏");
    private Button btExit = new Button("退出");
    private GridPane gridPane = new GridPane();
    private BorderPane borderPane = new BorderPane();
    private StackPane stackPane = new StackPane();
    private HBox hBox1 = new HBox();
    private HBox hBox2 = new HBox();

    //从Chessboard获得数据，并调用函数进行胜负判断
    WinOrLose(int[][] blackRecord, int[][] whiteRecord, int blackStep, int whiteStepe, int totalStep) {
        int number0 = 0, number1 = 1;
        judgeRowOrColumn(blackRecord, blackStep, number0, number1, totalStep);
        judgeRowOrColumn(blackRecord, blackStep, number1, number0, totalStep);
        judgeRowOrColumn(whiteRecord, whiteStepe, number0, number1, totalStep);
        judgeRowOrColumn(whiteRecord, whiteStepe, number1, number0, totalStep);
        judgeSlashOne(blackRecord, blackStep, number0, number1, totalStep);
        judgeSlashOne(whiteRecord, whiteStepe, number0, number1, totalStep);
        judgeSlashTow(blackRecord, blackStep, number0, number1, totalStep);
        judgeSlashTow(whiteRecord, whiteStepe, number0, number1, totalStep);
    }

    //判断行与列的胜负
    private void judgeRowOrColumn(int[][] recordArray, int step, int number0, int number1, int totalStep) {
        //定义数组，对每行或者每列上具有相同颜色棋子的个数进行统计
        int[] rowArray = new int[15];
        for (int i = 0; i < 15; i++)
            rowArray[i] = i;
        int[] countArray = new int[15];
        java.util.Arrays.fill(countArray, 0);
        for (int j = 0; j < step; j++) {
            for (int k = 0; k < 15; k++) {
                if (recordArray[j][number0] == rowArray[k])
                    countArray[k]++;
            }
        }
        for (int m = 0; m < 15; m++) {
            //若有某一行或一列相同颜色棋子不少于5个，则进入if语句判断
            if (countArray[m] >= 5) {
                //定义新数组，将此行（列）的相同颜色的棋子的对应列（行）坐标记录并排序
                int[] index = new int[225];
                java.util.Arrays.fill(index, 15);
                for (int j = 0, k = 0; j < step; j++) {
                    if (recordArray[j][number0] == rowArray[m]) {
                        index[k] = recordArray[j][number1];
                        k++;
                    }
                }
                java.util.Arrays.sort(index);
                boolean result = false;
                //对记录的坐标进行判定，若有五个棋子的列（行）坐标连续，则出现胜负
                for (int k = 4; k < countArray[m]; k++) {
                    if (index[k - 4] == (index[k] - 4)) {
                        result = true;
                        break;
                    } else
                        result = false;
                }
                //出现胜负，调用结果显示函数，显示Stage
                if (result && totalStep % 2 == 0) {
                    showResult("黑方获胜");
                } else if (result && totalStep % 2 == 1) {
                    showResult("白方获胜");
                }
            }
        }
    }

    //判断斜线胜负（平行于主对角线）
    private void judgeSlashOne(int[][] recordArray, int step, int number0, int number1, int totalStep) {
        //定义数组，对每条斜线上具有相同颜色棋子的个数进行统计
        int[] slashArray = new int[29];
        for (int j = 0; j < 29; j++) {
            slashArray[j] = j;
        }
        int[] count = new int[29];
        java.util.Arrays.fill(count, 0);
        for (int j = 0; j < step; j++) {
            for (int k = 0; k < 29; k++) {
                if (recordArray[j][number1] - recordArray[j][number0] + 14 == slashArray[k])
                    count[k]++;
            }
        }
        for (int m = 0; m < 29; m++) {
            //若有某一条斜线相同颜色棋子不少于5个，则进入if语句判断
            if (count[m] >= 5) {
                //定义新数组，将此斜线的相同颜色的棋子的对应行坐标记录并排序
                int[] index = new int[225];
                java.util.Arrays.fill(index, 15);
                for (int j = 0, k = 0; j < step; j++) {
                    if (recordArray[j][number1] - recordArray[j][number0] + 14 == slashArray[m]) {
                        index[k] = recordArray[j][0];
                        k++;
                    }
                }
                java.util.Arrays.sort(index);
                boolean result = false;
                //对记录的坐标进行判定，若有五个棋子的行坐标连续，则出现胜负
                for (int k = 4; k < count[m]; k++) {
                    if (index[k - 4] == (index[k] - 4)) {
                        result = true;
                        break;
                    } else
                        result = false;
                }
                //出现胜负，调用结果显示函数，显示Stage
                if (result && totalStep % 2 == 0) {
                    showResult("黑方获胜");
                } else if (result && totalStep % 2 == 1) {
                    showResult("白方获胜");
                }
            }
        }
    }

    //判断斜线胜负（平行于副对角线）
    private void judgeSlashTow(int[][] recordArray, int step, int number0, int number1, int totalStep) {
        //定义数组，对每条斜线上具有相同颜色棋子的个数进行统计
        int[] slashArray = new int[29];
        for (int j = 0; j < 29; j++) {
            slashArray[j] = j + 2;
        }
        int[] countArray = new int[29];
        java.util.Arrays.fill(countArray, 0);
        for (int j = 0; j < step; j++) {
            for (int k = 0; k < 29; k++) {
                if (recordArray[j][number1] + recordArray[j][number0] == slashArray[k])
                    countArray[k]++;
            }
        }
        for (int m = 0; m < 29; m++) {
            //若有某一条斜线相同颜色棋子不少于5个，则进入if语句判断
            if (countArray[m] >= 5) {
                //定义新数组，将此斜线的相同颜色的棋子的对应行坐标记录并排序
                int[] zuobiao = new int[225];
                java.util.Arrays.fill(zuobiao, 15);
                for (int j = 0, k = 0; j < step; j++) {
                    if (recordArray[j][number1] + recordArray[j][number0] == slashArray[m]) {
                        zuobiao[k] = recordArray[j][0];
                        k++;
                    }
                }
                java.util.Arrays.sort(zuobiao);
                boolean result = false;
                //对记录的坐标进行判定，若有五个棋子的行坐标连续，则出现胜负
                for (int k = 4; k < countArray[m]; k++) {
                    if (zuobiao[k - 4] == (zuobiao[k] - 4)) {
                        result = true;
                        break;
                    } else
                        result = false;
                }
                //出现胜负，调用结果显示函数，显示Stage
                if (result && totalStep % 2 == 0) {
                    showResult("黑方获胜");
                } else if (result && totalStep % 2 == 1) {
                    showResult("白方获胜");
                }
            }
        }
    }

    //结果显示函数，构建并显示Stage
    private void showResult(String result) {
        //设置结果界面新游戏及退出按钮事件
        btExit.setOnAction(event -> {
            System.exit(0);
        });
        btNewGame.setOnAction(event -> {
            GameStage.getStage().close();
            stage.close();
            GameStage gameStage = new GameStage();
        });
        btExit.setPrefSize(100, 40);
        btExit.setOpacity(0.5);
        btNewGame.setPrefSize(100, 40);
        btNewGame.setOpacity(0.5);
        textResult.setText(result);
        textResult.setFont(Font.font(result, FontWeight.BOLD, FontPosture.ITALIC, 40));
        textIsNewGame.setFont(Font.font(textIsNewGame.getText(), FontWeight.BOLD, FontPosture.ITALIC, 40));
        gridPane.add(textResult, 0, 0);
        gridPane.add(textIsNewGame, 0, 1);
        borderPane.setTop(gridPane);
        gridPane.setAlignment(Pos.TOP_CENTER);
        hBox1.getChildren().addAll(btNewGame);
        borderPane.setCenter(hBox1);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.getChildren().add(btExit);
        borderPane.setBottom(hBox2);
        hBox2.setAlignment(Pos.BOTTOM_CENTER);
        stackPane.getChildren().addAll(imageView, borderPane);
        stage.setScene(new Scene(stackPane));
        stage.show();
    }
}
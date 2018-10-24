import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    // 启动文件，启动程序
    private Pane pane = new Pane();
    private Scene scene;
    Compress compress=new Compress();
    Decompress decompress=new Decompress();

    public void start(Stage myStage) {
        pane.getChildren().clear();
//        创建不同的按钮，并为按钮添加事件
        Button btCompress = new Button("压缩文件");
        Button btCompressDirectory = new Button("压缩文件夹");
        Button btDecompress = new Button("解压文件");
        Button btExit = new Button("退出");
        btCompress.setLayoutX(0);
        btCompress.setLayoutY(0);
        btCompressDirectory.setLayoutX(0);
        btCompressDirectory.setLayoutY(40);
        btDecompress.setLayoutX(0);
        btDecompress.setLayoutY(80);
        btExit.setLayoutX(0);
        btExit.setLayoutY(120);
        pane.getChildren().addAll(btCompress, btCompressDirectory, btDecompress, btExit);
//        压缩单个文件
        btCompress.setOnMouseClicked(e -> {
            pane.getChildren().clear();
            pane.getChildren().addAll(btCompress, btCompressDirectory, btDecompress, btExit);
            long time = 0;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose compression");
            File file = fileChooser.showOpenDialog(myStage);
            if (file != null) {
                try {
                    time = compress.compressMain(file);
                } catch (IOException e1) {
                }
            }
            double totalTime=time/1000.0;
            Label lblTime = new Label("压缩时间： " + totalTime + "s");
            Label lbRate;
            if(compress.getFileInitialLength()==0){
                lbRate=new Label("压缩率： 100%");
            }else{
                double compressRate=compress.getFileCompressLength()*1.0/compress.getFileInitialLength();
                compressRate=(int)(compressRate*10000)/100.0;
                lbRate=new Label("压缩率： "+compressRate+"%");
            }
            Button btOK = new Button("确定");
            btOK.setTextFill(Color.rgb(0,255,0));
            btOK.setLayoutX(120);
            btOK.setLayoutY(40);
            lblTime.setLayoutX(0);
            lblTime.setLayoutY(0);
            lbRate.setLayoutX(0);
            lbRate.setLayoutY(20);
            Pane pane1 = new Pane(lblTime,lbRate, btOK);
            pane1.setLayoutX(120);
            pane1.setLayoutY(30);
            pane.getChildren().add(pane1);

            btOK.setOnMouseClicked(e1 -> {
                pane.getChildren().remove(pane1);
            });
        });
//        压缩文件夹
        btCompressDirectory.setOnMouseClicked(e -> {
            pane.getChildren().clear();
            pane.getChildren().addAll(btCompress, btCompressDirectory, btDecompress, btExit);
            long time = 0;
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose compression");
            File file = directoryChooser.showDialog(myStage);
            if (file != null) {
                try {
                    time = compress.compressMain(file);
                } catch (IOException e1) {
                }
            }
            double totalTime=time/1000.0;
            Label lblTime = new Label("压缩时间： " + totalTime + " s");
            Label lbRate;
            if(compress.getFileInitialLength()==0){
                lbRate=new Label("压缩率： 100%");
            }else{
                double compressRate=compress.getFileCompressLength()*1.0/compress.getFileInitialLength();
                compressRate=(int)(compressRate*10000)/100.0;
                lbRate=new Label("压缩率： "+compressRate+"%");
            }
            Button btOK = new Button("确定");
            btOK.setLayoutX(120);
            btOK.setLayoutY(40);
            lblTime.setLayoutX(0);
            lblTime.setLayoutY(0);
            lbRate.setLayoutX(0);
            lbRate.setLayoutY(20);
            Pane pane1 = new Pane(lblTime,lbRate, btOK);
            pane1.setLayoutX(120);
            pane1.setLayoutY(30);
            pane.getChildren().add(pane1);

            btOK.setOnMouseClicked(e1 -> {
                pane.getChildren().remove(pane1);
            });
        });
//        解压文件
        btDecompress.setOnMouseClicked(e -> {
            pane.getChildren().clear();
            pane.getChildren().addAll(btCompress, btCompressDirectory, btDecompress, btExit);
            long time = 0;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Decompression");
            File file = fileChooser.showOpenDialog(myStage);
            if ((file != null) && (file.getName().endsWith("huff"))) {
                try {
                    time = decompress.decompressMain(file);
                } catch (IOException e1) {
                }
                double totalTime=time/1000.0;
                Label lblTime = new Label("解压时间： " + totalTime + " s");
                Button btOK = new Button("确定");
                btOK.setLayoutX(120);
                btOK.setLayoutY(40);
                Pane pane1 = new Pane(lblTime, btOK);
                pane1.setLayoutX(120);
                pane1.setLayoutY(30);
                pane.getChildren().add(pane1);

                btOK.setOnMouseClicked(e1 -> {
                    pane.getChildren().remove(pane1);
                });
            } else {
                Label lblTime = new Label("请选择后缀名为huff的文件进行解压");
                lblTime.setWrapText(true);
                lblTime.setMaxWidth(150);
                Button btOK = new Button("确定");
                btOK.setLayoutX(120);
                btOK.setLayoutY(40);
                Pane pane1 = new Pane(lblTime, btOK);
                pane1.setLayoutX(100);
                pane1.setLayoutY(30);
                pane.getChildren().add(pane1);

                btOK.setOnMouseClicked(e1 -> {
                    pane.getChildren().remove(pane1);
                });
            }

        });
//        退出
        btExit.setOnMouseClicked(event -> {
            System.exit(0);
        });
//        设置scene大小
        scene = new Scene(pane, 300, 140);
        myStage.setTitle("compress program");
        myStage.setResizable(false);
        myStage.setScene(scene);
        myStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

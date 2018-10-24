package Game;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;

public class GameMedia {
    private Menu mediaMenu = new Menu("音乐");
    private RadioMenuItem[] menuItems = new RadioMenuItem[5];
    private String[] mediaName = {"步步为营", "天行九歌", "上邪", "以剑之名"};
    private MenuBar menuBar = new MenuBar();
    private ToggleGroup toggleGroup = new ToggleGroup();
    private MediaPlayer[] mediaPlayer = new MediaPlayer[4];

    //构造函数，并调用函数设置组件
    GameMedia() {
        setMenuItems();
        setMediaMenu();
        setMenuBar();
        setToggleGroup();
    }

    //设置音乐菜单中的按钮
    private void setMenuItems() {
        menuItems[4] = new RadioMenuItem("音乐关");
        for (int i = 0; i < 4; i++) {
            String s = "media/" + mediaName[i] + ".mp3";
            menuItems[i] = new RadioMenuItem(mediaName[i]);
            mediaPlayer[i] = new MediaPlayer(new Media(GameMedia.class.getResource(s).toString()));
            int number = i;
            //设置菜单事件
            menuItems[i].setOnAction(event -> {
                stopMediaplayer();
                mediaPlayer[number] = new MediaPlayer(new Media(GameMedia.class.getResource("media/" + menuItems[number].getText() + ".mp3").toString()));
                mediaPlayer[number].play();
                mediaPlayer[number].setCycleCount(23333);
                if (menuItems[4].getText().equals("音乐开")) {
                    menuItems[4].setText("音乐关");
                }
            });
        }
        //设置开关事件
        menuItems[4].setOnAction(event -> {
            for (int i = 0; i < 4; i++) {
                //对于音乐菜单是否被选中等判断，并设置相应事件
                if (menuItems[i].isSelected() && menuItems[4].getText().equals("音乐关")) {
                    mediaPlayer[i].pause();
                    menuItems[4].setText("音乐开");
                    menuItems[4].setSelected(false);
                    break;
                } else if (menuItems[i].isSelected() && menuItems[4].getText().equals("音乐开")) {
                    mediaPlayer[i].play();
                    menuItems[4].setText("音乐关");
                    menuItems[4].setSelected(false);
                    break;
                } else if (menuItems[4].getText().equals("音乐关")) {
                    menuItems[4].setSelected(false);
                }
            }
        });
    }

    //将各个Item加入菜单中
    private void setMediaMenu() {
        mediaMenu.getItems().addAll(menuItems[0], menuItems[1], menuItems[2], menuItems[3], menuItems[4]);
    }

    //设置MenuBar
    private void setMenuBar() {
        menuBar.getMenus().add(mediaMenu);
    }

    //将菜单设置为单选
    private void setToggleGroup() {
        for (int i = 0; i < 4; i++) {
            menuItems[i].setToggleGroup(toggleGroup);
        }
    }

    //获取菜单
    public MenuBar getMenuBar() {
        return menuBar;
    }

    //获取菜单中的Item数组
    public RadioMenuItem[] getMuneItems() {
        return menuItems;
    }

    //停止音乐播放
    public void stopMediaplayer() {
        for (int j = 0; j < 4; j++) {
            mediaPlayer[j].pause();
            mediaPlayer[j].seek(Duration.ZERO);
        }
    }
}

package Game;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.KeyCombination;

public class GameMenu {
    private Menu menu = new Menu("菜单");
    private RadioMenuItem itNewGame = new RadioMenuItem("重新开始");
    private static RadioMenuItem itReturn = new RadioMenuItem("悔棋");
    private RadioMenuItem itSave = new RadioMenuItem("存档");
    private RadioMenuItem itExit = new RadioMenuItem("退出");
    private MenuBar menuBar = new MenuBar();

    GameMenu() {
        setItExit();
        setMenu();
    }

    //设置退出按钮的事件与快捷键
    private void setItExit() {
        itNewGame.setAccelerator(KeyCombination.valueOf("N"));
        itExit.setAccelerator(KeyCombination.valueOf("E"));
        itExit.setOnAction(event -> {
            System.exit(0);
        });
    }

    //将各个Item加入菜单
    private void setMenu() {
        menu.getItems().addAll(itNewGame, itReturn, itSave, itExit);
    }

    //设置并获取Menu
    public MenuBar getMenu() {
        menuBar.getMenus().addAll(menu);
        return menuBar;
    }

    public RadioMenuItem getItNewGame() {
        return itNewGame;
    }

    public static RadioMenuItem getItReturn() {
        return itReturn;
    }


    public RadioMenuItem getItSave() {
        return itSave;
    }
}

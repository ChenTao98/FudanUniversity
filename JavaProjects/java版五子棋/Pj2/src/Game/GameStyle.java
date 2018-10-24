package Game;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class GameStyle {
    private Menu styleMenu = new Menu("风格");
    private RadioMenuItem style1 = new RadioMenuItem("古风");
    private RadioMenuItem style2 = new RadioMenuItem("动漫");
    private ToggleGroup toggleGroup = new ToggleGroup();
    private MenuBar menuBar = new MenuBar();

    //在构造方法中调用函数，设置必要组件的样式、事件；
    GameStyle() {
        setStyleMenu();
        setToggleGroup();
        setMenuBar();
    }

    //往菜单加入选项
    private void setStyleMenu() {
        styleMenu.getItems().addAll(style1, style2);
    }

    //将选项设置为单选
    private void setToggleGroup() {
        style1.setToggleGroup(toggleGroup);
        style2.setToggleGroup(toggleGroup);
    }

    //设置MenuBar，以便菜单可以加入面板
    private void setMenuBar() {
        menuBar.getMenus().add(styleMenu);
    }

    //组件的获取函数
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public RadioMenuItem getStyleOne() {
        return style1;
    }

    public RadioMenuItem getStyleTwo() {
        return style2;
    }
}

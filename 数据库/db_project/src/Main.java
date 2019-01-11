import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pane.*;

public class Main extends Application {
    private BorderPane stagePane = new BorderPane();
    private BorderPane centerPane;
    private Menu menu = new Menu("菜单");
    private ToggleGroup radioItemGroup = new ToggleGroup();
    private MenuBar menuBar = new MenuBar();
    private RadioMenuItem[] radioMenuItemArray;
    private String[] menuItemNameArray = {"停车信息插入", "停车收费查询", "车位信息查询",
            "物业盈利汇总","物业收支查询", "住户账单管理", "住户信息管理", "房屋信息管理",
            "报修信息管理","投诉信息管理","维修记录管理","排查记录管理"};
    private String[] classNameArray = {ParkInsertPane.class.getName(), ParkQueryPane.class.getName(), ParkInfoQueryPane.class.getName(),
            PropertyProfitPane.class.getName(),PropertyInAndOutPane.class.getName(), BillPane.class.getName(), HouseholdInfoPane.class.getName(), RoomInfoPane.class.getName(),
            RepairPane.class.getName(),ComplaintPane.class.getName(),ServicePane.class.getName(),CheckPane.class.getName()};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        radioMenuItemArray = new RadioMenuItem[menuItemNameArray.length];
        for (int i = 0; i < radioMenuItemArray.length; i++) {
            radioMenuItemArray[i] = new RadioMenuItem(menuItemNameArray[i]);
            int index = i;
            radioMenuItemArray[i].setOnAction(event -> setItemAction(classNameArray[index]));
        }
        radioMenuItemArray[0].setSelected(true);
        radioItemGroup.getToggles().addAll(radioMenuItemArray);
        menu.getItems().addAll(radioMenuItemArray);
        menuBar.getMenus().add(menu);
        centerPane = new ParkInsertPane();
        stagePane.setTop(menuBar);
        stagePane.setCenter(centerPane);
        Scene scene = new Scene(stagePane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setItemAction(String className) {
        stagePane.getChildren().remove(centerPane);
        try {
            Class paneClass = Class.forName(className);
            centerPane = (BorderPane) paneClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        stagePane.setCenter(centerPane);
    }
}

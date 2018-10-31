package top.lf.stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.lf.core.AppContext;
import top.lf.core.constant.AppConstant;

/**
 * Created by YuanYan on 2018/10/31.
 */

public class MainStage extends Application {

    private final String FXML_URL = "/fxml/loginUi.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pkStage) throws Exception {
        //Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        //Application.setUserAgentStylesheet((getClass().getResource("MainStyle.css").toExternalForm()));//全局CSS
        Parent root = FXMLLoader.load(getClass().getResource(FXML_URL));
        pkStage.setTitle(AppConstant.APP_VERSION);//窗口标题
        pkStage.getIcons().add(new Image("assets/imgs/title/fee.png"));//标题Icon图标
        Scene pkScene = new Scene(root);
        pkStage.setScene(pkScene);
        pkStage.setAlwaysOnTop(true);//始终位于顶层显示
        pkStage.setResizable(false);//禁止调整窗口大小
        AppContext.stageManager.addStage("loginStage",pkStage);
        pkStage.show();
    }
}

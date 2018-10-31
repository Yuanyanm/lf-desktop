package top.lf.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.lf.core.AppContext;
import top.lf.core.constant.AppConstant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-01 13:13
 * @Description:
 */
public class LoginControl implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField userPwd;
    @FXML
    private Button btnReset;
    @FXML
    private TextField userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*btnLogin.setOnMousePressed(event -> {

        });*/
    }

    @FXML
    public void btnLogin_OnClick(){
        /**
         * 判断+校验
         * */

        //登录成功,跳转到收费主界面
        try {
            jump2Stage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void jump2Stage() throws IOException {
        AppContext.stageManager.getStage("loginStage").close();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/feeMainUi.fxml"));
        Stage pkStage = new Stage();
        pkStage.setTitle(AppConstant.APP_VERSION);//窗口标题
        pkStage.getIcons().add(new Image("assets/imgs/title/fee.png"));//标题Icon图标
        Scene pkScene = new Scene(root);
        pkStage.setScene(pkScene);
        //pkStage.setAlwaysOnTop(true);//始终位于顶层显示
        pkStage.setResizable(false);//禁止调整窗口大小
        AppContext.stageManager.addStage("feeMainStage",pkStage);
        pkStage.show();
    }


}

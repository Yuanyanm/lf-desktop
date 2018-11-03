package top.lf.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import top.lf.util.DateUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-03 12:38
 * @Description:
 */
public class QrScanControl implements Initializable {

    @FXML
    private Button btnSubmitPay;

    @FXML
    private Label stateLabel;

    @FXML
    private Label stateMsgLabel;

    //当前订单支付提交状态
    public static int PAY_STATE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("----initialize--- "+ DateUtil.currentDateTime());
        //重置支付状态
        PAY_STATE = 0;

    }

    @FXML
    public void btnSubmitPay_OnClicked(){
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();*/
        showTimedDialog(5000,"我是提示信息啊啊啊");
        Image image;
        //图片为manage.png  设置为40*40 的大小显示
        image = new Image("assets/imgs/icon/paying.png",30,30,false,false);
        stateLabel.setGraphic(new ImageView(image));
        stateLabel.setText("请扫描或手工输入对方付款码！");

    }


    public static void showTimedDialog(long time, String message) {
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.APPLICATION_MODAL);
        Button closeBtn = new Button("知道了");
        closeBtn.setOnAction(e -> {
            popup.close();
        });
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(new Label(message), closeBtn);
        Scene scene = new Scene(root);
        popup.getIcons().add(new Image("assets/imgs/title/fee.png"));//标题Icon图标
        popup.setScene(scene);
        popup.setTitle("提示信息");
        popup.show();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                if (popup.isShowing()) {
                    Platform.runLater(() -> popup.close());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}

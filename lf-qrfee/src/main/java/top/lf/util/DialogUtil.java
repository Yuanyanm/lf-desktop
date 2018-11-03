package top.lf.util;

import javafx.application.Platform;
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

/**
 * Created by YuanYan on 2018/11/3.
 */
public class DialogUtil {

    public static void showTimedDialog(long time, String message) {
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setMinWidth(120);
        popup.setMinHeight(50);
        popup.setResizable(false);
        Button closeBtn = new Button("知道了");
        closeBtn.setOnAction(e -> {
            popup.close();
        });
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setSpacing(20);
        Image image = new Image("assets/imgs/icon/qipao.png",20,20,false,false);
        Label msgLabel = new Label();
        msgLabel.setText(message);
        msgLabel.setGraphic(new ImageView(image));
        root.getChildren().addAll(msgLabel, closeBtn);
        Scene scene = new Scene(root);
        popup.getIcons().add(new Image("assets/imgs/title/js.png"));//标题Icon图标
        popup.setScene(scene);
        popup.setTitle("提示信息！");
        popup.show();
        if(time > 0){
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
}

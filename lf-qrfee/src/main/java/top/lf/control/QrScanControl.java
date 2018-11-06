package top.lf.control;

import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import top.lf.core.AppContext;
import top.lf.core.base.AppControl;
import top.lf.util.ImageUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-03 12:38
 * @Description:
 */
public class QrScanControl implements AppControl {

    @FXML
    private Button btnSubmitPay;
    @FXML
    private TextField qrCodeTxt;
    @FXML
    private Label msgLabel;
    @FXML
    private Label payMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnSubmitPay_OnClicked() {
        msgLabel.setGraphic(new ImageView(ImageUtil.getImage("info")));
        if (StrUtil.isBlank(qrCodeTxt.getText())) {
            msgLabel.setText("请扫描或手工输入对方付款码！");
            msgLabel.setTextFill(Paint.valueOf("black"));
            return;
        } else {
            //禁止再次提交
            btnSubmitPay.setDisable(true);
            /**
             *提交支付接口，并根据支付结果友好展示提示信息
             * 支付中：需要对方输入密码！——此种状态需要定时轮询支付结果
             * 支付失败：账户余额不足！
             * 支付失败：支付密码错误！......
             * 支付成功，支付渠道【微信】，金额：Y99.99。
             */
            Stage thisStage = (Stage) msgLabel.getScene().getWindow();
            //********************** 支付中 ***************************
            btnSubmitPay.setText("支付中...");
            msgLabel.setText("支付中(请勿关闭当前窗口)，请稍后...");

            Task<String> tmpTask = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    //call方法区域非javaFx主线程，不能直接使用fxml内部控件
                    Thread.sleep(2000);//模拟数据发送，并等待返回。
                    String sss = "支付中：需要对方[输入密码]...";
                    //updateMessage(sss);
                    // 非UI线程对UI进行数据修改
                    Platform.runLater(() -> {
                        msgLabel.setText(sss);
                    });
                    boolean payFlag = false;
                    int tmpSecs = 0;
                    while (true) {
                        tmpSecs++;
                        if(payFlag){
                            //支付成功，延时2秒，自动关闭窗口并结束轮询。
                            Thread.sleep(2000);
                            updateMessage("closeStage");
                            break;
                        }
                        Thread.sleep(5000);//模拟等待用户输入密码，并间隔5秒查询一次支付结果。
                        updateMessage("支付中(请勿关闭当前窗口)，请稍后...");
                        if (tmpSecs == 4) {
                            updateMessage("[微信]支付成功，金额：￥99.99 ！");
                            payFlag = true;
                        }
                    }
                    return "";
                }
            };


            //payMsg属性监听
            payMsg.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (StrUtil.isBlank(newValue)) {
                        newValue = "支付中(请勿关闭当前窗口)，请稍后...";
                    } else {
                        msgLabel.setText(newValue);
                        if (newValue.contains("成功")) {
                            btnSubmitPay.setText("支付成功");
                        } else if (newValue.equals("closeStage")) {
                            thisStage.close();
                            FeeMainControl feeMainControl = (FeeMainControl)AppContext.CONTROL.get("feeMainControl");
                            feeMainControl.resetUi();
                        }
                    }
                }
            });


            //UI控件属性与Task属性绑定，“自动更新”UI控件
            payMsg.textProperty().bind(tmpTask.messageProperty());
            //msgLabel.textProperty().bind(tmpTask.messageProperty());
            /*tmpTask.setOnSucceeded(e -> {
                System.out.println("=======0008");
                msgLabel.textProperty().unbind();
                msgLabel.setText("[微信]支付成功，金额：￥99.99 ！");
            });*/
            Thread thread = new Thread(tmpTask);
            thread.setDaemon(true);
            thread.start();


          /*  System.out.println("================ A");
            msgLabel.setText("支付中：需要对方[输入密码]...");
            int i = 0 ;
            while (true){
                if(new Date().getTime() - time > 5000){
                    break;
                }
            }
            System.out.println("================ B");
            btnSubmitPay.setText("支付成功");
            msgLabel.setText("[微信]支付成功，金额：￥99.99 ！");*//*


            System.out.println("33: " + DateUtil.currentTime());

            /*//********************* 支付失败 **************************
             //需重新生成支付流水号
             *//*btnSubmitPay.setText("再次支付");
            msgLabel.setGraphic(new ImageView(ImageUtil.getImage("notice")));
            msgLabel.setText("支付失败：对方账户余额不足！");
            qrCodeTxt.setText("");
            btnSubmitPay.setDisable(false);*//*
            /*//********************* 支付成功 **************************
             *//*btnSubmitPay.setText("支付成功");
            msgLabel.setText("[微信]支付成功，金额：￥99.99 ！");
            try {
                //支付成功，三秒后自动关闭扫码窗口
                Thread.sleep(3000);
                //thisStage.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public void resetUi() {

    }
}

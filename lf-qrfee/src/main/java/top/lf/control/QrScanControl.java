package top.lf.control;

import cn.hutool.core.util.StrUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import top.lf.util.ImageUtil;

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
    private TextField qrCodeTxt;
    @FXML
    private Label msgLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnSubmitPay_OnClicked() {
        if (StrUtil.isBlank(qrCodeTxt.getText())) {
            msgLabel.setGraphic(new ImageView(ImageUtil.getImage("info")));
            msgLabel.setText("请扫描或手工输入对方付款码！");
            msgLabel.setTextFill(Paint.valueOf("black"));
            return;
        } else {
            btnSubmitPay.setDisable(true);//禁止再次提交
            /**
             *提交支付接口，并根据支付结果友好展示提示信息
             * 支付中：需要对方输入密码！——此种状态需要定时轮询支付结果
             * 支付失败：账户余额不足！
             * 支付失败：支付密码错误！......
             */
            msgLabel.setText("支付中，请稍后...");
        }
    }

}

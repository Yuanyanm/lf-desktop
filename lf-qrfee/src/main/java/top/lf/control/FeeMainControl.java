package top.lf.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.lf.core.AppContext;
import top.lf.core.base.AppControl;
import top.lf.core.constant.AppConstant;
import top.lf.core.event.WindowsCloseEvent;
import top.lf.vo.FeeVo;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-01 13:13
 * @Description:
 */
public class FeeMainControl implements AppControl {

    @FXML
    public TableView<FeeVo> mzFeeTable;

    @FXML
    public TableColumn<FeeVo, CheckBox> colCheckBox;

    @FXML
    public CheckBox colSelectAll;

    @FXML
    public TableColumn<FeeVo, String> colVouchNo;

    @FXML
    public TableColumn<FeeVo, String> colFeeType;

    @FXML
    public TableColumn<FeeVo, String> colFeeAmt;

    @FXML
    public TableColumn<FeeVo, String> colVouchDate;

    @FXML
    public MenuItem itBtn0101;

    @FXML
    public MenuItem itBtn0102;

    @FXML
    public TextField patientNo;

    @FXML
    public Button btnQueryFee;

    @FXML
    public Label patientName;

    @FXML
    public Label feeVouchNum;

    @FXML
    public Label totalFeeAmt;

    public ObservableList<FeeVo> feeData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //行选中事件(同一行不能连续两次触发该事件)
        mzFeeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FeeVo>() {
            @Override
            public void changed(ObservableValue<? extends FeeVo> observable, FeeVo oldValue, FeeVo newValue) {
                //newValue.cb.setSelected(!newValue.cb.isSelected());
                //System.out.println("====COL======== "+oldValue+"  , "+newValue);
            }
        });

        //行点击事件
        mzFeeTable.setRowFactory(tv -> {
            TableRow<FeeVo> row = new TableRow<FeeVo>();
            row.setOnMouseClicked(event -> {
                //点击次数=event.getClickCount()
                if (!row.isEmpty()) {
                    FeeVo vo = row.getItem();
                    vo.cb.setSelected(!vo.cb.isSelected());
                    autoCalculateFee();
                }
            });
            return  row;
        });

        //CheckBox选中改变事件
        ChangeListener<Boolean> cbListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                autoCalculateFee();
            }
        };

        //表头全选单选框
        colSelectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(feeData != null){
                    for (FeeVo itemData : feeData) {
                        itemData.cb.setSelected(newValue);
                    }
                }
                autoCalculateFee();
            }
        });

        feeData = FXCollections.observableArrayList(
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S000001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S000011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,false,"S000021","检查费","88.62","2018-10-20 10:25:48")
        );
        colCheckBox.setCellValueFactory(new PropertyValueFactory<FeeVo,CheckBox>("cb"));
        colVouchNo.setCellValueFactory(new PropertyValueFactory<FeeVo,String>("vouchNo"));
        colFeeType.setCellValueFactory(new PropertyValueFactory<FeeVo,String>("feeType"));
        colFeeAmt.setCellValueFactory(new PropertyValueFactory<FeeVo,String>("feeAmt"));
        colVouchDate.setCellValueFactory(new PropertyValueFactory<FeeVo,String>("vouchDate"));
        /*colVouchNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVouchNo()));
        colFeeType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFeeType()));
        colFeeAmt.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFeeAmt()));
        colVouchDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVouchDate()));*/

        mzFeeTable.setItems(feeData);
        autoCalculateFee();
    }

    @FXML
    public void btnScanQrCode_OnClick(){
        //totalFeeAmt.setDisable(true);//是否可用
        //totalFeeAmt.setVisible(false);//是否可见
        //totalFeeAmt.setManaged(false);//是否隐藏
        String feeAmtText = totalFeeAmt.getText();
        if("0.00".equals(feeAmtText) || Double.valueOf(feeAmtText) == 0.00){
            System.out.println("没有有效支付金额，不能发起支付！");
        }
        Stage qrScanStage = AppContext.STAGE.get("qrScanStage");
        if(qrScanStage == null){
            try {
                jump2Stage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            qrScanStage.show();
        }
    }

    /**
     * UI控件事件绑定
     * */
    public void uiEventBind(){


    }

    @FXML
    public void patientNoTextFieldAction(ActionEvent event) {

    }

    /**
     * 动态计算选中单据及金额信息并更新界面元素显示
     * */
    public void autoCalculateFee(){
        int i = 0;
        Double tmpAmt = new Double("0.00");
        //金额保留两位小数
        DecimalFormat df = new DecimalFormat("#0.00");
        if(feeData != null && feeData.size() > 0){
            for (FeeVo itemData : feeData) {
                if(itemData.cb.isSelected()){
                    i++;//累计选中单据数
                    tmpAmt += Double.valueOf(itemData.getFeeAmt());//累计选中单据金额
                }
            }
        }
        feeVouchNum.setText(""+i);
        totalFeeAmt.setText(df.format(tmpAmt));
    }

    @Override
    public void resetUi() {
        patientNo.setText("");
        //CheckBox选中改变事件
        ChangeListener<Boolean> cbListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                autoCalculateFee();
            }
        };
        feeData = FXCollections.observableArrayList(
                new FeeVo(cbListener,true,"S005001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S005011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S005001","检验费","33.85","2018-10-20 10:35:48"),
                new FeeVo(cbListener,true,"S005011","西药费","24.56","2018-10-20 10:15:48"),
                new FeeVo(cbListener,true,"S005001","检验费","33.85","2018-10-20 10:35:48")
        );
       // mzFeeTable.setItems(feeData);
    }

    public void jump2Stage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/QrScanUi.fxml"));
        Stage pkStage = new Stage();
        pkStage.setTitle(AppConstant.APP_VERSION);//窗口标题
        pkStage.getIcons().add(new Image("assets/imgs/title/fee.png"));//标题Icon图标
        Scene pkScene = new Scene(root);
        pkStage.setScene(pkScene);
        pkStage.setAlwaysOnTop(true);//始终位于顶层显示
        pkStage.setResizable(false);//禁止调整窗口大小
        //AppContext.stageManager.addStage("qrScanStage",pkStage);
        AppContext.STAGE.put("qrScanStage",pkStage);
        AppContext.CONTROL.put("feeMainControl",this);
        pkStage.setOnCloseRequest(new WindowsCloseEvent("qrScanStage",true));
        pkStage.show();

    }

}

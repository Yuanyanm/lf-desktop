package top.lf.vo;

import javafx.beans.property.SimpleStringProperty;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-19 15:19
 * @Description:
 */
public class OutpatientFeeVo {

    private final SimpleStringProperty vouchNo = new SimpleStringProperty();
    private final SimpleStringProperty feeType= new SimpleStringProperty();
    private final SimpleStringProperty feeAmt = new SimpleStringProperty();
    private final SimpleStringProperty vouchDate = new SimpleStringProperty();

    public OutpatientFeeVo(String vouchNo,String feeType,String feeAmt,String vouchDate) {
        this.setVouchNo(vouchNo);
        this.setFeeType(feeType);
        this.setFeeAmt(feeAmt);
        this.setVouchDate(vouchDate);
    }

    public String getVouchNo() {
        return vouchNo.get();
    }

    public SimpleStringProperty vouchNoProperty() {
        return vouchNo;
    }

    public void setVouchNo(String vouchNo) {
        this.vouchNo.set(vouchNo);
    }

    public String getFeeType() {
        return feeType.get();
    }

    public SimpleStringProperty feeTypeProperty() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType.set(feeType);
    }

    public String getFeeAmt() {
        return feeAmt.get();
    }

    public SimpleStringProperty feeAmtProperty() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt.set(feeAmt);
    }

    public String getVouchDate() {
        return vouchDate.get();
    }

    public SimpleStringProperty vouchDateProperty() {
        return vouchDate;
    }

    public void setVouchDate(String vouchDate) {
        this.vouchDate.set(vouchDate);
    }
}



package top.lf.vo;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-20 10:14
 * @Description:
 */
public class FeeVo {
    public CheckBox cb = new CheckBox();
    public String vouchNo;
    public String feeType;
    public String feeAmt;
    public String vouchDate;

    public FeeVo() {
    }

    public FeeVo(Object cbListener, Boolean isSelect, String vouchNo, String feeType, String feeAmt, String vouchDate) {
        if(cbListener != null && cbListener instanceof ChangeListener){
            cb.selectedProperty().addListener((ChangeListener)cbListener);
        }
        this.cb.setSelected(isSelect);
        this.vouchNo = vouchNo;
        this.feeType = feeType;
        this.feeAmt = feeAmt;
        this.vouchDate = vouchDate;
    }

    public CheckBox getCb() {
        return cb;
    }

    public void setCb(CheckBox cb) {
        this.cb = cb;
    }

    public String getVouchNo() {
        return vouchNo;
    }

    public void setVouchNo(String vouchNo) {
        this.vouchNo = vouchNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getVouchDate() {
        return vouchDate;
    }

    public void setVouchDate(String vouchDate) {
        this.vouchDate = vouchDate;
    }
}

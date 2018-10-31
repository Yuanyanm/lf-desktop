package top.lf.model;

import java.io.Serializable;

/**
 * @Author: YuanYan
 * @Create At: 2018-09-23 16:36
 * @Description: 下拉单选类
 */
public class SingleSelModel implements Serializable {
    private static final long serialVersionUID = 1205588444200502821L;
    private String selVal;
    private String selDesc;

    public SingleSelModel(String selVal, String selDesc) {
        this.selVal = selVal;
        this.selDesc = selDesc;
    }

    public String getSelVal() {
        return selVal;
    }

    public String getSelDesc() {
        return selDesc;
    }
}

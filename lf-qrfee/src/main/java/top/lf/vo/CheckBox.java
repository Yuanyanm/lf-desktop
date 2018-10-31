package top.lf.vo;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-20 10:12
 * @Description:
 */
public class CheckBox {
    CheckBox cBox = new CheckBox();

    public ObservableValue<CheckBox> getCheckBox() {
        return new ObservableValue<CheckBox>() {
            @Override
            public void addListener(ChangeListener<? super CheckBox> listener) {
            }
            @Override
            public void removeListener(ChangeListener<? super CheckBox> listener) {
            }
            @Override
            public CheckBox getValue() {
                return cBox;
            }
            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }
        };
    }

    public Boolean isSelected() {
        System.out.println("----- "+cBox.isSelected());
        return cBox.isSelected();
    }
}

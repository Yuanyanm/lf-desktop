package top.lf.core.event;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import top.lf.core.AppContext;
import top.lf.util.DateUtil;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-03 13:44
 * @Description:
 */
public class WindowsCloseEvent implements EventHandler<WindowEvent> {

    private String stageName;

    private boolean closeable;

    public WindowsCloseEvent(String stageName,boolean closeFlag){
        this.stageName = stageName;
        this.closeable = closeFlag;
    }

    public void handle(WindowEvent event) {
        if(!closeable){
            //阻止事件传递
            event.consume();
        }else{
            AppContext.STAGE.remove(stageName);
            AppContext.CONTROL.remove(stageName+"Control");
        }
    }
}


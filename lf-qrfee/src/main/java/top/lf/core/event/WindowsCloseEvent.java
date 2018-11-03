package top.lf.core.event;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import top.lf.util.DateUtil;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-03 13:44
 * @Description:
 */
public class WindowsCloseEvent implements EventHandler<WindowEvent> {

    private Stage targetStage;

    private boolean closeable;

    public WindowsCloseEvent(Stage stage,boolean closeFlag){
        this.targetStage = stage;
        this.closeable = closeFlag;
    }

    public void handle(WindowEvent event) {
        System.out.println("------- "+ DateUtil.currentDateTime());
        if(!closeable){
            //阻止事件传递
            event.consume();
        }
    }
}


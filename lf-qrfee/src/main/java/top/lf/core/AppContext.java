package top.lf.core;

import javafx.stage.Stage;
import top.lf.core.base.AppControl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-01 13:40
 * @Description: App应用上下文
 */
public class AppContext {
    public static StageManager STAGE_MANAGER = new StageManager();
    public static Map<String, Stage> STAGE = new TreeMap<>();
    public static Map<String, AppControl> CONTROL = new TreeMap<>();
}

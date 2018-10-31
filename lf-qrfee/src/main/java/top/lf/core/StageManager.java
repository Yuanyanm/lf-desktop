package top.lf.core;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YuanYan
 * @Create At: 2018-10-01 13:38
 * @Description: Stage管理类
 */
public class StageManager {
    //存放所有的Stage实例
    private Map<String, Stage> stageMap = new HashMap<>();

    public void addStage(String name, Stage stage) {
        stageMap.put(name, stage);
    }

    public Stage getStage(String name) {
        return stageMap.get(name);
    }

    public void closeStage(String name) {
        stageMap.get(name).close();
    }

    //实现Stage的跳转，从currentStage跳转到targetStage
    public void jump(String currentStageName, String targetStageName) {
        stageMap.get(currentStageName).close();
        stageMap.get(targetStageName).show();
    }

    public void release(String name) {
        stageMap.remove(name);
    }

}

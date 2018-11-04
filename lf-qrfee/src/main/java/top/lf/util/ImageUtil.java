package top.lf.util;

import javafx.scene.image.Image;

/**
 * Created by YuanYan on 2018/11/3.
 */
public class ImageUtil {

    public static Image getImage(String imgType){
        //info-友好提示类，notice-错误警告类
        String imgUrl = "assets/imgs/title/ts.png";//默认info
        if("notice".equals(imgType)){
            imgUrl = "assets/imgs/title/js.png";
        }else if("paying".equals(imgType)){
            imgUrl = "assets/imgs/icon/paying.png";
        }
        return new Image(imgUrl,20,20,false,false);
    }

}

package top.lf.busi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-08 16:09
 * @Description:
 */
@Controller
public class MainController {

    @RequestMapping("/123")
    @ResponseBody
    public String homeInx123() {
        return "我是SpringBoot 应用~";
    }

    @RequestMapping("/")
    public String homeInx() {
        return "index.html";
    }


}

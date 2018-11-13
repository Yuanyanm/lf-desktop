package top.lf.busi.web;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "/t/{viewId}",method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable String viewId) {
        System.out.println("============ viewId: "+viewId);
        ModelAndView mv  = new ModelAndView();
        mv.setViewName(viewId);
        mv.addObject("_dc", DateUtil.currentSeconds()+"");
        return  mv;
    }


}

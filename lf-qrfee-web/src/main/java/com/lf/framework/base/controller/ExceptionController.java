package com.lf.framework.base.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: YuanYan
 * @Create At: 2018-03-22 16:05
 * @Description:统一异常视图映射
 */
@Controller
@RequestMapping(value = "/errorpage")
public class ExceptionController extends BaseController {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionController.class);

    @RequestMapping(value={"/{viewName}"}, method= RequestMethod.GET)
    public String viewPage(@PathVariable String viewName, Model model) {
        return "/errorpage/"+viewName;
    }
}

package com.lf.framework.base.controller;

import com.lf.framework.core.exception.ParameterException;
import com.lf.framework.core.exception.SessionInvalidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: YuanYan
 * @Create At: 2018-03-22 16:05
 * @Description:
 */
@Controller
@RequestMapping(value = "/api")
public class TestController extends BaseController {
    private static final Logger LOGGER = LogManager.getLogger(TestController.class);

    @RequestMapping("/func01")
    public String func01() {
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");

        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");

        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");

        return "/index";
    }

    @RequestMapping("/func02")
    public String func02() {
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");
        try {
            int i = 1 / 0;
        }catch (Exception e){
            throw new ParameterException("我是自定义【参数】异常");
        }

        return "/index";
    }

    @RequestMapping("/func03")
    public String func03() {
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");

        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");
        try {
            int i = 1 / 0;
        }catch (Exception e){
            throw e;
        }
        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");

        return "/index333";
    }

    @RequestMapping("/func04")
    public String func04() {
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");
        LOGGER.debug("#################  debug ########################");

        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");
        LOGGER.info("#################  info ########################");
        try {
            int i = 1 / 0;
        }catch (Exception e){
            throw new SessionInvalidException("您尚未登录或者登录已经失效，请重新登录！");
        }
        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");
        LOGGER.error("#################  error ########################");

        return "/index";
    }

    public static void main(String[] args) {
    }
}

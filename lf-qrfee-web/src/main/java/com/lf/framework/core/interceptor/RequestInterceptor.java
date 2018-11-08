package com.lf.framework.core.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: YuanYan
 * @Create At: 2018-03-22 16:49
 * @Description:对所有Http请求进行拦截，并设置basePath值，供前端将所有地址转换成绝对地址
 */
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(RequestInterceptor.class);
    private static String ctxPath = null;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ctxPath == null) {
            String path = request.getContextPath();
            ctxPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            if (ctxPath == null || ctxPath.equals("")) ctxPath = "/";
        }
        //后续Controller中可对request中的属性进行覆盖
        request.setAttribute("ctxPath",ctxPath);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOGGER.debug("############### postHandle(S) ##########################");
        if (isAjax(request) && modelAndView == null) {
            LOGGER.debug("###");
            LOGGER.debug("### Ajax请求，返回的为JSON~");
            LOGGER.debug("###");
            LOGGER.debug("############### postHandle(E) ##########################");
            return;
        }
        modelAndView = (modelAndView == null ? new ModelAndView() : modelAndView);
        String tmpView = modelAndView.getViewName();
        LOGGER.debug("###");
        LOGGER.debug("### Request.Referer : " + request.getHeader("Referer"));
        LOGGER.debug("### Current Return ViewName: " + tmpView);
       /*if (response.getStatus() == 404) {
            LOGGER.debug("###");
            LOGGER.debug("### 404 Error~ ");
            modelAndView.setViewName("redirect:/error/404");
            //modelAndView.setViewName("forward:/error/404");
        }else if (response.getStatus() != 200) {
             LOGGER.debug("###");
             LOGGER.debug("### 500 Error ~ ");
             modelAndView.setViewName("redirect:/error/500");
         }*/
        LOGGER.debug("###");
        LOGGER.debug("############### postHandle(E) ##########################");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private static boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

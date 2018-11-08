package com.lf.framework.core.resolver;

import com.lf.framework.core.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: YuanYan
 * Create At: 2018/3/23 10:44
 * Description:自定义全局统一异常解析,Order优先级设置最高(系统所有异常由当前Handler处理)
 */
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver,Ordered{

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private static String ctxPath = null;
	private static final String HEADER_REQUEST_KEY = "X-Requested-With";
	private static final String HEADER_REQUEST_VALUE = "XMLHttpRequest";
	private static final String PARAMETER_AJAX_KEY = "__TO_JSON_HANDLER__";
	private String defaultErrorView;
	public String getDefaultErrorView() {
		return defaultErrorView;
	}
	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        if (ctxPath == null) {
            String path = request.getContextPath();
            ctxPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            if (ctxPath == null || ctxPath.equals("")) ctxPath = "/";
        }
        //设置basePath值，供前端将所有地址转换成绝对地址
        request.setAttribute("ctxPath",ctxPath);

        LOGGER.error("#############################全局异常(S)#########################################");
        LOGGER.error("###");
        LOGGER.error("###" + exception.getMessage());
		ModelAndView modelAndView;
        // ********************* 标准Ajax请求，返回错误数据 *************************
		if (HEADER_REQUEST_VALUE.equals(request.getHeader(HEADER_REQUEST_KEY))
				|| request.getParameter(PARAMETER_AJAX_KEY) != null) {
			modelAndView = new ModelAndView(new MappingJackson2JsonView());
			modelAndView.getModel().put("success", false);
            modelAndView.getModel().put("message", exception.getMessage());
            LOGGER.error("################## Ajax请求异常(S) #####################");
            LOGGER.error("####");
            LOGGER.error("#### "+ exception.getMessage());
            LOGGER.error("####");
            LOGGER.error("################## Ajax请求异常(E) #####################");

            if (exception instanceof SessionInvalidException) {
                response.setStatus(401);
            }else if (exception instanceof LoginException) {
                response.setStatus(403);
			}else if (exception instanceof ParameterException) {
				response.setStatus(503);
			}else if (exception instanceof BusinessException) {
				response.setStatus(501);
			}else if (exception instanceof ServiceException) {
				response.setStatus(501);
			}else {
                //其他未明确定义的异常
                response.setStatus(500);
                modelAndView.getModel().put("message","系统开小差啦，请联系管理员！");
            }
            LOGGER.error("###");
            LOGGER.error("#############################全局异常(E)#########################################");
			return modelAndView;

		} else {
		    // ************ 其他请求(postMan、httpClient、Url直接访问)返回错误视图 ****************
            LOGGER.error("################## 其他请求异常(非Ajax)(S) #####################");
            LOGGER.error("####");
            LOGGER.error("#### "+ exception.getMessage());
            LOGGER.error("####");
            LOGGER.error("################## 其他请求异常(非Ajax)(E) #####################");
            modelAndView  = new ModelAndView(defaultErrorView);
            modelAndView.addObject("errorMsg",exception.getMessage());
            //获取异常详细描述信息，供前端显示
            StringBuffer stringBuffer = new StringBuffer(exception.toString() + "\n");
            StackTraceElement[] messages = exception.getStackTrace();
            int length = messages.length;
            for (int i = 0; i < length; i++) {
                stringBuffer.append("\t"+messages[i].toString()+"\n");
            }
            modelAndView.addObject("exceptionMsg",stringBuffer.toString());
            if (exception instanceof SessionInvalidException) {
                response.setStatus(401);
                modelAndView.setViewName("/errorpage/401");
                return modelAndView;
            }else if (exception instanceof NoHandlerFoundException){
                modelAndView.setViewName("/errorpage/404");
            }else if(!(exception instanceof BaseException)){
                //所有非自定义异常“模糊”统一处理(如：403、501、502、运行时异常等......)
                modelAndView.addObject("errorMsg","系统开小差啦，请联系管理员！");
            }
            LOGGER.error("###");
            LOGGER.error("#############################全局异常(E)#########################################");
            return modelAndView;
		}
	}

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

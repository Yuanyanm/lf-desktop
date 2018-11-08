package com.lf.framework.core.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: YuanYan
 * @Create At: 2018-04-24 11:16
 * @Description:自定义过滤器，防止XSS、SQL注入攻击
 */
public class XssHttpServletRequestFilter implements Filter {

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper(
                (HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}

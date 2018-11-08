package com.lf.framework.base.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
    public static final String PAGE_ROOT = "/viewpage";

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getRespone() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected HttpSession getHttpSession() {
        return getRequest().getSession();
    }

    protected String getHttpBasePath() {
        return this.getRequest().getScheme() + "://" + this.getRequest().getServerName() + ":" + this.getRequest().getServerPort() + this.getRequest().getContextPath();
    }

	/*protected Principal getCurrentPrincipal() {
        return SessionHolder.getCurrentPrincipal();
	}
	
	protected List<String> getCurrentRoleIds() {
		return this.getCurrentPrincipal().getRoleIds();
	}
	
	protected String getCurrentRoleIdsStr() {
		return this.getCurrentPrincipal().getRoleIdsStr();
	}

    protected String getCurrentOrgNo() {
	    return SessionHolder.getCurrentOrgInfo().getOrgNo();
    }

    protected String getCurrentAppAuthToken() {
        return SessionHolder.getCurrentShopAppAuthToken();
    }

    protected String getCurrentShopPid() {
        return SessionHolder.getCurrentShopAuthInfo().getShopPid();
    }

    protected String getCurrentShopId() {
        return SessionHolder.getCurrentShopAuthInfo().getShopId();
    }

    protected String getCurrentIsvAppId() {
        return SessionHolder.getCurrentShopAuthInfo().getIsvAppId();
    }

    protected String getRequestIp() {
        return SessionHolder.getRequestClientIP();
    }*/

}

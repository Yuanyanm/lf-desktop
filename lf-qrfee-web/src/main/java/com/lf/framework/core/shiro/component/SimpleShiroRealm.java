package com.lf.framework.core.shiro.component;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: YuanYan
 * Create At: 2018/4/12 11:33
 * Description:自定义Realm,Shiro安全数据源(用户登录及具体权限获取)
 */
public class SimpleShiroRealm extends AuthorizingRealm  {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleShiroRealm.class);
	
	@Autowired
    //private ShiroRealmService shiroRealmService;

	/**
     * 执行登录认证(执行SecurityUtils.getSubject().login(token)时触发)
	 */
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		/**
         * 可以在此做用户登录验证(即将LoginService中登录逻辑直接放在这里)
         * */
        //身份认证验证成功，返回一个AuthenticationInfo实现;
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getUsername() ,token.getCredentials(), getName());
		return simpleAuthenticationInfo;
    }

	
	/**
     * 获取授权信息（查询用户拥有的资源权限,执行SecurityUtils.getSubject().isPermitted()鉴权时触发）
	 */
	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String principal = (String) principals.fromRealm(getName()).iterator().next();
        
       /* //角色roles授权
        List<String> roles = shiroRealmService.getCurrentUserRoles(principal);
        if(roles != null && roles.size() > 0) authorizationInfo.setRoles(new HashSet<String>(roles));
  
        //许可perms授权
        List<String> perms = shiroRealmService.getCurrentUserPermissions(principal);
        if(perms != null && perms.size() > 0) authorizationInfo.addStringPermissions(perms);
        
        //其他许可
        List<String> otherPermissions = shiroRealmService.selectOtherPermissions(principal);
        if(otherPermissions != null && otherPermissions.size() > 0) authorizationInfo.addStringPermissions(otherPermissions);
        
        authorizationInfo.setRoles(new HashSet<String>( roles ));
        authorizationInfo.addStringPermissions(perms);*/

        LOGGER.debug("=========================================================================");
        LOGGER.debug("AuthorizingRealm doGetAuthorizationInfo start");
        LOGGER.debug("登录用户：" + principal);
        LOGGER.debug("角色信息:" + "");//用户角色
        LOGGER.debug("资源信息:" + "");//用户资源许可
        LOGGER.debug("otherPerms:" + "");
        LOGGER.debug("AuthorizingRealm doGetAuthorizationInfo end");
        LOGGER.debug("=========================================================================");
        return authorizationInfo;  
    }

}


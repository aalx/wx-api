package com.github.niefy.modules.sys.oauth2;

import com.github.niefy.common.utils.Constant;
import com.github.niefy.modules.sys.entity.SysUserEntity;
import com.github.niefy.modules.sys.service.ShiroService;
import com.github.niefy.modules.sys.entity.SysUserTokenEntity;
import com.github.niefy.modules.sys.service.SysRoleService;
import com.github.niefy.modules.sys.service.SysUserRoleService;
import com.github.niefy.modules.wx.entity.WxAccount;
import com.github.niefy.modules.wx.service.WxAccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 认证
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        //token失效
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
        //账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        try {
            List<WxAccount> wxAccountList ;
            if(user.getUserId()!= Constant.SUPER_ADMIN){
                 wxAccountList =wxAccountService.queryRoleAccount(user.getUserId());
            }else{
                wxAccountList =wxAccountService.list();
            }

            if (!CollectionUtils.isEmpty(wxAccountList)) {
                Set<String> wxAccounts = new HashSet();
                for (WxAccount wxAccount : wxAccountList) {
                    wxAccounts.add(wxAccount.getAppid());
                }
                user.setAppids(wxAccounts);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }
}

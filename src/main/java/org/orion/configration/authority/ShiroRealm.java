package org.orion.configration.authority;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.rbac.LoginResult;
import org.orion.common.rbac.OrionUserRole;
import org.orion.common.rbac.RolePermission;
import org.orion.common.rbac.User;
import org.orion.systemAdmin.service.AuthorizeActionService;
import org.orion.systemAdmin.service.RbacService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm {

    private static LoginResult loginResult;

    {
        this.setCredentialsMatcher(new AllowAllCredentialsMatcher());
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        RbacService rbacService = SpringUtil.getBean(RbacService.class);
        User user = loginResult.getUser();
        if (user != null) {
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            List<OrionUserRole> userRoles = rbacService.getUserRole(user);
            for (OrionUserRole userRole : userRoles) {
                roles.add(userRole.getRoleId());
                List<RolePermission> rolePermissions = rbacService.getRolePermission(userRole);
                for (RolePermission rolePermission : rolePermissions) {
                    permissions.add(rolePermission.getPermission());
                }
            }
            authorizationInfo.setRoles(roles);
            authorizationInfo.setStringPermissions(permissions);
            return authorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        AuthorizeActionService authorizeActionService = SpringUtil.getBean(AuthorizeActionService.class);
        User user = new User();
        user.setLoginId(token.getUsername());
        user.setPwd(new String(token.getPassword()));
        LoginResult result = authorizeActionService.login(user);
        ShiroRealm.loginResult = result;
        if (result.getStatus() != 1) {
            throw new AuthenticationException("LOGIN_DENIED");
        } else {
            return new SimpleAuthenticationInfo(result.getUser().getLoginId(), result.getUser().getPwd(), getName());
        }
    }

    public static LoginResult getLoginResult() {
        return ShiroRealm.loginResult;
    }
}

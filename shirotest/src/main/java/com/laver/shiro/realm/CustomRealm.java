package com.laver.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by L on 2018/8/27.
 */
public class CustomRealm extends AuthorizingRealm {

    private static final String salt=")(%&*^%$)(^^&()";



    Map<String,String> userMap=new HashMap<String, String>(16);

    {
        userMap.put("laver","0d84194d344614dbeab72bdec2670168");

        super.setName("customRealm");
    }


    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String userName=(String)principals.getPrimaryPrincipal();
        //从数据库或缓存中获取角色数据
        Set<String> roles=getRolesByUserName(userName);

        Set<String> permissions=getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }
    private Set<String> getRolesByUserName(String userName){
        Set<String> sets=new HashSet<String>();
        sets.add("user");
        sets.add("admin");
        return sets;
    }
    private Set<String> getPermissionsByUserName(String userName){
        Set<String> sets=new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //1.从主体传过来的认证信息中获得用户名
        String userName=(String)token.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password=getPasswordByUserName(userName);
        if (password==null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo("laver",password,"customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(salt));

        return authenticationInfo;
    }


    //模拟数据库访问
    private String getPasswordByUserName(String userName){
        return userMap.get(userName);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash=new Md5Hash("123456",salt);
        System.out.println(md5Hash);

    }
}

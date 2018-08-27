package com.laver.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by L on 2018/8/27.
 */
public class CustomRealmTest {

    @Test
    public void testAuthentication(){

        CustomRealm customRealm=new CustomRealm();

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        //2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("laver","123456");
        subject.login(token);


//        subject.logout();

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        subject.checkRole("admin");
//
        subject.checkPermission("user:delete");
    }

}
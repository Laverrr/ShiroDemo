package com.laver.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by L on 2018/8/27.
 */
public class JdbcRealmTest {

    DruidDataSource dataSource=new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shirotest");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }


    @Test
    public void testAuthentication(){

        JdbcRealm jdbcRealm=new JdbcRealm();

        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql="select password from User where username=?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql="select role from Role where username=?";
        jdbcRealm.setUserRolesQuery(roleSql);


        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token1=new UsernamePasswordToken("laver","123456");
        UsernamePasswordToken token2=new UsernamePasswordToken("bb","123");
        subject.login(token2);


//        subject.logout();

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

//        subject.checkRole("admin");
        subject.checkRole("user");
//
//
//        subject.checkRoles("admin","user");
//
//        subject.checkPermission("user:select");


    }
}

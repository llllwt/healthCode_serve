package com.noodles.healthycode.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.noodles.healthycode.entity.Student;
import com.noodles.healthycode.service.StudentService;
import com.noodles.healthycode.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AccountRealm  extends AuthorizingRealm {


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    StudentService studentService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken)authenticationToken;

        String userid = jwtUtils.getClaimByToken((String)jwtToken.getPrincipal()).getSubject();

        Student student = studentService.getById(userid);
        if(student == null){
            throw new UnknownAccountException("学生不存在");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(student,profile);

        System.out.println("--------------------------");

        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}

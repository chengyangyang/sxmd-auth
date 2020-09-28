package com.sxmd.base;

import cn.hutool.core.collection.CollectionUtil;
import com.sxmd.content.baseauthority.service.BaseAuthorityService;
import com.sxmd.content.baseuser.entity.BaseUserEntity;
import com.sxmd.content.baseuser.service.BaseUserService;
import com.sxmd.exception.SxmdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:  用户加载 权限和用户名密码校验
 *
 * @author cy
 * @date 2020年08月18日 18:53
 * Version 1.0
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取数据
        BaseUserEntity baseUser = baseUserService.getBaseUser(username);
        if (baseUser.isLock()) {
            throw new SxmdException("该账号被冻结。不能进行登录。");
        }
        // 查询用户权限
        List<String> codes = baseAuthorityService.findCodeByUserId(baseUser.getId());
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (CollectionUtil.isNotEmpty(codes)) {
            codes.forEach(x -> authorities.add(new SimpleGrantedAuthority(x)));
        }
        BaseUser baseUserResult = new BaseUser(baseUser.getUsername(), baseUser.getPassword(), authorities);
        return baseUserResult;
    }


}

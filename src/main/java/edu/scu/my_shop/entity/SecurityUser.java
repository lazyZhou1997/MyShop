package edu.scu.my_shop.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUser extends User implements UserDetails {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    public SecurityUser(User user){

        if (null!=user){
            this.setUserId(user.getUserId());
            this.setBirthday(user.getBirthday());
            this.setHeadImg(user.getHeadImg());
            this.setRole(user.getRole());
            this.setUserName(user.getUserName());
            this.setUserPassword(user.getUserPassword());
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        //授权
        SimpleGrantedAuthority authority = null;
        //用户角色
        boolean role = this.getRole();
        //role为true表示管理员
        if (role){
            authority = new SimpleGrantedAuthority("ADMIN");
        }else{//否则为普通用户
            authority = new SimpleGrantedAuthority("USER");
        }

        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getUserPassword();
    }

    @Override
    public String getUsername() {
        return super.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

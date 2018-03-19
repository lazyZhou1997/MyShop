package edu.scu.my_shop.security;

import edu.scu.my_shop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: 周秦春
 * @Description: 用于用户和角色的认证配置
 * @Date: Create in 2017/9/4 19:49
 * @ModifyBy:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自己编写的用户信息的服务类
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * 登陆成功处理
     */
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    /**
     * 配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // FIXME
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/registerpage",
                "/file","/fileUpload",//FIXME: add proper authority
                "/register",
                "/loginpage",
                "/",
                "/js/**",
                "/fonts/**",
                "/css/**",
                "/images/**",
                "/webjars/**",
                "/fail").permitAll()//无需登录认证权限访问
                .anyRequest().authenticated() //其他所有资源需要认证，登陆后访问
                .antMatchers("/userInfoPage","/changeUserInfo","/getUserInfo").hasAnyRole()
                .antMatchers("/admin").hasRole("ADMIN") //登陆之后拥有"ADMIN"权限才能够访问，否则会出现“403”权限不足的提示
                .and()
                .formLogin() //表单登陆
                .loginPage("/loginpage")
                .loginProcessingUrl("/login") //指定登陆页面为"/login"
                .successHandler(loginSuccessHandler) //登陆成功处理
                .failureUrl("/fail")
                .failureForwardUrl("/fail").permitAll()//登录失败页面跳转
                .defaultSuccessUrl("/index")//登录成功跳转页面
                .and()
                .rememberMe() //登陆后记住用户，下次自动登录，数据库中必须存在名为persistent_logins的表
                .tokenValiditySeconds(1209600);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //指定密码加密所使用的加密器为passwordEncoder()
        //将密码加密后写入数据库
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder(10));

        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }
}
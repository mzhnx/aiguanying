package com.gec.system.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gec.model.vo.LoginVo;
import com.gec.system.custom.CustomUser;
import com.gec.system.service.SysLoginLogService;
import com.gec.system.util.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 * </p>
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;
    private SysLoginLogService sysLoginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager,
                            RedisTemplate redisTemplate,
                            SysLoginLogService sysLoginLogService) {
        System.out.println("------------------TokenLoginFilter构造函数------------------");
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/admin/system/index/login", "POST")
        );
        this.redisTemplate = redisTemplate;
        this.sysLoginLogService=sysLoginLogService;
    }

    /**
     * 登录认证
     *
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        System.out.println("------------------attemptAuthentication用户登录认证---------------------");
        try {
            LoginVo loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVo.class);
            System.out.println("请求认证的账号 = " + loginVo.getUsername());
            System.out.println("请求认证的密码 = " + loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginVo.getUsername(),
                            loginVo.getPassword()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录成功
     *
     * @param request
     * @param response
     * @param chain
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        System.out.println("-------------------认证成功-----------------------");
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        String token = JwtHelper.createToken(
                customUser.getSysUser().getId() + "",
                customUser.getSysUser().getUsername()
        );
        // 记录成功的日志

        sysLoginLogService.recordLoginLog(customUser.getUsername(),
                1, IpUtil.getIpAddress(request),"登录成功");
        // todo
        // 保存权限数据到redis
        redisTemplate.opsForValue().set(
                customUser.getUsername(),
                JSON.toJSONString(customUser.getAuthorities())
        );
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        System.out.println("-------------------认证失败-------------------");
        if (e.getCause() instanceof RuntimeException) {
            System.out.println("运行时异常!");
            ResponseUtil.out(response, Result.build(null, 204, e.getMessage()));
        } else {
            System.out.println("账号不正确!");
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
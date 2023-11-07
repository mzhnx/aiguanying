package com.gec.system.filter;

import com.alibaba.fastjson.JSON;
import com.gec.system.util.JwtHelper;
import com.gec.system.util.ResponseUtil;
import com.gec.system.util.Result;
import com.gec.system.util.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("-----------------doFilterInternal权限认证--------------------");
        logger.info("uri:" + request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())||"/admin/system/upload/uploadImage".equals(request.getRequestURI())||"/admin/system/upload/uploadVideo".equals(request.getRequestURI())) {
//            SecurityContextHolder.getContext().setAuthentication(
//                    new UsernamePasswordAuthenticationToken(null, null,null));
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken token = getAuthentication(request);
        System.out.println("更新的token = " + token);
        if (null != token) {
            // 重新登记上下文,身份认证功能（对于不是permitAll）其实就是要有这个
            SecurityContextHolder.getContext().setAuthentication(token);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        System.out.println("-----------------getAuthentication()--------------------");
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:" + token);
        if (!StringUtils.isEmpty(token)) {
            String useruame = JwtHelper.getUsername(token);
            logger.info("useruame:" + useruame);
            if (!StringUtils.isEmpty(useruame)) {
                // todo
                // 从redis获取authority
                String authoritiesString = (String) redisTemplate.opsForValue().get(useruame);

                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String) map.get("authority")));
                }


                return new UsernamePasswordAuthenticationToken(
                        useruame,
                        null,
                        authorities
                );
            }
        }
        return null;
    }
}

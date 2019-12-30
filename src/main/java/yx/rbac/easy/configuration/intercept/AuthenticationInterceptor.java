package yx.rbac.easy.configuration.intercept;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;
import yx.rbac.easy.configuration.annotion.Authentication;
import yx.rbac.easy.configuration.annotion.UserLoginToken;
import yx.rbac.easy.configuration.constant.RDSConstant;
import yx.rbac.easy.configuration.entity.User;
import yx.rbac.easy.configuration.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    JedisCluster jedisCluster;

    @Value("${token.timeout}")
    private int tokenTimeout;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();

        User user = null;

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                user = tokenUser(token);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUsername() + user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
            }
        }

        //开始权限认证处理
        if(method.isAnnotationPresent(Authentication.class)){

            if(user==null){
                user = tokenUser(token);
            }

            if("ADMIN".equals(user.getUsername()))      //admin用户具有所有权限
                return true;

            Authentication per = method.getAnnotation(Authentication.class);
            String anotionId = per.id();

            Set<String> permSet = jedisCluster.smembers(RDSConstant.USER_PERMISSION_HEADER + ":" + user.getId());
            if(permSet.contains(anotionId))
                return true;
            else
                throw new RuntimeException("用户不具备访问当前接口权限！");
        }

        return true;
    }

    /**
     * 从token中获取user
     * @param token
     * @return
     */
    public User tokenUser(String token){
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId = "";
        try {

            String audience = JWT.decode(token).getAudience().get(0);
            userId = audience.split(":")[0];
            long timestamp = Long.valueOf(audience.split(":")[1]);
            long currentTime = System.currentTimeMillis();
            if((currentTime-timestamp) > tokenTimeout){
                throw new RuntimeException("token已过期!");
            }

        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }

        return user;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }

}

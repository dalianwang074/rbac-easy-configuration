package yx.rbac.easy.configuration.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yx.rbac.easy.configuration.entity.User;

@Service
public class TokenService {



    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId()+":"+System.currentTimeMillis())
                .sign(Algorithm.HMAC256(user.getUsername() + user.getPassword()));
        return token;
    }
}

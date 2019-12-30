package yx.rbac.easy.configuration.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;
import yx.rbac.easy.configuration.constant.RDSConstant;
import yx.rbac.easy.configuration.dao.UserDao;
import yx.rbac.easy.configuration.entity.User;
import yx.rbac.easy.configuration.util.MD5Util;

import java.time.LocalTime;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDao userDao;

    @Transactional
    public User findUserById(String id){
        return userDao.findOne(Long.valueOf(id));
    }

    @Transactional
    public User findByUsername(User user){
        return userDao.findByUsername(user.getUsername());
    }

    @Autowired
    JedisCluster jedisCluster;


    /**
     * 用户登录
     * @param user
     * @return
     */
    public String login(User user){
        JSONObject jsonObject=new JSONObject();
        User userForBase=findByUsername(user);
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return jsonObject.toString();
        }else {
            String loginPassword = MD5Util.strDigest(user.getPassword());
            if (!userForBase.getPassword().equals(loginPassword)){
                jsonObject.put("message","登录失败,密码错误");
                return jsonObject.toString();
            }else {
                //登录成功！
                String token = tokenService.getToken(userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                return jsonObject.toString();
            }
        }
    }

    /**
     * 从redis中获取用户所有权限
     * @param userId
     * @return
     */
    public String listUserPermissions(long userId){
        Set<String> permSet = jedisCluster.smembers(RDSConstant.USER_PERMISSION_HEADER + ":" + userId);
        JSONArray array = new JSONArray();
        permSet.stream().forEach(permission -> array.add(permission));
        return array.toString();
    }

    /**
     * 测试数据插入时间
     * 13:34:37.624
     * 13:34:38.126
     * 500条数据，共耗时  502 毫秒
     * 及格
     */
    @Transactional
    public void testAddUsers(){
        System.out.println(LocalTime.now());
        for(int i=0;i<500;i++){
            User user = new User();
            user.setUsername("g");
            user.setPassword("123456");
            userDao.save(user);
        }
        System.out.println(LocalTime.now());

    }

}

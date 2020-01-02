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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @PersistenceContext
    EntityManager entityManager;

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
     *
     * 服务器上的数据库
     * 单条插入，耗时也需要 1.5 s 秒
     * 换成批量插入，耗时也需要 1.3 s 秒
     */
    @Transactional
    public void testAddUsers1(){
        LocalTime time1 = LocalTime.now();

        List<User> list = new ArrayList<>();
        for(int i=0;i<500;i++){
            User user = new User();
            user.setUsername("g");
            user.setPassword("123456");
//            userDao.save(user);
            list.add(user);
        }

        userDao.save(list);

        LocalTime time2 = LocalTime.now();
        System.out.println(time1);
        System.out.println(time2);
    }

    /**
     * 设置配置文件 batch_versioned_data: true
     * 使用entityManager方式，
     * 最总结果均在1.3秒左右，与单条插入相差无几。
     *
     * 足以证明，jpa不需要批量插入.
     *
     * 刚刚又对比mybatis测试了一下。
     * mybatis单条插入需要2.3s
     * 但批量插入仅仅需要90ms。  mygod！
     */
    @Transactional
    public void testAddUsers2(){
        LocalTime time1 = LocalTime.now();

        List<User> list = new ArrayList<>();
        for(int i=0;i<500;i++){
            User user = new User();
            user.setUsername("g");
            user.setPassword("123456");
            list.add(user);
            entityManager.persist(user);
        }
        entityManager.flush();
        entityManager.clear();

        LocalTime time2 = LocalTime.now();
        System.out.println(time1);
        System.out.println(time2);
    }

    /**
     * 幸好hibernate支持原生sql插入方式。
     * 通过拼接数据sql实现批量插入，最终只耗时30毫秒！！！
     */
    @Transactional
    public void testAddUsers(){
        LocalTime time1 = LocalTime.now();

        StringBuilder sb = new StringBuilder(500);

        sb.append("insert into user (username,password) values ");
        sb.append("('un1','pw1')");
        for(int i = 1;i<500;i++){
            sb.append(",('un1','pw1')");
        }

        entityManager.createNativeQuery(sb.toString()).executeUpdate();

        LocalTime time2 = LocalTime.now();
        System.out.println(time1);
        System.out.println(time2);
    }

}

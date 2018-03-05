package cn.e3mail.sso.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mail.mapper.TbUserMapper;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.pojo.TbUserExample;
import cn.e3mail.pojo.TbUserExample.Criteria;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;
import jedis.JedisClient;
@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private TbUserMapper usermapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")//@value注解获取属性配置文件的属性
	private Integer SESSION_EXPIRE;
	@Override
	public E3Result userLogin(String username, String password) {
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		//根据用户名和密码查数据库
		  //根据用户查，如果没有返回用户名或密码错误
		criteria.andUsernameEqualTo(username);
		List<TbUser> user = usermapper.selectByExample(example);
		if (user==null&&user.size()==0) {
			return E3Result.build(400,"用户名或密码错误");
		}
			//如果有获取用户对象，将该用户的密码与输入的密码比对，不一致返回用户名或密码错误
		TbUser tbUser = user.get(0);
		if (!password.equals(tbUser.getPassword())) {
			return E3Result.build(400, "用户名或密码错误");
		}
		//生成token，使用uuid作为token，token相当于sessionId,返回表现层添加到cookie
		String token = UUID.randomUUID().toString();
		//将token保存到redis
			//最好不把密码带入cookie
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
		//设置token的过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		//返回token
		return E3Result.ok(token);
	}

}

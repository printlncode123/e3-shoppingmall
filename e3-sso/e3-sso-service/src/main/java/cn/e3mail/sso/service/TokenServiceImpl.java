package cn.e3mail.sso.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mail.pojo.TbUser;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;
import jedis.JedisClient;
@Service
public class TokenServiceImpl implements TokenService{
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	@Override
	public E3Result getToken(String token) {
		// 从redis中获取token
		String json = jedisClient.get("SESSION:"+token);
		//如果token为空，返回登录已过期
		if (StringUtils.isBlank(json)) {
			return E3Result.build(201, "登录的用户名已过期");
		}
			//否则根据token拿到用户信息,重新设置token的过期时间
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
			jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		return E3Result.ok(user);
	}

}

package cn.e3mail.sso.service;

import cn.e3mail.utils.E3Result;

public interface LoginService {
	//登录，登录成功生成token（sessionId）,将token作为key,用户信息作为value保存到redis
	E3Result userLogin(String username,String password);
}

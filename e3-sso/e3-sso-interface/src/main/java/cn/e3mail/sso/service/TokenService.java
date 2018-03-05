package cn.e3mail.sso.service;

import cn.e3mail.utils.E3Result;

public interface TokenService {
	E3Result getToken(String token);//此处的token是cookie中的sessionId,用uuid
}

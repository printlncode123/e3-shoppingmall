package cn.e3mail.sso.service;

import cn.e3mail.pojo.TbUser;
import cn.e3mail.utils.E3Result;

public interface RegisterService {
	E3Result checkData(String param,int type);//param:验证的数据(用户名，手机号，邮箱)，type:验证的数据类型(1:用户名，2：手机号，3：邮箱)
	E3Result register(TbUser user);
}

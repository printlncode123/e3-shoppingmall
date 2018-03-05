package cn.e3mail.sso.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mail.mapper.TbUserMapper;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.pojo.TbUserExample;
import cn.e3mail.pojo.TbUserExample.Criteria;
import cn.e3mail.utils.E3Result;
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Override
	public E3Result checkData(String param, int type) {
		//创建example
		TbUserExample example=new TbUserExample();
		//创建查询条件对象
		Criteria criteria = example.createCriteria();
		//判断数据类型1:用户名，2：手机号，3：邮箱
		if (type==1) {
			//根据用户名查询，设置查询条件
			criteria.andUsernameEqualTo(param);
		}else if (type==2) {
			//根据手机号查询，设置查询条件
			criteria.andPhoneEqualTo(param);
		}else if (type==3) {
			//根据邮箱查询，设置查询条件
			criteria.andEmailEqualTo(param);
		}else {
			//数据类型错误，返回false
			E3Result.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> obj = tbUserMapper.selectByExample(example);
		if (obj!=null&&obj.size()>0) {
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}
	public E3Result register(TbUser user) {
		//数据有效性校验
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) 
				|| StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		//1：用户名 2：手机号 3：邮箱
		E3Result result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "此用户名已经被占用");
		}
		result = checkData(user.getPhone(), 2);
		if (!(boolean)result.getData()) {
			return E3Result.build(400, "手机号已经被占用");
		}
		//补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对密码进行md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//把用户数据插入到数据库中
		tbUserMapper.insert(user);
		//返回添加成功
		return E3Result.ok();
	}

}

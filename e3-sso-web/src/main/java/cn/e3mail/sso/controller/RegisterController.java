package cn.e3mail.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.pojo.TbUser;
import cn.e3mail.sso.service.RegisterService;
import cn.e3mail.utils.E3Result;

@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	@RequestMapping("/page/register")
	public String regist(){
		return "register";
	}
	
	@ResponseBody
	@RequestMapping("/user/check/{param}/{type}")
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result checkData = registerService.checkData(param, type);
		return checkData;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user) {
		E3Result e3Result = registerService.register(user);
		return e3Result;
	}
}

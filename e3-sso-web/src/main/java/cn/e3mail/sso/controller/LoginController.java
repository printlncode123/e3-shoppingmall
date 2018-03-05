package cn.e3mail.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.sso.service.LoginService;
import cn.e3mail.utils.CookieUtils;
import cn.e3mail.utils.E3Result;
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${Token_KEY}")
	private String Token_KEY;
	
	@RequestMapping("/page/login")
	public String loginPage(String redirect,Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@ResponseBody
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public E3Result userLogin(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result result = loginService.userLogin(username, password);
		//判断是否登录成功
		if(result.getStatus()==200){
			String token = result.getData().toString();
			//将token保存到cookie
			CookieUtils.setCookie(request, response, Token_KEY, token);
			
		}
		return result;
	}
}

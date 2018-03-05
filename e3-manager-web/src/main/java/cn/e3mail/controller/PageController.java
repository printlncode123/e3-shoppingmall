package cn.e3mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	@RequestMapping("/{page}")//restful风格，配置指定格式的url，映射到对应的参数
	public String page(@PathVariable String page){
		return page;
	}
}

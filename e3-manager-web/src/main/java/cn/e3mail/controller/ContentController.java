package cn.e3mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.content.service.ContentService;
import cn.e3mail.pojo.TbContent;
import cn.e3mail.utils.E3Result;

//内容管理
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	@ResponseBody
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	public E3Result addContent(TbContent content){
		E3Result cont = contentService.addContent(content);
		return cont;
	}
}

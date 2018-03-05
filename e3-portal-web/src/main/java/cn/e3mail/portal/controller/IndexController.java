package cn.e3mail.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mail.content.service.ContentService;
import cn.e3mail.pojo.TbContent;
/**
 * 首页展示Controller
 * <p>Title: IndexController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	@Value("${CONTENT_LUNBO_ID}")//获取resource.properties文件中配置的内容分类id
	private long CONTENT_LUNBO_ID;
	@RequestMapping("/index")
	public String index(Model model){
		List<TbContent> contentsByCid = contentService.findContentByCid(CONTENT_LUNBO_ID);//内容分类id是在resource.properties文件中配置的，需要通过@value("${CONTENT_LUNBO_ID}")注解获取
		model.addAttribute("ad1List", contentsByCid);//因为需要在页面上显示数据内容所以需要model来封装查询结果
		return "index";
	}
}

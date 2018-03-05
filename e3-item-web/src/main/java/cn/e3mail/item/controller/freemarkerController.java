package cn.e3mail.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Controller
public class freemarkerController {
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@RequestMapping("/generateHtml")
	@ResponseBody
	public String generateHtml() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException{
		//获取configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板文件创建模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//设置数据集
		Map map = new HashMap();
		map.put("hello","123,456");
		//创建writer流对象设置生成的静态文件的路径
		Writer out=new FileWriter(new File("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\freemarker\\hello2.html"));
		//生成静态文件
		template.process(map, out);
		//关闭流
		out.close();
		return "ok";//通过@responeBody注解将ok字符串直接响应到浏览器客户端
	}
}

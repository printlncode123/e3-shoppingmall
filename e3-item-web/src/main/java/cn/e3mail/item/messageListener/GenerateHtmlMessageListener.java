package cn.e3mail.item.messageListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mail.item.pojo.Item;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbItemDesc;
import cn.e3mail.service.TbItemService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * 添加商品时以topic形式(广播)发送消息到activemq服务器，这里接收activemq服务器发过来的消息，根据商品id查询基本信息和描述信息，从而生成静态页面
 * <p>Title: GenerateHtmlMessageListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class GenerateHtmlMessageListener implements MessageListener{

	@Autowired
	private TbItemService tbItemService;
	@Autowired
	private FreeMarkerConfigurer configuration;
	@Value("ITME_PATH_PREFIX")//@value注解表示从属性配置文件中获取指定属性的值
	private String ITME_PATH_PREFIX;
	
	@Override
	public void onMessage(Message message) {
		try {
			//创建模板文件
			//获取消息内容（商品id）
			TextMessage textMessage=(TextMessage) message;
			String text = textMessage.getText();
			Long itemId=new Long(text);
			//等待接收消息(等待商品添加完毕：等待事务提交)
			Thread.sleep(1000);
			//根据id查询商品基本信息和描述信息，添加到数据集，一般使用map分装
			TbItem tbItem = tbItemService.findItemById(itemId);
			Item item=new Item(tbItem);
			TbItemDesc tbItemDesc = tbItemService.findItemDescByItemId(itemId);
			Map map=new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", tbItemDesc);
			//加载模板文件
			Configuration con = configuration.createConfiguration();
			Template template = con.getTemplate("item.ftl");
			//创建writer输出流对象，设置生成的静态文件的路径以及名称
			Writer out=new FileWriter(new File(ITME_PATH_PREFIX+itemId+".html"));
			//使用模板文件生成静态文件
			template.process(map, out);
			//关闭输出流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

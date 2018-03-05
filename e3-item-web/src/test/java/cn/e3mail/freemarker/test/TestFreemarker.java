package cn.e3mail.freemarker.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestFreemarker {
	@Test
	public void test() throws IOException, TemplateException{
		//创建一个模板文件
		//创建一个Configuration对象，用来加载模板文件
		Configuration configuration=new Configuration(Configuration.getVersion());
		//设置模板文件的路径
		configuration.setDirectoryForTemplateLoading(new File("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\e3-item-web\\src\\main\\webapp\\WEB-INF\\freemarker"));
		//设置编码，一般是UTF-8
		configuration.setDefaultEncoding("utf-8");
		//加载模板文件
//		Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		//设置数据集，pojo或map,推荐使用map
		Map map=new HashMap();
		map.put("hello", "hello freemarker");
		Student student=new Student(1, "张丽", 28, '女');
		map.put("student", student);
		List<Student> stuList=new ArrayList<Student>();
		stuList.add(new Student(2,"张三", 13, '男'));
		stuList.add(new Student(3,"李四", 14, '男'));
		stuList.add(new Student(4,"王五", 15, '男'));
		stuList.add(new Student(5,"赵六", 16, '男'));
		stuList.add(new Student(6,"孙七", 17, '男'));
		map.put("stuList", stuList);//数据是集合
		map.put("date", new Date());//数据是日期类型
		map.put("val", null);//如果是空值
		map.put("pk", 111);//如果是不是空值
		//创建writer对象，设置生成的静态页面的路径
//		Writer out = new FileWriter(new File("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\freemarker\\hello.txt"));
		Writer out = new FileWriter(new File("E:\\e3_eclipse-mars_workspace\\JAVAEE32\\freemarker\\student.html"));
		//生成静态资源
		template.process(map, out);
		//关闭
		out.close();
	}
}

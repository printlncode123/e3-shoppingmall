import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mail.mapper.TbItemMapper;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbItemExample;

public class PageHelperTest {
	@Test
	public void test(){
		//从spring容器中获取要分页的对象的mapper进行查询
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		//查询前设置分页信息
		PageHelper.startPage(1, 30);
		TbItemExample example=new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);//查询的list结果集
		PageInfo<TbItem> info=new PageInfo<>(list);
		System.out.println(info.getTotal());//总记录
		System.out.println(info.getPageSize());
		System.out.println(info.getSize());
	}
}

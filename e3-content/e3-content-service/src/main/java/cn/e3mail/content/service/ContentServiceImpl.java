package cn.e3mail.content.service;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.e3mail.mapper.TbContentMapper;
import cn.e3mail.pojo.TbContent;
import cn.e3mail.pojo.TbContentExample;
import cn.e3mail.pojo.TbContentExample.Criteria;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;
import jedis.JedisClient;

/**
 * 内容管理
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService{
	@Value("${CONTENT_LIST}")//通过@value注解获取resource.properties文件中配置的CONTENT_LIST
	private String CONTENT_LIST;
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	//增删改数据库时实现缓存同步(增删改数据库时缓存中数据是不发生变化的，此时要做到缓存同步需将缓存中数据删除，缓存数据为空就会去查数据库重新添加到缓存)
	@Override
	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		tbContentMapper.insert(content);
		//缓存同步(删除缓存中之前的数据去查数据库重新加载到缓存)
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok(content);//返回的是该对象，里面有id,status,pojo
	}
	
	//根据内容分类id查询内容
	//添加缓存（如果缓存中没有数据就去查询数据库）
	@Override
	public List<TbContent> findContentByCid(long cid) { 
		try {
			//先去查缓存
			String c = jedisClient.hget(CONTENT_LIST, cid+"");
			if (StringUtils.isNotBlank(c)) {
				//将给字符串转为Java对象
				List<TbContent> jsonToList = JsonUtils.jsonToList(c, TbContent.class);
				return jsonToList;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();//创建条件对象
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> contents = tbContentMapper.selectByExampleWithBLOBs(example);
		try{
		//因为redis的存储类型都是字符串，所以得将contents转为json串
		String json = JsonUtils.objectToJson(contents);
		jedisClient.hset(CONTENT_LIST, cid+"", json);}
		catch(Exception e){
			e.printStackTrace();
		}
		return contents;
	}

}

package cn.e3mail.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mail.common.EasyUIDatagridResult;
import cn.e3mail.mapper.TbItemDescMapper;
import cn.e3mail.mapper.TbItemMapper;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbItemDesc;
import cn.e3mail.pojo.TbItemExample;
import cn.e3mail.pojo.TbItemExample.Criteria;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.IDUtils;
import cn.e3mail.utils.JsonUtils;
import jedis.JedisClient;

/**
 * <p>Title: TbItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 * 根据商品id查询商品信息
 */
@Service
public class TbItemServiceImpl implements TbItemService {
	
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")//获取属性配置文件里的属性
	private String ITEM_IFNO;
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;
	
	//查询商品，为了高并发访问量设置缓存：先查缓存，没有去查数据库。当不常访问的商品占用缓存时可以设置过期时间，一定时间后缓存失效
	@Override
	public TbItem findItemById(long id) {
		//先查缓存
		try{
			String json = jedisClient.get(ITEM_IFNO+":"+id+":BASE");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			return tbItem;
		}catch(Exception e){
			e.printStackTrace();
		}
		//缓存无则查数据库，将查到的结果添加到缓存
		//TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);//根据主键查询
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();//创建查询条件对象
		criteria.andIdEqualTo(id);//设置查询条件
		//执行查询
		List<TbItem> list = tbItemMapper.selectByExample(example);//根据条件查询
		try {
			//添加缓存
			jedisClient.set(ITEM_IFNO+":"+id+":BASE", JsonUtils.objectToJson(list.get(0)));
			//设置缓存过期时间，不设置该商品将永久保存在缓存中
			jedisClient.expire(ITEM_IFNO+":"+id+":BASE", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//判空
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public EasyUIDatagridResult findItemListPage(int page,int rows) {//当前页，每页显示的记录数
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//封装结果集到EasyUIDatagridResult
		EasyUIDatagridResult result=new EasyUIDatagridResult();
		result.setRows(list);//设置每页显示的记录
		PageInfo<TbItem> info=new PageInfo<>(list);
		long total = info.getTotal();//总记录数
		result.setTotal(total);//设置总记录数
		return result;
	}
	@Override
	public E3Result addItem(TbItem item, String desc) {
		//创建商品id
		final long itemId = IDUtils.genItemId();
		//将商品id设置到item方法参数中
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//调用tbitemmapper的insert()添加到数据库
		tbItemMapper.insert(item);
		//-----------------------商品表
		
		//创建商品描述对象
		TbItemDesc itemDesc=new TbItemDesc();
		//设置商品详情描述
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//执行insert()添加到数据库
		tbItemDescMapper.insert(itemDesc);
		//----------------------商品描述表
		
		//发送消息，需注入JMSTemplate,topicDestination
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");//将商品id作为消息发送
				return textMessage;
			}
		});
		return E3Result.ok();
	}
	@Override
	public TbItemDesc findItemDescByItemId(long itemId) {
		try {
			//先查询缓存
			String json = jedisClient.get(ITEM_IFNO+":"+itemId+":DESC");
			TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
			return tbItemDesc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			//将查询结果添加到缓存
			jedisClient.set(ITEM_IFNO+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDesc));
			//设置过期时间
			jedisClient.expire(ITEM_IFNO+":"+itemId+":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}

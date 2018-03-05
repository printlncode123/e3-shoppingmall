package cn.e3mail.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mail.mapper.TbItemMapper;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;
import jedis.JedisClient;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;
	@Override
	public E3Result addCart(long userId, long itemId, Integer num) {
		// 从redis中获取(key:userId,field:itemId,value:商品)
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
			//存在，转json，设置商品数量,直接增加，存到redis//返回成功
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
			//不存在,添加商品,设置数量，存到redis//返回成功
		      //根据id查询指定商品
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		item.setNum(num);
		String image = item.getImage();
		if (StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		return E3Result.ok();
	}
	@Override
	public E3Result merge(long userId, List<TbItem> itemList) {
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		
		return E3Result.ok();
	}
	@Override
	public List<TbItem> findByRedis(long userId) {
		List<String> list=jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> items=new ArrayList<TbItem>();
		for (String json : list) {
			TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
			items.add(tbItem);
		}
		
		return items;
	}
	@Override
	public E3Result updateCart(long userId, long itemId, int num) {
		//从redis中取商品信息
				String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
				//更新商品数量
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				tbItem.setNum(num);
				//写入redis
				jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
				return E3Result.ok();
	}
	@Override
	public E3Result deleteCart(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}
	@Override
	public E3Result cleanCart(long userId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}

}

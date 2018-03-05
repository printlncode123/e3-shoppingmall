package cn.e3mail.order.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.e3mail.mapper.TbOrderItemMapper;
import cn.e3mail.mapper.TbOrderMapper;
import cn.e3mail.mapper.TbOrderShippingMapper;
import cn.e3mail.order.pojo.OrderInfo;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbOrderItem;
import cn.e3mail.pojo.TbOrderShipping;
import cn.e3mail.utils.E3Result;
import jedis.JedisClient;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private  JedisClient jedisClient;
	@Value("${ORDER_GEN_KEY}")//获取属性文件中的属性值
	private String ORDER_GEN_KEY;
	@Value("${ORDER_ID_DEF}")
	private int ORDER_ID_DEF;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	//生成订单
	@Override
	public E3Result generateOrder(OrderInfo orderInfo) {
		// 订单表
			//生成订单ID，使用redis的incr自增长（redis高性能，在秒杀提交订单情况下出现高并发使用redis生成订单id）
		if (!jedisClient.exists(ORDER_GEN_KEY)) {
			jedisClient.set(ORDER_GEN_KEY, ORDER_GEN_KEY);//设置起始值
		}
		String orderID=jedisClient.incr(ORDER_GEN_KEY).toString();//在redis中自增
			//补全属性
		orderInfo.setOrderId(orderID);
		orderInfo.setCreateTime(new Date());
		orderInfo.setStatus(1);
		orderInfo.setUpdateTime(new Date());
			//调用mapper的insert()
		orderMapper.insert(orderInfo);
		// 订单明细表
			//补全属性
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem orderItem : orderItems) {
			//生成订单明细ID，使用redis的incr
			String orderItemID = jedisClient.incr(ORDER_GEN_KEY).toString();
			orderItem.setId(orderItemID);
			orderItem.setOrderId(orderItemID);
			//调用mapper的insert（）
			orderItemMapper.insert(orderItem);
		}
		// 订单物流表
			//补全属性
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderID);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
			//调用mapper的insert（）
		orderShippingMapper.insert(orderShipping);
		return E3Result.ok(orderID);
	}

}

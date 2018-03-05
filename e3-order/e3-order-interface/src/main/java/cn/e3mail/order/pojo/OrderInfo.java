package cn.e3mail.order.pojo;

import java.io.Serializable;
import java.util.List;

import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbOrder;
import cn.e3mail.pojo.TbOrderItem;
import cn.e3mail.pojo.TbOrderShipping;
/**
 * 订单的扩展类
 * <p>Title: OrderInfo</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class OrderInfo extends TbOrder implements Serializable {
	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
	
}

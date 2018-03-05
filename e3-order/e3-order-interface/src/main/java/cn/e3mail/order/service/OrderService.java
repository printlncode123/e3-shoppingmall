package cn.e3mail.order.service;

import cn.e3mail.order.pojo.OrderInfo;
import cn.e3mail.utils.E3Result;

public interface OrderService {
	E3Result generateOrder(OrderInfo orderInfo);
}

package cn.e3mail.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.e3mail.cart.service.CartService;
import cn.e3mail.order.pojo.OrderInfo;
import cn.e3mail.order.service.OrderService;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.sso.service.TokenService;
import cn.e3mail.utils.CookieUtils;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;

@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	//展示订单确认页面
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request){
		//通过拦截器拦截(判断是否登录),从request中获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//根据用户id获取购物车商品列表
			/*在拦截器中判断//如果是未登录情况下添加购物车结算的话，先要将cookie中的购物车商品列表合并到redis中，在从redis中一并查询
		String json = CookieUtils.getCookieValue(request, "cart", true);
		cartService.merge(user.getId(), JsonUtils.jsonToList(json, TbItem.class));*/
		List<TbItem> itemList = cartService.findByRedis(user.getId());
		//将购物车商品列表返回到页面
		request.setAttribute("cartList", itemList);
		//返回订单确认页面
		return "order-car";
	}
	
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(HttpServletRequest request,OrderInfo orderInfo){
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		E3Result result = orderService.generateOrder(orderInfo);
		//如果订单生成成功需要删除购物车
		if (result.getStatus()==200) {
			cartService.cleanCart(user.getId());
		}
		String orderID = (String) result.getData();
		request.setAttribute("orderId", orderID);
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}
}

package cn.e3mail.cart.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mail.cart.service.CartService;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.service.ItemCatService;
import cn.e3mail.service.TbItemService;
import cn.e3mail.utils.CookieUtils;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;

@Controller
public class CartController {
	@Autowired
	private TbItemService itemService;
	@Autowired
	private CartService cartService;
	@Value("COOKIE_CART_EXPIRE")
	private int COOKIE_CART_EXPIRE;
	@RequestMapping("/cart/add/{itemId}")//"http://localhost:8090/cart/add/"+product_id+".html?num=" + num;
	public String addToCart(@PathVariable Long ItemId,@RequestParam(defaultValue="1") Integer num,HttpServletRequest request,HttpServletResponse response){
		//登录状态下
		TbUser user = (TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.addCart(user.getId(), ItemId, num);
			return "cartSuccess";
		}
		
		//未登录
		//从cookie中获取购物车中商品列表信息(json串)
			//如果为空直接返回空集合
			//否则转成pojo，遍历，获取商品id与新增商品id比对
		List<TbItem> cartCookie = getCartCookie(request);
		boolean flag=false;//标记
		for (TbItem tbItem : cartCookie) {
			//如果id一致，直接增加数量
			if (tbItem.getId()==ItemId.longValue()) {
				tbItem.setNum(tbItem.getNum()+num);
				flag=true;
				break;
			}
		}
		//否则根据id查询商品信息，将该商品存入cookie,并设置过期时间
		if (!flag) {
			TbItem item = itemService.findItemById(ItemId);
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			cartCookie.add(item);
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartCookie), COOKIE_CART_EXPIRE,true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	//获取cookie中购物车商品信息
	public List<TbItem> getCartCookie(HttpServletRequest request){
		String cookieValue = CookieUtils.getCookieValue(request, "cart", true);//true表示转码，因为购物车中商品信息会有中文，防止出现乱码
		if (StringUtils.isBlank(cookieValue)) {
			return new ArrayList<TbItem>();
		}
		List<TbItem> list = JsonUtils.jsonToList(cookieValue, TbItem.class);
		return list;
	}
	
	//展示购物车商品
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response){
		//拿到cookie里的购物车
		List<TbItem> list = getCartCookie(request);
		//获取登录的用户信息
		TbUser user=(TbUser) request.getAttribute("user");
		if (user!=null) {
			//合并cookie和redis
			cartService.merge(user.getId(), list);
			//删除浏览器中的cookie
			CookieUtils.deleteCookie(request, response, "cart");
			//从redis中查询购物车(合并后的)
			list = cartService.findByRedis(user.getId());
		}
		request.setAttribute("cartList", list);
		return "cart";
	}
	
	//更新购物车商品数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")//"/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val()
	public E3Result updateCart(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从cookie中获取购物车商品信息
		List<TbItem> cartCookie = getCartCookie(request);
		TbUser user = (TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.updateCart(user.getId(), itemId, num);
			return E3Result.ok();
		}
		//修改购物车数量
		for (TbItem tbItem : cartCookie) {
			if (tbItem.getId()==itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		//保存到cookie
		CookieUtils.setCookie(request, response, "cart",JsonUtils.objectToJson(cartCookie), COOKIE_CART_EXPIRE, true);
		//返回成功
		return E3Result.ok();
	}
	
	//删除购物车商品信息
	@RequestMapping("/cart/delete/{itemId}")///cart/delete/${cart.id}.html
	public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		//从cookie中获取购物车商品列表
		List<TbItem> cartCookie = getCartCookie(request);
		TbUser user = (TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		for (TbItem tbItem : cartCookie) {
			if (tbItem.getId()==itemId.longValue()) {
				//根据商品id获取指定商品信息
				//从cookie中删除该商品信息
				cartCookie.remove(tbItem);
				break;
			}
		}
		//重新设置到cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartCookie), COOKIE_CART_EXPIRE, true);
		//返回页面（重定向到/cart/cart.html,地址栏发生变化）
		return "redirect:/cart/cart.html";
	}
}

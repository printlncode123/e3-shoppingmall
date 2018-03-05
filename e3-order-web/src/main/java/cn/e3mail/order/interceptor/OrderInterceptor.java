package cn.e3mail.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mail.cart.service.CartService;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.sso.service.TokenService;
import cn.e3mail.utils.CookieUtils;
import cn.e3mail.utils.E3Result;
import cn.e3mail.utils.JsonUtils;
 
public class OrderInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	@Override//handler(controller中的方法)执行之前
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 获取cookie中的token（uuid）
		String token = CookieUtils.getCookieValue(request, "token", true);
		//token不存在，未登录
		if (StringUtils.isBlank(token)) {
			//拦截，跳转到登录页面，需要传递参数使登录后再跳转到当前页面
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
			
		//token存在,调用sso服务获取token对应的内容
		E3Result result = tokenService.getToken(token);
			//判断用户是否存在
				//不存在，登录过期，拦截，需重新登录（跳转到登录页面，需要传递参数使登录后再跳转到当前页面）
		if (result.getStatus()!=200) {
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
			    //存在，登录状态，获取当前用户，保存到request中
		TbUser user=(TbUser) result.getData();
		request.setAttribute("user",user);
					//判断cookie中是否有购物车列表，有则合并到服务端redis中
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNotBlank(json)) {
			cartService.merge(user.getId(), JsonUtils.jsonToList(json,TbItem.class));
		}
					//放行
		return true;
	}

	@Override//handler执行之后，modelandview返回之前
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override//modelandview返回之后
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}

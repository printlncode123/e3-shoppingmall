package cn.e3mail.cart.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import cn.e3mail.pojo.TbUser;
import cn.e3mail.sso.service.TokenService;
import cn.e3mail.utils.CookieUtils;
import cn.e3mail.utils.E3Result;

public class LoginInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// handler执行之前
		//从cookie取出token
		String token = CookieUtils.getCookieValue(request, "token");
		//没有取到token,未登录，直接放行
		if (StringUtils.isBlank(token)) {
			return true;//直接放行//return false;拦截
		}
		//取到token，获取用户信息
			//调用sso服务获取用户
		E3Result result = tokenService.getToken(token);
		//未取到用户信息，登录超时，直接放行
		if (result.getStatus()!=200) {
			return true;
		}
		//取到用户信息，登录状态
		TbUser user = (TbUser) result.getData();
			//将取到的用户信息存到request,这里的request就是controller中的request,只要判断controller中request是否有user。放行
		request.setAttribute("user", user);
		return true; 
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，modelandview执行之前
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// modelandview执行之后
		
	}

}

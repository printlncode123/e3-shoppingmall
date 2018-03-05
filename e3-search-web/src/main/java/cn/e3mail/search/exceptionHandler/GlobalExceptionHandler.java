package cn.e3mail.search.exceptionHandler;

import java.beans.ExceptionListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//全局异常处理器
//当service层将异常抛给表现层，表现层不处理继续往上抛会抛给springmvc框架，springmvc框架会找全局异常处理器进行处理
public class GlobalExceptionHandler implements HandlerExceptionResolver{
	Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//打印栈信息
		ex.printStackTrace();
		//写日志
		logger.debug("测试出现异常。。。");
		logger.info("系统出现异常了");
		logger.trace("系统出现异常", ex);
		//发送邮件和短信
		//使用jmail发送邮件，使用第三方工具webservice发送短信
		//显示友好页面
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}

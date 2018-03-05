package cn.e3mail.activemqConsumer.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActivemqSpring {
	@Test
	public void test() throws IOException{
		//初始化spring ioc容器
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring/applicationContext-activemqConsumer");
		//等待接收消息
		System.in.read();//不回车控制台就会一直响应等待接收消息
	}
}

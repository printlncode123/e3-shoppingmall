package com.redis.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jedis.JedisClient;
import jedis.JedisClientPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedisClient {
	@Test
	public void test(){
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("testabc", "aaaa");
		System.out.println(jedisClient.get("testabc"));
		
	}
}

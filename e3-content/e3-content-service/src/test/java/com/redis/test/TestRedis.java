package com.redis.test;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
public class TestRedis {
	@Test
	public void test(){
		//创建一个jedis连接对象
		Jedis jedis=new Jedis("192.168.25.130",6379);
		//通过jedis操作redis库(nosql,作为缓存库)
		jedis.set("first operation", "very good");
		String s = jedis.get("first operation");
		System.out.println(s);
		//关闭连接
		jedis.close();
	}
	
	//通过连接获取jedis，操作redis库，降低频繁创建连接对象（耗资源）的压力
	@Test
	public void testByPool(){
		//创建连接池对象
		JedisPool jedisPool=new JedisPool("192.168.25.130", 6379);
		//从连接池中取出一个jedis连接对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis连接对象对redis库进行操作
		String s = jedis.get("first operation");
		System.out.println(s);
		//关闭jedis连接
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	//使用jediscluster连接redis集群
	@Test
	public void testByJedsiCluster(){
		//创建jedisCluster对象，参数是nodes,他是一个set类型泛型是HostAndPost
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.130", 7001));
		nodes.add(new HostAndPort("192.168.25.130", 7002));
		nodes.add(new HostAndPort("192.168.25.130", 7003));
		nodes.add(new HostAndPort("192.168.25.130", 7004));
		nodes.add(new HostAndPort("192.168.25.130", 7005));
		nodes.add(new HostAndPort("192.168.25.130", 7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		//通过jediscluster对象操作Redis库
		jedisCluster.set("test", "a?b*c+_/");
		System.out.println(jedisCluster.get("test"));
		//关闭jedisCluster连接
		jedisCluster.close();
	}
}

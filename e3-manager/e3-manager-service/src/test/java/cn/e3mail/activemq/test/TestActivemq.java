package cn.e3mail.activemq.test;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
//测试使用activemq代理发送消息==生产者(消息发送)通知activemq(代理)将消息发给消费者(消息接收方)  
//quene点对点的形式发送消息，如果没有消息接收方，消息会保存到mq服务端
public class TestActivemq {
	//发送消息
	@Test
	public void testQueneProducer() throws JMSException{
		//需要依赖activemq的jar包
		//创建连接工厂对象，需要activemq的地址ip:端口
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建session会话对象，用于创建生产对象，目标对象，消息对象
		  //有两个参数
		    //第一个参数,是否开启事务，如果开启表示消息发送失败重新发送，该事务一般用于分布式事务，开启第二个参数就无意义。一般不开启(false)
			//第二个参数:表示应答模式。自动应答和手动应答。自动应答：接收消息后自动连服务。手动应答：接收消息后需提供程序连服务
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//不开启事务，自动应答
		//使用session创建destinatin对象,有两种：quene（点对点），topic(一对多，广播)
		Queue queue = session.createQueue("test-quene");//起名字
		//使用session创建生产者即消息提供方
		MessageProducer producer = session.createProducer(queue);
		//使用session创建消息对象
		TextMessage textMessage = session.createTextMessage("text message");
		//发送消息
		producer.send(textMessage);
		//关闭各种连接(生产者session,connection对象)
		producer.close();
		session.close();
		connection.close();
	}
	
	
	//接收消息
		@Test
		public void testQueneConsumer() throws JMSException, IOException{
			//创建工厂连接对象，连接activemq服务
			ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
			//创建连接对象
			Connection connection = connectionFactory.createConnection();
			//开启连接
			connection.start();
			//创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//不开启事务，自动应答
			//创建destination（quene:点到点），因为要从quene接收消息
			Queue queue = session.createQueue("test-quene");
			//e创建消费者
			MessageConsumer consumer = session.createConsumer(queue);
			//设置监听器监听消息
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					TextMessage textmessage=(TextMessage) message;
					try {
						String text = textmessage.getText();
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			//等待接收消息,控制台不敲回车就一直等待
			System.in.read();
			//关闭各种连接
			consumer.close();
			session.close();
			connection.close();
		}

		//topic广播的形式发送消息，如果没有消息接收方，消息丢失
		@Test
		public void testTopicProducer() throws JMSException{
			//创建工厂连接对象，需要ip:端口
			ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
			//创建连接对象
			Connection connection = connectionFactory.createConnection();
			//开启连接
			connection.start();
			//创建session对象
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//创建destination对象，这里用topic
			Topic topic = session.createTopic("test-topic");
			//创建生产者
			MessageProducer producer = session.createProducer(topic);
			//创建消息对象
			TextMessage textMessage = session.createTextMessage("topic send");
			//发送消息
			producer.send(textMessage);
			//关闭各种连接
			producer.close();
			session.close();
			connection.close();
		}
		
		
		//接收消息
		@Test
		public void testTopicConsumer() throws JMSException, IOException{
			//创建工厂连接对象，连接activemq服务
			ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
			//创建连接对象
			Connection connection = connectionFactory.createConnection();
			//开启连接
			connection.start();
			//创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//不开启事务，自动应答
			//创建destination（quene:点到点），因为要从quene接收消息
			Topic topic = session.createTopic("test-topic");
			//e创建消费者
			MessageConsumer consumer = session.createConsumer(topic);
			//设置监听器监听消息
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					TextMessage textmessage=(TextMessage) message;
					try {
						String text = textmessage.getText();
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			System.out.println("消费者3启动");
			//等待接收消息,控制台不敲回车就一直等待
			System.in.read();
			//关闭各种连接
			consumer.close();
			session.close();
			connection.close();
		}
		
			
}

package cn.e3mail.search.activemqMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener{

	//接收到的消息
	@Override
	public void onMessage(Message arg0) {
		TextMessage message=(TextMessage) arg0;
		//取出消息的内容
		try {
			String text = message.getText();
			System.out.println(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}

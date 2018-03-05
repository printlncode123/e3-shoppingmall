package cn.e3mail.search.activemqMessageListener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mail.common.SearchItem;
import cn.e3mail.search.mapper.ItemMapper;
//接收到的添加商品服务发送的消息
public class AddItemMessageListener implements MessageListener{
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage = (TextMessage) message;
			//获取消息内容//从消息中获取id
			String text = textMessage.getText();
			//强转
			Long itemId=new Long(text);
			//等待接收消息
			Thread.sleep(1000);//因为添加商品事务没提交就发送了消息，再次等待1秒等那边事务提交了在接收消息查数据库存到索引库
			//根据id查询商品
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//创建文档对象
			SolrInputDocument document=new SolrInputDocument();
			//添加文档域
			document.addField("id", itemId);
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCatagory_name());
			//将文档添加到索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

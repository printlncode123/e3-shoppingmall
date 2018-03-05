package cn.e3mail.solr.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * 测试solr集群(solrCloud)的操作
 * <p>Title: TestSolrCloud</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class TestSolrCloud {
	@Test
	public void add() throws SolrServerException, IOException{
		//创建集群连接对象
		CloudSolrServer cloudSolrServer=new CloudSolrServer("192.168.25.129:2182");
		//设置默认使用的collection(collection表示一整个索引库)
		cloudSolrServer.setDefaultCollection("collection2");
		//创建文档对象
		SolrInputDocument document=new SolrInputDocument();
		//设置文档中域
		document.setField("id", "solrCloud1");
		document.setField("item_title", "书籍");
		//将文档添加到索引库中
		cloudSolrServer.add(document);
		//提交
		cloudSolrServer.commit();
	}
	
	//查询索引库
	@Test
	public void testQuery() throws SolrServerException{
		//创建连接对象，solrCloud由zookeeper管理
		CloudSolrServer cloudSolrServer=new CloudSolrServer("192.168.25.129:2181");
		//设置默认用的collection
		cloudSolrServer.setDefaultCollection("collection2");
		//创建查询对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询信息
		solrQuery.set("q", "id");
		solrQuery.set("df", "item_title");
		//执行查询
		QueryResponse queryResponse = cloudSolrServer.query(solrQuery);
		SolrDocumentList results = queryResponse.getResults();
		//遍历打印
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}
	} 
}

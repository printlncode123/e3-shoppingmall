package cn.e3mail.solr.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	@Test
	//将文档添加到索引库
	public void add() throws SolrServerException, IOException{
		//solrJ客户端连接solr服务(创建一个solrServer连接对象)
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建文档对象(solrinputdocument)
		SolrInputDocument solrInputDocument=new SolrInputDocument();
		//将域添加到文档中，域需要在schema.xml中定义,文档中必须有id域
		solrInputDocument.addField("id", "doc1");
		solrInputDocument.addField("title", "三星手机优惠");
		solrInputDocument.addField("price","3000");
		//将文档添加到索引库
		solrServer.add(solrInputDocument);
		//提交
		solrServer.commit();
	}
	
	//将文档从索引库删除
	@Test
	public void delete() throws SolrServerException, IOException{
		//创建solrserver连接对象连接solr服务
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//删除操作
		//solrServer.deleteById("doc1");//根据文档id删除
		solrServer.deleteByQuery("id:doc1");//根据条件删
		solrServer.commit();
	}
	
	
	//简单查询
	@Test
	public void query() throws SolrServerException{
		//创建solrserver连接对象，连接solr服务
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建solrQuery查询对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		//solrQuery.setQuery("*:*");
		solrQuery.set("q", "*:*");//查询所有
		//执行查询，返回QueryResponse对象
		QueryResponse query = solrServer.query(solrQuery);
		//获取结果集:
		SolrDocumentList results = query.getResults();
		//打印一共多少条记录
		long numFound = results.getNumFound();
		System.out.println(numFound);
		//遍历取出文档列表的每个
		for (SolrDocument doc : results) {
			System.out.println(doc.get("id"));
			System.out.println(doc.get("item_title"));
			System.out.println(doc.get("item_sell_point"));
			System.out.println(doc.get("item_price"));
			System.out.println(doc.get("item_image"));
			System.out.println(doc.get("item_category_name"));
		}
	}
	
	//复杂查询
	@Test
	public void testQueryComplex() throws SolrServerException{
		//创建连接对象连接solr服务
		SolrServer solrServer=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建查询对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询信息
		solrQuery.set("q", "手机");
		//设置默认查询返回，默认查询哪些域
		solrQuery.set("df", "item_title");
		//设置分页查询
		solrQuery.setStart(0);
		solrQuery.setRows(10);
		//设置高亮显示
		solrQuery.setHighlight(true);//开启高亮
		solrQuery.setHighlightSimplePre("<em>");//设置高亮前缀
		solrQuery.setHighlightSimplePost("</em>");//设置高亮后缀
		solrQuery.addHighlightField("item_title");//设置高亮显示的域
		//执行查询		//返回QueryResponse对象
		QueryResponse query = solrServer.query(solrQuery);
		//获取结果集
		SolrDocumentList results = query.getResults();
		//循环遍历，文档列表和高亮显示是分开的
		long numFound = results.getNumFound();
		System.out.println(numFound);
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();//获取高亮结果集
		for (SolrDocument doc : results) {
			Map<String, List<String>> map = highlighting.get("id");//根据id拿到高亮的 map
			List<String> list = highlighting.get(doc.get("id")).get("item_title");
			String title="";
			if (list!=null&&list.size()>0) {
				title= list.get(0);
			}else{
				title=(String) doc.get("item_title");
			}
			
			System.out.println(doc.get("id"));
			//System.out.println(title);
			System.out.println(doc.get("item_sell_point"));
			System.out.println(doc.get("item_price"));
			System.out.println(doc.get("item_image"));
			System.out.println(doc.get("item_category_name"));
		}
	}
}

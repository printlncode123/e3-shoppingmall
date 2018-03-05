package cn.e3mail.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sun.tools.doclets.internal.toolkit.resources.doclets;

import cn.e3mail.common.SearchItem;
import cn.e3mail.common.SearchResult;

@Repository
public class SearchDAO {
	@Autowired
	private SolrServer solrServer;//由spring ioc容器创建管理
	public SearchResult findSearchItems(SolrQuery solrQuery) throws SolrServerException{//根据条件查询
		//执行查询
		QueryResponse query = solrServer.query(solrQuery);
		//获取结果集
		SolrDocumentList results = query.getResults();
		//获取总记录数
		long numFound = results.getNumFound();
		//获取docs和高亮
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
		List<SearchItem> searchItems=new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : results) {
			SearchItem searchItem=new SearchItem();
			//将doc域值设置到searchItem对象中			
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCatagory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
		
			String title="";
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> list = map.get("item_title");
			if (list!=null&&list.size()>0) {
				title=list.get(0);
			}else{
				title=(String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			searchItems.add(searchItem);
		}
		SearchResult r=new SearchResult();
		r.setItems(searchItems);
		r.setQuery(numFound);
		return r;
	}
}

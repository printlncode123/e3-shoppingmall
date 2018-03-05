package cn.e3mail.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mail.common.SearchResult;
import cn.e3mail.search.dao.SearchDAO;

@Service
public class SearchServiceImpl implements SearchService{
	@Autowired
	private SearchDAO searchDAO;
	@Override
	public SearchResult findByQuery(String keywords, int page, int rows) throws SolrServerException {
		//创建solrquery对象
		SolrQuery solrQuery=new SolrQuery(); 
		//设置查询信息
		solrQuery.set("q", keywords);
		//设置默认查询域信息
		solrQuery.set("df", "item_title");
		//设置分页
		if (page<=0)page=1;
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		//设置高亮
		solrQuery.setHighlight(true);//开启高亮
		solrQuery.addHighlightField("item_title");//设置高亮域
		solrQuery.setHighlightSimplePre("<em style='color:red'>");//设置高亮前缀
		solrQuery.setHighlightSimplePost("</em>");//设置高亮后缀
		//执行查询
		SearchResult searchResult = searchDAO.findSearchItems(solrQuery);
		//取出总记录数，根据总记录数和每页显示的记录数求总页数
		long queryCounts = searchResult.getQuery();
		int totalPages = (int) Math.ceil(queryCounts/rows);
		//将总页数设置到searchresult
		searchResult.setTotalPages(totalPages);
		//返回结果
		return searchResult;
	}

}

package cn.e3mail.search.service;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mail.common.SearchItem;
import cn.e3mail.search.mapper.ItemMapper;
import cn.e3mail.utils.E3Result;
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;//由spring ioc 容器创建管理
	
	//将查询的商品添加到索引库
	@Override
	public E3Result addItemToSolr() {
		
			try {
				//查询商品
				List<SearchItem> itemList = itemMapper.findItemList();
				//遍历商品
				for (SearchItem item : itemList) {
					//创建文档对象
					SolrInputDocument solrInputDocument=new SolrInputDocument();
					//将商品信息作为域添加到文档中
					solrInputDocument.addField("id", item.getId());
					solrInputDocument.addField("item_title", item.getTitle());
					solrInputDocument.addField("item_sell_point", item.getSell_point());
					solrInputDocument.addField("item_price", item.getPrice());
					solrInputDocument.addField("item_image", item.getImage());
					solrInputDocument.addField("item_category_name", item.getCatagory_name());
					//将文档添加到索引库中
				solrServer.add(solrInputDocument);
				}
				//提交
				solrServer.commit();
				//返回成功
				return E3Result.ok();
			} catch (Exception e) {
				e.printStackTrace();
				return E3Result.build(1, "倒入数据到索引库失败");
			}
		}
}

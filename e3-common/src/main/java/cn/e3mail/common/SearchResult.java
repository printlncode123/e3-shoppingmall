package cn.e3mail.common;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private int totalPages;
	private long query;//总记录数
	private List<SearchItem> items;//每页显示的记录
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getQuery() {
		return query;
	}
	public void setQuery(long query) {
		this.query = query;
	}
	public List<SearchItem> getItems() {
		return items;
	}
	public void setItems(List<SearchItem> items) {
		this.items = items;
	}
	
}

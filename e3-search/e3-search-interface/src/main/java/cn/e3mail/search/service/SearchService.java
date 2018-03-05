package cn.e3mail.search.service;

import cn.e3mail.common.SearchResult;

public interface SearchService {
	SearchResult findByQuery(String keywords,int page,int rows) throws Exception;
}

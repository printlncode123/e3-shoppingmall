package cn.e3mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.search.service.SearchItemService;
import cn.e3mail.utils.E3Result;

@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	@ResponseBody
	@RequestMapping("/index/item/import")
	public E3Result importToSolrLib(){
		E3Result e3Result = searchItemService.addItemToSolr();
		return e3Result;
	}
}

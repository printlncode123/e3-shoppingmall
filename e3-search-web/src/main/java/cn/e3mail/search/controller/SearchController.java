package cn.e3mail.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mail.common.SearchResult;
import cn.e3mail.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@Value("${ITEM_ROWS}")//@value注解用来获取属性文件中配置的属性值
	private int ITEM_ROWS;
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception{
		//get请求时，keyword是中文时会出现乱码，需要zhuanma
		keyword=new String(keyword.getBytes("iso-8859-1"), "UTF-8");
		SearchResult result = searchService.findByQuery(keyword, page, ITEM_ROWS);
		//要把结果返回到页面，所以需要model
		model.addAttribute("query", keyword);//查询条件
		model.addAttribute("totalPages", result.getTotalPages());//总页数
		model.addAttribute("page", page);//当前页
		model.addAttribute("recourdCount",result.getQuery());//总记录数
		model.addAttribute("itemList", result.getItems());//每页显示的记录
		return "search";//返回到页面
	}
}

package cn.e3mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.common.EasyUIDatagridResult;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.service.TbItemService;
import cn.e3mail.utils.E3Result;

@Controller
public class TbItemController {
	@Autowired
	private TbItemService tbItemServiceImpl;
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem geTbItem(@PathVariable long itemId){
		TbItem tbItem = tbItemServiceImpl.findItemById(itemId);
		return tbItem;
	}
	
	//分页查询商品
	@ResponseBody
	@RequestMapping("/item/list")
	public EasyUIDatagridResult list(Integer page,Integer rows){
		EasyUIDatagridResult result = tbItemServiceImpl.findItemListPage(page, rows);
		return result;
	}
	
	//商品添加
	@ResponseBody
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	public E3Result addItem(TbItem item,String desc){
		E3Result result = tbItemServiceImpl.addItem(item, desc);
		return result;
		
	}
}

package cn.e3mail.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mail.item.pojo.Item;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbItemDesc;
import cn.e3mail.service.TbItemService;

@Controller
public class ItemController {
	@Autowired
	private TbItemService tbItemService;
	@RequestMapping("/item/{itemId}")
	public String itemDesc(@PathVariable Long itemId,Model model){
		//查询商品
		TbItem tbItem = tbItemService.findItemById(itemId);
		Item item=new Item(tbItem);
		//查询商品描述
		TbItemDesc itemDesc = tbItemService.findItemDescByItemId(itemId);
		//将结果分装到model,返回到页面
		model.addAttribute(item);
		model.addAttribute(itemDesc);
		return "item";
	}
}

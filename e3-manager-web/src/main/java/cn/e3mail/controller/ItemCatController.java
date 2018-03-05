package cn.e3mail.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mail.common.EasyUITreeNode;
import cn.e3mail.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatServiceImpl;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> findCatParent(@RequestParam(name="id",defaultValue="0")Long parentId){//将接收的string类型的请求参数转为long类型的方法参数传入
		List<EasyUITreeNode> list = itemCatServiceImpl.findCatByParentId(parentId);
		return list;
	}
}

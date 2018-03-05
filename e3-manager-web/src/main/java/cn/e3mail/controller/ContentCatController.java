package cn.e3mail.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.e3mail.common.EasyUITreeNode;
import cn.e3mail.content.service.ContentCatService;
import cn.e3mail.utils.E3Result;

@Controller
public class ContentCatController {
	@Autowired
	private ContentCatService contentCatServiceImpl;
	
	//查询所有的类别
	@ResponseBody
	@RequestMapping("/content/category/list")
	public List<EasyUITreeNode> findContentCat(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> list = contentCatServiceImpl.findContentCat(parentId);
		return list;
	}
	
	//新增内容分类
	@ResponseBody
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)//以post的形式发送请求
	public E3Result addCat(Long parentId,String name){
		E3Result addCat = contentCatServiceImpl.addCat(parentId, name);
		return addCat;
	}
	
}

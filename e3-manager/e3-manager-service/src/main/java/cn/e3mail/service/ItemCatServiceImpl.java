package cn.e3mail.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mail.common.EasyUITreeNode;
import cn.e3mail.mapper.TbItemCatMapper;
import cn.e3mail.pojo.TbItemCat;
import cn.e3mail.pojo.TbItemCatExample;
import cn.e3mail.pojo.TbItemCatExample.Criteria;
@Service//添加该注解表示该service实例由springioc容器创建
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	@Override
	public List<EasyUITreeNode> findCatByParentId(long parentId) {
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();//创建查询条件对象
		criteria.andParentIdEqualTo(parentId);//设置条件信息
		List<TbItemCat> catList = tbItemCatMapper.selectByExample(example);//根据条件（父节点id）查询
		List<EasyUITreeNode> list=new ArrayList<EasyUITreeNode>();
		for (TbItemCat tbItemCat : catList) {
			EasyUITreeNode easyUITreeNode=new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			list.add(easyUITreeNode);
		}
		return list;
	}

}

package cn.e3mail.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mail.common.EasyUITreeNode;
import cn.e3mail.content.service.ContentCatService;
import cn.e3mail.mapper.TbContentCategoryMapper;
import cn.e3mail.pojo.TbContentCategory;
import cn.e3mail.pojo.TbContentCategoryExample;
import cn.e3mail.pojo.TbContentCategoryExample.Criteria;
import cn.e3mail.utils.E3Result;
@Service
public class ContentCatServiceImpl implements ContentCatService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> findContentCat(long parentId) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();//创建条件对象
		criteria.andParentIdEqualTo(parentId);//设置具体条件信息
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);//根据条件查询
		List<EasyUITreeNode> nodelist=new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodelist.add(node);
		}
		return nodelist;
	}

	@Override
	public E3Result addCat(long parentId, String name) {
		//创建tbcontentcatagory对象
		TbContentCategory tbContentCategory=new TbContentCategory();
		//设置填充该对象的属性
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		tbContentCategory.setIsParent(false);
		//使用指定mapper调用insert方法，此时返回主键id
		contentCategoryMapper.insert(tbContentCategory);
		//判断父节点的isparent属性是否为true，不是就改为true
		  //根据parentId查出父节点
		  TbContentCategory contentCategoryParent = contentCategoryMapper.selectByPrimaryKey(parentId);
		  if (!contentCategoryParent.getIsParent()) {
			contentCategoryParent.setIsParent(true);
			//调用指定mapper的update()进行更新
			  contentCategoryMapper.updateByPrimaryKey(contentCategoryParent);
		}
		return E3Result.ok(tbContentCategory);
	}

}

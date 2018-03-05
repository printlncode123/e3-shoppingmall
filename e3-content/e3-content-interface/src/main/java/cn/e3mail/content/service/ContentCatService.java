package cn.e3mail.content.service;

import java.util.List;

import cn.e3mail.common.EasyUITreeNode;
import cn.e3mail.utils.E3Result;

public interface ContentCatService {
	List<EasyUITreeNode> findContentCat(long parentId);
	E3Result addCat(long parentId,String name);//根据父节点id添加类别
}

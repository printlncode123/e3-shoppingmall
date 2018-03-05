package cn.e3mail.service;

import java.util.List;

import cn.e3mail.common.EasyUITreeNode;

/**
 * <p>Title: ItemCatService</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 * 商品分类管理
 */
public interface ItemCatService {
	List<EasyUITreeNode> findCatByParentId(long parentId);
}

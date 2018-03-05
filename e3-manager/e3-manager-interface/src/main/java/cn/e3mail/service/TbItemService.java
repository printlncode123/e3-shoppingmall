package cn.e3mail.service;

import cn.e3mail.common.EasyUIDatagridResult;
import cn.e3mail.pojo.TbItem;
import cn.e3mail.pojo.TbItemDesc;
import cn.e3mail.utils.E3Result;

public interface TbItemService {
	TbItem findItemById(long id);
	EasyUIDatagridResult findItemListPage(int page,int rows);
	E3Result addItem(TbItem item,String desc);
	TbItemDesc findItemDescByItemId(long itemId);
}

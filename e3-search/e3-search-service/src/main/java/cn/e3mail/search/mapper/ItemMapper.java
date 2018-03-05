package cn.e3mail.search.mapper;

import cn.e3mail.common.SearchItem;
import cn.e3mail.pojo.TbContentCategory;
import cn.e3mail.pojo.TbContentCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
//从数据库查写到索引库查询商品
public interface ItemMapper {
    List<SearchItem> findItemList();
    SearchItem getItemById(long id);//根据添加商品的服务发送的id从数据库查询指定商品
}
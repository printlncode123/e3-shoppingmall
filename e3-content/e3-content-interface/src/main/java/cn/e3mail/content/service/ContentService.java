package cn.e3mail.content.service;
//内容管理接口

import java.util.List;

import cn.e3mail.pojo.TbContent;
import cn.e3mail.utils.E3Result;

public interface ContentService {
	E3Result addContent(TbContent content);
	List<TbContent> findContentByCid(long cid);
}

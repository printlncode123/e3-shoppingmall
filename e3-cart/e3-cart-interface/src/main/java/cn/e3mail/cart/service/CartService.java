package cn.e3mail.cart.service;

import java.util.List;

import cn.e3mail.pojo.TbItem;
import cn.e3mail.utils.E3Result;

public interface CartService {
	//将购物车中商品添加到redis
	E3Result addCart(long userId,long itemId,Integer num);
	//合并cookie和redis的商品
	E3Result merge(long userId,List<TbItem> itemList);//cookie中的购物车与redis中的购物车合并
	//查询redis中的商品
	List<TbItem> findByRedis(long userId);
	//更新redis中购物车
	E3Result updateCart(long userId,long itemId,int num);
	//删除redis中购物车
	E3Result deleteCart(long userId,long itemId);
	//清空购物车
	E3Result cleanCart(long userId);
}

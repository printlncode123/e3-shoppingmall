package cn.e3mail.item.pojo;

import cn.e3mail.pojo.TbItem;

public class Item extends TbItem {
	public Item(TbItem tbItem){
		this.setId(tbItem.getId());
		this.setCid(tbItem.getCid());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setBarcode(tbItem.getBarcode());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
	}
	
	public String[] getImages(){
		String image2 = this.getImage();
		if (image2!=null && !image2.equals("")) {
			String[] imgs = image2.split(",");
			return imgs;
		}
		return null;
	}
}

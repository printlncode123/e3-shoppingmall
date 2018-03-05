package cn.e3mail.common;

import java.io.Serializable;

public class SearchItem implements Serializable{
	private static final long serialVersionUID = 1L;
	/* 将商品表与商品类别表关联，使用solr查询商品
	 * select 
			a.id,
			a.title,
			a.sell_point,
			a.price,
			a.image,
			b.name catagory_name 
		from tb_item a LEFT JOIN tb_item_cat b on a.cid=b.id;
	*/
	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String catagory_name ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCatagory_name() {
		return catagory_name;
	}
	public void setCatagory_name(String catagory_name) {
		this.catagory_name = catagory_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String[] getImages(){
		if (image!=null&&!"".equals(image)) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
}              

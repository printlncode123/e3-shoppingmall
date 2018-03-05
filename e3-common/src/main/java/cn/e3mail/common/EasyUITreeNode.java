package cn.e3mail.common;

import java.io.Serializable;

/**
 * <p>Title: EasyUITreeNode</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 * 查询类别返回的结果
 */
public class EasyUITreeNode implements Serializable{
	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}

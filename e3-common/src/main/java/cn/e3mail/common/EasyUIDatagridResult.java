package cn.e3mail.common;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: EasyUIDatagridResult</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 * 分页查询显示的结果类型
 */
public class EasyUIDatagridResult implements Serializable{
	private long total;//总记录数
	private List rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}

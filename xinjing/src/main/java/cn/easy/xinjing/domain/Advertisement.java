package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_advertisement")
public class Advertisement extends AtEntity {
	/**标题*/
	private String title;
	/**摘要*/
	private String summary;
	/**内容（链接）*/
	private String content;
	/**类型*/
	private Integer type;
	/**状态*/
	private Integer status;
	/**图片*/
	private String pic;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getSummary() { return summary; }
	public void setSummary(String summary) { this.summary = summary; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public Integer getType() { return type; }
	public void setType(Integer type) { this.type = type; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getPic() { return pic; }
	public void setPic(String pic) { this.pic = pic; }

}

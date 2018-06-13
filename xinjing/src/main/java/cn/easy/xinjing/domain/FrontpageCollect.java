package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_frontpage_collect")
public class FrontpageCollect extends AtEntity {
	/**行业新闻头条ID*/
	private String newsId;
	/**用户ID*/
	private String userId;
	/**是否收藏*/
	private Integer isCollect;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getNewsId() { return newsId; }
	public void setNewsId(String newsId) { this.newsId = newsId; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public Integer getIsCollect() { return isCollect; }
	public void setIsCollect(Integer isCollect) { this.isCollect = isCollect; }

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}
}

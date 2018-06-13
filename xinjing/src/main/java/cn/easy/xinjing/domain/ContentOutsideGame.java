package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_content_outside_game")
public class ContentOutsideGame extends AtEntity {
	/**内容表ID*/
	private String contentId;
	/**内容*/
	private String content;

	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
}

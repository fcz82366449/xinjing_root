package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Hiddenable
@Table(name = "xj_comment")
public class Comment extends AtEntity implements IHiddenEntity {
	/**关联ID*/
	private String objectId;
	/**关联类型*/
	private String objectType;
	/**评论内容*/
	private String content;
	/**状态*/
	private Integer status;
	/**回复信息*/
	private String feedback;
	/**评分*/
	private Integer score;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getObjectId() { return objectId; }
	public void setObjectId(String objectId) { this.objectId = objectId; }
	public String getObjectType() { return objectType; }
	public void setObjectType(String objectType) { this.objectType = objectType; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getFeedback() { return feedback; }
	public void setFeedback(String feedback) { this.feedback = feedback; }
	public Integer getScore() { return score; }
	public void setScore(Integer score) { this.score = score; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

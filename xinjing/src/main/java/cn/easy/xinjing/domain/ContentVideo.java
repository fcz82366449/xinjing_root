package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.AtEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "xj_content_video")
public class ContentVideo extends AtEntity {
	/**内容表ID*/
	private String contentId;
	/**内容*/
	private String content;

	private int videopassword;

	public int getVideopassword() {
		return videopassword;
	}

	public void setVideopassword(int videopassword) {
		this.videopassword = videopassword;
	}

	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }

}

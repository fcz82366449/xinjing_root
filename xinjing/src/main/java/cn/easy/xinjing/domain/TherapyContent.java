package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "xj_therapy_content")
public class TherapyContent extends IdEntity {
	/**疗法分类Id*/
	private String therapyId;
	/**内容Id*/
	private String contentId;
	public String getTherapyId() { return therapyId; }
	public void setTherapyId(String therapyId) { this.therapyId = therapyId; }
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }

}

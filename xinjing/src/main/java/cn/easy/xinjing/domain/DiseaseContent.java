package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "xj_disease_content")
public class DiseaseContent extends IdEntity {
	/**病种分类Id*/
	private String diseaseId;
	/**内容Id*/
	private String contentId;
	public String getDiseaseId() { return diseaseId; }
	public void setDiseaseId(String diseaseId) { this.diseaseId = diseaseId; }
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }

}

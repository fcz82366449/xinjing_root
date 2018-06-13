package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_section_office_disease")
public class SectionOfficeDisease extends AtEntity {
	/**科室Id*/
	private String sectionOfficeId;
	/**病种Id*/
	private String diseaseId;
	public String getSectionOfficeId() { return sectionOfficeId; }
	public void setSectionOfficeId(String sectionOfficeId) { this.sectionOfficeId = sectionOfficeId; }
	public String getDiseaseId() { return diseaseId; }
	public void setDiseaseId(String diseaseId) { this.diseaseId = diseaseId; }

}

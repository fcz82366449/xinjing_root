package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_disease_therapy")
public class DiseaseTherapy extends AtEntity {
	/**病种Id*/
	private String diseaseId;
	/**疗法Id*/
	private String therapyId;
	public String getDiseaseId() { return diseaseId; }
	public void setDiseaseId(String diseaseId) { this.diseaseId = diseaseId; }
	public String getTherapyId() { return therapyId; }
	public void setTherapyId(String therapyId) { this.therapyId = therapyId; }

}

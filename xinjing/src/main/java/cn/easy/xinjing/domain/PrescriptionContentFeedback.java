package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.AtEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "xj_prescription_content_feedback")
public class PrescriptionContentFeedback extends AtEntity {
	/**电子处方Id*/
	private String prescriptionId;
	/**内容Id*/
	private String contentId;
	/**电子处方内容关联Id*/
	private String prescriptionContentId;
	/**使用时间*/
	private Date useAt;

	public String getPrescriptionId() { return prescriptionId; }
	public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getPrescriptionContentId() { return prescriptionContentId; }
	public void setPrescriptionContentId(String prescriptionContentId) { this.prescriptionContentId = prescriptionContentId; }
	public Date getUseAt() { return useAt; }
	public void setUseAt(Date useAt) { this.useAt = useAt; }

}

package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_content_hospital")
public class ContentHospital extends AtEntity {
	/**场景分配id*/
	private String contentDpId;
	/**内容Id*/
	private String contentId;
	/**状态*/
	private Integer status;



	public String getContentDpId() { return contentDpId; }
	public void setContentDpId(String contentDpId) { this.contentDpId = contentDpId; }
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }

}

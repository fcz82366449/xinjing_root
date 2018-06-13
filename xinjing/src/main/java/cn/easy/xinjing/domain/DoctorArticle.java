package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Hiddenable
@Table(name = "xj_doctor_article")
public class DoctorArticle extends AtEntity implements IHiddenEntity {
	/**null*/
	private String title;
	/**null*/
	private String content;
	/**null*/
	private Integer status;
	/**医生Id*/
	private String doctorId;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

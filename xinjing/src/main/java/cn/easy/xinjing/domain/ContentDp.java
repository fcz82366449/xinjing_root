package cn.easy.xinjing.domain;


import javax.persistence.*;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Hiddenable
@Table(name = "xj_content_dp")
public class ContentDp extends AtEntity implements IHiddenEntity {
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**状态*/
	private Integer status;
	/**备注*/
	private String remark;
	/**是否隐藏*/
	private Integer hidden = 0;

	private Set<ContentHospital> contentHospitalSet = new HashSet<ContentHospital>();

	//映射单1-n的关联关系
	@JoinColumn(name="contentDpId")
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE})
	public Set<ContentHospital> getContentHospitalSet() {
		return contentHospitalSet;
	}

	public void setContentHospitalSet(Set<ContentHospital> contentHospitalSet) {
		this.contentHospitalSet = contentHospitalSet;
	}

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

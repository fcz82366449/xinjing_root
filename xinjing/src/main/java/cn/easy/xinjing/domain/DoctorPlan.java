package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

@Entity
@Hiddenable
@Table(name = "xj_doctor_plan")
public class DoctorPlan extends AtEntity implements IHiddenEntity {
	/**方案名称*/
	private String name;
	/**方案编码*/
	private String code;
	/**医生id*/
	private String doctorId;
	/**助记码*/
	private String helpCode;
	/**排序*/
	private Integer sort;
	/**备注*/
	private String remark;
	/**状态*/
	private Integer status;
	/**描述*/
	private String description;
	/**是否隐藏*/
	private Integer hidden = 0;

	private String hospitalId;

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	public String getHelpCode() { return helpCode; }
	public void setHelpCode(String helpCode) { this.helpCode = helpCode; }
	public Integer getSort() { return sort; }
	public void setSort(Integer sort) { this.sort = sort; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

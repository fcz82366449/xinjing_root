package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })

@Entity
@Hiddenable
@Table(name = "xj_vr_room")
public class VrRoom extends AtEntity implements IHiddenEntity {
	/**名称*/
	private String name;
	/**地区编码*/
	private String region;
	/**地址*/
	private String address;
	/**状态*/
	private Integer status;
	/**排班信息*/
	private String scheduleInfo;
	/**备注*/
	private String remark;
	/**是否隐藏*/
	private Integer hidden = 0;

	/**
	 * 介绍
	 */
	private String description;
	/**
	 * 医院的外键
	 */
	private String hospital_id ;


	/**
	 * 图片集
	 */
	private String coverPic;
	/**
	 * 是否轮播
	 */
	private String carousel;

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	public String getCarousel() {
		return carousel;
	}

	public void setCarousel(String carousel) {
		this.carousel = carousel;
	}

	/**
	 * 级联对象


	private Hospital hospital;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}*/

	public String getHospital_id() {
		return hospital_id;
	}

	public void setHospital_id(String hospital_id) {
		this.hospital_id = hospital_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getScheduleInfo() { return scheduleInfo; }
	public void setScheduleInfo(String scheduleInfo) { this.scheduleInfo = scheduleInfo; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	@Override
	public Integer getHidden() { return hidden; }
	@Override
	public void setHidden(Integer hidden) { this.hidden = hidden; }

}

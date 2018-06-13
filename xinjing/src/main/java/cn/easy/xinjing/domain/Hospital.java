package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

@Entity
@Hiddenable
@Table(name = "xj_hospital")
public class Hospital extends AtEntity implements IHiddenEntity {
	/**助记码*/
	private String helpCode;
	/**名称*/
	private String name;
	/**地区*/
	private String region;
	/**地址*/
	private String address;
	/**排序*/
	private Integer sort;
	/**备注*/
	private String remark;
	/**摘要*/
	private String abstracts;
	/**介绍*/
	private String description;
	/**状态*/
	private Integer status;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**
	 * 图片集
	 */
	private String coverPic;

	/**
	 * 是否轮播
	 */
	private String carousel;

	private Integer isdisplay;

	public Integer getIsdisplay() {
		return isdisplay;
	}

	public void setIsdisplay(Integer isdisplay) {
		this.isdisplay = isdisplay;
	}

	public String getCarousel() {
		return carousel;
	}

	public void setCarousel(String carousel) {
		this.carousel = carousel;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	public String getHelpCode() { return helpCode; }
	public void setHelpCode(String helpCode) { this.helpCode = helpCode; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public Integer getSort() { return sort; }
	public void setSort(Integer sort) { this.sort = sort; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	public String getAbstracts() { return abstracts; }
	public void setAbstracts(String abstracts) { this.abstracts = abstracts; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

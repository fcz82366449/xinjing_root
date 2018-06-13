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
@Table(name = "xj_evaluating")
public class Evaluating extends AtEntity implements IHiddenEntity {
	/**名称*/
	private String name;
	/**评测编码*/
	private String code;
	/**缩略图*/
	private String coverPic;
	/**介绍*/
	private String description;
	/**备注*/
	private String remark;
	/**状态*/
	private Integer status;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getCoverPic() { return coverPic; }
	public void setCoverPic(String coverPic) { this.coverPic = coverPic; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

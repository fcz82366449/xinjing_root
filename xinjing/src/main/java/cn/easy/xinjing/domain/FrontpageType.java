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
@Table(name = "xj_frontpage_type")
public class FrontpageType extends AtEntity implements IHiddenEntity {
	/**助记码*/
	private String helpCode;
	/**名称*/
	private String name;
	/**父节点ID*/
	private String pid;
	/**排序*/
	private Integer sort;
	/**备注*/
	private String remark;
	/**状态*/
	private Integer status;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getHelpCode() { return helpCode; }
	public void setHelpCode(String helpCode) { this.helpCode = helpCode; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getPid() { return pid; }
	public void setPid(String pid) { this.pid = pid; }
	public Integer getSort() { return sort; }
	public void setSort(Integer sort) { this.sort = sort; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

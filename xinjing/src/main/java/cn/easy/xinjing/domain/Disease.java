package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.xinjing.utils.Constants;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 病种
 */
@Entity
@Hiddenable
@Table(name = "xj_disease")
public class Disease extends AtEntity implements IHiddenEntity {
	/**助记码*/
	private String helpCode;
	/**名称*/
	private String name;
	/**父节点ID*/
	private String pid;
	/**排序*/
	private Integer sort;
	private String description;

	/**备注*/
	private String remark;
	/**状态*/
	private Integer status = Constants.DISEASE_STATUS_INIT;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	@Override
	public Integer getHidden() { return hidden; }
	@Override
	public void setHidden(Integer hidden) { this.hidden = hidden; }
}

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
@Table(name = "xj_banks")
public class Banks extends AtEntity implements IHiddenEntity {
	/**名称*/
	private String name;
	/**编码*/
	private String code;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

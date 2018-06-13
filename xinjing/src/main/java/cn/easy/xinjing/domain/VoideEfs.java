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
@Table(name = "xj_voide_efs")
public class VoideEfs extends AtEntity implements IHiddenEntity {
	/**密钥*/
	private Integer password;
	/**预留密钥*/
	private Integer password2;
	/**是否隐藏*/
	private Integer hidden = 0;

	public Integer getPassword() { return password; }
	public void setPassword(Integer password) { this.password = password; }
	public Integer getPassword2() { return password2; }
	public void setPassword2(Integer password2) { this.password2 = password2; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

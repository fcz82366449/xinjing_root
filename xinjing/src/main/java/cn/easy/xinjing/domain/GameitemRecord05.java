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
@Table(name = "xj_gameitem_record05")
public class GameitemRecord05 extends AtEntity implements IHiddenEntity {
	/**游戏记录表头id*/
	private String gameheadRecordId;
	/**游戏阶段*/
	private String gamephase;
	/**做的次数*/
	private Integer operationnumber;
	/**总个数*/
	private Integer totalnumber;
	/**正确的个数*/
	private Integer correctnumber;
	/**正确率*/
	private String correctrate;
	/**所用时间*/
	private String timeconsuming;
	/**是否完成*/
	private String completeif;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**错误个数*/
	private Integer inaccuracynumber;
	/**错误率*/
	private String inaccuracyrate;

	public Integer getInaccuracynumber() {
		return inaccuracynumber;
	}

	public void setInaccuracynumber(Integer inaccuracynumber) {
		this.inaccuracynumber = inaccuracynumber;
	}

	public String getInaccuracyrate() {
		return inaccuracyrate;
	}

	public void setInaccuracyrate(String inaccuracyrate) {
		this.inaccuracyrate = inaccuracyrate;
	}

	public String getGameheadRecordId() { return gameheadRecordId; }
	public void setGameheadRecordId(String gameheadRecordId) { this.gameheadRecordId = gameheadRecordId; }
	public String getGamephase() { return gamephase; }
	public void setGamephase(String gamephase) { this.gamephase = gamephase; }
	public Integer getOperationnumber() { return operationnumber; }
	public void setOperationnumber(Integer operationnumber) { this.operationnumber = operationnumber; }
	public Integer getTotalnumber() { return totalnumber; }
	public void setTotalnumber(Integer totalnumber) { this.totalnumber = totalnumber; }
	public Integer getCorrectnumber() { return correctnumber; }
	public void setCorrectnumber(Integer correctnumber) { this.correctnumber = correctnumber; }
	public String getCorrectrate() { return correctrate; }
	public void setCorrectrate(String correctrate) { this.correctrate = correctrate; }
	public String getTimeconsuming() { return timeconsuming; }
	public void setTimeconsuming(String timeconsuming) { this.timeconsuming = timeconsuming; }
	public String getCompleteif() { return completeif; }
	public void setCompleteif(String completeif) { this.completeif = completeif; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

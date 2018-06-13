package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

@Entity
@Hiddenable
@Table(name = "xj_singlesel_record")
public class SingleselRecord extends AtEntity implements IHiddenEntity {
	/**评测人*/
	private String evaluationPeople;
	/**评测主键*/
	private String evaluatingId;
	/**评测题目主键*/
	private String singleselId;
	/**题目分值*/
	private BigDecimal tmscore;
	/**答案*/
	private String description;
	/**评测时间*/
	private Timestamp evaluationTime;
	/**备注*/
	private String remark;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getEvaluationPeople() { return evaluationPeople; }
	public void setEvaluationPeople(String evaluationPeople) { this.evaluationPeople = evaluationPeople; }
	public String getEvaluatingId() { return evaluatingId; }
	public void setEvaluatingId(String evaluatingId) { this.evaluatingId = evaluatingId; }
	public String getSingleselId() { return singleselId; }
	public void setSingleselId(String singleselId) { this.singleselId = singleselId; }
	public BigDecimal getTmscore() { return tmscore; }
	public void setTmscore(BigDecimal tmscore) { this.tmscore = tmscore; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Timestamp getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(Timestamp evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

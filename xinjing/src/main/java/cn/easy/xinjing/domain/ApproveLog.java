package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_approve_log")
public class ApproveLog extends AtEntity {
	/**关联ID*/
	private String objectId;
	/**关联类型*/
	private String objectType;
	/**审核结果*/
	private String result;
	/**审核意见*/
	private String remark;
	public String getObjectId() { return objectId; }
	public void setObjectId(String objectId) { this.objectId = objectId; }
	public String getObjectType() { return objectType; }
	public void setObjectType(String objectType) { this.objectType = objectType; }
	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}

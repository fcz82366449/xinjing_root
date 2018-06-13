package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Hiddenable
@Table(name = "xj_doctor_appointment")
public class DoctorAppointment extends AtEntity implements IHiddenEntity {
	/**开始时间*/
	private Date startAt;
	/**结束时间*/
	private Date endAt;
	/**状态*/
	private Integer status;
	/**预约人*/
	private String bookingPerson;
	/**预约人Id*/
	private String bookingPersonId;
	/**医生Id*/
	private String doctorId;
	/**是否隐藏*/
	private Integer hidden = 0;
	public Date getStartAt() { return startAt; }
	public void setStartAt(Date startAt) { this.startAt = startAt; }
	public Date getEndAt() { return endAt; }
	public void setEndAt(Date endAt) { this.endAt = endAt; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getBookingPerson() { return bookingPerson; }
	public void setBookingPerson(String bookingPerson) { this.bookingPerson = bookingPerson; }
	public String getBookingPersonId() { return bookingPersonId; }
	public void setBookingPersonId(String bookingPersonId) { this.bookingPersonId = bookingPersonId; }
	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}

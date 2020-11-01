package com.p6spy.mybaits.entity;

import java.util.Date;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@TableName("EVE_ALLIANCE_INFO")
public class AlliancePO extends Model<AlliancePO> {
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer allianceId;

	private Integer creatorCorporationId;

	private Integer creatorId;

	private Date dateFounded;

	private Integer executorCorporationId;

	private String allianceName;

	private String ticker;

	private Integer factionId;

	private String status;

	private Date createTime;

	private String remark;
	
	public AlliancePO() {
	}

	public Integer getAllianceId() {
		return allianceId;
	}

	public void setAllianceId(Integer allianceId) {
		this.allianceId = allianceId;
	}

	public Integer getCreatorCorporationId() {
		return creatorCorporationId;
	}

	public void setCreatorCorporationId(Integer creatorCorporationId) {
		this.creatorCorporationId = creatorCorporationId;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getDateFounded() {
		return dateFounded;
	}

	public void setDateFounded(Date dateFounded) {
		this.dateFounded = dateFounded;
	}

	public Integer getExecutorCorporationId() {
		return executorCorporationId;
	}

	public void setExecutorCorporationId(Integer executorCorporationId) {
		this.executorCorporationId = executorCorporationId;
	}

	public String getAllianceName() {
		return allianceName;
	}

	public void setAllianceName(String allianceName) {
		this.allianceName = allianceName;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Integer getFactionId() {
		return factionId;
	}

	public void setFactionId(Integer factionId) {
		this.factionId = factionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

}

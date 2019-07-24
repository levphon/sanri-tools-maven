package com.sanri.app.task;

import sanri.utils.excel.Version;
import sanri.utils.excel.annotation.ExcelColumn;
import sanri.utils.excel.annotation.ExcelExport;

@ExcelExport(autoWidth = true, version = Version.EXCEL2007)
public class ResultBean {
	@ExcelColumn(value = "姓名", index = 0)
	private String name;
	@ExcelColumn(value = "车架号", index = 1)
	private String standno;
	@ExcelColumn(value = "保险公司保单号", index = 2)
	private String fullInsuranceNo;
	@ExcelColumn(value = "创建时间 ", index = 3)
	private String createTime;

	public ResultBean() {
	}

	public ResultBean(String name, String standno, String fullInsuranceNo, String createTime) {
		super();
		this.name = name;
		this.standno = standno;
		this.fullInsuranceNo = fullInsuranceNo;
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStandno() {
		return standno;
	}

	public void setStandno(String standno) {
		this.standno = standno;
	}

	public String getFullInsuranceNo() {
		return fullInsuranceNo;
	}

	public void setFullInsuranceNo(String fullInsuranceNo) {
		this.fullInsuranceNo = fullInsuranceNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		// return ToStringBuilder.reflectionToString(this,
		// ToStringStyle.SHORT_PREFIX_STYLE);
		return name + "\t" + standno + "\t" + fullInsuranceNo + "\t" + createTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof ResultBean){
			ResultBean other = (ResultBean) obj;
			if(other.standno.equals(standno)){
				return true;
			}
		}
		
		return false;
	}

}
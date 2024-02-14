package model;

import java.io.Serializable;

public class CompanyBean implements Serializable{

	private static final long serialVersionUID = 5141598854025362144L;
	
	private int companyId;
	private String companyName, companyDescription;
	
	public CompanyBean() {
		super();
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

}

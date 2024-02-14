package model;

import java.io.Serializable;

public class CategoryBean implements Serializable{

	private static final long serialVersionUID = 3179233735094245438L;

	private int categoryId;
	private String categoryName;

	public CategoryBean() {
		super();
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}

package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class ProductBean implements Serializable {

	private static final long serialVersionUID = 1773538882520869219L;
	
	private int productId, companyId, deleteCategory;
	private boolean active;
	private BigDecimal productPrice;
	private byte[] productCover; 
	private String productName, productDescription;
	private LinkedHashMap<Integer, Integer> categories; //per ogni entry di categoria, un valore idCategoria
	//es 1 5, 1 8  significa che ho due entry della chiave 1 con valori 5 e 8 (categ 1 con sottocateg 5 e 8)
		
	public ProductBean() {
		super();
	}

	//getters and setters
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getDeleteCategory() {
		return deleteCategory;
	}

	public void setDeleteCategory(int deleteCategory) {
		this.deleteCategory = deleteCategory;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public byte[] getProductCover() {
		return productCover;
	}

	public void setProductCover(byte[] productCover) {
		this.productCover = productCover;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public LinkedHashMap<Integer, Integer> getCategories() {
		return categories;
	}

	public void setCategories(LinkedHashMap<Integer, Integer> categories) {
		this.categories = categories;
	}
	
}

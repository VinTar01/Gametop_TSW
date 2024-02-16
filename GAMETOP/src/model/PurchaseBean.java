package model;

import java.io.Serializable;
import java.time.LocalDate;

import cart.Cart;

public class PurchaseBean implements Serializable{

	private static final long serialVersionUID = 2191801109408218838L;
	
	private int purchaseId, accountId, addressId, deleteProductId;
	private String paymentType, purchaseFirstName, purchaseLastName, purchasePhoneNumber;
	private LocalDate date;
	private Cart cart;
	
	public PurchaseBean() {
		super();
	}

	//getters and setters
	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getDeleteProductId() {
		return deleteProductId;
	}

	public void setDeleteProductId(int deleteProductId) {
		this.deleteProductId = deleteProductId;
	}

	public String getPurchasePhoneNumber() {
		return purchasePhoneNumber;
	}

	public void setPurchasePhoneNumber(String purchasePhoneNumber) {
		this.purchasePhoneNumber = purchasePhoneNumber;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPurchaseFirstName() {
		return purchaseFirstName;
	}

	public void setPurchaseFirstName(String purchaseFirstName) {
		this.purchaseFirstName = purchaseFirstName;
	}

	public String getPurchaseLastName() {
		return purchaseLastName;
	}

	public void setPurchaseLastName(String purchaseLastName) {
		this.purchaseLastName = purchaseLastName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
}

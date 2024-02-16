package cart;

import java.math.BigDecimal;

import model.ProductBean;

public class CartItem {

	private final ProductBean product;
	private int quantity;

	public CartItem(ProductBean product, int quantity) {
		this.product=product;
		this.quantity=quantity;
	}

	public ProductBean getProduct() {
		return product;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public BigDecimal total() {
		return product.getProductPrice().multiply(BigDecimal.valueOf(quantity));
	}
	
}

package function;

import java.util.Arrays;
import java.util.Comparator;

import model.ProductBean;

public class ProductComparator implements Comparator<ProductBean>{

	private enum Product {active, product_price, product_name, product_cover, product_description}
	private Product sortingBy;

	@Override
	public int compare(ProductBean product1, ProductBean product2) {
		switch(sortingBy) {
		case active: return Boolean.compare(product1.isActive(), product2.isActive());
		case product_price: return product1.getProductPrice().compareTo(product2.getProductPrice());
		case product_name: return product1.getProductName().compareTo(product2.getProductName());
		case product_cover: return Arrays.compare(product1.getProductCover(), product2.getProductCover());
		case product_description: return product1.getProductDescription().compareTo(product2.getProductDescription());
		}
		throw new RuntimeException("Invalid sorter parameter");
	}

	public void setSortingBy(String sortBy) {
		this.sortingBy = Product.valueOf(sortBy);
	}
	
}

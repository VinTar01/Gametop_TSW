package cart;

import java.math.BigDecimal;
import java.util.List;

import model.ProductBean;

public class Cart {

	private List<CartItem> items;
	
	public Cart(List<CartItem> items) {
		this.items=items;
		
	}
	
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(CartItem item : items) {
			total = total.add(item.total());
		}
		return total;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	
	public void removeItems() {
		items = null;
	}
	
	public boolean addProduct(ProductBean product, int quantity) {
		for(CartItem item : items) {
			/*Si controlla se l'ID del prodotto passato come argomento corrisponde all'ID del prodotto associato all'oggetto CartItem*/
			if(product.getProductId() == item.getProduct().getProductId()) {
				quantity += item.getQuantity(); //aggiorno la quantità
				
				/*Viene verificato se la quantità risultante supera il limite massimo consentito di 3. 
				 * Se supera il limite, il metodo restituisce false senza aggiungere il prodotto.
				 */
				if(quantity <= 3) {
					item.setQuantity(quantity);
					return true;
				}
				else return false;
			}
		}
		/*Se nessun CartItem corrispondente viene trovato, 
		 * viene creato un nuovo CartItem per il prodotto e la quantità specificati e aggiunto alla lista items. 
		 * Il metodo restituisce true.
		 */
		return items.add(new CartItem(product, quantity));
	}
	
	public boolean removeProduct(ProductBean product) {
		for(CartItem item : items)
			if(product.getProductId() == item.getProduct().getProductId())
				return items.remove(item);
		
		return false;
	}
	
}

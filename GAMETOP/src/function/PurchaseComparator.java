package function;

import java.util.Comparator;

import model.PurchaseBean;

public class PurchaseComparator implements Comparator<PurchaseBean>{
	
	private enum Purchase {purchase_phone_number, total, payment_type, purchase_firstname, purchase_lastname, date}
	private Purchase sortingBy;

	@Override
	public int compare(PurchaseBean purchase1, PurchaseBean purchase2) {
		switch(sortingBy) {
		case purchase_phone_number: return purchase1.getPurchasePhoneNumber().compareTo(purchase2.getPurchasePhoneNumber());
		case total: return purchase1.getCart().getTotal().compareTo(purchase2.getCart().getTotal());
		case payment_type: return purchase1.getPaymentType().compareTo(purchase2.getPaymentType());
		case purchase_firstname: return purchase1.getPurchaseFirstName().compareTo(purchase2.getPurchaseFirstName());
		case purchase_lastname: return purchase1.getPurchaseLastName().compareTo(purchase2.getPurchaseLastName());
		case date: return purchase1.getDate().compareTo(purchase2.getDate());
		}
		throw new RuntimeException("Invalid sorter parameter");
	}

	public void setSortingBy(String sortBy) {
		this.sortingBy = Purchase.valueOf(sortBy);
	}
	
}

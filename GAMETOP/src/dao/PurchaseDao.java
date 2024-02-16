package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.sql.DataSource;

import cart.Cart;
import cart.CartItem;
import function.PurchaseComparator;
import model.PurchaseBean;
import model.ProductBean;

public class PurchaseDao implements GenericDao<PurchaseBean> {
	
	private static final String TABLE_NAME = "purchase";
	private DataSource ds = null;

	public PurchaseDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Purchase Model creation....");
	}
	
	@Override
	public synchronized void doSave(PurchaseBean purchase) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<CartItem> items = purchase.getCart().getItems();

		String insertSQL1 = "INSERT INTO " + PurchaseDao.TABLE_NAME
				+ " (account_id, address_id, purchase_phone_number, payment_type, purchase_firstname, purchase_lastname, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		String insertSQL2 = "INSERT INTO composed"
				+ " (purchase_id, product_id, price, quantity) VALUES (?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL1);
			preparedStatement.setInt(1, purchase.getAccountId());
			preparedStatement.setInt(2, purchase.getAddressId());
			preparedStatement.setString(3, purchase.getPurchasePhoneNumber());
			preparedStatement.setString(4, purchase.getPaymentType());
			preparedStatement.setString(5, purchase.getPurchaseFirstName());
			preparedStatement.setString(6, purchase.getPurchaseLastName());
			preparedStatement.setDate(7, Date.valueOf(purchase.getDate()));

			preparedStatement.executeUpdate();
			connection.commit();
			
			setPurchaseId(purchase);
			
			for(CartItem item : items) {
				if (preparedStatement != null)
					preparedStatement.close();
				
				preparedStatement = connection.prepareStatement(insertSQL2);
				preparedStatement.setInt(1, purchase.getPurchaseId());
				preparedStatement.setInt(2, item.getProduct().getProductId());
				preparedStatement.setBigDecimal(3, item.getProduct().getProductPrice());
				preparedStatement.setInt(4, item.getQuantity());
				
				preparedStatement.executeUpdate();
			}

			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}

	@Override
	public synchronized void doUpdate(PurchaseBean purchase) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String updateSQL = "UPDATE " + PurchaseDao.TABLE_NAME
				+ " SET account_id = ?, address_id = ?, purchase_phone_number = ?"
				+ " payment_type = ?, purchase_first_name = ?, purchase_last_name = ? date = ?"
				+ " WHERE purchase_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setInt(1, purchase.getAccountId());
			preparedStatement.setInt(2, purchase.getAddressId());
			preparedStatement.setString(3, purchase.getPurchasePhoneNumber());
			preparedStatement.setString(4, purchase.getPaymentType());
			preparedStatement.setString(5, purchase.getPurchaseFirstName());
			preparedStatement.setString(6, purchase.getPurchaseLastName());
			preparedStatement.setDate(7, Date.valueOf(purchase.getDate()));
			preparedStatement.setInt(8, purchase.getPurchaseId());

			preparedStatement.executeUpdate();
			
			Collection<CartItem> items = purchase.getCart().getItems();
				 	
			Collection<CartItem> beanItems = doRetrieveByKey(purchase.getPurchaseId()).getCart().getItems();
				
			boolean update = false;
				
			for(CartItem item : items) {
					
				for(CartItem currentItem : beanItems) {
					if(item.getProduct().getProductId() == currentItem.getProduct().getProductId()) {
						update = true;
						break;
					}
				}
				if (preparedStatement != null)
					preparedStatement.close();
					
				if(update) {
					updateSQL = "UPDATE composed"
							+ " SET quantity = ?, price = ? WHERE purchase_id = ? AND product_id = ?";
						
					preparedStatement = connection.prepareStatement(updateSQL);
					preparedStatement.setInt(1, item.getQuantity());
					preparedStatement.setBigDecimal(2, item.getProduct().getProductPrice());
					preparedStatement.setInt(3, purchase.getPurchaseId());
					preparedStatement.setInt(4, item.getProduct().getProductId());
				}
				else {
					updateSQL = "INSERT INTO composed"
							+ " (purchase_id, product_id, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";
					
					preparedStatement = connection.prepareStatement(updateSQL);
					preparedStatement.setInt(1, purchase.getPurchaseId());
					preparedStatement.setInt(2, item.getProduct().getProductId());
					preparedStatement.setInt(3, item.getQuantity());
					preparedStatement.setBigDecimal(4, item.getProduct().getProductPrice());
				}
				preparedStatement.executeUpdate();
			}
		
			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}	
	}

	@Override
	public synchronized boolean doDelete(PurchaseBean purchase) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + PurchaseDao.TABLE_NAME + " WHERE purchase_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			
			if(purchase.getDeleteProductId() == 0) {
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.setInt(1, purchase.getPurchaseId());
			}
			else {
				deleteSQL = "DELETE FROM composed WHERE purchase_id = ? AND product_id = ?";
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.setInt(1, purchase.getPurchaseId());
				preparedStatement.setInt(2, purchase.getDeleteProductId());
				purchase.setDeleteProductId(0);
			}
			
			result = preparedStatement.executeUpdate();
			
			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return (result != 0);
	}
	
	@Override
	public synchronized PurchaseBean doRetrieveByKey(int purchaseId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		PurchaseBean purchase = null;
		Cart cart = new Cart(new LinkedList<CartItem>());

		String selectSQL = "SELECT * FROM " + PurchaseDao.TABLE_NAME + " NATURAL JOIN composed WHERE purchase_id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, purchaseId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				purchase = new PurchaseBean();
				ProductBean product = new ProductBean();
				
				purchase.setPurchaseId(rs.getInt("purchase_id"));
				purchase.setAccountId(rs.getInt("account_id"));
				purchase.setAddressId(rs.getInt("address_id"));
				purchase.setPurchasePhoneNumber(rs.getString("purchase_phone_number"));
				purchase.setPaymentType(rs.getString("payment_type"));
				purchase.setPurchaseFirstName(rs.getString("purchase_first_name"));
				purchase.setPurchaseLastName(rs.getString("purchase_last_name"));
				purchase.setDate(rs.getDate("date").toLocalDate());
				product.setProductId(rs.getInt("product_id"));
				product.setProductPrice(rs.getBigDecimal("price"));
				cart.addProduct(product, rs.getInt("quantity"));
			}
	
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return purchase;
	}

	@Override
	public synchronized Collection<PurchaseBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<PurchaseBean> purchases = new LinkedList<PurchaseBean>();
		PurchaseBean purchaseBean = null;
		Cart cart = null;
		int currentPurchaseId = -1;

		String selectSQL = "SELECT * FROM " + PurchaseDao.TABLE_NAME + " NATURAL JOIN composed ORDER BY purchase_id";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
			    int purchaseId = rs.getInt("purchase_id");
			    int accountId = rs.getInt("account_id");
			    int addressId = rs.getInt("address_id");
			    String purchasePhoneNumber = rs.getString("purchase_phone_number");
			    String paymentType = rs.getString("payment_type");
			    String purchaseFirstName = rs.getString("purchase_firstname");
			    String purchaseLastName = rs.getString("purchase_lastname");
			    LocalDate date = rs.getDate("date").toLocalDate();
			    if (purchaseId != currentPurchaseId) {
			        // Nuovo ordine, crea un nuovo OrderBean
			        if (purchaseBean != null) {
			        	purchaseBean.setCart(cart); // Imposta i prodotti dell'ordine precedente
			        	purchases.add(purchaseBean); // Aggiungi l'ordine alla collezione
			        }

			        // Crea un nuovo OrderBean per l'ordine corrente
			        purchaseBean = new PurchaseBean();
			        purchaseBean.setPurchaseId(purchaseId);
			        purchaseBean.setAccountId(accountId);
			        purchaseBean.setAddressId(addressId);
			        purchaseBean.setPurchasePhoneNumber(purchasePhoneNumber);
			        purchaseBean.setPaymentType(paymentType);
			        purchaseBean.setPurchaseFirstName(purchaseFirstName);
			        purchaseBean.setPurchaseLastName(purchaseLastName);
			        purchaseBean.setDate(date);

			        // Crea un nuovo Cart per i prodotti dell'ordine corrente
			        cart = new Cart(new LinkedList<CartItem>());

			        currentPurchaseId = purchaseId;
			    }

			    // Crea un nuovo ProductBean per ogni riga
			    ProductBean product = new ProductBean();
			    product.setProductId(rs.getInt("product_id"));
			    product.setProductPrice(rs.getBigDecimal("price"));

			    // Aggiunge il prodotto e la quantit� al Cart
			    cart.addProduct(product, rs.getInt("quantity"));
			}

			// Aggiungi l'ultimo ordine alla collezione
			if (purchaseBean != null) {
				purchaseBean.setCart(cart);
				purchases.add(purchaseBean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		
		if (order != null && !order.equals("") && !order.equalsIgnoreCase("purchase_id")) {
			PurchaseComparator comparator = new PurchaseComparator();
			comparator.setSortingBy(order);
			Collections.sort(new LinkedList<PurchaseBean>(purchases), comparator);
		}
		
		return purchases;
	}
	
	private void setPurchaseId(PurchaseBean purchase) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;


		String selectSQL = "SELECT " + PurchaseDao.TABLE_NAME + ".purchase_id "
				+ "FROM " + PurchaseDao.TABLE_NAME + " LEFT JOIN composed ON " + PurchaseDao.TABLE_NAME + ".purchase_id = composed.purchase_id "
						+ "WHERE composed.purchase_id IS NULL";


		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next())
				purchase.setPurchaseId(rs.getInt(PurchaseDao.TABLE_NAME + ".purchase_id"));

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
	}
	
	
	
	
	//aggiunto per il range di date
	public synchronized Collection<PurchaseBean> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    Collection<PurchaseBean> orders = new LinkedList<>();

	    String query = "SELECT " +
	                   "    p.purchase_id, " +
	                   "    p.payment_type, " +
	                   "    p.date, " +
	                   "    p.account_id, " +
	                   "    p.address_id, " +
	                   "    p.purchase_phone_number, " +
	                   "    p.purchase_firstname, " +
	                   "    p.purchase_lastname, " +
	                   "    c.product_id, " +
	                   "    c.quantity, " +
	                   "    c.price, " +
	                   "    pr.active, " +
	                   "    pr.product_price, " +
	                   "    pr.product_name, " +
	                   "    pr.product_cover, " +
	                   "    pr.product_description, " +
	                   "    pr.company_id " +
	                   "FROM " +
	                   "    purchase p " +
	                   "JOIN " +
	                   "    composed c ON p.purchase_id = c.purchase_id " +
	                   "JOIN " +
	                   "    product pr ON c.product_id = pr.product_id " +
	                   "WHERE " +
	                   "    p.date BETWEEN ? AND ?";

	    try {
	        connection = ds.getConnection();
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setDate(1, Date.valueOf(startDate));
	        preparedStatement.setDate(2, Date.valueOf(endDate));
	        resultSet = preparedStatement.executeQuery();

	        Map<Integer, PurchaseBean> orderMap = new HashMap<>();

	        while (resultSet.next()) {
	            int purchaseId = resultSet.getInt("purchase_id");
	            PurchaseBean order = orderMap.get(purchaseId);

	            if (order == null) {
	                order = new PurchaseBean();
	                order.setPurchaseId(purchaseId);
	                order.setPaymentType(resultSet.getString("payment_type"));
	                order.setDate(resultSet.getDate("date").toLocalDate());
	                order.setAccountId(resultSet.getInt("account_id"));
	                order.setAddressId(resultSet.getInt("address_id"));
	                order.setPurchasePhoneNumber(resultSet.getString("purchase_phone_number"));
	                order.setPurchaseFirstName(resultSet.getString("purchase_firstname"));
	                order.setPurchaseLastName(resultSet.getString("purchase_lastname"));
	                order.setCart(new Cart(new LinkedList<CartItem>()));
	                orderMap.put(purchaseId, order);
	                orders.add(order);
	            }

	            ProductBean product = new ProductBean();
	            product.setProductId(resultSet.getInt("product_id"));
	            product.setActive(resultSet.getBoolean("active"));
	            product.setProductPrice(resultSet.getBigDecimal("price"));
	            product.setProductName(resultSet.getString("product_name"));
	            product.setProductCover(resultSet.getBytes("product_cover"));
	            product.setProductDescription(resultSet.getString("product_description"));
	            product.setCompanyId(resultSet.getInt("company_id"));

	            CartItem cartItem = new CartItem(product, resultSet.getInt("quantity"));
	            order.getCart().addProduct(product, resultSet.getInt("quantity"));
	        }

	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            // Log or handle exception
	        }
	    }

	    return orders;
	}

	
	
	
	
	
}

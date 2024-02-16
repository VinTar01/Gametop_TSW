package dao;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.sql.DataSource;

import function.ProductComparator;
import model.ProductBean;

public class ProductDao implements GenericDao<ProductBean>{

	private static final String TABLE_NAME = "product";
	private DataSource ds = null;

	public ProductDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Product Model creation....");
	}
	
	@Override
	public synchronized void doSave(ProductBean product) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e le query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    
	    // Ottenimento delle categorie associate al prodotto
	    LinkedHashMap<Integer, Integer> categories = product.getCategories();

	    // Stringhe SQL per l'inserimento del prodotto e delle categorie associate
	    String insertSQL1 = "INSERT INTO " + ProductDao.TABLE_NAME
	            + " (active, product_price, product_name, product_cover, product_description, company_id) VALUES (?, ?, ?, ?, ?, ?)";
	    String insertSQL2 = "INSERT INTO has"
	            + " (category_id, subcategory_id, product_id) VALUES (?, ?, ?)";
	    
	    try {
	        // Connessione al database e disabilitazione dell'autocommit
	        connection = ds.getConnection();
	        connection.setAutoCommit(false);
	        
	        // Preparazione e esecuzione della query per l'inserimento del prodotto
	        preparedStatement = connection.prepareStatement(insertSQL1);
	        preparedStatement.setBoolean(1, product.isActive());
	        preparedStatement.setBigDecimal(2, product.getProductPrice());
	        preparedStatement.setString(3, product.getProductName());
	        preparedStatement.setBinaryStream(4, new ByteArrayInputStream(product.getProductCover()));
	        preparedStatement.setString(5, product.getProductDescription());
	        preparedStatement.setInt(6, product.getCompanyId());

	        preparedStatement.executeUpdate();
	        connection.commit();
	        
	        // Impostazione dell'ID del prodotto appena inserito
	        setProductId(product);
	        System.out.println("\n" + product.getProductId());
	        
	        // Iterazione sulle categorie e preparazione ed esecuzione della query per l'inserimento delle categorie associate
	        for(Map.Entry<Integer, Integer> category : categories.entrySet()) {
	            if (preparedStatement != null)
	                preparedStatement.close();
	            
	            preparedStatement = connection.prepareStatement(insertSQL2);
	            preparedStatement.setInt(1, category.getKey());
	            preparedStatement.setInt(2, category.getValue());
	            preparedStatement.setInt(3, product.getProductId());
	            
	            preparedStatement.executeUpdate();
	        }

	        // Esecuzione del commit per confermare le modifiche nel database
	        connection.commit();
	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
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
	public synchronized void doUpdate(ProductBean product) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e le query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    // Stringa SQL per l'aggiornamento del prodotto
	    String updateSQL = "UPDATE " + ProductDao.TABLE_NAME
	            + " SET active = ?, product_price = ?, product_name = ?, product_cover = ?, product_description = ?,"
	            + " company_id = ? WHERE product_id = ?";

	    try {
	        // Connessione al database e disabilitazione dell'autocommit
	        connection = ds.getConnection();
	        connection.setAutoCommit(false);
	        preparedStatement = connection.prepareStatement(updateSQL);
	        preparedStatement.setBoolean(1, product.isActive());
	        preparedStatement.setBigDecimal(2, product.getProductPrice());
	        preparedStatement.setString(3, product.getProductName());
	        preparedStatement.setBinaryStream(4, new ByteArrayInputStream(product.getProductCover()));
	        preparedStatement.setString(5, product.getProductDescription());
	        preparedStatement.setInt(6, product.getCompanyId());
	        preparedStatement.setInt(7, product.getProductId());

	        // Esecuzione dell'aggiornamento del prodotto
	        preparedStatement.executeUpdate();
	        
	        // Ottenimento delle categorie del prodotto da aggiornare e delle categorie esistenti
	        LinkedHashMap<Integer, Integer> productCategories = product.getCategories();
	        LinkedHashMap<Integer, Integer> beanCategories = doRetrieveByKey(product.getProductId()).getCategories();
	        
	        // Iterazione sulle categorie del prodotto da aggiornare
	        for(Map.Entry<Integer, Integer> category : productCategories.entrySet()) {
	            boolean save = true;
	            
	            // Controllo se la categoria esiste già per il prodotto
	            for(Map.Entry<Integer, Integer> currentCategory : beanCategories.entrySet()) {
	                if(category.getKey() == currentCategory.getKey()) {
	                    save = false;
	                    break;
	                }
	            }
	            
	            // Se la categoria non esiste, la aggiungo
	            if(save) {
	                if (preparedStatement != null)
	                    preparedStatement.close();
	                
	                // Preparazione ed esecuzione della query per l'inserimento della categoria associata al prodotto
	                updateSQL = "INSERT INTO has"
	                        + " (category_id, subcategory_id, product_id) VALUES (?, ?, ?)";
	                preparedStatement = connection.prepareStatement(updateSQL);
	                preparedStatement.setInt(1, category.getKey());
	                preparedStatement.setInt(2, category.getValue());
	                preparedStatement.setInt(3, product.getProductId());
	                
	                preparedStatement.executeUpdate();
	            }
	        }

	        // Esecuzione del commit per confermare le modifiche nel database
	        connection.commit();
	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
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
	public synchronized boolean doDelete(ProductBean product) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e le query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    // Variabile per memorizzare il numero di righe modificate
	    int result = 0;
	    
	    // Stringa SQL per la cancellazione del prodotto
	    String deleteSQL = "DELETE FROM " + ProductDao.TABLE_NAME + " WHERE product_id = ?";

	    try {
	        // Connessione al database e disabilitazione dell'autocommit
	        connection = ds.getConnection();
	        connection.setAutoCommit(false);
	        
	        // Se non è specificata una categoria da cancellare, viene eliminato il prodotto
	        if(product.getDeleteCategory() == 0) {
	            preparedStatement = connection.prepareStatement(deleteSQL);
	            preparedStatement.setInt(1, product.getProductId());
	        }
	        // Altrimenti, viene eliminata solo la relazione tra il prodotto e la categoria specificata
	        else {
	            deleteSQL = "DELETE FROM has WHERE product_id = ? AND subcategory_id = ?";
	            preparedStatement = connection.prepareStatement(deleteSQL);
	            preparedStatement.setInt(1, product.getProductId());
	            preparedStatement.setInt(2, product.getDeleteCategory());
	            product.setDeleteCategory(0);
	        }
	        
	        // Esecuzione della query di eliminazione e memorizzazione del numero di righe modificate
	        result = preparedStatement.executeUpdate();
	        
	        // Esecuzione del commit per confermare le modifiche nel database
	        connection.commit();
	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    // Restituzione true se almeno una riga è stata eliminata, altrimenti false
	    return (result != 0);
	}

	
	@Override
	public synchronized ProductBean doRetrieveByKey(int productId) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e la query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    // LinkedHashMap per memorizzare le categorie del prodotto
	    LinkedHashMap<Integer, Integer> categories = new LinkedHashMap<Integer, Integer>();
	    // Creazione di un nuovo oggetto ProductBean
	    ProductBean bean = new ProductBean();

	    // Stringa SQL per selezionare il prodotto con le sue categorie
	    String selectSQL = "SELECT * FROM " + ProductDao.TABLE_NAME + " NATURAL JOIN has WHERE product_id = ?";

	    try {
	        // Connessione al database e preparazione della query
	        connection = ds.getConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, productId);

	        // Esecuzione della query
	        ResultSet rs = preparedStatement.executeQuery();

	        // Estrazione dei dati dal ResultSet e impostazione dei valori nell'oggetto ProductBean
	        while (rs.next()) {
	            bean.setProductId(rs.getInt("product_id"));
	            bean.setActive(rs.getBoolean("active"));
	            bean.setProductPrice(rs.getBigDecimal("product_price"));
	            bean.setProductName(rs.getString("product_name"));
	            bean.setProductCover(rs.getBytes("product_cover"));
	            bean.setProductDescription(rs.getString("product_description"));
	            bean.setCompanyId(rs.getInt("company_id"));
	            // Aggiunta delle categorie alla mappa categories
	            categories.put(rs.getInt("category_id"), rs.getInt("subcategory_id"));
	        }
	        // Impostazione delle categorie nell'oggetto ProductBean
	        bean.setCategories(categories);

	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    // Restituzione dell'oggetto ProductBean
	    return bean;
	}


	@Override
	public synchronized Collection<ProductBean> doRetrieveAll(String order) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e la query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    // Creazione della collezione per memorizzare i prodotti
	    Collection<ProductBean> products = new LinkedList<ProductBean>();
	    // Variabile per memorizzare il prodotto corrente
	    ProductBean bean = null;
	    // LinkedHashMap per memorizzare le categorie del prodotto corrente
	    LinkedHashMap<Integer, Integer> categories = null;
	    // Variabile per tenere traccia dell'ID del prodotto corrente durante l'iterazione del ResultSet
	    int currentProductId = -1;

	    // Stringa SQL per selezionare tutti i prodotti ordinati per ID del prodotto
	    String selectSQL = "SELECT * FROM " + ProductDao.TABLE_NAME + " NATURAL JOIN has ORDER BY product_id";

	    try {
	        // Connessione al database e preparazione della query
	        connection = ds.getConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);

	        // Esecuzione della query
	        ResultSet rs = preparedStatement.executeQuery();

	        // Iterazione attraverso i risultati del ResultSet
	        while (rs.next()) {
	            int productId = rs.getInt("product_id");
	            // Se il prodotto attuale è diverso dal prodotto precedente, crea un nuovo oggetto ProductBean
	            if (productId != currentProductId) {
	                if (bean != null) {
	                    // Imposta le categorie del prodotto precedente
	                    bean.setCategories(categories);
	                    // Aggiungi il prodotto alla collezione
	                    products.add(bean);
	                }

	                // Crea un nuovo oggetto ProductBean per il prodotto corrente
	                bean = new ProductBean();
	                bean.setProductId(productId);
	                bean.setActive(rs.getBoolean("active"));
	                bean.setProductPrice(rs.getBigDecimal("product_price"));
	                bean.setProductName(rs.getString("product_name"));
	                bean.setProductCover(rs.getBytes("product_cover"));
	                bean.setProductDescription(rs.getString("product_description"));
	                bean.setCompanyId(rs.getInt("company_id"));

	                // Crea un nuovo LinkedHashMap per le categorie del prodotto corrente
	                categories = new LinkedHashMap<Integer, Integer>();

	                // Aggiorna l'ID del prodotto corrente
	                currentProductId = productId;
	            }

	            // Aggiungi la categoria e la sottocategoria al LinkedHashMap delle categorie
	            categories.put(rs.getInt("category_id"), rs.getInt("subcategory_id"));
	        }

	        // Aggiungi l'ultimo prodotto alla collezione
	        if (bean != null) {
	            bean.setCategories(categories);
	            products.add(bean);
	        }

	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    
	    // Ordina la collezione in base all'ordine specificato
	    if (order != null && !order.equals("") && !order.equalsIgnoreCase("product_id")) {
	        ProductComparator comparator = new ProductComparator();
	        comparator.setSortingBy(order);
	        Collections.sort(new LinkedList<ProductBean>(products), comparator);
	    }
	    
	    // Restituisce la collezione dei prodotti
	    return products;
	}

	
	
	
	
	
	/* serve per impostare l'ID del prodotto nel bean del prodotto. 
	 * Questo metodo viene utilizzato nel contesto del salvataggio di un nuovo prodotto nel database. 
	 * Poiché l'ID del prodotto potrebbe essere generato automaticamente dal database e non fornito dall'utente, 
	 * il metodo cerca l'ID del prodotto non associato (cioè un ID che non è già presente nella tabella di associazione has) 
	 * e lo imposta nel bean del prodotto prima di inserire il nuovo prodotto nel database.
	 *  In breve, questo metodo aiuta a garantire che venga assegnato un ID univoco
	 *  a ciascun nuovo prodotto che viene salvato nel database.
	 */
	
	private void setProductId(ProductBean product) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e la query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    // Definizione della query SQL per selezionare l'ID del prodotto non associato
	    String selectSQL = "SELECT " + ProductDao.TABLE_NAME + ".product_id "
	            + "FROM " + ProductDao.TABLE_NAME + " LEFT JOIN has ON " + ProductDao.TABLE_NAME + ".product_id = has.product_id "
	                    + "WHERE has.product_id IS NULL";

	    try {
	        // Connessione al database
	        connection = ds.getConnection();
	        // Preparazione della dichiarazione SQL
	        preparedStatement = connection.prepareStatement(selectSQL);

	        // Esecuzione della query SQL
	        ResultSet rs = preparedStatement.executeQuery();

	        // Iterazione sui risultati della query
	        while (rs.next())
	            // Impostazione dell'ID del prodotto nel bean del prodotto
	            product.setProductId(rs.getInt(ProductDao.TABLE_NAME + ".product_id"));

	    } finally {
	        // Chiusura delle risorse
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	}
}

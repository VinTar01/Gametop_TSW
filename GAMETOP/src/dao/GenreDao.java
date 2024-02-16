package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.GenreBean;

public class GenreDao implements GenericDao<GenreBean>{

	private static final String TABLE_NAME = "genre";
	private DataSource ds = null;

	public GenreDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Account Model creation....");
	}
	
	@Override
	public synchronized void doSave(GenreBean genre) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Integer> products = genre.getProducts();

		String insertSQL1 = "INSERT INTO " + GenreDao.TABLE_NAME
				+ " (genre_name) VALUES (?)";

		String insertSQL2 = "INSERT INTO Includes"
				+ " (product_id, genre_id) VALUES (?, ?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL1);
			preparedStatement.setString(1, genre.getGenreName());

			preparedStatement.executeUpdate();
			
			for(int productId : products) {
				if (preparedStatement != null)
					preparedStatement.close();
				
				preparedStatement = connection.prepareStatement(insertSQL2);
				preparedStatement.setInt(1, productId);
				preparedStatement.setInt(2, genre.getGenreId());
				
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
	public synchronized void doUpdate(GenreBean genre) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE " + GenreDao.TABLE_NAME
				+ " SET genre_name = ? WHERE genre_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, genre.getGenreName());
			preparedStatement.setInt(1, genre.getGenreId());;

			preparedStatement.executeUpdate();
				
			Collection<Integer> genreProducts = genre.getProducts();
				
			Collection<Integer> beanProducts = doRetrieveByKey(genre.getGenreId()).getProducts();
				
			boolean save = true;
				
			for(int productId : genreProducts) {
					
				for(int currentProductId : beanProducts) {
					if(productId == currentProductId) {
						save = false;
						break;
					}
				}
				if(save) {
					if (preparedStatement != null)
						preparedStatement.close();
						
					updateSQL = "INSERT INTO includes"
							+ " (product_id, genre_id) VALUES (?, ?)";
						
					preparedStatement = connection.prepareStatement(updateSQL);
					preparedStatement.setInt(1, productId);
					preparedStatement.setInt(2, genre.getGenreId());
						
					preparedStatement.executeUpdate();
				}
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
	public synchronized boolean doDelete(GenreBean genre) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE " + GenreDao.TABLE_NAME + ", includes"
				+ " FROM " + GenreDao.TABLE_NAME + " NATURAL JOIN includes WHERE genre_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			
			if(genre.getDeleteProductId() == 0) {
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.setInt(1, genre.getGenreId());
			}
			else {
				deleteSQL = "DELETE FROM includes WHERE genre_id = ? AND product_id = ?";
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.setInt(1, genre.getGenreId());
				preparedStatement.setInt(2, genre.getDeleteProductId());
				genre.setDeleteProductId(0);
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
	public synchronized GenreBean doRetrieveByKey(int genreId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		GenreBean bean = new GenreBean();
		LinkedList<Integer> products = new LinkedList<Integer>(); 

		String selectSQL = "SELECT * FROM " + GenreDao.TABLE_NAME + " NATURAL JOIN includes WHERE genre_id = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, genreId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setGenreId(rs.getInt("genre_id"));
				bean.setGenreName(rs.getString("genre_name"));
				products.add(rs.getInt("product_id"));
			}
			
			bean.setProducts(products);

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return bean;
	}

	@Override
	public synchronized Collection<GenreBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<GenreBean> genres = new LinkedList<GenreBean>();
		GenreBean bean = null;
		LinkedList<Integer> products = null;
		int currentGenreId = -1;

		String selectSQL = "SELECT * FROM " + GenreDao.TABLE_NAME + " NATURAL JOIN includes ORDER BY genre_id";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int genreId = rs.getInt("genre_id");
				String genreName = rs.getString("genre_name");
			    if (genreId != currentGenreId) {
			        // Nuovo genere, crea un nuovo GenreBean
			        if (bean != null) {
			           bean.setProducts(products); // Imposta i prodotti del genere precedente
			           genres.add(bean); // Aggiungi il genere alla collezione
			        }

			        // Crea un nuovo GenreBean per il genere corrente
			        bean = new GenreBean();
			        bean.setGenreId(genreId);
			        bean.setGenreName(genreName);

			        // Crea un nuovo LinkedList per i prodotti del genere corrente
			        products = new LinkedList<Integer>();

			        currentGenreId = genreId;
			    }

			    // Aggiunge il prodotto al LinkedList dei prodotti
			    products.add(rs.getInt("product_id"));
			}

			// Aggiungi l'ultimo genere alla collezione
			if (bean != null) {
			    bean.setProducts(products);
			    genres.add(bean);
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
		
		if (order != null && !order.equals("") && !order.equalsIgnoreCase("genre_id")) {
			Collections.sort(new LinkedList<GenreBean>(genres), 
					(GenreBean genre1, GenreBean genre2) -> {return genre1.getGenreName().compareTo(genre2.getGenreName());});
		}
		
		return genres;
	}
	
}

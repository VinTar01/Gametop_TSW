package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.CategoryBean;

public class CategoryDao implements GenericDao<CategoryBean>{

	private static final String TABLE_NAME = "category";
	private DataSource ds = null;

	public CategoryDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Category Model creation....");
	}
	
	@Override
	public synchronized void doSave(CategoryBean category) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + CategoryDao.TABLE_NAME
				+ " (category_name) VALUES (?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, category.getCategoryName());

			preparedStatement.executeUpdate();

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
	public synchronized void doUpdate(CategoryBean category) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE " + CategoryDao.TABLE_NAME
				+ " SET category_name = ? WHERE category_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, category.getCategoryName());
			preparedStatement.setInt(2, category.getCategoryId());

			preparedStatement.executeUpdate();

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
	public synchronized boolean doDelete(CategoryBean category) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + CategoryDao.TABLE_NAME + " WHERE category_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, category.getCategoryId());

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
	public synchronized CategoryBean doRetrieveByKey(int categoryId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		CategoryBean bean = new CategoryBean();

		String selectSQL = "SELECT * FROM " + CategoryDao.TABLE_NAME + " WHERE category_id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, categoryId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setCategoryId(rs.getInt("category_id"));
				bean.setCategoryName(rs.getString("category_name"));
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
		return bean;
	}

	@Override
	public synchronized Collection<CategoryBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<CategoryBean> categories = new LinkedList<CategoryBean>();

		String selectSQL = "SELECT * FROM " + CategoryDao.TABLE_NAME;

		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				CategoryBean bean = new CategoryBean();

				bean.setCategoryId(rs.getInt("category_id"));
				bean.setCategoryName(rs.getString("category_name"));
				categories.add(bean);
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
		return categories;
	}
	
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.AddressBean;

public class AddressDao implements GenericDao<AddressBean>{

	private static final String TABLE_NAME = "address";
	private DataSource ds = null;

	public AddressDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Address Model creation....");
	}
	
	@Override
	public synchronized void doSave(AddressBean address) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + AddressDao.TABLE_NAME
				+ " (zip, country, city, street) VALUES (?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setInt(1, address.getZip());
			preparedStatement.setString(2, address.getCountry());
			preparedStatement.setString(3, address.getCity());
			preparedStatement.setString(4, address.getStreet());
	

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
	public synchronized void doUpdate(AddressBean address) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE " + AddressDao.TABLE_NAME
				+ " SET zip = ?, country = ?, city = ?, street = ? WHERE address_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setInt(1, address.getZip());
			preparedStatement.setString(2, address.getCountry());
			preparedStatement.setString(3, address.getCity());
			preparedStatement.setString(4, address.getStreet());
			preparedStatement.setInt(5, address.getAddressId());

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
	public synchronized boolean doDelete(AddressBean address) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + AddressDao.TABLE_NAME + " WHERE address_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, address.getAddressId());

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
	public synchronized AddressBean doRetrieveByKey(int addressId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		AddressBean bean = new AddressBean();

		String selectSQL = "SELECT * FROM " + AddressDao.TABLE_NAME + " WHERE address_id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, addressId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setAddressId(rs.getInt("address_id"));
				bean.setZip(rs.getInt("zip"));
				bean.setCountry(rs.getString("country"));
				bean.setCity(rs.getString("city"));
				bean.setStreet(rs.getString("street"));
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
	public synchronized Collection<AddressBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<AddressBean> addresses = new LinkedList<AddressBean>();

		String selectSQL = "SELECT * FROM " + AddressDao.TABLE_NAME;

		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				AddressBean bean = new AddressBean();

				bean.setAddressId(rs.getInt("address_id"));
				bean.setZip(rs.getInt("zip"));
				bean.setCountry(rs.getString("country"));
				bean.setCity(rs.getString("city"));
				bean.setStreet(rs.getString("street"));
				addresses.add(bean);
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
		return addresses;
	}
	
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.CompanyBean;

public class CompanyDao implements GenericDao<CompanyBean>{

	private static final String TABLE_NAME = "company";
	private DataSource ds = null;

	public CompanyDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Company Model creation....");
	}
	
	@Override
	public synchronized void doSave(CompanyBean company) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + CompanyDao.TABLE_NAME
				+ " (company_name, company_description) VALUES (?, ?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, company.getCompanyName());
			preparedStatement.setString(2, company.getCompanyDescription());

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
	public synchronized void doUpdate(CompanyBean company) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE " + CompanyDao.TABLE_NAME
				+ " SET company_name = ?, company_description = ? WHERE company_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, company.getCompanyName());
			preparedStatement.setString(2, company.getCompanyDescription());
			preparedStatement.setInt(3, company.getCompanyId());

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
	public synchronized boolean doDelete(CompanyBean company) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + CompanyDao.TABLE_NAME + " WHERE company_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, company.getCompanyId());

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
	public synchronized CompanyBean doRetrieveByKey(int companyId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		CompanyBean bean = new CompanyBean();

		String selectSQL = "SELECT * FROM " + CompanyDao.TABLE_NAME + " WHERE company_id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, companyId);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setCompanyId(rs.getInt("company_id"));
				bean.setCompanyName(rs.getString("company_name"));
				bean.setCompanyDescription(rs.getString("company_description"));
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
	public synchronized Collection<CompanyBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<CompanyBean> companies = new LinkedList<CompanyBean>();

		String selectSQL = "SELECT * FROM " + CompanyDao.TABLE_NAME;

		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				CompanyBean bean = new CompanyBean();

				bean.setCompanyId(rs.getInt("company_id"));
				bean.setCompanyName(rs.getString("company_name"));
				bean.setCompanyDescription(rs.getString("company_description"));
				companies.add(bean);
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
		return companies;
	}
	
}

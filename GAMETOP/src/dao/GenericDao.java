package dao;

import java.sql.SQLException;
import java.util.Collection;

public interface GenericDao<T> {

	public void doSave(T bean) throws SQLException;
	
	public void doUpdate(T bean) throws SQLException;
	
	public boolean doDelete(T bean) throws SQLException;
	
	public T doRetrieveByKey(int param) throws SQLException;
	
	public Collection<T> doRetrieveAll(String order) throws SQLException;
	
}

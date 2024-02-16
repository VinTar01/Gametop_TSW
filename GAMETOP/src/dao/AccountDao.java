package dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.AccountBean;

public class AccountDao implements GenericDao<AccountBean> {

	private static final String TABLE_NAME = "account";
	private DataSource ds = null;

	public AccountDao(DataSource ds) {
		this.ds = ds;
		
		System.out.println("DataSource Account Model creation....");
	}
	
	@Override
	public synchronized void doSave(AccountBean account) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + AccountDao.TABLE_NAME
				+ " (account_firstname, account_lastname, email, password, birthdate) VALUES (?, ?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, account.getAccountFirstName());
			preparedStatement.setString(2, account.getAccountLastName());
			preparedStatement.setString(3, account.getEmail());
			preparedStatement.setString(4, account.getPassword());
			preparedStatement.setDate(5, Date.valueOf(account.getBirthDate()));

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
	public synchronized void doUpdate(AccountBean account) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE " + AccountDao.TABLE_NAME
				+ " SET email = ?, password = ?, account_cover = ? WHERE account_id = ?";

		try {
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, account.getEmail());
			preparedStatement.setString(2, account.getPassword());
			if(account.getAccountCover() != null)
				preparedStatement.setBinaryStream(3, new ByteArrayInputStream(account.getAccountCover()));
			else
				preparedStatement.setBinaryStream(3, null);
			preparedStatement.setInt(4, account.getAccountId());

			preparedStatement.executeUpdate();
			
			//vedo se l'account passato da aggiornare ha la map di indirizzi diversa da null
			if(account.getAddresses() != null) {
				
				//contiene gli indirizzi attualmente associati all'account che si sta aggiornando (indirizzi aggiornati)
				LinkedHashMap<Integer, List<String>> accountAddresses = account.getAddresses();
				
				//ottengo l'account passato come parametro
				AccountBean bean = doRetrieveByKey(account.getAccountId());
				
				//contiene gli indirizzi recuperati dal database per l'account corrente prima dell'aggiornamento
				LinkedHashMap<Integer, List<String>> beanAddresses = bean.getAddresses();
				
				boolean update = false;
				
				/*Per ogni indirizzo associato all'account, 
				 * viene controllato se esiste già un record corrispondente nella tabella save che mantiene i dettagli dell'indirizzo 
				 * associato all'account. 
				 * Viene eseguito un controllo comparando gli indirizzi presenti nell'account con quelli presenti nel database. 
				 * Se un indirizzo è già presente, viene impostata una variabile booleana update su true.
				 */
				
				for(Map.Entry<Integer, List<String>> address : accountAddresses.entrySet()) {
					
					if(beanAddresses != null) {
						for(Map.Entry<Integer, List<String>> currentAddress : beanAddresses.entrySet()) {
							if(address.getKey() == currentAddress.getKey()) {
								update = true;
								break;
							}
						}
					}
					if (preparedStatement != null)
						preparedStatement.close();
					
					/*Viene preparata una nuova query per l'inserimento o l'aggiornamento 
					 * dell'indirizzo nella tabella save, a seconda se l'indirizzo è già presente o meno.
					Viene eseguito l'aggiornamento o l'inserimento dell'indirizzo tramite preparedStatement.executeUpdate().*/
					
					if(update) {
						updateSQL = "UPDATE save"
								+ " SET account_phone_number = ?, firstname = ?, lastname = ?  WHERE account_id = ? AND address_id = ?";
						
						preparedStatement = connection.prepareStatement(updateSQL);
						preparedStatement.setString(1, (address.getValue().get(0)));
						preparedStatement.setString(2, address.getValue().get(1));
						preparedStatement.setString(3, address.getValue().get(2));
						preparedStatement.setInt(4, account.getAccountId());
						preparedStatement.setInt(5, address.getKey());
					}
					else {
						updateSQL = "INSERT INTO save"
								+ " (account_id, address_id, account_phone_number, firstname, lastname) VALUES (?, ?, ?, ?, ?)";
						
						preparedStatement = connection.prepareStatement(updateSQL);
						preparedStatement.setInt(1, account.getAccountId());
						preparedStatement.setInt(2, address.getKey());
						preparedStatement.setString(3, address.getValue().get(0));
						preparedStatement.setString(4, address.getValue().get(1));
						preparedStatement.setString(5, address.getValue().get(2));
					}
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

	/* elimina un record dalla tabella degli account, 
	 * eventualmente anche un record associato dalla tabella degli indirizzi se l'ID dell'indirizzo da eliminare non è zero. 
	 * Alla fine, restituisce true se almeno una riga è stata eliminata con successo, altrimenti restituisce false.
	 */
	@Override
	public synchronized boolean doDelete(AccountBean account) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    int result = 0;

	    String deleteSQL = "DELETE FROM " + AccountDao.TABLE_NAME + " WHERE account_id = ?";

	    try {
	        connection = ds.getConnection();
	        connection.setAutoCommit(false);
	        
	        // Verifica se l'ID dell'indirizzo da eliminare è zero
	        if(account.getDeleteAddressId() == 0) {
	            // Se l'ID dell'indirizzo da eliminare è zero, esegui la query di eliminazione solo per l'account
	            preparedStatement = connection.prepareStatement(deleteSQL);
	            preparedStatement.setInt(1, account.getAccountId());
	        }
	        else {
	            // Se l'ID dell'indirizzo da eliminare è diverso da zero, esegui la query di eliminazione sia per l'account che per l'indirizzo specificato
	            deleteSQL = "DELETE FROM save WHERE account_id = ? AND address_id = ?";
	            preparedStatement = connection.prepareStatement(deleteSQL);
	            preparedStatement.setInt(1, account.getAccountId());
	            preparedStatement.setInt(2, account.getDeleteAddressId());
	            account.setDeleteAddressId(0); // Reimposta l'ID dell'indirizzo da eliminare a zero dopo l'eliminazione
	        }

	        // Esegue la query di eliminazione e memorizza il numero di righe modificate
	        result = preparedStatement.executeUpdate();
	        connection.commit(); // Conferma la transazione nel database
	    } finally {
	        // Chiude le risorse aperte
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    // Restituisce true se almeno una riga è stata eliminata, altrimenti restituisce false
	    return (result != 0);
	}

	
	/* recupera un account dal database utilizzando l'ID dell'account come chiave primaria. 
	 * Recupera i dati dell'account dalla tabella degli account e gli indirizzi associati a quell'account
	 *  dalla tabella save. Successivamente, crea un oggetto AccountBean, 
	 *  imposta i dati recuperati e gli indirizzi associati a questo account e lo restituisce.
	 */

	@Override
	public synchronized AccountBean doRetrieveByKey(int accountId) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement1 = null;
	    PreparedStatement preparedStatement2 = null;

	    // Crea un nuovo oggetto AccountBean per contenere i dati recuperati
	    AccountBean bean = new AccountBean();
	    // Crea una mappa per memorizzare gli indirizzi associati a questo account
	    LinkedHashMap<Integer, List<String>> addresses = new LinkedHashMap<Integer, List<String>>();
	    // Lista per memorizzare i valori degli indirizzi
	    List<String> addressesValues;

	    // Query SQL per recuperare i dati dell'account
	    String selectSQL = "SELECT * FROM " + AccountDao.TABLE_NAME + " WHERE account_id = ?";

	    try {
	        connection = ds.getConnection();
	        // Prepara la query per recuperare i dati dell'account
	        preparedStatement1 = connection.prepareStatement(selectSQL);
	        preparedStatement1.setInt(1, accountId);

	        // Esegue la query per recuperare i dati dell'account
	        ResultSet rs = preparedStatement1.executeQuery();
	        
	        // Query SQL per recuperare gli indirizzi associati a questo account
	        selectSQL = "SELECT * FROM save WHERE account_id = ?";

	        while (rs.next()) {
	            // Imposta i dati dell'account nell'oggetto bean
	            bean.setAccountId(accountId);
	            bean.setAccountFirstName(rs.getString("account_firstname"));
	            bean.setAccountLastName(rs.getString("account_lastname"));
	            bean.setEmail(rs.getString("email"));
	            bean.setPassword(rs.getString("password"));
	            bean.setAccountCover(rs.getBytes("account_cover"));
	            bean.setBirthDate(rs.getDate("birthdate").toLocalDate());
	            
	            // Prepara la query per recuperare gli indirizzi associati a questo account
	            preparedStatement2 = connection.prepareStatement(selectSQL);
	            preparedStatement2.setInt(1, accountId);
	            
	            // Esegue la query per recuperare gli indirizzi associati a questo account
	            ResultSet ps = preparedStatement2.executeQuery();
	            
	            while(ps.next()) {
	                // Crea una lista per memorizzare i valori dell'indirizzo
	                addressesValues = new LinkedList<String>();
	                // Aggiunge i valori dell'indirizzo alla lista
	                addressesValues.add(ps.getString("account_phone_number"));
	                addressesValues.add(ps.getString("firstname"));
	                addressesValues.add(ps.getString("lastname"));
	                // Memorizza i valori dell'indirizzo nella mappa utilizzando l'ID dell'indirizzo come chiave
	                addresses.put(ps.getInt("address_id"), addressesValues);
	            }
	        }
	        
	        // Se non ci sono indirizzi associati a questo account, imposto l'oggetto a null
	        if(addresses.size() == 0)
	            addresses = null;
	        
	        // Imposta gli indirizzi recuperati nell'oggetto bean
	        bean.setAddresses(addresses);

	    } finally {
	        // Chiude le risorse aperte (connessioni e statement)
	        try {
	            if (preparedStatement1 != null)
	                preparedStatement1.close();
	            if (preparedStatement2 != null)
	                preparedStatement2.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    // Restituisce l'oggetto bean contenente i dati recuperati dall'account
	    return bean;
	}


	@Override
	public synchronized Collection<AccountBean> doRetrieveAll(String order) throws SQLException {
	    // Dichiarazione delle variabili per la connessione al database e le query SQL
	    Connection connection = null;
	    PreparedStatement preparedStatement1 = null;
	    PreparedStatement preparedStatement2 = null;
	    
	    // Creazione di una lista per memorizzare gli account recuperati
	    Collection<AccountBean> accounts = new LinkedList<AccountBean>();
	    
	    // Variabili temporanee per memorizzare i dati degli account e degli indirizzi
	    AccountBean bean = null;
	    LinkedHashMap<Integer, List<String>> addresses = null;
	    List<String> addressesValues = null;
	    
	    // Stringa SQL per selezionare tutti gli account
	    String selectSQL = "SELECT * FROM " + AccountDao.TABLE_NAME;
	    
	    // Aggiunta dell'ordinamento alla query SQL, se specificato
	    if (order != null && !order.equals("")) {
	        selectSQL += " ORDER BY " + order;
	    }
	    
	    try {
	        // Connessione al database e preparazione della query SQL per gli account
	        connection = ds.getConnection();
	        preparedStatement1 = connection.prepareStatement(selectSQL);
	        
	        // Esecuzione della query per selezionare tutti gli account
	        ResultSet rs = preparedStatement1.executeQuery();
	        
	        // Preparazione della query SQL per selezionare gli indirizzi associati a ciascun account
	        selectSQL = "SELECT * FROM save WHERE account_id = ?";
	        
	        // Iterazione su tutti gli account trovati
	        while (rs.next()) {
	            // Creazione di un nuovo oggetto AccountBean e inizializzazione delle sue proprietà
	            addresses = new LinkedHashMap<Integer, List<String>>();
	            bean = new AccountBean();
	            int accountId = rs.getInt("account_id");
	            bean.setAccountId(accountId);
	            bean.setAccountFirstName(rs.getString("account_firstname"));
	            bean.setAccountLastName(rs.getString("account_lastname"));
	            bean.setEmail(rs.getString("email"));
	            bean.setPassword(rs.getString("password"));
	            bean.setAccountCover(rs.getBytes("account_cover"));
	            bean.setBirthDate(rs.getDate("birthdate").toLocalDate());
	            
	            // Preparazione della query per selezionare gli indirizzi associati all'account corrente
	            preparedStatement2 = connection.prepareStatement(selectSQL);
	            preparedStatement2.setInt(1, accountId);
	            
	            // Esecuzione della query per selezionare gli indirizzi associati all'account corrente
	            ResultSet ps = preparedStatement2.executeQuery();
	            
	            // Iterazione su tutti gli indirizzi trovati
	            while (ps.next()) {
	                // Creazione di una lista per memorizzare i dettagli dell'indirizzo
	                addressesValues = new LinkedList<String>();
	                addressesValues.add(ps.getString("account_phone_number"));
	                addressesValues.add(ps.getString("firstname"));
	                addressesValues.add(ps.getString("lastname"));
	                
	                // Aggiunta dei dettagli dell'indirizzo alla mappa degli indirizzi
	                addresses.put(ps.getInt("address_id"), addressesValues);
	            }
	            
	            // Se non sono stati trovati indirizzi, impostare la mappa degli indirizzi su null
	            if (addresses.size() == 0)
	                addresses = null;
	            
	            // Impostazione degli indirizzi trovati nell'oggetto AccountBean
	            bean.setAddresses(addresses);
	            
	            // Aggiunta dell'account alla lista di account
	            accounts.add(bean);
	        }
	        
	    } finally {
	        // Chiusura delle risorse e rilascio della connessione al database
	        try {
	            if (preparedStatement1 != null)
	                preparedStatement1.close();
	            if (preparedStatement2 != null)
	                preparedStatement2.close();
	        } finally {
	            if (connection != null)
	                connection.close();
	        }
	    }
	    
	    // Restituzione della lista di account recuperati dal database
	    return accounts;
	}

	
}

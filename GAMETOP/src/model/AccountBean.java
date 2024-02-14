package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;




public class AccountBean implements Serializable {

	private static final long serialVersionUID = -3329531942555081186L;
	
	private int accountId, deleteAddressId;
	private byte[] accountCover;
	private String  accountFirstName, accountLastName, email, password;
	private LocalDate birthDate;
	private LinkedHashMap<Integer, List<String>> addresses; //per ogni entry(indirizzo) ho una lista di stringhe(info indirizzo della tabella save)
	
	public AccountBean() {
		super();
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getDeleteAddressId() {
		return deleteAddressId;
	}

	public void setDeleteAddressId(int deleteAddressId) {
		this.deleteAddressId = deleteAddressId;
	}

	public byte[] getAccountCover() {
		return accountCover;
	}

	public void setAccountCover(byte[] accountCover) {
		this.accountCover = accountCover;
	}

	public String getAccountFirstName() {
		return accountFirstName;
	}

	public void setAccountFirstName(String accountFirstName) {
		this.accountFirstName = accountFirstName;
	}

	public String getAccountLastName() {
		return accountLastName;
	}

	public void setAccountLastName(String accountLastName) {
		this.accountLastName = accountLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LinkedHashMap<Integer, List<String>> getAddresses() {
		return addresses;
	}

	public void setAddresses(LinkedHashMap<Integer, List<String>> addresses) {
		this.addresses = addresses;
	}

}

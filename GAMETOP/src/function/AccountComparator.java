package function;

import java.util.Arrays;
import java.util.Comparator;

import model.AccountBean;

public class AccountComparator implements Comparator<AccountBean>{

	private enum Account {account_firstname, account_lastname, email, password, account_cover, birthdate}
	private Account sortingBy;

	@Override
	public int compare(AccountBean account1, AccountBean account2) {
		switch(sortingBy) {
		case account_firstname: return account1.getAccountFirstName().compareTo(account2.getAccountFirstName());
		case account_lastname : return account1.getAccountLastName().compareTo(account2.getAccountLastName());
		case email: return account1.getEmail().compareTo(account2.getEmail());
		case password: return account1.getPassword().compareTo(account2.getPassword());
		case account_cover: return Arrays.compare(account1.getAccountCover(), account2.getAccountCover());
		case birthdate: return account1.getBirthDate().compareTo(account2.getBirthDate());
		}
		throw new RuntimeException("Invalid sorter parameter");
	}

	public void setSortingBy(String sortBy) {
		this.sortingBy = Account.valueOf(sortBy);
	}
	
}

package control.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import dao.AccountDao;
import dao.AddressDao;
import dao.PurchaseDao;
import model.AccountBean;
import model.AddressBean;
import model.PurchaseBean;

/**
 * Servlet implementation class AddressControl
 */
@WebServlet("/User/AddressControl")
public class AddressControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddressControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("operation") != null) {	
			AccountBean account = (AccountBean) request.getSession().getAttribute("account");
			AccountDao accountDao = new AccountDao((DataSource) getServletContext().getAttribute("DataSource"));
			AddressDao addressDao = new AddressDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			response.setContentType("application/json");
			
			try {
				if(request.getParameter("operation").equals("insert")) {
					
					boolean find = false;
					AddressBean address = new AddressBean();
					LinkedList<String> personalData = new LinkedList<String>();
					LinkedList<String> list = new LinkedList<String>();
					LinkedHashMap<Integer, List<String>> addresses =  new LinkedHashMap<Integer, List<String>>();
					
					list.add(request.getParameter("name"));
					list.add(request.getParameter("surname"));
					list.add(request.getParameter("phone"));
					list.add(request.getParameter("country"));
					list.add(request.getParameter("city"));
					list.add(request.getParameter("zip"));
					list.add(request.getParameter("street"));
					
					address.setCountry(list.get(3));
					address.setCity(list.get(4));
					address.setZip(Integer.parseInt(list.get(5)));
					address.setStreet(list.get(6));
					
					for(AddressBean currentAddress : addressDao.doRetrieveAll("")) {
						if(currentAddress.getCountry().equals(address.getCountry()) && currentAddress.getCity().equals(address.getCity()) &&
						   currentAddress.getZip() == address.getZip() && currentAddress.getStreet().equals(address.getStreet())) {
						   	address.setAddressId(currentAddress.getAddressId());
						   	find = true;
						   	break;
						}
					}
					
					if(!find) {
						addressDao.doSave(address);
						
						for(AddressBean currentAddress : addressDao.doRetrieveAll("")) {
							if(currentAddress.getCountry().equals(address.getCountry()) && currentAddress.getCity().equals(address.getCity()) &&
							   currentAddress.getZip() == address.getZip() && currentAddress.getStreet().equals(address.getStreet())) {
							   	address.setAddressId(currentAddress.getAddressId());
							   	break;
							}
						}
					}
					
					personalData.add(list.get(2));
					personalData.add(list.get(0));
					personalData.add(list.get(1));
					
					list.addFirst(address.getAddressId() + "");
					
					addresses.put(address.getAddressId(), personalData);
					
					account.setAddresses(addresses);
					
					accountDao.doUpdate(account);
					
					
					response.getWriter().write(new Gson().toJson(list));
				}
				else if(request.getParameter("operation").equals("update")) {
					
					boolean save = false, update = true;
					LinkedList<String> list = new LinkedList<String>();
					list.add(request.getParameter("id"));
					
					AddressBean address = addressDao.doRetrieveByKey(Integer.parseInt(list.get(0)));
					LinkedList<String> personalData = new LinkedList<String>();
					LinkedHashMap<Integer, List<String>> addresses =  new LinkedHashMap<Integer, List<String>>();
					
					PurchaseDao purchaseDao = new PurchaseDao((DataSource) getServletContext().getAttribute("DataSource"));
					
					list.add(request.getParameter("name"));
					list.add(request.getParameter("surname"));
					list.add(request.getParameter("phone"));
					list.add(request.getParameter("country"));
					list.add(request.getParameter("city"));
					list.add(request.getParameter("zip"));
					list.add(request.getParameter("street"));
					
					if(address.getCountry().equals(list.get(4)) && address.getCity().equals(list.get(5)) && address.getZip() == Integer.parseInt(list.get(6)) &&
					   address.getStreet().equals(list.get(7))) {
						
						System.out.println("Caso 1: il nuovo AddressBean ha gli stessi dati precedenti");
						
						personalData.add(list.get(3));
						personalData.add(list.get(1));
						personalData.add(list.get(2));
						
						addresses.put(address.getAddressId(), personalData);
						
						account.setAddresses(addresses);
						
						accountDao.doUpdate(account);
					}
					else {
						
						for(PurchaseBean purchase : purchaseDao.doRetrieveAll("")) {
							if(Integer.parseInt(list.get(0)) == purchase.getAddressId()) {
								System.out.println("Caso 2: l'AddressBean da aggiornare è presente in un PurchaseBean");
								save = true;
								break;
							}
						}
					
						if(!save) {
							
							for(AccountBean currentAccount : accountDao.doRetrieveAll("")) {
								if(currentAccount.getAddresses() != null) {
									for(Map.Entry<Integer, List<String>> currentAccountAddress : currentAccount.getAddresses().entrySet()) {
										if(account.getAccountId() != currentAccount.getAccountId() && Integer.parseInt(list.get(0)) == currentAccountAddress.getKey()) {
											System.out.println("Caso 3: l'AddressBean da aggiornare è presente in un'altro AccountBean");
											save = true;
											break;
										}
									}
								}
							}
					
							if(!save) {
								int newAddressId = 0;
								
								for(AddressBean currentAddress : addressDao.doRetrieveAll("")) {
									if(currentAddress.getCountry().equals(list.get(4)) && currentAddress.getCity().equals(list.get(5)) && currentAddress.getZip() == Integer.parseInt(list.get(6)) &&
									   currentAddress.getStreet().equals(list.get(7))) {
										System.out.println("Caso 4: il nuovo AddressBean da salvare è già presente nel DB");
										update = false;
										newAddressId = currentAddress.getAddressId();
										break;
									}
								}
								
								if(update) {
									System.out.println("Caso 5: il nuovo AddressBean da salvare non è presente nel DB, aggiorniamo semplicemente l'AddressBean eseguendo il doUpdate dell'AddressDao");
									
									address.setCountry(list.get(4));
									address.setCity(list.get(5));
									address.setZip(Integer.parseInt(list.get(6)));
									address.setStreet(list.get(7));
									
									addressDao.doUpdate(address);
									
									personalData.add(list.get(3));
									personalData.add(list.get(1));
									personalData.add(list.get(2));
									
									addresses.put(address.getAddressId(), personalData);
									
									account.setAddresses(addresses);
									
									accountDao.doUpdate(account);
								}
								else {
									account.setDeleteAddressId(Integer.parseInt(list.get(0)));
									accountDao.doDelete(account);
									account.setAddresses(null);
									
									personalData.add(list.get(3));
									personalData.add(list.get(1));
									personalData.add(list.get(2));
									
									addresses.put(newAddressId, personalData);
									
									account.setAddresses(addresses);
									
									accountDao.doUpdate(account);
									
									list.set(0, newAddressId + "");
									
									boolean delete = true;
									
									for(PurchaseBean order : purchaseDao.doRetrieveAll("")) {
										if(address.getAddressId() == order.getAddressId()) {
											delete = false;
											break;
										}
									}
									
									if(delete) {
										for(AccountBean currentAccount : accountDao.doRetrieveAll("")) {
											if(currentAccount.getAddresses() != null) {
												for(Map.Entry<Integer, List<String>> currentAddress : currentAccount.getAddresses().entrySet()) {
													if(currentAccount.getAccountId() != account.getAccountId() && address.getAddressId() == currentAddress.getKey()) {
														delete = false;
														break;
													}
												}
											}
											
											if(!delete)
												break;
										}
									}
									
									if(delete)
										addressDao.doDelete(addressDao.doRetrieveByKey(address.getAddressId()));
								}
							}
						}
						
						if(save) {
							for(AddressBean currentAddress : addressDao.doRetrieveAll("")) {
								if(currentAddress.getCountry().equals(list.get(4)) && currentAddress.getCity().equals(list.get(5)) && currentAddress.getZip() == Integer.parseInt(list.get(6)) &&
								   currentAddress.getStreet().equals(list.get(7))) {
									System.out.println("Caso 2/3 + Caso 4: il nuovo AddressBean da salvare è già presente nel DB");
									save = false;
									address.setAddressId(currentAddress.getAddressId());
									break;
								}
							}
							
							if(save) {
								address.setCountry(list.get(4));
								address.setCity(list.get(5));
								address.setZip(Integer.parseInt(list.get(6)));
								address.setStreet(list.get(7));
							
								addressDao.doSave(address);
							
								for(AddressBean currentAddress : addressDao.doRetrieveAll("")) {
									if(currentAddress.getCountry().equals(list.get(4)) && currentAddress.getCity().equals(list.get(5)) && currentAddress.getZip() == Integer.parseInt(list.get(6)) &&
											currentAddress.getStreet().equals(list.get(7))) {
									
										address.setAddressId(currentAddress.getAddressId());
										break;
									}
								}
							}
							
							account.setDeleteAddressId(Integer.parseInt(list.get(0)));
							accountDao.doDelete(account);
							account.setAddresses(null);
							
							personalData.add(list.get(3));
							personalData.add(list.get(1));
							personalData.add(list.get(2));
							
							addresses.put(address.getAddressId(), personalData);
							
							account.setAddresses(addresses);
							
							accountDao.doUpdate(account);
							
							list.set(0, address.getAddressId() + "");
						}
					}
					
					response.getWriter().write(new Gson().toJson(list));
				}
				else if(request.getParameter("operation").equals("delete")) {
					int id = Integer.parseInt(request.getParameter("id"));
					boolean delete = true;
					
					PurchaseDao purchaseDao = new PurchaseDao((DataSource) getServletContext().getAttribute("DataSource"));
					
					for(PurchaseBean order : purchaseDao.doRetrieveAll("")) {
						if(id == order.getAddressId()) {
							delete = false;
							break;
						}
					}
					
					if(delete) {
						for(AccountBean currentAccount : accountDao.doRetrieveAll("")) {
							if(currentAccount.getAddresses() != null) {
								for(Map.Entry<Integer, List<String>> address : currentAccount.getAddresses().entrySet()) {
									if(currentAccount.getAccountId() != account.getAccountId() && id == address.getKey()) {
										delete = false;
										break;
									}
								}
							}
							
							if(!delete)
								break;
						}
					}
					
					if(delete)
						addressDao.doDelete(addressDao.doRetrieveByKey(id));
					else {
						account.setDeleteAddressId(id);
						accountDao.doDelete(account);
					}
					
					account.setAddresses(null);
					
					response.getWriter().write(new Gson().toJson("Nessun indirizzo salvato"));
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

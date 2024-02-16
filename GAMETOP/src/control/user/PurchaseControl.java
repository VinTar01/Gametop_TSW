package control.user;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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

import cart.Cart;
import dao.AccountDao;
import dao.AddressDao;
import dao.PurchaseDao;
import model.AccountBean;
import model.AddressBean;
import model.PurchaseBean;

/**
 * Servlet implementation class PurchaseControl
 */
@WebServlet("/User/Order")
public class PurchaseControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PurchaseControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		String payment = request.getParameter("payment");
		
		if(cart.getItems() != null && payment != null) {
			
			PurchaseDao purchaseDao = new PurchaseDao((DataSource) getServletContext().getAttribute("DataSource"));
			AddressDao addressDao = new AddressDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			AccountBean account = (AccountBean) request.getSession().getAttribute("account");
			PurchaseBean purchase = new PurchaseBean();
			AddressBean address = null;
			
			try {
				if(account.getAddresses() != null) {
					for(Map.Entry<Integer, List<String>> currentAddress : account.getAddresses().entrySet()) {
						purchase.setPurchasePhoneNumber(currentAddress.getValue().get(0));
						purchase.setPurchaseFirstName(currentAddress.getValue().get(1));
						purchase.setPurchaseLastName(currentAddress.getValue().get(2));
						
						address = addressDao.doRetrieveByKey(currentAddress.getKey());
					}
				}
				else {
					purchase.setPurchasePhoneNumber(request.getParameter("phone"));
					purchase.setPurchaseFirstName(request.getParameter("name"));
					purchase.setPurchaseLastName(request.getParameter("surname"));
					
					address = new AddressBean();
					
					address.setCountry(request.getParameter("country"));
					address.setCity(request.getParameter("city"));
					address.setZip(Integer.parseInt(request.getParameter("zip")));
					address.setStreet(request.getParameter("street"));
					
					boolean find = false;
					
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
					
					if(Boolean.parseBoolean(request.getParameter("save"))) {
						AccountDao accountDao = new AccountDao((DataSource) getServletContext().getAttribute("DataSource"));
						LinkedHashMap<Integer, List<String>> accountAddress = new LinkedHashMap<Integer, List<String>>();
						List<String> accountData = new LinkedList<String>();
						
						accountData.add(purchase.getPurchasePhoneNumber());
						accountData.add(purchase.getPurchaseFirstName());
						accountData.add(purchase.getPurchaseLastName());
						
						accountAddress.put(address.getAddressId(), accountData);
						account.setAddresses(accountAddress);
						
						accountDao.doUpdate(account);
					}
				}
				System.out.println("Indirizzo:" + address.getAddressId() + ", " + address.getCountry() + ", " + address.getCity() + ", " + address.getZip() + ", " + address.getStreet());
				
				purchase.setAddressId(address.getAddressId());
				purchase.setAccountId(account.getAccountId());
				purchase.setPaymentType(request.getParameter("payment"));
				purchase.setDate(LocalDate.now());
				purchase.setCart(new Cart(cart.getItems()));
				
				purchaseDao.doSave(purchase);
				
				cart.removeItems();
				
				request.setAttribute("address", address);
				request.setAttribute("purchase", purchase);
				
				request.getRequestDispatcher("/user/Purchase.jsp").forward(request, response);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		response.sendRedirect("/GAMETOP/Home");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

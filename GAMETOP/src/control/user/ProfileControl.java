package control.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.AddressDao;
import model.AccountBean;
import model.AddressBean;

/**
 * Servlet implementation class ProfileControl
 */
@WebServlet("/User/Profile")
public class ProfileControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProfileControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AddressDao addressDao = new AddressDao((DataSource) getServletContext().getAttribute("DataSource"));
		AccountBean account = (AccountBean) request.getSession().getAttribute("account");
		
		try {
			if(account.getAddresses() != null) {
				for(Map.Entry<Integer, List<String>> address : account.getAddresses().entrySet()) {
					AddressBean currentAddress = addressDao.doRetrieveByKey(address.getKey());
					System.out.println("ProfileControl Map.Entry address: " + address + ", " + address.getKey() + ", " + address.getValue());
					System.out.println("ProfileControl: " + currentAddress);
					request.setAttribute("address", currentAddress);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/user/Profile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

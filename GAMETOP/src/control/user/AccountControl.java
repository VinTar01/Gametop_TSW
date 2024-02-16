package control.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import dao.AccountDao;
import model.AccountBean;

/**
 * Servlet implementation class AccountControl
 */
@WebServlet("/User/AccountControl")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AccountControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AccountControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    	
    	
    	String path;
    	AccountBean account = (AccountBean) request.getSession().getAttribute("account");
    	
    	if((Boolean) request.getSession().getAttribute("isAdmin"))
    		path = "/Admin/Profile";
    	else
    		path = "/User/Profile";
    	
    	if(request.getParameter("updateEmail") != null && request.getParameter("updateEmail").equals("true")) {
    		String email = request.getParameter("email");
    	
    		if(email != null) {
    	
    			boolean save = true;		
    			AccountDao accountDao = new AccountDao((DataSource)getServletContext().getAttribute("DataSource"));
    	
    			try {
    				Collection<AccountBean> accounts = accountDao.doRetrieveAll("");
    				
    				for(AccountBean currentAccount : accounts)
    					if(currentAccount.getEmail().equals(email))
    						save = false;
    				
    				if(!save) {
    					request.getRequestDispatcher(path).forward(request, response);
    					return;
    				}
    					
    				account.setEmail(email);
    				accountDao.doUpdate(account);
    				
    			} catch (SQLException e) {
    				e.printStackTrace();		
    			}
    		}
    	}
    	
    	else if(request.getParameter("retrieve") != null) {
			if(request.getParameter("retrieve").equals("1")) {
			
				System.out.println("display profile picture");
			
				byte[] cover = account.getAccountCover();
				System.out.println("Immagine: " + cover);
				ServletOutputStream out = response.getOutputStream();
			
				if (cover != null) 
				{
					out.write(cover);
					response.setContentType("image/jpeg");
				}
				out.close();
			}
			return;
		}
		
		else if(request.getParameter("updateCover") != null && request.getParameter("updateCover").equals("true")){
			AccountDao accountDao = new AccountDao((DataSource) getServletContext().getAttribute("DataSource"));
		
			System.out.println("starting servlet for update profile picture");
			
			if(request.getParts() != null && request.getParts().size() > 0) {
				for (Part part : request.getParts()) {
					String fileName = part.getSubmittedFileName();
					if (fileName != null && !fileName.equals("")) {
						account.setAccountCover(part.getInputStream().readAllBytes());
					}
				}
				try {
					accountDao.doUpdate(account);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("profile picture updated");
			}
		}
    	response.sendRedirect(request.getContextPath() + path);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

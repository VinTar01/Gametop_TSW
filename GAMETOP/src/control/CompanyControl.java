package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.CompanyDao;

/**
 * Servlet implementation class CompanyControl
 */
@WebServlet("/Company")
public class CompanyControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CompanyControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	if(request.getParameter("retrieve") != null) {
    		CompanyDao companyDao = new CompanyDao((DataSource) getServletContext().getAttribute("DataSource"));
    		
    		try {
    			request.setAttribute("company", companyDao.doRetrieveByKey(Integer.parseInt(request.getParameter("retrieve"))));
    		}
    		catch(SQLException e) {
    			e.printStackTrace();
    		}
    		
    		request.getRequestDispatcher("/common/Company.jsp").forward(request, response);
    		return;
    	}
    	
    	response.sendRedirect(request.getHeader("referer"));
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

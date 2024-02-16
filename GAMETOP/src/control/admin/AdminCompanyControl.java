package control.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import dao.CompanyDao;
import model.CompanyBean;

/**
 * Servlet implementation class CompanyControl
 */
@WebServlet("/Admin/CompanyControl")
public class AdminCompanyControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AdminCompanyControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String operation = request.getParameter("companyOperation");
		
		if(operation != null) {
			CompanyDao companyDao = new CompanyDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			try {
				if(operation.equals("insert")) {
					CompanyBean company = new CompanyBean();
				
					company.setCompanyName(request.getParameter("name"));
					company.setCompanyDescription(request.getParameter("description"));
					
					companyDao.doSave(company);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("update")) {
					CompanyBean company = companyDao.doRetrieveByKey(Integer.parseInt(request.getParameter("companies")));
					
					for(String parameter : request.getParameterValues("companyInformation")) {
						
						if(parameter.equals("name"))
							company.setCompanyName(request.getParameter("name"));
						
						else if(parameter.equals("description"))
							company.setCompanyDescription(request.getParameter("description"));
					}
					companyDao.doUpdate(company);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("delete")) {
					CompanyBean company = new CompanyBean();
					
					company.setCompanyId(Integer.parseInt(request.getParameter("companies")));
					
					companyDao.doDelete(company);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("retrieveAll")) {
					response.setContentType("application/json");
					
					response.getWriter().write(new Gson().toJson(companyDao.doRetrieveAll("")));
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

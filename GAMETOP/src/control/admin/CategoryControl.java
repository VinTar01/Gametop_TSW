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

import dao.CategoryDao;
import model.CategoryBean;

/**
 * Servlet implementation class CategoryControl
 */
@WebServlet("/Admin/CategoryControl")
public class CategoryControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CategoryControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String operation = request.getParameter("categoryOperation");
		
		if(operation != null) {
			CategoryDao categoryDao = new CategoryDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			try {
				if(operation.equals("insert")) {
					CategoryBean category = new CategoryBean();
					category.setCategoryName(request.getParameter("name"));
					
					categoryDao.doSave(category);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("update")) {
					CategoryBean category = new CategoryBean();
					
					category.setCategoryId(Integer.parseInt(request.getParameter("categories")));
					category.setCategoryName(request.getParameter("name"));
					
					categoryDao.doUpdate(category);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("delete")) {
					CategoryBean category = new CategoryBean();
					category.setCategoryId(Integer.parseInt(request.getParameter("categories")));
					
					categoryDao.doDelete(category);
					
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
				
				else if(operation.equals("retrieveAll")) {
					response.setContentType("application/json");
						
					response.getWriter().write(new Gson().toJson(categoryDao.doRetrieveAll("")));
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

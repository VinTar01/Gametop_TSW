package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.CategoryDao;
import dao.ProductDao;
import model.CategoryBean;
import model.ProductBean;

/**
 * Servlet implementation class HomeControl
 */
@WebServlet("/Home")
public class HomeControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public HomeControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
		CategoryDao categoryDao = new CategoryDao((DataSource) getServletContext().getAttribute("DataSource"));
		
		try {
			LinkedList<ProductBean> products = new LinkedList<ProductBean>();
			
			for(ProductBean product : productDao.doRetrieveAll(""))
				if(product.isActive())
					products.add(product);
			
			if(request.getParameterValues("category") != null) {
				
				String[] categories = request.getParameterValues("category");
				String[] categoriesUpdated = categories.clone();
				LinkedList<ProductBean> productsFound = new LinkedList<ProductBean>();
					
				for(ProductBean product : products) {
					int founds = 0;
					Map<Integer, Integer> productCategories = product.getCategories(); 
						
					for(String category : categories) {
						for(Map.Entry<Integer, Integer> currentCategory : productCategories.entrySet()) {
							if(Integer.parseInt(category) == currentCategory.getKey() || Integer.parseInt(category) == currentCategory.getValue()) {
								founds ++;
								break;
							}
						}
					}
					if(founds == categories.length) {
						productsFound.add(product);
							
						for(String category : categories) {
							if(productCategories.containsKey((Integer.parseInt(category)))) {
								boolean save = true;
									
								for(String currentCategory : categoriesUpdated) {
									if(productCategories.get(Integer.parseInt(category)) == Integer.parseInt(currentCategory)) {
										save = false;
										break;
									}
								}
									
								if(save) {
									categoriesUpdated = Arrays.copyOf(categoriesUpdated, categoriesUpdated.length + 1);
									categoriesUpdated[categoriesUpdated.length - 1] = productCategories.get(Integer.parseInt(category)) + "";
									break;
								}
							}
						}
					}
						
				}
					
				if(categoriesUpdated.length > categories.length) {
					LinkedList<CategoryBean> newCategories = new LinkedList<CategoryBean>();
						
					for(int i = categories.length; i < categoriesUpdated.length; i++)
						newCategories.add(categoryDao.doRetrieveByKey(Integer.parseInt(categoriesUpdated[i])));
						
					request.setAttribute("newCategories", newCategories);
				}
				
				request.setAttribute("products", productsFound);
			}
			else {
				LinkedList<CategoryBean> newCategories = new LinkedList<CategoryBean>();
				
				for(CategoryBean category : categoryDao.doRetrieveAll(""))
					for(ProductBean product : products) {
						Map<Integer, Integer> categories = product.getCategories();
						
						if(categories.containsKey(category.getCategoryId()) || categories.containsValue(category.getCategoryId())) {
							newCategories.add(category);
							break;
						}
					}
				
				request.setAttribute("newCategories", newCategories);
				request.setAttribute("products", products);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("forward", "false");
		request.getRequestDispatcher("/common/Home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

package control.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import com.google.gson.Gson;

import cart.CartItem;
import dao.ProductDao;
import dao.PurchaseDao;
import model.ProductBean;
import model.PurchaseBean;

/**
 * Servlet implementation class ProductControl
 */
@WebServlet("/Admin/ProductControl")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AdminProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AdminProductControl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("productOperation") != null) {
			ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
		
			try {
				if(request.getParameter("productOperation").equals("insert")) {
					
					byte[] cover = null;
					ProductBean product = new ProductBean();
					LinkedList<Integer> categoriesList = new LinkedList<Integer>();
					LinkedHashMap<Integer, Integer> categories = new LinkedHashMap<Integer, Integer>();
					
					if(request.getParts() != null && request.getParts().size() > 0) {
						for (Part part : request.getParts()) {
							String fileName = part.getSubmittedFileName();
							if (fileName != null && !fileName.equals("")) 
								cover = part.getInputStream().readAllBytes();
						}
					}
					
					Enumeration<String> parameterNames = request.getParameterNames();
					
					while(parameterNames.hasMoreElements()) {
						String parameter = parameterNames.nextElement();
						
						if(parameter.contains("productCategories"))
							categoriesList.add(Integer.parseInt(request.getParameter(parameter)));
					}
					
					for(int i = 1; i < categoriesList.size(); i++)
						categories.put(categoriesList.get(i - 1), categoriesList.get(i));
				
					product.setActive(Boolean.parseBoolean(request.getParameter("active")));
					product.setProductName(request.getParameter("name"));
					product.setProductPrice(new BigDecimal(request.getParameter("price")));
					product.setProductCover(cover);
					product.setProductDescription(request.getParameter("description"));
					product.setCompanyId(Integer.parseInt(request.getParameter("productCompanies")));
					product.setCategories(categories);
				
					productDao.doSave(product);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
			
				else if(request.getParameter("productOperation").equals("update")) {
			
					ProductBean product = new ProductBean();
					product = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("products")));
				
					for(String parameter : request.getParameterValues("productInformation")) {
					
						if(parameter.equals("active")) 
							product.setActive(Boolean.parseBoolean(request.getParameter("selectProductActive")));
						
						else if(parameter.equals("name"))
								product.setProductName(request.getParameter("name"));
							
						else if(parameter.equals("price"))
							product.setProductPrice(new BigDecimal(request.getParameter("price")));
							
						else if(parameter.equals("cover")) { 
							if(request.getParts() != null && request.getParts().size() > 0) {
								for (Part part : request.getParts()) {
									String fileName = part.getSubmittedFileName();
									if (fileName != null && !fileName.equals("")) 
										product.setProductCover(part.getInputStream().readAllBytes());
								}
							}
						}
							
						else if(parameter.equals("description"))
							product.setProductDescription(request.getParameter("description"));
							
						else if(parameter.equals("company"))
							product.setCompanyId(Integer.parseInt(request.getParameter("productCompanies")));
							
						else if(parameter.equals("category")) {
							boolean delete = false;
							LinkedList<Integer> categoriesList = new LinkedList<Integer>();
							LinkedHashMap<Integer, Integer> categories = new LinkedHashMap<Integer, Integer>();
							Enumeration<String> parameterNames = request.getParameterNames();
							
							while(parameterNames.hasMoreElements()) {
								String parameterName = parameterNames.nextElement();
								
								if(parameterName.contains("productCategories"))
									categoriesList.add(Integer.parseInt(request.getParameter(parameterName)));
							}
								
							for(int i = 1; i < categoriesList.size(); i++)
								categories.put(categoriesList.get(i - 1), categoriesList.get(i));
								
							for(Map.Entry<Integer, Integer> category : product.getCategories().entrySet()) {
								delete = true;
									
								for(Map.Entry<Integer, Integer> currentCategory : categories.entrySet())
									if(category.getKey() == currentCategory.getKey() && category.getValue() == currentCategory.getValue()) {
										delete = false;
										break;
									}
									
								if(delete) {
									product.setDeleteCategory(category.getValue());
									productDao.doDelete(product);
									request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
								}
							}
								
							product.setCategories(categories);
						}
					}
				
					productDao.doUpdate(product);
					request.getRequestDispatcher("/Admin/successOperation").forward(request, response);
				}
			
				else if(request.getParameter("productOperation").equals("delete")) {
				
					PurchaseDao purchaseDao = new PurchaseDao((DataSource) getServletContext().getAttribute("DataSource"));
					boolean found = false;
					ProductBean product = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("products")));
					
					for(PurchaseBean purchase : purchaseDao.doRetrieveAll("")) {
						for(CartItem item : purchase.getCart().getItems())
							if(item.getProduct().getProductId() == product.getProductId()) {
								found = true;
								break;
							}
						if(found)
							break;
					}
					
					if(found) {
						product.setActive(false);
						productDao.doUpdate(product);
					}
					else
						productDao.doDelete(product);
				}
				
				else if(request.getParameter("productOperation").equals("retrieveAll")) {
					response.setContentType("application/json");
				
					response.getWriter().write(new Gson().toJson(productDao.doRetrieveAll("")));
				}
				
				/*else if(request.getParameter("productOperation").equals("retrieveProductCategories")) {
					response.setContentType("application/json");
					
					ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
					CategoryDao categoryDao = new CategoryDao((DataSource) getServletContext().getAttribute("DataSource"));
					
					int i = 0;
					LinkedHashMap<Integer, Integer> productCategories = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("products"))).getCategories();
					LinkedHashMap<Integer, String> productCategoriesValues = new LinkedHashMap<Integer, String>();
					
					orderProductCategories(productCategories);
					
					for(Map.Entry<Integer, Integer> productCategory : productCategories.entrySet()) {
						CategoryBean category = categoryDao.doRetrieveByKey(productCategory.getKey());
						productCategoriesValues.put(category.getCategoryId(), category.getCategoryName());
						
						if(i == productCategories.size() - 1) {
							CategoryBean categoryTail = categoryDao.doRetrieveByKey(productCategory.getValue());
							productCategoriesValues.put(categoryTail.getCategoryId(), categoryTail.getCategoryName());
						}
						
						i++;
					}
					
					response.getWriter().write(new Gson().toJson(productCategoriesValues));
				}*/
					
					/*else if(operation.equals("addProductCategory")) {
						
						ProductBean product = productDao.doRetrieveByKey(id);
						LinkedHashMap<Integer, Integer> productCategories = new LinkedHashMap<Integer, Integer>();
						
						productCategories.put(getTailCategory(product.getCategories()), categoriesList.getFirst());
						
						for(int i = 1; i < categoriesList.size(); i++)
							productCategories.put(categoriesList.get(i - 1), categoriesList.get(i));
						
						product.setCategories(productCategories);
						productDao.doUpdate(product);
					}
					
					else if(operation.equals("removeProductCategory")) {
						
						ProductBean product = new ProductBean();
						
						product.setProductId(id);
						product.setDeleteCategory(categoriesList.getFirst());
						productDao.doDelete(product);
					}*/
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/*private int getTailCategory(LinkedHashMap<Integer, Integer> categories) {
		
		int tail = 0;
		
		for(Map.Entry<Integer, Integer> category : categories.entrySet()) {
			tail = 1;
			
			for(Map.Entry<Integer, Integer> currentCategory : categories.entrySet())
				if(category.getValue() == currentCategory.getKey())
					tail = 0;
			
			if(tail == 1) {
				return category.getValue();
			}
		}
		
		return tail;
	}
	
	private void orderProductCategories(LinkedHashMap<Integer, Integer> categories) {
		
		int root = 0;
		
		for(Map.Entry<Integer, Integer> category : categories.entrySet()) {
			root = 1;
			
			for(Map.Entry<Integer, Integer> currentCategory : categories.entrySet())
				if(category.getKey() == currentCategory.getValue())
					root = 0;
			
			if(root == 1) {
				root = category.getKey();
				break;
			}
		}
		
		orderCategories(categories, root);
	}
	
	private void orderCategories(LinkedHashMap<Integer, Integer> categories, int root) {
		
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		
		map.put(root, categories.get(root));
		
		for(int i = 1; i < categories.size();) {
			for(Map.Entry<Integer, Integer> category : categories.entrySet()) {
				if(map.containsValue(category.getKey()) && !map.containsKey(category.getKey())) {
					map.put(category.getKey(), category.getValue());
					i++;
				}
			}
		}
		
		categories.clear();
		categories.putAll(map);
	}*/
}

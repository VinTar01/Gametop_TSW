package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.ProductDao;
import dao.CategoryDao;
import dao.CompanyDao;
import model.ProductBean;
import model.CategoryBean;
import model.CompanyBean;

/**
 * Servlet implementation class ProductControl
 */
@WebServlet("/Product")
public class ProductControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductControl() {
        super();
    }

    // Metodo per gestire richieste GET
    /*Nel metodo doGet, gestisce due tipi di richieste:
	Se il parametro "retrieve" è presente nella richiesta,
	 recupera i dettagli del prodotto e le sue categorie dal database e li passa alla JSP per la visualizzazione.
	Se il parametro "cover" è presente nella richiesta,
	 recupera l'immagine di copertina del prodotto dal database e la restituisce come risposta HTTP.*/
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		if(request.getParameter("retrieve") != null) {
			// Inizializza i DAO necessari
			ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
			CategoryDao categoryDao = new CategoryDao((DataSource) getServletContext().getAttribute("DataSource"));
			CompanyDao companyDao = new CompanyDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			try {
				// Recupera il prodotto dal database
				ProductBean product = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("retrieve")));
				
				// Verifica se il prodotto è attivo
				if(product.isActive()) {
					// Recupera l'azienda associata al prodotto
					CompanyBean company = companyDao.doRetrieveByKey(product.getCompanyId());
				
					// Recupera le categorie associate al prodotto
					LinkedList<CategoryBean> categories = new LinkedList<CategoryBean>();
					Map<Integer, Integer> categoriesMap = product.getCategories();
				
					boolean key = true;
				
					// Itera attraverso le categorie e aggiungile alla lista
					for(Map.Entry<Integer, Integer> category : categoriesMap.entrySet()) {
						if(key) {
							categories.add(categoryDao.doRetrieveByKey(category.getKey()));
							key = false;
						}
					
						categories.add(categoryDao.doRetrieveByKey(category.getValue()));
					}
				
					// Imposta gli attributi della richiesta per passare i dati alla JSP
					request.setAttribute("product", product);
					request.setAttribute("categories", categories);
					request.setAttribute("company", company);
					
					// Inoltra la richiesta alla JSP per visualizzare i dettagli del prodotto
					request.getRequestDispatcher("/common/Product.jsp").forward(request, response);
					return;
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		// Se viene richiesta l'immagine di copertina di un prodotto
		else if(request.getParameter("cover") != null) {
			System.out.println("display product picture");
			
			// Inizializza il DAO del prodotto
			ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
			
			try {
				// Recupera il prodotto dal database
				ProductBean product = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("cover")));
			
				byte[] cover = product.getProductCover();
				ServletOutputStream out = response.getOutputStream();
			
				if (cover != null) 
				{
					out.write(cover);
					response.setContentType("image/jpeg");
				}
				out.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		// Se non viene specificato alcun parametro, reindirizza alla homepage
		response.sendRedirect("/GAMETOP/Home");
				
	}
	
    // Metodo per gestire richieste POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

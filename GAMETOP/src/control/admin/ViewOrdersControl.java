package control.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.PurchaseDao;
import model.PurchaseBean;

@WebServlet("/Admin/ViewOrdersControl")
public class ViewOrdersControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i parametri della data di inizio e di fine dalla richiesta
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        // Parsifica le date
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        // Crea un'istanza di PurchaseDao
        PurchaseDao purchaseDao = new PurchaseDao((DataSource) getServletContext().getAttribute("DataSource"));

        try {
            // Ottieni gli ordini nel range di date specificato
            Collection<PurchaseBean> orders = purchaseDao.getOrdersByDateRange(startDate, endDate);

            // Imposta gli ordini come attributo della richiesta per la visualizzazione nella pagina JSP
            request.setAttribute("orders", orders);

            // Inoltra la richiesta alla pagina JSP per la visualizzazione degli ordini
            request.getRequestDispatcher("/Admin/ViewOrders").forward(request, response);
        } catch (SQLException e) {
            // Gestione dell'eccezione SQLException, ad esempio log o reindirizzamento a una pagina di errore
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

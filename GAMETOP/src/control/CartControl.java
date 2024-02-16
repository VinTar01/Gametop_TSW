// Import necessari per la servlet
package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.Gson;

import dao.ProductDao;
import model.ProductBean;
import cart.Cart;
import cart.CartItem;

/**
 * Servlet implementation class CartControl
 * Questa servlet gestisce le operazioni relative al carrello dell'utente.
 * 
 * In particolare, questo controllo si occupa di:

Aggiungere o rimuovere prodotti dal carrello.
Aggiornare la quantità di un prodotto nel carrello.
Calcolare il totale del carrello.
Gestire eventuali eccezioni durante l'interazione con il database.
L'utilizzo di JSON (JavaScript Object Notation) è dovuto alla necessità di comunicare in modo efficiente e
strutturato tra il lato client (browser) e il lato server (servlet).
JSON permette di rappresentare dati complessi in un formato leggibile e trasmissibile tramite HTTP.
 Nella pagina JSP, viene utilizzato JSON per inviare e ricevere dati relativi al carrello
  (ad esempio, l'aggiunta di un prodotto, la modifica della quantità, il calcolo del totale) senza dover ricaricare completamente la pagina. 
  Questo permette un'esperienza utente più fluida e interattiva.
 */
@WebServlet("/CartControl")
public class CartControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * Costruttore della servlet
     */
    public CartControl() {
        super();
    }

    /**
     * Metodo per gestire le richieste GET
     * @param request Oggetto HttpServletRequest che rappresenta la richiesta HTTP
     * @param response Oggetto HttpServletResponse che rappresenta la risposta HTTP
     * @throws ServletException Eccezione generata in caso di errore nella servlet
     * @throws IOException Eccezione generata in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Verifica se il parametro "operation" è presente nella richiesta
        if(request.getParameter("operation") != null) {
            
            try {
                // Imposta il tipo di contenuto della risposta come JSON
                response.setContentType("application/json");
                // Crea un'istanza di ProductDao utilizzando il DataSource fornito dal contesto servlet
                ProductDao productDao = new ProductDao((DataSource) getServletContext().getAttribute("DataSource"));
                // Ottiene il prodotto corrispondente all'ID fornito dalla richiesta
                ProductBean product = productDao.doRetrieveByKey(Integer.parseInt(request.getParameter("id")));
                // Crea una lista per memorizzare i risultati da restituire come JSON
                LinkedList<Object> list = new LinkedList<Object>();
                // Ottiene la sessione corrente o ne crea una nuova se non esiste
                HttpSession session = request.getSession();
                // Ottiene il carrello dalla sessione
                Cart cart = (Cart) session.getAttribute("cart");

                // Se l'operazione è "add"
                if(request.getParameter("operation").equals("add")) {
                
                    int quantity = 1;
                    
                    // Controlla se è stata specificata una quantità diversa da quella predefinita
                    if(request.getParameter("quantity") != null)
                        quantity = Integer.parseInt(request.getParameter("quantity"));
                    
                    synchronized(session) {
                        // Se il carrello non esiste, ne crea uno nuovo e lo memorizza nella sessione
                        if(cart == null) {
                            cart = new Cart(new LinkedList<CartItem>());
                            session.setAttribute("cart", cart);
                        }
                        
                        // Se la lista degli elementi del carrello è nulla, inizializzala
                        if(cart.getItems() == null)
                            cart.setItems(new LinkedList<CartItem>());
                        
                        // Aggiunge il prodotto al carrello con la quantità specificata
                        boolean value = cart.addProduct(product, quantity);
                        
                        // Aggiunge l'ID del prodotto alla lista da restituire come JSON
                        list.add(product.getProductId());
                        
                        // Itera attraverso gli elementi del carrello
                        for(CartItem item : cart.getItems()) {
                            // Se l'elemento corrente corrisponde al prodotto aggiunto
                            if(item.getProduct().getProductId() == product.getProductId()) {
                                // Aggiunge il totale dell'elemento alla lista da restituire come JSON
                                list.add(item.total().toString());
                                break;
                            }
                        }
                        
                        // Aggiunge il totale complessivo del carrello alla lista da restituire come JSON
                        list.add(cart.getTotal().toString());
                        
                        // Se il prodotto non è stato aggiunto per intero al carrello, aggiunge un messaggio
                        // di avviso alla lista da restituire come JSON
                        if(!value)
                            list.add("La quantità massima per questo prodotto nel tuo carrello è stata raggiunta");
                    }
                }
                // Se l'operazione è "remove"
                else if(request.getParameter("operation").equals("remove")) {
                
                    synchronized(session) {
                        // Se ci sono più elementi nel carrello
                        if(cart.getItems().size() > 1) {
                            // Rimuove il prodotto specificato dal carrello
                            cart.removeProduct(product);
                            // Aggiunge l'ID del prodotto e il nuovo totale del carrello alla lista da restituire come JSON
                            list.add(product.getProductId());
                            list.add(cart.getTotal().toString());
                        }
                        // Se c'è un solo elemento nel carrello, rimuove tutti gli elementi
                        else {
                            cart.removeItems();
                            // Aggiunge "home" alla lista da restituire come JSON per indicare il reindirizzamento alla homepage
                            list.add("home");
                        }
                    }
                }
        
                // Restituisce la lista convertita in formato JSON come risposta
                response.getWriter().write(new Gson().toJson(list));
                return;
            }
            // Gestisce eventuali eccezioni SQL
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Reindirizza alla pagina precedente
        response.sendRedirect(request.getHeader("referer"));
    }

    /**
     * Metodo per gestire le richieste POST, reindirizza alla doGet.
     * @param request Oggetto HttpServletRequest che rappresenta la richiesta HTTP
     * @param response Oggetto HttpServletResponse che rappresenta la risposta HTTP
     * @throws ServletException Eccezione generata in caso di errore nella servlet
     * @throws IOException Eccezione generata in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Richiama il metodo doGet per gestire la richiesta
        doGet(request, response);
    }

}
 
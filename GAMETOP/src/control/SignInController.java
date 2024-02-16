package control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import dao.AccountDao;
import model.AccountBean;

/**
 * Servlet implementation class Signin
 * Gestisce la logina della registrazione di un nuovo account
 */
@WebServlet(name = "SigninAccount", urlPatterns = { "/SigninAccount" })
public class SignInController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public SignInController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupero dei parametri dalla richiesta
    	/*Recupero dei parametri dalla richiesta: 
    	I parametri come nome, cognome, email, password e data di nascita vengono recuperati dalla richiesta HTTP*/
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String birthdate = request.getParameter("date");
        
        // Hashing della password
        /* La password fornita viene convertita in un hash 
         * utilizzando l'algoritmo SHA-512 per garantire la sicurezza delle credenziali dell'utente
         */
        String hashPassword = toHash(password);
        
        boolean save = true;
        boolean emailAlreadyExists = false; // AGGIUNTA VAR BOOLEANA PER VERIFICARE SE EMAIL é NEL DB

        
        // Creazione dell'oggetto AccountBean con i parametri ottenuti
        AccountBean account = new AccountBean();
        account.setAccountFirstName(nome);
        account.setAccountLastName(cognome);
        account.setEmail(email);
        account.setPassword(hashPassword);
        account.setBirthDate(LocalDate.parse(birthdate));
        
        // Recupero del DataSource dal contesto della servlet
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        AccountDao accountDao = new AccountDao(ds);
        try {
            // Recupero di tutti gli account dal database
            Collection<AccountBean> accounts = accountDao.doRetrieveAll("account_id");
            
            // Controllo se esiste già un account con l'email fornita
            for(AccountBean currentAccount : accounts)
                if(email.equals(currentAccount.getEmail()))
                    save = false; //esiste gia account con email fornita
                    emailAlreadyExists = true; // Imposta emailAlreadyExists a true se l'email esiste già
            
            if(save) {
                // Salvataggio dell'account nel database se non esiste già
                accountDao.doSave(account);
                System.out.println("Account Creato");
                
                // Recupero dell'account appena salvato dal database
                accounts = accountDao.doRetrieveAll("account_id");
                
                // Impostazione dell'ID dell'account appena salvato nell'oggetto AccountBean
                for(AccountBean currentAccount : accounts)
                    if(email.equals(currentAccount.getEmail()))
                        account.setAccountId(currentAccount.getAccountId());
                
                // Creazione della sessione e reindirizzamento dell'utente in base al ruolo
                /*Viene creata una sessione e l'account viene aggiunto come attributo della sessione.
                Se l'account è stato identificato come amministratore (utilizzando un'email specifica definita nel file di configurazione),
                 viene impostato un attributo isAdmin della sessione su true, altrimenti su false.*/
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                
                // Verifica se l'email dell'account corrisponde all'email di un amministratore
                if(email.equals(getServletContext().getInitParameter("email"))) {
                    session.setAttribute("isAdmin", Boolean.TRUE);
                    response.sendRedirect(request.getContextPath() + "/Admin/Profile");
                }
                else {
                    session.setAttribute("isAdmin", Boolean.FALSE);
                    response.sendRedirect(request.getContextPath() + "/User/Profile");
                }
            }
            else {
                // Se l'account esiste già, reindirizza l'utente alla pagina di registrazione
                System.out.println("Account già presente con questa email");
                request.setAttribute("emailAlreadyExists", true); // Imposta l'attributo emailAlreadyExists a true
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Signin");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            // Gestione delle eccezioni SQL
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Richiama doGet() se la richiesta è di tipo POST
        doGet(request, response);
    }

    // Metodo per ottenere l'hash della password utilizzando l'algoritmo SHA-512
    private String toHash(String password) {
        String hashString = null;
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashString = "";
            for (int i = 0; i < hash.length; i++) {
                hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1,3);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            // Gestione delle eccezioni NoSuchAlgorithmException
            System.out.println(e);
        }
        return hashString;
    }
    
}
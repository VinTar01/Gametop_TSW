package control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.AccountDao;
import model.AccountBean;

/**
 * Servlet implementation class Login
 */
@WebServlet(name = "LogInController", urlPatterns = { "/LoginAccount" })
public class LogInController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogInController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/Login");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        boolean found = false;

        RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("/Login");

        System.out.println(toHash("sa"));

        System.out.println(password);

        String hashPassword = toHash(password);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        AccountDao accountDao = new AccountDao(ds);
        try {
            Collection<AccountBean> accounts = accountDao.doRetrieveAll("account_id");
            System.out.println(accounts);
            for (AccountBean account : accounts) {

                if (email.equals(account.getEmail()) && hashPassword.equals(account.getPassword())) {
                    found = true;

                    if (account.getEmail().equals(getServletContext().getInitParameter("email"))) {
                        String emailContext = (String) getServletContext().getInitParameter("email");
                        System.out.println(emailContext);
                        request.getSession().setAttribute("isAdmin", Boolean.TRUE); // inserisco il token nella
                                                                                        // sessione
                        request.getSession().setAttribute("account", account);
                        System.out.println("Accesso Admin");
                        System.out.println("\n" + account.getAccountFirstName() + ", " + account.getAccountLastName());
                        response.sendRedirect(request.getContextPath() + "/Admin/Profile");
                    } else {
                        request.getSession().setAttribute("isAdmin", Boolean.FALSE);
                        request.getSession().setAttribute("account", account);
                        System.out.println("Accesso user");
                        System.out.println("\n" + account.getAccountFirstName() + ", " + account.getAccountLastName());
                        response.sendRedirect(request.getContextPath() + "/User/Profile");
                    }

                }
            }
            if (!found) {
                System.out.println("Username e password errati");
                request.setAttribute("errorMessage", "Email o password non validi");
                dispatcherToLoginPage.forward(request, response);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String toHash(String password) {
        String hashString = null;
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashString = "";
            for (int i = 0; i < hash.length; i++) {
                hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return hashString;
    }

}

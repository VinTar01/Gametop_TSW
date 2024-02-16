package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Questa servlet è configurata come un filtro URL che intercetta tutte le richieste (urlPatterns = "/*").
@WebFilter(filterName = "/AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = -8842520711893136034L;

	public AccessControlFilter() {
    }

	public void destroy() {
	}

	//Il metodo doFilter viene eseguito ogni volta che viene fatta una richiesta HTTP.
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		//Nel metodo doFilter, viene controllato se l'utente è un amministratore (isAdmin).
		Boolean isAdmin = (Boolean) httpServletRequest.getSession().getAttribute("isAdmin");
		//Il percorso della richiesta (path) viene ottenuto utilizzando HttpServletRequest.getServletPath()
		String path = httpServletRequest.getServletPath();
		System.out.println(path);
		/*Se l'utente tenta di accedere a una risorsa nella directory "/User/" o "/Admin/" 
		 * e non è autenticato come amministratore (isAdmin == null),
		 * viene reindirizzato alla pagina di login (httpServletResponse.sendRedirect(...)).
		 */
		if (path.contains("/User/") && isAdmin==null) {//se un utente prova ad accedere ad una risorsa accessibile con login
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/Login");
			return;
		} else if (path.contains("/Admin/") && isAdmin==null) {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/Login");
			return;
			
		/*Se un utente tenta di accedere a una risorsa nella directory "/Admin/" e non è un amministratore (!isAdmin), 
		 * viene restituito un errore 403 (accesso negato) utilizzando HttpServletResponse.sendError(403, "Accesso negato").
		 */
		} else if (path.contains("/Admin/") && !isAdmin) {
			httpServletResponse.sendError(403, "Accesso negato");
			return;
		}
		
		/*Se l'utente è autenticato come amministratore o l'accesso è consentito per la risorsa richiesta, 
		 * la richiesta viene inoltrata al filtro successivo nella catena mediante chain.doFilter(request, response)
		 */
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}

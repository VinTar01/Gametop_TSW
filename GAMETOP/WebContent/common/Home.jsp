<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="java.util.LinkedList, model.AccountBean, model.ProductBean, model.CategoryBean"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1, width=device-width" charset="UTF-8">
<title>GameTop</title>
<%@ include file="Header.jsp" %>
<script src="https://code.jquery.com/jquery-3.7.0.js"></script>
<link rel="stylesheet" href="/GAMETOP/css/styleHome.css">


<script src="common/scripts/Home.js"></script>
</head>
<body>

<%!@SuppressWarnings("unchecked")%>

<%-- Verifica se è presente un account nella sessione --%>
<% if (request.getAttribute("forward") == null) {
    // Se non c'è un redirect, torna alla pagina Home
    response.sendRedirect(request.getContextPath() + "/Home");
    return;
}
%>

<br><br><br><br><br><br><br><br>

<!--La sezione "categories" mostra un elenco di categorie disponibili. -->
<div id="categories">

<%LinkedList<CategoryBean> newCategories = (LinkedList<CategoryBean>) request.getAttribute("newCategories");

System.out.println(newCategories);

/*Viene inizializzata una stringa url con il percorso relativo alla pagina Home ("/GAMETOP/Home?"). 
Questo URL servirà per generare i link per ogni categoria*/
String url = "/GAMETOP/Home?";
/*Viene recuperato l'array di stringhe categories che contiene i valori dei parametri "category" inviati nella richiesta HTTP.
Questi parametri possono essere utilizzati per mantenere la selezione delle categorie durante la navigazione.*/
String[] categories = request.getParameterValues("category");

/*Se sono presenti nuove categorie disponibili (newCategories != null), 
viene generato un elenco di link per ogni categoria.
Per ciascuna categoria, viene aggiunto un elemento <li> alla lista <ul>
Ogni link viene costruito concatenando l'URL di base (url) con il parametro "category" relativo all'ID della categoria.
Se ci sono più categorie selezionate, i parametri aggiuntivi vengono concatenati con "&".
Questo garantisce che i link mantengano la selezione delle categorie durante la navigazione*/
if(newCategories != null){
    if(categories != null){
        for(int i = 0; i < categories.length; i++){
            if(i > 0)
                url += "&";
            url += "category=" + categories[i];
        }
    }%>

    <ul>

    <!-- Alla fine, l'elenco di link viene reso nella pagina HTML,
    consentendo all'utente di fare clic su di essi per navigare nelle diverse categorie. -->
    <%for(int i = 0; i < newCategories.size(); i++){
        if(i > 0 || (i == 0 && categories != null)){ %>
            <li><a href='<%=url + "&category=" + newCategories.get(i).getCategoryId()%>'><%=newCategories.get(i).getCategoryName()%></a></li>
        <% } 
        else{ %>
            <li><a href='<%=url + "category=" + newCategories.get(i).getCategoryId()%>'><%=newCategories.get(i).getCategoryName()%></a></li>
        <% } 
    }%>

    </ul>
<% } %>

</div><br><br>


<!-- Visualizzazione Prodotti: La sezione "products" mostra una tabella di prodotti disponibili. 
Per ciascun prodotto, viene visualizzata un'immagine e un link per visualizzare i dettagli del prodotto. 
La tabella è organizzata in righe e colonne, con un massimo di quattro prodotti per riga. -->
<div id="products"> 


<table>
    <tr>

    <%LinkedList<ProductBean> products = (LinkedList<ProductBean>) request.getAttribute("products");

      for(int i = 0; i < products.size(); i++){ %>
        <td><img src=<%="/GAMETOP/Product?cover=" + products.get(i).getProductId()%> onerror="this.src='./imgs/image-not-found.png'" style="width:100px;height:150px"><br>
        <a href=<%="/GAMETOP/Product?retrieve=" + products.get(i).getProductId()%>><%=products.get(i).getProductName()%></a></td>

        <%if((i + 1) % 4 == 0){%>
            </tr><tr>
        <% } %>
    <% } %>

    </tr>
</table>

</div><br><br>

<%@ include file="Footer.jsp" %>

</body>
</html>

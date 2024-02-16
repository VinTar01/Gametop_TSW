<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="cart.Cart, cart.CartItem"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cart</title>
<%@ include file="Header.jsp" %>
<link rel="stylesheet" href="/GAMETOP/css/styleCart.css">
<script src="https://code.jquery.com/jquery-3.7.0.js"></script>
<script src="common/scripts/Cart.js"></script>
</head>
<body>

<a id="home" href="/GAMETOP/Home"><span>Home</span></a>

<% 
    // Recupera il carrello dalla sessione
    Cart cart = (Cart)session.getAttribute("cart");

    // Se il carrello o la lista degli elementi nel carrello sono nulli, reindirizza alla pagina precedente
    if(cart == null || cart.getItems() == null){
        response.sendRedirect(request.getHeader("referer"));
        return;
    }
%>
   
   <h2>Carrello</h2>
   
<% 
    // Itera attraverso gli elementi del carrello
    for(CartItem item : cart.getItems()){
%>
    <div id='<%= "div" + item.getProduct().getProductId() %>'>
    
        <span style="display:none"><%= item.getProduct().getProductId() %></span>
        <%= item.getProduct().getProductName() %>
        <!-- Bottone per rimuovere un prodotto dal carrello -->
        <button id='<%= "remove" + item.getProduct().getProductId() %>' onclick="addProduct($(this))" <% if(item.getQuantity() < 2){ %> disabled <% } %>>-</button>  
        <!-- Campo di input per la quantità del prodotto nel carrello -->
        <input type="text" id='<%= "quantity" + item.getProduct().getProductId() %>' name="quantity" value="<%= item.getQuantity() %>" disabled>
        <!-- Bottone per aggiungere un prodotto al carrello -->
        <button id='<%= "add" + item.getProduct().getProductId() %>' onclick="addProduct($(this))" <% if(item.getQuantity() > 2){ %> disabled <% } %>>+</button>  
        <!-- Totale per l'elemento del carrello -->
        <span id='<%= "span" + item.getProduct().getProductId() %>' ><%= item.total() %></span>&euro;
        <!-- Link per rimuovere un prodotto dal carrello -->
        <a href="" onclick="removeProduct($(this)); return false;">Rimuovi</a>
    
        <hr><br>
    
    </div>
    
    <% 
        // Stampa il totale dell'elemento del carrello
        System.out.println(item.total());
    } 
%>
   
   <!-- Subtotale del carrello -->
   Subtotale <span id="subtotal"><%= cart.getTotal() %></span>&euro;<br>
   <!-- Spese di spedizione -->
   Spese di sped. 0.00&euro;<br><br>
   <% 
       // Stampa il totale del carrello
       System.out.println(cart.getTotal());
   %>
   <!-- Totale del carrello -->
   Totale <span id="total"><%= cart.getTotal() %></span>&euro;<br><br>
   <!-- Link per procedere all'acquisto -->
   <a href="/GAMETOP/User/Checkout/Address">Procedi all'acquisto</a>
        
   <%@ include file="Footer.jsp" %>     
</body>
</html>

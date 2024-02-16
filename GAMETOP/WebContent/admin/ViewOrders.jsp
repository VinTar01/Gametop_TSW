<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection, model.PurchaseBean, cart.CartItem" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visualizza Ordini</title>
    <%@ include file="/common/Header.jsp" %>
    <link rel="stylesheet" href="/GAMETOP/css/styleViewOrders.css">
</head>
<body>
    <h1>Visualizza Ordini</h1>
    
    <!-- Form per inserire il range di date -->
    <form action="/GAMETOP/Admin/ViewOrdersControl" method="get">
        <label>Data di inizio:</label>
        <input type="date" name="startDate">
        
        <label>Data di fine:</label>
        <input type="date" name="endDate">
        
        <button type="submit">Cerca</button>
    </form>

    <!-- Tabella degli ordini e relativi prodotti -->
    <table border="1">
        <tr>
            <th>ID Ordine</th>
            <th>Data Ordine</th>
            <th>Nome Utente</th>
            <th>Prodotto</th>
            <th>Quantità</th>
            <th>Prezzo</th>
        </tr>
        <!-- Utilizzo del tag scriptlet per iterare sugli ordini -->
        <% if (request.getAttribute("orders") != null) {
            Collection<PurchaseBean> orders = (Collection<PurchaseBean>) request.getAttribute("orders");
            for (PurchaseBean order : orders) {
                for (CartItem item : order.getCart().getItems()) {
        %>
        <tr>
            <td><%= order.getPurchaseId() %></td>
            <td><%= order.getDate() %></td>
            <td><%= order.getPurchaseFirstName() %> <%= order.getPurchaseLastName() %></td>
            <td><%= item.getProduct().getProductName() %></td>
            <td><%= item.getQuantity() %></td>
            <td><%= item.getProduct().getProductPrice() %></td>
        </tr>
        <%      }
            }
        } %>
    </table>
    
    <%@ include file="/common/Footer.jsp" %>
</body>
</html>

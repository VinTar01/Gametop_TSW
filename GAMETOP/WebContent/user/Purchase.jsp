<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="model.PurchaseBean, cart.Cart, cart.CartItem"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Purchase</title>
    <link rel="stylesheet" href="/GAMETOP/styles/stylePurchase.css">
</head>
<body>

    <h1>Grazie ${account.accountFirstName} ${account.accountLastName}!</h1>

    <p class="order-number">Il tuo numero d'ordine &egrave; <b>#${purchase.purchaseId}</b></p>

    <p class="info-message">Verr&agrave; inviata un'e-mail contenente le informazioni sul tuo acquisto.<br>
        Se hai domande sul tuo acquisto, invia un'e-mail a <b>gametop@gmail.com</b> o chiamaci allo <b>0895327164</b>.</p>

    <table class="order-summary">
        <thead>
            <tr>
                <th>Riepilogo ordine</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>
                    <b>Spedizione</b><br><br>
                    ${purchase.purchaseFirstName} ${purchase.purchaseLastName}<br>
                    ${purchase.purchasePhoneNumber}<br>
                    ${address.country}<br>
                    ${address.city} ${address.zip}<br>
                    ${address.street}
                </td>
            </tr>
            <tr>
                <td>
                    <b>Pagamento</b><br><br>
                    ${purchase.paymentType}
                </td>
            </tr>
            <tr>
                <td>
                    <%PurchaseBean purchase = (PurchaseBean) request.getAttribute("purchase");
                    Cart cart = purchase.getCart();
                    
                    for(CartItem item : cart.getItems()) {%>
                        <%= item.getProduct().getProductName() %>&emsp;<%= "Qty: " + item.getQuantity() %>&emsp;<%=item.total() + "&euro;\n\n" %>
                        <hr><br>
                    <% } %>
                    <%= "Subtotale " + cart.getTotal() %>&euro;<br>
                    <%= "Spese di sped. 0.00" %>&euro;<br>
                    <b><%= "Totale " + cart.getTotal()%>&euro;</b>
                </td>
            </tr>
        </tbody>
    </table>

    <br><br>
    <a href="/GAMETOP/Home">Home</a>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Payment</title>
    <!-- Inclusione delle librerie JavaScript -->
    <script src="https://code.jquery.com/jquery-3.7.0.js"></script>
    <link rel="stylesheet" href="/GAMETOP/css/stylePayment.css">
    <script src="../../user/scripts/Payment.js"></script>
</head>
<body>

    <h2>Pagamento</h2>
    
    <!-- Form per il pagamento -->
    <form action="/GAMETOP/User/Order" method="post">
        <!-- Input nascosti per i dettagli dell'indirizzo -->
        <input type="hidden" id="name" name="name" value="<%=request.getParameter("name")%>">
        <input type="hidden" id="surname" name="surname" value="<%=request.getParameter("surname")%>">
        <input type="hidden" id="phone" name="phone" value="<%=request.getParameter("phone")%>">
        <input type="hidden" id="country" name="country" value="<%=request.getParameter("country")%>">
        <input type="hidden" id="city" name="city" value="<%=request.getParameter("city")%>">
        <input type="hidden" id="zip" name="zip" value="<%=request.getParameter("zip")%>">
        <input type="hidden" id="street" name="street" value="<%=request.getParameter("street")%>">
        <input type="hidden" id="save" name="save" value="<%=request.getParameter("save")%>">
        
        <!-- Selezione del metodo di pagamento -->
        <input type="radio" id="paypal" name="payment" value="PayPal"> PayPal<br>
        <input type="radio" id="card" name="payment" value="Carta"> Carta di Credito<br><br>

        <!-- Div per il metodo di pagamento -->
        <div id="paymentDiv">
        
        </div>
        
    </form>
    
    

</body>
</html>

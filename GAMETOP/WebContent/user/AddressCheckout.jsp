<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="model.AccountBean"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Address</title>
    <link rel="stylesheet" href="/GAMETOP/css/styleFormNewAddress.css">
    <script src="common/scripts/validateAddress.js"></script>
</head>
<body>

<% 
    // Recupera l'account dall'attributo della sessione
    AccountBean account = (AccountBean) request.getSession().getAttribute("account");

    // Verifica se l'account ha indirizzi salvati
    if (account.getAddresses() == null) { %>

        <!-- Se non ci sono indirizzi salvati, mostra il modulo di inserimento degli indirizzi di spedizione -->
        <h2>Spedizione</h2>

        <!-- Form per l'inserimento degli indirizzi -->
        <form id = "AddressForm" action="/GAMETOP/User/Checkout/Payment" method="post">

            <label>Nome</label> <input type="text" id="name" name="name" required
            onchange="validateFormElem(this, document.getElementById('errorName'), emptyFieldErrorMessage"><br>
            <span id="errorName"></span>
            
            <label>Cognome</label> <input type="text" id="surname" name="surname" required
            onchange="validateFormElem(this, document.getElementById('errorName'), emptyFieldErrorMessage"><br>
            <span id="errorSurname"></span>
            
            <label>Numero di telefono</label> <input type="text" id="phone" name="phone" required pattern="[0-9]{10}"
            onchange="validateFormElem(this, document.getElementById('errorName'), phoneErrorMessage)"><br>
            <span id="errorPhone"></span>
            
            <label>Nazione</label> <input type="text" id="country" name="country" required
            onchange="validateFormElem(this, document.getElementById('errorName'), emptyFieldErrorMessage"><br>
            <span id="errorNazione"></span>
            
            <label>Citt&agrave;</label> <input type="text" id="city" name="city" required
            onchange="validateFormElem(this, document.getElementById('errorName'), emptyFieldErrorMessage"><br>
            <span id="errorCittà"></span>
            
            <label>Codice postale</label> <input type="text" id="zip" name="zip" required pattern="[0-9]{5}"
            onchange="validateFormElem(this, document.getElementById('errorName'), capErrorMessage)"><br>
            <span id="errorCap"></span>
            
            <label>Piazza/Viale/Via</label> <input type="text" id="street" name="street" required
            onchange="validateFormElem(this, document.getElementById('errorName'), emptyFieldErrorMessage"><br><br>
            <span id="errorVia"></span>

            <!-- Opzione per salvare l'indirizzo -->
            <label>Salva indirizzo</label> <input type="checkbox" id="save" name="save" value="true"><br>
            
            <!-- Pulsante per procedere al checkout -->
            <button id="checkout" onclick="return validate()">Cassa</button>

        </form>

<% } else {
    // Se l'account ha già degli indirizzi salvati, reindirizza alla pagina di pagamento
    response.sendRedirect("/GAMETOP/User/Checkout/Payment");
}%>

</body>
</html>

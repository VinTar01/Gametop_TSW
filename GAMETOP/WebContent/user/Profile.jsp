<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="java.util.List, model.AccountBean, model.AddressBean"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Profile</title>
    
    <script src="https://code.jquery.com/jquery-3.7.0.js"></script>
    <script src="../user/scripts/AccountCover.js"></script>
    <script src="../user/scripts/Address.js"></script>
    <link rel="stylesheet" type="text/css" href="/GAMETOP/styles/styleProfileUser.css">
</head>
<body>

<div class="container">

<h1>Profile</h1>

<img class="profile-image" src="/GAMETOP/User/AccountControl?retrieve=1" onerror="this.src='../imgs/nophoto.png'" alt="Profile Image">

<form action="/GAMETOP/User/AccountControl?updateCover=true" method="post" enctype="multipart/form-data" id="upload">
    <input type="file" id="accountCover" name="accountCover" style="display:none;"/>
    <input type="button" value="CAMBIA IMMAGINE" onclick="changeAccountCover()" />
    <input type="submit" id="submitFile" style="display:none;">
</form>

<form action="/GAMETOP/User/AccountControl?updateEmail=true" method="post" class="profile-form">
    <p>Account ID: #${account.accountId}</p>
    <p>Nome: ${account.accountFirstName}</p>
    <p>Cognome: ${account.accountLastName}</p>
    <p>Email: <input type="text" name="email" value="${account.email}"> <button type="submit">Cambia</button></p>
    <p>Data di nascita: ${account.birthDate}</p>
</form>

<div id="addressDiv" class="address">
<% AccountBean account = (AccountBean) request.getSession().getAttribute("account");
   AddressBean address = (AddressBean) request.getAttribute("address");

   if(address != null){
       List<String> personalData = account.getAddresses().get(address.getAddressId()); %>

    <span id="span0" style="display:none"><%= address.getAddressId() %></span>
    <p><span id="span1"><%= personalData.get(1) %></span>&nbsp;<span id="span2"><%= personalData.get(2) %></span></p>
    <p><span id="span3"><%= personalData.get(0) %></span></p>
    <p><span id="span4"><%= address.getCountry() %></span><span id="span5">&nbsp;</span><span id="span6"><%= address.getZip() %></span></p>
    <p><span id="span7"><%= address.getCity() %></span><span id="span8">&nbsp;</span><span id="span9"><%= address.getStreet() %></span></p>

    <button type="button" id="changeAddress">Modifica</button>
    <button type="button" id="deleteAddress">Elimina</button>
<% } else { %>
    <p>Nessun indirizzo salvato</p>
    <button type="button" id="addAddress">Aggiungi indirizzo</button>
<% } %>
</div>

<a href="/GAMETOP/Home">Home</a> <a href="/GAMETOP/LogOut">Logout</a>

</div> <!-- Chiude il container -->

<%@ include file="/common/Footer.jsp" %>
</body>
</html>

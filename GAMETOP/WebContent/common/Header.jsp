<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="model.AccountBean" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameTop</title>
    <!-- Includi qui i tuoi fogli di stile CSS -->
    <link rel="stylesheet" href="/GAMETOP/styles/styleHeader.css">
    <!-- Includi qui il tuo script JavaScript esterno -->
   
</head>
<body>
    <header>
        <h1>GameTop</h1>
        <!-- Aggiungi qui altri elementi dell'intestazione, come menu di navigazione, logo, etc. -->
        <!-- Se vuoi includere l'orario attuale tramite JavaScript/AJAX, puoi farlo qui -->
        <div id="current-time"></div>
        <!-- Gestione accesso, login e carrello -->
        <%
     		// Recupera l'account dalla sessione
            AccountBean account = (AccountBean) session.getAttribute("account");
        

     // Se l'utente è autenticato, mostra i link al profilo dell'utente o all'area amministrativa a seconda delle credenziali
     if (account != null) {
         boolean admin = (Boolean) session.getAttribute("isAdmin");

         if (admin) {
             // Se l'utente è un amministratore, mostra il link al profilo amministrativo
     %>
             <a href="/GAMETOP/Admin/Profile">profilo</a><br>
     <%
         } else if (!admin) {
             // Se l'utente è un utente normale, mostra il link al profilo utente e al carrello
     %>
             <a href="/GAMETOP/User/Profile">profilo</a><br>
             <a href="/GAMETOP/Cart">carrello</a>
     <%
         }
     } else {
         // Se l'utente non è autenticato, mostra i link per il login e il carrello
     %>
         <a href="/GAMETOP/Login">login</a><br>
         <a href="/GAMETOP/Cart">carrello</a>
     <%
     }
     %>
    
    </header>
    <!-- Aggiungi qui altri contenuti che potrebbero apparire nell'header -->
</body>
</html>

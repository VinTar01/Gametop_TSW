<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1, width=device-width" charset="ISO-8859-1">
    <title>Login</title>
    <script src="common/scripts/validateLogin.js"></script>
    <%@ include file="Header.jsp" %>
    <link rel="stylesheet" href="/GAMETOP/css/styleLoginForm.css">
    
</head>
<body>



<form id="LogForm" action="LoginAccount" method="post">

    <span id="errorLogin"> ${requestScope.errorMessage}</span> <br>
    <label>Email</label> <br>
    <input type="email" name="email" required
     onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)">  <br>
     <span id="errorMail"></span>
    
    <label>Password</label> <br>
	<input type="password" name="password" 
       required 
       pattern=".{8,}"
       title="La password deve essere lunga almeno 8 caratteri"
       onchange="validateFormElem(this, document.getElementById('errorPassword'), passwordErrorMessage)"> 
	<span id="errorPassword"></span>
	<br>
    
    <button type="submit" onclick="return validate()">ACCEDI</button>
    <button type="reset">RESET</button> <br>
</form>

<div id="register-link">
    <a href="/GAMETOP/Signin">Registrati</a>
</div>

<%@ include file="Footer.jsp" %>   

</body>
</html>

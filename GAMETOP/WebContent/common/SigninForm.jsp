<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GameTop</title>
<%@ include file="Header.jsp" %>
<link rel="stylesheet" href="/GAMETOP/styles/styleSigninForm.css">
<script src="common/scripts/validateSignin.js"></script>
</head>
<body>

<form id="RegForm" action="/GAMETOP/SigninAccount" method="post">
		<label> Nome </label> <br>
		<input type="text" name="nome" required
		 onchange="validateName(this, document.getElementById('errorName'), nameErrorMessage))">  <br>
    	 <span id="errorName"></span> <br> <br>
		
		<label> Cognome </label> <br>
		<input type="text" name="cognome" required
		 onchange="validateName(this, document.getElementById('errorSurname'), surnameErrorMessage)">  <br>
    	 <span id="errorSurname"></span> <br> <br>
		
		<label> Data di nascita </label> <br>
		<input type="date" name="date" pattern="\d{4}-\d{2}-\d{2}">
		
		<label>Email</label> <br>
    	<input type="email" name="email" required
    	 onchange="validateFormElem(this, document.getElementById('errorEmail'), emailErrorMessage)">  <br>
    	 <span id="errorMail"></span> <br>
    
    	<label>Password</label> <br>
	    <input type="password" name="password" 
        required pattern=".{8,}"
        title="La password deve essere lunga almeno 8 caratteri"
        onchange="validateFormElem(this, document.getElementById('errorPassword'), passwordErrorMessage)"> <br>
	    <span id="errorPassword"></span>
	    <br>
		
		<button type="submit" onclick="return validate()"> REGISTRATI </button>
		<button type="reset"> RESET </button>
	</form>
	


<%@ include file="Footer.jsp" %>
</body>
</html>
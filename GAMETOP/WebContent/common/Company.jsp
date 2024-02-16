<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Company</title>
<%@ include file="Header.jsp"%>
<link rel="stylesheet" href="/GAMETOP/css/styleCompany.css">
</head>
<body>

<h1>${company.companyName}</h1>
${company.companyDescription}

<%@ include file="Footer.jsp"%>

</body>
</html>
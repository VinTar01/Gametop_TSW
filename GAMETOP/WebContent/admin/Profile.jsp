<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Profile</title>
<link rel="stylesheet" href="/GAMETOP/styles/styleProfileAdmin.css">
<script src="https://code.jquery.com/jquery-3.7.0.js"></script>
<script src="../user/scripts/AccountCover.js"></script>
<script src="../admin/scripts/ProductOperation.js"></script>
<script src="../admin/scripts/CategoryOperation.js"></script>
<script src="../admin/scripts/CompanyOperation.js"></script>
</head>
<body>

<h1>Profile</h1>

<img src="/GAMETOP/User/AccountControl?retrieve=1" width="10%" height=10% onerror="this.src='../imgs/nophoto.png'">

<form action="/GAMETOP/User/AccountControl?updateCover=true" method="post" enctype="multipart/form-data" id="upload">
    <input type="file" id="accountCover" name="accountCover" style="display:none;"/>
    <input type="button" value="CAMBIA IMMAGINE" onclick="changeAccountCover()" />
    <input type="submit" id="submitFile" style="display:none;">
</form><br>

Account ID: #${account.accountId}<br>
Nome: ${account.accountFirstName}<br>
Cognome: ${account.accountLastName}<br>
Email: ${account.email}<br> 
Data di nascita: ${account.birthDate}<br><br>

<h2>Product Operations</h2>

<form id="productForm" action="/GAMETOP/Admin/ProductControl" method="post" enctype="multipart/form-data">
   
<div>
    <input type="radio" name="productOperation" onchange="removeCategoryOperation(); removeCompanyOperation(); saveProduct(); " value="insert"> Inserisci
    <input type="radio" name="productOperation" onchange="removeCategoryOperation(); removeCompanyOperation(); updateProduct();" value="update"> Aggiorna 
    <input type="radio" id="productOperationDelete" name="productOperation" onchange="removeCategoryOperation(); removeCompanyOperation(); deleteProduct();" value="delete" >Elimina<br><br>
</div>
   
<div id = "productsIdDiv">
</div>
   
<div id = "productInformationDiv">
</div>
   
<div id = "productCategoriesDiv">
</div>
   
</form><br><br>
   
<h2>Category Operations</h2>

<form id="categoryForm" action="/GAMETOP/Admin/CategoryControl" method="post">
   
<div>
    <input type="radio" name="categoryOperation" onchange="removeProductOperation(); removeCompanyOperation(); saveCategory();" value="insert"> Inserisci
    <input type="radio" name="categoryOperation" onchange="removeProductOperation(); removeCompanyOperation(); updateCategory();" value="update"> Aggiorna 
    <input type="radio" name="categoryOperation" onchange="removeProductOperation(); removeCompanyOperation(); deleteCategory();" value="delete" >Elimina<br><br>
</div>
   
<div id = "categoriesIdDiv">
</div>
   
<div id = "categoryInformationDiv">
</div>
   
</form><br><br>
   
<h2>Company Operations</h2>

<form id="companyForm" action="/GAMETOP/Admin/CompanyControl" method="post">
   
<div>
    <input type="radio" name="companyOperation" onchange="removeProductOperation(); removeCategoryOperation(); saveCompany();" value="insert"> Inserisci
    <input type="radio" name="companyOperation" onchange="removeProductOperation(); removeCategoryOperation(); updateCompany();" value="update"> Aggiorna 
    <input type="radio" name="companyOperation" onchange="removeProductOperation(); removeCategoryOperation(); deleteCompany();" value="delete" >Elimina<br><br>
</div>
   
<div id = "companiesIdDiv">
</div>
   
<div id = "companyInformationDiv">
</div>
   
</form><br><br>

<a href="/GAMETOP/Home">Home</a> <a href="/GAMETOP/LogOut">Logout</a> 
<a href="/GAMETOP/admin/ViewOrders.jsp">Visualizza Ordini</a> <!-- Aggiunto il link per la pagina ViewOrders.jsp -->
   
</body>
</html>

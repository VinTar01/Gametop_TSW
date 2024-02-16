<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="java.util.LinkedList, model.CategoryBean"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product</title>
	<%@ include file="Header.jsp"%>
    <script src="https://code.jquery.com/jquery-3.7.0.js"></script>
    <link rel="stylesheet" href="/GAMETOP/css/styleProduct.css">
    <script src="common/scripts/Product.js"></script>
</head>
<body>

<div class="container">
    <div class="product-container">
        <h1>${product.productName}</h1>
        <!-- Immagine del prodotto -->
        <img src="/GAMETOP/Product?cover=${product.productId}" class="product-image"
             onerror="this.src='./imgs/image-not-found.png'" alt="Product Image">
        <!-- Prezzo del prodotto -->
        <p>${product.productPrice}&euro;</p>
        <!-- Bottone Aggiungi al Carrello -->
        <button onclick="addProduct()" class="add-to-cart-button">Aggiungi al carrello</button>

        <!-- Azienda e Categorie del prodotto -->
        <p>Azienda: <a href="/GAMETOP/Company?retrieve=${company.companyId}">${company.companyName}</a></p>
        <!-- Iterazione delle categorie -->
        <p>Categorie:
            <%
                LinkedList<CategoryBean> categoriesList = (LinkedList<CategoryBean>) request.getAttribute("categories");
                for (int i = 0; i < categoriesList.size(); i++) {
                    out.print(categoriesList.get(i).getCategoryName());
                    if (i != categoriesList.size() - 1) {
                        out.print(", ");
                    }
                }
            %>
        </p>
    </div>

    <div class="description-container">
        <!-- Descrizione del prodotto -->
        <h2>Descrizione</h2>
        <p>${product.productDescription}</p>
        <!-- Codice del prodotto -->
        <p>Codice prodotto: <span id="productId">${product.productId}</span></p>
    </div>
</div>

<%@ include file="Footer.jsp" %>
</body>
</html>



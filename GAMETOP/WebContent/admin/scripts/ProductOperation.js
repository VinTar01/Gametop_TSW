function saveProduct(){

	removeProductsIdSelectDiv();
	removeProductInformationGroupDiv();
	addProductInformationGroupDiv();
	removeProductCategoriesSelectDiv(0);
	addProductCategoriesSelectDiv(1);
	addProductButton();
}

function updateProduct(){

	removeProductsIdSelectDiv();
	addProductsIdSelectDiv();
	removeProductInformationGroupDiv();
	
	$("#productInformationDiv").append('<div id="productInformationGroupDiv">');
		
	let divName = $('<div id = "productName">');
		
	let checkbox = $('<input type="checkbox" id="checkboxProductName" name="productInformation" value="name">');
	checkbox.on("change", function() { 

						  	if($(this).prop("checked"))								
								$("#productName").append(" ", '<input type="text" id="textProductName" name="name">');
							else
								$("#textProductName").remove();
						});
						
	divName.append(checkbox, " ", "<label>Nome</label>");
	
	let divActive = $('<div id = "productActive">');
		
	checkbox = $('<input type="checkbox" id="checkboxProductActive" name="productInformation" value="active">');
	checkbox.on("change", function() { 

						  	if($(this).prop("checked"))								
								$("#productActive").append(" ", '<select id="selectProductActive" name="selectProductActive"><option value="true">si</option><option value="false">no</option></select>');
							else
								$("#selectProductActive").remove();
						});
						
	divActive.append(checkbox, " ", "<label>Attivo</label>");
	
	let divPrice = $('<div id = "productPrice">');;
		
	checkbox = $('<input type="checkbox" id="checkboxPrice" name="productInformation" value="price">');
	checkbox.on("change", function() { 
							
							if($(this).prop("checked"))
								$("#productPrice").append(" ", '<input type="text" id="textPrice" name="price">');
							else
								$("#textPrice").remove();
						});
						
	divPrice.append(checkbox, " ", "<label>Prezzo</label>");					
	
	let divCover = $('<div id = "productCover">');
		
	checkbox = $('<input type="checkbox" id="checkboxCover" name="productInformation" value="cover">');
	checkbox.on("change", function() { 
							
							if($(this).prop("checked"))
								$("#productCover").append(" ", '<input type="file" id="fileCover" name="cover" required>');
							else
								$("#fileCover").remove();
						});
		
	divCover.append(checkbox, " ", "<label>Immagine</label>");					
	
	let divDescription = $('<div id = "productDescription">');
	
	checkbox = $('<input type="checkbox" id="checkboxProductDescription" name="productInformation" value="description">');	
	checkbox.on("change", function() { 
							
							if($(this).prop("checked"))
								$("#productDescription").append(" ", '<textarea id="textProductDescription" name="description" rows="4" cols="50"></textarea>');
							else
								$("#textProductDescription").remove();
						});
						
	divDescription.append(checkbox, " ", "<label>Descrizione</label>");					
	
	let divCompany = $('<div id = "productCompany">');
	
	checkbox = $('<input type="checkbox" id="checkboxCompany" name="productInformation" value="company">');	
	checkbox.on("change", function() {
	 
							if($(this).prop("checked")){
								$("#productCompany").append(" ", '<select id="productCompanies" name="productCompanies"></select>');
								
								$.post("/GAMETOP/Admin/CompanyControl", {"companyOperation" : "retrieveAll"}, function(data){			
									$.each(data, function(i, company) {
										$("#productCompanies").append('<option value="' + company.companyId + '">' + company.companyName + '</option>');
									});
								});
							}
							else
								$("#productCompanies").remove();
						});
						
	divCompany.append(checkbox, " ", "<label>Azienda</label>");					
	
	$("#productInformationGroupDiv").append(divName, divActive, divPrice, divCover, divDescription, divCompany);
	
	removeProductCategoriesSelectDiv(0);
	
	$("#productCategoriesDiv").append($('<input type="checkbox" id="checkboxCategory" name="productInformation" value="category">'), " ", "<label>Categoria</label>");
	$("#checkboxCategory").on("change", function() {
	 										let checkbox;
	 
											if($(this).prop("checked")){
												checkbox = $("#checkboxCategory").detach();
												removeProductCategoriesSelectDiv(0);
												addProductCategoriesSelectDiv(1);
												$("#productCategoriesSelectDiv1").prepend(checkbox, " ");
											}
											else{
												checkbox = $("#checkboxCategory").detach();
												removeProductCategoriesSelectDiv(0);
												$("#productCategoriesDiv").append(checkbox, " ", "<label>Categoria</label>");
											}
										});
										
	addProductButton();
}

function deleteProduct(){

	removeProductsIdSelectDiv();
	addProductsIdSelectDiv();
	removeProductInformationGroupDiv();
	removeProductCategoriesSelectDiv(0);
	addProductButton();
}

function removeProductOperation(){

	$("#productForm input[type='radio']").prop("checked", false);

	removeProductsIdSelectDiv();
	removeProductInformationGroupDiv();
	removeProductCategoriesSelectDiv(0);
	removeProductButton();
}

function addProductsIdSelectDiv(){
	
	if($("#productsIdSelectDiv")[0] == null){
		
		$("#productsIdDiv").append('<div id="productsIdSelectDiv">');
	
		$("#productsIdSelectDiv").append("<label>Codice Prodotto</label>", " ", '<select id="products" name="products"></select>');
		
		$.post("/GAMETOP/Admin/ProductControl", {"productOperation" : "retrieveAll"}, function(data){	
			$.each(data, function(i, product) {
				if(product.active)
					$("#products").append('<option value="' + product.productId + '">' + product.productName + '</option>');
				else{
					if($('#productOperationDelete').is(':checked')){
						alert("Checked");
						$("#products").append('<option value="' + product.productId + '" disabled>' + product.productName + " - DISATTIVATO" + '</option>');
					}
					else
						$("#products").append('<option value="' + product.productId + '">' + product.productName + " - DISATTIVATO" + '</option>');
				}
			});
		})
	}
}

function removeProductsIdSelectDiv(){
	if($("#productsIdSelectDiv")[0] != null)
		$("#productsIdSelectDiv").remove();
}

function addProductInformationGroupDiv(){

	$("#productInformationDiv").append('<div id="productInformationGroupDiv">');
	
	$("#productInformationGroupDiv").append("<label>Nome</label>", " ", '<input type="text" name="name">', "<br>");
	$("#productInformationGroupDiv").append("<label>Attivo</label>", " ", '<select name="active"><option value="true">si</option><option value="false">no</option></select>', "<br>");
	$("#productInformationGroupDiv").append("<label>Prezzo</label>", " ", '<input type="text" name="price">', "<br>");
	$("#productInformationGroupDiv").append("<label>Immagine</label>", " ", '<input type="file" name="cover" required>', "<br>");
	$("#productInformationGroupDiv").append("<label>Descrizione</label>", " ", '<textarea name="description" rows="4" cols="50"></textarea>', "<br>");
	$("#productInformationGroupDiv").append("<label>Azienda</label>", " ", '<select id="productCompanies" name="productCompanies"></select>', "<br>");
	
	$.post("/GAMETOP/Admin/CompanyControl", {"companyOperation" : "retrieveAll"}, function(data){			
		$.each(data, function(i, company) {
			$("#productCompanies").append('<option value="' + company.companyId + '">' + company.companyName + '</option>');
		});
	});	
}

function removeProductInformationGroupDiv(){
	if($("#productInformationGroupDiv")[0] != null)
		$("#productInformationGroupDiv").remove();
}

function addProductCategoriesSelectDiv(num){

	$("#productCategoriesDiv").append('<div id="productCategoriesSelectDiv' + num + '">');
	
	if(num == 1){
		addProductCategoriesSelectDiv(num + 1);
		$("#productCategoriesSelectDiv" + num).append("<label>Seleziona categoria</label>", " ", '<select id="productCategories' + num + '" name="productCategories' + num + '"></select>');
	}
	else
		$("#productCategoriesSelectDiv" + num).append("<label>Seleziona sottocategoria</label>", " ", '<select id="productCategories' + num + '" name="productCategories' + num + '"></select>');
	
	if(num > 1){
		$("#productCategoriesSelectDiv" + num).append(" ", '<button type="button" id="add' + num + '">+</button>');
		$("#add" + num).on("click", function(){ addProductCategoriesSelectDiv(num + 1);});
	}
		
	if(num > 2){
		$("#productCategoriesSelectDiv" + num).append(" ", '<button type="button" id="remove' + num + '">-</button>');
		$("#remove" + num).on("click", function(){ removeProductCategoriesSelectDiv(num);});
	}
		
	if(num > 3)
		$("#remove" + (num - 1)).attr("disabled", true);
		
	$.post("/GAMETOP/Admin/CategoryControl", {"categoryOperation" : "retrieveAll"}, function(data){			
		$.each(data, function(i, category) {
				$("#productCategories" + num).append('<option value="' + category.categoryId + '">' + category.categoryName + '</option>');
		});
	});
}

function removeProductCategoriesSelectDiv(num){

	if(num == 0)
		$("#productCategoriesDiv").empty();			//remove all child
	
	else if($("#productCategoriesSelectDiv" + num)[0] != null)
		$("#productCategoriesSelectDiv" + num).remove();
	
	if(num > 2)
		$("#remove" + (num - 1)).attr("disabled", false);
}

function addProductButton(){
	if(!$("#productForm").children().last().is("button"))
		$("#productForm").append('<button type="submit">Esegui</button>');
}

function removeProductButton(){
	if($("#productForm").children().last().is("button"))
		$("#productForm").children().last().remove();
}
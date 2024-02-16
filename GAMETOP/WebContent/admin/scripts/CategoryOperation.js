function saveCategory(){

	removeCategoriesIdSelectDiv();
	addCategoryInformationGroupDiv();
	addCategoryButton();
}

function updateCategory(){

	addCategoriesIdSelectDiv();
	addCategoryInformationGroupDiv();
	addCategoryButton();
}

function deleteCategory(){

	addCategoriesIdSelectDiv();
	removeCategoryInformationGroupDiv();
	addCategoryButton();
}

function removeCategoryOperation(){
	
	$('#categoryForm input[type="radio"]').prop("checked", false);
	
	removeCategoriesIdSelectDiv();
	removeCategoryInformationGroupDiv();
	removeCategoryButton();
}

function addCategoriesIdSelectDiv(){
	
	if($("#categoriesIdSelectDiv")[0] == null){
		
		$("#categoriesIdDiv").append('<div id="categoriesIdSelectDiv">');
		
		$("#categoriesIdSelectDiv").append("<label>Codice Categoria</label>", " ", '<select id="categories" name="categories"></select>');
		
		$.post("/GAMETOP/Admin/CategoryControl", {"categoryOperation" : "retrieveAll"}, function(data){
			$.each(data, function(i, category) {
				$("#categories").append('<option value="' + category.categoryId + '">' + category.categoryName + '</option>');
			});
		})
	}
}

function removeCategoriesIdSelectDiv(){
	if($("#categoriesIdSelectDiv")[0] != null)
		$("#categoriesIdSelectDiv").remove();
}

function addCategoryInformationGroupDiv(){
	
	if($("#categoryInformationGroupDiv")[0] == null){
		
		$("#categoryInformationDiv").append('<div id="categoryInformationGroupDiv">');
	
		$("#categoryInformationGroupDiv").append("<label>Nome</label>", " ", '<input type="text" name="name">', "<br>");
	}
}

function removeCategoryInformationGroupDiv(){
	if($("#categoryInformationGroupDiv")[0] != null)
		$("#categoryInformationGroupDiv").remove();
}

function addCategoryButton(){

	if(!$("#categoryForm").children().last().is("button"))
		$("#categoryForm").append('<button type="submit">Esegui</button>');
}

function removeCategoryButton(){

	if($("#categoryForm").children().last().is("button"))
		$("#categoryForm").children().last().remove();
}
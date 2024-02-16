function saveCompany(){

	removeCompaniesIdSelectDiv();
	removeCompanyInformationGroupDiv();
	addCompanyInformationGroupDiv();
	addCompanyButton();
}

function updateCompany(){

	addCompaniesIdSelectDiv();
	removeCompanyInformationGroupDiv();
	
	$("#companyInformationDiv").append('<div id="companyInformationGroupDiv">');
	
	let divName = $('<div id = "companyName">');
	
	let checkbox = $('<input type="checkbox" id="checkboxCompanyName" name="companyInformation" value="name">');
	checkbox.on("change", function() {
							 	
						  	if($(this).prop("checked"))
								$("#companyName").append(" ", '<input type="text" id="textCompanyName" name="name">');
							else
								$("#textCompanyName").remove();
						  });
	
	divName.append(checkbox, " ", "<label>Nome</label>");
	
	let divDescription = $('<div id = "companyDescription">');
	
	checkbox = $('<input type="checkbox" id="checkboxCompanyDescription" name="companyInformation" value="description">');
	checkbox.on("change", function() {
							 	
						  	if($(this).prop("checked"))
								$("#companyDescription").append(" ", '<textarea id="textCompanyDescription" name="description" rows="4" cols="50"></textarea>');
							else
								$("#textCompanyDescription").remove();
						  });
	
	divDescription.append(checkbox, " ", "<label>Descrizione</label>");
	
	$("#companyInformationGroupDiv").append(divName, divDescription);
	
	addCompanyButton();		
}

function deleteCompany(){

	addCompaniesIdSelectDiv();
	removeCompanyInformationGroupDiv();
	addCompanyButton();
}

function removeCompanyOperation(){
	
	$('#companyForm input[type="radio"]').prop("checked", false);
	
	removeCompaniesIdSelectDiv();
	removeCompanyInformationGroupDiv();
	removeCompanyButton();
}

function addCompaniesIdSelectDiv(){
	
	if($("#companiesIdSelectDiv")[0] == null){
		
		$("#companiesIdDiv").append('<div id="companiesIdSelectDiv">');
		
		$("#companiesIdSelectDiv").append("<label>Codice Azienda</label>", " ", '<select id="companies" name="companies"></select>');
		
		$.post("/GAMETOP/Admin/CompanyControl", {"companyOperation" : "retrieveAll"}, function(data){
			$.each(data, function(i, company) {
				$("#companies").append('<option value="' + company.companyId + '">' + company.companyName + '</option>');
			});
		})
	}
}

function removeCompaniesIdSelectDiv(){
	if($("#companiesIdSelectDiv")[0] != null)
		$("#companiesIdSelectDiv").remove();
}

function addCompanyInformationGroupDiv(){
	
	$("#companyInformationDiv").append('<div id="companyInformationGroupDiv">');
	
	$("#companyInformationGroupDiv").append("<label>Nome</label>", " ", '<input type="text" name="name">', "<br>");
	$("#companyInformationGroupDiv").append("<label>Descrizione</label>", " ", '<textarea name="description" rows="4" cols="50"></textarea>', "<br>");
}

function removeCompanyInformationGroupDiv(){	
	if($("#companyInformationGroupDiv")[0] != null)
		$("#companyInformationGroupDiv").remove();
}

function addCompanyButton(){

	if(!$("#companyForm").children().last().is("button"))
		$("#companyForm").append('<button type="submit">Esegui</button>');
}

function removeCompanyButton(){

	if($("#companyForm").children().last().is("button"))
		$("#companyForm").children().last().remove();
}
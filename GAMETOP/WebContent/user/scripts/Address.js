$(document).ready(function(){
	var divChildren = $("#addressDiv").children().clone();
	
	Address(divChildren);
});
	
function Address(divChildren){
	
	
	$("#addAddress").on("click", function(){
		
		$("#addressDiv").empty();
		
		$("#addressDiv").append('<form id="addressForm" method="post"><fieldset id="fieldset"></fieldset></form>');
								
		$("#fieldset").append('<label>Nome</label>', ' ', '<input type="text" id="name" name="name">', '<br>',
								 '<label>Cognome</label>', ' ', '<input type="text" id="surname" name="surname">', '<br>',
								 '<label>Numero di telefono</label>', ' ', '<input type="tel" id="phone" name="phone">', '<br>',
								 '<label>Nazione</label>', ' ', '<input type="text" id="country" name="country">', '<br>',
								 '<label>Citt&agrave;</label>', ' ', '<input type="text" id="city" name="city">', '<br>',
								 '<label>Codice Postale</label>', ' ', '<input type="text" id="zip" name="zip">', '<br>',
								 '<label>Piazza/Viale/Via</label>', ' ', '<input type="text" id="street" name="street">', '<br><br>',
								 '<button type="button" id="cancel">Annulla</button>', ' ', '<button type="button" id="save">Salva</button>');
							
		Address(divChildren);	
	});
	
	$("#cancel").on("click", function(){
		$("#addressDiv").empty();
		
		$("#addressDiv").append(divChildren);
		
		Address(divChildren);
	});
		
	$("#save").on("click", function(){
		
		var operation;
		
		if($("#addressId").length)
			operation = "update";
		else
			operation = "insert";
		
		$.post("/GAMETOP/User/AddressControl", {"operation" : operation,
												"id" : $("#addressId").val(),
												"name" : $("#name").val(),
												"surname" : $("#surname").val(),
												"phone" : $("#phone").val(),
												"country" : $("#country").val(),
												"city" : $("#city").val(),
												"zip" : $("#zip").val(),
												"street" : $("#street").val()},
												function(data){
													
			$("#addressDiv").empty();
													
			$.each(data, function(i, element) {
				
				if(i == 0)
					$("#addressDiv").append('<span id="span' + i +'" style="display:none">' + element + '</span>');
				else	
					$("#addressDiv").append('<span id="span' + i +'">' + element + '</span>');
				
				if(i == 1 || i == 5)
					$("#addressDiv").append("<span>&nbsp;</span>");
				else
					$("#addressDiv").append("<br>");		
			});
			
			$("#addressDiv").append('<button type="button" id="changeAddress">Modifica</button><span>&nbsp;</span><button type="button" id="deleteAddress">Elimina</button>');
			
			divChildren = $("#addressDiv").children().clone();
			
			Address(divChildren);			
		});
	});
	
	$("#changeAddress").on("click", function(){
		
		const elements = [];
		
		for(var i = 0; i < 8; i++)
			elements[i] = $("#span" + i).text();
		
		children = $("#addressDiv").empty();
		
		$("#addressDiv").append('<form id="addressForm" method="post"><fieldset id="fieldset"></fieldset></form>');
		
		$("#fieldset").append('<input type="text" id="addressId" name="id" style="display:none" value="' + elements[0] + '">', 
								'<label>Nome</label>', ' ', '<input type="text" id="name" name="name" value="' + elements[1] + '">', '<br>',
								'<label>Cognome</label>', ' ', '<input type="text" id="surname" name="surname" value="' + elements[2] + '">', '<br>',
								'<label>Numero di telefono</label>', ' ', '<input type="tel" id="phone" name="phone" value="' + elements[3] + '">', '<br>',
								'<label>Nazione</label>', ' ', '<input type="text" id="country" name="country" value="' + elements[4] + '">', '<br>',
								'<label>Citt&agrave;</label>', ' ', '<input type="text" id="city" name="city" value="' + elements[5] + '">', '<br>',
								'<label>Codice Postale</label>', ' ', '<input type="text" id="zip" name="zip" value="' + elements[6] + '">', '<br>',
								'<label>Piazza/Viale/Via</label>', ' ', '<input type="text" id="street" name="street" value="' + elements[7] + '">', '<br><br>',
								'<button type="button" id="cancel">Annulla</button>', ' ', '<button type="button" id="save">Salva</button>');	
								
		Address(divChildren);						
	});
	
	$("#deleteAddress").on("click", function(){
		
		if (confirm("Sei sicuro di voler cancellare questo indirizzo?")) {

			$.post("/GAMETOP/User/AddressControl", {"operation" : "delete", "id" : $("#span0").text()}, function(data){
				$("#addressDiv").empty();
				$("#addressDiv").append('<span>' + data + '</span>', '<br>', '<button type="button" id="addAddress">Aggiungi indirizzo</button>');
				
				divChildren = $("#addressDiv").children().clone();
				Address(divChildren);
			});
  		}
	});
}
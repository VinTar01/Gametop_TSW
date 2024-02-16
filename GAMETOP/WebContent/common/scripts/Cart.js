
/*Questo script JavaScript associato alla pagina JSP del carrello (Cart.jsp) gestisce le interazioni dell'utente con il carrello,
 consentendo l'aggiunta o la rimozione di prodotti e l'aggiornamento dinamico del carrello senza dover ricaricare completamente la pagina.
*/

function addProduct(data){
	
	//alert(data);
	//alert($(data)[0]);
	//alert(data.parent().children().first().text());
	
	/*In particolare, il metodo addProduct(data) è chiamato quando l'utente preme il pulsante "+" o "-", 
	mentre il metodo removeProduct(data) è chiamato quando l'utente preme il pulsante "Rimuovi".

	Il metodo addProduct(data):

	Estrae l'ID del prodotto dal primo figlio dell'elemento genitore.
	Aggiorna la quantità del prodotto nel carrello in base all'azione dell'utente (+1 o -1).
	Invia una richiesta POST al servlet CartControl con i parametri "operation" (operazione di aggiunta), 
	"id" (ID del prodotto) e "quantity" (quantità da aggiungere).
	Aggiorna dinamicamente la pagina con i nuovi dati ricevuti dalla servlet.*/

	var id = data.parent().children().first().text();
	
	var input = data.parent().find("input");
	var value = parseInt(input.val(), 10);
	var quantity; 
	
	//alert(id + ", " + input[0] + ", " + quantity);
	
	if(data.text() == "-"){
		value -= 1;
		
		if(value == 1)
			data.prop("disabled", true);
			
		if(value == 2)
			$("#add" + id).prop("disabled", false);
			
		quantity = -1;
	}
	else{
		value += 1;
		
		if(value == 2)
			$("#remove" + id).prop("disabled", false);
		
		if(value == 3)
			data.prop("disabled", true);
			
		quantity = 1;
	}
	
	input.val(value);
	
	$.post("/GAMETOP/CartControl", {"operation" : "add", "id" : id, "quantity" : quantity}, function(data){
		
		var id;
		
		$.each(data, function(i, object) {
			
			//alert(i + ", " + object);
			
			if(i == 0){
				//alert(object);
				id = object;
			}
			
			if(i == 1){	
				$("#span" + id).text(object);
				//alert(id);
			}
			
			if(i == 2){
				$("#subtotal").text(object);
				$("#total").text(object);
			}
		});
	});
}



/*Il metodo removeProduct(data):

Estrae l'ID del prodotto dal primo figlio dell'elemento genitore.
Invia una richiesta POST al servlet CartControl con i parametri "operation" (operazione di rimozione) e "id" (ID del prodotto).
Aggiorna dinamicamente la pagina con i nuovi dati ricevuti dalla servlet, rimuovendo l'elemento corrispondente dal carrello 
o reindirizzando l'utente alla homepage se il carrello è vuoto.
In sostanza, questo script consente un'interazione fluida con il carrello dell'utente, 
senza dover aggiornare manualmente la pagina ogni volta che viene apportata una modifica al carrello.
*/
function removeProduct(data){
	
	//alert(data[0]);
	//alert(data.parent().children().first().text());
	
	var id = data.parent().children().first().text();
	
	$.post("/GAMETOP/CartControl", {"operation" : "remove", "id" : id}, function(data){
		$.each(data, function(i, object) {
			if(object == "home"){
				//alert(object);
				$("#" + object + " span").trigger("click");
			}
			else if(i == 0)
				$("#div" + data).remove();
			else{
				$("#subtotal").text(object);
				$("#total").text(object);
			}
		});
	});
	
	$("#div" + id).find("hr").remove();
}







function addProduct(){
	
	$.post("/GAMETOP/CartControl", {"operation" : "add", "id" : $("#productId").text()}, function(data){
	var flag=true;
		$.each(data, function(i, object) {
				
			if(i == 3){
				alert(object);
				flag=false;
				}
			
		});
		if(flag)
		 alert("Prodotto aggiunto al carrello");
	})
}


/*Lo script JavaScript addProduct() effettua una richiesta POST al servlet CartControl per aggiungere un prodotto al carrello. 

Effettua una richiesta POST a "/GAMETOP/CartControl" con i parametri "operation" impostato su "add" e "id" impostato sull'ID del prodotto, 
ottenuto dall'elemento HTML con id "productId".

Nel callback della richiesta AJAX, esegue un'iterazione attraverso i dati ricevuti dalla risposta del servlet.

Utilizza la funzione $.each() per iterare attraverso l'array data e per ogni elemento, 
esegue una funzione di callback che riceve l'indice e il valore corrente.

Controlla se l'indice i è uguale a 3. Se sì, mostra un messaggio di avviso (alert) contenente il valore corrente object.

In sintesi, quando viene chiamata la funzione addProduct(),
 viene effettuata una richiesta al servlet CartControl per aggiungere un prodotto al carrello.
  Se la richiesta ha successo, viene mostrato un messaggio di avviso contenente i dati aggiunti al carrello.

Questo è sembra essere associato a una funzione di aggiunta di prodotti in una pagina JSP chiamata Product.jsp,


*/

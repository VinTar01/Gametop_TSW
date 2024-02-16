/*Questo file JavaScript associato alla pagina Payment.jsp
 gestisce il comportamento dinamico della pagina in base alla selezione del metodo di pagamento (PayPal o Carta di Credito). Ecco cosa fa:

Utilizza $(document).ready() per assicurarsi che il codice venga eseguito quando il documento è pronto.
Assegna un evento di cambio (change) agli elementi radio con id paypal e card.
Quando viene selezionata l'opzione PayPal (#paypal), svuota il contenuto del div con id paymentDiv
 se esiste e aggiunge un campo di input per l'email e un pulsante di pagamento.
Quando viene selezionata l'opzione Carta di Credito (#card), 
svuota il contenuto del div con id paymentDiv se esiste e aggiunge una serie di campi di input per i dettagli della carta di credito
 (nome, numero carta, data di scadenza, CVV) e un pulsante di pagamento.
In sostanza, questo script modifica dinamicamente il contenuto del div paymentDiv in base al metodo di pagamento selezionato dall'utente.
*/

$(document).ready(function(){

	$("#paypal").on("change", function(){
		if($("#paymentDiv").lenght != 0)
			$("#paymentDiv").empty();
			
		$("#paymentDiv").append('<label>Email</label>', ' ', '<input type="text" name="email">', '<br><br>', '<button>Paga</button>');
	});
	
	
	$("#card").on("change", function(){
		if($("#paymentDiv").lenght != 0)
			$("#paymentDiv").empty();
			
		$("#paymentDiv").append('<label>Nome sulla carta di credito</label>', ' ', '<input type="text" name="fullname">', '<br>',
		                        '<label>Numero Carta</label>', ' ', '<input type="text" name="cardNumber">', '<br>',
								'<label>Data di scadenza</label>', '<select name="month"><option value="1">01</option><option value="2">02</option>' +
								'<option value="3">03</option><option value="4">04</option><option value="5">05</option><option value="6">06</option>' +
								'<option value="7">07</option><option value="8">08</option><option value="9">09</option><option value="10">10</option>' +
								'<option value="11">11</option><option value="12">12</option></select>', '<select name="year">' +
								'<option value="2023">2023</option><option value="2024">2024</option><option value="2025">2025</option>' +
								'<option value="2026">2026</option><option value="2027">2027</option><option value="2028">2028</option>' +
								'<option value="2029">2029</option><option value="2030">2030</option><option value="2031">2031</option>' +
								'<option value="2032">2032</option>', '<br>', '<label>CVV</label>', ' ', '<input type="text" name="cvv">', '<br><br>',
								'<button>Paga</button>');
	});
});
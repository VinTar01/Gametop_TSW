/**
 * 
 */



const emptyFieldErrorMessage  = "Questo campo non puo essere vuoto";
const emailErrorMessage = "Devi rispettare il formato username@domain.ext";
const passwordErrorMessage = "La password deve essere lunga almeno 8 caratteri"
const nameErrorMessage  = "La lettera iniziale deve essere maiuscola";
const surnameErrorMessage  = "La lettera iniziale deve essere maiuscola";


function validateFormElem(formElem, span, errorMessage){
	if(formElem.checkValidity()){
		formElem.classList.remove("error");
		span.style.color = "black";
		span.innerHTML = "";
		return true;
	}
	formElem.classList.add("error");
	span.style.color = "red";
	if(formElem.validity.valueMissing){
		span.innerHTML = emptyFieldErrorMessage;
	} else{
		span.innerHTML = errorMessage;
	}
	
	return false;
	
	
}






function validate() {
    let valid = true;   
    let form = document.getElementById("RegForm");
    
    let spanEmail = document.getElementById("errorMail");
    let spanPassword = document.getElementById("errorPassword");
    
    let spanName = document.getElementById("errorName");
    let spanSurname = document.getElementById("errorSurname");
    
    // Esegui la validazione per nome e cognome
    if (!validateFormElem(form.nome, spanName, nameErrorMessage)) {
        valid = false;
    }
    if (!validateFormElem(form.cognome, spanSurname, surnameErrorMessage)) {
        valid = false;
    }

    // Esegui la validazione per l'email e la password
    if (!validateFormElem(form.email, spanEmail, emailErrorMessage)) {
        valid = false;
    }
    if (!validateFormElem(form.password, spanPassword, passwordErrorMessage)) {
        valid = false;
    }
    
    // Se entrambi i campi sono vuoti, restituisci false
    if (form.nome.value.trim() === "" || form.cognome.value.trim() === "" || form.email.value.trim() === "" || form.password.value.trim() === "") {
        valid = false;
    }
    
    return valid;
}

/**
 * 
 */


const emptyFieldErrorMessage  = "Questo campo non puo essere vuoto";
const emailErrorMessage = "Devi rispettare il formato username@domain.ext";
const passwordErrorMessage = "La password deve essere lunga almeno 8 caratteri"


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
	let form = document.getElementById("LogForm");
	
	let spanEmail = document.getElementById("errorMail");
	if (!validateFormElem(form.email, spanEmail, emailErrorMessage)){
		valid = false;
	}
	
	let spanPassword = document.getElementById("errorPassword");
	if (!validateFormElem(form.password, spanPassword, passwordErrorMessage)){
		valid = false;
	}
	
	return valid;
}
// Messaggi di errore
const emptyFieldErrorMessage = "Questo campo non può essere vuoto";
const capErrorMessage = "Devi inserire 5 cifre numeriche";
const phoneErrorMessage = "Devi inserire un numero di telefono valido (10 cifre)";

// Funzione per validare un campo del form
function validateFormElem(formElem, span, errorMessage) {
    if (formElem.checkValidity()) {
        formElem.classList.remove("error");
        span.style.color = "black";
        span.textContent = "";
        return true;
    } else {
        formElem.classList.add("error");
        span.style.color = "red";
        span.textContent = errorMessage;
        return false;
    }
}

// Funzione per validare tutti i campi del form prima di inviare il modulo
function validate() {
    let valid = true;
    let form = document.getElementById("AddressForm");

    // Seleziona gli span per i messaggi di errore
    let spanPhone = document.getElementById("errorPhone");
    let spanCap = document.getElementById("errorCap");

    // Esegui la validazione per il numero di telefono
    if (!validateFormElem(form.phone, spanPhone, phoneErrorMessage)) {
        valid = false;
    }

    // Esegui la validazione per il CAP
    if (!validateFormElem(form.zip, spanCap, capErrorMessage)) {
        valid = false;
    }

    // Verifica se ci sono campi vuoti
    if (form.name.value.trim() === "" || form.surname.value.trim() === "" || form.phone.value.trim() === "" || form.country.value.trim() === "" || form.city.value.trim() === "" || form.zip.value.trim() === "" || form.street.value.trim() === "") {
        valid = false;
    }

    return valid;
}


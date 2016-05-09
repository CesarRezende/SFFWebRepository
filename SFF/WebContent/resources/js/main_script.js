function cancelKeyPressEvent(event) {

	if (event.keyCode != 9) {
		if (event.preventDefault) {
			event.preventDefault();
		}
		if (event.stopImmediatePropagation) {
			event.stopImmediatePropagation();
		}
	}

	
}
function validateRequest() {

	if ($("#validateRequestMsg").length >= 0) {

		if (confirm($("#validateRequestMsg").val())) {
			return true;
		} else {
			return false;
		}

	}

}


function createSoapMsg(methodName, fieldsObject){
	
	var soapMessage =   '<?xml version="1.0" encoding="utf-16"?>' +
						'<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">'+
						' <soap:Body>'+
						    '<'+methodName+' xmlns="http://controller/">';
	
	var fieldsObjectKeys = Object.keys(fieldsObject);
	for(var i = 0; i < fieldsObjectKeys.length; i++){
		soapMessage += '<'+fieldsObjectKeys[i]+' xmlns="">'+fieldsObject[fieldsObjectKeys[i]]+'</'+fieldsObjectKeys[i]+'>';	
	}
	
     
	soapMessage += '</'+methodName+'>'+
				   '</soap:Body>'+
				   '</soap:Envelope>';
	
	return soapMessage;
	
}

function validateEntradaVariavelManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		if ($(".error_message").length <= 0) {
			window.parent.$('#entradavariavelmanager').dialog('close');
		}

	}
}

function loadJQueryEntradaVariavelManager() {

	$("#mainform\\:txtID").prop("disabled", true);

	$(".valuefield").maskMoney({thousands:'.', decimal:',', allowZero:true});
	
	$(".valuelong").mask('0000');
	
	$(".integerfield").maskMoney({thousands:'.', decimal:',', allowZero:true, precision:0});
	
	$(".datefield").mask('00/00/0000');
	
	$(".datefield").datepicker();
	
	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#entradavariavelmanager').dialog('close');

	});

	$(".ui-button").button();
}


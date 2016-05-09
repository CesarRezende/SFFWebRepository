function validateEntradaFixaManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		if ($(".error_message").length <= 0) {
			window.parent.$('#entradafixamanager').dialog('close');
		}

	}
}

function loadJQueryEntradaFixaManager() {

	$("#mainform\\:txtID").prop("disabled", true);

	$(".valuefield").maskMoney({thousands:'.', decimal:',', allowZero:true});
	
	$(".integerfield").maskMoney({thousands:'.', decimal:',', allowZero:true, precision:0});
	
	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#entradafixamanager').dialog('close');

	});

	$(".ui-button").button();
}


function validateTipoGastoManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		if ($(".error_message").length <= 0) {
			window.parent.$('#tipogastomanager').dialog('close');
		}

	}
}

function loadJQueryTipoGastoManager() {

	$("#mainform\\:txtID").prop("disabled", true);

	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#tipogastomanager').dialog('close');

	});

	$(".ui-button").button();
}


function validateUsuarioManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		if ($(".error_message").length <= 0) {
			window.parent.$('#usuariomanager').dialog('close');
		}

	}
}

function loadJQueryUsuarioManager() {

	$("#mainform\\:txtID").prop("disabled", true);

	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#usuariomanager').dialog('close');

	});

	$(".ui-button").button();
}


function validateFamiliaManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		if ($(".error_message").length <= 0) {
			window.parent.$('#familiamanager').dialog('close');
		}

	}
}

function loadJQueryFamiliaManager() {

	$("#mainform\\:txtID").prop("disabled", true);

	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#familiamanager').dialog('close');

	});

	$(".ui-button").button();
}


function enabletxtTipoGasto(){
	if($("#mainform\\:optTipoMovimentacao\\:0").is(":checked")){
		$("#mainform\\:txtTipoGasto").prop("disabled",true);
	}else{
		$("#mainform\\:txtTipoGasto").prop("disabled",false);
	}	
} 

$(document).ready(function(){
	setTimeout(enabletxtTipoGasto, 100);
});
 
function validateMovimentacaoFinanceiraManager(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		
		if ($(".error_message").length <= 0) {
			window.parent.$('#movimentacaofinanceiramanager').dialog('close');
		}

	}
} 

function loadJQueryMovimentacaoFinanceira() {

	$("#mainform\\:txtID").prop("disabled", true);

	$("#mainform\\:txtDataLancamento").prop("disabled", true);

	
	if($("#mainform\\:optSituacao\\:0").is(":checked")){
		$("#mainform\\:txtDataRealizada").prop("disabled", true);
	}
	
	$("#mainform\\:btnCancel").click(function() {

		window.parent.$('#movimentacaofinanceiramanager').dialog('close');

	});

	
	
	$(".valuefield").maskMoney({thousands:'.', decimal:',', allowZero:true});
	
	$(".datefield").mask('00/00/0000');
	
	$(".ui-button").button();

	$(".datefield").datepicker();

}

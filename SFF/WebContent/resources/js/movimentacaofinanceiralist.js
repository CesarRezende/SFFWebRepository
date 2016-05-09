// Remove the formatting to get integer data for summation
        var intVal = function ( i ) {
            return typeof i === 'string' ?
                i.replace(/[R\$.]/g, '').replace(/[,]/g, '.')*1 :
                typeof i === 'number' ?
                    i : 0;
        };
        
jQuery.fn.dataTableExt.oSort['mydate-asc'] = function(x,y) {
    var retVal;
    x = $.trim(x);
    y = $.trim(y);
 
    if(x.length == 10){
	    var datex = new Date(intVal(x.substr(6, 4)) , intVal(x.substr(3, 2)), intVal(x.substr(0, 2)), 0, 0, 0, 0);
	    var datey = new Date(intVal(y.substr(6, 4)) , intVal(y.substr(3, 2)), intVal(y.substr(0, 2)), 0, 0, 0, 0);
    }
    
    if (x==y) retVal= 0;
    else if (x == "" || x == "&nbsp;") retVal=  1;
    else if (y == "" || y == "&nbsp;") retVal=  -1;
    else if ((x.length != 10 && x > y) || (x.length == 10 && datex > datey)) retVal=  1;
    else retVal = -1;  // <- this was missing in version 1
 
    return retVal;
};
jQuery.fn.dataTableExt.oSort['mydate-desc'] = function(y,x) {
    var retVal;
    x = $.trim(x);
    y = $.trim(y);
    
    if(x.length == 10){
	    var datex = new Date(intVal(x.substr(6, 4)) , intVal(x.substr(3, 2)), intVal(x.substr(0, 2)), 0, 0, 0, 0);
	    var datey = new Date(intVal(y.substr(6, 4)) , intVal(y.substr(3, 2)), intVal(y.substr(0, 2)), 0, 0, 0, 0);
    }
 
    if (x==y) retVal= 0; 
    else if (x == "" || x == "&nbsp;") retVal=  -1;
    else if (y == "" || y == "&nbsp;") retVal=  1;
    else if ((x.length != 10 && x > y) || (x.length == 10 && datex > datey)) retVal=  1;
    else retVal = -1; // <- this was missing in version 1
 
    return retVal;
 };


function DefaultCurrencyToRealFormat(number) {
		var p = number.toFixed(2).split(".");
		return p[0].split("").reverse().reduce(function (acc, number, i, orig) {
			return number + (i && !(i % 3) ? "." : "") + acc;
		}, "") + "," + p[1];

		formatNumber = formatNumber.replace("-.", "-");
		
		if(formatNumber.charAt(0) == '.')
			formatNumber = formatNumber.substring(1);
		
		return formatNumber;
	}

$(document).ready(function() {
	balance = 0;

var ths = $('#mainform\\:movimentacaoFinanceiraGrid thead tr th');
	
	var trs = $('#mainform\\:movimentacaoFinanceiraGrid tbody tr');
	var tds = $('#mainform\\:movimentacaoFinanceiraGrid tbody tr td');
	
	for(var i = 0; i < ths.length - tds.length; i++){
		
		trs.append("<td></td>");  
	}
	
	$('#mainform\\:movimentacaoFinanceiraGrid').dataTable({
		"language": {
            "decimal": ","
            ,"thousands": "."
			,"emptyTable": "Nenhum registro dispon&iacute;vel nesta tabela"
			,"info": "Pagina _PAGE_ de _PAGES_"
			,"infoEmpty": "Nenhum registro encontrado"
			,"infoFiltered": " - filtrado de _MAX_ registros"
			,"loadingRecords": "Por favor aguarde - carregando..."
			,"processing": "Processando"
			,"search": "Buscar:"
			,"zeroRecords": "Nenhum registro encontrado"
			,"lengthMenu": "Mostrar _MENU_ registros"
			,"paginate": {
				"first":      "Primeiro",
				"last":       "&Uacute;ltimo",
				"next":       "Pr&oacute;ximo",
				"previous":   "Anterior"
			}
        }
	,"aoColumnDefs": [{ 'bSortable': false, 'aTargets': [ 0,1,2,3,4,5,6,7,8,9 ] } ]
	,"columns": [
	             { "width": "5%" },
	             { "width": "35%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "5%" },
	             { "width": "5%" },
	             { "width": "10%" },
	             { "width": "5%" },
	             { "width": "5%" }
	           ]
	,"aoColumns": [
	              null
	              ,null
	              ,{ "sType" : "mydate" }
	              ,{ "sType" : "mydate" }
	              ,null
	              ,null
	              ,null
	              ,null
	          ]
	,"footerCallback": function ( row, data, start, end, display ) {
        var api = this.api(), data, type;

       
		
        // Total over this page
        data = api.column( 4, { page: 'current'} ).data();
        type = api.column( 5, { page: 'current'} ).data();
        var pageTotal = 0 ;
        var monthTotal = 0;
			
        
        if($("#mainform\\:txtBalanceType").val()  == "D")
        	pageTotal -= intVal($("#mainform\\:txtBalance").val());
        else
        	pageTotal += intVal($("#mainform\\:txtBalance").val());
        
		for(i = 0; i < data.length; i++ ){
			if(type[i] == "C"){
				pageTotal +=  (intVal(data[i]));
				monthTotal+=  (intVal(data[i]));
			}
			else{
				pageTotal -=  (intVal(data[i]));
				monthTotal-=  (intVal(data[i]));
			}
			
		}
			

		for(i = 0; i < api.columns()[0].length; i++ ){
			$( api.column( i ).footer() ).empty();
		}
       
	   if(pageTotal >= 0 ){
		   $(row).removeClass("debitrow").addClass("creditrow");
	   }else{
		   $(row).removeClass("creditrow").addClass("debitrow");
		   pageTotal *= -1;
	   }
		   
	   
	   if(monthTotal >= 0 ){
		   $( $(row).find("td").get(4)).removeClass("debitrow").addClass("creditrow");
	   }else{
		   $( $(row).find("td").get(4)).removeClass("creditrow").addClass("debitrow");
		   monthTotal *= -1;
	   }

		// Update footer
        $( api.column( 7 ).footer() ).html(
            DefaultCurrencyToRealFormat(pageTotal)
        );
        
        $( api.column( 4 ).footer() ).html(
                DefaultCurrencyToRealFormat(monthTotal)
            );
		
		
		
		
    }
	,"rowCallback":function(row, data){
		var intVal = function ( i ) {
            return typeof i === 'string' ?
                i.replace(/[R\$.]/g, '').replace(/[,]/g, '.')*1 :
                typeof i === 'number' ?
                    i : 0;
        };
		
        
		if(data[5] == "C"){
			$(row).addClass("creditrow");
			balance += (intVal(data[4]));
		}else{
			$(row).addClass("debitrow");
			balance -= (intVal(data[4]));
		}
	
		
		
		if(balance >= 0)
			$(row).find("td").get(7).innerHTML = DefaultCurrencyToRealFormat(balance);
		else
			$(row).find("td").get(7).innerHTML = DefaultCurrencyToRealFormat(balance * -1);
		
		if(balance >= 0){
			$($(row).find("td").get(7)).removeClass("debitrow").addClass("creditrow");
		}else{
			$($(row).find("td").get(7)).removeClass("creditrow").addClass("debitrow");
		}
	}
	,"preDrawCallback": function( settings ) {
        var api = this.api();
 
        balance = 0;
        if($("#mainform\\:txtBalanceType").val()  == "D")
        	balance -= intVal($("#mainform\\:txtBalance").val());
        else
        	balance += intVal($("#mainform\\:txtBalance").val());
    }
	,"order": [[ 2, "asc" ],[ 3, "asc" ],[ 0,"asc" ]]
	,paging: false
	
	
	   
});


//Cria Campos de Filtragem para Cada Coluna

    // Setup - add a text input to each footer cell
    $('#mainform\\:movimentacaoFinanceiraGrid thead th').each( function () {
        var title = $('#mainform\\:movimentacaoFinanceiraGrid thead th').eq( $(this).index() ).text();
        if(title != "Editar" && title != "Apagar" && title != "Saldo" )
        	$(this).html( '<span class=\"table-title\" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;">'+$(this).html()+'</span>' + '<br/><input type="text" placeholder="'+title+'" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;" />' );
        
    } );
 
    // DataTable
    var table = $('#mainform\\:movimentacaoFinanceiraGrid').DataTable();

    table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', table.column( colIdx ).header() ).on( 'keyup change', function () {
        	
            table
                .column( colIdx )
                .search( this.value )
                .draw();
            	defineMenuSize();
        } );
    } );



    // DataTable
    //var table = $('#mainform\\:movimentacaoFinanceiraGrid').DataTable();
 
    // Apply the order
//    table.columns().eq( 0 ).each( function ( colIdx ) {
//    	 var title = $('#mainform\\:movimentacaoFinanceiraGrid thead th').eq( colIdx ).text();
//         if(title != "Editar" && title != "Apagar")
//	        $( 'span', table.column( colIdx ).header() ).on( 'click', function () {
//				var lastOrdered = table.order();
//				
//				if(!lastOrdered){
//					table
//					.column( colIdx )
//						.order( 'asc' )
//						.draw();
//				}
//				else if(eval(lastOrdered[0][0]) == colIdx){
//					if(lastOrdered[0][1] == "asc"){
//						table
//						.column( colIdx )
//							.order( 'desc' )
//							.draw();
//					}else{
//						table
//						.column( colIdx )
//							.order( 'asc' )
//							.draw();
//					}
//					
//				
//				}else{
//					table
//					.column( colIdx )
//						.order( 'asc' )
//						.draw();
//				}
//				
//	        } );
//        
//    } );



   

	

	$('#mainform\\:insertButton').click(function (){ 
		window.parent.$.showModalDialog({ url: "movimentacaofinanceiramanager.xhtml", "height": 360, "width": 900, resizable: false, scrollable: false,  objAjax: "movimentacaofinanceira|mainform\\:movimentacaoFinanceiraGrid", callback: loadJQueryMovimentacaoFinanceira });
	});
	
	loadJQueryMovimentacaoFinanceira();
	
	$('#mainform\\:txtYearMonth').datepicker( {
	    changeMonth: true,
	    changeYear: true,
	    showButtonPanel: true,
	    dateFormat: 'mm/yy',
	    onClose: function(dateText, inst) { 
	        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
	        $(this).datepicker('setDate', new Date(year, month, 1));
	        
	        $("#mainform\\:btnYearMonth").click();
	    }
	});
	
	 //Oculta Componente de Busca Padão do TabaTable
    $('.dataTables_filter').css("display", "none");
	
	
});




function loadJQueryMovimentacaoFinanceira(){
	
	 $("#mainform\\:txtBalance").prop("disabled", true);
	 $("#mainform\\:txtBalance").css("background-color", "#FFFFFF");
	 $("#mainform\\:txtBalance").css("border", "1px solid black");
	 
	 if($("#mainform\\:txtBalanceType").val()  == "D")
		 $("#mainform\\:txtBalance").removeClass("creditrow").addClass("debitrow");
	 else
		 $("#mainform\\:txtBalance").removeClass("debitrow").addClass("creditrow");
	 defineMenuSize();
		 
}

function editMovimentacaoFinanceira(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		
		window.parent.$.showModalDialog({ url: "movimentacaofinanceiramanager.xhtml?id="+data.source.nextSibling.defaultValue, "height": 360, "width": 900, resizable: false, scrollable: false,  objAjax: "movimentacaofinanceira|mainform\\:movimentacaoFinanceiraGrid", callback: loadJQueryMovimentacaoFinanceira });

	}
} 
// Remove the formatting to get integer data for summation
var intVal = function ( i ) {
    return typeof i === 'string' ?
        i.replace(/[R\$.]/g, '').replace(/[,]/g, '.')*1 :
        typeof i === 'number' ?
            i : 0;
};

jQuery.fn.dataTableExt.oSort['situation-asc'] = function(x,y) {
    x = $.trim(x);
    y = $.trim(y);
    
    var xValue = 0;
    var yValue = 0;
    
    switch (x) {
	case "(A) Aberto":
		xValue = 1;
		break;
	case "(A) Pago Parcial":
		xValue = 2;
		break;
	case "(F) Pago":
		xValue = 3;
		break;
	default:
		break;
	}
    
    switch (y) {
	case "(A) Aberto":
		yValue = 1;
		break;
	case "(A) Pago Parcial":
		yValue = 2;
		break;
	case "(F) Pago":
		yValue = 3;
		break;
	default:
		break;
	}
   
    return xValue - yValue;
};
jQuery.fn.dataTableExt.oSort['situation-desc'] = function(y,x) {
	 x = $.trim(x);
	    y = $.trim(y);
	    
	    var xValue = 0;
	    var yValue = 0;
	    
	    switch (x) {
		case "(A) Aberto":
			xValue = 1;
			break;
		case "(A) Pago Parcial":
			xValue = 2;
			break;
		case "(F) Pago":
			xValue = 3;
			break;
		default:
			break;
		}
	    
	    switch (y) {
		case "(A) Aberto":
			yValue = 1;
			break;
		case "(A) Pago Parcial":
			yValue = 2;
			break;
		case "(F) Pago":
			yValue = 3;
			break;
		default:
			break;
		}
	   
	    return yValue - xValue ;
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
	
var ths = $('#mainform\\:saidasVariaveisGrid thead tr th');
	
	var trs = $('#mainform\\:saidasVariaveisGrid tbody tr');
	var tds = $('#mainform\\:saidasVariaveisGrid tbody tr td');
	
	for(var i = 0; i < ths.length - tds.length; i++){
		
		trs.append("<td></td>");  
	}
	

	$('#mainform\\:saidasVariaveisGrid').dataTable({
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
	,"aoColumnDefs": [{ 'bSortable': false, 'aTargets': [ 0,1,2,3,4,5,6,7,8,9,10,11,12 ] } ]
	,"columns": [
	             { "width": "5%" },
	             { "width": "20%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "5%" },
	             { "width": "5%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "2%" },
	             { "width": "3%" }
	           ]
	,"order": [[ 10, "asc" ]]
	,"aoColumns": [
		              null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,null
		              ,{ "sType" : "situation" }
		              ,null
		              ,null
		          ]
	,"footerCallback": function ( row, data, start, end, display ) {
        var api = this.api(), valueCollumn, totalValueCollumn, totalLeftCollumn;

       
		
        // Total over this page
        valueCollumn = api.column( 7, { page: 'current'} ).data();
        totalValueCollumn = api.column( 8, { page: 'current'} ).data();
        totalLeftCollumn = api.column( 9, { page: 'current'} ).data();
        var pageTotalValue = 0 ;
        var pageTotalTotalValue = 0;
        var pageTotalLeftValue = 0;
			
        
        
		for(i = 0; i < valueCollumn.length; i++ ){
			pageTotalValue +=  (intVal(valueCollumn[i]));
			pageTotalTotalValue +=  (intVal(totalValueCollumn[i]));
			pageTotalLeftValue +=  (intVal(totalLeftCollumn[i]));
		}
			

		for(i = 0; i < api.columns()[0].length; i++ ){
			$( api.column( i ).footer() ).empty();
		}

		   
		$( $(row).find("td").get(7)).addClass("debitrow");
		$( $(row).find("td").get(8)).addClass("debitrow");
		$( $(row).find("td").get(9)).addClass("debitrow");

		// Update footer
        $( api.column( 7 ).footer() ).html(
            DefaultCurrencyToRealFormat(pageTotalValue)
        );
        
        $( api.column( 8 ).footer() ).html(
                DefaultCurrencyToRealFormat(pageTotalTotalValue)
        );

        $( api.column( 9 ).footer() ).html(
                DefaultCurrencyToRealFormat(pageTotalLeftValue)
        );
    }
	,"rowCallback":function(row, data){

		$(row).addClass("debitrow");
	
	}
	   
});


//Cria Campos de Filtragem para Cada Coluna

    // Setup - add a text input to each footer cell
    $('#mainform\\:saidasVariaveisGrid thead th').each( function () {
        var title = $('#mainform\\:saidasVariaveisGrid thead th').eq( $(this).index() ).text();
        if(title != "Editar" && title != "Apagar" && title != "")
        	$(this).html( '<span class=\"table-title\" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;">'+$(this).html()+'</span>' + '<br/><input type="text" placeholder="'+title+'" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;" />' );
        
    } );
 
    // DataTable
    var table = $('#mainform\\:saidasVariaveisGrid').DataTable();

    table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', table.column( colIdx ).header() ).on( 'keyup change', function () {
        	
            table
                .column( colIdx )
                .search( this.value )
                .draw();
            
        } );
    } );



 

    
    
    $('#mainform\\:btnWebService').click(function (){ 
    	var soapMessage = createSoapMsg("printMessage", {name: "Cesar Rezende"});
    		
    	$.ajax({

    		url: "webservices?wsdl",
    		// the data to send (will be converted to a query string)
    		data: soapMessage,
    		// whether this is a POST or GET request
    		type: "POST",
    		// the type of data we expect back
    		dataType : "xml",
    		// code to run if the request succeeds;
    		// the response is passed to the function
    		contentType: "text/xml; charset=\"utf-8\"",
    		success: function( result ) {
    			alert($(result).find("return").get(0).innerHTML);
    		},
    		// code to run if the request fails; the raw request and
    		// status codes are passed to the function
    		error: function( xhr, status, errorThrown ) {
    		alert( "Desculpe, houve um problema!" );
    		console.log( "Error: " + errorThrown );
    		console.log( "Status: " + status );
    		console.dir( xhr );
    		},
    		// code to run regardless of success or failure
    		complete: function( xhr, status ) {
    		
    		}
    		});
    	
	});
    
    

//Oculta Componente de Busca Padão do TabaTable
    $('.dataTables_filter').css("display", "none");

	

	$('#mainform\\:insertButton').click(function (){ 
		window.parent.$.showModalDialog({ url: "saidavariavelmanager.xhtml", "height": 457, "width": 600, resizable: false, scrollable: false,  objAjax: "saidavariavel|mainform\\:saidasVariaveisGrid", callback: loadJQuerySaidaVariavel });
	});
	
	loadJQuerySaidaVariavel();
});


function loadJQuerySaidaVariavel(){

}

function editSaidaVariavel(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		
		window.parent.$.showModalDialog({ url: "saidavariavelmanager.xhtml?id="+data.source.nextSibling.defaultValue, "height": 457, "width": 600, resizable: false, scrollable: false,  objAjax: "saidavariavel|mainform\\:saidasVariaveisGrid", callback: loadJQuerySaidaVariavel });

	}
} 



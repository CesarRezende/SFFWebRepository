// Remove the formatting to get integer data for summation
var intVal = function ( i ) {
    return typeof i === 'string' ?
        i.replace(/[R\$.]/g, '').replace(/[,]/g, '.')*1 :
        typeof i === 'number' ?
            i : 0;
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
	
var ths = $('#mainform\\:saidasFixasGrid thead tr th');
	
	var trs = $('#mainform\\:saidasFixasGrid tbody tr');
	var tds = $('#mainform\\:saidasFixasGrid tbody tr td');
	
	for(var i = 0; i < ths.length - tds.length; i++){
		
		trs.append("<td></td>");  
	}
	

	$('#mainform\\:saidasFixasGrid').dataTable({
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
	             { "width": "25%" },
	             { "width": "20%" },
	             { "width": "10%" },
	             { "width": "5%" },
	             { "width": "10%" },
	             { "width": "10%" },
	             { "width": "5%" },
	             { "width": "5%" },
	             { "width": "5%" }
	           ]
	,"order": [[ 7, "desc" ],[ 1, "asc" ]]  
	,"footerCallback": function ( row, data, start, end, display ) {
        var api = this.api(), data;

       
		
        // Total over this page
        data = api.column( 6, { page: 'current'} ).data();
        var pageTotal = 0 ;
			
        
        	pageTotal += intVal($("#mainform\\:txtBalance").val());
        
		for(i = 0; i < data.length; i++ ){
			pageTotal +=  (intVal(data[i]));
		}
			

		for(i = 0; i < api.columns()[0].length; i++ ){
			$( api.column( i ).footer() ).empty();
		}

		   
		$( $(row).find("td").get(6)).addClass("debitrow");

		// Update footer
        $( api.column( 6 ).footer() ).html(
            DefaultCurrencyToRealFormat(pageTotal)
        );

    }
	,paging: false
	,"rowCallback":function(row, data){

		$(row).addClass("debitrow");
	
	}
	   
});


//Cria Campos de Filtragem para Cada Coluna

    // Setup - add a text input to each footer cell
    $('#mainform\\:saidasFixasGrid thead th').each( function () {
        var title = $('#mainform\\:saidasFixasGrid thead th').eq( $(this).index() ).text();
        if(title != "Editar" && title != "Apagar" && title != "" )
        	$(this).html( '<span class=\"table-title\" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;">'+$(this).html()+'</span>' + '<br/><input type="text" placeholder="'+title+'" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;" />' );
        
    } );
 
    // DataTable
    var table = $('#mainform\\:saidasFixasGrid').DataTable();

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
    var table = $('#mainform\\:saidasFixasGrid').DataTable();
 
    // Apply the order
    table.columns().eq( 0 ).each( function ( colIdx ) {
    	 var title = $('#mainform\\:saidasFixasGrid thead th').eq( colIdx ).text();
         if(title != "Editar" && title != "Apagar" && title != "" )
	        $( 'span', table.column( colIdx ).header() ).on( 'click', function () {
				var lastOrdered = table.order();
				
				if(!lastOrdered){
					table
					.column( colIdx )
						.order( 'asc' )
						.draw();
				}
				else if(eval(lastOrdered[0][0]) == colIdx){
					if(lastOrdered[0][1] == "asc"){
						table
						.column( colIdx )
							.order( 'desc' )
							.draw();
					}else{
						table
						.column( colIdx )
							.order( 'asc' )
							.draw();
					}
					
				
				}else{
					table
					.column( colIdx )
						.order( 'asc' )
						.draw();
				}
				
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
		window.parent.$.showModalDialog({ url: "saidafixamanager.xhtml", "height": 457, "width": 600, resizable: false, scrollable: false,  objAjax: "saidafixa|mainform\\:saidasFixasGrid", callback: loadJQuerySaidaFixa });
	});
	
	loadJQuerySaidaFixa();
});


function loadJQuerySaidaFixa(){
	defineMenuSize();
}

function editSaidaFixa(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		
		window.parent.$.showModalDialog({ url: "saidafixamanager.xhtml?id="+data.source.nextSibling.defaultValue, "height": 457, "width": 600, resizable: false, scrollable: false,  objAjax: "saidafixa|mainform\\:saidasFixasGrid", callback: loadJQuerySaidaFixa });

	}
} 



$(document).ready(function() {
	
var ths = $('#mainform\\:familiasGrid thead tr th');
	
	var trs = $('#mainform\\:familiasGrid tbody tr');
	var tds = $('#mainform\\:familiasGrid tbody tr td');
	
	for(var i = 0; i < ths.length - tds.length; i++){
		
		trs.append("<td></td>");  
	}
	

	$('#mainform\\:familiasGrid').dataTable({
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
	,"aoColumnDefs": [{ 'bSortable': false, 'aTargets': [ 0,1,2,3 ] } ]
	,"columns": [
	             { "width": "22%" },
	             { "width": "68%" },
	             { "width": "10%" },
	             { "width": "10%" }
	           ]
	,"order": [[ 1, "asc" ]]  
	
	   
});


//Cria Campos de Filtragem para Cada Coluna

    // Setup - add a text input to each footer cell
    $('#mainform\\:familiasGrid thead th').each( function () {
        var title = $('#mainform\\:familiasGrid thead th').eq( $(this).index() ).text();
        if(title != "Editar" && title != "Apagar" && title != "")
        	$(this).html( '<span class=\"table-title\" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;">'+$(this).html()+'</span>' + '<br/><input type="text" placeholder="'+title+'" style="width:'+Math.round(this.style.width.replace("px","") * 0.9)+'px;" />' );
        
    } );
 
    // DataTable
    var table = $('#mainform\\:familiasGrid').DataTable();

    table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', table.column( colIdx ).header() ).on( 'keyup change', function () {
        	
            table
                .column( colIdx )
                .search( this.value )
                .draw();
            
        } );
    } );



    // DataTable
    var table = $('#mainform\\:familiasGrid').DataTable();
 
    // Apply the order
    table.columns().eq( 0 ).each( function ( colIdx ) {
    	 var title = $('#mainform\\:familiasGrid thead th').eq( colIdx ).text();
         if(title != "Editar" && title != "Apagar" && title != "")
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
		window.parent.$.showModalDialog({ url: "familiamanager.xhtml", "height": 220, "width": 400, resizable: false, scrollable: false,  objAjax: "familia|mainform\\:familiasGrid", callback: loadJQueryFamilia });
	});
	
	loadJQueryFamilia();
});


function loadJQueryFamilia(){

}

function editFamilia(data) {
	if (data.status === 'begin') {

	}
	if (data.status === 'complete') {

	}
	if (data.status === 'success') {
		
		window.parent.$.showModalDialog({ url: "familiamanager.xhtml?id="+data.source.nextSibling.defaultValue, "height": 220, "width": 400, resizable: false, scrollable: false,  objAjax: "familia|mainform\\:familiasGrid", callback: loadJQueryFamilia });

	}
} 



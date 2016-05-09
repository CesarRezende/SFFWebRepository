(function ($) {
    $.fn.showModalDialog = function (options) {

        var optns = $.extend({}, $.fn.showModalDialog.defaults, options);

        var name = GetFrameName(optns.url);

        var $frame = $('<iframe id="' + name + '" />');
        $frame.attr({
            'src': optns.url,
            'scrolling': optns.scrolling
        });

        $frame.css({
            'padding': 0,
            'margin': 0,
            'padding-bottom': 10
        });

        var $modalWindow = $frame.dialog({
            autoOpen: true,
            modal: true,
            width: optns.width,
            height: optns.height,
            resizable: optns.resizable,
            position: optns.position,
            closeOnEscape: false,
            autoFocus: false,
            hide: { effect: 'fade', duration: 200 },
            show: { effect: 'fade', duration: 200 },
            close: function () {

                optns.returnValue = $frame[0].contentWindow.window.returnValue;

                if (optns.objAjax != null) {

                    var frameAndObj = optns.objAjax.split("|");

                    $.post(window.parent.location,
                        function (data) {
                            frameName = '#' + frameAndObj[0];

                            for (var objs = 1; objs < frameAndObj.length; objs++) {

                                objName = '#' + frameAndObj[objs]; 

                                var content = $(data).find(objName);
                                
                                var table = $(objName).DataTable();
                                
                                var rows = table
                                .rows()
                                .remove()
                                .draw();
                                
                                $('tbody', content).find("tr").each(function(index, value){
                                	var array = [];
                                	
                                	$('td',value).each(function(index, value){
                                    	
                                    	
                                    	array[index] = value.innerHTML;

                                    });
                                	if(array.length > 0 ){
	                                	table
	                                    .row.add( array );
	                                    
                                	}
                                });

                                table.draw();
                            }

                             
                            var callbacks = $.Callbacks( "once" );
                            callbacks.add( optns.callback);
                            callbacks.fire(  );
                          
                        }
                    );
                }
                else {

                    window.parent.frames[0].location = window.parent.frames[0].location;
                }


                $(this).dialog('destroy').remove();


            },
            resizeStop: function () { $frame.css("width", "100%"); }
        });

        $frame.css("width", "100%");

        $frame[0].contentWindow.window.parent.dialogArguments = optns.dialogArguments;

        window.parent.dialogArguments = optns.dialogArguments;

        $frame[0].contentWindow.window.close = function () { $modalWindow.dialog('close'); };

        $frame.load(function () {
            if ($modalWindow) {

                var maxTitleLength = 100;
                var title = $(this).contents().find("title").html();
                title = title + " - ScreenId : " + name;

                if (title.length > maxTitleLength) {
                    title = title.substring(0, maxTitleLength) + '...';
                }

                $modalWindow.dialog('option', 'title', title);
            }
        });

        return null;
    };

    $.fn.showModalDialog.defaults = {
        url: null,
        dialogArguments: null,
        height: 'auto',
        width: 'auto',
        position: 'center',
        resizable: false,
        scrolling: 'no',
        closeOnEscape: false,
        onClose: function () { },
        returnValue: null,
        callback: function () { }
    };

})(jQuery);

jQuery.showModalDialog = function (options) { $().showModalDialog(options); };

function GetFrameName(url) {

    //Sincroniza o nome do dialog com o nome da classe
    var name = url.split("/");

    for (var index = 0; index < name.length; index++) {
        if (name[index].indexOf(".xhtml") != -1) {
            var trash = name[index].substring(name[index].indexOf("."), name[index].length);
            name = name[index].replace(trash, "");
            break;
        }
    }

    return name;

}

function RefreshFrame(frameObjs) {

    var frameAndObj = frameObjs.split("|");

    $.post(window.parent.frames[0].location,
                        function (data) {

                            frameName = '#' + frameAndObj[0];

                            for (var objs = 1; objs < frameAndObj.length; objs++) {

                                objName = '#' + frameAndObj[objs];

                                var content = $(data).find(objName);

                                var frame = $(frameName).get(0).contentDocument.body;

                                $('*', frame).find(objName).empty();

                                $('*', frame).find(objName).html(content);

                            }
                        }
                    );
}
ACC.colorbox = {
	config: {
		maxWidth:"100%",
		opacity:0.7,
		width:"auto",
		transition:"none",
		close: "",
        overlayClose: true,
		title:'<div class="headline"><span class="headline-text">{title}</span></div>',
		onComplete: function() {
			errorColorBox();
			$.colorbox.resize();
			ACC.common.refreshScreenReaderBuffer();
			
		},
		onClosed: function() {
			errorColorBoxClosed();
			ACC.common.refreshScreenReaderBuffer();
		}
	},

	open: function(title,config){
		var config = $.extend({},ACC.colorbox.config,config);
		config.title = config.title.replace(/{title}/g,title);
		return $.colorbox(config);
	},

	resize: function(){
		$.colorbox.resize();
	},

	close: function(){
		$.colorbox.close();
		$.colorbox.remove();
		$('#cboxClose').click(); // Then this will close the box, $.colorbox.close() still doesn't work
		$.colorbox.init(); // Re-init, otherwise colorbox stops working
		
	}
};
function errorColorBox() {
	var errorLength = $("#colorbox #cboxError").length;
	if(errorLength > 0) {
		$(".page-search #colorbox").removeAttr('style');
		$(".page-search #colorbox").css("margin-top","15%");
		
	}
}
function errorColorBoxClosed() {
	$(".page-search #colorbox").css("margin-top","");
}
ACC.colorbox = {
	config: {
		maxWidth:"100%",
		opacity:0.7,
		width:"auto",
		transition:"none",
		close:'',
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
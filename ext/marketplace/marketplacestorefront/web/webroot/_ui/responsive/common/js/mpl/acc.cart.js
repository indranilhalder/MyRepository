ACC.cart = {

	_autoload: [
		"bindHelp"
	],

	bindHelp: function(){
		$(document).on("click",".js-cart-help",function(e){
			e.preventDefault();
			var title = $(this).data("help");
			ACC.colorbox.open(title,{
				html:$(".js-help-popup-content").html(),
				width:"300px"
			});
		})
	}

};

$(function() {

	if ($("ul#giftYourselfProducts").length) {
		
		$.ajax({
			url: ACC.config.encodedContextPath + "/cart/giftlist",
			type: 'GET',
			success: function (data)
			{
				if (data != '') {
				    $("div#wishlistBanner").removeAttr("style");
				    $("ul#giftYourselfProducts").html(data);
				}
			}
		});
	}
});
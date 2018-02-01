var ww = $(window).width();
if(ww < 768){
	var checkout_page_name = $('#checkoutPageName').val();
	if( checkout_page_name == "Payment Options" ){
	 applyPromotion(null, "none", "none");
	}
}


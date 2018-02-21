var product = "";
var slatWidget = false;
var MSD = {

		renderFlatWidgetMSD: function() {
			if($('#for_homefurnishing').val() == "true"){
				var productCode = $('#pdp_product_code').val();
				console.log(productCode);
				msdService.call(productCode);
			}
		},

}

var msdService = {

		call: function(productCode){
			$.ajax({
				url: '/getMSDWidgets',
				dataType: "json",
				type: 'GET',
				crossDomain: true,
				data:{
					//"productCode": productCode.toUpperCase()
					"productCode": 'MP000000000113324'
				}, 
				success: function(data){
					$("#msd_widget_hf").html(msdRender.carousel(data));
					console.log(data);
					//

				}


			});
		}
}

$(document).ready(function(){
	MSD.renderFlatWidgetMSD()
});

var msdRender = {



		carousel: function(responseText) {
			try{
				var MSDJObject = JSON.parse(responseText.responsedata);
				if(MSDJObject !=null && (MSDJObject!="" && MSDJObject !=null ))
					// if(STWJObject.STWElements)
				{
					var msdWidgetProducts = "";
					msdWidgetProducts += '<div class="carousel-component">';
					msdWidgetProducts += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
					$.each(MSDJObject.data, function(index, values) {
						$.each(values, function(index, value) {
							var productId = value.product_id;
							var product = productId.toUpperCase();
							msdWidgetProducts += '<div class="item slide">';
							msdWidgetProducts += '<div class="product-tile"><li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile stw_widget_list_elements productParentList" style="display: inline-block;position: relative;"><div class="image"><a href="' + value.image_link + '" class="product-tile"> <img src="' + value.image_link + '"></a>';
							//msdWidgetProducts += '<div onclick=popupwindow("' + product + '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div></div>';
							//msdWidgetProducts += ' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: ' + value.availableColor + ';border: 1px solid rgb(204, 211, 217);" title="' + value.availableColor + '"></span></li></ul>';
							//msdWidgetProducts += '<div class="brand">' + value.productBrand + '</div>';
							msdWidgetProducts += ' <a href="/p/' + value.product_id + '" class="item"><h3 class="product-name">' + value.title + '</h3>';
//							if(savingPriceCal > 0){
//							msdWidgetProducts += '<div class="price"><span class="stw-mrp">&#8377;' + value.mrp + '</span><span class="stw-mop">&#8377;' + value.mop + '</span>';
//							msdWidgetProducts += '<p class="savings pdp-savings"> <span>(-' + savingsOnProduct + '%)</span></p>';
							//msdWidgetProducts += '</div></a></div></li></div></div>';
							msdWidgetProducts += '</div></a></div></li></div>';
							msdWidgetProducts += '<div class="gotopdp"><a href="'+value.productUrl+'">View</a></div></div>';
//							}
//							else{
							msdWidgetProducts += '<div class="price"><span>&#8377;' + value.price + '</span>';	
							msdWidgetProducts += '</div></a></div></li></div></div>';
//							}
						});
					});
					msdWidgetProducts += '</div></div>';
					return msdWidgetProducts;
				}
			}
			catch(e)
			{
				console.log("Error="+e);
			}
		},
		
		bindCarousel: function() {
	    	$(".stw-widget-owl").owlCarousel({
	            items: 5,
	            loop: true,
	            nav: true,
	            dots: false,
	            navText: [],
	            lazyLoad: true,
	            
	        });
	    }



}
ACC.quickview = {

	_autoload: [
		"bindToUiCarouselLink"
	],
		
		
	initQuickviewLightbox:function(){
		ACC.product.enableAddToCartButton();
		//ACC.product.bindToAddToCartForm();
		ACC.product.addToBag();
		ACC.product.enableStorePickupButton();
	},
		
	refreshScreenReaderBuffer: function ()
	{
		// changes a value in a hidden form field in order
		// to trigger a buffer update in a screen reader
		$('#accesibility_refreshScreenReaderBufferField').attr('value', new Date().getTime());
	},
	


	bindToUiCarouselLink: function ()
	{
		var titleHeader = $('#quickViewTitle').html();
		$(".js-reference-item,.comparison-table .product-tile").colorbox({
			close:'<span class="glyphicon glyphicon-remove"></span>',
			title: titleHeader,
			maxWidth:"100%",
			onComplete: function ()
			{
				quickviewGallery();
				ACC.quickview.refreshScreenReaderBuffer();
				ACC.quickview.initQuickviewLightbox();
				ACC.ratingstars.bindRatingStars($(".quick-view-stars"));
			},

			onClosed: function ()
			{
				ACC.quickview.refreshScreenReaderBuffer();
				if((window.top==window) && $("body").hasClass("page-cartPage")) {
				    // You're not in a frame, so you reload the site.
				    window.setTimeout('location.reload()'); 
			     }
			}
		});
	}
	
};



function quickviewGallery() {
	$(document).ready(function(){
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
		$(".imageList ul li img").css("height", thumbnailImageHeight);
	 	$("#previousImage").css("opacity","0.5");
	 	$("#nextImage").css("opacity","1");
	 	var listHeight = $(".imageList li").height();
	 	if($("#previousImage").length){
	 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
	 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
	 	}
	 	$(".imageListCarousel").show();
	 	
	 	if ('ontouchstart' in window) {
	 		$(".quick-view-popup #variantForm .select-size span.selected").next("ul").hide();
			  $(".quick-view-popup #variantForm .select-size span.selected").click(function(){
				  $(this).next("ul").toggleClass("select_height_toggle");
			  });
			}
	 });
	
	
}

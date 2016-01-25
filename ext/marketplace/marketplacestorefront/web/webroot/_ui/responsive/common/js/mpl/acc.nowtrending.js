ACC.nowtrending = {

	_autoload : [ "bindNowTrendingCategory" ],

	bindNowTrendingCategory : function() {
		var calculate_width=function(){
			var window_width=$('.owl-wrapper-outer').width();
			var item_width=0;
			if($(window).width() > 768) {
				item_width=window_width/5;
			} else if($(window).width() <= 768 && $(window).width() > 480) {
				item_width=window_width/3;
			}
			else{
				item_width=window_width/2;
			}
			$('.owl-item').css('width',item_width);
		};
		
		/*$(window).resize(function(){
			calculate_width();
		});*/
		
		$("#categories").change(function() {
			
							// alert(ACC.config.encodedContextPath+"/view/MplNowTrendingProductCarouselComponentController/nowtrending");
							var param = $("#categories").val();
							$.ajax({
										type : "GET",
										dataType : "json",
										url : ACC.config.encodedContextPath
												+ "/view/MplNowTrendingProductCarouselComponentController/nowtrending?categoryCode="
												+ param,

										success : function(response) {
											$("#defaultNowTrending .owl-wrapper").empty();
											$.each(response, function(k, v) {
																var renderHtml = "<div class='owl-item' ><div class='item slide'><a class='product-tile' href='"
																		+ ACC.config.encodedContextPath
																		+ v.productUrl
																		+ "'><div class='image'><img src='"
																		+ v.productImageUrl
																		+ "'/></div><div class='short-info'><h3 class='product-name'>"
																		+ v.productName
																		+ "</h3><div class='price'>"+v.productPrice+"</div></div></a><a href='"+ACC.config.encodedContextPath+v.productUrl+"/quickView' class='js-reference-item'>QuickView</a></div></div>";
																$("#defaultNowTrending .owl-wrapper").append(renderHtml);
															});
											ACC.quickview.bindToUiCarouselLink();
											calculate_width();
										},
										error : function() {
											alert('Error occured');
										}
									});
						});

	}
};


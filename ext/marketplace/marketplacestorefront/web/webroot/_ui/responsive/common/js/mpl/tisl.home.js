$("div.departmenthover").on(
		"mouseover touchend",
		function() {

			var id = this.id;
			var code = id.substring(4);

			if (!$.cookie("dept-list") && window.localStorage) {
				for ( var key in localStorage) {
					if (key.indexOf("deptmenuhtml") >= 0) {
						window.localStorage.removeItem(key);
						// console.log("Deleting.." + key);
					}
				}
			}

			if (window.localStorage
					&& (html = window.localStorage.getItem("deptmenuhtml-"
							+ code)) && html != "") {
				// console.log("Local");
				$("ul." + id).html(decodeURI(html));
			} else {
				$.ajax({
					url : ACC.config.encodedContextPath
							+ "/departmentCollection",
					type : 'GET',
					data : "&department=" + code,
					success : function(html) {
						// console.log("Server");
						$("ul." + id).html(html);
						if (window.localStorage) {
							$.cookie("dept-list", "true", {
								expires : 1,
								path : "/store"
							});
							window.localStorage.setItem("deptmenuhtml-" + code,
									encodeURI(html));
						}
					}
				});
			}

		});

$(".A-ZBrands")
		.on(
				"mouseover touchend",
				function(e) {
					if ($("li#atozbrandsdiplay").length) {
						// console.log("Dipslaying A-Z Brands..");

						if (!$.cookie("dept-list") && window.localStorage) {
							for ( var key in localStorage) {
								if (key.indexOf("atozbrandmenuhtml") >= 0) {
									window.localStorage.removeItem(key);
									// console.log("Deleting.." + key);
								}
							}
						}

						if (window.localStorage
								&& (html = window.localStorage
										.getItem("atozbrandmenuhtml"))
								&& html != "") {
							// console.log("Local");
							if ($("div#appendedAtoZBrands") == null
									|| $("div#appendedAtoZBrands").length == 0) {
								$("li#atozbrandsdiplay")
										.append(decodeURI(html));
							}
						} else {
							// console.log("Server");
							$
									.ajax({
										url : ACC.config.encodedContextPath
												+ "/atozbrands",
										type : 'GET',
										success : function(html) {
											console.log(html)
											if ($("div#appendedAtoZBrands") == null
													|| $("div#appendedAtoZBrands").length == 0) {
												$("li#atozbrandsdiplay")
														.append(html);
											}
											if (window.localStorage) {
												$.cookie("dept-list", "true", {
													expires : 1,
													path : "/store"
												});
												window.localStorage.setItem(
														"atozbrandmenuhtml",
														encodeURI(html));
											}
										}
									});
						}
					}
				});

$("span.helpmeshopbanner").on("click touchend", function() {

	$.ajax({
		url : ACC.config.encodedContextPath + "/helpmeshop",
		type : 'GET',
		success : function(html) {
			$("div#helpmeshopcontent").html(html);
		}
	});
});

$("a#tracklink").on("mouseover touchend", function(e) {
	e.stopPropagation();

	$.ajax({
		url : ACC.config.encodedContextPath + "/headerTrackOrder",
		type : 'GET',
		success : function(html) {
			$("ul.trackorder-dropdown").html(html);
		}
	});
});

$("a#myWishlistHeader").on("mouseover touchend", function(e) {
	e.stopPropagation();

	$.ajax({
		url : ACC.config.encodedContextPath + "/headerWishlist",
		type : 'GET',
		data : "&productCount=" + $(this).attr("data-count"),
		success : function(html) {
			$("div.wishlist-info").html(html);
		}
	});
});
//

var activePos = 0;
$(window).on("resize load",function(){
	activePos = ($(window).width() > 790)?3:1;
});

function getBrandsYouLoveAjaxCall() {
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
			.ajax({
				type : "GET",
				dataType : "json",
				url : ACC.config.encodedContextPath + "/getBrandsYouLove",
				data : dataString,
				
				success : function(response) {
					console.log(response.subComponents);
					defaultComponentId="";
					renderHtml = "<h1>" + response.title + "</h1>"
							+ "<div class='home-brands-you-love-carousel'>";
					$
							.each(
									response.subComponents,
									function(k, v) {
										console.log(v.brandLogoUrl);
										
										if (!v.showByDefault) {
											renderHtml += "<div class='home-brands-you-love-carousel-brands item' id='"
													+ v.compId
													+ "'><img src='"
													+ v.brandLogoUrl
													+ "'></img></div>";
										} else {
											renderHtml += "<div class='home-brands-you-love-carousel-brands item active' id='"
													+ v.compId
													+ "'><img src='"
													+ v.brandLogoUrl
													+ "'></img></div>";
											defaultComponentId= v.compId;
										}

									});
					renderHtml += "</div>";
					renderHtml +='<div class="bulprev"></div><div class="bulnext"></div>';
					$('#brandsYouLove').html(renderHtml);
					
					getBrandsYouLoveContentAjaxCall(defaultComponentId);
				},
				error : function() {
					// globalErrorPopup('Failure!!!');
					console.log("Error while getting brands you love");
				},
				complete : function() {
					$(".home-brands-you-love-carousel").owlCarousel({
						navigation:false,
						navigationText : [],
						pagination:false,
						itemsDesktop : [5000,7], 
						itemsDesktopSmall : [1400,7], 
						itemsTablet: [790,3], 
						itemsMobile : [480,3], 
						rewindNav: false,
						lazyLoad:false,
						mouseDrag: false,
						touchDrag: false
					});
					
					
					var index = $(".home-brands-you-love-carousel-brands.active").parents('.owl-item').index()
					
					
					
					if(index > activePos) {
						for(var i = 0; i < index - activePos;i++) {
							$(".home-brands-you-love-carousel .owl-wrapper").append($(".home-brands-you-love-carousel .owl-item").first());
						}
					} else if (index < activePos) {
						for(var i = 0; i < activePos - index;i++) {
							$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
						}
					}
					
					
				}
			});
}

// Get Brands You Love Content AJAX
function getBrandsYouLoveContentAjaxCall(id) {
	if (window.localStorage
			&& (html = window.localStorage.getItem("brandContent-" + id)) && html != "") {
		// console.log("Local");
		$(".home-brands-you-love-carousel").css("margin-bottom","20px");
		//$('#brandsYouLove').append(defaultHtml);
		$('.home-brands-you-love-desc').remove();
		$('#brandsYouLove').append(decodeURI(html));
	}
	else{
		
	$
			.ajax({
				type : "GET",
				dataType : "json",
				beforeSend: function(){
					$(".home-brands-you-love-carousel").css("margin-bottom","120px");
					$("#brandsYouLove").append("<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 200px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='/store/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>");
                },
				url : ACC.config.encodedContextPath
						+ "/getBrandsYouLoveContent",
				data : {
					"id" : id
				},
				success : function(response) {
					
					
					$('.home-brands-you-love-desc').remove();
					defaultHtml = "<div class='home-brands-you-love-desc'>";
					if (typeof response.text !== "undefined") {
						defaultHtml += response.text;
					}
					if (typeof response.firstProductImageUrl !== "undefined") {
						
						defaultHtml += "<div class='home-brands-you-love-side-image left'><a href='"+ACC.config.encodedContextPath+response.firstProductUrl+"'><img src='"
								+ response.firstProductImageUrl
								+ "'></img>";
						if (typeof response.firstProductTitle !== "undefined"){
							defaultHtml +="<p class='product-name'>"+response.firstProductTitle+"</p>";
						}
						if (typeof response.firstProductPrice !== "undefined"){
							defaultHtml +="<p class='price normal'><span class='priceFormat'>"+response.firstProductPrice+"</span></p>";
			                  
						}
						defaultHtml +="</a></div>"
					}
					defaultHtml += "<div class='home-brands-you-love-main-image'>";
					
					if (typeof response.bannerImageUrl !=="undefined") {
						defaultHtml += "<div class='home-brands-you-love-main-image-wrapper'>";
						if (typeof response.bannerText !=="undefined") {
							defaultHtml += "<div class='visit-store-wrapper'>"
									+ response.bannerText + "</div>";
						}
						
						defaultHtml += "<img src='" + response.bannerImageUrl
								+ "'></img></div></div>";

					}

					if (typeof response.secondproductImageUrl !== "undefined") {
						defaultHtml += "<div class='home-brands-you-love-side-image right'><a href='"+ACC.config.encodedContextPath+response.secondProductUrl+"'><img src='"
								+ response.secondproductImageUrl
								+ "'></img>";
						if (typeof response.secondProductTitle !== "undefined"){
							defaultHtml +="<p class='product-name'>"+response.secondProductTitle+"</p>";
						}
						if (typeof response.secondProductPrice !== "undefined"){
							defaultHtml +="<p class='normal price'><span class='priceFormat'>"+response.secondProductPrice+"</span></p>";
			                  
						}
						defaultHtml +="</a></div>"
					}

					defaultHtml += "</div>";
					
					$(".home-brands-you-love-carousel").css("margin-bottom","20px");
					
					$('#brandsYouLove').append(defaultHtml);
					
					window.localStorage.setItem("brandContent-" + id,
							encodeURI(defaultHtml));

				},
				complete: function(){
					$('#brandsYouLove .loaderDiv').remove();
				},
				error : function() {
					$('#brandsYouLove .loaderDiv').remove();
					$(".home-brands-you-love-carousel").css("margin-bottom","20px");
					console.log("Error while getting brands you love content");
				}
			});
	}
}
// ENd AJAX CALL
if ($('#brandsYouLove').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	if (window.localStorage) {
		for ( var key in localStorage) {
			if (key.indexOf("brandContent") >= 0) {
				window.localStorage.removeItem(key);
				console.log("Deleting.." + key);
			}
		}
	}
	getBrandsYouLoveAjaxCall();
	
}

var bulCount = $(".home-brands-you-love-carousel-brands.active").index() - 1;
/*$(document).on("click", ".home-brands-you-love-carousel-brands",
		function() {
			$(".home-brands-you-love-carousel-brands").removeClass('active');
			$(this).addClass('active');
			$('.home-brands-you-love-desc').remove();
			bulCount = $(this).parent().index();
			getBrandsYouLoveContentAjaxCall($(this).attr("id"));
		});*/





$(document).on("click", ".home-brands-you-love-carousel-brands",
		function() {
			$(".home-brands-you-love-carousel-brands").removeClass('active');
			$(this).addClass('active');
			$('.home-brands-you-love-desc').remove();
			var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
			var index = $(this).parents('.owl-item').index();
			if(index > activePos) {
				for(var i = 0; i < index - activePos;i++) {
					$("#brandsYouLove .owl-wrapper").css('transition','all 0.2s linear');
					$("#brandsYouLove .owl-wrapper").css('transform','translate3d(-'+iw+'px, 0px, 0px)');
					setTimeout(function(){
					$(".home-brands-you-love-carousel .owl-wrapper").append($(".home-brands-you-love-carousel .owl-item").first());
					$("#brandsYouLove .owl-wrapper").css('transition','all 0s linear');
					$("#brandsYouLove .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
					},200);
				}
			} else if (index < activePos) {
				for(var i = 0; i < activePos - index;i++) {
					$("#brandsYouLove .owl-wrapper").css('transition','all 0.2s linear');
					$("#brandsYouLove .owl-wrapper").css('transform','translate3d('+iw+'px, 0px, 0px)');
					setTimeout(function(){
					$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
					$("#brandsYouLove .owl-wrapper").css('transition','all 0s linear');
					$("#brandsYouLove .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
					},200);
				}
			}
			var index = $(this).parents('.owl-item').index();
			getBrandsYouLoveContentAjaxCall($(this).attr("id"));
		});

	$(document).on("click",".bulprev",function(){
		/*$('.home-brands-you-love-desc').remove();
		$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").removeClass('active');
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").eq(activePos).addClass('active');
		getBrandsYouLoveContentAjaxCall($(".home-brands-you-love-carousel-brands.active").attr('id'));*/
	
		var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
		$('.home-brands-you-love-desc').remove();
		$("#brandsYouLove .owl-wrapper").css('transition','all 0.3s ease');
		$("#brandsYouLove .owl-wrapper").css('transform','translate3d('+iw+'px, 0px, 0px)');
		setTimeout(function(){
		$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").removeClass('active');
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").eq(activePos).addClass('active');
		getBrandsYouLoveContentAjaxCall($(".home-brands-you-love-carousel-brands.active").attr('id'));
		$("#brandsYouLove .owl-wrapper").css('transition','all 0s ease');
		$("#brandsYouLove .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
		},300);
	
	}); 
	
	$(document).on("click",".bulnext",function(){
		
		var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
		$('.home-brands-you-love-desc').remove();
		$("#brandsYouLove .owl-wrapper").css('transition','all 0.3s ease');
		$("#brandsYouLove .owl-wrapper").css('transform','translate3d(-'+iw+'px, 0px, 0px)');
		setTimeout(function(){
		$(".home-brands-you-love-carousel .owl-wrapper").append($(".home-brands-you-love-carousel .owl-item").first());
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").removeClass('active');
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").eq(activePos).addClass('active');
		getBrandsYouLoveContentAjaxCall($(".home-brands-you-love-carousel-brands.active").attr('id'));
		$("#brandsYouLove .owl-wrapper").css('transition','all 0s ease');
		$("#brandsYouLove .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
		},300);
		
		
	});


if($('#ia_site_page_id').val()=='homepage'){
	

/*setInterval(function() {

$('.bulnext').click();

}, 20000);
*/}



// AJAX CALL BEST PICKS START
if ($('#bestPicks').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	getBestPicksAjaxCall();
}


function getBestPicksAjaxCall(){
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getBestPicks",
		data : dataString,
		
		success : function(response) {
			renderHtml = "<h1>" + response.title + "</h1>"
				+ "<div class='home-best-pick-carousel'>";
			
			$
				.each(
						response.subItems,function(k, v){
							
							if(v.url){
								renderHtml += "<a href='"
									+ v.url
									+ "' class='item'>";
							}
							
							if(v.imageUrl){
								renderHtml += "<div class='home-best-pick-carousel-img'> <img src='"
									+ v.imageUrl
									+ "'></img></div>";
							}
							
							if(v.text){
								renderHtml += "<div class='short-info'>"
									+ v.text
									+ "</div>";
							}
							
							renderHtml += "</a>";
							
						
				});
			renderHtml += "</div> <a href='/store/o/all' class='view-cliq-offers'> View Cliq Offers </a>";	
			$("#bestPicks").html(renderHtml);
			// console.log()
		},
		
		error : function() {
			console.log("Error while getting best picks");
		},
		
		complete: function() {
			$(".home-best-pick-carousel").owlCarousel({
				navigation:true,
				navigationText : [],
				pagination:false,
				itemsDesktop : [5000,5], 
				itemsDesktopSmall : [1400,5], 
				itemsTablet: [650,1], 
				itemsMobile : [480,1], 
				rewindNav: false,
				lazyLoad:true,
				scrollPerPage:true
			});
		}

	});
}

// AJAX CALL BEST PICKS END

//AJAX CALL PRODUCTS YOU CARE START
if ($('#productYouCare').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	getProductsYouCareAjaxCall();
}

function getProductsYouCareAjaxCall(){
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getProductsYouCare",
		data : dataString,
		
		success : function(response) {
			
			renderHtml = "<h1>" + response.title + "</h1>";
			renderHtml += "<div class='home-product-you-care-carousel'>";
			$
				.each(
						response.categories,function(k, v){
							
						console.log('Category name: '+v.categoryName);
						console.log('Category code: '+v.categoryCode);
						console.log('Category media url: '+v.mediaURL);
						
						var URL = ACC.config.encodedContextPath+"/Categories/"+v.categoryName+"/c/"+v.categoryCode;
						//for url
						renderHtml += "<a href='"
							+ URL
							+ "' class='item'>";
						//for image
						renderHtml += "<div class='home-product-you-care-carousel-img'> <img src='"
							+ v.mediaURL
							+ "'></img></div>";
						
						renderHtml += "<div class='short-info'><h3 class='product-name'><span>"
							+ v.categoryName
							+ "</span></h3></div>";
						
						renderHtml += "</a>";
						
				});
		
			renderHtml += "</div>";	
			$("#productYouCare").html(renderHtml);
		},
		
		error : function() {
			console.log('Error while getting getProductsYouCare');
		},
		
		complete: function() {
			$(".home-product-you-care-carousel").owlCarousel({
				navigation:true,
				navigationText : [],
				pagination:false,
				itemsDesktop : [5000,4], 
				itemsDesktopSmall : [1400,4], 
				itemsTablet: [650,2], 
				itemsMobile : [480,2], 
				rewindNav: false,
				lazyLoad:true,
				scrollPerPage:true
			});
		}

	});
}

//AJAX CALL PRODUCTS YOU CARE END

function getNewAndExclusiveAjaxCall(){
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getNewAndExclusive",
		data : dataString,
		
		success : function(response) {
			
		
			console.log(response.newAndExclusiveProducts);
			var defaultHtml = "";
			renderHtml = "<h1>" + response.title + "</h1>"
					+ "<div class='carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference' id='new_exclusive'>";
			$.each(
							response.newAndExclusiveProducts,
							function(key, value) {
								
									renderHtml += "<div class='item slide'><div class='newExclusiveElement'><a href='"+ACC.config.encodedContextPath+value.productUrl+"'><img src='"
											+ value.productImageUrl
											+ "'></img><p class='New_Exclusive_title'>" + value.productTitle + "</p><p class='New_Exclusive_price'><span class='priceFormat'>" + value.productPrice + "</span></p></a></div></div>"; 
											

							});
		renderHtml += "</div><a href='"+ACC.config.encodedContextPath+"/search/viewOnlineProducts' class='new_exclusive_viewAll'>View All</a>";
			$('#newAndExclusive').html(renderHtml);
			

		
		

		},
		error : function() {
			console.log("Error while getting new and exclusive");
		},
		
		complete: function() {
			$("#new_exclusive").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false,
			scrollPerPage:true
		});

			setTimeout(function(){
				/*if($(window).width() > 773) {
					$('#newAndExclusive').css('min-height',$('#newAndExclusive').parent().height()+'px');
				}*/
				//alert($('#newAndExclusive').height() +"|||"+$('#stayQued').height())
				if ($(window).width() > 773) {
					if ($('#newAndExclusive').height() > $('#stayQued').height()) {
						$('#stayQued').css('min-height',
								$('#newAndExclusive').outerHeight() + 'px');
					} else {
						$('#newAndExclusive').css('min-height',
								$('#stayQued').outerHeight() + 'px');
					}
				}
				
			},2500);
		}
	});
}

if ($('#newAndExclusive').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getNewAndExclusiveAjaxCall();
}


/* Promotional Banner Section starts */
function getPromoBannerHomepage(){
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getPromoBannerHomepage",
		data : dataString,

		success : function(response) {
			console.log(response.bannerImage);
			var defaultHtml = "";
			var bannerUrlLink = response.bannerUrlLink;
			var bannerImage = response.bannerImage;
			var bannerAltText = response.bannerAltText;
			var promoText1 = response.promoText1;
			var promoText2 = response.promoText2;
			var promoText3 = response.promoText3;
			var promoText4 = response.promoText4;
			

			//renderHtml = '<img src="' + response.bannerImage +'"/>';
			renderHtml = promoText1; 

			$('#promobannerhomepage').html(renderHtml);

		},
		error : function() {
			console.log('Failure in Promo!!!');
		}
	});
}


if ($('#promobannerhomepage').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getPromoBannerHomepage();
}
/* Promotional Banner Section Ends */



/* StayQued Section starts */
function getStayQuedHomepage(){
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getStayQuedHomepage",
		data : dataString,
		
		success : function(response) {
			console.log(response.bannerImage);
			var defaultHtml = "";
			var linkText = "";
			var bannerUrlLink = response.bannerUrlLink;
			var bannerImage = response.bannerImage;
			var bannerAltText = response.bannerAltText;
			var promoText1 = response.promoText1;
			var promoText2 = response.promoText2;
			var promoText3 = response.promoText3;
			var promoText4 = response.promoText4;
			if($(promoText2).is('p')){
				linkText = $(promoText2).text();
			} else {
				linkText = promoText2;
			}
			renderHtml = '<h1><span></span><span class="h1-qued">Stay Qued</span></h1><div class="qued-content">'+promoText1+'<a href="'+ ACC.config.encodedContextPath+bannerUrlLink+'" class="button maroon">'+linkText+'</a></div><div class="qued-image"><img src="'+bannerImage+'" class="img-responsive"></div>'; 
			$('#stayQued').html(renderHtml);

		},
		error : function() {
			console.log('Failure in StayQued!!!');
		}
	});
}


if ($('#stayQued').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getStayQuedHomepage();
}
/* StayQued Section Ends */

if ($('#showcase').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	if (window.localStorage) {
		for ( var key in localStorage) {
			if (key.indexOf("showcaseContent") >= 0) {
				window.localStorage.removeItem(key);
				console.log("Deleting.." + key);
			}
		}
	}
	getShowCaseAjaxCall();
}

// AJAX call for Showcase
function getShowCaseAjaxCall() {
	var env = $("#previewVersion").val();
	if(env == "true"){
		var dataString = 'version=Staged';
	} else {
		var dataString = 'version=Online';
	}
	$
			.ajax({
				type : "GET",
				dataType : "json",
				url : ACC.config.encodedContextPath + "/getCollectionShowcase",
				data : dataString,
				
				success : function(response) {
					console.log(response.subComponents);
					defaultComponentId="";
					renderHtml = "<h1>" + response.title + "</h1>"
							+ "<div class='showcase-heading showcase-switch'>";
					$
							.each(
									response.subComponents,
									function(k, v) {
										if (!v.showByDefault) {
											renderHtml += "<div class='showcaseItem'><a id='"
													+ v.compId
													+ "'>"+v.headerText+"</a></div>";
										} else {
											renderHtml += "<div class='showcaseItem'><a id='"
												+ v.compId
												+ "' class='showcase-border'>"+v.headerText+"</a></div>";
											defaultComponentId= v.compId;
										}

									});
					renderHtml += "</div>";
					$('#showcase').html(renderHtml);
					
					getShowcaseContentAjaxCall(defaultComponentId);
				},
				error : function() {
					// globalErrorPopup('Failure!!!');
					console.log("Error while getting showcase");
				}
			});
}
// Get Showcase Content AJAX
function getShowcaseContentAjaxCall(id) {
	if (window.localStorage
			&& (html = window.localStorage.getItem("showcaseContent-" + id)) && html != "") {
		// console.log("Local");
		$('.about-one showcase-section').remove();
		$('#showcase').append(decodeURI(html));
	}
	else{
		
	$
			.ajax({
				type : "GET",
				dataType : "json",
				beforeSend: function(){
					$(".showcase-switch").css("margin-bottom","80px");
					$("#showcase").append("<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='/store/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>");
                },
				url : ACC.config.encodedContextPath
						+ "/getShowcaseContent",
				data : {
					"id" : id
				},
				success : function(response) {
					$('.about-one.showcase-section').remove();
					defaultHtml = "<div class='about-one showcase-section'>";
					if (typeof response.bannerImageUrl !=="undefined") {
						defaultHtml +="<div class='desc-section'>";
						if(typeof response.bannerUrl !=="undefined"){
							defaultHtml +="<a href='"+ACC.config.encodedContextPath+response.bannerUrl+"'>";
						}
						defaultHtml += "<img src='"+ response.bannerImageUrl
						+ "'></img>";	
						if(typeof response.bannerUrl !=="undefined"){
							defaultHtml+="</a>";
						}
						defaultHtml +="</div>";
					}
					
				
					if (typeof response.text !== "undefined") {
						defaultHtml += "<div class='desc-section'>"+response.text+"</div>"
						
					}
					
					if (typeof response.firstProductImageUrl !== "undefined") {
						
						defaultHtml += " <div class='desc-section'><a href='"+ACC.config.encodedContextPath+response.firstProductUrl+"'><img src='"
								+ response.firstProductImageUrl
								+ "'></img>";
						
							defaultHtml +="<div class='showcase-center'>" ;
							if (typeof response.firstProductTitle !== "undefined"){
								defaultHtml +="<h3 class='product-name'>"+response.firstProductTitle+"</h3>";
							}
							if (typeof response.firstProductPrice !== "undefined"){
								defaultHtml +="<div class='price'><p class='normal'><span class='priceFormat'>"+response.firstProductPrice+"</span></p></div>";
				                  
							}	
			                
							defaultHtml +="</a></div>";
			                  
						}
					

					defaultHtml += "</div>";
					
					$('#showcase .loaderDiv').remove();
					$(".showcase-switch").css("margin-bottom","0px");
					$('#showcase').append(defaultHtml);
					
					window.localStorage.setItem("showcaseContent-" + id,
							encodeURI(defaultHtml));

				},
				error : function() {
					console.log("Error while getting showcase content");
					$('#showcase .loaderDiv').remove();
					$(".showcase-switch").css("margin-bottom","0px");
				}
			});
	}
}
// ENd AJAX CALL

$(document).on("click", ".showcaseItem",
		function() {
			var id=$(this).find('a').attr("id");
			$(".showcaseItem").find("a").removeClass("showcase-border");
			$(this).find('a').addClass('showcase-border');
			
			$('.about-one.showcase-section').remove();
			 getShowcaseContentAjaxCall(id);
		});


$(window).on(
		'resize',
		function() {
			if ($(window).width() > 773) {
				if ($('#newAndExclusive').height() > $('#stayQued').height()) {
					$('#stayQued').css('min-height',
							$('#newAndExclusive').outerHeight() + 'px');
				} else {
					$('#newAndExclusive').css('min-height',
							$('#stayQued').outerHeight() + 'px');
				}

			} else {
				$('#newAndExclusive').css('min-height', 'auto');
				$('#stayQued').css('min-height', 'auto');
			}
		});

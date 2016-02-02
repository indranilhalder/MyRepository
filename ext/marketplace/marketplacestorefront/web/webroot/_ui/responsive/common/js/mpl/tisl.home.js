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

function getBrandsYouLoveAjaxCall() {
	$
			.ajax({
				type : "GET",
				dataType : "json",
				url : ACC.config.encodedContextPath + "/getBrandsYouLove",

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
											renderHtml += "<div class='home-brands-you-love-carousel-brands' id='"
													+ v.compId
													+ "'><img src='"
													+ v.brandLogoUrl
													+ "'></img></div>";
										} else {
											renderHtml += "<div class='home-brands-you-love-carousel-brands active' id='"
													+ v.compId
													+ "'><img src='"
													+ v.brandLogoUrl
													+ "'></img></div>";
											defaultComponentId= v.compId;
										}

									});
					renderHtml += "</div>";
					$('#brandsYouLove').html(renderHtml);
					
					getBrandsYouLoveContentAjaxCall(defaultComponentId);
				},
				error : function() {
					//globalErrorPopup('Failure!!!');
					console.log("Error while getting brands you love");
				}
			});
}

// Get Brands You Love Content AJAX
function getBrandsYouLoveContentAjaxCall(id) {
	if (window.localStorage
			&& (html = window.localStorage.getItem("brandContent-" + id)) && html != "") {
		// console.log("Local");
		$('.home-brands-you-love-desc').empty();
		$('#brandsYouLove').append(decodeURI(html));
	}
	else{
		
	$
			.ajax({
				type : "GET",
				dataType : "json",
				
				url : ACC.config.encodedContextPath
						+ "/getBrandsYouLoveContent",
				data : {
					"id" : id
				},
				success : function(response) {
					$('.home-brands-you-love-desc').empty();
					defaultHtml = "<div class='home-brands-you-love-desc'>";
					
					if (typeof response.firstProductImageUrl !== "undefined") {
						
						defaultHtml += "<div class='home-brands-you-love-side-image left'><a href='"+ACC.config.encodedContextPath+response.firstProductUrl+"'><img src='"
								+ response.firstProductImageUrl
								+ "'></img></a></div>";
					}
					defaultHtml += "<div class='home-brands-you-love-main-image'>";
					if (typeof response.text !== "undefined") {
						defaultHtml += response.text;
					}
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
								+ "'></img></a></div>";
					}

					defaultHtml += "</div>";
					
					$('#brandsYouLove').append(defaultHtml);
					
					window.localStorage.setItem("brandContent-" + id,
							encodeURI(defaultHtml));

				},
				error : function() {
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
$(document).on("mouseover", ".home-brands-you-love-carousel-brands",
		function() {
			$(".home-brands-you-love-carousel-brands").removeClass('active');
			$(this).addClass('active');
			$('.home-brands-you-love-desc').empty();
			bulCount = $(this).index();
			getBrandsYouLoveContentAjaxCall($(this).attr("id"));
		});

setInterval(function() {

	$(".home-brands-you-love-carousel-brands").removeClass('active');
	$(".home-brands-you-love-carousel-brands").eq(bulCount).addClass('active');
	componentId = $(".home-brands-you-love-carousel-brands").eq(bulCount).attr(
			'id');
	getBrandsYouLoveContentAjaxCall(componentId);
	bulCount++;
	if (bulCount == $(".home-brands-you-love-carousel-brands").length) {
		bulCount = 0;
	}

}, 20000);



//AJAX CALL BEST PICKS START
if ($('#bestPicks').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	getBestPicksAjaxCall();
}


function getBestPicksAjaxCall(){
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getBestPicks",
		
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
			renderHtml += "</div> <a href='#' class='view-cliq-offers'> View Cliq Offers </a>";	
			$("#bestPicks").html(renderHtml);
			//console.log()
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
				itemsTablet: [650,2], 
				itemsMobile : [480,2], 
				rewindNav: false,
				lazyLoad:true
			});
		}

	});
}

//AJAX CALL BEST PICKS END

function getNewAndExclusiveAjaxCall(){
	
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getNewAndExclusive",

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
											+ "'></img></a><p class='New_Exclusive_title'>" + value.productTitle + "</p><p class='New_Exclusive_title'>" + value.productPrice + "</p></div></div>"; 
											

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
			itemsMobile : false
		});
		}
	});
}

if ($('#newAndExclusive').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getNewAndExclusiveAjaxCall();
}


/*Promotional Banner Section starts*/
function getPromoBannerHomepage(){
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getPromoBannerHomepage",

		success : function(response) {
			console.log(response.bannerImage);
			var defaultHtml = "";
			var bannerUrlLink = response.bannerUrlLink;
			var bannerImage = response.bannerImage;
			var majorPromoText = response.majorPromoText;
			var minorPromo2Text = response.minorPromo2Text;
			var bannerAltText = response.bannerAltText;
			var minorPromo1Text = response.minorPromo1Text;
			var promoText1 = response.promoText1;
			var promoText2 = response.promoText2;
			var promoText3 = response.promoText3;
			var promoText4 = response.promoText4;
			
			//renderHtml = '<img src="' + response.bannerImage +'"/>';
			renderHtml = '<a href="' + bannerUrlLink + '">' + '<img src="' + bannerImage +'"/>' +'</a>'; 
			$('#promobannerhomepage').html(renderHtml);

		},
		error : function() {
			globalErrorPopup('Failure in Promo!!!');
		}
	});
}


if ($('#promobannerhomepage').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getPromoBannerHomepage();
}
/*Promotional Banner Section Ends*/


/*StayQued Section starts*/
function getStayQuedHomepage(){
	$
	.ajax({
		type : "GET",
		dataType : "json",
		url : ACC.config.encodedContextPath + "/getStayQuedHomepage",

		success : function(response) {
			console.log(response.bannerImage);
			var defaultHtml = "";
			var bannerUrlLink = response.bannerUrlLink;
			var bannerImage = response.bannerImage;
			var majorPromoText = response.majorPromoText;
			var minorPromo2Text = response.minorPromo2Text;
			var bannerAltText = response.bannerAltText;
			var minorPromo1Text = response.minorPromo1Text;
			var promoText1 = response.promoText1;
			var promoText2 = response.promoText2;
			var promoText3 = response.promoText3;
			var promoText4 = response.promoText4;
			renderHtml = '<a href="' + bannerUrlLink + '">' + '<img src="' + bannerImage +'"/>' +'</a>'; 
			$('#stayQued').html(renderHtml);

		},
		error : function() {
			globalErrorPopup('Failure in StayQued!!!');
		}
	});
}


if ($('#stayQued').children().length == 0 && $('#ia_site_page_id').val()=='homepage') {
	
	getStayQuedHomepage();
}
/*StayQued Section Ends*/

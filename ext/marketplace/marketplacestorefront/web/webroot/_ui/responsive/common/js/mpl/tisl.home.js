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
					globalErrorPopup('Failure!!!');
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
						
						defaultHtml += "<div class='home-brands-you-love-side-image left'><img src='"
								+ response.firstProductImageUrl
								+ "'></img></div>";
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
						defaultHtml += "<div class='home-brands-you-love-side-image left'><img src='"
								+ response.secondproductImageUrl
								+ "'></img></div>";
					}

					defaultHtml += "</div>";
					
					$('#brandsYouLove').append(defaultHtml);
					
					window.localStorage.setItem("brandContent-" + id,
							encodeURI(defaultHtml));

				},
				error : function() {
					globalErrorPopup('Failure!!!');
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

var headerLoggedinStatus = false;
var csrfDataChanged = false;
$(function() {
    $.ajax({
       url: ACC.config.encodedContextPath + "/fetchToken",
       type: 'GET',
       async:false,
       cache:false,
       success: function(data) {
           $("input[name='CSRFToken']").each(function() {
               this.value = data;
           });
           ACC.config.CSRFToken = data;
           var crsfSession = window.sessionStorage.getItem("csrf-token");
           if(window.sessionStorage && (null == crsfSession || crsfSession != data)){
          	 csrfDataChanged = true;
          	 window.sessionStorage.setItem("csrf-token",data);
           }
       }
   });
});
$(function() {
	//TISPRO-522 IE Issue Fix
	
	var header = window.sessionStorage.getItem("header");
	var userCookChanged = userCookieChanged();
	if(forceSetHeader() || null == header || userCookChanged == true || csrfDataChanged){
		$.ajax({
	        url: ACC.config.encodedContextPath + "/setheader?timestamp="+Date.now(),
	        type: 'GET',
	        cache:false,
	        success: function(data) {
	        	window.sessionStorage.setItem("header" , JSON.stringify(data));
	        	setHeader(data);
	        }
	    });
	}else{
		 header = JSON.parse(header);
		 setHeader(header);
	 	}
	});
 
$("div.departmenthover").on("mouseover touchend", function() {
    var id = this.id;
    var code = id.substring(4);

    if (!$.cookie("dept-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("deptmenuhtml") >= 0) {
                window.localStorage.removeItem(key);
                // console.log("Deleting.." + key);

            }
        }
    }
    if (window.localStorage && (html = window.localStorage.getItem("deptmenuhtml-" + code)) && html != "") {
        // console.log("Local");
        $("ul." + id).html(decodeURI(html));
        //LazyLoad();
    } else {
        $.ajax({
            url: ACC.config.encodedContextPath +
                "/departmentCollection",
            type: 'GET',
            data: "department=" + code,
            success: function(html) {
                // console.log("Server");
                $("ul." + id).html(html);
                if (window.localStorage) {
                    $.cookie("dept-list", "true", {
                        expires: 1,
                        path: "/"

                    });
                    window.localStorage.setItem(
                        "deptmenuhtml-" + code,
                        encodeURI(html));

                }
            }
        });


    }
    
});


$(".A-ZBrands").on("mouseover touchend", function(e) {
	var componentUid = $("#componentUid").val();
    if ($("li#atozbrandsdiplay").length) {
        // console.log("Dipslaying A-Z Brands..");

        if (!$.cookie("dept-list") && window.localStorage) {
            for (var key in localStorage) {
                if (key.indexOf("atozbrandmenuhtml") >= 0) {
                    window.localStorage.removeItem(key);
                    // console.log("Deleting.." + key);

                }
            }
        }
        if (window.localStorage && (html = window.localStorage.getItem("atozbrandmenuhtml")) && html != "") {
            // console.log("Local");
            if ($("div#appendedAtoZBrands") == null || $(
                "div#appendedAtoZBrands").length == 0) {
                $("li#atozbrandsdiplay").append(decodeURI(html));
            }
        } else {
            // console.log("Server");

            $.ajax({
                url: ACC.config.encodedContextPath +
                    "/atozbrands",
                type: 'GET',
                data : {
					 "componentUid" : componentUid
					},
                success: function(html) {
                    //console.log(html)
                    if ($("div#appendedAtoZBrands") == null ||
                        $("div#appendedAtoZBrands").length ==
                        0) {
                        $("li#atozbrandsdiplay").append(
                            html);
                    }
                    if (window.localStorage) {
                        $.cookie("dept-list", "true", {
                            expires: 1,
                            path: "/"

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

//$("span.helpmeshopbanner").on("click touchend", function() {
//
//	$.ajax({
//		url : ACC.config.encodedContextPath + "/helpmeshop",
//		type : 'GET',
//		success : function(html) {
//			$("div#latestOffersContent").html(html);
//		}
//	});
//});


$("span.latestOffersBanner").on("click touchend", function() {
	$.ajax({
		url : ACC.config.encodedContextPath + "/listOffers",
		type : 'GET',
		success : function(html) {
			$("div#latestOffersContent").html(html);
		},
		
		complete : function() {
			$(".topConcierge").owlCarousel({
				navigation:true,
				navigationText : [],
				pagination:false,
				itemsDesktop : [5000,5], 
				itemsDesktopSmall : [1400,5], 
				itemsTablet: [650,1], 
				itemsMobile : [480,1], 
				rewindNav: false,
				scrollPerPage:true
			});
			
			var tcitemLength = $(".topConcierge .owl-item").length,tcitemWidth = $(".topConcierge .owl-item").outerWidth() ;
			if (tcitemLength < 5) {
				$(".topConcierge .owl-wrapper").css({
				"width":tcitemLength*tcitemWidth,
				"margin" : "auto"
				});
			}
			
			
		}, 
	});
});


$(document).on('click',"div#closeConceirge",function(e) {
	$(this).parents('.banner').removeClass('active');
});

var trackLinkHover;
$("a#tracklink").on("mouseover touchend", function(e) {
    e.stopPropagation();
    trackLinkHover = setTimeout(function(){
    	$.ajax({
            url: ACC.config.encodedContextPath +
                "/headerTrackOrder",
            type: 'GET',
            cache:false,
            success: function(html) {
                $("ul.trackorder-dropdown").html(html);
            }
        });
    },300);
});
$("li.track.trackOrder").on("mouseleave", function() {
	clearTimeout(trackLinkHover);
});


var wishlistHover;
$("a#myWishlistHeader").on("mouseover touchend", function(e) {
    e.stopPropagation();
    wishlistHover = setTimeout(function(){
    	$.ajax({
            url: ACC.config.encodedContextPath + "/headerWishlist",
            type: 'GET',
            data: "&productCount=" + $(this).attr("data-count"),
            success: function(html) {
                $("div.wishlist-info").html(html);
            }
        });
    },300);
    
});
$("li.wishlist").on("mouseleave", function() {
	clearTimeout(wishlistHover);
});
//TISPRO-522-IE Issue Fix
var loginHover;
$("li.ajaxloginhi").on("mouseover touchend", function(e) {
    e.stopPropagation();
    loginHover = setTimeout(function(){
    	if ($("ul.ajaxflyout").html().trim().length <= 0) {
            $.ajax({
                url: ACC.config.encodedContextPath +
                    "/headerloginhi?timestamp="+Date.now(),
                type: 'GET',
                success: function(html) {
                    $("ul.ajaxflyout").html(html);
                }
            });
        }
    },300);
});

$("li.logIn-hi").on("mouseleave", function(e) {
	clearTimeout(loginHover);
});
//
var activePos = 0;
$(window).on("resize load", function() {
    activePos = ($(window).width() > 790) ? 3 : 1;
});

function getBrandsYouLoveAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getBrandsYouLove",
            data: dataString,
            success: function(response) {
                //console.log(response.subComponents);
                defaultComponentId = "";
                renderHtml = "<h1>" + response.title + "</h1>" +
                    "<div class='home-brands-you-love-carousel'>";
                $.each(response.subComponents, function(k, v) {
                    //console.log(v.brandLogoUrl);
                    if (!v.showByDefault) {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item' id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "'></img></div>";
                    } else {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item active' id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "'></img></div>";
                        defaultComponentId = v.compId;
                    }
                });
                renderHtml += "</div>";
                renderHtml +=
                    '<div class="bulprev"></div><div class="bulnext"></div>';
                $('#brandsYouLove').html(renderHtml);
                getBrandsYouLoveContentAjaxCall(defaultComponentId);
            },
            error: function() {
                // globalErrorPopup('Failure!!!');
                console.log("Error while getting brands you love");
            },
            complete: function() {
                $(".home-brands-you-love-carousel").owlCarousel({
                    navigation: false,
                    navigationText: [],
                    pagination: false,
                    itemsDesktop: [5000, 7],
                    itemsDesktopSmall: [1400, 7],
                    itemsTablet: [790, 3],
                    itemsMobile: [480, 3],
                    rewindNav: false,
                    mouseDrag: false,
                    touchDrag: false,
                });
                var index = $(
                    ".home-brands-you-love-carousel-brands.active"
                ).parents('.owl-item').index()
                if (index > activePos) {
                    for (var i = 0; i < index - activePos; i++) {
                        $(
                            ".home-brands-you-love-carousel .owl-wrapper"
                        ).append($(
                            ".home-brands-you-love-carousel .owl-item"
                        ).first());
                    }
                } else if (index < activePos) {
                    for (var i = 0; i < activePos - index; i++) {
                        $(
                            ".home-brands-you-love-carousel .owl-wrapper"
                        ).prepend($(
                            ".home-brands-you-love-carousel .owl-item"
                        ).last());
                    }
                }
            }
        });
    }
    // Get Brands You Love Content AJAX

function getBrandsYouLoveContentAjaxCall(id) {
	
        if (window.localStorage && (html = window.localStorage.getItem(
            "brandContent-" + id)) && html != "") {
            // console.log("Local");
            $(".home-brands-you-love-carousel").css("margin-bottom", "20px");
            //$('#brandsYouLove').append(defaultHtml);
            $('.home-brands-you-love-desc').remove();
            $('#brandsYouLove').append(decodeURI(html));
            //LazyLoad();
        } else {
            $.ajax({
                type: "GET",
                dataType: "json",
                beforeSend: function() {
                	var staticHost=$('#staticHost').val();
                    $(".home-brands-you-love-carousel").css(
                        "margin-bottom", "120px");
                    $("#brandsYouLove").append(
                        "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 200px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                    );
                },
                url: ACC.config.encodedContextPath +
                    "/getBrandsYouLoveContent",
                data: {
                    "id": id
                },
                success: function(response) {
                    $('.home-brands-you-love-desc').remove();
                    defaultHtml =
                        "<div class='home-brands-you-love-desc'>";
                    if (typeof response.text !== "undefined") {
                        defaultHtml += response.text;
                    }
                    if (typeof response.firstProductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-side-image left'><a href='" +
                            ACC.config.encodedContextPath +
                            response.firstProductUrl +
                            "'><img src='" + response.firstProductImageUrl +
                            "'></img>";
                        if (typeof response.firstProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='product-name'>" +
                                response.firstProductTitle + "</p>";
                        }
                        if (typeof response.firstProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='price normal'><span class='priceFormat'>" +
                                response.firstProductPrice +
                                "</span></p>";
                        }
                        defaultHtml += "</a></div>"
                    }
                    defaultHtml +=
                        "<div class='home-brands-you-love-main-image'>";
                    if (typeof response.bannerImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-main-image-wrapper'>";
                        if (typeof response.bannerText !==
                            "undefined") {
                            defaultHtml +=
                                "<div class='visit-store-wrapper'>" +
                                response.bannerText + "</div>";
                        }
                        defaultHtml += "<img src='" + response.bannerImageUrl +
                            "'></img></div></div>";
                    }
                    if (typeof response.secondproductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-side-image right'><a href='" +
                            ACC.config.encodedContextPath +
                            response.secondProductUrl +
                            "'><img src='" + response.secondproductImageUrl +
                            "'></img>";
                        if (typeof response.secondProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='product-name'>" +
                                response.secondProductTitle +
                                "</p>";
                        }
                        if (typeof response.secondProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='normal price'><span class='priceFormat'>" +
                                response.secondProductPrice +
                                "</span></p>";
                        }
                        defaultHtml += "</a></div>"
                    }
                    defaultHtml += "</div>";
                    $(".home-brands-you-love-carousel").css(
                        "margin-bottom", "20px");
                    $('#brandsYouLove').append(defaultHtml);
                    window.localStorage.setItem("brandContent-" +
                        id, encodeURI(defaultHtml));
                },
                complete: function() {
                   $('#brandsYouLove .loaderDiv').remove();
                },
                error: function() {
                    $('#brandsYouLove .loaderDiv').remove();
                    $(".home-brands-you-love-carousel").css(
                        "margin-bottom", "20px");
                    console.log(
                        "Error while getting brands you love content"
                    );
                }
            });
        }
    }
    // ENd AJAX CALL

var bulCount = $(".home-brands-you-love-carousel-brands.active").index() - 1;
/*$(document).on("click", ".home-brands-you-love-carousel-brands",
		function() {
			$(".home-brands-you-love-carousel-brands").removeClass('active');
			$(this).addClass('active');
			$('.home-brands-you-love-desc').remove();
			bulCount = $(this).parent().index();
			getBrandsYouLoveContentAjaxCall($(this).attr("id"));
		});*/
$(document).on("click", ".home-brands-you-love-carousel-brands", function() {
    $(".home-brands-you-love-carousel-brands").removeClass('active');
    $(this).addClass('active');
    $('.home-brands-you-love-desc').remove();
    var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
    var index = $(this).parents('.owl-item').index();
    if (index > activePos) {
        for (var i = 0; i < index - activePos; i++) {
            $("#brandsYouLove .owl-wrapper").css('transition',
                'all 0.2s linear');
            $("#brandsYouLove .owl-wrapper").css('transform',
                'translate3d(-' + iw + 'px, 0px, 0px)');
            setTimeout(function() {
                $(".home-brands-you-love-carousel .owl-wrapper")
                    .append($(
                        ".home-brands-you-love-carousel .owl-item"
                    ).first());
                $("#brandsYouLove .owl-wrapper").css(
                    'transition', 'all 0s linear');
                $("#brandsYouLove .owl-wrapper").css(
                    'transform',
                    'translate3d(0px, 0px, 0px)');
            }, 200);
        }
    } else if (index < activePos) {
        for (var i = 0; i < activePos - index; i++) {
            $("#brandsYouLove .owl-wrapper").css('transition',
                'all 0.2s linear');
            $("#brandsYouLove .owl-wrapper").css('transform',
                'translate3d(' + iw + 'px, 0px, 0px)');
            setTimeout(function() {
                $(".home-brands-you-love-carousel .owl-wrapper")
                    .prepend($(
                        ".home-brands-you-love-carousel .owl-item"
                    ).last());
                $("#brandsYouLove .owl-wrapper").css(
                    'transition', 'all 0s linear');
                $("#brandsYouLove .owl-wrapper").css(
                    'transform',
                    'translate3d(0px, 0px, 0px)');
            }, 200);
        }
    }
    var index = $(this).parents('.owl-item').index();
    getBrandsYouLoveContentAjaxCall($(this).attr("id"));
});

$(document).on("click", ".bulprev", function() {
    /*$('.home-brands-you-love-desc').remove();
		$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").removeClass('active');
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").eq(activePos).addClass('active');
		getBrandsYouLoveContentAjaxCall($(".home-brands-you-love-carousel-brands.active").attr('id'));*/
    var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
    $('.home-brands-you-love-desc').remove();
    $("#brandsYouLove .owl-wrapper").css('transition', 'all 0.3s ease');
    $("#brandsYouLove .owl-wrapper").css('transform', 'translate3d(' +
        iw + 'px, 0px, 0px)');
    setTimeout(function() {
        $(".home-brands-you-love-carousel .owl-wrapper").prepend(
            $(".home-brands-you-love-carousel .owl-item").last()
        );
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).removeClass('active');
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).eq(activePos).addClass('active');
        getBrandsYouLoveContentAjaxCall($(
            ".home-brands-you-love-carousel-brands.active"
        ).attr('id'));
        $("#brandsYouLove .owl-wrapper").css('transition',
            'all 0s ease');
        $("#brandsYouLove .owl-wrapper").css('transform',
            'translate3d(0px, 0px, 0px)');
    }, 300);
});
$(document).on("click", ".bulnext", function() {
    var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
    $('.home-brands-you-love-desc').remove();
    $("#brandsYouLove .owl-wrapper").css('transition', 'all 0.3s ease');
    $("#brandsYouLove .owl-wrapper").css('transform', 'translate3d(-' +
        iw + 'px, 0px, 0px)');
    setTimeout(function() {
        $(".home-brands-you-love-carousel .owl-wrapper").append(
            $(".home-brands-you-love-carousel .owl-item").first()
        );
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).removeClass('active');
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).eq(activePos).addClass('active');
        getBrandsYouLoveContentAjaxCall($(
            ".home-brands-you-love-carousel-brands.active"
        ).attr('id'));
        $("#brandsYouLove .owl-wrapper").css('transition',
            'all 0s ease');
        $("#brandsYouLove .owl-wrapper").css('transform',
            'translate3d(0px, 0px, 0px)');
    }, 300);
});
if ($('#ia_site_page_id').val() == 'homepage') {
    /*setInterval(function() {

$('.bulnext').click();

}, 20000);
*/
}
// AJAX CALL BEST PICKS START


function getBestPicksAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getBestPicks",
            data: dataString,
            success: function(response) {
                renderHtml = "<h1>" + response.title + "</h1>" +
                    "<div class='home-best-pick-carousel'>";
                $.each(response.subItems, function(k, v) {
                    if (v.url) {
                        renderHtml += "<a href='" +
                            appendIcid(v.url, v.icid) +
                            "' class='item'>";
                    }
                    if (v.imageUrl) {
                        renderHtml +=
                            "<div class='home-best-pick-carousel-img'> <img class='lazyOwl' data-src='" +
                            v.imageUrl + "'></img></div>";
                    }
                    if (v.text) {
                        renderHtml +=
                            "<div class='short-info'>" + v.text +
                            "</div>";
                    }
                    renderHtml += "</a>";
                });
                
                renderHtml +=
                    // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
                 	"</div> <a href='";
                if(typeof response.buttonLink!=="undefined"){
                	 renderHtml +=response.buttonLink+"'";
                }
                else{
                	renderHtml +=ACC.config.encodedContextPath+"/offersPage'";
                }
                
                renderHtml +="class='view-cliq-offers'>";
                if(typeof response.buttonText!=="undefined"){
                	 renderHtml +=response.buttonText;
                }
                else{
                	 renderHtml +=" View Cliq Offers ";
                }
                renderHtml +="</a>";
                   // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
                	//"</div> <a href='"+ACC.config.encodedContextPath+"/offersPage' class='view-cliq-offers'> View Cliq Offers </a>";
                $("#bestPicks").html(renderHtml);
                // console.log()
            },
            error: function() {
                console.log("Error while getting best picks");
            },
            complete: function() {
                $(".home-best-pick-carousel").owlCarousel({
                    navigation: true,
                    navigationText: [],
                    pagination: false,
                    itemsDesktop: [5000, 5],
                    itemsDesktopSmall: [1400, 5],
                    itemsTablet: [650, 1],
                    itemsMobile: [480, 1],
                    rewindNav: false,
                    lazyLoad: true,
                    scrollPerPage: true
                });
            }
        });
    }
    // AJAX CALL BEST PICKS END
    //AJAX CALL PRODUCTS YOU CARE START

function getProductsYouCareAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getProductsYouCare",
            data: dataString,
            success: function(response) {
                renderHtml = "<h1>" + response.title + "</h1>";
                renderHtml +=
                    "<div class='home-product-you-care-carousel'>";
                $.each(response.categories, function(k, v) {
                    var URL = ACC.config.encodedContextPath +
                    /*"/Categories/" + v.categoryName*/ v.categoryPath +   //TISPRD_2315
                    "/c-" + v.categoryCode.toLowerCase();
                    //for url
                    renderHtml += "<a href='" + appendIcid(
                            URL, v.icid) +
                        "' class='item'>";
                    //for image
                    renderHtml +=
                        "<div class='home-product-you-care-carousel-img'> <img class='lazyOwl' data-src='" +
                        v.mediaURL + "'></img></div>";
                    renderHtml +=
                        "<div class='short-info'><h3 class='product-name'><span>" +
                        v.categoryName +
                        "</span></h3></div>";
                    renderHtml += "</a>";
                });
                renderHtml += "</div>";
                $("#productYouCare").html(renderHtml);
            },
            error: function() {
                console.log(
                    'Error while getting getProductsYouCare');
            },
            complete: function() {
                $(".home-product-you-care-carousel").owlCarousel({
                    navigation: true,
                    navigationText: [],
                    pagination: false,
                    itemsDesktop: [5000, 4],
                    itemsDesktopSmall: [1400, 4],
                    itemsTablet: [650, 2],
                    itemsMobile: [480, 2],
                    rewindNav: false,
                    lazyLoad: true,
                    scrollPerPage: true
                });
            }
        });
    }
    //AJAX CALL PRODUCTS YOU CARE END

function getNewAndExclusiveAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getNewAndExclusive",
        data: dataString,
        success: function(response) {
            //console.log(response.newAndExclusiveProducts);
        	var staticHost=$('#staticHost').val();
            var defaultHtml = "";
            renderHtml = "<h1>" + response.title + "</h1>" +
                "<div class='carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference' id='new_exclusive'>";
            $.each(response.newAndExclusiveProducts, function(
                key, value) {
            	if(value.isNew == 'Y')
            	{
            	renderNewHtml = "<div style='z-index: 1;' class='new'><img class='brush-strokes-sprite sprite-New' src='"+staticHost+"/_ui/responsive/common/images/transparent.png'><span>New</span></div>";
            	} else {
            		renderNewHtml = '';
            	}
                renderHtml +=
                    "<div class='item slide'><div class='newExclusiveElement'><a href='" +
                    ACC.config.encodedContextPath +
                    value.productUrl + "'>"+renderNewHtml+"<img class='lazyOwl' data-src='" +
                    value.productImageUrl +
                    "'></img><p class='New_Exclusive_title'>" +
                    value.productTitle +
                    "</p><p class='New_Exclusive_price'><span class='priceFormat'>" +
                    value.productPrice +
                    "</span></p></a></div></div>";
            });
            renderHtml += "</div><a href='" + ACC.config.encodedContextPath +
                "/search/viewOnlineProducts' class='new_exclusive_viewAll'>View All</a>";
            $('#newAndExclusive').html(renderHtml);
        },
        error: function() {
            console.log("Error while getting new and exclusive");
        },
        complete: function() {
            $("#new_exclusive").owlCarousel({
                navigation: true,
                rewindNav: false,
                navigationText: [],
                pagination: false,
                items: 2,
                itemsDesktop: false,
                itemsDesktopSmall: false,
                itemsTablet: false,
                itemsMobile: false,
                scrollPerPage: true,
                lazyLoad: true
            });
            setTimeout(function() {
                /*if($(window).width() > 773) {
					$('#newAndExclusive').css('min-height',$('#newAndExclusive').parent().height()+'px');
				}*/
                //alert($('#newAndExclusive').height() +"|||"+$('#stayQued').height())
                        $('#stayQued').css('min-height',
                            $('#newAndExclusive').outerHeight() +
                            'px');
            }, 2500);
        }
    });
}

/* Promotional Banner Section starts */
function getPromoBannerHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath +
            "/getPromoBannerHomepage",
        data: dataString,
        success: function(response) {
        	var arr= new Array();
        	$.each( response, function(key, obj){
                    arr.push(key,obj);
            });
        	var finalArr = arr[1];
        	var count = finalArr.length;
            if(window.sessionStorage && (seqPromo = window.sessionStorage.getItem("PromoBannerHomepage"))) {
                seqPromo = parseInt(seqPromo);
            	if (seqPromo == '' || seqPromo >= count || seqPromo < 1) {
            		seqPromo = 1;
            	} else {
            		seqPromo=seqPromo+1;
            	}
        		showPromoBanner(finalArr[seqPromo-1]);
        		window.sessionStorage.setItem("PromoBannerHomepage", seqPromo);
        	} else {
        		showPromoBanner(finalArr[0]);
        		if(window.sessionStorage) {
        			window.sessionStorage.setItem("PromoBannerHomepage", 1);
        		}
        	}
        },
        error: function() {
            console.log('Failure in Promo!!!');
        }
    });
}

function showPromoBanner(response){
	 //console.log(response.bannerImage);
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
}

/* Promotional Banner Section Ends */
/* StayQued Section starts */
function getStayQuedHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getStayQuedHomepage",
        data: dataString,
        success: function(response) {
        	var arr= new Array();
        	$.each( response, function(key, obj){
                    arr.push(key,obj);
            });
        	var finalArr = arr[1];
        	var count = finalArr.length;
            if(window.sessionStorage && (seqStay = window.sessionStorage.getItem("StayQuedHomepage"))) {
                seqStay = parseInt(seqStay);
            	if (seqStay == '' || seqStay >= count || seqStay < 1) {
            		seqStay = 1;
            	} else {
            		seqStay=seqStay+1;
            	}
        		showStayQued(finalArr[seqStay-1]);
        		window.sessionStorage.setItem("StayQuedHomepage", seqStay);
        	} else {
        		showStayQued(finalArr[0]);
        		if(window.sessionStorage) {
        			window.sessionStorage.setItem("StayQuedHomepage", 1);
        		}
        	}
       },
        error: function() {
            console.log('Failure in StayQued!!!');
        }
    });
}

function showStayQued(response){
	//alert(response);
	var defaultHtml = "";
    var linkText = "";
    var bannerUrlLink = response.bannerUrlLink;
    var bannerImage = response.bannerImage;
    var bannerAltText = response.bannerAltText;
    var promoText1 = response.promoText1;
    var promoText2 = response.promoText2;
    var promoText3 = response.promoText3;
    var promoText4 = response.promoText4;
    if ($(promoText2).is('p')) {
        linkText = $(promoText2).text();
    } else {
        linkText = promoText2;
    }
    renderHtml =
        '<h1><span class="spriteImg"></span><span class="h1-qued">Stay Qued</span></h1><div class="qued-content">' +
        promoText1 + '<a href="' + bannerUrlLink +
        '" class="button maroon">' + linkText +
        '</a></div><div class="qued-image"><img class="lazy" src="' +
        bannerImage + '" class="img-responsive"></div>';
    $('#stayQued').html(renderHtml);
}


/* StayQued Section Ends */

// AJAX call for Showcase
function getShowCaseAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath +
                "/getCollectionShowcase",
            data: dataString,
            success: function(response) {
                //console.log(response.subComponents);
                defaultComponentId = "";
                renderHtml = "<h1>" + response.title + "</h1>" +
                    "<div class='showcase-heading showcase-switch'>";
                $.each(response.subComponents, function(k, v) {
                    if (!v.showByDefault) {
                        renderHtml +=
                            "<div class='showcaseItem'><a id='" +
                            v.compId + "'>" + v.headerText +
                            "</a></div>";
                    } else {
                        renderHtml +=
                            "<div class='showcaseItem'><a id='" +
                            v.compId +
                            "' class='showcase-border'>" +
                            v.headerText + "</a></div>";
                        defaultComponentId = v.compId;
                    }
                });
                renderHtml += "</div>";
                $('#showcase').html(renderHtml);
                getShowcaseContentAjaxCall(defaultComponentId);
            },
            error: function() {
                // globalErrorPopup('Failure!!!');
                console.log("Error while getting showcase");
            }
        });
    }
    // Get Showcase Content AJAX

function getShowcaseContentAjaxCall(id) {
	if (window.localStorage && (html = window.localStorage.getItem(
            "showcaseContent-" + id)) && html != "") {
            // console.log("Local");
            $('.about-one showcase-section').remove();
            $('#showcase').append(decodeURI(html));
            //LazyLoad();
        } else {
            $.ajax({
                type: "GET",
                dataType: "json",
                beforeSend: function() {
                	var staticHost=$('#staticHost').val();
                    $(".showcase-switch").css("margin-bottom",
                        "80px");
                    $("#showcase").append(
                        "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                    );
                },
                url: ACC.config.encodedContextPath +
                    "/getShowcaseContent",
                data: {
                    "id": id
                },
                success: function(response) {
                	
                    $('.about-one.showcase-section').remove();
                    defaultHtml =
                        "<div class='about-one showcase-section'>";
                    if (typeof response.bannerImageUrl !==
                        "undefined") {
                        defaultHtml += "<div class='desc-section'>";
                        if (typeof response.bannerUrl !==
                            "undefined") {
                            defaultHtml += "<a href='" + appendIcid(response.bannerUrl, response.icid) + "'>";
                        }
                        defaultHtml += "<img class='lazy' src='" + response.bannerImageUrl +
                            "'></img>";
                        if (typeof response.bannerUrl !==
                            "undefined") {
                            defaultHtml += "</a>";
                        }
                        defaultHtml += "</div>";
                    }
                    if (typeof response.text !== "undefined") {
                        defaultHtml += "<div class='desc-section'>" +
                            response.text + "</div>"
                    }
                    if (typeof response.firstProductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            " <div class='desc-section'><a href='" +
                            ACC.config.encodedContextPath +
                            response.firstProductUrl +
                            "'><img class='lazy' src='" + response.firstProductImageUrl +
                            "'></img>";
                        defaultHtml +=
                            "<div class='showcase-center'>";
                        if (typeof response.firstProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<h3 class='product-name'>" +
                                response.firstProductTitle +
                                "</h3>";
                        }
                        if (typeof response.firstProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<div class='price'><p class='normal'><span class='priceFormat'>" +
                                response.firstProductPrice +
                                "</span></p></div>";
                        }
                        defaultHtml += "</a></div>";
                    }
                    defaultHtml += "</div>";
                    $('#showcase .loaderDiv').remove();
                    $(".showcase-switch").css("margin-bottom",
                        "0px");
                    $('#showcase').append(defaultHtml);
                    window.localStorage.setItem("showcaseContent-" +
                        id, encodeURI(defaultHtml));
                },
                error: function() {
                    console.log(
                        "Error while getting showcase content");
                    $('#showcase .loaderDiv').remove();
                    $(".showcase-switch").css("margin-bottom",
                        "0px");
                }
            });
        }
    }
    // ENd AJAX CALL
$(document).on("click", ".showcaseItem", function() {
    var id = $(this).find('a').attr("id");
    $(".showcaseItem").find("a").removeClass("showcase-border");
    $(this).find('a').addClass('showcase-border');
    $('.about-one.showcase-section').remove();
    getShowcaseContentAjaxCall(id);
});
$(window).on('resize', function() {
    if ($(window).width() > 790) {
        $('#stayQued').css('min-height', 'auto');
    }
});

function appendIcid(url, icid) {
    if (url != null) {
        if (url.indexOf("?") != -1) {
            return url + "&icid=" + icid;
        } else {
            return url + "?icid=" + icid;
        }
    }
}
$(document).ready(function(){
	//TISPT-290
	if($('#pageTemplateId').val() =='LandingPage2Template'){
	lazyLoadDivs();

	setTimeout(function(){$(".timeout-slider").removeAttr("style")},1500);
}
//Fix for defect TISPT-202
getFooterOnLoad();

});
//call lazy load after ajaz for page stops
/*$(document).ajaxStop(function(){
	LazyLoad();
});

function LazyLoad(){
	$("img.lazy").lazyload({
    	effect : "fadeIn"
    });
}*/

$(document).ready(function() {
var resize_stop;
$(window).on('resize', function() {
	  clearTimeout(resize_stop);
	  resize_stop = setTimeout(function() {
		  $('.home-brands-you-love-carousel-brands.active').click();
		  
		  var tcitemLength = $(".topConcierge .owl-item").length,tcitemWidth = $(".topConcierge .owl-item").outerWidth() ;
			if (tcitemLength < 5) {
				$(".topConcierge .owl-wrapper").css({
				"width":tcitemLength*tcitemWidth,
				"margin" : "auto"
				});
			}
			
	  }, 250);
});

	
	if (!$.cookie("enhanced-search-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("enhancedSearchData") >= 0) {
                window.localStorage.removeItem(key);

            }
        }
    }
    if (window.localStorage && (data = window.localStorage.getItem("enhancedSearchData")) && data != "") {
        populateEnhancedSearch(JSON.parse(data));
    } 
    else {
	$.ajax({
		url : ACC.config.encodedContextPath + "/view/MplEnhancedSearchBoxComponentController/searchdropdown",
		type : 'GET',
		//dataType: "json",
		success : function(enhancedSearchData){
			 populateEnhancedSearch(enhancedSearchData);
			
			if (window.localStorage) {
                $.cookie("enhanced-search-list", "true", {
                    expires: 1,
                    path: "/"

                });
                window.localStorage.setItem(
                    "enhancedSearchData",
                    JSON.stringify(enhancedSearchData));
            }
			
		},
		error : function(error){
		}
	});
    }
    
   $(".lazy-brands").on("mouseover touchend", function(e) {
	   var lazyImgs = $(this).find("ul.images").find("img.lazy");
	   $(lazyImgs).each(function(){
		   var original = $(this).attr("data-src");
		   $(this).attr("src",original);
		   $(this).removeAttr("data-src");
	   });
   }); 
});



function populateEnhancedSearch(enhancedSearchData)
{
	var searchCode=$("#searchCodeForDropdown").val();
	var notPresentCategory=true;
	var notPresentBrand=true;
	var notPresentSeller=true;
	if(enhancedSearchData.categoryData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Departments"></optgroup>');
		for (var i=0; i<enhancedSearchData.categoryData.length; i++){
			var code=enhancedSearchData.categoryData[i].code;
			var name=enhancedSearchData.categoryData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentCategory=false;
			}
			$(".enhanced-search ul[label=Departments]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Departments]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(enhancedSearchData.brandData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Brands"></optgroup>');
		for (var i=0; i<enhancedSearchData.brandData.length; i++){
			var code=enhancedSearchData.brandData[i].code;
			var name=enhancedSearchData.brandData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentBrand=false;
			}
			$(".enhanced-search ul[label=Brands]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Brands]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
			
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(enhancedSearchData.sellerData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Sellers"></optgroup>');
		for (var i=0; i<enhancedSearchData.sellerData.length; i++){
			var code=enhancedSearchData.sellerData[i].id;
			var name=enhancedSearchData.sellerData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentSeller=false;
			}
			$(".enhanced-search ul[label=Sellers]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Sellers]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(notPresentCategory==true && notPresentBrand==true && notPresentSeller==true)
	{
		$(".select-list .dropdown li#all").addClass("selected");
		$("#searchBoxSpan").html($(".select-list .dropdown li#all").text());
	}
}
	
	//Added
	
	$("div.toggle.brandClass").on("mouseover touchend", function() {
		var componentUid = $(this).find('a').attr('id');
		 if (!$.cookie("dept-list") && window.localStorage) {
		        for (var key in localStorage) {
		            if (key.indexOf("brandhtml") >= 0) {
		                window.localStorage.removeItem(key);
		                // console.log("Deleting.." + key);

		            }
		        }
		    }
		 if (window.localStorage && (html = window.localStorage.getItem("brandhtml-" + componentUid)) && html != "") {
		        // console.log("Local");
		        //$("ul#"+componentUid).html(decodeURI(html));
			    $("ul[id='"+componentUid+"']").html(decodeURI(html));
		    }else{
		    	
		    	 $.ajax({
			            url: ACC.config.encodedContextPath +
			            "/shopbybrand",
			            type: 'GET',
			            data:{"compId":componentUid},
			            success: function(html) {
			                //$("ul#"+componentUid).html(html);
			            	$("ul[id='"+componentUid+"']").html(html); 
			                if (window.localStorage) {
			                    $.cookie("dept-list", "true", {
			                        expires: 1,
			                        path: "/"

			                    });
			                    window.localStorage.setItem(
			                        "brandhtml-" + componentUid,
			                        encodeURI(html));

			                }
			                
			            }
			        });
		    	
		    }
	       
	    
	});
	//End
	
	// Fix for defect TISPT-202
	
	function openNeedHelpSec()
	{
		$(this).removeClass("minimize");
		$("#h").toggle();
	}
	function getFooterOnLoad()
	{
		var slotUid = "FooterSlot";
		
		if (!$.cookie("dept-list") && window.localStorage) {
	        for (var key in localStorage) {
	            if (key.indexOf("footerhtml") >= 0) {
	                window.localStorage.removeItem(key);                
	            }
	        }
	    }
		
		if (window.localStorage && (html = window.localStorage.getItem("footerhtml")) && html != "") {
			$("#footerByAjaxId").html(decodeURI(html));
	    } else {
	        $.ajax({
	            url: ACC.config.encodedContextPath +
	                "/getFooterContent",
	            type: 'GET',
	            data : {
					 "id" : slotUid
					},
	            success: function(footerhtml) {
	            	$("#footerByAjaxId").html(footerhtml);
	            	
	                if (window.localStorage) {
	                    $.cookie("dept-list", "true", {
	                        expires: 1,
	                        path: "/"

	                    });
	                    window.localStorage.setItem(
	                        "footerhtml",
	                        encodeURI(footerhtml));
	                }
	         
					if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
						var footer_height=$('footer').height() + 20 + 'px';
						$(".body-Content").css('padding-bottom',footer_height);
					}
					else{
						$(".body-Content").css('padding-bottom','0px');
					}
	            }
	        });
	    }	
	}
	
	//TISPT-290
	function lazyLoadDivs(){
		//Changes
		
		$(window).on('scroll load',function() {
			lazyLoadfunction();
		});
		//End
		var ctrlKey = false;
		$(document).keydown(function(e) {
	        if (e.keyCode == 17) ctrlKey = true;
	    }).keyup(function(e) {
	        if (e.keyCode == 17) ctrlKey = false;
	    });

	    $(document).keydown(function(e) {
	        if (ctrlKey && (e.which == 109 || e.which == 107 || e.which == 189 || e.which == 187)) {
	        	lazyLoadfunction();
	        }
	    });
		
		
	}
	
	function lazyLoadfunction() {
		
		if ($(window).scrollTop() + $(window).height() >= $('#brandsYouLove').offset().top) {
	        if(!$('#brandsYouLove').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#brandsYouLove').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#brandsYouLove').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	 if (window.localStorage) {
	     		        for (var key in localStorage) {
	     		            if (key.indexOf("brandContent") >= 0) {
	     		                window.localStorage.removeItem(key);
	     		                //console.log("Deleting.." + key);
	     		            }
	     		        }
	     		    }
	     		    getBrandsYouLoveAjaxCall();
	        }
	        }
	}
		if ($(window).scrollTop() + $(window).height() >= $('#promobannerhomepage').offset().top) {
	        if(!$('#promobannerhomepage').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#promobannerhomepage').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#promobannerhomepage').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getPromoBannerHomepage();
	        }
	        }
	}
		if ($(window).scrollTop() + $(window).height() >= $('#bestPicks').offset().top) {
	        if(!$('#bestPicks').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#bestPicks').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#bestPicks').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getBestPicksAjaxCall();
	        }
	        }
	}
		
		if ($(window).scrollTop() + $(window).height() >= $('#productYouCare').offset().top) {
	        if(!$('#productYouCare').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#productYouCare').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#productYouCare').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getProductsYouCareAjaxCall();
	        }
	        }
	}
		if ($(window).scrollTop() + $(window).height() >= $('#newAndExclusive').offset().top) {
	        if(!$('#newAndExclusive').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#newAndExclusive').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#newAndExclusive').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getNewAndExclusiveAjaxCall();
	        }
	        }
	}
		
		if ($(window).scrollTop() + $(window).height() >= $('#stayQued').offset().top) {
	        if(!$('#stayQued').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#stayQued').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#stayQued').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getStayQuedHomepage();
	        }
	        }
	}
		
		
		if ($(window).scrollTop() + $(window).height() >= $('#showcase').offset().top) {
	        if(!$('#showcase').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#showcase').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#showcase').children().length == 0 && $('#pageTemplateId').val() ==
			    'LandingPage2Template') {
			    if (window.localStorage) {
			        for (var key in localStorage) {
			            if (key.indexOf("showcaseContent") >= 0) {
			                window.localStorage.removeItem(key);
			                //console.log("Deleting.." + key);
			            }
			        }
			    }
			    getShowCaseAjaxCall();
			}
	        }
	}
}
$(document).ready(function(){
	//start//
	$('header .content nav > ul > li > div.toggle').on('mouseover touchend',function(){
		$(this).parent().addClass('hovered');
		
		//department/////////////////////
		if($('header .content nav > ul > li:first-child').hasClass('hovered')) {
			var id = $('header .content nav > ul > li.hovered > ul > li:first-child .departmenthover').attr('id');
		    var code = id.substring(4);

		    if (!$.cookie("dept-list") && window.localStorage) {
		        for (var key in localStorage) {
		            if (key.indexOf("deptmenuhtml") >= 0) {
		                window.localStorage.removeItem(key);
		                // console.log("Deleting.." + key);

		            }
		        }
		    }
		    if (window.localStorage && (html = window.localStorage.getItem("deptmenuhtml-" + code)) && html != "") {
		        // console.log("Local");
		        $("ul." + id).html(decodeURI(html));
		        //LazyLoad();
		        
		    } else {
		        $.ajax({
		            url: ACC.config.encodedContextPath +
		                "/departmentCollection",
		            type: 'GET',
		            data: "department=" + code,
		            success: function(html) {
		                // console.log("Server");
		                $("ul." + id).html(html);
		                if (window.localStorage) {
		                    $.cookie("dept-list", "true", {
		                        expires: 1,
		                        path: "/"

		                    });
		                    window.localStorage.setItem(
		                        "deptmenuhtml-" + code,
		                        encodeURI(html));

		                }
		            }
		        });


		    }
		    
		} else {
			if($('header .content nav > ul > li.hovered > ul > li:first-child > div').hasClass('brandClass')) {
			////newbrand
				var componentUid = $('header .content nav > ul > li.hovered > ul > li:first-child > div > a ').attr('id');
				 if (!$.cookie("dept-list") && window.localStorage) {
				        for (var key in localStorage) {
				            if (key.indexOf("brandhtml") >= 0) {
				                window.localStorage.removeItem(key);
				                // console.log("Deleting.." + key);

				            }
				        }
				    }
				 if (window.localStorage && (html = window.localStorage.getItem("brandhtml-" + componentUid)) && html != "") {
				        // console.log("Local");
				        //$("ul#"+componentUid).html(decodeURI(html));
					    $("ul[id='"+componentUid+"']").html(decodeURI(html));
				    }else{
				    	
				    	 $.ajax({
					            url: ACC.config.encodedContextPath +
					            "/shopbybrand",
					            type: 'GET',
					            data:{"compId":componentUid},
					            success: function(html) {
					                //$("ul#"+componentUid).html(html);
					            	$("ul[id='"+componentUid+"']").html(html); 
					                if (window.localStorage) {
					                    $.cookie("dept-list", "true", {
					                        expires: 1,
					                        path: "/"

					                    });
					                    window.localStorage.setItem(
					                        "brandhtml-" + componentUid,
					                        encodeURI(html));

					                }
					                
					            }
					        });
				    	
				    }
			       
				 ////////new brand
			} else if($('header .content nav > ul > li.hovered > ul > li:first-child > div').hasClass('A-ZBrands')) {

				var componentUid = $("#componentUid").val();
			    if ($("li#atozbrandsdiplay").length) {
			        // console.log("Dipslaying A-Z Brands..");

			        if (!$.cookie("dept-list") && window.localStorage) {
			            for (var key in localStorage) {
			                if (key.indexOf("atozbrandmenuhtml") >= 0) {
			                    window.localStorage.removeItem(key);
			                    // console.log("Deleting.." + key);

			                }
			            }
			        }
			        if (window.localStorage && (html = window.localStorage.getItem("atozbrandmenuhtml")) && html != "") {
			            // console.log("Local");
			            if ($("div#appendedAtoZBrands") == null || $(
			                "div#appendedAtoZBrands").length == 0) {
			                $("li#atozbrandsdiplay").append(decodeURI(html));
			            }
			        } else {
			            // console.log("Server");

			            $.ajax({
			                url: ACC.config.encodedContextPath +
			                    "/atozbrands",
			                type: 'GET',
			                data : {
								 "componentUid" : componentUid
								},
			                success: function(html) {
			                    //console.log(html)
			                    if ($("div#appendedAtoZBrands") == null ||
			                        $("div#appendedAtoZBrands").length ==
			                        0) {
			                        $("li#atozbrandsdiplay").append(
			                            html);
			                    }
			                    if (window.localStorage) {
			                        $.cookie("dept-list", "true", {
			                            expires: 1,
			                            path: "/"

			                        });
			                        window.localStorage.setItem(
			                            "atozbrandmenuhtml",
			                            encodeURI(html));
			                    }
			                }
			            });
			        }
			    }

			}
		}
	});
	$(document).on('mouseover touchend','header .content nav > ul > li#shopMicrositeSeller > div.toggle',function(){
		$(this).parent().addClass('hovered');
	});
	$(document).on('mouseleave','header .content nav > ul > li.hovered > ul > li:first-child,header .content nav > ul > li > div.toggle',function(){
		$('header .content nav > ul > li').removeClass('hovered');
	});
	
});


	function forceUpdateHeader(){
		$.ajax({
	        url: ACC.config.encodedContextPath + "/setheader?timestamp="+Date.now(),
	        type: 'GET',
	        cache:false,
	        success: function(data) {
	        	window.sessionStorage.setItem("header" , JSON.stringify(data));
	        	setHeader(data);
	        }
	    });
	}
	
	function setHeader(data){
		   headerLoggedinStatus = data.loggedInStatus;
           //TISPT-197
           if(data.cartcount!='NaN')
       	{
           	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").html(data.cartcount);
       	}
           else
           {
           	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").html('0');
           }
           
           if (!headerLoggedinStatus) {

               $("a.headeruserdetails").html("Sign In");
             //Akamai caching
               $("a.headeruserdetails").attr('href','/login');
               $('#signIn').attr('class','sign-in-info signin-dropdown-body ajaxflyout');
               
               $("a.tracklinkcls").attr('href','/login');
               $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications');
           } else {
               var firstName = data.userFirstName;
               if (firstName == null || firstName.trim() ==
                   '') {
                   $("a.headeruserdetails").html("Hi!");
               } else {
                   $("a.headeruserdetails").html("Hi, " +
                       firstName + "!");
               }
               //Akamai caching
               $('#signIn').attr('class','dropdown-menu dropdown-hi loggedIn-flyout ajaxflyout');
               $("a.headeruserdetails").attr('href','/my-account');
               
               $("a.tracklinkcls").attr('href','#');
               if(data.notificationCount != null){
	               	 $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications&nbsp;(<span >'+data.notificationCount+'</span>)');
	               } else {
	               	 $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications');
	               } 
           }
	}
	
	function userCookieChanged(){
		var mplUserSession = window.sessionStorage.getItem("mpl-user");
        if(window.sessionStorage && (null == mplUserSession || mplUserSession != $.cookie("mpl-user"))){
       	 window.sessionStorage.setItem("mpl-user",$.cookie("mpl-user"));
       	 return true;
        }else{
        	return false;
        }
	}
	
	function forceSetHeader(){
	 var pageType = $("#pageType").val();
	 if(pageType == 'cart' || pageType == 'orderconfirmation' || pageType == 'update-profile'){
		 return true;
	 }
	}

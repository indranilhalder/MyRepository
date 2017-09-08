var product = "";
var tabsLoaded = false;
var stw = {
   renderFlatWidget: function(divElement) {
        var brand = window.location.pathname.split("/")[1];

        stwService.call('', brand);
    },
    renderTabsWidget: function(divElement) {
        var category = "";
        stwService.call(category, '');
    },  
    renderFlatWidgetHF: function(divElement) {
        //var brand = window.location.pathname.split("/")[1];
	   var brand = "";
        stwService.call('', brand);
    },
}
var callbackVar;
var stwService = {

    call: function(category, brand) {
        $.ajax({
            url: '/getStwrecomendations',
            dataType: "json",
            type: "GET",
            data: {
                pageType: 'Home',
                widgetType: 'STW',
                siteType: 'Marketplace',
                sendOnlyListingId: 'false',
                category: category,
                brand: brand
            },
            beforeSend: function() {
                var staticHost = $('#staticHost').val();
                $(".stw-widget-owl").css(
                    "margin-bottom", "120px");
                $("#stw_widget").append(
                    "<div class='loaderDiv' style='z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;'><img src='" + staticHost + "/_ui/responsive/common/images/red_loader.gif'/></div>"
                );
            },
            success: function(json) {
            	if(!$.isEmptyObject(json)){
            		var vistingIp = stwRender.visitingIpAddress(json);
                var isIpAvialable = true;//stwRender.wigetLoaderOnIp(vistingIp);
                if (($("#pageType").val() == "homepage") && (isIpAvialable == true)) {
                	alert('12345');
                    var stw_block = null;
                    if (tabsLoaded) {
                        var carousel = stwRender.carousel(json);
                        $("#stw_widget .carousel-component").html(carousel);
                        stwRender.bindCarousel();
                    } else {
                        var header = stwRender.header(json);
                        var tabs = stwRender.tabs(json);
                        var carousel = stwRender.carousel(json);
                      if(( carousel != undefined && carousel !="") && carousel!=null)
                     // if(carousel)
                       {
                        var stw_block = "<div class='best_seller stw-list'>" + header + tabs + "</div>" + carousel;
                        $("#stw_widget").html(stw_block);
                      	}
                        stwRender.bindCarousel();
                        tabsLoaded = true;
                    }

                } else {
                	
                    var header = stwRender.blpheader(json);
                    
                    if ($("#stw_widget_blp").length)
                    {
                     var carousel = stwRender.carousel(json);
                    }
                    else {
                    
                    	var carousel = stwRender.carouselHF(json);
                    	
                    }
                    
                    if(( carousel != undefined && carousel !="") && carousel!=null)
                    {
	                    var stw_block_Blp = "<div class='best_seller stw-list'>" + header + "</div>" + carousel;
	                    if ($("#stw_widget_blp").length) {
	                    	$("#stw_widget_blp").html(stw_block_Blp);
	                    }
	                    else {
	                    	
	                    	$("#stw_widget_hf").html(stw_block_Blp);
	                    }
                    
                    }
                    stwRender.bindCarousel();
                }
            	}else{
            		console.log("STW disabled from back end.")
            	}
            },
            fail: function() {
                console.log('STW failed to load --' + error);
            },
            complete: function() {
                $('#stw_widget .loaderDiv').remove();
            }
        });
        return callbackVar;
    }
}


var stwRender = {
	    wigetLoaderOnIp: function(ip) {
	    	var flag = true;
	    	//check if more than one ip is coming
	    	if(ip.indexOf(",") != -1){
	    		var ips = ip.split(",");
	    		var lastIp = ips[ips.length-1];
	    		if(lastIp.split(".").length == 4){
	    			var lastIpPart = lastIp.split(".")[3];
	    			if (lastIpPart % 2 == 0) {
		    			delete productWidget[4]; // if even then load STW and delete HOT NOW
					$("#ia_products_hot").empty();
		                return true;
		    		}else{
		    			return false;
		    		}
	    		}
	    	}else if(ip.split(".").length == 4){
	    		var lastIpPart = ip.split(".")[3];
	    		if (lastIpPart % 2 == 0) {
	    			delete productWidget[4]; // if even then load STW and delete HOT NOW
				$("#ia_products_hot").empty();
	                return true;
	    		}else{
	    			return false;
	    		}
	    	}else{
	    		return false;
	    	}
	    },

    visitingIpAddress: function(STWJOBJECT) {
        var visitingIp = STWJOBJECT.visiterIP;
        return visitingIp;
    },
    header: function(STWJObject) {
        var stwWidgetHeading = "";
        stwWidgetHeading += '<div class="best_seller_section hide_clplist">';
        stwWidgetHeading += '<div class="content">' + STWJObject.STWHeading + '</div>';
        stwWidgetHeading += '</div>';
        return stwWidgetHeading;
    },
    blpheader: function(STWJObject) {
        var stwWidgetHeading = "";
        stwWidgetHeading += '<div class="best_seller_section hide_clplist">';
        stwWidgetHeading += '<div class="content">' + STWJObject.STWBlpHeading + '</div>';
        stwWidgetHeading += '</div>';
        return stwWidgetHeading;

    },
    tabs: function(STWJObject) {
        var tabsFormationHtml = "";
        tabsFormationHtml += '<div class="Menu"><div class="mobile selectmenu">ALL</div>';
        tabsFormationHtml += '<ul><li class="active">ALL</li>';
        var allCatCodeNameArray = STWJObject.STWCategories.split(",");

        $.each(allCatCodeNameArray, function(index, value) {
            var categoryCodeNameArray = value.split("=");
            tabsFormationHtml += '<li>' + categoryCodeNameArray[1] + '</li>';
        });
        tabsFormationHtml += '</ul></div>';
        return tabsFormationHtml;
    },
    

    carousel: function(STWJObject) {
    	alert(STWJObject);
    	if(STWJObject !=null && (STWJObject.STWElements !="" && STWJObject.STWElements !=null ))
    		
    	//if(STWJObject.STWElements)
    	{
    	alert(STWJObject);
        var stwWidgetProducts = "";
        stwWidgetProducts += '<div class="carousel-component">';
        stwWidgetProducts += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
        $.each(STWJObject.STWElements, function(index, value) {
            var productId = value.listingId;
            var product = productId.toUpperCase();
            if (null != value.mrp && null != value.mop) {
                var savingPriceCal = (value.mrp - value.mop);
                var savingPriceCalPer = (savingPriceCal / value.mrp) * 100;
                var savingsOnProduct = Math.round((savingPriceCalPer * 100) / 100);
            }
            stwWidgetProducts += '<div class="item slide">';
            stwWidgetProducts += '<div class="product-tile"><li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile stw_widget_list_elements productParentList" style="display: inline-block;position: relative;"><div class="image"><a href="' + value.productUrl + '" class="product-tile"> <img src="' + value.imageUrl + '"></a>';
            stwWidgetProducts += '<div onclick=popupwindow("' + product + '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div></div>';
            stwWidgetProducts += ' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: ' + value.availableColor + ';border: 1px solid rgb(204, 211, 217);" title="' + value.availableColor + '"></span></li></ul>';
            stwWidgetProducts += '<div class="brand">' + value.productBrand + '</div>';
            stwWidgetProducts += ' <a href="' + value.productUrl + '" class="item"><h3 class="product-name">' + value.productName + '</h3>';
            stwWidgetProducts += '<div class="price"><span class="stw-mrp">&#8377;' + value.mrp + '</span><span class="stw-mop">&#8377;' + value.mop + '</span>';
            stwWidgetProducts += '<p class="savings pdp-savings"> <span>(-' + savingsOnProduct + '%)</span></p>';
            stwWidgetProducts += '</div></a></div></li></div></div>';
            
        });
        stwWidgetProducts += '</div></div>';
        return stwWidgetProducts;
        }
    },
    
    carouselHF: function(STWJObject) {
    	alert(STWJObject);
    	if(STWJObject !=null && (STWJObject.STWElements !="" && STWJObject.STWElements !=null ))
    		
    	//if(STWJObject.STWElements)
    	{
    	alert(STWJObject);
        var stwWidgetProducts = "";
        stwWidgetProducts += '<div class="carousel-component">';
        stwWidgetProducts += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference stw-widget-owl">';
        $.each(STWJObject.STWElements, function(index, value) {
            var productId = value.listingId;
            var product = productId.toUpperCase();
            if (null != value.mrp && null != value.mop) {
                var savingPriceCal = (value.mrp - value.mop);
                var savingPriceCalPer = (savingPriceCal / value.mrp) * 100;
                var savingsOnProduct = Math.round((savingPriceCalPer * 100) / 100);
            }
            stwWidgetProducts += '<div class="item slide">';
            stwWidgetProducts += '<div class="product-tile"><li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile stw_widget_list_elements productParentList" style="display: inline-block;position: relative;"><div class="image"><a href="' + value.productUrl + '" class="product-tile"> <img src="' + value.imageUrl + '"></a>';
            stwWidgetProducts += '<div onclick=popupwindow("' + product + '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div></div>';
            stwWidgetProducts += ' <div class="short-info"><ul class="color-swatch"><li><span  style="background-color: ' + value.availableColor + ';border: 1px solid rgb(204, 211, 217);" title="' + value.availableColor + '"></span></li></ul>';
            stwWidgetProducts += '<div class="brand">' + value.productBrand + '</div>';
            stwWidgetProducts += ' <a href="' + value.productUrl + '" class="item"><h3 class="product-name">' + value.productName + '</h3>';
            stwWidgetProducts += '<div class="price"><span class="stw-mrp">&#8377;' + value.mrp + '</span><span class="stw-mop">&#8377;' + value.mop + '</span>';
            stwWidgetProducts += '<p class="savings pdp-savings"> <span>(-' + savingsOnProduct + '%)</span></p>';
            stwWidgetProducts += '</div></a></div></li></div>';
            stwWidgetProducts += '<div class="gotopdp"><a href="'+value.productUrl+'">Go TO PDP</a></div></div>';
            

        });
        stwWidgetProducts += '</div></div>';
        return stwWidgetProducts;
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
            responsive: {
                // breakpoint from 0 up
                0: {
                    items: 1,
                    stagePadding: 50,
                    loop: ($(".stw-widget-owl .stw_widget_list_elements").length <= 1) ? false : true,
                },
                // breakpoint from 480 up
                480: {
                    items: 2,
                    stagePadding: 50,
                    loop: ($(".stw-widget-owl .stw_widget_list_elements").length <= 2) ? false : true,
                },
                // breakpoint from 768 up
                768: {
                    items: 3,
                    loop: ($(".stw-widget-owl .stw_widget_list_elements").length <= 3) ? false : true,
                },
                // breakpoint from 1080 up
                1080: {
                    items: 5,
                    loop: ($(".stw-widget-owl .stw_widget_list_elements").length <= 5) ? false : true,
                }
            }
        });
    }
}
    
$(document).ready(function() {
	
	$(window).on("scroll",function(){
		if($('.lazy-reached-stw').length){
		var hT = $('.lazy-reached-stw').offset().top,
	    hH = $('.lazy-reached-stw').outerHeight(),
		wH = $(window).height(),
	    wS = $(this).scrollTop();
		
		if (!$('#stw_widget').attr('loaded') && $('#stw_widget').length == 1 && wS > (hT + hH - wH)) {
			stw.renderTabsWidget();
	        $('#stw_widget').attr('loaded', true);
		}else if (!$('#stw_widget_blp').attr('loaded') && $('#stw_widget_blp').length == 1 && wS > (hT + hH - wH)) {
	        stw.renderFlatWidget();
	        $('#stw_widget_blp').attr('loaded', true);
	    }
		else if (!$('#stw_widget_hf').attr('loaded') && $('#stw_widget_hf').length == 1 && wS > (hT + hH - wH)) {
	        stw.renderFlatWidgetHF();
	        $('#stw_widget_hf').attr('loaded', true);
	    }
		}
	});
    
    $(document).off("click", ".best_seller.stw-list .Menu ul li").on("click", ".best_seller.stw-list .Menu ul li", function() {

        var category = $(this).text() == 'ALL' ? '' : $(this).text();
        stwService.call(category, '');
        $(".best_seller.stw-list .Menu ul li").removeClass("active");
        $(this).addClass("active");
        var active_text = $(this).text();
        $(".best_seller.stw-list .Menu .mobile.selectmenu").text(active_text);
        $('.best_seller.stw-list .Menu ul').slideUp();
    });
   // $(".best_seller .Menu .selectmenu").off("click").on("click", function() {
    	$(document).off("click", ".best_seller .Menu .selectmenu").on("click", ".best_seller .Menu .selectmenu", function(){
        $(this).next().slideToggle();
    });
});

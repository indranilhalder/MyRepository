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
               this.value = data.token;
           });
           ACC.config.CSRFToken = data.token;
           ACC.config.SessionId = data.sessionId;
           ACC.config.VisitorIp = data.vistiorIp;
           
           var crsfSession = window.sessionStorage.getItem("csrf-token");
           if(window.sessionStorage && (null == crsfSession || crsfSession != data.token)){
          	 csrfDataChanged = true;
          	 window.sessionStorage.setItem("csrf-token",data.token);
           }
           //TISPRD-3357
           callSetHeader();
       }
   });
});
function callSetHeader() {
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
	}
 
$(document).on("mouseover touchend", "div.departmenthover", function() {
    var id = this.id;
    //var code = id.substring(4);

    if (!$.cookie("dept-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("deptmenuhtml") >= 0) {
                window.localStorage.removeItem(key);
                // console.log("Deleting.." + key);

            }
        }
    }

});

	$(document).on("mouseover touchend", ".A-ZBrands", function(e) {
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


$("span.latestOffersBanner").on("click touchend", function() {
	/*TPR-644 START*/
	utag.link(
		{
			link_obj: this, 
			link_text: 'concierge_view_details' , 
			event_type : 'concierge_view_details' 
		});
	/*TPR-644 END*/
	$.ajax({
		url : ACC.config.encodedContextPath + "/listOffers",
		type : 'GET',
		success : function(html) {
			$("div#latestOffersContent").html(html);
		},
		
		complete : function() {
			$(".topConcierge").owlCarousel({
				items:5,
	    		loop: true,
	    		nav:true,
	    		dots:false,
	    		navText:[],
	    		slideBy:'page',
	    		responsive : {
	    			// breakpoint from 0 up
        			0 : {
        				items:1,
        				stagePadding: 50,
        			},
        			// breakpoint from 480 up
        			480 : {
        				items:2,
        				stagePadding: 50,
        			},
        			// breakpoint from 768 up
        			768 : {
        				items:3,
        			},
        			// breakpoint from 768 up
        			1280 : {
        				items:5,
        			}			
	    		}	
				/*navigation:true,
				navigationText : [],
				pagination:false,
				itemsDesktop : [5000,5], 
				itemsDesktopSmall : [1400,5], 
				itemsTablet: [650,1], 
				itemsMobile : [480,1], 
				rewindNav: false,
				scrollPerPage:true*/
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
            //data: "&productCount=" + $(this).attr("data-count"),
            data: "&productCount=" + $('li.wishlist').find('a').attr("data-count"),
            success: function(html) {
                $("div.wishlist-info").html(html);
                /*TPR-844*/
                var wlCode = [];
				$(".wlCode").each(function(){
					wlCode.push($(this).text().trim());
				});
				$(".plpWlcode").each(function(){
					var productURL = $(this).text(), n = productURL.lastIndexOf("-"), productCode=productURL.substring(n+1, productURL.length);
					//wlPlpCode.push(productCode.toUpperCase());
					
					for(var i = 0; i < wlCode.length; i++) {
						if(productCode.toUpperCase() == wlCode[i]) {
							console.log("Controle Inside");
							$(this).siblings(".plp-wishlist").addClass("added");
						}
					}
				});
				/*TPR-844*/
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
    // ENd AJAX CALL


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
//Fix for defect TISPT-202
getFooterOnLoad();

$(document).on("click", ".showcaseItem", function() {
	$('.selectmenu').text($(this).children().text());
	/*TPR-650 Start*/
	//TISQAEE-59
	var name=$(this).parents('#showcase').children('h2').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var value = $(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	utag.link({
		link_obj: this,
		link_text: name+'_'+value,
		event_type : name+'_click'
	});
	/*TPR-650 End*/
});

$(window).on("load resize", function() {
    if ($(window).width() <= 767) {
        $(".showcase-heading").hide();
        $(document).off("click",".selectmenu").on("click",".selectmenu",function() {
            $(".showcase-heading").slideToggle();
        });
        $(document).off("click",".showcase-heading").on("click",".showcase-heading",function() {
            $(this).slideUp();
        });
    } else {
        $(".showcase-heading").show();
        $(document).off("click",".showcase-heading").on("click",".showcase-heading",function() {
        });
    }
});

});

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
   
   //showMobileShowCase();
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
	
//	$("div.toggle.brandClass").on("mouseover touchend", function() {

	//End
	
	// Fix for defect TISPT-202
	
	function openNeedHelpSec()
	{
		if(!$('.gwc-chat-embedded-window').hasClass('minimized')){
			$(this).removeClass("minimize");
			$("#h").toggle();
		}
	}
	function getFooterOnLoad()
	{
		var slotUid = "FooterSlot";
		var pageName = $('#pageName').val();
		if (!$.cookie("dept-list") && window.localStorage) {
	        for (var key in localStorage) {
	            if (key.indexOf("footerhtml") >= 0) {
	                window.localStorage.removeItem(key);                
	            }
	        }
	    }
		
		if (window.localStorage && (html = window.localStorage.getItem("footerhtml")) && html != "" && pageName != "Cart Page") {
			$("#footerByAjaxId").html(decodeURI(html));
	    } else {
	    	if(pageName != "Cart Page")
	    	{
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
	}
	
	
$(document).ready(function()
{
	//start//
	$('header .content nav > ul > li > div.toggle').on('mouseover touchend',function()
	{
		$(this).parent().addClass('hovered');
		
		//department/////////////////////
		if($('header .content nav > ul > li:first-child').hasClass('hovered')) 
		{
			var id = $('header .content nav > ul > li.hovered > ul > li:first-child .departmenthover').attr('id');

		    //var code = id.substring(4);
		    if (!$.cookie("dept-list") && window.localStorage) {
		        for (var key in localStorage) {
		            if (key.indexOf("deptmenuhtml") >= 0) {
		                window.localStorage.removeItem(key);
		                // console.log("Deleting.." + key);

		            }
		        }
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
					                	var date = new Date();
					                	var minutes = 600;
					                	date.setTime(date.getTime() + (minutes * 60 * 1000)); 
					                	
					                    $.cookie("dept-list", "true", {
					                        expires: date,
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
           
           if($("#pageType").val() == 'cart'){
         		
      			$('#mybagcnt').html(data.cartcount);
             		
             	}
           
           if (!headerLoggedinStatus) {

        	   $("a.headeruserdetails").html("Sign In / Sign Up"); /*UF-249 text change*/
             //Akamai caching
               $("a.headeruserdetails").attr('href','/login');
               $('#signIn').attr('class','sign-in-info signin-dropdown-body ajaxflyout');
               
               $("a.tracklinkcls").attr('href','/login');
               $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications');
               $(".dropdown.sign-in-dropdown.sign-in.ajaxloginhi span#mobile-menu-toggle").remove();	/*add for PRDI-409 & PRDI-438*/
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
               /*$("a.headeruserdetails").attr('href','/my-account');*/
               $("a.headeruserdetails").attr('href',''); /*client feedback to remove href*/
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
	 //Fix for INC144315287 & INC144315355
	 if(pageType == 'cart' || pageType == 'orderconfirmation' || pageType == 'update-profile' || pageType == 'homepage'){
		 return true;
	 }
	}
	
	

	
	$(document).on("click","div.departmenthover + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.words").children().length == 0){
			$(this).siblings("div.departmenthover").mouseover();
		}
		
		
	});

	$(document).on("click","div.brandClass + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.images").children().length == 0){
			$(this).siblings("div.brandClass").mouseover();
		}
		
		
	});
	$(document).on("click","div.A-ZBrands + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.a-z div.view_brands").siblings().length == 0){
			$(this).siblings("div.A-ZBrands").mouseover();
		}
		
		
	});
	
	//Added for luxury site starts
	$('document').ready(function(){
	    $('#flip-navigation li a').each(function(){  
	        $(this).click(function(){  
	            $('#flip-navigation li').each(function(){  
	                $(this).removeClass('selected');  
	            });  
	            $(this).parent().addClass('selected');           
	            // Here we get the href value of the selected tab
	            var selected_tab = $(this).find("a").attr("href");             
	            var starting = selected_tab.indexOf("#");
	            var sub = selected_tab.substring(starting);            
	            $(sub).fadeIn();
	            return false;  
	        });  
	    });  
	}); 
	//Added for luxury site ends

	
   //  Change for TISPRD-4587 
	$(document).on("mouseenter",".A-ZBrands",function(){
		
		if($("#A-E").css("display") == "block"){
			
			$("#A-E").css("display","block");
		}
	});
		
		
		$("div.azWrapper").eq(1).children("div#A-E").show();
		$(document).on("mouseenter","div.toggle",function(){
			$("div.azWrapper").eq(1).children("div").hide();
			if($("div.azWrapper").parents("li.lazy-brands").index() === 0){
			$("div.azWrapper").eq(1).children("div#A-E").show();
			}
		});
		$(document).on("mouseenter",".lazy-brands>div.toggle.A-ZBrands,div.azWrapper>div#A-E",function(){
			$("div.azWrapper").eq(1).children("div#A-E").show();
		});
		
		
		$(document).on("mouseenter",".a-z:last div#groups div#group",function(){
			var index = $(this).index();
			$("div.azWrapper").eq(1).children("div").removeClass("showAZBrandsImportant");
			$("div.azWrapper").eq(1).children("div").hide();
			$("div.azWrapper").eq(1).children("div").eq(index).addClass("showAZBrandsImportant");
			$("div.azWrapper").eq(1).siblings("div#groups").children("div#group").children("a.brandGroupLink").removeClass("present").css({"border-bottom-style": "none","font-weight": "400"});
			$(this).children("a.brandGroupLink").addClass("present").css({"border-bottom-width": "3px","border-bottom-style": "solid","font-weight":"bold"}); 
		});
		$(document).on("mouseleave",".a-z:last",function(){
			$("div.azWrapper").eq(1).children("div").removeClass("showAZBrandsImportant");
		})
		$(document).on("mouseenter mousemove","ul.a-z:last",function(){
			if($('.a-z:last div#groups div#group:first a').css("border-bottom-style") == "solid") {
				$(".azWrapper").eq(1).children("#A-E").addClass("importantDisplay")
			} else {
				$(".azWrapper").eq(1).children("#A-E").removeClass("importantDisplay");
			}
		});

	
		 //changes for performance fixof TPR-561 
		$(document).ready(function() {
			//$(".shop_dept").on("mouseover touchend", function(e) {
				//e.stopPropagation();
				if (!$('.shopByDepartment_ajax').children().length){  
					$.ajax({
						url: ACC.config.encodedContextPath +  "/shopbydepartment",
						type: 'GET',
						//cache:false,
						//changes for CAR-224
						cache:true,
						success: function(html) {
							$(".shopByDepartment_ajax").html(html);
						}
					});
				}	
			});
		/*Start TISSQAEE-325*/
		$(document).ajaxComplete(function(){
			paddingAdjust();
		});
		$(window).on("load",function(){
			paddingAdjust();
		});
		
		function paddingAdjust(){
			var ht = $(".mainContent-wrapper footer").height();
			$(".body-Content").css("padding-bottom",ht);
		}
		/*End TISSQAEE-325*/
		
		//TPR-6654
		$(function() {
		$("#homepagePincodeCheck").click(function(){
		var pin = $("#home_pin").val();
		var regExp = /^([1-9])([0-9]){5}$/;
		if (pin == "") {
			$("#errorMessage").html("Please enter a valid pincode");
			$("#errorMessage").css('display', 'inline');
		}
		else if(!regExp.test(pin)){
			$("#errorMessage").html("Please enter a valid pincode");
			$("#errorMessage").css('display', 'inline');
		}
		else
		{
		var requiredUrl = ACC.config.encodedContextPath + "/homePincode";
		var dataString  = "pin=" + pin;
		jQuery
		.ajax({
			type: 'GET',
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(data) {
				$("#pincode-modal").modal('hide');
			  },
		    error : function(data) {
		    	console.log("pincode taken at homepage has failed");
		    }
		  });
	    }
    });
});
		

		

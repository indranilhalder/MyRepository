if ( ! window.TATA )
{
    window.TATA = {};
}
var TATA = window.TATA;

var screenXs="480px";
var screenSm="640px";
var screenMd="1024px";
var screenLg="1400px";

var screenXsMin="480px";
var screenSmMin="640px";
var screenMdMin="1024px";
var screenLgMin="1400px";

var screenXsMax="639px";
var screenSmMax="1023px";
var screenMdMax="1399px";

/*---Start of Checkout Payment tab switching  ----*/		
var paymentModes =  $("#viewPaymentCredit, #viewPaymentDebit, #viewPaymentNetbanking, #viewPaymentCOD, #viewPaymentEMI,#viewPaymentCreditMobile, #viewPaymentDebitMobile, #viewPaymentNetbankingMobile, #viewPaymentCODMobile, #viewPaymentEMIMobile");		
$(window).on('load resize',function(){			
paymentModes.on("click",function(e) {		
	 $('.cart.wrapper .left-block .payments.tab-view ul.tabs').show(200);		
	/*if($(window).width()<651){		
	 $('.cart.wrapper .left-block .payments.tab-view ul.tabs').show(200);		
	 $(this).parents('ul.nav').addClass('hide-menu');		
	 $(this).parents('.left-block').find('h1.payment-options').addClass('hide-menu');		
	 }*/		
	 if(paymentModes.parent().hasClass("active")){		
		 paymentModes.parent().removeClass("active");		
	 }		
	 $(this).parent().addClass("active"); 		
	 $('ul.accepted-cards li').removeClass('active-card');		
});		
/* $('.cart.wrapper .left-block .payments.tab-view .tabs li.change-payment').click(function(){		
	 $(this).parent().hide(200);		
	 $(this).parent().siblings('ul.nav').removeClass('hide-menu');		
	 $(this).parents('.left-block').find('h1.payment-options').removeClass('hide-menu');		
});*/		
});		
if($("#savedCard").css("display") === "block") {		
	 $(".newCardPayment").css("display","none");		
}		
/*---END of Checkout Payment tab switching  ----*/



TATA.CommonFunctions = {
    getUrlParameterByName: function(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }, 
    
    getCorrectErrorMessage: function(errorItem){
    	var errorMsg = errorItem.message;
    	if(errorMsg.indexOf("required") > 0){
        	var fieldName = $(errorItem.element).attr("placeholder");
        	if(fieldName === "Enter Your Email Address"){
        		fieldName = "Email Address";
        	}
        	errorMsg = fieldName + " is required";
    	}
    	return errorMsg;
    },
    
    signInValidate: function() {
        $("#loginForm").validate({
            onfocusout: !1,
            invalidHandler: function(form, validator) {
            	var errorMsg = TATA.CommonFunctions.getCorrectErrorMessage(validator.errorList[0]);
                validator.numberOfInvalids() && (validator.errorList[0].element.focus(), $("#loginForm .invalided-error").length > 0 ? $("#loginForm .invalided-error").html(errorMsg) : $("#loginForm").prepend('<div class="invalided-error">' + errorMsg + "</div>"));
            },
            rules: {
                j_username: {
                    required: !0,
                    email: !0,
                    maxlength: 120
                },
                j_password: {
                    required: !0,
                    maxlength: 30
                }
            },
            submitHandler: function(form) {
                $.ajax({
                    url: "/j_spring_security_check",
                    type: "POST",
                    returnType: "text/html",
                    data: $(form).serialize(),
                    success: function(data) {
                        if(data == 307 && isDuringCheckout){
                            isDuringCheckout = false;
                            window.location.href = "/checkout/multi/delivery-method/choose";
                            return true;
                        }

                        307 == data ? location.reload() : ($("#loginForm .invalided-error").html(""), $("#loginForm .invalided-error").length > 0 ? $("#loginForm .invalided-error").html("Oops! Your email ID and password don't match") : $("#loginForm").prepend("<div class='invalided-error'>Oops! Your email ID and password don't match</div>"), 
                        $("#j_password").val(""));
                    }
                });
            }
        });
    },
    
    signUpValidate: function() {
        $("#extRegisterForm").validate({
            onfocusout: !1,
            invalidHandler: function(form, validator) {
            	var errorMsg = TATA.CommonFunctions.getCorrectErrorMessage(validator.errorList[0]);
                validator.numberOfInvalids() && (validator.errorList[0].element.focus(), $("#extRegisterForm .invalided-error").length > 0 ? $("#extRegisterForm .invalided-error").html(errorMsg) : $("#extRegisterForm").prepend('<div class="invalided-error">' + errorMsg + "</div>"));
            },
            rules: {
                firstName: {
                    required: !0,
                    maxlength: 100
                },
                lastName: {
                    required: !0,
                    maxlength: 30
                },
                mobileNumber: {
                    minlength: 10,
                    maxlength: 10
                },
                email: {
                    required: !0,
                    email: !0,
                    maxlength: 120
                },
                pwd: {
                    required: !0,
                    maxlength: 30,
                    minlength: 8
                },
                checkPwd: {
                    required: !0,
                    maxlength: 30,
                    equalTo: '[name="pwd"]'
                }
            },
            messages:{
            	mobileNumber: {
            		minlength:"Mobile number should contain 10 digit number only.",
            		maxlength:"Mobile number should contain 10 digit number only.",
            	},
            	
            },
            submitHandler: function(form) {
                $.ajax({
                    url: "/login/register",
                    type: "POST",
                    returnType: "text/html",
                    dataType: "html",
                    data: $(form).serialize(),
                    success: function(data) {
                        if("email" != $(data).filter("input#hasErrorsInReg").val() && isDuringCheckout){
                            isDuringCheckout = false;
                            window.location.href = "/checkout/multi/delivery-method/choose";
                            return true;
                        }
                        if ("email" != $(data).filter("input#hasErrorsInReg").val()) return location.reload(), 
                        !1;
                        $("#extRegisterForm .invalided-error").length > 0 ? $("#extRegisterForm .invalided-error").html("Email ID already registered with us. Please Login or use other Email ID.") : $("#extRegisterForm").prepend('<div class="invalided-error">Email ID already registered with us. Please Login or use other Email ID.</div>');
                    }
                });
            }
        });
    }, 
    
    forgotPasswordValidate: function() {
        $("#forgottenPwdForm .invalided-error").html(""), $("#forgottenPwdForm .valid-message").html(""), 
        $("#forgottenPwdForm").validate({
            onfocusout: !1,
            invalidHandler: function(form, validator) {
            	var errorMsg = TATA.CommonFunctions.getCorrectErrorMessage(validator.errorList[0]);
                validator.numberOfInvalids() && (validator.errorList[0].element.focus(), $("#forgottenPwdForm .invalided-error").length > 0 ? $("#forgottenPwdForm .invalided-error").html(errorMsg) : $("#forgottenPwdForm").prepend('<div class="invalided-error">' + errorMsg + "</div>"));
            },
            rules: {
                email: {
                    required: !0,
                    email: !0,
                    maxlength: 120
                }
            },
            submitHandler: function(form) {
                const url = "/login/pw/request/confirmEmail?forgotPassword_email="+$("#forgotPassword_email").val();
                $.ajax({
                    url: url,
                    type:"GET",
                    returnType:"text/html",
                    dataType: "html",
                    success: function(data) {
                        if(data === "invalid_email"){
                            if($("#forgottenPwdForm .invalided-error").length > 0){
                                $("#forgottenPwdForm .invalided-error").html("Oops! This email ID isn't registered with us.");
                            }else{
                                $("#forgottenPwdForm").prepend("<div class='invalided-error'>Oops! This email ID isn't registered with us.</div>");
                            }
                        }
                        if(data === "success"){
                            $("#forgottenPwdForm").prepend("<div class='valid-message'>You've got an email.</div>");
                        }
                    }
                });
            }
        });
    },

    loadSignInForm: function(element){
        const loginURL = element.attr("data-href");
        $.ajax({
            url: loginURL,
            beforeSend: function(){
                $("#login-container .header-sign-in").html('<div class="luxury-loader"></div>');
            },
            success: function(data) {
                $("#login-container .header-sign-in").html(data), $("#header-account").addClass("active"),
                    $("body").removeClass("menu-open");
            }
        });
    },
    
    loadRegisterForm: function(element){
        const luxRegister = element.attr("href");
        $.ajax({
            url: luxRegister,
            beforeSend: function(){
                $("#login-container .header-signup").html('<div class="luxury-loader"></div>');
            },
            success: function(data) {
                $("#login-container .header-signup").html(data);
            }
        });
    },
    
    loadForgotPasswordForm: function(element){
        const pwsRequest = element.attr("href");
        $.ajax({
            url: pwsRequest,
            beforeSend: function(){
                $("#login-container .header-forget-pass").html('<div class="luxury-loader"></div>');
            },
            success: function(data) {
                $("#login-container .header-forget-pass").html(data);
            }
        });
    },

    Toggle: function() {

		/*$('.toggle-link').on('click', function(e){
		 e.preventDefault();
		 e.stopPropagation();
		 var Target = $(this).data("target-id");
		 var elem = $(this);
		 if(elem.hasClass("luxury-login")){
		 TATA.CommonFunctions.loadSignInForm(elem);
		 }
		 $('.toggle-skip').not(Target).removeClass('active');
		 $(Target).toggleClass('active');
		 return false;
		 });*/

        $('.toggle-link').on('click', function(e){
            e.preventDefault(), e.stopPropagation();
            var elem = $(this);
            var Target = $(this).data("target-id");
            if(elem.hasClass("luxury-login")){
                TATA.CommonFunctions.loadSignInForm(elem);
                $('#mypopUpModal').modal();
                $('.modal-backdrop').on("click", function(){
                    $('#mypopUpModal').modal('hide');
                });
            } else {
                isDuringCheckout = false;
                $('.toggle-skip').not(Target).removeClass('active');
                $(Target).toggleClass('active');
                if( $(Target).hasClass("header-search")) {
                    $("#js-site-search-input").focus();
                }
                return false;
            }

        });

    },

    DocumentClick: function() {

        var elem = $('.toggle-skip');

        $(document).on("click", function(event){
            elem.removeClass('active');
            setTimeout(function(){
                $('#header-account').removeClass('active-sign-in active-sign-up active-forget-password');
            },500);
        });

        elem.click(function(e){
            e.stopPropagation();
            e.preventDefault();
            var element = $(e.target);
            if(element.hasClass("register_link")){
                TATA.CommonFunctions.loadRegisterForm(element);
            }else if(element.hasClass("js-password-forgotten")){
                TATA.CommonFunctions.loadForgotPasswordForm(element);
            }else if(element.hasClass("header-signInButton")){
                TATA.CommonFunctions.signInValidate();
                $("#loginForm").submit();
            }else if(e.target.id == "luxury_register"){
                $(".invalided-error").html("");
                TATA.CommonFunctions.signUpValidate();
                $("#extRegisterForm").submit();
            }else if(e.target.id == "luxuryForgotPasswordByEmailAjax"){
                TATA.CommonFunctions.forgotPasswordValidate();
                $("#forgottenPwdForm").submit();
            }

            if(element.hasClass("toggle-btn")){
                var id = element.attr("for");
                $(".toggle").removeAttr("checked");
                $("#"+id).attr("checked", "checked");
                var genderValue = $("#"+id).val();
                $('#gender').val(genderValue);
            }

            if(element.hasClass("header-login-target-link")){
                var targetID = element.data('target-id');
                $('#header-account').removeClass('active-sign-in active-sign-up active-forget-password').addClass('active-'+targetID);
            }
        });
    },

    MainBanner: function() {
    	var $seastionSec = $('.seastion-sec .cms_disp-img_slot');
    	$seastionSec.html($.trim($seastionSec.html()));    	
    	
    	/*var $brandSliderWrapper = $('.brand-slider-wrapper');
    	$brandSliderWrapper.html($.trim($brandSliderWrapper.html()));*/

        $('.main-banner').slick({
            arrows: false,
            dots: true
        });

    },

    LookBookSlider: function() {

        $('.look-book-carousel').slick();

    },
    

    BrandSlider: function() {

        $('.brand-slider').slick({
            infinite: true,
            slidesToShow: 3,
            slidesToScroll: 3,
            dots: true,
            responsive: [
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1.5,
                        slidesToScroll: 1,
                        //centerMode: true,
                        arrows: false,
                        dots:false
                    }
                }
            ]
        });

    },

    Accordion: function() {
        var Acc = $('.accordion').find('.accordion-title');

        $(document).on('click','.accordion .accordion-title', function(){
            //Expand or collapse this panel
            $(this).toggleClass('active').next().stop().slideToggle(500);
            Acc.not($(this)).removeClass('active');
            //Hide the other panels
            $(".accordion-content").not($(this).next()).stop().slideUp(500);
            return false;
        });
    },

    ShopByCatagorySlider: function() {
        if($(window).width() >=768){
            $('.shop-by-catagory-slider').slick({
                slidesToScroll: 6,
                slidesToShow: 6,
                variableWidth: false,
                infinite: false,
                arrows: false,
                swipe: false,
                dots: false
            });
        }
    },    
    
    TrendingCatagorySlider: function() { 
        
        $('.trending-products-catagory').slick({
            slidesToScroll: 5,
            slidesToShow: 5,
            variableWidth: false,
            infinite: false,
            arrows: true,
            swipe: false,
            dots: false,
            infinite:true,
            
            responsive: [
                {
                breakpoint: 768,
                    settings: {
                    slidesToScroll: 1,
                    slidesToShow: 1,
                    infinite: true,
                    swipe: true,
                    arrows: false,
                    variableWidth: true                      
                    }
                }
            ]
        });
    },

    fillHeartForItemsInWishlist: function(){
        var wlCode = [];
        $(".wlCode").each(function(){
            wlCode.push($(this).text().trim());
        });
        $(".plpWlcode").each(function(){
            var productURL = $(this).text(), n = productURL.lastIndexOf("-"), productCode=productURL.substring(n+1, productURL.length);

            for(var i = 0; i < wlCode.length; i++) {
                if(productCode.toUpperCase() == wlCode[i]) {
                    console.log("Controle Inside");
                    $(this).siblings(".add-to-wishlist").addClass("added");
                }
            }
        });
        if($("#ia_product_code").length > 0 && wlCode.indexOf($("#ia_product_code").val()) > -1){
            $(".add-to-wl-pdp").addClass("added");
        }
    },
    
    deleteWishlist : function(){
    	$('.delete_wishlist').click(function(){
    		$('#manageMyList').modal('hide');
    		$('#deleteConfirmation .deleteWlConfirmationNo').css('display','inline-block');
    	});
    },
    
    wishlistInit: function(){
        TATA.CommonFunctions.luxuryForceUpdateHeader();
        $(document).on("click",".add-to-wishlist",function(){
            if ($(this).hasClass("added")){
                TATA.CommonFunctions.removeFromWishlist($(this).data("product"),this);
            } else {
                TATA.CommonFunctions.addToWishlist($(this).data("product"),this);
            }
        });

        var wishlistHover;

        $("a#myWishlistHeader").on("mouseover touchend", function(e) {
            e.stopPropagation();
            wishlistHover = setTimeout(function(){
                $.ajax({
                    url: ACC.config.encodedContextPath + "/headerWishlist",
                    type: 'GET',
                    //data: "&productCount=" + $(this).attr("data-count"),
                    data: "&productCount=" + $('li.wishlist').find('a#myWishlistHeader').attr("data-count"),
                    success: function(html) {
                        $("div.wishlist-info").html(html);
						/*TPR-844*/
                        TATA.CommonFunctions.fillHeartForItemsInWishlist();
						/*TPR-844*/
                    }
                });
            },300);

        });

        $("a#myWishlistHeader").on("mouseleave", function() {
            clearTimeout(wishlistHover);
        });
    },

    luxuryForceUpdateHeader: function(){
        
        $.ajax({
            url: ACC.config.encodedContextPath + "/setheader?timestamp="+Date.now(),
            type: 'GET',
            cache:false,
            success: function(data) {
                window.sessionStorage.setItem("header" , JSON.stringify(data));
                luxuryHeaderLoggedinStatus = data.loggedInStatus;
                TATA.CommonFunctions.setHeader(data);
            }
        });
    },
    
    setHeader: function(data){		
        if(data.cartcount==0){
            $("span.js-mini-cart-count,span.js-mini-cart-count-hover, span.responsive-bag-count").hide();
        }
        else
        {
        	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").show();
        	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").html(data.cartcount);
        }
     
	},

    urlToProductCode : function(productURL){
        var n = productURL.lastIndexOf("-");
        var productCode=productURL.substring(n+1, productURL.length);
        return productCode.toUpperCase();
    },

    addToWishlist: function(productURL, element){
        if(!luxuryHeaderLoggedinStatus) {
            $(".luxury-login").trigger("click");
            return false;
        }
        var productCode = TATA.CommonFunctions.urlToProductCode(productURL);
        var productarray = [];
        productarray.push(productCode);
        var wishName = "";
        var requiredUrl = ACC.config.encodedContextPath + "/search/addToWishListInPLP";
        var sizeSelected=true;

        if(!$('#variant li').hasClass('selected') || $("#variant,#sizevariant option:selected").val()=="#") {
            sizeSelected=false;
        }
        var ussid = $(element).attr("data-ussid");
        var dataString = 'wish=' + wishName + '&product=' + productCode + '&ussid=' + ussid +'&sizeSelected=' + sizeSelected;

        $.ajax({
            contentType : "application/json; charset=utf-8",
            url : requiredUrl,
            data : dataString,
            dataType : "json",
            success : function(data){
                if (data == true) {
                    $(".wishAddSucessPlp").addClass("active");
                    setTimeout(function(){
                        $(".wishAddSucessPlp").removeClass("active")
                    },3000)
                    $(element).addClass("added");
                }
                else{
                    $(".wishAlreadyAddedPlp").addClass("active");
                    setTimeout(function(){
                        $(".wishAlreadyAddedPlp").removeClass("active")
                    },3000)
                }
                $(element).addClass("added");
                if($('#pageType').val() == "productsearch"){
                    if(typeof utag !="undefined"){
                        utag.link({link_text: "add_to_wishlist_serp" , event_type : "add_to_wishlist_serp" ,product_sku_wishlist : productarray});
                    }
                } else {
                    if(typeof utag !="undefined"){
                        utag.link({link_text: "add_to_wishlist_plp" , event_type : "add_to_wishlist_plp" ,product_sku_wishlist : productarray});
                    }
                }

            },
            error : function(xhr, status, error){
                alert(error);
                if(typeof utag !="undefined"){
                    utag.link({error_type : 'wishlist_error'});
                }
            }
        });

        setTimeout(function() {
            $('a.wishlist#wishlist').popover('hide');
            $('input.wishlist#add_to_wishlist').popover('hide');
        }, 0);
    },

    removeFromWishlist : function(productURL,element) {
        var productCode = TATA.CommonFunctions.urlToProductCode(productURL);
        var wishName = "";
        var requiredUrl = ACC.config.encodedContextPath + "/search/removeFromWishListInPLP";
        var sizeSelected=true;

        if(!$('#variant li').hasClass('selected') || $("#variant,#sizevariant option:selected").val()=="#") {
            sizeSelected=false;
        }
        var ussid = $(element).attr("data-ussid");
        var dataString = 'wish=' + wishName + '&product=' + productCode + '&ussid=' + ussid + '&sizeSelected=' + sizeSelected;

        if(!luxuryHeaderLoggedinStatus) {
            $(".wishAddLoginPlp").addClass("active");
            setTimeout(function(){
                $(".wishAddLoginPlp").removeClass("active")
            },3000)
            return false;
        } else {
            $.ajax({
                contentType : "application/json; charset=utf-8",
                url : requiredUrl,
                data : dataString,
                dataType : "json",
                success : function(data){
                    if (data == true) {
                        $(".wishRemoveSucessPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishRemoveSucessPlp").removeClass("active")
                        },3000)
                        $(element).removeClass("added");
                    }
                    else{
                        $(".wishAlreadyAddedPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishAlreadyAddedPlp").removeClass("active")
                        },3000)
                    }
                    $(element).removeClass("added");
                },
                error : function(xhr, status, error){
                    alert(error);
                }
            });

            setTimeout(function() {
                $('a.wishlist#wishlist').popover('hide');
                $('input.wishlist#add_to_wishlist').popover('hide');

            }, 0);
        }
        return false;
    },

    leftBarAccordian: function(){
        if($(window).width() >=768){
			/*$('.facet:first').find('.allFacetValues').show();*/
        	 /*$(".facet").addClass("open");*/
            
            $(document).on('click', '.facetHead', function(e){
                e.stopPropagation();
                $(this).closest('.facet').toggleClass('open', function(){
                    /*$(this).find('.allFacetValues').slideToggle();*/
                });
            });
        }else{
			/*$('.facet:first').removeClass('open');*/
            $(document).on('click', '.facetHead', function(e){
                e.stopPropagation();
                $(this).closest('.facet').addClass('open').find('.allFacetValues').show();
                $(this).closest('.facet').siblings().removeClass('open').find('.allFacetValues').hide();
            });
        }
    },

    WindowScroll: function() {

        var winWidth = $(window).width();
        $(window).scrollTop() > 1 ? $("body").addClass("sticky-nav") : $("body").removeClass("sticky-nav");

        var $window = $(window),
            leftbarElelTop = $('.plp-banner').outerHeight()+$('header').outerHeight();
        if(winWidth >= 768 && $('.leftbar').length){
            if($(window).scrollTop() >= $('.product-grid:last-child').offset().top - 150){
                $('.leftbar').removeClass('sticky-leftbar');
            }else{
                $('.leftbar').toggleClass('sticky-leftbar', $window.scrollTop() > leftbarElelTop);
            }
        }

    },

    Header: {
        MobileMenu: function() {
        	if($(window).width() < 768){
	        	$(".mega-menu li span").each(function() {
	    	    	$(this).prev().css('pointer-events','none');
	        	});
	        	$(".mega-menu > li ").on("click",function(){
	        		$(".mega-menu li span").each(function() {
	        	    	$(this).prev().css('pointer-events','none');
	            	});
	        		$(".mega-menu > li ").each(function(){
	        			$(".sub-menu-toggle",this).first().removeClass("active").next(".sub-menu").removeClass("active");
	        		});
	        		$(this).addClass("parent");
					$(".sub-menu-toggle",this).first().addClass("active").next(".sub-menu").addClass("active");
					$(".sub-menu-inner").addClass("open-inner-menu");
					$("a:first-child",this).css('pointer-events','auto');
	        	});
	        	
	        	$(document).on("click", ".open-inner-menu li", function() {
	        		$(".open-inner-menu li").each(function(){
	        			$(".sub-menu-toggle",this).first().removeClass("active").next(".sub-menu").removeClass("active");
	        		});
	        		$(".sub-menu-toggle",this).first().toggleClass("active").next(".sub-menu").toggleClass("active");
	        		$("a",this).css('pointer-events','auto');
	        		$(this).closest('.parent').children(":first").css('pointer-events','auto');
	        		
	        	});
        	}
            $("#hamburger-menu").on("click", function() {
                $("body").addClass("menu-open");
            }), $("#main-nav-close").on("click", function() {
                $("body").removeClass("menu-open");
            }); 
            $('#main-nav .sub-menu-toggle').on('click', function(){
                $(this).toggleClass('active').next('.sub-menu').toggleClass('active');
            });
        },
        HeaderMinicart: function() {
            $("header .mini-bag").hide(), $("header .mini-cart-link,header #myWishlistHeader").html(""), 
            $(window).width() >= 768 && $("header .bag").hover(function() {
                $(this).find(".mini-bag").show();
            }, function() {
                $(this).find(".mini-bag").hide();
            });
        },
        init: function() {
            this.MobileMenu(), this.HeaderMinicart();
        }
    },

    Footer: function() {
        if($(window).width() >= 768){
            $('footer').find('.accordion').removeClass('accordion');
            $('footer').find('.accordion-content').removeClass('accordion-content');
        }
        $('#main-nav > ul.mega-menu').addClass('footer-cloned-ul').clone().appendTo('.footer-popular-search');
        $('.footer-popular-search .footer-cloned-ul > li').append('<br/>');
        $('.footer-popular-search .footer-cloned-ul > li').each(function(){
            if($(this).find('.sub-menu').length){
                $(this).show();
            }
            else{
                $(this).hide();
            }
        });
        var Acc1 = $('.accordion').find('h3');

        $(document).on('click','.accordion h3', function(){
            //Expand or collapse this panel
            $(this).toggleClass('active').next().stop().slideToggle(500);
            Acc1.not($(this)).removeClass('active');
            //Hide the other panels
            $(".accordion-content").not($(this).next()).stop().slideUp(500);
            return false;
        });
    }, 
    
    deliveryaddressform: function() {

        $("#address-form").click(function(){
            $.ajax({
                url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/new-address",
                type: "GET",
                cache: false,
                dataType: "html",
                success : function(response) {
                    //alert('here');
                    $(".addnewAddresPage").html(response);
                },
                error : function(resp) {
                }

            });
        });
    },
    
    swipeLookBook: function() {
		$(window).resize(function(){		
		      if($(window).width() < 768) {
		    	  $('.mob-slicker').slick({
				slide: 'li',
				arrows: false,    		
				responsive: [{	
		  	      	breakpoint: 767,
		  	      	settings: {
			  	        slidesToShow: 2.5,
			  	        slidesToScroll: 1
		  	      	}	
		  	    }]
			});
		  }
		});    	
    },
    
   removeProdouct:function(){
    	$(".lux-remove-entry-button").on("click",function() {
    		  var productRemoved = $(this).closest("li.item").prev().find("input[name='productCodeMSD']").val(); 
    		  var productArray = [];
    		  productArray.push(productRemoved);
    				var x = $(this).closest("li.item").prev();
    		        var productCodeMSD;
    		        var salesHierarchyCategoryMSD;
    		        var basePriceForMSD;
    		        var rootCategoryMSD;
    		        var subPriceForMSD;
    		        
    		        var y = x.find("input").each(function(index ){
    		        	if(index == '0')
    		        		{
    		        		salesHierarchyCategoryMSD = $(this).attr("value");
    		        		}
    		        	else if(index == '1')
    		        		{
    		        		rootCategoryMSD = $(this).attr("value");
    		        		}
    		        	
    		        	else if(index == '2')
    	       		{
    		        		productCodeMSD = $(this).attr("value");
    	       		}
    		        	
    		        	else if(index == '3')
    	       		{
    		        		basePriceForMSD = $(this).attr("value");
    	       		}
    		        	
    		        	else if(index == '4')
    	       		{
    		        		subPriceForMSD = $(this).attr("value");
    	       		}
    		        	
    		        });
    				var entryNumber1 = $(this).attr('id').split("_");
    				var entryNumber = entryNumber1[1];
    				var form = $('#updateCartForm' + entryNumber[1]);
    				var entryUssid = entryNumber1[2];
    				localStorage.setItem("remove_index", entryNumber); //add for TISPRD-9417
    				$.ajax({
    	              
    					url : ACC.config.encodedContextPath
    							+ "/cart/removeFromMinicart?entryNumber="
    							+ entryNumber,
    					type : 'GET',
    					cache : false,
    					success : function(response) {
    						window.location.reload();
    					},
    					error : function(resp) {
    						console.log(resp);

    					}
    				});
    			});
    },
    displayRemoveCoupon:function(){
    	if ($("#currentPageName").val()=="selectPage" || $("#currentPageName").val()=="choosePage"){
     	$.ajax({
            url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/applyPromotions",
            type: "GET",
            data: { 'paymentMode' : "" , 'bankName' :""  , 'guid' : ""},
            returnType: "text/html",
            dataType: "json",
            success: function(response) {
            		if(response.voucherDiscount.couponDiscount!=null){
            			document.getElementById("couponValue").innerHTML=response.voucherDiscount.couponDiscount.formattedValue;
            			$("#couponApplied").css("display","block");
            			$('#couponFieldId').attr('readonly', true);
            		}
	 				if($("#couponFieldId").val()=="")
	 					{
	 						$("#couponFieldId").val(response.voucherDiscount.voucherCode);
	 					}
	 				$("#couponMessage").html("Coupon application may be changed based on promotion application");
	 				$('#couponMessage').show();
	 				$('#couponMessage').delay(5000).fadeOut('slow');
	 				setTimeout(function(){ $("#couponMessage").html(""); }, 10000);
		
            	} 
     		});
    	}
   },
    
    init: function () {

        var _self = TATA.CommonFunctions;

        _self.Header.init();
        _self.Footer();
        _self.Toggle();
        _self.DocumentClick();
        _self.WindowScroll();
        _self.MainBanner();
        _self.LookBookSlider();
        _self.BrandSlider();
        _self.Accordion();
        _self.ShopByCatagorySlider();
        _self.TrendingCatagorySlider();
        _self.wishlistInit();
        _self.deleteWishlist();
        _self.leftBarAccordian();
        _self.deliveryaddressform();
        _self.swipeLookBook();  
        _self.removeProdouct();
        _self.displayRemoveCoupon();
    }

};





TATA.Pages = {

    // PLP Page
    PLP: {
        loadMoreInit: function(){
            var pathName = window.location.pathname;
            var query = window.location.search;
            var pageType = $('#pageType').val();
            totalNoOfPages = parseInt($('input[name=noOfPages]').val());
            totalNoOfPages == '' ? 1 : totalNoOfPages;
            currentPageNo = 1;
            $(document).on('click','.loadMore', function() {
                pageQuery=$("#pageQuery").val();

                if(pageQuery == ""){
                    if(pageType == 'productsearch'){
                        url = $('#searchPageDeptHierTreeForm').serialize();
                    }else{
                        url = $('#categoryPageDeptHierTreeForm').serialize();
                    }
                    pageQuery = url+TATA.Pages.PLP.addSortParameter();
                }

                if (pageQuery != "" && /page-[0-9]+/.test(pageQuery)) {
                    pageQueryString = pageQuery.match(/page-[0-9]+/);
                    prevPageNoString = pageQueryString[0].split("-");
                    prevPageNo = parseInt(prevPageNoString[1]);
                    currentPageNo = prevPageNo+1;
                    ajaxUrl = pageQuery.replace(/page-[0-9]+/, 'page-' + currentPageNo);
                } else {
                    currentPageNo++
                    ajaxUrl = pathName.replace(/[/]$/,"") + '/page-' + currentPageNo + "?" + pageQuery;
                }
                if (currentPageNo <= totalNoOfPages) {
                    TATA.Pages.PLP.performLoadMore(ajaxUrl);
                    if (currentPageNo == totalNoOfPages){
                        $(this).hide();
                    }
                }
            });
        },
        sortInit : function(){
            $(document).on('change','.responsiveSort',function(){
                TATA.Pages.PLP.performSort();
            });
        },

        addSortParameter: function(){
            var item = $(".responsiveSort option:selected").val();
            var url = "";
            switch (item) {
                case 'relevance':
                    url = url+'&sort=relevance';
                    break;
                case 'new':
                    url = url+'&sort=isProductNew';
                    break;
                case 'discount':
                    url = url+'&sort=isDiscountedPrice';
                    break;
                case 'low':
                    url = url+'&sort=price-asc';
                    break;
                case 'high':
                    url = url+'&sort=price-desc';
                    break;
            }
            return url;
        },

        performSort : function (){
            var pathName = window.location.pathname;
            var pageType = $('#pageType').val();
            pathName = pathName.replace(/page-[0-9]+/, 'page-1');
            if(pageType == 'productsearch'){
                url = $('#searchPageDeptHierTreeForm').serialize();
            }else{
                url = $('#categoryPageDeptHierTreeForm').serialize();
            }
            var queryUrl = url+TATA.Pages.PLP.addSortParameter();
            TATA.Pages.PLP.performAjax(pathName +'?'+queryUrl);
        },
        performLoadMore : function(ajaxUrl){

            $('body').addClass('loader');

            $.ajax({
                url: ajaxUrl,
                data: {
                    pageSize: 24,
                    lazyInterface:'Y',
                    icid:''
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
                    if($(filtered).has('.product-grid')){
                        $(".product-grid",filtered).each(function(){
                            $(".product-grid-wrapper").append($(this));
                        });
                    }
                    $("#pageQuery").val(ajaxUrl);
                },
                complete: function(){
                    $('body').removeClass('loader');
                }
            });
        },        
        showSelectedRefinements: function() {
            if($(".facetValues .facet-form input:checked").length == 0){
                // $(".plp-wrapper h4.categor-name").show();
            }
            if($(window).width() >= 768) {
                $(".facetValues .facet-form input:checked").each(function () {
                    $(this).parents(".allFacetValues").show(), $(this).parents(".facet").addClass("open");
                });
            }
            $('.facet-form input[type=checkbox]').each(function () {
                var colorString = $(this).attr("data-colour");
                var colorArray = colorString.split("_");
                var colorCode = "#"+colorArray[1];
                $(this).next('label').append("<span class='plp-filter-color'></span>");
                $(this).next('label').find('.plp-filter-color').css('background-color',colorCode);

            });

        },
        filterByFacet: function() {
            $(document).on("click", ".reset-filters", function() {
                var resetUrl = $(this).data("resetqueryurl") + TATA.Pages.PLP.addSortParameter();
                TATA.Pages.PLP.performAjax(resetUrl);
            }), $(document).on("click", ".remove-filter", function() {
            	var removeFacetUrl = $(this).attr("data-removeUrl");
            	TATA.Pages.PLP.performAjax(removeFacetUrl, "");
            }), $(document).on("change", ".facet-form input:checkbox", function() {
                var requestUrl = $(this).closest("form").attr("action") + "?" + $(this).closest("form").serialize();
                /* TCL-843  $(".plp-wrapper h4.categor-name").hide();*/
                TATA.Pages.PLP.performAjax(requestUrl, $(this).closest('.facet').children('.facetHead').attr('id'));
            });
        },
        performAjax: function(requestUrl, facetHeadId) {

            $('body').addClass('loader');

            $.ajax({
                url: requestUrl,
                data: {
                    lazyInterface: "Y",
                    icid:''
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
                    $(filtered).has("h4.categor-name") && $(".plp-wrapper h4").html($(filtered).find("h4.categor-name"));
                    $(filtered).has(".filterblocks") && ($(".filterblocks").html($(filtered).find(".filterblocks")),
                        TATA.Pages.PLP.showSelectedRefinements()), $(filtered).has(".product-grid") && $(".product-grid-wrapper").html($(filtered).find(".product-grid-wrapper").html());
                    if($(filtered).has('input[name=noOfPages]')){
                        totalPages = parseInt($(filtered).find('input[name=noOfPages]').val());
                        if(totalPages > 1){
                            $("#pageQuery").val("");
                            currentPageNo = 1;
                            totalNoOfPages = totalPages;
                            $(".loadMore").show();
                        }else{
                            $(".loadMore").hide();
                        }
                    }
                    TATA.Pages.PLP.productHover();
                },
                complete: function(){
                    $('body').removeClass('loader');
                    $('.plp-leftbar-close a').on('click', function(){
                        $('.leftbar').removeClass('active');
                        $('.facet').removeClass('open')
                    });
                    if(typeof facetHeadId != 'undefined' && facetHeadId !== null && facetHeadId !== '') {
                        $('#'+facetHeadId).click();
                    }
                }
            });
            
            //Update URL without Refresh
            console.log("replace the current URL with parameters");
            var currentURL=window.location.href.split('?')[0];
            console.log(requestUrl.split('?')[1]);
            console.log("current URL is "+currentURL);
            history.pushState(null, null, currentURL+"?"+requestUrl.split('?')[1]);
        },

        Filtershow: function() {
            $('.plp-mob-filter').on('click', function(){
                $('.leftbar').addClass('active');
                $('.facetHead').find('a').first().click();
            });
            $('.plp-leftbar-close a').on('click', function(){
                $('.leftbar').removeClass('active');
                $('.facet').removeClass('open')
            });
        },

        showModelImg: function(){
            $('.plp-modelimg-show').on('click', function(){
                $('.plp-model-img').show().siblings().hide();
            });
            $('.plp-productimg-show').on('click', function(){
                $('.plp-model-img').hide().siblings('.plp-default-img').show();
            });

        },

        addGiftWrap: function() {
            $('#addGiftWrap').on('change', function(){
                if($(this).is(':checked')) {
                    $(this).parents('.addGiftBlock').addClass('giftWrapAdded');
                } else {
                    $(this).parents('.addGiftBlock').removeClass('giftWrapAdded');
                }
            });
        },

        TwoColumnseperator: function(){
            $('.grid-count-two').on('click', function(){
                $('.product-list-wrapper').addClass('twocolumngrid');
            });
            $('.grid-count-three').on('click', function(){
                $('.product-list-wrapper').removeClass('twocolumngrid');
            });
        },
        PLPsingleColumseperator:function(){
            $('.plp-single-view').on('click', function(){
                $('.product-list-wrapper').toggleClass('plpsinglecolumn');
            });

        },

        ProductSort: function(){
            $('.sort-wrapper .btn').on('click', function(){
                $(this).addClass('active').siblings().removeClass('active');
            });
        },

        productGrid: function(){
            $('.grid-seperator').on('click', function(){
                $(this).addClass('active').siblings().removeClass('active');
            });

        },

        productHover: function(){
            $(".product-grid").hover(function () {
                    if(!$(this).find('.plp-hover-img').hasClass("error")){
                        $(this).addClass("hover");
                        $(this).find('.plp-hover-img').show().siblings().hide();
                    }
                },
                function () {
                    $(this).removeClass("hover");
                    if($('.plp-modelimg-show.active').length){
                        $(this).find('.plp-model-img').show().siblings().hide();
                    }else{
                        $(this).find('.plp-default-img').show().siblings().hide();
                    }
                });
        },

        // PLP Page initiate
        init: function () {
            var _self = TATA.Pages.PLP;
            var pageNoPaginationNew = 1;
            var totalNoOfPages = 0;

            _self.Filtershow();
            _self.showModelImg();
            _self.addGiftWrap();
            _self.TwoColumnseperator();
            _self.PLPsingleColumseperator();
            _self.ProductSort();
            _self.productGrid();
            _self.productHover();
            _self.filterByFacet();
            _self.showSelectedRefinements();
            _self.sortInit();
            _self.loadMoreInit();

        }
    },

    LANDING: {
        owlCarosel_customize: function (){
            if($(window).width() <= 767){
                $('.sort-by-fature .selectboxit-text').html('SORT');
            }
            $(".luxgender-carousel .js-owl-carousel").owlCarousel({
                dots: true,
                loop:true,
                merge:true,
                responsive:{
                    0:{
                        items:1
                    },
                    768:{
                        items:4,
                    }
                },
            });

            $(".lux-main-banner-slider .electronic-rotatingImage").owlCarousel({
                dots: true,
                loop:true,
                items: 1,
                autoplay: true,
                autoplayTimeout: 5000,
            });
        },
        init: function () {
            var _self = TATA.Pages.LANDING;
            _self.owlCarosel_customize();
        }
    },

	/*  CHECKOUT:  {

	 Netbanking: function(){

	 $("payment-tab #viewPaymentNetbanking").on('click', function(){

	 $(".net-bank.netbankingPanel ul li").addClass("le-radio");
	 });
	 },

	 init: function () {
	 var _self = TATA.Pages.CHECKOUT;
	 _self.Netbanking();
	 }
	 },
	 */

    PDP:  {

        wishlistInit: function(){
            $(document).on("click touchstart","#OutOfStockAddToWishList",function(){
                if(!$(".add-to-wl-pdp").hasClass("added")){
                    $(".add-to-wl-pdp").trigger("click");
                }
            });
            $(document).on("click touchstart",".add-to-wl-pdp",function(){
                if(!luxuryHeaderLoggedinStatus) {
                    $(".luxury-login").trigger("click");
                    return false;
                }

                var dataString = TATA.Pages.PDP.getDataString();

                if ($(this).hasClass("added")){
                    TATA.Pages.PDP.removeFromWishlist(dataString);
                } else {
                    var sizeSelected=true;
                    if(!$('#variant li').hasClass('selected') || $("#variant,#sizevariant option:selected").val()=="#") {
                        sizeSelected=false;
                    }
                    dataString = dataString + '&sizeSelected=' + sizeSelected;
                    TATA.Pages.PDP.addToWishlist(dataString);
                    var productCodePost = $("#productCodePost").val();
                    var productcodearray =[];
                    productcodearray.push(productCodePost);
                    utag.link({
                        link_obj: this,
                        link_text: 'add_to_wishlist_pdp' ,
                        event_type : 'add_to_wishlist_pdp',
                        product_sku_wishlist : productcodearray
                    });
                }
            });
        },

        getDataString: function(){
            var productCodePost = $("#productCodePost").val();
            var productcodearray =[];
            productcodearray.push(productCodePost);
            var wishName = "";
            var ussidValue=$("#ussid").val();
            var dataString = 'wish=' + wishName + '&product=' + productCodePost + '&ussid=' + ussidValue;
            return dataString;
        },

        getLastModifiedWishlist: function(ussidValue) {
            var isInWishlist = false;
            var requiredUrl = ACC.config.encodedContextPath + "/p-getLastModifiedWishlistByUssid";
            var dataString = 'ussid=' + ussidValue;
            $.ajax({
                contentType : "application/json; charset=utf-8",
                url : requiredUrl,
                data : dataString,
                dataType : "json",
                async: false,
                success : function(data) {
                    if (data == true) {
                        isInWishlist = true;
                        $('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').addClass("added");
                        $("#add_to_wishlist").attr("disabled",true);
                        $('.add_to_cart_form .out_of_stock #add_to_wishlist').addClass("wishDisabled");
                    }

                },
                error : function(xhr, status, error) {
                    $("#wishlistErrorId_pdp").html("Could not add the product in your wishlist");
                }
            });
            return isInWishlist;
        },

        populateMyWishlistFlyOut: function(wishName) {
            var requiredUrl = ACC.config.encodedContextPath + "/my-account/wishlistAndItsItems";
            var dataString = 'wishlistName=' + wishName;

            $.ajax({
                contentType : "application/json; charset=utf-8",
                url : requiredUrl,
                data : dataString,
                dataType : "json",
                success : function(response) {
                    $('#DropDownMyWishList').empty();
                    for(var i in response) {
                        var name=response[i].wishlistName;
                        var size=response[i].wishlistSize;
                        var url=response[i].wishlistUrl;
                        $('#DropDownMyWishList').append('<li><a href="'+url+'">'+name+'<br><span>'+size+'&nbsp;items</span></a></li>');
                    }
                },
            })
        },
        addToWishlist: function(dataString) {
            var requiredUrl = ACC.config.encodedContextPath + "/p-addToWishListInPDP";
            $.ajax({
                contentType : "application/json; charset=utf-8",
                url : requiredUrl,
                data : dataString,
                dataType : "json",
                success : function(data) {
                    $(".add-to-wl-pdp").addClass("added");
                    if (data == true) {
                        $(".wishAddSucessPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishAddSucessPlp").removeClass("active")
                        },3000);
                    }
                    else{
                        $(".wishAlreadyAddedPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishAlreadyAddedPlp").removeClass("active")
                        },3000);
                    }
                }
            });
        },

        removeFromWishlist: function(dataString){
            var requiredUrl = ACC.config.encodedContextPath+"/p" + "-removeFromWl";
            $.ajax({
                url: requiredUrl,
                type: "GET",
                data: dataString,
                dataType : "json",
                cache: false,
                contentType : "application/json; charset=utf-8",
                success : function(data) {
                    $(".add-to-wl-pdp").removeClass("added");
                    if (data == true) {
                        $(".wishRemoveSucessPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishRemoveSucessPlp").removeClass("active")
                        },3000);
                    }
                    else{
                        $(".wishAlreadyAddedPlp").addClass("active");
                        setTimeout(function(){
                            $(".wishAlreadyAddedPlp").removeClass("active")
                        },3000);
                    }
                }
            });
        },

        // PDP Page Slider
        Slider: function() {
            $('.pdp-img-slider').slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: true,
                fade: true,
                asNavFor: '.pdp-img-nav',
                responsive: [
                    {
                        breakpoint: 768,
                        settings: {
                            fade: false,
                            dots: true,
                            arrows: false
                        }
                    }
                ]
            });

            $('.pdp-img-nav').slick({
                slidesToShow: 5,
                asNavFor: '.pdp-img-slider',
                arrows: false,
                vertical: true,
                focusOnSelect: true,
                responsive: [
                    {
                        breakpoint: 768,
                        settings: {
                            fade: false,
                            dots: true,
                            arrows: false
                        }
                    }
                ]
            });
        },

        Zoomer: function() {

            if ($(window).width() > 789) {

                $('.pdp-img-nav .slick-slide, .pdp-img .slick-arrow').on('click', function(){
                    var luxzoomImg = $('.pdp-img-nav .slick-current img').attr('data-zoom-image');
                    $('.zoomer').data('zoom-image', luxzoomImg ).elevateZoom({
                        //scrollZoom : true,
                        zoomWindowWidth:400,
                        zoomWindowHeight:400
                    });
                });

                $(document).ready(function(){
                    var luxzoomImg = $('.pdp-img-nav .slick-current img').attr('data-zoom-image');
                    $('.zoomer').data('zoom-image', luxzoomImg ).elevateZoom({
                        //scrollZoom : true,
                        zoomWindowWidth:400,
                        zoomWindowHeight:400
                    });
                });
            }


        },

        openPopup: function (url) {            
            
            $('#share-modal .soc-links').on('click', function(){
                newwindow = window.open(url,'name','height=400,width=400');
                if (window.focus) {newwindow.focus()}
                return false;               
            });
        },

        videoPlay: function(){
            
            //For Firefox we have to handle it in JavaScript 
            
            var vids = $(".lux-main-banner-slider video"); 
            $.each(vids, function(){
                this.controls = false; 
            }); 
            
            $('.pdp-social-links .play').on('click', function(){
                $('body').addClass('pdp-video-active');
                $('video').each(function ()
                {
                    this.play();
                });
            });

            $('.pdp-img-nav .slick-slide').on('click', function(){
                $('body').removeClass('pdp-video-active');

                $('video').each(function ()
                {
                    this.load();
                });
            });


        },
        BankEMI: function(){
            $('.emi-header').on('click', function(){

                var productVal = $("#prodPrice").val();
                var optionData = "<ul>";
                $("#EMITermTable").hide();
                $("#emiTableTHead").hide();
                $("#emiTableTbody").hide();
                $(".emifselectbank").show();
                
                var requiredUrl = ACC.config.encodedContextPath + "/p" + "-enlistEMIBanks";
                var dataString = 'productVal=' + productVal;
                $.ajax({
                    contentType : "application/json; charset=utf-8",
                    url : requiredUrl,
                    data : dataString,
                    dataType : "json",
                    success : function(data) {
                        for (var i = 0; i < data.length; i++) {
                            optionData += "<li value='" + data[i] + "'>" + data[i]
                                + "</li>";
                        }
                        optionData  += "</ul>";
                        $("#bankNameForEMI").html(optionData);
						/*TPR-641*/
                        utag.link({
                            link_obj: this,
                            link_text: 'emi_more_information' ,
                            event_type : 'emi_more_information',
                            product_id : productIdArray
                        });

                    },
                    error : function(xhr, status, error) {

                    }
                });
            });

            $(document).on("click",'#bankNameForEMI li', function() {
                var productVal = $("#prodPrice").val();
                var selectedBank = $(this).text();
                $(this).addClass('active').siblings().removeClass('active');
                var contentData = '';
                var productId=[];
                productId.push($('#product_id').val());
                $(".emifselectbank").hide();
                
                if (selectedBank != "select") {
                    var dataString = 'selectedEMIBank=' + selectedBank + '&productVal=' + productVal;
                    $.ajax({
                        url : ACC.config.encodedContextPath + "/p-getTerms",
                        data : dataString,
						/*data : {
						 'selectedEMIBank' : selectedBank,
						 'productVal' : productVal
						 },*/
                        type : "GET",
                        cache : false,
                        success : function(data) {
                            if (data != null) {
                                $("#emiTableTHead").show();
                                $("#emiTableTbody").show();
                                for (var index = 0; index < data.length; index++) {
                                    contentData += '<tr>';
                                    contentData += "<td data-th='Tenure (Months)'>" + data[index].term + "</td>";
                                    contentData += "<td data-th='Interest Rate'>" + data[index].interestRate
                                        + "</td>";
                                    contentData += "<td data-th='Monthly Installments'>" + data[index].monthlyInstallment
                                        + "</td>";
                                    contentData += "<td data-th='Total Interest paid to bank'>" + data[index].interestPayable
                                        + "</td>";
                                    contentData += '</tr>';
                                }

                                $("#emiTableTbody").html(contentData);
                                $("#EMITermTable").show();
                            } else {
                                $('#emiNoData').show();
                            }

							/*TPR-641 starts  */
                            emiBankSelectedTealium = "emi_option_" + selectedBank.replace(/ /g, "").replace(/[^a-z0-9\s]/gi, '').toLowerCase();
							/* TPR-4725  quick view emi*/
                            emiBankSelected = selectedBank.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/[',."]/g,"");

                            if(typeof utag !="undefined"){
                                utag.link({
                                    link_text: emiBankSelectedTealium ,
                                    event_type : 'emi_option_selected',
                                    emi_selected_bank : emiBankSelected,
                                    product_id :productId
                                });
                            }
							/*TPR-641 ends*/
                        },
                        error : function(resp) {
                            $('#emiSelectBank').show();
                        }
                    });
                } else {

                }
            });


        },
        luxury_overlay_close:function(){
            $('.luxury-over-lay,.lux-keepshop-btn').on('click', function(){
                $('#addtocart-popup,.luxury-over-lay').hide();
            })

        },
        luxuryDeliveryOptions:function() {
            $('.luxuyChangepincode').on("click", function() {
                $(this).hide();
                $('#pin').show();
                $('#pdpPincodeCheck').show();
            });
            
            if($("#pin").val()==""){
                $(".luxuyChangepincode").addClass("hide");
            }else{
                $(".luxuyChangepincode").removeClass("hide");
            }
        },
        
        writeReview:function() {
        	 $("body").on("click touchstart", ".gig-rating-writeYourReview,.gig-rating-readReviewsLink", function(e) {
             	e.preventDefault();
        		$('.accordion-title').removeClass('active');
        		$('.accordion-content').hide();
        		$('.review-accordion').addClass('active');
        		$('.review-accordion-content').show();        		
        	});
        },
        editAddressCheckout:function() {
            $('.edit_address').on('click',function(){
                $(this).addClass('disable-click');
            });

            $(document).on('click', '.address-details .cancelBtnEdit', function(e){
                $('.edit_address').removeClass('disable-click');
            });
        },
        // PDP Page initiate
        init: function () {
            var _self = TATA.Pages.PDP;
            
            _self.Slider();
            _self.Zoomer();
            _self.openPopup();
            _self.videoPlay();
            _self.BankEMI();
            _self.luxury_overlay_close();
            _self.wishlistInit();
            _self.luxuryDeliveryOptions();
            _self.writeReview();
            _self.editAddressCheckout();
        }
    },

        MYACCOUNT: {
    	init: function(){
    		$(".edit").on('click', function(e) {
    			var addressId = $(this).attr("data-addressId");
    			TATA.Pages. MYACCOUNT.editLuxuryAddress(addressId);
    	});
         
           /* $(".view-details").on('click', function(e) {
                
                $(this).parents(".saved-cards").find(".paymentItem").toggleClass("active");                
               
            });*/
    	},
    	editLuxuryAddress: function(addressId) {
            var requiredUrl = ACC.config.encodedContextPath + "/my-account/populateAddressDetail", dataString = "&addressId=" + addressId;
            $.ajax({
                url: requiredUrl,
                type: "GET",
                data: dataString,
                dataType: "json",
                cache: !1,
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                	var fulladdr = data.line1;
                    data.line2 && (console.log("Inside line2 checking***"),fulladdr  += data.line2);
                    data.line3 && (console.log("Inside line3 checking***"),fulladdr  += data.line3);
                    $("#addressId").val(addressId), $("#firstName").val(data.firstName), $("#lastName").val(data.lastName), 
                    $("#line1").val(fulladdr),$("#line2").val(""),$("#line3").val(""),
                    $("#postcode").val(data.postcode),$(".address_landmarks").val(data.landmark), $(".address_landmarkOther").val(data.landmark), 
                    loadPincodeData("edit").done(function() {
                        otherLandMarkTri(data.landmark, "defult");
                    }), $("#townCity").val(data.townCity), $("#mobileNo").val(data.mobileNo), $("#stateListBox").data("selectBox-selectBoxIt").selectOption(data.state), 
                    "Home" == data.addressType && (document.getElementById("new-address-option-1").checked = !0), 
                    "Work" == data.addressType && (document.getElementById("new-address-option-2").checked = !0), 
                    $("#headerAdd").css("display", "none"), $("#headerEdit").css("display", "block"), 
                    $("#addNewAddress").css("display", "none"), $("#edit").css("display", "block");
                },
                error: function(data) {
                    console.log(data.responseText);
                }
            });
        }            
    },
        
    init: function () {
        var _self = TATA.Pages;
        _self.PLP.init();
        _self.PDP.init();
        // _self.CHECKOUT.init();
        _self.LANDING.init();
        _self.MYACCOUNT.init();
    },
};

function luxurycheckPincodeServiceability(buttonType, el)
{

    /*spinner commented starts*/
    //$("#pinCodeDispalyDiv").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right:0;bottom:0; left:0; top:0; margin:auto; height: 30px;">');
    /*spinner commented ends*/
    // $("#pinCodeDispalyDiv
    // .spinner").css("left",(($("#pinCodeDispalyDiv").width()+$("#pinCodeDispalyDiv").width())/2)+10);
    /*TPR-3446 new starts*/
    //$("body").append("<div id='no-click' style='opacity:0.6; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");

    /*TPR-3446 new ends*/
    var selectedPincode=$('#defaultPinCodeIds').val();
    var regPostcode = /^([1-9])([0-9]){5}$/;
    $(".deliveryUlClass").remove();//TPR-1341
    var utagCheckPincodeStatus="";

    if(selectedPincode === ""){
        $( "#error-Id").hide();
        $( "#error-IdBtm").hide();
        $( "#error-Id_tooltip").hide();
        $( "#error-Id_tooltip_btm").hide();
        $("#emptyId").css({
            "color":"#ff1c47",
            "display":"block",
        });
        $("#emptyIdBtm").css({
            "color":"#ff1c47",
            "display":"block",
        });
        $( "#emptyId_tooltip").show();
        $("#emptyId_tooltip_btm").show();
        $('#checkout-id #checkout-enabled').addClass('checkout-disabled');
        $('#checkout-id-down #checkout-down-enabled').addClass('checkout-disabled'); //UF-69
        $("#cartPinCodeAvailable").hide();
        $("#cartPinCodeAvailableBtm").hide();// TPR-1055//UF-68
        //$("#pinCodeButtonIds").text("Change Pincode");
        document.getElementById("pinCodeButtonIds").className = "ChangePincode"; //UF-71
        //$("#pinCodeButtonIds").text("Change Pincode");
        document.getElementById("pinCodeButtonIds").className = "ChangePincode"; //UF-71
        // setTimeout(function(){
        $("#unserviceablepincode").hide();// tpr-1341
        $("#unserviceablepincodeBtm").hide();// tpr-1341//UF-68
        $("#unserviceablepincode_tooltip").hide();
        $("#unserviceablepincode_tooltip_btm").hide();//UF-68
        $(".cartItemBlankPincode").show();
        //$("#pinCodeButtonIds").text("Check");
        document.getElementById("pinCodeButtonIds").className = "CheckAvailability"; 	//UF-71
        $("#AvailableMessage").hide();
        $("#AvailableMessageBtm").hide();
        $(".pincodeServiceError").hide();
        $(".delivery ul.success_msg").hide();
        $("#pinCodeDispalyDiv .spinner").remove();
        $("#no-click,.spinner").remove();
        // },500);
        $("body,html").animate({ scrollTop: $('#emptyId').offset().top - 5000 }); //added for INC_11152
        return false;
    }
    else if(regPostcode.test(selectedPincode) != true){
        $("#defaultPinCodeIds").css("color","red");
        $("#error-Id").show();
        $("#error-IdBtm").show();//UF-68
        $("#error-Id_tooltip").show();
        $( "#error-Id_tooltip_btm").show();
        $("#cartPinCodeAvailable").hide();// TPR-1055
        $("#cartPinCodeAvailableBtm").hide();// TPR-1055//UF-68
        $("#unserviceablepincode").hide();
        $("#unserviceablepincodeBtm").hide();//UF-68
        $("#unserviceablepincode_tooltip").hide();
        $("#unserviceablepincode_tooltip_btm").hide();
        $("#AvailableMessage").hide();
        $("#AvailableMessageBtm").hide();//UF-68
        //TPR-1341
        $(".pincodeServiceError").hide();
        $(".delivery ul.success_msg").hide();
        //$("#pinCodeButtonIds").text("Change Pincode");
        document.getElementById("pinCodeButtonIds").className = "ChangePincode"; //UF-71
        $(".cartItemBlankPincode").show();
        $("#emptyId").hide();
        $("#emptyIdBtm").hide();//UF-68
        $("#emptyId_tooltip").hide();
        $("#emptyId_tooltip_btm").hide();//UF-68
        $("#error-Id").css({
            "color":"red",
            "display":"block",

        });
        $("#error-IdBtm").css({
            "color":"red",
            "display":"block",

        });
        $("#error-Id_tooltip").show();
        $( "#error-Id_tooltip_btm").show();//UF-68
        $('#checkout-id #checkout-enabled').addClass('checkout-disabled');
        $('#checkout-id-down #checkout-down-enabled').addClass('checkout-disabled'); //UF-69
        // setTimeout(function(){
        $("#pinCodeDispalyDiv .spinner").remove();
        $("#no-click,.spinner").remove();
        // },500);
        return false;
    }
    // TPR-1055 starts

    //else if($("#pinCodeButtonIds").text() == 'Change Pincode'&& $(el).attr("id") =="pinCodeButtonIds"){



    //else if($("#pinCodeButtonIds").text() == 'Change Pincode'&& $(el).attr("id") =="pinCodeButtonIds"){
    else if(document.getElementById("pinCodeButtonIds").className == 'ChangePincode' && $(el).attr("id") =="pinCodeButtonIds")//UF-71
    {
        $("#unserviceablepincode").hide();
        $("#unserviceablepincodeBtm").hide();//UF-68
        $("#unserviceablepincode_tooltip").hide();
        $("#unserviceablepincode_tooltip_btm").hide();
        $("#cartPinCodeAvailable").show();
        $("#cartPinCodeAvailableBtm").show();//UF-68
        $(".pincodeServiceError").hide();
        $("#AvailableMessage").hide();
        $("#AvailableMessageBtm").hide();//UF-68
        $(".cartItemBlankPincode").show();
        //$("#pinCodeButtonIds").text("Check");
        document.getElementById("pinCodeButtonIds").className = "CheckAvailability"; 	//UF-71
        $('#defaultPinCodeIds').focus();
        $('#defaultPinCodeIdsBtm').focus();//UF-68
        $("#pinCodeDispalyDiv .spinner").remove();
        $("#emptyId").hide();
        $("#emptyIdBtm").hide();//UF-68
        $("#emptyId_tooltip").hide();
        $("#emptyId_tooltip_btm").hide();
        $("#error-Id").hide();
        $("#error-IdBtm").hide();//UF-68
        $("#error-Id_tooltip").hide();
        $( "#error-Id_tooltip_btm").hide();
        $("#no-click,.spinner").remove();
        $(".delivery ul.success_msg").hide();//TPR-1341
        return false;
        // TPR-1055 ends
    }
    
    else if(document.getElementById("pinCodeButtonIds").className == 'ChangePincode' && $(el).attr("id") =="pinCodeButtonIds")//UF-71
    {
        $("#unserviceablepincode").hide();
        $("#unserviceablepincodeBtm").hide();//UF-68
        $("#unserviceablepincode_tooltip").hide();
        $("#unserviceablepincode_tooltip_btm").hide();
        $("#cartPinCodeAvailable").show();
        $("#cartPinCodeAvailableBtm").show();//UF-68
        $(".pincodeServiceError").hide();
        $("#AvailableMessage").hide();
        $("#AvailableMessageBtm").hide();//UF-68
        $(".cartItemBlankPincode").show();
        //$("#pinCodeButtonIds").text("Check");
        //$("#pinCodeButtonIds").text("Check Availability");
        document.getElementById("pinCodeButtonIdsBtm").className = "CheckAvailability";	//UF-71
        $('#defaultPinCodeIds').focus();
        $('#defaultPinCodeIdsBtm').focus();//UF-68
        $("#pinCodeDispalyDiv .spinner").remove();
        $("#emptyId").hide();
        $("#emptyIdBtm").hide();//UF-68
        $("#emptyId_tooltip").hide();
        $("#emptyId_tooltip_btm").hide();
        $("#error-Id").hide();
        $("#error-IdBtm").hide();//UF-68
        $("#error-Id_tooltip").hide();
        $( "#error-Id_tooltip_btm").hide();
        $("#no-click,.spinner").remove();
        $(".delivery ul.success_msg").hide();//TPR-1341
        return false;
        // TPR-1055 ends
    } //CAR-246
    else if(selectedPincode!==""){
        // TPR-5666 | cartGuid Append in url during pincode servicability check
        var cartGuidParamValue = luxurygetParameterByName("cartGuid");
        if(typeof cartGuidParamValue != "undefined"){
            $(location).attr('href',ACC.config.encodedContextPath + "/cart?cartGuid="+cartGuidParamValue+"&pincode="+selectedPincode);
        }
        else{
            $(location).attr('href',ACC.config.encodedContextPath + "/cart?pincode="+selectedPincode);
        }
    }
    else
    {
        // $("#defaultPinCodeIds").prop('disabled', true);
        //$("#pinCodeButtonIds").text("Check Pincode");
        document.getElementById("pinCodeButtonIds").className = "CheckPincode"; //UF-71
        document.getElementById("pinCodeButtonIdsBtm").className = "CheckPincode"; //UF-71//UF-68
        $("#defaultPinCodeIds").css("color","black");
        $( "#error-Id").hide();
        $("#defaultPinCodeIdsBtm").css("color","black");
        $( "#error-IdBtm").hide();//UF-68
        $( "#error-Id_tooltip").hide();
        $( "#error-Id_tooltip_btm").hide();
        // $("#cartPinCodeAvailable").show();//TPR-1055
        $("#emptyId").hide();
        $("#emptyIdBtm").hide();//UF-68
        $("#emptyId_tooltip").hide();

        var staticHost = $('#staticHost').val();
        $("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
        $("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');

        $("#emptyId_tooltip_btm").hide();
        $.ajax({
            url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
            type: "GET",
            cache: false,
            success : function(response) {
                //UF-84
                $("#pincodeforcart").html("&nbsp;("+ selectedPincode + ")"); /*TISPRDT-690 */
                //"sprint merger issue
                var responeStr=response['pincodeData'].split("|");
                //TPR-970 changes
                populateCartDetailsafterPincodeCheck(responeStr[1]);
                //TPR-970 changes
                if(responeStr[0]=="N")
                {
                    utagCheckPincodeStatus = "cart_pincode_check_failure";
                    /*if(typeof utag !="undefined")
                     {
                     //TPR-4736 | DataLAyerSchema changes | cart
                     utag.link({
                     //"link_obj": this,
                     "link_text": "cart_pincode_check_failure",
                     "event_type" : "cart_pincode_check_failure",
                     "cart_pin_non_servicable" : selectedPincode
                     });
                     }*/
                    // TISTI-255
                    // Please try later or contact our helpdesk");
                    // TISPRD-1666 - console replaced with alert and resp print
                    $("#AvailableMessage").hide();
                    $("#cartPinCodeAvailable").hide();
                    $("#unserviceablepincode").show();
                    $("#AvailableMessageBtm").hide();//UF-68
                    $("#cartPinCodeAvailableBtm").hide();//UF-68
                    $("#unserviceablepincodeBtm").show();//UF-68
                    $(".pincodeServiceError").show();
                    luxurypopulatePincodeDeliveryMode(response,buttonType);
                    reloadpage(selectedPincode,buttonType);
                    $("#isPincodeServicableId").val('N');
                    // reloadpage(selectedPincode,buttonType);
                }
                else

                {
                    utagCheckPincodeStatus = "cart_pincode_check_success";
                    /*if(typeof utag !="undefined")
                     {
                     //TPR-4736 | DataLAyerSchema changes | cart
                     utag.link({
                     //"link_obj": this,
                     "link_text": "cart_pincode_check_success",
                     "event_type" : "cart_pincode_check_success",
                     "cart_pin_servicable" : selectedPincode
                     });
                     }*/
                    $(".pincodeServiceError").hide();
                    $("#unserviceablepincode").hide();
                    $("#cartPinCodeAvailable").hide();
                    $("#unserviceablepincodeBtm").hide();//UF-68
                    $("#cartPinCodeAvailableBtm").hide();//UF-68
                    $("#AvailableMessage").html("Available delivery options for the pincode " +selectedPincode+ " are");
                    $("#AvailableMessage").show();
                    $("#AvailableMessageBtm").show();//UF-68
                    luxurypopulatePincodeDeliveryMode(response,buttonType);
                    reloadpage(selectedPincode,buttonType);
                }

                // TISPRM-33
                $("#defaultPinDiv").show();

                // $("#changePinDiv").hide();
                $('#defaultPinCodeIdsq').val(selectedPincode);
                // setTimeout(function(){

                $("#pinCodeDispalyDiv .spinner").remove();
                $("#no-click,.spinner").remove();
                // },500);
                // TPR-1055
                $('#defaultPinCodeIds').blur();
                if ( $('#defaultPinCodeIds').val() == "") {

                    // $("#cartPinCodeAvailable").html("Enter your pincode to
                    // see your available delivery options");
                    $("#cartPinCodeAvailable").show();
                    //$("#pinCodeButtonIds").text("Check Availability")
                    document.getElementById("pinCodeButtonIds").className = "CheckAvailability"; //UF-71

                } else {
                    $("#cartPinCodeAvailable").hide();
                    $("#cartPinCodeAvailableBtm").hide();//UF-68
                    // $("#unserviceablepincode").hide();
                    //$("#pinCodeButtonIds").text("Change Pincode")
                    document.getElementById("pinCodeButtonIds").className = "ChangePincode"; //UF-71
                    document.getElementById("pinCodeButtonIdsBtm").className = "ChangePincode"; //UF-71//UF-68
                }
            },
            error : function(resp) {
                utagCheckPincodeStatus = "cart_pincode_check_failure";
                /*if(typeof utag !="undefined"){
                 //TPR-4736 | DataLAyerSchema changes | cart
                 utag.link({
                 //"link_obj": this,
                 "link_text": "cart_pincode_check_failure",
                 "event_type" : "cart_pincode_check_failure",
                 "cart_pin_non_servicable" : selectedPincode
                 });
                 }*/
                //TISTI-255
                //alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
                console.log(resp);
                $("#isPincodeServicableId").val('N');
                reloadpage(selectedPincode,buttonType);

// TISPRD-1666 - console replaced with alert and resp print
                var errorDetails=JSON.stringify(resp);
                console.log("errorDetails 1>> "+errorDetails);

                handleExceptionOnServerSide(errorDetails);
                console.log('Some issue occured in checkPincodeServiceability');
                // setTimeout(function(){
                $("#pinCodeDispalyDiv .spinner").remove();
                $("#no-click,.spinner").remove();
                // },500);
            },
            complete : function(resp){

                //TPR-4736 | DataLAyerSchema changes | cart
                if(utagCheckPincodeStatus == "cart_pincode_check_failure"){
                    if(typeof utag !="undefined"){
                        utag.link({
                            "link_text": utagCheckPincodeStatus,
                            "event_type" : utagCheckPincodeStatus,
                            "cart_pin_non_servicable" : selectedPincode
                        });
                    }
                }
                else{
                    if(typeof utag !="undefined"){
                        utag.link({
                            "link_text": "cart_pincode_check_success",
                            "event_type" : "cart_pincode_check_success",
                            "cart_pin_servicable" : selectedPincode
                        });
                    }
                }
            }

        });


    }
}

function luxurygetParameterByName(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search)){
        return decodeURIComponent(name[1]);
    }
}

function luxurycheckIsServicable()
{

    var selectedPincode=$("#defaultPinCodeIds").val();
    if(selectedPincode!=null && selectedPincode != undefined && selectedPincode!=""){

        $.ajax({
            url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
            type: "GET",
            cache: false,
            success : function(response) {
                luxurypopulatePincodeDeliveryMode(response,'pageOnLoad');
            },
            error : function(resp) {
                //TISTI-255
                //TISPRD-1666 - console replaced with alert and resp print
                //alert("Some issues are there with CheckopopulatePincodeDeliveryModeut at this time. Please try  later or contact our helpdesk");
                console.log(resp);
                var errorDetails=JSON.stringify(resp);
                console.log("errorDetails 1>> "+errorDetails);

                handleExceptionOnServerSide(errorDetails);

                console.log('Some issue occured in checkPincodeServiceability');
                $("#isPincodeServicableId").val('N');
                if(typeof utag !="undefined")
                {
                    //TPR-4736 | DataLAyerSchema changes | cart
                    utag.link({
                        "error_type" : "pincode_check_error",
                    });
                }
            }
        });
    }

}

function luxurypopulatePincodeDeliveryMode(response, buttonType){

    var checkoutLinkURlId = $('#checkoutLinkURlId').val();
    // response='Y|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"4","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12},{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"12","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12}]';
    // response='N|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"2","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":2},{"fulfilmentType":null,"isPrepaidEligible":null,"ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":null,"cod":null,"transportMode":null,"isCODLimitFailed":null,"deliveryDate":null,"isServicable":"N","stockCount":null}]';

// console.log(response);
    //"sprint merger issue
    var values=response['pincodeData'].split("|");
    var isServicable=values[0];
    var selectedPincode=values[1];
    var deliveryModeJsonMap=values[2];

    $(".pincodeServiceError").hide();
    if(deliveryModeJsonMap=="null"){
        $('.unservicePins').show();
        $("#checkout-enabled").css("pointer-events","none");
        $("#checkout-enabled").css("cursor","not-allowed");
        $("#checkout-enabled").css("opacity","0.5");
        /*UF-69*/
        $("#checkout-down-enabled").css("pointer-events","none");
        $("#checkout-down-enabled").css("cursor","not-allowed");
        $("#checkout-down-enabled").css("opacity","0.5");
        $("#expressCheckoutButtonId").css("pointer-events","none");
        $("#expressCheckoutButtonId").css("cursor","default");
        $("#expressCheckoutButtonId").css("opacity","0.5");
        var pincodeEntered = $('#defaultPinCodeIds').val();
        var pincodeServiceError = "This item is not serviceable for pincode "+pincodeEntered+"!";
        // console.log(pincodeServiceError);
        var elementId = $(".desktop li:nth-child(3) ul");
        elementId.hide();
        $(".pincodeServiceError").text(pincodeServiceError);
        // TPR-933
        $('.success_msg').hide();
        //TPR-1341
        $(".cartItemBlankPincode").hide();

        $(".deliveryUlClass").remove();
    }else{
        //TPR-1341

        $('#unsevisablePin').hide();
        $(".pincodeServiceError").hide();
        $("#checkout-enabled").css("pointer-events","all");
        $("#checkout-enabled").css("cursor","cursor");
        $("#checkout-enabled").css("opacity","1");
        $(".delivery").show();
        /*UF-69*/
        $("#checkout-down-enabled").css("pointer-events","all");
        $("#checkout-down-enabled").css("cursor","cursor");
        $("#checkout-down-enabled").css("opacity","1");
        $("#expressCheckoutButtonId").css("pointer-events","all");
        $("#expressCheckoutButtonId").css("cursor","cursor");
        $("#expressCheckoutButtonId").css("opacity","1");
        var deliveryModeJsonObj = JSON.parse(deliveryModeJsonMap);
        var length = Object.keys(deliveryModeJsonObj).length;
        var isStockAvailable="Y";

        if(deliveryModeJsonMap == 'N') {
            console.log("This is NO");
        }
    }





    for ( var key in deliveryModeJsonObj) {
        var ussId= deliveryModeJsonObj[key].ussid;
        $("#"+ussId+"_qtyul").remove();
        if(deliveryModeJsonObj[key].isServicable==='N'){
            $("#"+ussId).remove();
            var newUi = document.createElement("ul");
            newUi.setAttribute("id", ussId);
            newUi.setAttribute("class", "deliveryUlClass");//TPR-1341
            var newSpan = document.createElement("span");
            var text = document.createTextNode("This item is not serviceable for pincode "+selectedPincode+"!");
            newSpan.appendChild(text);
            newUi.appendChild(newSpan);
            $("#"+ussId+"_li").append(newUi);
            $(".cartItemBlankPincode").hide();//TPR-1341
            $(".pincodeServiceError").hide();
            //console.log("Not servicable");
        }
        else{
            //console.log("No Stock");
            $("#"+ussId).remove();
            var newUi = document.createElement("ul");
            newUi.setAttribute("id", ussId);
            // TPR-933 class added
            newUi.setAttribute("class", "success_msg");
            var jsonObj=deliveryModeJsonObj[key].validDeliveryModes;

            var inventory=deliveryModeJsonObj[key].stockCount;
            var quantityValue=$("#quantity_"+ussId).val();
            var stockAvailable =false;

            for ( var count in jsonObj) {


                inventory=jsonObj[count].inventory;
                if(parseFloat(quantityValue) <= parseFloat(inventory)){
                    stockAvailable=true;
                }
            }

            if(stockAvailable==false){
                var newUl = document.createElement("ul");
                newUl.setAttribute("id", ussId+'_qtyul');
                newUl.setAttribute("class", 'less-stock');
                var newLi = document.createElement("li");
                var text = document.createTextNode("Oops! We only have "+inventory+" in stock! For pincode : "+selectedPincode);
                newLi.appendChild(text);
                newUl.appendChild(newLi);
                $("#"+ussId+"_qty").append(newUl);
                isStockAvailable="N";
            }
            var newLi = document.createElement("li");
            newLi.setAttribute("class", "methodHome");
            var text = document.createTextNode("Home Delivery");
            newLi.appendChild(text);
            var newLi1 = document.createElement("li");
            newLi1.setAttribute("class", "methodExpress");
            var text = document.createTextNode("Express Delivery");
            newLi1.appendChild(text);
            var newLi2 = document.createElement("li");
            newLi2.setAttribute("class", "methodClick");
            var text = document.createTextNode("Click and Collect");
            newLi2.appendChild(text);

            var isHd = false;
            var isEd = false;
            var isCnc = false;
            //newUi.appendChild(newLi1);
            for ( var count in jsonObj) {
                var inventory=0;
                var deliveryType=jsonObj[count].type;
                inventory=jsonObj[count].inventory;

                if(deliveryType==='HD') {
                    //newLi1.setAttribute("class", "methodExpress lowOpacity");
                    //newLi2.setAttribute("class", "methodClick lowOpacity");
                    isHd = true;

                }
                else if(deliveryType==='ED'){
                    //newLi.setAttribute("class", "methodHome lowOpacity");
                    //newLi2.setAttribute("class", "methodClick lowOpacity");
                    isEd = true;
                }
                else if(deliveryType==='CNC'){
                    //newLi.setAttribute("class", "methodHome lowOpacity");
                    //newLi1.setAttribute("class", "methodExpress lowOpacity");
                    isCnc = true;
                }


            }
            if (isHd) {
                newLi.setAttribute("class", "methodHome");
            }
            else {
                newLi.setAttribute("class", "methodHome lowOpacity");
            }
            if (isEd) {
                newLi1.setAttribute("class", "methodExpress");
            }
            else {
                newLi1.setAttribute("class", "methodExpress lowOpacity");
            }
            if (isCnc) {
                newLi2.setAttribute("class", "methodClick");
            }
            else {
                newLi2.setAttribute("class", "methodClick lowOpacity");
            }

            newUi.appendChild(newLi);
            newUi.appendChild(newLi1);
            newUi.appendChild(newLi2);
            /** **TISPRM-65 - Cart Page show pincode serviceability msg** */
            /** **Tpr-634 - commented for scope of improvement** */
            /*
             * var cartMessage = document.createElement("span"); cartMessage.id =
             * "successPin" cartMessage.style.color = "green"; var message =
             * document.createTextNode("Yes, it's available. Go ahead.");
             * cartMessage.appendChild(message); newUi.appendChild(cartMessage);
             */
            $("#"+ussId+"_li").append(newUi);
            //TPR-1341
            $(".cartItemBlankPincode").hide();
            $(".pincodeServiceError").hide();

        }
    }

    $("#defaultPinCodeIds").val(selectedPincode);



    if(isServicable==='Y' && isStockAvailable==='Y')
    {

        // TISBOX-879
        $("#isPincodeServicableId").val('Y');
        $('#checkout-id #checkout-enabled').removeClass('checkout-disabled'); // TISEE-6257
        $('#checkout-id-down #checkout-down-enabled').removeClass('checkout-disabled'); //UF-69
        $('#expresscheckoutid #expressCheckoutButtonId').removeClass('express-checkout-disabled'); // TISEE-6257
        // Code Start TISPRD-437
        var str1 = document.referrer;
        if(str1.indexOf('checkout') != -1){ // last page checkout
            if($('.global-alerts').length != 0) { // error exist in dom
                var errortext = $(".global-alerts,alert-danger, alert-dismissable").text();
                if( errortext != null && errortext != 'undefined' && errortext != '') {
                    $(".global-alerts").remove();
                }
            }
        }
        // Code End TISPRD-437

        if(buttonType=='typeCheckout')
        {

            redirectToCheckout(checkoutLinkURlId);
        }

        if(buttonType=='typeExpressCheckout')
        {
            var expressCheckoutAddresId=$("#expressCheckoutAddressSelector").val();
            $(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/express?expressCheckoutAddressSelector="+expressCheckoutAddresId);
        }
    }
    else
    {
        $("#isPincodeServicableId").val('N');
        $('#checkout-id #checkout-enabled').addClass('checkout-disabled'); // TISEE-6257
        $('#checkout-id-down #checkout-down-enabled').addClass('checkout-disabled'); //UF-69
        $('#expresscheckoutid #expressCheckoutButtonId').addClass('express-checkout-disabled'); // TISEE-6257
    }
}

function openPopFromCart(entry,productCode,ussid) {
 
 // var productCode = $("#product").val();
 var requiredUrl = ACC.config.encodedContextPath + "/p"+"-viewWishlistsInPDP";
 var dataString = 'productCode=' + productCode+ '&ussid=' + ussid;//modified for ussid

 var entryNo = $("#entryNo").val(entry);
  $.ajax({
  contentType : "application/json; charset=utf-8",
  url : requiredUrl,
  data : dataString,
  dataType : "json",
  success : function(data) {
   if(data==null)
   {
    $("#wishListNonLoggedInId").show();
    $("#wishListDetailsId").hide();
   }
   else if (data == "" || data == []) {
    loadDefaultWishLstForCart(productCode,ussid);
   }
   else
   {
    LoadWishListsFromCart(data, productCode,ussid); 
   } 
   
  },
  error : function(xhr, status, error) {
   $("#wishListNonLoggedInId").show();
   $("#wishListDetailsId").hide();
  }
 });
}

function LoadWishListsFromCart(data, productCode,ussid) {
    
 // modified for ussid
 
 //var ussid = $("#ussid").val()
 
 var wishListContent = "";
 var wishName = "";
 $this = this;
 $("#wishListNonLoggedInId").hide();
 $("#wishListDetailsId").show();

 for ( var i in data) {
  var index = -1;
  var checkExistingUssidInWishList = false;
  var wishList = data[i];
  wishName = wishList['particularWishlistName'];
  wishListList[i] = wishName;
  var entries = wishList['ussidEntries'];
  for ( var j in entries) {
   var entry = entries[j];
   if (entry == ussid) {
    
    checkExistingUssidInWishList = true;
    break;

   }
  }
  if (checkExistingUssidInWishList) {
   index++;
            
   wishListContent = wishListContent
     + "<tr class='d0'><td class='le-radio'><input type='radio' name='wishlistradio' id='radio_"
     + i
     + "' style='display: none' onclick='selectWishlist("
     + i + ")' disabled><label for='radio_"
     + i + "'>"+wishName+"</label></td></tr>";
  } else {
   index++;
    
   wishListContent = wishListContent
     + "<tr><td class='le-radio'><input type='radio' name='wishlistradio' id='radio_"
     + i
     + "' style='display: none' onclick='selectWishlist("
     + i + ")'><label for='radio_"
     + i + "'>"+wishName+"</label></td></tr>";
  }

 }

 $("#wishlistTbodyId").html(wishListContent);
 $('#selectedProductCode').attr('value',productCode);
 $('#proUssid').attr('value',ussid);

}

function removefromCart(entryNo,wishName)
{
    $.ajax({
        contentType : "application/json; charset=utf-8",
        url :  ACC.config.encodedContextPath+"/cart/removeFromMinicart?entryNumber="+entryNo,
        dataType : "json",
        success : function(data) {

            var productName = $("#moveEntry_"+entryNo).parents(".item").find(".desktop .product-name > a").text();
            $("#moveEntry_"+entryNo).parents(".item").hide().empty();
            /* $(".product-block > li.header").append('<span>'+productName+' Moved to '+wishName+'</span>'); */

            //$('.moveToWishlistMsg').html("Item successfully moved to "+wishName);
            //$('.moveToWishlistMsg').show();
            setTimeout(function() {
                $(".product-block > li.header > span").fadeOut(6000).remove();
                //  $(".moveToWishlistMsg").fadeOut().empty();
            }, 6000);
            location.reload();


        },
        error:function(data){
            alert("error");
        }

    });

}


function addToWishlistForCart(ussid,productCode)
{
    var wishName = "";
    var sizeSelected=true;
    var productcodearray = [];

    if (wishListList == "") {
        wishName = $("#defaultWishName").val();
    } else {
        wishName = wishListList[$("#hidWishlist").val()];
    }


    if(wishName==""){
        var msg=$('#wishlistnotblank').text();
        $('#addedMessage').show();
        $('#addedMessage').html(msg);
        return false;
    }
    if(wishName==undefined||wishName==null){
        $("#wishlistErrorId").html("Please select a wishlist");
        $("#wishlistErrorId").css("display","block");
        return false;
    }

    $("#wishlistErrorId").css("display","none");

    productcodearray.push(productCode);
    var requiredUrl = ACC.config.encodedContextPath + "/p"+ "-addToWishListInPDP";
    var dataString = 'wish='+wishName
        +'&product='+ productCode
        +'&ussid='+ ussid
        +'&sizeSelected='+ sizeSelected;

    var entryNo = $("#entryNo").val();

    $.ajax({
        contentType : "application/json; charset=utf-8",
        url : requiredUrl,
        data : dataString,
        dataType : "json",
        success : function(data) {
            if (data == true) {

                $("#radio_" + $("#hidWishlist").val()).prop("disabled", true);

                if(typeof utag !="undefined"){
                    utag.link({
                        link_obj: this,
                        link_text: 'cart_add_to_wishlist' ,
                        event_type : 'cart_add_to_wishlist',
                        product_sku_wishlist : productcodearray
                    });
                }

                localStorage.setItem("movedToWishlist_msgFromCart", "Y");

                /* 				var msg=$('#movedToWishlistFromCart').text();
                 $('#movedToWishlist_Cart').show();
                 $('#movedToWishlist_Cart').html(msg);
                 setTimeout(function() {
                 $("#movedToWishlist_Cart").fadeOut().empty();
                 }, 1500); */


                /* 		var msg=$('#wishlistSuccess').text() + wishName;
                 $('#addedMessage').show();
                 $('#addedMessage').html(msg);
                 setTimeout(function() {
                 $("#addedMessage").fadeOut().empty();
                 }, 5000); */
                removefromCart(entryNo,wishName);
            }
        },
    })

    $('a.wishlist#wishlist').popover('hide');
}

function addToWishlistForCart(ussid,productCode,alreadyAddedWlName)
{
    var wishName = "";
    var sizeSelected=true;

    if (wishListList == "") {
        wishName = $("#defaultWishName").val();
    } else {
        wishName = wishListList[$("#hidWishlist").val()];
    }


    if(wishName==""){
        var msg=$('#wishlistnotblank').text();
        $('#addedMessage').show();
        $('#addedMessage').html(msg);
        return false;
    }
    if(wishName==undefined||wishName==null){
        if(alreadyAddedWlName!=undefined || alreadyAddedWlName!=""){
            if(alreadyAddedWlName=="[]"){
                $("#wishlistErrorId").html("Please select a wishlist");
            }
            else{
                alreadyAddedWlName=alreadyAddedWlName.replace("[","");
                alreadyAddedWlName=alreadyAddedWlName.replace("]","");
                $("#wishlistErrorId").html("Product already added in your wishlist "+alreadyAddedWlName);
            }
            $("#wishlistErrorId").css("display","block");
        }
        return false;
    }

    $("#wishlistErrorId").css("display","none");


    var requiredUrl = ACC.config.encodedContextPath + "/p"+ "-addToWishListInPDP";
    var dataString = 'wish='+wishName
        +'&product='+ productCode
        +'&ussid='+ ussid
        +'&sizeSelected='+ sizeSelected;

    var entryNo = $("#entryNo").val();

    var productcodearray =[];
    productcodearray.push(productCode);
    $.ajax({
        contentType : "application/json; charset=utf-8",
        url : requiredUrl,
        data : dataString,
        dataType : "json",
        success : function(data) {
            if (data == true) {

                $("#radio_" + $("#hidWishlist").val()).prop("disabled", true);

                /*TPR-656*//*TPR-4738*/
                if(typeof utag !="undefined"){
                    utag.link({
                        link_obj: this,
                        link_text: 'cart_add_to_wishlist' ,
                        event_type : 'cart_add_to_wishlist',
                        product_sku_wishlist : productcodearray
                    });
                }
                /*TPR-656 Ends*/

                localStorage.setItem("movedToWishlist_msgFromCart", "Y");


                /*
                 * var msg=$('#movedToWishlistFromCart').text();
                 * $('#movedToWishlist_Cart').show(); $('#movedToWishlist_Cart').html(msg);
                 * setTimeout(function() { $("#movedToWishlist_Cart").fadeOut().empty(); },
                 * 1500);
                 */


                /*
                 * var msg=$('#wishlistSuccess').text() + wishName;
                 * $('#addedMessage').show(); $('#addedMessage').html(msg);
                 * setTimeout(function() { $("#addedMessage").fadeOut().empty(); },
                 * 5000);
                 */
                removefromCart(entryNo,wishName);
            }
        },
    })

    $('a.wishlist#wishlist').popover('hide');
}

var wishListList = [];

function LoadWishListsFromCart(data, productCode,ussid) {

    // modified for ussid

    // var ussid = $("#ussid").val()
    var addedWlList_cart = [];
    var wishListContent = "";
    var wishName = "";
    $this = this;
    $("#wishListNonLoggedInId").hide();
    $("#wishListDetailsId").show();

    for ( var i in data) {
        var index = -1;
        var checkExistingUssidInWishList = false;
        var wishList = data[i];
        wishName = wishList['particularWishlistName'];
        wishListList[i] = wishName;
        var entries = wishList['ussidEntries'];
        for ( var j in entries) {
            var entry = entries[j];
            if (entry == ussid) {

                checkExistingUssidInWishList = true;
                break;

            }
        }
        if (checkExistingUssidInWishList) {
            index++;

            wishListContent = wishListContent
                + "<tr class='d0'><td class='le-radio'><input type='radio' name='wishlistradio' id='radio_"
                + i
                + "' style='display: none' onclick='selectWishlist("
                + i + ")' disabled><label for='radio_"
                + i + "'>"+wishName+"</label></td></tr>";
            addedWlList_cart.push(wishName);
        } else {
            index++;

            wishListContent = wishListContent
                + "<tr><td class='le-radio'><input type='radio' name='wishlistradio' id='radio_"
                + i
                + "' style='display: none' onclick='selectWishlist("
                + i + ")'><label for='radio_"
                + i + "'>"+wishName+"</label></td></tr>";
        }
        $("#alreadyAddedWlName_cart").val(JSON.stringify(addedWlList_cart));
    }

    $("#wishlistTbodyId").html(wishListContent);
    $('#selectedProductCode').attr('value',productCode);
    $('#proUssid').attr('value',ussid);

}

function selectWishlist(i,productCode, ussid)
{
    $("#hidWishlist").val(i);
}
function loadDefaultWishLstForCart(productCode,ussid) {

    var wishListContent = "";
    var wishName = $("#defaultWishId").text();
    $("#wishListNonLoggedInId").hide();
    $("#wishListDetailsId").show();

    wishListContent = wishListContent
        + "<tr><td><input type='text' id='defaultWishName' value='"
        + wishName + "'/></td></td></tr>";
    $("#wishlistTbodyId").html(wishListContent);
    $('#selectedProductCode').attr('value',productCode);
    $('#proUssid').attr('value',ussid);
}


$(document).ready(function () {
    luxurycheckIsServicable();
    
    $('.checkout-paymentmethod .payment-tab').removeClass('active');
    $('#card').css('display','none');
    
    $('.checkout-paymentmethod #COD').css('display','none');   
    
    $('.credit_tab').on('click',function(){
        if($('.new_card_tab.credit_tab').hasClass('active_tab')){
        $('.newCardPaymentCC').show();
        };
    }); 
    
    $('#sameAsShipping').on('click',function(){
        if($('#sameAsShipping').is(":checked")){
            $('.payment-billing-form').hide();
        }else{ 
            $('.payment-billing-form').show();
        }   
    });
    
    $(document).on("click", ".gig-rating-button", function() {
        $(".ratingsAndReview").trigger("click");
    });
    
    
    $(document).on("click", ".variant-select", function(){
    	$(".add-to-wl-pdp").removeClass("added");
    	if($("a#myWishlistHeader").length > 0){
    		$("a#myWishlistHeader").trigger("mouseover");
    	}
	});
    
    $(document).on("click",".lux-remove-entry-button",(function(){
        $(window).scrollTop(0);
    }));
    
    $("body.page-cartPage .cart.wrapper .checkout-types div#checkout-id").on("mouseover",function(){
        if($(this).find("a#checkout-enabled.checkout-disabled").length > 0){
            $(this).css("cursor","not-allowed");
        }
        else{
            $(this).css("cursor","default");
        }
    });  
    
    luxuryHeaderLoggedinStatus = false;
    isDuringCheckout = false;
    TATA.CommonFunctions.init();
    TATA.Pages.init();
    $("#gender, .select-bar select, #stateListBox, .responsiveSort").selectBoxIt();
    $('.header-login-target-link').on('click', function(){
        var targetID = $(this).data('target-id');
        $('#header-account').removeClass('active-sign-in active-sign-up active-forget-password').addClass('active-'+targetID);
    });
    if(TATA.CommonFunctions.getUrlParameterByName("showPopup") === "true"){
        $(".luxury-login").trigger("click");
        if(window.location.href.indexOf("/cart") > 0){
            isDuringCheckout = true;
            window.history.pushState({}, null, '/cart');
        }else{
            window.history.pushState({}, null, '/');
        }
    }
    
    /* CHECKOUT PAGE*/
    
    if($(".progress-barcheck").hasClass("choosePage")){
		$(".step-1").addClass("active");
		$(".progress-barg span.step").addClass("step1");
		$(".step-2,.step-3").addClass("in-active");		
		
	}
	else if ($(".progress-barcheck").hasClass("selectPage")){
		$(".step-2").addClass("active");
		$(".step-1").addClass("step-done");
		$(".progress-barg span.step").addClass("step2");
		$(".step-3").addClass("in-active");		
	}
	else if  ($(".progress-barcheck").hasClass("paymentPage")){
		$(".step-3").addClass("active");
		$(".step-1,.step-2").addClass("step-done");
		$(".progress-barg span.step").addClass("step3");
	}
    
    
    /*POPULAR SEARCHES COLLAPSE START*/
    
    $(".footer-popular-accordian-title").click(function () {      
        $(".footer-popular-search .mega-menu").slideToggle('400', function() {
            $(window).scrollTop($(document).height());
        });
        $("#footer-popular-accordian-icon").toggleClass('glyphicon-minus','glyphicon-plus');
    });
    
    /*POPULAR SEARCHES COLLAPSE END*/
    
    /*PINCODE CHANGES IN CARTPAGE*/    
        $('.cartItemBlankPincode .defaultPinCode').on('click', function(e){
            e.stopImmediatePropagation(); 
            if($(window).width() >=768){
                $("html, body").animate({ scrollTop: $(".cartBottomCheck").position().top}, 800);                
            }
        });   
    /*PINCODE CHANGES IN CARTPAGE*/
});

$(window).scroll(function () {
    TATA.CommonFunctions.WindowScroll();
});

checkforPriceFilter=function(){
	console.log("check for sort Parameter");
	var sortValue=$.urlParam("sort");
	if(null !=sortValue){
		console.log($.urlParam("sort"));
		var sorting="relevance";
		if(sortValue ==="isProductNew"){
			console.log("Is PRoduct New");
			var sorting="new";
		}
		else if (sortValue ==="isDiscountedPrice"){
			console.log("Is Discounted Price");
			var sorting="discount";
		}
		else if (sortValue ==="price-asc"){
			console.log("price Ascending");
			var sorting="low";
		}	
		else if (sortValue ==="price-desc"){
			console.log("price Decending");
			var sorting="high";
		}	
		else{
			console.log("Releavance-Default");
		}
		console.log($('.responsiveSort option[value='+sorting).text());
		$('span .selectboxit-text').html($('.responsiveSort option[value='+sorting).text());
		$('.responsiveSort option[value='+sorting).attr('selected','selected');

	}
	else{
		console.log("no sort parameters in the URL");
	}
}

$.urlParam = function(name){
	var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	if(null != results){
		return results[1] || 0;
	}
	return null;
}



$(document).ready(function (){
    $("#clear_filter").click(function(){
        $(".reset-filters").trigger("click"); 
    });
    $("#apply_filter").click(function() {
        $(".plp-leftbar-close a").trigger("click");
    });
    checkforPriceFilter();
});

$(document).ready(function() {
    $("body.page-cartPage #checkout-id a").removeClass("checkout-disabled");
    $("body.page-cartPage #checkout-id a").click(function (event) {
        if ($("#defaultPinCodeIds").val() === '' || $("#defaultPinCodeIds").val().length == 0) {
            $("#defaultPinCodeIds").focus();
            $("#defaultPinCodeIds").css('border', '1px solid red');
            event.preventDefault();
            return false;
        }
    })
});

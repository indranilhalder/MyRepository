if ( ! window.TATA )
{
    window.TATA = {};
}
var TATA = window.TATA;

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
                    number:true,
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
            		minlength:"Please enter at least 10 numbers.",
            		maxlength:"Please enter no more than 10 numbers.",
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
    	
    	var $brandSliderWrapper = $('.brand-slider-wrapper');
    	$brandSliderWrapper.html($.trim($brandSliderWrapper.html()));

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
                        slidesToShow: 1,
                        slidesToScroll: 1,
                        //centerMode: true,
                        arrows: false
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
                dots: true
            });
        }
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

    wishlistInit: function(){
        TATA.CommonFunctions.luxuryForceUpdateHeader();
        $(document).on("click, touchstart",".add-to-wishlist",function(){
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
            }
        });
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
        	 $(".facet").addClass("open");
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
            if($(window).scrollTop() >= $('.product-grid:last-child').offset().top - 100){
                $('.leftbar').removeClass('sticky-leftbar');
            }else{
                $('.leftbar').toggleClass('sticky-leftbar', $window.scrollTop() > leftbarElelTop);
            }
        }

    },

    Header: {
        MobileMenu: function() {
            $('#hamburger-menu').on('click', function(){
                $('body').addClass('menu-open');
            });
            $('#main-nav-close').on('click', function(){
                $('body').removeClass('menu-open');
            });
            $('#main-nav .sub-menu-toggle').on('click', function(){
                $(this).toggleClass('active').next('.sub-menu').toggleClass('active');
            });
        },
        HeaderMinicart: function(){
            $('header .mini-bag').hide();
            $('header .mini-cart-link,header #myWishlistHeader').html('');
            $("header .bag").hover(function () {
                    $(this).find('.mini-bag').show();
                },
                function () {
                    $(this).find('.mini-bag').hide();
                });
        },

        init: function() {
            this.MobileMenu();
            this.HeaderMinicart();
        }
    },

    Footer: function() {
        if($(window).width() >= 768){
            $('footer').find('.accordion').removeClass('accordion');
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
        _self.wishlistInit();
        _self.leftBarAccordian();
        _self.deliveryaddressform();
        _self.swipeLookBook();  
        _self.removeProdouct();         
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
            var item = $(".responsiveSort").val();
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
                    lazyInterface:'Y'
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
                    if($(filtered).has('.product-grid')){
                        $('.product-grid-wrapper').append($(filtered).find(".product-grid-wrapper"));
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
                $(".plp-wrapper h4.categor-name").show();
            }
            $(".facetValues .facet-form input:checked").each(function() {
                $(this).parents(".allFacetValues").show(), $(this).parents(".facet").addClass("open");
            });
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
                var resetUrl = $(this).data("resetqueryurl") + $(".responsiveSort").find(":selected");
                TATA.Pages.PLP.performAjax(resetUrl);
            }), $(document).on("click", ".remove-filter", function() {
                var relevantCheckbox = $(this).attr("data-facetCode");
                $("#" + relevantCheckbox).click();
            }), $(document).on("change", ".facet-form input:checkbox", function() {
                var requestUrl = $(this).closest("form").attr("action") + "?" + $(this).closest("form").serialize();
                $(".plp-wrapper h4.categor-name").hide();
                TATA.Pages.PLP.performAjax(requestUrl);
            });
        },
        performAjax: function(requestUrl) {

            $('body').addClass('loader');

            $.ajax({
                url: requestUrl,
                data: {
                    lazyInterface: "Y"
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
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
                    $(".product-grid").hover(function () {
                            $(this).addClass("hover");
                            $(this).find('.plp-hover-img').show().siblings().hide();
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
                complete: function(){
                    $('body').removeClass('loader');
                    $('.plp-leftbar-close a').on('click', function(){
                        $('.leftbar').removeClass('active');
                        $('.facet').removeClass('open')
                    });
                }
            });
        },

        Filtershow: function() {
            $('.plp-mob-filter').on('click', function(){
                $('.leftbar').addClass('active');
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
                    $(this).addClass("hover");
                    $(this).find('.plp-hover-img').show().siblings().hide();
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
                    $(".wishAddSucess").addClass("active");
                    setTimeout(function(){
                        $(".wishAddSucess").removeClass("active")
                    },3000);
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
                    $(".wishRemoveSucess").addClass("active");
                    setTimeout(function(){
                        $(".wishRemoveSucess").removeClass("active")
                    },3000);
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
        },
        
        writeReview:function() {
        	$('body').on('click', '.gig-rating-writeYourReview', function () {
        		$('.accordion-title').removeClass('active');
        		$('.accordion-content').hide();
        		$('.review-accordion').addClass('active');
        		$('.review-accordion-content').show();        		
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
        }
    },

        MYACCOUNT: {
    	init: function(){
    		$(".edit").on('click', function(e) {
    			var addressId = $(this).attr("data-addressId");
    			TATA.Pages. MYACCOUNT.editLuxuryAddress(addressId);
    	});
         
            $(".view-details").on('click', function(e) {
                
                $(".saved-cards").find(".paymentItem").toggleClass("active");                
               
            });
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
                    $("#addressId").val(addressId), $("#firstName").val(data.firstName), $("#lastName").val(data.lastName), 
                    $("#line1").val(data.line1), $("#line2").val(data.line2), $("#line3").val(data.line3), 
                    $("#postcode").val(data.postcode), $(".address_landmarks").val(data.landmark), $(".address_landmarkOther").val(data.landmark), 
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


        
        
$(document).ready(function () {
   /* checkPincodeServiceability();
    populatePincodeDeliveryMode();*/
    
	  $(document).on("click", ".gig-rating-button", function(){
	    	$(".ratingsAndReview").trigger("click");
		});
    var luxuryluxuryHeaderLoggedinStatus = false;
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
});

$(window).scroll(function () {
    TATA.CommonFunctions.WindowScroll();
});

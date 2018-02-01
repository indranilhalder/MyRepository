/*----------Start of  validate email in feedback-----------*/
	mobileVal=false,deskVal=false,styleMobContainer=null,longStyleContainer=null,styleDeskContainer=null;
	function validateEmail()
    { 	 var x = document.getElementById("emailField");
		 if (/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(x.value))  
		  {  
			$("#invalidEmail").hide();
			return true;
		  }  
    }
	
	function validateEmailOnSubmit()
    { 	 var x = document.getElementById("emailField");
   
    if(x.value.trim().length<=0){
		$("#invalidEmail").show();
	 	return false;
	}else if (!(/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(x.value)))  
		  {  
		 	$("#invalidEmail").show();
		 	return false;
		 } else {
			 $("#shareFeedback").hide();
			 return true;
		 }
		 
    }


	
	function validateFeedback()
    { 	 var x = document.getElementById("commentNO");
		 if (x.value.trim().length>0)  
		  {  
			$("#invalidFeedback").hide();
		    return true;
		  }  
    }

	
	function validateFeedbackOnSubmit()
    { 	 var x = document.getElementById("commentNO");
		 if (x.value.trim().length<=0)  
		 { 	
		 	$("#invalidFeedback").show();
		 	$("#shareFeedback").show();
		 	return false;
		 } else {
			 return true;
		 }
    }
	
	/*---------- END of validate email in feedback-----------*/

/*------Start of Authentic Exclusive function ---*/
	
	function authentic() {
		var $li = $(".page-authenticAndExclusive ul.feature-brands li");
		for(var i = 0; i < $li.length; i+=3) {
			var a,b,c,d;
			a=parseInt($li.eq(i).find(".image").css("height"))+parseInt($li.eq(i).find(".logo").css("height"))+parseInt($li.eq(i).find(".logo").css("margin-bottom"))+parseInt($li.eq(i).find(".logo").css("margin-top"))+parseInt($li.eq(i).find("span").css("height"));
			b=parseInt($li.eq(i+1).find(".image").css("height"))+parseInt($li.eq(i+1).find(".logo").css("height"))+parseInt($li.eq(i+1).find(".logo").css("margin-bottom"))+parseInt($li.eq(i+1).find(".logo").css("margin-top"))+parseInt($li.eq(i+1).find("span").css("height"));
			c=parseInt($li.eq(i+2).find(".image").css("height"))+parseInt($li.eq(i+2).find(".logo").css("height"))+parseInt($li.eq(i+2).find(".logo").css("margin-bottom"))+parseInt($li.eq(i+2).find(".logo").css("margin-top"))+parseInt($li.eq(i+2).find("span").css("height"));
			
			if ( a > b && a > c ) {
					d = a; 
				}
		      else if ( b > a && b > c ) {
		    	  d = b;
		      }
		      else {
		    	  d = c;
		      }
			$li.eq(i).css("min-height",d);
			$li.eq(i+1).css("min-height",d);
			$li.eq(i+2).css("min-height",d);
		}

	}
	
/*------END of Authentic Exclusive function ---*/
	
/*--- Start of A-Z column function---*/
	
	function atoz_listing(){
		$('.brands-page').find("ul .desktop ul.list_custom li").removeClass("columns-4-mobile");
	var list_count;
	$('.brands-page').find("ul .desktop ul.list_custom li").each(function(){
		list_count=$(this).find("a").length;
		if(list_count <= 6)
		{				
			$(this).addClass("columns-1");
		}
		else if(list_count > 6 && list_count <= 12)
			{
				$(this).addClass("columns-2");
			}
		else if(list_count > 12 && list_count <= 18)
		{
			$(this).addClass("columns-3");
		}
		else
		{
			$(this).addClass("columns-4");
		}
	});
	
}		
	
/*--- END of A-Z column function---*/

//TISPRO-508	
	
	function closing() {
		$("#zoomModal").modal('hide');
		$("#zoomModal").removeClass("active");
	}
	function closingVideo(){
		$("#videoModal").modal('hide');
		$("#videoModal").removeClass("active");
		var x = $("#player").attr('src');
		var z = $("#player").attr('src', x+"&autoplay=0");
	}	
	
	
$(document).ready(function(){
	/*alert("fhgfhfgh");
	$(".shopstyle-indicator li").on("click", function(){


	    $("#rotatingImageTimeout").trigger("to.owl.carousel", [toIndex, $(this).index , true]);


	});*/

	//TISEEII-640 issue fix -- Start
	$(".facet.js-facet .js-facet-name").each(function(){
		var x = $(this).html().length; 
		//if(x == "6")
			if($.trim($(this).html())==''){
			//alert("no data");
				$(this).parent().hide();
			}
	});
	//TISEEII-640 issue fix -- End
	
	$(document).keydown(function(e){
		if(e.which == 27) {
			$('.modal').modal('hide');
		}
	}); 

	/*------------Start of SNS auto complete----------*/
			
			var style = null ;
			// For INC144315410
			var findGetParameter = function findGetParameter(parameterName) {
 			    var result = null,
 		        tmp = [];
 			    location.search
 			    .substr(1)
 		        .split("&")
 		        .forEach(function (item) {
 		        tmp = item.split("=");
 		        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
 		    });
 		    return result;
			}

			var isLux = findGetParameter('isLux');
			//console.log("isLux"+ isLux);
			var isLuxury = $("#isLuxury").val();
			//console.log("isLuxury"+ isLuxury);
			if (isLuxury != "true" || isLuxury == "undefined"){
				//console.log("isLuxury"+ isLuxury);
				isLuxury = false ;
			}
			if ( isLux == "false"){
				//console.log("isLux"+ isLux);
				isLux = false ;
			}
			
			var marketplaceHeader = (isLux || isLuxury) ? false : true ;
			//console.log("marketplaceHeader"+ marketplaceHeader);
			if (isLux == "true"){
				$(".marketplace-header").css("visibility","hidden");
			}
			else{
				$(".marketplace-header").css("visibility","visible");
			}
			if(marketplaceHeader){
			if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login")))
				{
					 if($(window).scrollTop() == 0){
						 window.setTimeout(function(){
							 style = "display:block;width: "+$("#js-site-search-input").css("width")+"; left: "+$("#js-site-search-input").offset().left+"px";
							 
							 $("#ui-id-1").attr("style",style);
						 },100);
						 
					 }	
					
					 style = "display:block;width: "+$('#js-site-search-input').css('width')+"; left: "+$('#js-site-search-input').offset().left+"px";
				}
			}
			  $("ul#ui-id-1").attr("style",style);
			  
			  $("#js-site-search-input").keypress(function(){
	
				  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css("border","1px solid #dfd1d5");
				});
	
				if($('body').hasClass("template-pages-layout-micrositePage1") && marketplaceHeader){
				    $(window).scroll(function () {
					  if($(".ui-autocomplete").is(":visible")){
					  window.setTimeout(function(){
						  $("#js-site-micrositesearch-input").parents('form#search_form_microsite').next('.ui-autocomplete.ui-front.links.ui-menu').css({
							  left : $('#js-site-micrositesearch-input').offset().left,
							  width: $('#js-site-micrositesearch-input').outerWidth()
						  });
						  },300);
					  }
					  if($(window).scrollTop() > 0){
					    	
					    	$("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').hide();
					    }
  					});    
				  $('#js-site-micrositesearch-input').keydown(function () {
					  $("#js-site-micrositesearch-input").parents('form#search_form_microsite').next('.ui-autocomplete.ui-front.links.ui-menu').css({
						  left : $('#js-site-micrositesearch-input').offset().left,
						  width: $('#js-site-micrositesearch-input').outerWidth()
					  });
			  });
		  }
			  
	/* -----------------END  of SNS auto complete---------*/
		
	/* -----------------Start of Out of stock styling---------*/
		 $('a.stockLevelStatus').parents('div.image').find('a img').addClass('out-of-stock-product');
		 
	/* -----------------END  of Out of stock styling---------*/
		 
		 
	/*----start of Sticky Bag --------*/
		 
			 /*var x = $(".js-mini-cart-count").text();*/		/*commented as part of UF-249*/
			 /*$(".js-mini-cart-count-hover").text(parseInt($(".js-mini-cart-count").text()));*/		/*commented as part of UF-249*/
			   
				  
			   $(window).scroll(function () {
				   if(marketplaceHeader){
					  /*--------changes for Sticky Header in MyBag--------------*/
				   if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login"))){
						 if( $(window).scrollTop() > $('.bottom').offset().top && !($('.bottom').hasClass('active'))){
				      $('.bottom').addClass('active');
				      $('.productGrid-header-wrapper').addClass('active-header');	
			    		//$(".productGrid-header-wrapper").addClass("active_adjust");
			      /*TPR-4471 change*/
				      $('header > .content .top').addClass('header_fix');
				    } else if ($(window).scrollTop() == 0){
				      $('.bottom').removeClass('active');
				      $('.productGrid-header-wrapper').removeClass('active-header');
				      //$(".productGrid-header-wrapper").removeClass("active_adjust");/*TPR-4471 change*/
				   $('header > .content .top').removeClass('header_fix');
				    }
					  }
				   }
				  }); 
	 /*----END of Sticky Bag --------*/
	
	/*----------start of changes to get search-text and category and assign to hidden fields-----------*/
		
		
			var dropText = document.getElementById("searchCategory");
			if(dropText!= null && dropText != "undefined" && dropText.selectedIndex >= 0)
			{
				var searchCategoryText = dropText.options[dropText.selectedIndex].text;
				var $wholeText = $("div.results").text();
				var positon = $wholeText.indexOf("f");
				var quotedText= $wholeText.slice(positon+4);
				var res1 = quotedText.substr(quotedText.indexOf("\"")+1);
				var res2 =res1.slice(0,res1.indexOf("\"")); 
				var searchTextFeedback = $('#text').val();
				$("#searchCategoryhidden").val(searchCategoryText);
				$("#searchText").val(searchTextFeedback);			  
			}
	/*----------start of changes to get search-text and category and assign to hidden fields-----------*/
		
		
	/*-------Start of saved cards card details in My account -------*/
		
		
			$(".saved-cards .number paymentItem .view-details").click(function(){
	
				if ($(this).parent().hasClass('active'))
				{
					$(".saved-cards .number.paymentItem").removeClass("active");
				}
				else
				{
					$(".saved-cards .number.paymentItem").addClass("active");
				}
			});
			
	/*-------END of saved cards card details in My account -------*/
	
	/*----- Start of selecting drop-down text in search box header---------*/	
			$(".select-list ul li").on("click", function(){
				  $(".selected-dropdownText").text($(this).text());
				});
			
	/*----- END  of selecting drop-down text in search box header---------*/
			
	/*-----------Start of SERP codes-----------------*/ 
			$(window).on("load resize", function() {
				if($(".toggle-filterSerp").css("display") == "block"){
				/*$(".facet-name.js-facet-name h4").first().addClass("active-mob");
				$(".facet-name.js-facet-name h4").parent().siblings().hide();
				$(".facet-name.js-facet-name h4.active-mob").parent().siblings().show();*/
				}
				else{
				$(".facet-name.js-facet-name h3").removeClass("active-mob");
					$(".facet_desktop .facet-name.js-facet-name h3").each(function(){
					if($(this).hasClass("active")){
						$(this).parent().siblings().show();
						$(this).parent().siblings().find("#searchPageDeptHierTree").show();
						$(this).parent().siblings().find("#categoryPageDeptHierTree").show();
					}
					});
				}
				
				$(".store-locator-header,.download-app").parent("ul").css("display","block");		/*PRDI-261*/
				
				});
			
			var sessionFacetName;
			$(document).on("click",".facet-name.js-facet-name h3",function(){
				if(typeof(Storage) !== "undefined") {
						if($(this).parents().hasClass("facet_mobile")){
							$(".facet-name.js-facet-name h3").removeClass("active-mob");
							$(".js-facet-top-values.active").removeClass("selectedMobile");
							$(".facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden").removeClass("selectedMobile");
							$(this).parents(".facet_mobile").siblings().find(".facet-values.js-facet-values.js-facet-form").hide();
							if($(this).parent().siblings('#searchPageDeptHierTreeForm').length == 0){
								$('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide();
							}
							else{
								$(".facet-values.js-facet-values.js-facet-form").hide();
							}
							if($(this).parent().siblings('#categoryPageDeptHierTreeForm').length == 0){
								$('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide();
							}
							else{
								$(".facet-values.js-facet-values.js-facet-form").hide();
							}
							$(this).addClass("active-mob");
							$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').show();
							$(this).siblings('.brandSelectAllMain').show();
							$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").show();
							$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").show();
							$(this).parent().closest("li").find(".js-facet-top-values.active").addClass("selectedMobile");
							$(this).parent().closest("li").find(".facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden").addClass("selectedMobile");
						}
						else{
					    if($(this).hasClass('active')) {
					    	$(this).removeClass('active');
					    	$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').hide(100);
					     	$(this).siblings('.brandSelectAllMain').hide(100);
					    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide(100);
					    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide(100);
					    } else {
					    	$(this).addClass('active');
					    	$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').show(100);
					    	$(this).siblings('.brandSelectAllMain').show(100);
					    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").show(100);
					    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").show(100);
					    }
					    
						}
					    	sessionFacetName = $(this).text();
							if(sessionStorage.getItem(sessionFacetName) == null){
								sessionStorage.setItem(sessionFacetName, false);
							} else{
								if($(this).hasClass('active')) {
									sessionStorage.setItem(sessionFacetName, true);
								} else {
									sessionStorage.setItem(sessionFacetName, false);
								}
							}
					    
							
				}
				else {
			        document.getElementById("result").innerHTML = "Sorry, your browser does not support web storage...";
			    }
			});
			
			
			$(".js-facet").each(function(){
				if(sessionStorage.getItem($(this).find('.js-facet-name > h3').text()) == "false" && null != sessionStorage.getItem($(this).find('.js-facet-name > h3').text())) {
					$(this).find('.js-facet-name h3').removeClass('active');
					$(this).find('.facet-values.js-facet-values.js-facet-form').hide(100);
					$(this).find('.brandSelectAllMain').hide(100);
				}
			});
			if(sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text()) == "false" && null != sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text())) {
				$('ul.product-facet > .js-facet-name > h3').removeClass('active');
				$('#searchPageDeptHierTreeForm #searchPageDeptHierTree').hide(100);
		    	$("#categoryPageDeptHierTreeForm #categoryPageDeptHierTree").hide(100);
			}
			
			
			//Moved out from document-ready
			/*$(".toggle-filterSerp").click(function(){
				$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
				$(this).toggleClass("active");
				});*/
			$(".product-facet .facet-list li input.applied-color").each(function(){
				var appliedColor = $(this).attr("value");
				$(".product-facet li.filter-colour a").each(function(){
					var availableColors = $(this).attr("title");
					if( appliedColor ==  availableColors ){
						$(this).parent().addClass("active");
					}
				});
			
			});	
			var search_count_text= $(".searchString i").text().trim();
			$(".searchString i").text(search_count_text);
	$(".facet-list.filter-opt li").each(function(){
		if($(this).find("input.colour").length > 0){
			var selected_colour = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Colour .facet-list li.filter-colour").each(function(){
			var colour_name = $(this).find("input[name=facetValue]").val().split("_", 1);
			if(colour_name == selected_colour){
				$(this).addClass("selected-colour");
			}
			if(selected_colour == "Multicolor"){
				if(colour_name == "Multi"){
					$(this).addClass("selected-multi-colour");
				}
			}
			});
		}
		if($(this).find("input.size").length > 0){
			var selected_size = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Size .facet-list li.filter-size").each(function(){
				var size_val = $(this).find(".js-facet-sizebutton").val();
				if(size_val == selected_size){
					$(this).addClass("selected-size");
				}
			});
		}
	});
	/*-----------END of SERP Codes-----------------*/ 
	
		
	/*--------------Common JS FUNCTIONALITY CODES----------*/
		
			$(".lookbook-text").each(function(){
				if($(this).children().length == 0) {
					$(this).parent().css("margin-bottom","0px");
					}
			});
			$(".electronics-brand .feature-brands").each(function(){
				if($(this).children().length == 0) {
					$(this).parents(".brands").hide();
					}
			});
			$(".apparel-brand .feature-brands").each(function(){
				if($(this).children().length == 0) {
					$(this).parents(".brands").hide();
				}
			});
			$(".feature-collections .collections .chef").each(function(){
				if($(this).children().length == 0) {
					$(this).hide();
				}
			});
			
			$(document).on("click",".toggle",function(e){
				var p = $(e.currentTarget).parent();
			    if(p.hasClass('active')) {
			      p.removeClass('active');
			    } else {
			      p.addClass('active');
			    }
			   /* var style=null;
				if($(window).width() < 773) {
					$("span#mobile-menu-toggle").unbind('click');
					$(document).on("click", "span#mobile-menu-toggle", function(){
						$("a#tracklink").mouseover();
						$(this).parent('li').siblings().find('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
						$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
						$(this).next().slideToggle();
						$(this).toggleClass("menu-dropdown-arrow");
					});
					--- Mobile view shop by brand and department ---

					$("li.short.words").siblings("li.long.words").hide();
					  $("li.short.words").unbind('click');
					  $("li.short.words").click(function(){
						$(this).toggleClass('active');
					    $(this).nextAll().each(function(){

					      if($(this).hasClass("short")) {
					        return false;
					      }

					      $(this).toggle();
					    });

					  });

					--- Mobile view shop by brand and department --- 
				}
				else {
					$("#mobile-menu-toggle").next().attr("style",style);
					$("li.short.words,li.long.words").next().attr("style",style); 
				} */
			});
			$(document).on("click","footer h3.toggle",function(e){
				
					if ($(window).width() > 790) {
						$(e.currentTarget).parent().removeClass("active");
					} 
			 }); 
			$(window).on("load resize", function() {
				if ($(window).width() > 790) {
					$("footer h3.toggle").parent().removeClass("active");
				}
			});
			$(".close").on("click",function(e){
				$(e.currentTarget).closest('.banner').removeClass('active');
			});
			
			$(".toggle a").on("click",function(e){
				e.stopPropagation();
				var p = $(e.currentTarget).parent().parent();
				if(!p.hasClass('active')) {
				      p.addClass('active');
				    } else {
				      window.location.href=$(e.currentTarget).attr('href');
				   }
			});
	
	/*------- END of Common functionality codes ------*/  
			
	/*----Start of SignIn & SignUp tab Switching -----*/
			
			 $(document).on("click","#signIn_link",function(e) {
			      $(this).addClass('active');
			      $("#SignUp_link,#sign_up_content").removeClass("active");
			      $("#sign_in_content").addClass("active");
			    });
			 $(document).on("click","#SignUp_link",function(e) {
			      $(this).addClass('active');
			      $("#signIn_link, #sign_in_content").removeClass("active");
			      $("#sign_up_content").addClass("active");
			    });
	/*----Start of SignIn & SignUp tab Switching -----*/
			 
			 /*----Start of  PDP tabs -----*/
			 $(".SpecWrap .Padd .tabs-block .nav > li:first-child, .SpecWrap .Padd .tabs-block .tabs > li:first-child").addClass("active");
			 $(".tabs-block .nav.pdp li").on("click",function(e) {
				 //$("ul.nav.pdp li").removeClass('active');
				 $(this).siblings().removeClass('active');
				 $(this).addClass('active');
				 var count = $(this).index() + 1;
				 if ($(this).parent().hasClass('productNav')){
					 $(this).parents().find("ul.tabs.pdp.productTabs>li").removeClass('active');
					 $(this).parents().find("ul.tabs.pdp.productTabs>li:nth-child("+count+")").addClass("active");
				 }
				 else {
					 $(this).parents().find("ul.tabs.pdp.specTabs>li").removeClass('active');
					 $(this).parents().find("ul.tabs.pdp.specTabs>li:nth-child("+count+")").addClass("active");
				 }
				 //$("ul.tabs.pdp>li").removeClass('active'); 
				// $("ul.tabs.pdp>li").eq(count).addClass("active");
			    }); 
	/*----END of  PDP tabs -----*/

	/*----START-- Added for tab horizontal scrolling while working on Jewellery  -----
			 
			 
			 $(".FineJewellery .nav.pdp, .FashionJewellery .nav.pdp").owlCarousel({
					items: 5,
					loop: false,
					nav: false,
					dots: false,
					navText: [],
					touchDrag: true,
					mouseDrag: true,
				    responsive: {
						0 : {
						  items: 2,
						  autoHeight: false,
						  stagePadding: 0,
						},
						480 : {
						   items: 2,
						  autoHeight: false,
						},
						768 : {
						   items: 4,
						   autoHeight: false,
						   stagePadding: 0,
						},
						980 : {
						   items: 4,
						},
						1024 : {
						   items: 4,
						}
					 }
				});
			 
	----END-- Added for tab horizontal scrolling while working on Jewellery  -----*/
			 
	/*----Start of  PDP tabs for Jewellery -----*/
			 
			 if($(".tabs-block").hasClass("FineJewellery") || $(".tabs-block").hasClass("FashionJewellery")){			 
				 $(".tabs-block.FineJewellery .nav.pdp .owl-item, .tabs-block.FashionJewellery .nav.pdp .owl-item").on("click",function(e) {
					 var count = $(".tabs-block .nav.pdp .owl-item").index(this);
					 $(".tabs-block .nav.pdp .owl-item").removeClass("current");
					 $(this).addClass("current");
					 $("ul.tabs.pdp>li").removeClass("active");
					 $("ul.tabs.pdp>li").eq(count).addClass("active");
			    }); 
			 }
	/*----END of  PDP tabs for Jewellery -----*/
	
	/*----Start of  SHop by brand A_E hover functionality  -----*/
			 $(".range").hide();
			 $(".range.current").show();
			 var id= $(".range.current").attr('id');
			
			 $('[data-tab='+id+']').css({
					"border-bottom":"3px solid",
					"font-weight":"bold"
				});
			 $('nav>ul>li>ul>li>.toggle').hover(function(){
				 $(".range").removeClass('current');
				 $('#A-E').addClass('current');
				  $(".range").hide();
			 $(".range.current").show();
			 var id= $(".range.current").attr('id');
			 $('.brandGroupLink').css({
				 "border-bottom":"none",
					"font-weight":"400"
				});
			 $('[data-tab='+id+']').css({
				 "border-bottom":"3px solid",
					"font-weight":"bold"
				});
			 })
	/*----END of  SHop by brand A_E hover functionality  -----*/
			 
	/*---Start of Checkout Payment tab switching  ----*/
			 var paymentModes =  $("#viewPaymentCredit, #viewPaymentDebit, #viewPaymentNetbanking, #viewPaymentCOD, #viewPaymentEMI,#viewPaymentCreditMobile, #viewPaymentDebitMobile, #viewPaymentNetbankingMobile, #viewPaymentCODMobile, #viewPaymentEMIMobile,#viewPaymentMRupee, #viewPaymentMRupeeMobile");
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
			 
	 /*---Start of Micro site brand header toggle functionality ---*/
			 
			 if($('body').hasClass('template-pages-layout-micrositePage1')){
	
				 //$(this).find('header').first().addClass('compact');
				 $(this).find('header').first().find('.compact-toggle').addClass('open');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 }); 
				 
			
				 
				/* $(this).find('header').find('.compact-toggle').toggleClass('open');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 });*/
			 }
			/* $(window).scroll(function () {
			 if($("header .content .bottom").hasClass("active")){
					$("header > .content .top .compact-toggle.open").css("display","none");
				}
				else{
					$("header > .content .top .compact-toggle.open").css("display","inline-block");
				}
			 });*/
	/*---END of Micro site brand header toggle functionality ---*/
			 
	/*-------Start of Marketplace preferences--------*/
			  $(document).ready(function() {
				  if($("#radioInterest1").is(":checked")) {
					  $("#radioInterest1").click();
					}
			  })
			 
			 
			 
			 
			 $("#radioInterest1").click(function(){
					$("body .account.preferences .right-account form fieldset span input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset span label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form fieldset .mplPref-category input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset .mplPref-category label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form fieldset .freq input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset .freq label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});	
					$("body .account.preferences .right-account form fieldset input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form .button-set a,body .account.preferences .right-account form .button-set a:hover").css({
						"color":"#000",
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form .button-set a").prop("disabled","true");
				})
				$("#radioInterest0").click(function(){
					var style=null;
					$("body .account.preferences .right-account form fieldset .mplPref-category input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset .mplPref-category label").attr("style",style);
					$("body .account.preferences .right-account form fieldset .freq input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset .freq label").attr("style",style);
					$("body .account.preferences .right-account form fieldset input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset label").attr("style",style);
					$("body .account.preferences .right-account form fieldset span input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset span label").attr("style",style);
					$("body .account.preferences .right-account form .button-set a").removeAttr('disabled');
					$("body .account.preferences .right-account form .button-set a,body .account.preferences .right-account form .button-set a:hover").attr("style",style);
					
				})
				 $("#unsubcribe-link").click(function(e){
				        if($("#unsubcribe-link").attr("disabled")=="disabled")
				        {
				            e.preventDefault(); 
				        }        
				    });  
	/*-------End of marketplace preferences--------*/
				
	/*---Start of Questionnaire---*/
				$("body form.questionnaire-form").parents().find(".breadcrumbs.wrapper").css("display","none");
				
				$("body form.questionnaire-form").parents().find("header").addClass("compact");
				
				$("body div.marketplace-mystyle").find(".breadcrumbs.wrapper").css("display","none");
				
				$("body div.marketplace-mystyle").find("header").addClass("compact");
				
				
					 $("body form.questionnaire-form").parents().find('header').find('.compact-toggle').click(function(){
						 $(this).parents('header').toggleClass('compact');
						 $(this).parents('header').find('.compact-toggle').toggleClass('open');
						/* $(".compact-toggle.open").css({
							 "opacity":"1",
						 });*/
					 });
					 $("body div.marketplace-mystyle").find('header').find('.compact-toggle').click(function(){
						 $(this).parents('header').toggleClass('compact');
						 $(this).parents('header').find('.compact-toggle').toggleClass('open');
						 /*$(".compact-toggle.open").css({
							 "opacity":"1",
						 });*/
					 });
	
				$("#question-0-0,#question-0-1").click(function()
				{
					$(this).parents(".gender").removeClass("active").siblings(".products").addClass("active");
				});
				
				$('.questionnaire-form fieldset .buttons .next').click(function(){
					$(this).parents('fieldset').removeClass("active").next().addClass("active");
				});
				$('.questionnaire-form fieldset .buttons .prev').click(function(){
					$(this).parents('fieldset').removeClass("active").prev().addClass("active");
				});
	
		/*---END of Questionnaire---*/
		
	
		
	/*---Start of codes for A-Z Brands */
	
		    if($(window).resize().width()<=790){
		        var divId='#subcategory-'+$('.brandCategory').eq(0).attr('id');
		        $(divId).show();
		    }
	
		    $('.cmsBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
			    	   $(this).find('.nav-wrapper').hide();
			       }
		    });
		    $('.subBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
		    		$(this).find('.nav-wrapper').hide();
		    	}
		    });
		    $('.cmsMultiBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
		    		$(this).find('.nav-wrapper').hide();
		    	}
		    });
		$('select[name="test"]').change(function(){
			if($(this).find('.brandCategory').is(':selected')){
				var x=document.getElementById("mySelect").selectedIndex;
				var y=document.getElementById("mySelect").options;
				var i=y[x].index;
				$('.cmsBrands').hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	/*$('.cmsBrands').eq(i).show();*/
		    	var divId='#subcategory-'+$(this).find('.brandCategory').eq(i).attr('id');
		        $(divId).show();
		       if($(divId).find('.list_custom li').length == 0){
		    	   $(divId).find('.nav-wrapper').hide();
		       }
		       
			}
		
		if($(this).find('.cmsManagedBrands').is(':selected')){
			var x=document.getElementById("mySelect").selectedIndex;
			var y=document.getElementById("mySelect").options;
			var i=y[x].index;
			var num=$('.brands-page .brands-left #mySelect .brandCategory').length;
			i=i-num;
			$('.cmsBrands').hide();
	    	$(".subBrands").hide();
	    	$(".cmsMultiBrands").hide();
	    	/*$('.cmsBrands').eq(i).show();*/
	    	var divId='#cmsManaged-'+$(this).find('.cmsManagedBrands').eq(i).attr('id');
	    	var divId1='#cmsMultiManaged-'+$(this).find('.cmsManagedBrands').eq(i).attr('id');
	    	$(divId).show();
	    	 $(divId1).show();
	    	 if($(divId).find('.list_custom li').length == 0){
		    	   $(divId).find('.nav-wrapper').hide();
		       }
	    	 if($(divId1).find('.list_custom li').length == 0){
		    	   $(divId1).find('.nav-wrapper').hide();
		       }
		}
		
				var leftListId=$(this).children(":selected").attr("id");
				$(".brands-page .brands-left ul > li").removeClass('active');
		    	$(".brands-page .brands-left ul > li>a").filter(function(){
	  				 return this.id === leftListId
						}).parent('li').addClass('active');
	});
		
	
			
			$('.cmsBrands').hide();
			$(".cmsMultiBrands").hide();
		  
		    $(".brandCategory").click(function(){
		    	$('.cmsBrands').hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	var divId='#subcategory-'+$(this).attr('id');
		    	var selectOptionId=$(this).attr('id');
		    	$('#mySelect option').filter(function(){
	  				 return this.id === selectOptionId
						}).prop('selected', true);
		        $(divId).show();
		    });
		    $(".cmsManagedBrands").click(function(){
		    	$(".cmsBrands").hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	var divId='#cmsManaged-'+$(this).attr('id');
		    	var divId1='#cmsMultiManaged-'+$(this).attr('id');
		    	var selectOptionId=$(this).attr('id');
		    	$('#mySelect option').filter(function(){
	  				 return this.id === selectOptionId
						}).prop('selected', true);
		    	 $(divId1).show();
		        $(divId).show();
		       
		    });
	
	
			$(".saved-cards .number .view-details").click(function(e){
				if ($(e.currentTarget).parent().hasClass('active'))
					{
					$(this).parent().removeClass("active");
					}
				else
					{
					$(this).parent().addClass("active");
					}
			});
			
		atoz_listing();
	    $('.brands-page .list_custom li span.letter').each(function(){
	    	if((($(this).text().toUpperCase().charCodeAt(0)>47) &&($(this).text().toUpperCase().charCodeAt(0)<58)) || (($(this).text().toUpperCase().charCodeAt(0)>64) &&($(this).text().toUpperCase().charCodeAt(0)<72))){
	    		
	    		$(this).addClass('a-g-set');
	    	}
	    	else if((($(this).text().toUpperCase().charCodeAt(0)>71) &&($(this).text().toUpperCase().charCodeAt(0)<79))){
	    		
	    		$(this).addClass('h-n-set');
	    	}
		else if((($(this).text().toUpperCase().charCodeAt(0)>78) &&($(this).text().toUpperCase().charCodeAt(0)<86))){
	    		
	    		$(this).addClass('o-u-set');
	    	}
		else{
			
			$(this).addClass('v-z-set');
		}
	    });
	
	$(".brands-page .list_custom li").hide();
		$(".brands-page .list_custom li span.letter.a-g-set").parent().show();
				
		 // TPR-1287 Start
	    $(".brandCategory").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	var brandCatName = $(this).text().toLowerCase().trim() + "-brands";
	    		    	
	    	//window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    	window.history.pushState('obj', 'newtitle', '/brands/' + brandCatName +'/b-'+elementId.toLowerCase());
	    	
	    	
	    });
	    
	    
	    $(".cmsManagedBrands").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	
	    	var brandCatName = $(this).text().toLowerCase().trim().replace(/\s/g, "-") + "-brands";
	    	
	    	//window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    	window.history.pushState('obj', 'newtitle', '/brands/' + brandCatName +'/b-'+elementId);
	    	
	    });
	  // TPR-1287 end
	
	    
	    $(".brands-page .desktop ul.nav > li").click(function() {
	    	$(".brands-page .desktop ul.nav > li").removeClass("active");
	    	$(this).addClass("active");
	    	var listId=$(this).attr('id');
	    	var letterid=".brands-page .desktop ul.nav > li#"+listId;
	    	var clickedBlock=".brands-page .list_custom li span.letter."+listId;
	    	$('.brands-page .list_custom li').hide();
	    	$(clickedBlock).parent().show();
	    	$(letterid).addClass('active');
			  $("body,html").animate({ scrollTop: $(this).parents('.desktop').offset().top }, "slow");

		});
	    $(".brands-page .brands-left ul > li").click(function() {
	    	$(".brands-page .brands-left ul > li").removeClass("active");
	    	$(this).addClass("active");
	    });
	    
	    
	/*---END of codes for A-Z Brands */
	
	/*-----Start of  for static benefits section --- */
	
	function benifits_height() {
		$('.benefits ul li').each(function(count) {
			var liHeight = ($('.sign-in .tabs li.active').height()+20)/3;
			var liPadding = $('.benefits ul li').eq(count).find("div:last-child").height();
		//	console.log("active"+liHeight);
			$('.benefits ul li').eq(count).css({
													"height" : liHeight+1,
													"padding" : (liHeight-liPadding)/2+"px 20px"
			
												});
			
		});
	}
	
		
		$('.sign-in .nav li, #sign_in_content .button_fwd_wrapper button, #sign_up_content .form-actions button').click(function(){
			benifits_height();
		});
		$(window).on("load resize",function(){
			if($(window).width() >= 790 ) {
				benifits_height();
			}
		});
		
	/*--END of  for static benefits section----- */
	
/* ---Start of Mobile view left nav --*/
		
		if($(window).width() < 790) {
			$(document).on("click","header .content .top .toggle",function(){
				if($(this).parent().hasClass("active")){
					$(this).parent().siblings(".overlay").addClass("overlay-sideNav");
					$("body").css("overflow","hidden");
				}
				else {
					$(this).parent().siblings(".overlay").removeClass("overlay-sideNav");
					$("body").css("overflow","auto");
				}
				
			});
		}
		
	/*---END of Mobile view left nav --*/	
		
		
		
		
		
	/*----Start of Wishlist Codes---------*/
		
		$('.js-rename-wishlist').click(function(){
			$('.rename-wishlist-container').hide();
			$(this).prev('.rename-wishlist-container').toggle();
			$('.js-rename-wishlist').show();
			$('.wishlist-name').show();
			$(this).toggle();
			$(this).siblings('.wishlist-name').toggle();
			
		});
		$('.modal').click( function (event) {
		
		    if(!($(event.target).hasClass('js-rename-wishlist')) && !($(event.target).hasClass('rename_link')) && !($(event.target).hasClass('rename-input'))){
		    	$('.rename-wishlist-container').hide();
		    	$('.js-rename-wishlist').show();
				$('.wishlist-name').show();
		    }
	
		});
		
		/*if($('body').find('a.wishlist#wishlist').length > 0){
		$('a.wishlist#wishlist').popover({ 
	    html : true,
	    content: function() {
	      return $(this).parents().find('.add-to-wishlist-container').html();
	    }
	  });
	  }
	  if($('body').find('input.wishlist#add_to_wishlist').length > 0){
		$('input.wishlist#add_to_wishlist').popover({ 
			html : true,
			placement: function (){
				if(($(window).width() < 768)) {
				return 'top';
				}
				else
					return 'bottom';
			},
			content: function() {
				return $(this).parents().find('.add-to-wishlist-container').html();
			}
		});
	  }*/
	  if($('body').find('a.cart_move_wishlist').length > 0){
		$('a.cart_move_wishlist').popover({ 
			html : true,
			content: function() {
				return $('.add-to-wishlist-container').html();
			}
		});
		}
		if($('body').find('ul.wish-share a.mailproduct').length > 0){
		$('ul.wish-share a.mailproduct').popover({ 
				html : true,
				content: function() {
					var loggedIn = $("#loggedIn").val();
					if(loggedIn=='true') {
						return $(this).parents().find('#emailSend').html();
					} else {
						return '<div style="padding: 10px;">'+$(this).parents().find('#emailLoggedInId').html()+'</div>';
						
					}
				}
			});
			}
		$('ul.wish-share div.share').mouseleave(function(){
			 $(this).find('[data-toggle="popover"]').popover('hide');
		});
	  $('body').on('click', function (e) {
	    $('[data-toggle="popover"]').each(function () {
	        
	        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
	            $(this).popover('hide');
	        }
	    });
	});
	  
	  $('.create-newlist-link').click(function(){
		  $('header .content .top').toggleClass('no-z-index');
	  });
	  $('#createNewList').find("div.overlay").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  $('#createNewList').find("a.close").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  $('#createNewList').find("button.close").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  
	/*--------END of wishlist Codes------*/
	  
	 /*--- Start of function for Internet Explorer video z-index overlapping---*/
	
	  var ua = window.navigator.userAgent;
	  var msie = ua.indexOf("MSIE ");
	
	  if (msie > 0)
	  {      // If Internet Explorer, return version number
		  // alert(parseInt(ua.substring(msie + 5, ua.indexOf(".", msie))));
	
		  $('iframe').each(function(){
			  var url = $(this).attr("src");
			  $(this).attr("src",url+"?wmode=transparent");
		  });
	  }
	  if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)){
		  $("#js-site-search-input").attr("readonly","readonly");
		  $("#mailtext").attr("readonly","readonly");
			$('#js-site-search-input').on( 'click', function() {
			    $(this).removeAttr('readonly').focus().select();
			});
			
			$('#js-site-search-input').on( 'blur', function() {
			    $( this ).prop( 'readonly', 'readonly' );
			});
			$('#mailtext').on( 'click', function() {
				$(this).removeAttr('readonly').focus().select();
			});
			
			$('#mailtext').on( 'blur', function() {
				$( this ).prop( 'readonly', 'readonly' );
			});
			
		  
	  }
	 /*---END  of function for Internet Explorer video z-index overlapping ends----*/
		
		$(".checkout-shipping #addressForm input[type='checkbox']").change(function () {
		    if ($(this).prop( "checked" )===true) {
		        // checked
		       $(this).parent().css("color","#a9143c");
		       $(this).parent().addClass('checkbox-checked');
		       
		    }
		    else {
		    	$(this).parent().css("color","#666");
		    	$(this).parent().removeClass('checkbox-checked');
		    	}
		});
	
		/*---------Start of compareNow----------*/

		var comparsionProductsLength=$(".products-compareTable").find("td").length;
	
		if(comparsionProductsLength%3==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"33.3%",
			})
		}
		else if(comparsionProductsLength%2==0 && comparsionProductsLength%4==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"25%",
			})
		}
		else if(comparsionProductsLength%5==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"20%",
			});
		}
		
	/*---------END of compareNow ----------*/
		        
	
	
	/*-------- Script from Functionality team ( JS.tag script) -------*/
	
	
				var contextPath = ACC.config.contextPath;
				$("input[name=yes]").click(function() {
					$(".search-feedback").hide();
					$(".feed-back-categories").hide();
					$(".feed-back").hide();
					$(".feed-back-form").fadeIn();
				});
	
				$("input[name=no]").click(
						function() {
							$(".search-feedback").hide();
							$(".feed-back-form").hide();
							$(".feed-back-categories").show();
							//ajax call for categories
	
							$.ajax({
								url : contextPath
										+ "/feedback/searchcategotylist",
								type : "GET",
								returnType : "JSON",
								success : function(data) {
									listSelect = "";
								//	console.log(data);
									$.each(data, function(k, v) {
										if(v != null){
											listSelect += '<option value="'+v+'">'
													+ v + '</option>';
											}
									});
								//	console.log(listSelect);
									$("#feedCategory").html(listSelect);
									$(".feed-back").show();
								},
								fail : function(data) {
									alert("Failed to load categories");
								}
							});
						});
	
				$("#feedCategory").change(function() {
					$(".feed-back").show();
				});
	
				//ajax call for saving feedback on Yes
	
				$("input[name=submitFeedBackYes]").click(function() {
					var params = $("#feedBackFormYes").serialize();
					$.ajax({
						url : contextPath + "/feedback/feedbackyes",
						type : "GET",
						returnType : "text",
						data : params,
						success : function(data) {
							if (data == "SUCCESS") {
								alert("Thanks For your FeedBack");
								$(".feed-back-categories").hide();
								$(".feed-back").hide();
								$(".feedback-thankyou").fadeIn();
							}
	
						},
						fail : function(data) {
							alert("Failed to load categories");
						}
					});
				});
	
				$("input[name=submitFeedBackNo]").click(function() {
					var params = $("#feedBackFormNo").serialize();
					if(!validateEmailOnSubmit() && !validateFeedbackOnSubmit()) {
						return;
					}
					
					if(!validateEmailOnSubmit() || !validateFeedbackOnSubmit()) {
						return;
					}
					
					$.ajax({
						url : contextPath + "/feedback/feedbackno",
						type : "GET",
						returnType : "text",
						data : params,
						success : function(data) {
							if (data == "SUCCESS") {
								$(".search-feedback").hide();
								$(".feed-back-categories").hide();
								$(".feed-back").hide();
								$(".feed-back-form").fadeIn();
								/*TPR-4730*/
								if(typeof utag !="undefined"){
									utag.link({ link_text: 'search_feedback_no_submit', event_type : 'search_feedback_no_submit' });
									}	
							}
						},
						fail : function(data) {
							alert("Failed to load categories");
							/*TPR-4730*/
							if(typeof utag !="undefined"){
								utag.link({ error_type : "search_feedback_error" });
								}
							
						}
					});
				});
		
	
				$(".share-trigger").click(
						function() {
							if ($(this).parent().siblings(".share-wrapper").is(
									":visible")) {
	
								$(this).parent().siblings(".share-wrapper")
										.hide();
							} else {
								$(".share-wrapper").hide();
								$(this).parent().siblings(".share-wrapper")
										.show();
							}
						});
				$(".share-wrapper-buybox").hide();
				$(".share-trigger-buybox").click(function() {
					$(".share-wrapper-buybox").toggle();
				});
	
	/*---	END from Functionality team ( JS.tag script) ----*/
				
	/*----- Start of SERP pagination ----*/
				
	/*	$(".pagination").parents(".bottom-pagination").find("li.prev a").append("<span class='lookbook-only'> Page</span>");
		$(".pagination").parents(".bottom-pagination").find("li.next a").append("<span class='lookbook-only'> Page</span>");*/
		
	/*----- END of SERP pagination ----*/		
		
	
	
	
	/*--------Start of PDP pincode placeholder removing on focus ----*/
	
		
		$("#pin").focus(function(){
			$(this).attr("placeholder","");
			$(this).siblings(".placeholder").hide(); /* Fix for IE */
		});
		
		$("#pin").blur(function(){
			if($("#pin").val()=="")
			$(this).attr("placeholder","Pincode");
		});
		
	/*--------END of PDP pincode placeholder removing on focus ----*/	
		
	
	/*---- Start of added for hero products demarcation ---*/
	
	if($(".product-listing.product-grid.hero_carousel .owl-stage").children().length>0){
		$(".product-listing.product-grid.hero_carousel").css("border-bottom","1px solid #e5e5e5");
		$(".product-listing.product-grid.hero_carousel").before("<h3 class='heroTitle'>Shop Our Top Picks</h3>");
		
	}
	
	/*---- END of added for hero products demarcation ---*/
	
	/*---Start of Authentic and exclusive function call---*/
	
		 authentic();
	
		 $(window).on("load resize",function(){
				if($(".js-mini-cart-count").text() != undefined && $(".js-mini-cart-count").text()!=null)
					{
					if($(".js-mini-cart-count-hover").text().length > 1){
						$(".responsive-bag-count").text($($(".js-mini-cart-count-hover")[0]).text());		/*UF-249*/
					}else{
						$(".responsive-bag-count").text($(".js-mini-cart-count-hover").text());		/*UF-249*/
					}
					}
				var $li = $(".page-authenticAndExclusive ul.feature-brands li");
				if($(window).width() <790) {
					$li.css("min-height","0");
				} else {
					 authentic();
				}
			});
	
	/*---END of Authentic and exclusive function call---*/

	/*--- Start of  Mobile view Left menu Sign In toggle---- */
	$(window).on("load resize",function(e){
			var style=null;
			if($(window).width() < 773) {
				  $(document).off("click","span#mobile-menu-toggle").on("click","span#mobile-menu-toggle",function() {
					$("a#tracklink").mouseover();
					$(this).parent('li').siblings().children('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
					$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
					$(this).next().slideToggle();
					$(this).toggleClass("menu-dropdown-arrow");
					
					
					/* $(this).parent('li').siblings("li.short.words").nextAll().each(function(){
						 alert('1')
					      if($(this).hasClass("short")) {
					        return false;
					      }
					     $(this).siblings("li.long.words").hide(200);
					    });
					    $(this).parent('li.short.words').nextAll().each(function(){
					    	alert('2')
						      if($(this).hasClass("short")) {
						        return false;
						      }
						      $(this).toggle(200);
					    });*/
					
				});
				  $(document).on("click","ul.words span#mobile-menu-toggle",function() {
					var id = $(this).parents('ul.words').siblings("div.departmenthover").attr("id"), ind = $(this).parent('li.short.words').index("."+id+" .short.words")
						$(".long.words").hide();
						div_container=$(this).parent(".short.words");
						if($(this).hasClass('menu-dropdown-arrow')){
							/*for(var i = $("."+id+" .short.words").eq(ind).index(); i < $("."+id+" .short.words").eq(ind+1).index()-1; i++) {
								$("."+id+" .long.words").eq(i-ind-1).show();
							}
							if(ind == ($("."+id+" .short.words").length/2)-1) {
								$("."+id+" .short.words").eq(ind).nextAll().show();
							}*/
							for(var i=1;i<$(".long.words").length;i++){
								if(div_container.next().hasClass("long")){
									div_container.next().show();
									div_container=div_container.next();
								}
								else
									break;
								}		
						} else {
							$(".long.words").hide();
						}
						longStyleContainer=$(this).parent(".short.words").next(".long.words").attr("style");
						$(this).parents("li.level1").nextAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
						$(this).parents("li.level1").prevAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
				  });
				/*--- Mobile view shop by brand and department ---*/

				// $("li.short.words").siblings("li.long.words").hide();
			/*	$(document).off("click","li.short.words ").on("click","li.short.words",function() {
				    $(this).siblings("li.short.words").nextAll().each(function(){

				      if($(this).hasClass("short")) {
				        return false;
				      }

				      $(this).siblings("li.long.words").hide(200);
				    });
				    $(this).nextAll().each(function(){

					      if($(this).hasClass("short")) {
					        return false;
					      }

					      $(this).toggle(200);
					    });

				  });
*/
				/*--- Mobile view shop by brand and department ---*/ 
			}
			else {
				$("#mobile-menu-toggle").next().attr("style",style);
				$("li.short.words,li.long.words").next().attr("style",style); 
			}
			
			div_container=$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent(".short.words");
			for(var i=1;i<$(".long.words").length;i++){
				if(div_container.next().hasClass("long")){
					div_container.next().attr("style",longStyleContainer);
					div_container=div_container.next();
				}
				else
					break;
				}	
			
		});
	$(".customer-care-tabs>.tabs .customer-care-tab>ul>li .tabs li").click(function(){
							$(this).nextAll().removeClass( "active" );
							$(this).prevAll().removeClass( "active" );
	 });	 
		
		/*--- END of  Mobile view Left menu Sign In toggle---- */
	/*--- Start of  Mobile view sort by arrow in SERP and PLP---- */
	
	/*$(".progtrckr .progress.processing").each(function(){

		var len = $(this).children("span.dot").length;
		if(len == 2) {
			$(this).children("span.dot").first().css("marginLeft","16.5%");
		} else if(len == 1) {
			$(this).children("span.dot").first().css("marginLeft","33%");		TPR-6013 Order History 
		}

	}); */
	$(".progtrckr .progress.processing").each(function(){
		var len = $(this).children("span.dot").length;
		if(len == 3) {
			$(this).children("span.dot").css("marginLeft","12%");
		}
		if(len == 2) {
			$(this).children("span.dot").css("marginLeft","16.5%");
		} 
		
		if(len == 4) {
			$(this).children("span.dot").css("marginLeft","8%");
		} 

	});
	$(".progtrckr").each(function(){
		//$(this).find(".progress.processing .dot:not(.inactive)").last().find('img').show();
		if($(this).find(".progress.processing .dot:not(.inactive)").last().next(".message").css("display") == "block"){
			$(this).find(".progress.processing .dot:not(.inactive)").last().find('img').show(); //TISPRDT-1570
		}
	});	/*TPR-6013 Order History */

	
	/*$(window).on("load resize",function(){
		if($(window).width()<651)
			{
		$('.feature-collections ul.collections li.chef.sub .simple-banner-component').each(function(){
		var h3_height= $(this).find('h3').height();
		if(h3_height>0){
			h3_height=h3_height + 30;
			$(this).siblings().css('padding-top',h3_height);
		}
		
		});
			}
	});*/
	if ($("#searchPageDeptHierTree").children().length==0){
		
		$("#searchPageDeptHierTree").css("padding","0px");
		$("#searchPageDeptHierTree").parent("form").siblings('li.facet').first().css("border-top","0px");
	}
	if ($(".customer-service").find(".side-nav").length == 0){
		$(".customer-service .left-nav-footer-mobile").css("display","none");
	}
	else{
		$(".customer-service .left-nav-footer-mobile").css("display","block");
	}
	$(".customer-service .side-nav ul li").each(function(){
		var title = $(this).find("a").attr("title");
		var link = $(this).find("a").attr("href");
		if ($(this).hasClass("active")){
			$(".customer-service .left-nav-footer-mobile").append("<option value="+link+" data-href="+link+" selected>"+title+"</option>");
		}
		else{
		$(".customer-service .left-nav-footer-mobile").append("<option value="+link+" data-href="+link+">"+title+"</option>");
		}
	});
	if($("#sameAsShipping").is(":checked")){
		$("#sameAsShipping").prev('h2').hide();
	}
	else{
		$("#sameAsShipping").prev('h2').show();
	}
	$("#sameAsShipping").click(function(){
		if($("#sameAsShipping").is(":checked")){
			$("#billingAddress fieldset .error-message").html("");
			$(this).prev('h2').hide();
		}
		else{
			$(this).prev('h2').show();
		}
		});

	/*$(window).on("load resize", function() {
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
			$(".imageList ul li img").css("height", thumbnailImageHeight);
		});*/
	
	$('.marketplace-checkout').find('a').click(function(e){
		e.preventDefault();
	});
	$(".marketplace-checkout").parents().find('header .content .top .marketplace.compact a').hide();
	  $(window).scroll(function () {
		  if($(".ui-autocomplete").is(":visible") && marketplaceHeader){
			  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
				  /*start change of INC144313747*/
				  /*left : $('#js-site-search-input').offset().left,*/
				  /*end change of INC144313747*/
				  width: $('#js-site-search-input').outerWidth()
			  });
		  }
		  
	  });
	  $('#js-site-search-input').keydown(function () {
		  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
			      /*start change of INC144313747*/  
			  	  /*left : $('#js-site-search-input').offset().left,*/
			  	  /*end change of INC144313747*/
				  width: $('#js-site-search-input').outerWidth()
			  });
	  });
	  
	  $("input[name='j_username']").parents("form#extRegisterForm").attr("autocomplete","off");
		$("input[name='email']").parents("form#loginForm").attr("autocomplete","off");
		$("input[name='j_username'],input[name='email']").attr("autocomplete","off");
		$("input[type='password']").attr("autocomplete","new-password");
		/*$(window).on("load resize", function() {
			var $li = $("body .account .right-account.rewards>div.your-activity>ul.coupon-container .coupon-box");
			var top_margin=$li.css("margin-top");
			if (top_margin == '0px') {
			for(var i = 0; i < $li.length; i+=3) {
				var first_elem,second_elem,third_elem;
				first_elem=parseInt($li.eq(i).find("h2").css("height")) + parseInt($li.eq(i).find("p.coupon_count").css("height")) + parseInt($li.eq(i).find("div.left").css("height"));
				second_elem=parseInt($li.eq(i+1).find("h2").css("height")) + parseInt($li.eq(i+1).find("p.coupon_count").css("height")) + parseInt($li.eq(i+1).find("div.left").css("height"));;
				third_elem=parseInt($li.eq(i+2).find("h2").css("height")) + parseInt($li.eq(i+2).find("p.coupon_count").css("height")) + parseInt($li.eq(i+2).find("div.left").css("height"));;
				var li_max_height=Math.max(first_elem,second_elem,third_elem) + 30;
				$li.eq(i).css("height",li_max_height);
				$li.eq(i+1).css("height",li_max_height);
				$li.eq(i+2).css("height",li_max_height);
			}
			var remaining_li=$li.length % 3;
			if(remaining_li==2){
				first_elem=parseInt($li.eq($li.length - 1).find("h2").css("height")) + parseInt($li.eq($li.length - 1).find("p.coupon_count").css("height")) + parseInt($li.eq($li.length - 1).find("div.left").css("height"));;
				second_elem=parseInt($li.eq($li.length - 2).find("h2").css("height")) + parseInt($li.eq($li.length - 2).find("p.coupon_count").css("height")) + parseInt($li.eq($li.length - 2).find("div.left").css("height"));;
				var li_max_height=Math.max(first_elem,second_elem) + 30;
				$li.eq($li.length - 1).css("height",li_max_height);
				$li.eq($li.length - 2).css("height",li_max_height);
			}
			}
			else{
				for(var i = 0; i < $li.length; i+=2) {
					var first_elem,second_elem;
					first_elem=parseInt($li.eq(i).find("h2").css("height")) + parseInt($li.eq(i).find("p.coupon_count").css("height")) + parseInt($li.eq(i).find("div.left").css("height"));
					second_elem=parseInt($li.eq(i+1).find("h2").css("height")) + parseInt($li.eq(i+1).find("p.coupon_count").css("height")) + parseInt($li.eq(i+1).find("div.left").css("height"));
					var li_max_height=Math.max(first_elem,second_elem) + 30;
					$li.eq(i).css("height",li_max_height);
					$li.eq(i+1).css("height",li_max_height);
				}
				var remaining_li=$li.length % 2;
				if(remaining_li==1){
					$li.eq($li.length - 1).css("height",'auto');
				}
			}
		});*/
		
		if ('ontouchstart' in window) {
			$('body').addClass("touchDevice");
	 		$("header .content nav > ul > li > ul > li > .toggle a").attr("href","#nogo");
			}
		
		if($('.lookbook_wrapper .bottom-pagination').children().length==0){
			$('.lookbook_wrapper .bottom-pagination').css('padding','0');
			}
			if($('body .lookbook_wrapper .lookbook-pagination').children().length==0){
				$('body .lookbook_wrapper .lookbook-pagination').css('padding','0');
			}
			if($('.lookbook_wrapper .listing.wrapper .product-listing.product-grid').children().length==0){
			$('.lookbook_wrapper .listing.wrapper .product-listing.product-grid').parents().find('.listing.wrapper').css('height','0px');
			}

		if($('.promo-block .promo-img').children().length == 0){
			$('.promo-block .pdp-promoDesc').css({'float':'none','margin':'0px auto'});
		}
					
			$(document).on("click",".mini-cart-close",function(){
				$(this).parents('.mini-transient-bag').remove();
			});
			
			/*$(document).on("click","#addToCartButton, .serp-addtobag.js-add-to-cart",function(){
				if($(window).width() > 773) {
					$("#cboxClose").click();
				 $('html,body').animate({
				            scrollTop: 0
				        }, 500);
				 
				} 
				
				});*/
		/*	$('.tata-rewards').popover({
			    html: 'true',
			    placement: 'top',
			    trigger: 'hover',
			    content: $(".reward-popover").html()
			});*/
			
			$('.tata-rewards').popover({
				html: 'true',
			    placement: 'top',
			    trigger: 'manual',
			    content: $(".reward-popover").html()
		    }).on("mouseenter", function () {
		        var _this = this;
		        $(this).popover("show");
		        $(this).siblings(".popover").on("mouseleave", function () {
		            $(_this).popover('hide');
		        });
		    }).on("mouseleave", function () {
		        var _this = this;
		        setTimeout(function () {
		            if (!$(".popover:hover").length) {
		                $(_this).popover("hide")
		            }
		        }, 100);
		    });
		$(document).on("click",'.select-size',function() {
			$(this).toggleClass('active');
		});
		var selectOpen = false;
		$(document).on("mouseleave",'.select-size',function() {
			if($('.select-size').hasClass("active")) {
				selectOpen = true;
			}
		});
		$(document).on("click",function() {
			if(selectOpen) {
				$('.select-size').removeClass('active');
				selectOpen = false;
			}
		});
		/*start change for INC_11789*/
		/*if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var footer_height=$('footer').height() + 20 + 'px';
			$(".body-Content").css('padding-bottom',footer_height);
		}*/
		if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var footer_height=$('footer').height() + 20 + 'px';
			var ua_check = window.navigator.userAgent;
			var msie_check = ua_check.indexOf("MSIE ");
			$(".body-Content").css('padding-bottom',footer_height);
			$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
			if(msie_check > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)){
				setTimeout(function () {
					var footer_height=$('footer').height() + 20 + 'px';
					$(".body-Content").css('padding-bottom',footer_height);
					$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
				},500);
				}
		} 
		/*end change for INC_11789*/
		else{
			$(".body-Content").css('padding-bottom','0px');
		}
		
		$(window).on("resize", function() {
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				var footer_height=$('footer').height() + 20 + 'px';
				$(".body-Content").css('padding-bottom',footer_height);
				$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
			}
			else{
				$(".body-Content").css('padding-bottom','0px');
			}
		});		
		
		if (navigator.userAgent.indexOf('Safari') > 0 && navigator.userAgent.indexOf('Chrome') < 0) {
			$("body").addClass('safariBrowser');
			/*if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				$('header .content nav > ul > li:first-child + li').css('margin-left','25px');
			}*/
		}
		var resize_sbb;
		$(window).resize(function(){
			clearTimeout(resize_sbb);
			resize_sbb = setTimeout(function(){
				if (navigator.userAgent.indexOf('Safari') > 0 && navigator.userAgent.indexOf('Chrome') < 0) {
					if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
						$('header .content nav > ul > li:first-child + li').css('margin-left','25px');
					} else {
						$('header .content nav > ul > li:first-child + li').css('margin-left','0px');
					}
				}
			},100);
		});
		
		$('header .content nav > ul > li').on('touchend',function(){
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none' && $('body').hasClass('touchDevice')){
				$('header .content nav > ul > li').children('ul').css("height","0px");
				if($(this).children('ul').height() > 0) {
					$(this).children('ul').css("height","0px");
				} else {
					$(this).children('ul').css("height","500px"); //changes for TPR-6410
				}
			}
				
		});

		$('header .content nav > ul > li ul').on('touchend',function(e){
			e.stopPropagation();
		});

		
		$(document).on('touchend',function(e){
			e.stopPropagation();
			if(!$(e.target).parents().is('nav') && $('body').hasClass('touchDevice') && $('header div.bottom .marketplace.linear-logo').css('display') == 'none') {
				$('header .content nav > ul > li').children('ul').css("height","0px");
			} 
				
			if(!$(e.target).parents().hasClass('select-list') && $('body').hasClass('touchDevice')) {
				$('.select-view .select-list').removeClass('touch_click');
				$('.select-view .select-list ul').css({'max-height':'0','border-color':'transparent'});
			}
		});

		$(document).on('touchend','.select-view .select-list',function(){
			if($('body').hasClass('touchDevice')){
				$('.select-view .select-list').removeClass('touch_click');
				if($(this).children('ul').height() > 2) {
					$(this).removeClass('touch_click');
					$(this).find('ul').css({'max-height':'0','border-color':'transparent'});
				} else {
					$(this).addClass('touch_click');
					$(this).find('ul').css({'max-height':'500px','border-color':'#dfd1d5'});	
				}
			}

		});

		$(document).on('touchend','.select-view .select-list ul',function(e){
			$('.select-view .select-list').removeClass('touch_click');
			$('.select-view .select-list ul').css({'max-height':'0','border-color':'transparent'});
			e.stopPropagation();
		}); 
		//TISPRO-480
		$('.sign-in-dropdown').mouseleave(function(){$('.sign-in-dropdown input').blur();});
		
		$(document).on('touchend','.select-view .select-list ul li',function(e){
			$(this).click();
		});
		$(window).on("load resize", function() {
			var filter_height = 0;
			if ($(".searchSpellingSuggestionPrompt").is(":visible")) {
				filter_height=$(".searchSpellingSuggestionPrompt").outerHeight() + 96; /* PRDI-69 */
			 /*else {
				filter_height=$(".facet-list.filter-opt").height() + 32;
			}*/
			$(".listing.wrapper .left-block").css("margin-top",filter_height+"px");
			}
		});
		
		$('.checkout.wrapper .product-block.addresses li.item .addressEntry').click(function(){
			$(this).find('input:radio[name=selectedAddressCode]').prop('checked', true);
			});
		
		//Mobile menu
		navhtml = $("nav").html();	
		
		$('header .content .container > .right').prepend(navhtml);
		$('header .content .container > .right ul:first-child > li div').removeClass('toggle');
		$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
		$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul li').removeClass('toggle');
		$("ul li #mobile-menu-toggle+ul li ul.words li.long").unwrap("ul"); //TISPRDT-1296, TISPRDT-1300
		$("ul li #mobile-menu-toggle+ul li ul.words li.long").unwrap(".l2_wrapper"); //TISPRDT-1296, TISPRDT-1300
		setTimeout(function () {
  		  navhtmlMicrosite = $(".brand-header nav ul li").html();
  		 $('header .content .container > .right > ul:first-child').prepend('<li id="shopMicrositeSeller"></li>');
  		  $('header .content .container > .right > ul:first-child > li#shopMicrositeSeller').html(navhtmlMicrosite);
  		$('header .content .container > .right ul:first-child > li#shopMicrositeSeller div').removeClass('toggle');
		$('header .content .container > .right ul li#shopMicrositeSeller #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
		$('header .content .container > .right ul li#shopMicrositeSeller #mobile-menu-toggle + ul li ul li').removeClass('toggle');
	        }, 50);
		
		$(document).off("click","header .content .container > .right > ul:first-child > li#shopMicrositeSeller li.level1 > div > span#mobile-menu-toggle").on("click", "header .content .container > .right > ul:first-child > li#shopMicrositeSeller li.level1 > div > span#mobile-menu-toggle",function(){
			$(this).parents(".level1").siblings().find("span#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
			$(this).parents(".level1").siblings().find(".words").hide();
			$(this).parents(".level1").find(".words").toggle();
			
			
			
		});
		
		//Mobile level 1 active
		$(document).on('click','header > .content .top ul:first-child > li > span.mainli',function() {
			if($(this).prev().hasClass('bgred')){
				$(' header > .content .top ul:first-child li > div').removeClass("bgred");
				}else{
				$(' header > .content .top ul:first-child li > div').removeClass("bgred");
				$(this).prev().addClass("bgred");			
			}		
		});
	/*	$(document).on('click','header > .content .top ul:first-child li#shopMicrositeSeller > span#mobile-menu-toggle',function() {
			setTimeout(function () {
			if($(this).prev().hasClass('bgred')){
				console.log("inside if microsite");
				$(' header > .content .top ul:first-child li#shopMicrositeSeller > div').removeClass("bgred");
			}else{
				$(' header > .content .top ul:first-child li#shopMicrositeSeller > div').removeClass("bgred");	
				console.log("inside else microsite");
				$(this).prev().addClass("bgred");			
			}
			 }, 190);
		});*/

		//loadGigya();
		var sort_top=parseInt($(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top"));
		$(window).on("load resize", function() {
			if($(window).width() <= 773){
				/*$('.listing.wrapper .left-block').css('margin-top','20px');*/ /* PRDI-69 */
				var search_text_height = $(".listing.wrapper .search-result h2").height();
				var search_spelling_height = $(".searchSpellingSuggestionPrompt").height();
				
				if((search_text_height > 14) && (search_spelling_height <= 0)){
					var sort_top1 = sort_top + (search_text_height - 14);
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top1+"px");
				}
				else if((search_text_height <= 14) && (search_spelling_height > 0)){
					var sort_top_2=$(".searchSpellingSuggestionPrompt").height() + sort_top + 20;
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top_2+"px");
				}
				else if((search_text_height > 14) && (search_spelling_height > 0)){
					var sort_top3 = (search_text_height - 14) + $(".searchSpellingSuggestionPrompt").height() + sort_top + 20;
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top3+"px");
				}
				else{
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top+"px");
				}
				if($(".searchSpellingSuggestionPrompt").height()>0){
					var left_block_top_margin= $(".searchSpellingSuggestionPrompt").height() + 96; /* PRDI-69 */
					$('.listing.wrapper .left-block').css('margin-top',left_block_top_margin+'px');
				}
			}
		});
		// commented as part of UI-UX Fixes cart page UF-61
		//setTimeout(function () {
/*		$("body.page-cartPage .cart.wrapper .product-block li.item>ul.desktop>li.delivery").addClass("collapsed");
				$(".mobile-delivery").click(function(){
					$(this).parents("li.delivery").toggleClass("collapsed");
				});
*/		//}, 100);
		$(window).on("load resize", function() {
			$("body.page-cartPage .cart.wrapper .product-block li.item").each(function(){
				if($(this).find("ul.desktop>li.price").css("position")=="absolute"){
					var price_height=$(this).find("ul.desktop>li.price").height() + 20;
					$(this).find(".cart-product-info").css("padding-bottom",price_height+"px");
					var price_top = $(this).find(".cart-product-info").height() + 8;
					$(this).find("ul.desktop>li.price").css("top",price_top+"px");
					var qty_top = price_top + $(this).find("ul.desktop>li.price").height() + 11;
					$(this).find("ul.desktop>li.qty").css("top",qty_top+"px");
				}
				else{
					$(this).find(".cart-product-info").css("padding-bottom","0px");
					$(this).find("ul.desktop>li.price").css("top","auto");
					$(this).find("ul.desktop>li.qty").css("top","auto");
				}
			});
			if($("body.page-cartPage .cart.wrapper .checkout-types li.express-checkout").children().length == 0){
				$("body.page-cartPage .cart.wrapper .checkout-types li#checkout-id").addClass("onlyCheckout");
				$("body.page-cartPage .cart.wrapper .checkout-types").addClass("onlyCheckoutButton");
				$("body.page-cartPage .continue-shopping.desk-view-shopping").addClass("onlyCheckoutLink");
			}
			});
		
		$(document).ready(function() {
			if($("#pageType").val() == "cart")
			{
					
					$("body.page-cartPage .cart.wrapper .product-block li.item").each(function(){
						if($(this).find("ul.desktop>li.price").css("position")=="absolute"){
							var price_height=$(this).find("ul.desktop>li.price").height() + 20;
							$(this).find(".cart-product-info").css("padding-bottom",price_height+"px");
							var price_top = $(this).find(".cart-product-info").height() + 8;
							$(this).find("ul.desktop>li.price").css("top",price_top+"px");
							var qty_top = price_top + $(this).find("ul.desktop>li.price").height() + 11;
							$(this).find("ul.desktop>li.qty").css("top",qty_top+"px");
						}
						else{
							$(this).find(".cart-product-info").css("padding-bottom","0px");
							$(this).find("ul.desktop>li.price").css("top","auto");
							$(this).find("ul.desktop>li.qty").css("top","auto");
						}
					});
					if($("body.page-cartPage .cart.wrapper .checkout-types li.express-checkout").children().length == 0){
						$("body.page-cartPage .cart.wrapper .checkout-types li#checkout-id").addClass("onlyCheckout");
						$("body.page-cartPage .cart.wrapper .checkout-types").addClass("onlyCheckoutButton");
						$("body.page-cartPage .continue-shopping.desk-view-shopping").addClass("onlyCheckoutLink");
					}
					
			}		
		});
		
		$(".product-tile .image .item.quickview").each(function(){
			if($(this).find(".addtocart-component").length == 1){
				$(this).addClass("quick-bag-both");
			}
			});
		if($(".facet-list.filter-opt").children().length > 0){
		var filter_html = $(".listing.wrapper .right-block .facet-values.js-facet-values").html();
		$(".listing.wrapper .left-block").before(filter_html);
		$(".listing.wrapper .right-block .facet-values.js-facet-values").html("").hide();
		}
		else{
			$(".facet-list.filter-opt").hide();
		}
		
		$(document).ajaxComplete(function(){
			if($(".facet-list.filter-opt").children().length > 0){
			var filter_html = $(".listing.wrapper .right-block .facet-values.js-facet-values").html();
			$(".listing.wrapper .left-block").before(filter_html);
			$(".listing.wrapper .right-block .facet-values.js-facet-values").html("").hide();
			}
			else{
				$(".facet-list.filter-opt").hide();
			}
			$(".facet-list.filter-opt li").each(function(){
				if($(this).find("input.colour").length > 0){
					var selected_colour = $(this).find("input.applied-color").val();
					$(".left-block .product-facet .facet.Colour .facet-list li.filter-colour").each(function(){
					var colour_name = $(this).find("input[name=facetValue]").val().split("_", 1);
					if(colour_name == selected_colour){
						$(this).addClass("selected-colour");
					}
					if(selected_colour == "Multicolor"){
						if(colour_name == "Multi"){
							$(this).addClass("selected-multi-colour");
						}
					}
					});
				}
				if($(this).find("input.size").length > 0){
			var selected_size = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Size .facet-list li.filter-size").each(function(){
				var size_val = $(this).find(".js-facet-sizebutton").val();
				if(size_val == selected_size){
					$(this).addClass("selected-size");
				}
			});
				}
			});
			if($(".listing.wrapper").length > 0){
				if($(".searchSpellingSuggestionPrompt").length>0){
					$(".toggle-filterSerp").css("margin-top",$(".searchSpellingSuggestionPrompt").height() + 40 + "px");
					$(".listing.wrapper .right-block").css("padding-top",$(".searchSpellingSuggestionPrompt").height() + 50 + "px");
				}
				if(($(".toggle-filterSerp").length>0) && ($(".facet-list.filter-opt").length>0)){
					var sort_top= $(".toggle-filterSerp").offset().top - $(".listing.wrapper").offset().top;
					if($(".facet-list.filter-opt").offset().top == 0){
						var pagination_top= sort_top - 4;
					}
					else{
						var pagination_top= sort_top - 16;
					}
					
					/*TISPRDT-1179*/	/*TISPRDT-1222*/
					if(!$("body").hasClass("page-productGrid")){
					if($(".listing.wrapper .right-block .listing-menu > div.list_title_sort").css("display") == "block"){
					$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top",sort_top+"px");
					}
					else{
						$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top","auto");
					}
					}
					$("body.page-search .listing.wrapper .right-block .listing-menu").css("display","block");
					/*TISPRDT-1179*/	/*TISPRDT-1222*/
					
					$(".listing.wrapper .right-block .listing-menu > div .pagination.mobile.tablet-pagination").css("top",pagination_top+"px");
				}
			}
			
			
if($(".facet.js-facet.Colour .js-facet-values.js-facet-form .more-lessFacetLinks").length == 0){	
var colorMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
				+'<div class="more checkbox-menu" style="display: block;">'
				+'<a href="#" class="more">+&nbsp;<span class="colourNumber">0</span>&nbsp;more&nbsp;<span class="colourText">colours</span></a>'
				+'</div><div class="less checkbox-menu" style="display: none;">'
				+'<a href="#" class="less"> - less colours</a>'
				+'</div></div>';

$(".facet.js-facet.Colour .js-facet-values.js-facet-form").append(colorMoreLess);
}
if($(".facet.js-facet.Size .js-facet-values.js-facet-form .more-lessFacetLinks").length == 0){	
var sizeMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
				+'<div class="more checkbox-menu" style="display: block;">'
				+'<a href="#" class="more">+&nbsp;<span class="sizeNumber">0</span>&nbsp;more&nbsp;<span class="sizeText">sizes</span></a>'
				+'</div><div class="less checkbox-menu" style="display: none;">'
				+'<a href="#" class="less"> - less sizes</a>'
				+'</div></div>'; 

$(".facet.js-facet.Size .js-facet-values.js-facet-form").append(sizeMoreLess);
}
if($(".facet.js-facet.Colour").length > 0){
	colorSwatch();
}
if($(".facet.js-facet.Size").length > 0){
	sizeSwatch();
}
$(".product-tile .image .item.quickview").each(function(){
	if($(this).find(".addtocart-component").length == 1){
		$(this).addClass("quick-bag-both");
	}
	});		
/*added for TISTE-225*/
if ($(".facet-list.filter-opt").children().length){
	if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
	var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
	$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
	}
	else{
		var sort_height =$(".facet-list.filter-opt").outerHeight() - 12 + "px";
		$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
	}
}
/*added for TISTE-225 end*/

		});
		$(window).on("load resize", function() {
		if($(".listing.wrapper").length > 0){
			if($(".searchSpellingSuggestionPrompt").length>0){
				$(".toggle-filterSerp").css("margin-top",$(".searchSpellingSuggestionPrompt").height() + 40 + "px");
				$(".listing.wrapper .right-block").css("padding-top",$(".searchSpellingSuggestionPrompt").height() + 50 + "px");
			}
			if(($(".toggle-filterSerp").length>0) && ($(".facet-list.filter-opt").length>0)){
				var sort_top= $(".toggle-filterSerp").offset().top - $(".listing.wrapper").offset().top;
				if($(".facet-list.filter-opt").offset().top == 0){
					var pagination_top= sort_top - 4;
				}
				else{
					var pagination_top= sort_top - 16;
				}
				
				/*TISPRDT-1179*/		/*TISPRDT-1222*/
				if(!$("body").hasClass("page-productGrid")){
				if($(".listing.wrapper .right-block .listing-menu > div.list_title_sort").css("display") == "block"){
				$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top",sort_top+"px");
				}
				else{
					$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top","auto");
				}
				}
				/*TISPRDT-1179*/		/*TISPRDT-1222*/
				
				$(".listing.wrapper .right-block .listing-menu > div .pagination.mobile.tablet-pagination").css("top",pagination_top+"px");
			}
		}
		});
		
		/*TPR-250*/
		$(".tabs-block .tabs.pdp.productTabs > li").each(function(){
			if($(this).find("li").length == 0 || ($(this).find("li").length != 0 && ($(this).find("li").eq(0).text().trim() === "")))
			$(this).html("We have not updated the description for this Brand yet. We will get this sorted in a while");
			});
		/*TPR-250*/
		
});

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

        
/*colour and size swatch 3 lines TISPRM-123*/

var colorSwatchFlag,sizeSwatchFlag;

$(document).ready(function() {
	
	var colorMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
						+'<div class="more checkbox-menu" style="display: block;">'
						+'<a href="#" class="more">+&nbsp;<span class="colourNumber">0</span>&nbsp;more&nbsp;<span class="colourText">colours</span></a>'
						+'</div><div class="less checkbox-menu" style="display: none;">'
						+'<a href="#" class="less"> - less colours</a>'
						+'</div></div>';
	
	var sizeMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
						+'<div class="more checkbox-menu" style="display: block;">'
						+'<a href="#" class="more">+&nbsp;<span class="sizeNumber">0</span>&nbsp;more&nbsp;<span class="sizeText">sizes</span></a>'
						+'</div><div class="less checkbox-menu" style="display: none;">'
						+'<a href="#" class="less"> - less sizes</a>'
						+'</div></div>'; 
	
	$(".facet.js-facet.Colour .js-facet-values.js-facet-form").append(colorMoreLess);
	$(".facet.js-facet.Size .js-facet-values.js-facet-form").append(sizeMoreLess);
	colorSwatch();
	sizeSwatch();
	
	
	/*CLP section js starts*/
	
	//Top Categories section
/*	$(".top_categories .top_categories_section:not(:first-child)").wrapAll("<div class='top_categories_wrapper'></div>");
*/	
	/*if ($(".top_categories .top_categories_section").first().children().hasClass('content')) {
			$(".top_categories .top_categories_section:not(:first-child)").wrapAll("<div class='top_categories_wrapper'></div>");

	}
	else {
			$(".top_categories .top_categories_section").wrapAll("<div class='top_categories_wrapper'></div>");
$(".top_categories_wrapper").css("padding-top", "40px");
	} 
	
	
	$(".top_categories").find(".top_categories_wrapper>.top_categories_section:nth-child(3n + 1)").each(function(){
		$(this).nextAll().slice(0, 2).wrapAll("<div class='top_categories_section sub_categories'>");
		});*/
	//TPR-559 Hide/Unhide Component UI fixes starts
	if ($("div.top_categories").find('.content').length == 0) {
		$("div.top_categories").css("padding-top","50px");
	}	
	if ($("div.top_categories").find('.simple-banner-component').length == 0 && $("div.top_categories").find('.content').length == 0) {
		$("div.top_categories").hide();
	}
	//TPR-559 Hide/Unhide Component UI fixes ends 
	//Style Edit section
		/*$(".style_edit > div").slice(0,2).wrapAll("<div class='style_edit_left'>");*/
	if($(".style_edit .style_edit_left").children().length == 0	){
		$(".style_edit .style_edit_left").css("width", "auto");
	}
	//Top Brands section
		//TPR-559 Hide/Unhide Component UI fixes starts
		//$(".top_brands > div").slice(1).wrapAll("<div class='clp_top_brands'>");
		if ($("div.top_brands div").first().children().hasClass('content')) {
			$("div.top_brands div").first().addClass('top_brands_heading');
			$(".top_brands > div").slice(1).wrapAll("<div class='clp_top_brands'>");
		}
		else {
			$(".top_brands > div").wrapAll("<div class='clp_top_brands'>");
		}
			$(".clp_top_brands .top_brands_section").each(function(){
		if($(this).find(".simple-banner-component").length == 0){
			$(this).addClass("view_more_link");
		}
		});
		//TPR-559 Hide/Unhide Component UI fixes ends 
		//Best Seller section
		if (!$(".best_seller .best_seller_section").first().children().hasClass('content')) {
			$(".best_seller").css("padding-top","50px");
		}
/*		$(".best_seller .best_seller_section:first-child").after("<div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div>"); 
*/		
	$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text= $(this).children("h1").text();
		if(li_text != ""){
		$(".best_seller .best_seller_carousel .Menu ul").append("<li>"+li_text+"</li>");
		}
		});
		var best_seller_count = $(".best_seller .Menu ul li").length;
		var active_best_seller_heading = parseInt((best_seller_count/2)) + 1;
		$(".best_seller .Menu ul li:nth-child("+active_best_seller_heading+")").addClass("active");
		//$(".best_seller .best_seller_section:nth-of-type(4)").addClass("show_clplist");
		var active_text_onload = $(".best_seller .Menu ul li.active").text();
		$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text_onload= $(this).children("h1").text();
		if(li_text_onload == active_text_onload){
		$(this).removeClass("hide_clplist").addClass("show_clplist");
		}
		});
		$(".best_seller .Menu .mobile.selectmenu").text($(".best_seller .Menu ul li.active").text());
		$(".best_seller .Menu ul li").off("click").on("click", function(){
			$(".best_seller .Menu ul li").removeClass("active");
			$(this).addClass("active");
			var active_text = $(this).text();
		$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text= $(this).children("h1").text();
		if(li_text == active_text){
		$(".best_seller .best_seller_section").removeClass("show_clplist").addClass("hide_clplist");
		$(this).removeClass("hide_clplist").addClass("show_clplist");
		}
		});
		$(".best_seller .Menu .mobile.selectmenu").text(active_text);
		$('.best_seller .Menu ul').slideUp();
		});
		$(".best_seller .Menu .selectmenu").off("click").on("click", function() {
            $(this).next().slideToggle();
        });
		//Winter Launch section
		/*$(".winter_launch  > .winter_launch_section").slice(-4).wrapAll("<div class='clp_winter_launch_wrapper'>");*/
		if ($(".winter_launch  > .winter_launch_section").first().children().hasClass('content')) {
			$(".winter_launch  > .winter_launch_section").first().addClass('winter_launch_heading');
			$(".winter_launch  > .winter_launch_section.winter_launch_heading").nextAll().wrapAll("<div class='clp_winter_launch_wrapper'>");
		}
		else {
			$(".winter_launch  > .winter_launch_section").wrapAll("<div class='clp_winter_launch_wrapper'>");
		}
		//Top deal blog section
		/* start change for INC_11268*/
		/*$(".top_deal  > a").nextAll().wrapAll("<div class='blog_container'>");*/
		/*if($(".top_deal > .carousel-component").length > 0){
			$(".top_deal  > .carousel-component + a").nextAll().wrapAll("<div class='blog_container'>");
		}
		else{
				$(".top_deal").children().wrapAll("<div class='blog_container'>");
		}*/
		/*end change for INC_11268*/
		/*$(".top_deal  > a").nextAll().wrapAll("<div class='blog_container'>");
		var clp_blog_count = $(".top_deal  > .blog_container").children().length;
		$(".top_deal  > .blog_container").children().slice(0,clp_blog_count/2).wrapAll("<div class='blog_feature'>");
		$(".top_deal  > .blog_container").children().slice(1,clp_blog_count - 1).wrapAll("<div class='blog_feature'>");
		$(".top_deal  > .blog_container > .blog_feature").each(function(){
			$(this).children().last().prevAll().wrapAll("<div class='blog_content'>");
		});*/
		$(".top_deal  > .blog_container > .blog_feature").each(function(){
			if ($(this).children().last().hasClass('simple-banner-component')) {
				$(this).children().last().prevAll().wrapAll("<div class='blog_content'>");
			}
			else {
				$(this).addClass("no-blog-image");
				$(this).children().wrapAll("<div class='blog_content'>");
			}
		});
		//Shop For section
		/*$(".shop_for .shop_for_component:not(:first-child)").slice(0,3).wrapAll("<div class='shop_for_left_wrapper'></div>");
		$(".shop_for > .shop_for_left_wrapper").nextAll().slice(0,4).wrapAll("<div class='shop_for_links'>");*/
		
		//For blank cms container remove bottom border
		
		if(($(".style_edit .style_edit_left").children().length == 0	) && ($(".style_edit .style_edit_right").children().length == 0	)){			
			$(".style_edit").hide();
			$(".top_deal").css("margin-top", "50px");
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".top_brands").children().length==0){
			$(".top_brands").hide();
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".winter_launch").children().length==0){
			$(".winter_launch").hide();
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".top_deal .blog_container").children().length==0){
			$(".top_deal .blog_container").css("border-bottom","none");
		}
		if (!$('.shop_for .content').length && !$('.shop_for a').length && !$('.shop_for .simple-banner-component').length) {
			$(".shop_for").hide();
		} 
		if (!$('.top_deal .content').length && !$('.top_deal a').length && !$('.top_deal .simple-banner-component').length && !$('.top_deal .feature-categories').length) {
			$('.top_deal').hide();
		}
		if (!$('.best_seller .content').length && !$('.best_seller a').length && !$('.best_seller .carousel-component').length) {
			$('.best_seller').hide();
		}
		if (!$('.top_deal .blog_container .blog_feature .simple-banner-component').length && !$('.top_deal .blog_container .blog_feature .content').length && !$('.top_deal .blog_container .blog_feature a').length) {
			$('.top_deal .blog_container').hide();
		}
		
		//For CLP blank cms container remove extra space

		
		
		
		/*CLP section js ends*/
		
		/* UF-68 UF-69 */
		topMarginAdjust();
		/* UF-68 UF-69 */
});
$(window).resize(function() {
	
	/* UF-68 UF-69 */
	topMarginAdjust();
	/* UF-68 UF-69 */

	clearTimeout(colorSwatchFlag);
	clearTimeout(sizeSwatchFlag);
	colorSwatchFlag = setTimeout(function() {
		if(!$(".facet.js-facet.Colour .more-lessFacetLinks .less").is(':visible')) {
			colorSwatch();
		}
	}, 200)
	sizeSwatchFlag = setTimeout(function() {
		if(!$(".facet.js-facet.Size .more-lessFacetLinks .less").is(':visible')) {
			sizeSwatch();
		}
	}, 200)
	
	/* UF-68 UF-69 */
	
	/*if($(window).width() >= 1008){
		//$("body.page-cartPage .cartBottomCheck").removeClass("cartBottomCheckShow");
	var cartBottomCheckTopMargin = $("body.page-cartPage .cart-bottom-block").height() - $("body.page-cartPage .cartBottomCheck button#pinCodeButtonIdsBtm").outerHeight();
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top",cartBottomCheckTopMargin);
	}*/
	
	/* UF-68 UF-69 */
});

$(document).on("click",".facet.js-facet.Colour .more-lessFacetLinks .more",function(e) {
	e.preventDefault();
	$("li.filter-colour.deactivate").removeClass("deactivate");
	$('.facet.js-facet.Colour .more-lessFacetLinks .more').hide();
	$('.facet.js-facet.Colour .more-lessFacetLinks .less').show();
});
$(document).on("click",".facet.js-facet.Colour .more-lessFacetLinks .less",function(e) {
	e.preventDefault();
	colorSwatch();
	$('.facet.js-facet.Colour .more-lessFacetLinks .more').show();
	$('.facet.js-facet.Colour .more-lessFacetLinks .less').hide();
});
$(document).on("click",".facet.js-facet.Size .more-lessFacetLinks .more",function(e) {
	e.preventDefault();
	$("li.filter-size.deactivate").removeClass("deactivate");
	$('.facet.js-facet.Size .more-lessFacetLinks .more').hide();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').show();
});
$(document).on("click",".facet.js-facet.Size .more-lessFacetLinks .less",function(e) {
	e.preventDefault();
	sizeSwatch();
	$('.facet.js-facet.Size .more-lessFacetLinks .more').show();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').hide();
});
$(document).on('click','.facet.js-facet.Colour .js-facet-name h3',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			colorSwatch();
		}
	},80)
});
$(document).on('click','.facet.js-facet.Size .js-facet-name h3',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			sizeSwatch();
		}
	},80)
});

//changes for TISPRO-796
//$(document).on('click','.left-block .toggle-filterSerp',function(){
function toggleFilter(){
	colorSwatch();
	sizeSwatch();
	//Mobile view filter ajax
	//$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
	//$(".toggle-filterSerp").toggleClass("active");
	/*mobile filter*/
	$(".mob-filter-wrapper").fadeIn();
	$(this).toggleClass("active");
	
	//TPR-845
	// Fixing error of facet starts
	if ($(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').length) {
		var spanCountMoreViewColor = $(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-colour").length;
		//TISQAUATS-12 starts
		spanCountMoreViewColor = spanCountMoreViewColor + $(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-multi-colour").length;
		//TISQAUATS-12 ends
		if(spanCountMoreViewColor)
		{
			//$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			//TISQAUATS-12 starts
			if ($(".facet_mobile .filter-colour.selected-colour").length) {
				$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			} else {
				$(".facet_mobile .filter-colour.selected-multi-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			}
			//TISQAUATS-12 ends
		}
	}
	else {
	// Fixing error of facet ends
		var spanCount_colour=$(".facet_mobile .filter-colour.selected-colour").length;
		//TISQAUATS-12 starts
		spanCount_colour = spanCount_colour + $(".facet_mobile .filter-colour.selected-multi-colour").length;
		//TISQAUATS-12 ends
		if(spanCount_colour>0)
		{
				//TISSQAUAT-723 starts
				//$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				if ($(".facet_mobile .filter-colour.selected-colour").length) {
					$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				}
				else {
					$(".facet_mobile .filter-colour.selected-multi-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				}
				//TISSQAUAT-723 ends
		}
	// Fixing error of facet starts	
	}
	// Fixing error of facet ends	

	var spanCount_size=$(".facet_mobile .filter-size.selected-size").length;
	if(spanCount_size>0)
	{
		$(".facet_mobile .filter-size.selected-size").parents(".facet.js-facet").find(".category-icons span").text(spanCount_size);
	}

	$(".facet_mobile .facet.js-facet").each(function(){
		//console.log('hi');
		var spanCountMoreView = $(this).find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("input[type=checkbox]:checked").length;
		if(spanCountMoreView){
			$(this).find(".category-icons span").text(spanCountMoreView);
		}else{
			var spanCount=$(this).find(".facet-list li").find("input[type=checkbox]:checked").length;
			if(spanCount>0)
				{
					$(this).find(".category-icons span").text(spanCount);
				}
		}
		});
	$(".category-icons").each(function(){
		if($(this).find("span").text() == ""){
			$(this).addClass("blank");
		}
		else{
			$(this).removeClass("blank");
		}
	});
	$(".facet-name.js-facet-name h3").removeClass("active-mob");
	$(".facet-name.js-facet-name h3").first().addClass("active-mob");
	$(".facet-name.js-facet-name h3").parent().siblings().hide();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().show();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().find("#searchPageDeptHierTree").show();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().find("#categoryPageDeptHierTree").show();
	
	/*TPR-3658 start*/
	var j = 0;
	$(".listing.wrapper .mob-filter-wrapper > .listing-leftmenu > div.facet_mobile").not(".facet-name").each(function(){
		if($(this).children().length == 0){
			return true;
		}
		if($(this).find(".facet.js-facet.collectionIds").length > 0){
			return true;
		}
		if(j % 2 == 0){
			$(this).addClass("light-bg");
		}
		j++;
		});
	/*TPR-3658 end*/
}
		//	});
	$(".category-icons").each(function(){
	if($(this).find("span").text() == ""){
	$(this).addClass("blank");
	}
	else{
	$(this).removeClass("blank");
	}
	});

$(document).on('click','.left-block .filter-close',function(){
	/*mobile filter*/
	$(".mob-filter-wrapper").fadeOut();
	});
function colorSwatch() {
	var row = 0, start = 0, count_mobile = 0, end_mobile = 0,count_desktop = 0, end_desktop = 0, back = true;
	$(".facet_mobile li.filter-colour, .facet_desktop li.filter-colour").removeClass("deactivate");

	end_mobile = $(".facet_mobile li.filter-colour").length;
	end_desktop = $(".facet_desktop li.filter-colour").length;

	$(".facet_mobile li.filter-colour").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_mobile li.filter-colour").slice(start, end_mobile).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	$(".facet_desktop li.filter-colour").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_desktop li.filter-colour").slice(start, end_desktop).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	
	count_mobile = $(".facet_mobile li.filter-colour.deactivate").length;
	count_desktop = $(".facet_desktop li.filter-colour.deactivate").length;
	$(".facet_mobile .colourNumber").text(count_mobile);
	$(".facet_desktop .colourNumber").text(count_desktop);
	if(count_mobile == 0) {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').hide();
	} else if (count_mobile == 1) {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colour");
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colours");
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	}
	if(count_desktop == 0) {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').hide();
	} else if (count_desktop == 1) {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colour");
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colours");
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	}
	
}


function sizeSwatch() {
	//alert();
	var row = 0, start = 0, count_mobile = 0, end_mobile = 0,count_desktop = 0, end_desktop = 0, back = true;
	$(".facet_mobile li.filter-size, .facet_desktop li.filter-size").removeClass("deactivate");

	end_mobile = $(".facet_mobile li.filter-size").length;
	end_desktop = $(".facet_desktop li.filter-size").length;

	$(".facet_mobile li.filter-size").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 7 && back) {
							start = $(this).next().index();
							$(".facet_mobile li.filter-size").slice(start, end_mobile).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	$(".facet_desktop li.filter-size").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 7 && back) {
							start = $(this).next().index();
							$(".facet_desktop li.filter-size").slice(start, end_desktop).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	count_mobile = $(".facet_mobile li.filter-size.deactivate").length;
	count_desktop = $(".facet_desktop li.filter-size.deactivate").length;
	$(".facet_mobile .facet.js-facet.Size .sizeNumber").text(count_mobile);
	$(".facet_desktop .facet.js-facet.Size .sizeNumber").text(count_desktop);
	if(count_mobile == 0) {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').hide();
	} else if (count_mobile == 1) {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("size");
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("sizes");
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	}
	if(count_desktop == 0) {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').hide();
	} else if (count_desktop == 1) {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("size");
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("sizes");
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	}

}
/*colour and size swatch 3 lines TISPRM-123*/

/*added for gigya   TISPT-203 */
/*
 * Call for Gigya Closed With New 
 * function callGigya(){
	//Start
	
	$.ajax({
	        type: "GET",
	        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
	        success: function() {
	        	 $.ajax({
	 		        type: "GET",
	 		        url: commonResource+'/js/minified/acc.gigya.min.js?v='+buildNumber,
	 		        success: function() {
	 		        	loadGigya();
	 		        },
	 		        dataType: "script",
	 		        cache: true
	 		    });
	        },
	        dataType: "script",
	        cache: true
	    });
	//End 
}

function callGigyaWhenNotMinified(){
	//Start
	$.ajax({
	        type: "GET",
	        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
	        success: function() {
	        	 $.ajax({
	 		        type: "GET",
	 		        url: commonResource+'/js/minified/acc.gigya.js?v='+buildNumber,
	 		        success: function() {
	 		        	loadGigya();
	 		        },
	 		        dataType: "script",
	 		        cache: true
	 		    });
	        },
	        dataType: "script",
	        cache: true
	    });
	//End 
}
*/
/* Changes for TISPT-203 ends  */

/*TPR-198 : Page reload on SortBy and ViewBy*/
//Sort by filter
//TPR- 565 custom sku addded functionality
function sortByFilterResult(top){
	
	if($("input[name=customSku]").length > 0){
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
		var pageNo = 1;
		if ($("#paginationFormBottom .pagination.mobile li.active span").length) {
			pageNo = $("#paginationFormBottom .pagination.mobile li.active span").text();
			if (typeof(pageNo) === "undefined"){
				pageNo = 1;
			}
		}  
		var requiredUrl = '/CustomSkuCollection/' + $("input[name=customSkuCollectionId]").val() + '/page-' + pageNo;
		var dataString = $('#sortForm' + top).serialize() + "&pageSize=" + $("select[name=pageSize]").val();
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(response) {
				//console.log(response);
				// putting AJAX respons to view
				$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
				$('#facetSearchAjaxData .left-block').after(response);
			},
			error : function(xhr, status, error) {				
				console.log("Error >>>>>> " + error);
			},
			complete: function() {
				// AJAX changes for custom price filter
				$("#no-click").remove();
				$(".loaderDiv").remove();
				
			}
		});
		
	}else{
		$("#hidden-option-width").html($(".sort-refine-bar select").find('option:selected').text());
		 var option_width=$("#hidden-option-width").width() + 22;
		$(".sort-refine-bar select.black-arrow-left").css("background-position-x",option_width);
		
		$('#sortForm'+top).submit();
	}
	
}

//View by filter
//TPR- 565 custom sku addded functionality
function viewByFilterResult(top){

	if($("input[name=customSku]").length > 0){
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<div class="loaderDiv"  style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
		var pageNo = 1;
		if ($("#paginationFormBottom .pagination.mobile li.active span").length) {
			pageNo = $("#paginationFormBottom .pagination.mobile li.active span").text();
			if (typeof(pageNo) === "undefined"){
				pageNo = 1;
			}
		}		   
		var requiredUrl = '/CustomSkuCollection/' + $("input[name=customSkuCollectionId]").val() + '/page-' + pageNo;
		var dataString = $('#pageSize_form' + top).serialize();
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(response) {
				//console.log(response);
				// putting AJAX respons to view
				$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
				$('#facetSearchAjaxData .left-block').after(response);
			},
			error : function(xhr, status, error) {				
				console.log("Error >>>>>> " + error);
			},
			complete: function() {
				// AJAX changes for custom price filter
				$("#no-click").remove();
				$(".loaderDiv").remove();
			}
		});
		
	}else{
		$('#pageSize_form'+top).submit();
	}	
}
/*Filter scroll changes start*/
var fix_width= $(".listing.wrapper .left-block").width() + 38;	/*UF-290*/
$(window).on("scroll",function(){
	if($(window).width() > 790 && $('.listing.wrapper .right-block').height() > $('.listing.wrapper .left-block').height() && $('.listing.wrapper .left-block').length > 0) {
		
		if ($('.listing.wrapper .right-block').offset().top >= $('.listing.wrapper .left-block').offset().top - parseInt($('.listing.wrapper .left-block').css("margin-top"))){
			$('.listing.wrapper .left-block').removeClass("fix bottom");
		} else  {
			$('.listing.wrapper .left-block').addClass("fix")
		}
		
		if($(window).scrollTop() >  $('.listing.wrapper .left-block').height() - $(window).height() + $('.listing.wrapper .left-block').offset().top ){
			$('.listing.wrapper .left-block').addClass("fix").removeClass("bottom");
		}
		
		if($(window).scrollTop() >  $('.listing.wrapper .right-block').height() - $(window).height() + $('.listing.wrapper .right-block').offset().top ){
			$('.listing.wrapper .left-block').removeClass("fix").addClass("bottom");
		}
		$(".listing.wrapper .left-block.fix").css("width",fix_width+"px");	/*UF-290*/
	} else {
		$('.listing.wrapper .left-block').removeClass("fix bottom");
	}
});
/*Filter scroll changes end*/

$(document).ready(function() {
	$('.wish-share.mobile .share span').click(function(){
		if($(this).hasClass('active')) {
			$(this).removeClass('active')
		} else {
			$(this).addClass('active')
		}
	});
	$(document).on('click','.zoomLens',function(){hit();})
	$(document).on('click','.product-image-container.device img',function(){
		if($(this).attr("data-type")=='image'){		/*add if for INC144314454*/
			hit({
				windowWidth : $(window).width()
			});
		/*start change for INC144314454*/
		}else{
			var url = $(this).attr("data-videosrc");
			/*$("#player").show();
			$("#player").attr("src",url);*/
			$("#videoModal1 #player").attr("src",url).show();
			$("#videoModal1").modal();
			$("#videoModal1").addClass("active");
			//$(".productImagePrimary .picZoomer-pic-wp img").hide();
			/*$(".zoomContainer").remove();
			$('.picZoomer-pic').removeData('zoom-image');*/
			if ($(window).width() < 1025) {
				$(".product-info .product-image-container.device").show();
			}
		}
		/*end change for INC144314454*/
	});
	var pdpStyle;
		 $(window).on('load resize',function(){	
			clearTimeout(pdpStyle);
			 pdpStyle = setTimeout(function(){
				if(($(window).width() > 767) && ($(window).width() < 1025)) {
					var img_height = $(".product-info .product-image-container.device").height();
					var detail_height = $(".product-info .product-detail").height();
					if (img_height < detail_height ){
					var diff = detail_height - img_height;
					$(".product-info>div.tabs-block").css("margin-top","-"+diff+"px");
					}
					
				}
			},250)
		 });
});

$(document).ajaxComplete(function(){
	if(!$("body").hasClass("pageLabel-homepage") && !$("body").hasClass("template-pages-layout-micrositePage1")){
		$("body").find(".content-block-slider.electronic-brand-slider").removeClass("timeout-slider");
	}
});

/*TPR-179(Shop The Style start)*/
$(document).ready(function(){
	
	$(".timeout-slider").find(".owl-item.active").find(".item.slide").click(function(){
		var link = $(this).find("a").attr("href");
		//console.log(link);
	});
	
	$(".item.slide").find("a").click(function(){
		  var link= $(this).attr("href"); //$(".item.slide").find("a").attr("href"); 
		  var sublink=link.substr(0, link.indexOf('?')); 
		  var id = "#"+sublink.split("#")[1];
		  //console.log(id);
		  $('html, body').animate({
		        scrollTop: $(id).offset().top -100
		    }, 1000);
	});
});



/*END TPR-179(Shop The Style start)*/

/*checkout login error start*/
$(document).ready(function() {
	 $(window).on('load resize',function(){	
		 header_ht = $("header").outerHeight();
$("body:not(.page-checkout-login) .top.checkout-top .content").css("margin-top",header_ht);
if ($("body.page-checkout-login .global-alerts").length > 0){
	$(".top.checkout-top .content").css("margin-top","0px");
	$(".global-alerts").css("margin-top",header_ht+"px");
}
$(document).click(".global-alerts .close", function(){
	$(".global-alerts").css("margin-top","0px");
	$(".top.checkout-top .content").css("margin-top",header_ht+"px");
});
	 });
});
/*checkout login error end */


/*$(document).ready(function(){
	$(".feature-collections h2").each(function(){
		var txth2 = $(this).text();
		$(this).replaceWith("<h3>"+txth2+"</h3>");
	});
	$(".feature-collections h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".menu li h3").each(function(){
		var txth = $(this).text();
		$(this).replaceWith("<h2>"+txth+"</h2>");
	});
	$(".electronics-brand .brands h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".feature-categories h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".trending h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2><span style='color: black !important;'>"+txth1+"</span></h2>");
	});
	
});*/


var ia_prod;
$(window).resize(function(){
	clearTimeout(ia_prod);
	ia_prod = setTimeout(function(){
		var a = $(".pdp .trending#ia_products .image").height()/2 + 20;
		$(".pdp .trending#ia_products .owl-controls").css("top",a)
	},100);
});
$(window).scroll(function(){
	if($(".pdp .trending#ia_products").children().length > 0 && $(window).scrollTop()  > $(".pdp .trending#ia_products").offset().top - $(window).height()) {
		var a = $(".pdp .trending#ia_products .image").height()/2 + 20;
		$(".pdp .trending#ia_products .owl-controls").css("top",a);
	}
});

/*checkout address modified starts*/

/*$(document).on("click",".acc_head",function(){
	$(this).siblings(".acc_content").slideToggle();
});
$(document).on("click",".add-address",function(){
	$(this).siblings(".formaddress").slideToggle();
	$(this).slideToggle();
});
$(document).on("click",".cancelBtn",function(){
	$(this).parents(".formaddress").siblings(".add-address").slideToggle();
	$(this).parents(".formaddress").slideToggle();
});*/
if ($(".address-accordion").length) {
    $(".address-accordion").smk_Accordion({
        closeAble: true,
        closeOther: false,
        slideSpeed: 750,
    })
}
$(".formaddress").hide();
$("#address-form").click(function() {
	/*added by sneha R2.3*/
	$(".editnewAddresPage").empty();
	/*end of sneha R2.3*/
    $(".add-address").hide();
    $(".formaddress").slideToggle();

});
  $(".cancelBtn").click(function() {
	  //alert('here');
	  	
        $(".editnewAddresPage, .formaddress").slideUp();
        $(".add-address").slideDown();
    });
	  $(document).on("click",".cancelBtnEdit",function(){	
		 // console.log("sadsadfsf");
		  var cancel_id = $(this).attr('id');
		  //console.log(cancel_id);
		  var address_id = cancel_id.split('-');
		 // console.log(address_id);
		$('#'+address_id[1]).slideUp();  
	  //$(this).parents().find(".formaddress").slideUp();
  });
$(".checkTab .address-list").last().addClass("last");
if($(".choose-address .acc_content").children(".address-list").length == 0){
	$(".add-address").css({
	  float: "none"
});
	$(".checkTab .formaddress").css({
		margin : "0px auto",
		float: "none",
		width: "80%",
		overflow: "hidden"
	});
//$(".choose-address .acc_head").css("text-align","center");
}
if ($("#couponMessage").children().length == 0){
	$("#couponMessage").css("padding","0px");
}
/*	$(document).on("click",".edit",function(e){	
		 e.stopPropagation();
	alert("hi");
	$(this).prev(".address").css("display","none");
});*/

/*checkout address modified ends*/


/* TPR-1217 starts Click And Collect Starts */
$(document).ready(function(){
	$(".checkout-shipping-items.left-block.left-block-width").parents(".checkout-content.cart.checkout.wrapper").addClass("shipCartWrapper");
	$(".shipCartWrapper").parents(".mainContent-wrapper").find("footer").addClass("shipCartFooter");
});
/* TPR-1217 starts Click And Collect Ends */

$('.checkout.wrapper .formaddress select[name="state"]').on("change",function(){$(this).css("color","#000");});

/* TPR-1601 checkout progress bar start */
$(document).ready(function(){
	if($(".progress-barcheck").hasClass("choosePage")){
		$(".step-1").addClass("active");
		$(".progress-barg span.step").addClass("step1");
		$(this).children().find(".step-2").addClass("in-active");
		$(this).children().find(".step-3").addClass("in-active");
		
	}
	else if ($(".progress-barcheck").hasClass("selectPage")){
		$(".step-2").addClass("active");
		$(".step-1").addClass("step-done");
		$(".progress-barg span.step").addClass("step2");
		$(this).children().find(".step-3").addClass("in-active");
	}
	else if  ($(".progress-barcheck").hasClass("paymentPage")){
		$(".step-3").addClass("active");
		$(".step-1,.step-2").addClass("step-done");
		$(".progress-barg span.step").addClass("step3");
	}
	/*start changes for INC_11120*/
	 /*setTimeout(function () {
		 if ($('body').find(".smartbanner.smartbanner-android").length > 0){
				$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","82px");
			}
			else{
				$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","0px");
			}
     }, 100);*/
	/*end changes for INC_11120*/
	 $(document).on("click",".smartbanner-close",function(){
		$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","0px");
	 });
	 $(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").show(); //TPR-4471
});
/* TPR-1601 checkout progress bar end  */

//----BLP------//
//--top category section---//
/*$(".top_categories_blp").first().find("ul.categories.count-3>li:nth-child(3n + 1)").each(function(){
		$(this).nextAll().slice(0, 2).wrapAll("<li class='sub_li_blp'><ul class='sub_ul_blp'>");
		});*/


//$(".top_categories_blp .top_categories_section_blp:not(:first-child)").wrapAll("<div class='top_categories_wrapper_blp'></div>");
/*$(".top_categories_blp").find(".top_categories_wrapper_blp>.top_categories_section_blp:nth-child(3n + 1)").each(function(){
	$(this).nextAll().slice(0, 2).wrapAll("<div class='top_categories_section_blp sub_categories_blp'>");
	});*/
	//TPR-559 Hide/Unhide Component UI fixes starts 
	if ($("div.top_categories_blp").find('.content').length == 0) {
		$("div.top_categories_blp").css("padding-top","50px");
	}	
	if ($("div.top_categories_blp").find('.simple-banner-component').length == 0 && $("div.top_categories_blp").find('.content').length == 0) {
		$("div.top_categories_blp").hide();
	}	
//TPR-559 Hide/Unhide Component UI fixes ends 
//--top category section end---//
//--top brands section----//
//TPR-559 Hide/Unhide Component UI fixes starts
		//$(".blp_top_brands > div").slice(1).wrapAll("<div class='top_brands_blp'>");
		if ($("div.blp_top_brands div").first().children().hasClass('content')) {
			$("div.blp_top_brands div").first().addClass('top_brands_heading');
			$(".blp_top_brands > div").slice(1).wrapAll("<div class='top_brands_blp'>");
		}
		else {
			$(".blp_top_brands > div").wrapAll("<div class='top_brands_blp'>");
		}
			
		//TPR-559 Hide/Unhide Component UI fixes ends 
//--top brands section end----//
//---style edit section start---//
/*$(".style_edit_blp > div").slice(0,3).wrapAll("<div class='style_edit_left_blp'>");*/
		if($(".style_edit_blp .style_edit_left_blp").children().length == 0	){
			$(".style_edit_blp .style_edit_left_blp").css("width", "auto");
		}
//---style edit section end-----//
//---feature collection section start---//
/*$(".featured_collection  > .featured_collection_section").slice(-4).wrapAll("<div class='blp_featured_collection_wrapper'>");*/
if ($(".featured_collection  > .featured_collection_section").first().children().hasClass('content')) {
	$(".featured_collection  > .featured_collection_section").first().addClass('feature_collection_heading');
	$(".featured_collection  > .featured_collection_section.feature_collection_heading").nextAll().wrapAll("<div class='blp_featured_collection_wrapper'>");
}
else {
	$(".featured_collection  > .featured_collection_section").wrapAll("<div class='blp_featured_collection_wrapper'>");
}
//----feature collection section end-----//
//--shop for section----//
/*$(".shop_for_blp > a").slice(0,4).wrapAll("<div class='shop_for_links_blp'>");*/

/*$(".shop_for_blp .shop_for_component_blp:not(:first-child)").slice(0,3).wrapAll("<div class='shop_for_left_wrapper_blp'></div>");
$(".shop_for_blp > .shop_for_left_wrapper_blp").nextAll().slice(0,4).wrapAll("<div class='shop_for_links_blp'>");
*///--shop for section end----//
//---shop the look section start----//
/*$(".shop_the_look > div").slice(2,4).wrapAll("<div class='shop_the_look_left'>");
$(".shop_the_look").find(".yCmsComponent.shop_the_look_section > a").addClass('shop_view_more');
$(".shop_the_look").find(".yCmsComponent.shop_the_look_section > a").parent().addClass('shop_view_more_wrapper');
*/
$(".shop_the_look .shop_the_look_left .shop_the_look_section").each(function(){
	if($(this).find(".carousel-component").length == 0){
		$(this).addClass("shop_view_more_link");
	}
});
//----shop the look section end-----//
//----blog section start------//
/*start change for INC_11268*/
/*$(".top_deal_blp  > a").nextAll().wrapAll("<div class='blog_container_blp'>");*/
/*if($(".top_deal_blp > .carousel-component").length > 0){
	$(".top_deal_blp  > .carousel-component + a").nextAll().wrapAll("<div class='blog_container_blp'>");
}
else{
		$(".top_deal_blp").children().wrapAll("<div class='blog_container_blp'>");
}*/
/*end change for INC_11268*/
/*$(".top_deal_blp  > a").nextAll().wrapAll("<div class='blog_container_blp'>");
var blp_blog_count = $(".top_deal_blp  > .blog_container_blp").children().length;
$(".top_deal_blp  > .blog_container_blp").children().slice(0,blp_blog_count/2).wrapAll("<div class='blog_feature_blp'>");
$(".top_deal_blp  > .blog_container_blp").children().slice(1,blp_blog_count - 1).wrapAll("<div class='blog_feature_blp'>");*/
$(".top_deal_blp  > .blog_container_blp > .blog_feature_blp").each(function(){
	if ($(this).children().last().hasClass('simple-banner-component')) {
			$(this).children().last().prevAll().wrapAll("<div class='blog_content_blp'>");
		}
		else {
			$(this).addClass("no-blog-image");
			$(this).children().wrapAll("<div class='blog_content_blp'>");
		}
});
//-----blog section end------//
//-----Logo slot start------//
/*$(".common_logo_slot > div").slice(0,2).wrapAll("<div class='common_logo_slot_wrapper'>");*/
if($(".common_logo_slot .common_logo_slot_wrapper").children().length ==0){
	$(".common_logo_slot .common_logo_slot_wrapper").css("background","#fff");
}
//-----Logo slot end------//
if($(".shop_the_look .shop_the_look_section").length==0){
	$(".shop_the_look").css("border-bottom","none");
}
if($(".body-Content").find(".top_deal_blp").children().length==0){
	$(".top_deal_blp").css("border-bottom","none");
}


if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".blp_top_brands").children().length==0){
	$(".blp_top_brands").hide();
}
if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".featured_collection").children().length==0){
	$(".featured_collection").hide();
}
if(($(".style_edit_blp .style_edit_left_blp").children().length == 0	) && ($(".style_edit_blp .style_edit_right_blp").children().length == 0	)){
	$(".style_edit_blp").hide();
	$(".top_deal_blp").css("margin-top", "50px");
}
if (!$('.shop_for_blp .content').length && !$('.shop_for_blp a').length && !$('.shop_for_blp .simple-banner-component').length) {
	$(".shop_for_blp").hide();
} 
/*if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look").children().length==0){	
	$(".shop_the_look").css({
		'padding' : '0px !important',
		'margin' : '0px'		
	});
}*/

if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look .shop_the_look_section").length==0){	
	/*$(".shop_the_look").prop("style","padding:0px !important;margin:0px;border-bottom:none");*/
	$(".shop_the_look").hide();
}


if (!$('.top_deal_blp .content').length && !$('.top_deal_blp a').length && !$('.top_deal_blp .simple-banner-component').length && !$('.top_deal_blp .feature-categories').length) {
	$('.top_deal_blp').hide();
}

if (!$('.top_deal_blp .blog_container_blp .blog_feature_blp .simple-banner-component').length && !$('.top_deal_blp .blog_container_blp .blog_feature_blp .content').length && !$('.top_deal_blp .blog_container_blp .blog_feature_blp a').length) {
	$('.top_deal_blp .blog_container_blp').hide();
}	
if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look").children().length==0){	
	/* start change for INC_11268*/
	/*$(".shop_the_look").prop("style","padding:0px !important;margin:0px");*/
	$(".shop_the_look").prop("style","padding:0px !important;margin:0px;border-bottom:none");
	/*end change for INC_11268*/
}

//-----BLP------//

/*
$(document).on("click",".toggle-filterSerp",function(){
$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
$(this).toggleClass("active");
});*/

$(window).on("load resize",function(){
	htVal=$(".trending .product-image").height();
	$(".trending .owl-controls").css("top",(htVal/2)+"px");
	$(".trending .owl-controls").css("display","block");
	
	/*sprint8*/
	var arr=[".best-offers_blp .view-best-offers",".best-offers .view-best-offers","#bestOffers.best-offers.feature-collections a.view-best-offers"];
	for(var i=0;i<arr.length;i++){
	if($(arr[i]).text().trim() != ""){
	$(arr[i]).addClass("bestBtnTxtPresence");
	}
	}
	/*sprint8*/
	
});

/*TPR-4471 starts*/

//$(window).on("load resize",function(e){
	//if($(window).width() > 773) {
$(document).off("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div.toggle").on("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div.toggle",function() {
	if($(window).width() <= 773){
	$(this).parent().toggleClass('clicked-menu');
	$(".productGrid-menu").toggleClass('prodGridMargin');
	if(!($(".productGrid-menu nav > ul > li.clicked-menu > ul").hasClass('clicked-ul'))){
		$(".productGrid-menu nav > ul > li.clicked-menu > ul").addClass("clicked-ul");
	}
	if(!($(".productGrid-menu nav > ul > li.clicked-menu > ul > li:first-child").hasClass('clicked-first-level'))){
		$(".productGrid-menu nav > ul > li.clicked-menu > ul > li:first-child").addClass("clicked-first-level");
	}
	}
	});
$(document).off("mouseover touchend",".productGrid-menu nav > ul > li.clicked-menu > ul > li").on("mouseover touchend",".productGrid-menu nav > ul > li.clicked-menu > ul > li",function() {
		$(".productGrid-menu nav > ul > li.clicked-menu > ul").removeClass('clicked-ul');
		$(".productGrid-menu nav > ul > li.clicked-menu > ul > li").removeClass('clicked-first-level');
		});

	//}
//});

/*UF-29*/
$(".gig-rating-readReviewsLink_pdp").on("click",function() {
	  $("body,html").animate({ scrollTop: $('#ReviewSecion').offset().top - 50 }, "slow");
});

/* UF-73*/
$('.cartItemBlankPincode a').click(function() {

	$('html,body').animate({ scrollTop: $(this.hash).offset().top - 85}, 300);
	$('#changePinDiv').addClass('blankPincode');
	$(this.hash).focus();
	return false;

	e.preventDefault();

	}); 

$(document).mouseup(function (e)
		{
		    var container = $(".cartItemBlankPincode a");

		    if (!container.is(e.target)
		        && container.has(e.target).length === 0)
		    {
		    	$('#changePinDiv').removeClass('blankPincode');
		    }
		});

/*UF-85*/
$("body.page-cartPage .cart.wrapper .checkout-types li#checkout-id").on("mouseover",function(){
	if($(this).find("a#checkout-enabled.checkout-disabled").length > 0){
		$(this).css("cursor","not-allowed");
	}
	else{
		$(this).css("cursor","default");
	}
});

/*TPR-4471 starts*/
$(window).on("load resize",function(e){
	var style=null;
	if($(window).width() < 773) {
		
		$(document).off("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div").on("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div",function() {
			if($(window).width() <= 773) {
			$(".productGrid-menu nav .mainli.menu-dropdown-arrow").parent("li.level1").css("background-color","rgb(169, 20, 60)");
			$(this).parent().toggleClass('clicked-menu-mobile');
			if($(this).parent().hasClass('clicked-menu-mobile'))
				mobileVal=true;
			else
				mobileVal=false;
			$(".productGrid-menu nav #mobile-menu-toggle + ul#topul").toggle();
			if($(".productGrid-menu nav ul li#shopMicrositeSeller").hasClass("clicked-menu-mobile"))
				$(".productGrid-menu nav").append("<div class='overlay overlay-sideNav'></div>");
			else
				$(".productGrid-menu nav .overlay.overlay-sideNav").remove();
			}
			});
		
		$(document).off("click",".productGrid-menu nav span#mobile-menu-toggle");
		
	}
	else {
		//$(".productGrid-menu nav #mobile-menu-toggle").next().attr("style",style);
		//$("li.short.words,li.long.words").next().attr("style",style); 
	}
	//$("li.level1 .words").css("display","none");
	
	
	/*$("li.level1 ul.words").hide();
	$(document).on("click","li.level1 .toggle",function(){
		$("li.level1 ul.words").hide();
		$(this).parent("li.level1").children("ul.words").toggleClass("L1Show");
	});*/
	
});

	/*$(document).off("click","li.level1 #mobile-menu-toggle").on("click","li.level1 #mobile-menu-toggle",function(){
		$(".clicked-menu-mobile#shopMicrositeSeller #topul li.level1 ul.words").prop("style","border:3px solid red");
		if($(this).hasClass("menu-dropdown-arrow") && $(this).parent().hasClass("level1")){
			alert("level1");
			$(this).parent(".level1").children("ul.words").css("display","block");
		}
		else if ($(this).hasClass("menu-dropdown-arrow") && $(this).parent().hasClass("short")){
			alert("short");
			$(this).parent().nextAll(".long").css("display","block");
			alert("short after");
		}
		else
			$(this).parent(".level1").children("ul.words").removeClass("L1WordsDisplay");
	});*/
	
/*$(document).ready(function(){
	if($(window).width() < 791){
		$("#shopMicrositeSeller").addClass("clicked-menu active clicked-menu-mobile");
	}
});*/
	/*$(document).off("click","span.mainli").on("click","span.mainli",function(){
			alert();
			//$("#shopMicrositeSeller").addClass("clicked-menu active clicked-menu-mobile");
			//$("#shopMicrositeSeller li.level1 ul.words").css("display","none");
		//$("li.level1 ul.words").css("border","3px solid red");
});*/
	
	/*$(window).on("load resize",function(){
		if($(window).width < 791)
		$("#shopMicrositeSeller").css("line-height",($('.productGrid-header').height()+"px"));
	});*/

/*$(document).on("click","#shopMicrositeSeller > span#mobile-menu-toggle",function(){
	var overlayContainer='<div class="overlay overlay-sideNav" style="display: block;"></div>';
	if($(window).width() < 791 && !$(".productGrid-header-wrapper .productGrid-header .productGrid-menu > nav > .overlay").hasClass("overlay-sideNav"))
		$(".productGrid-header-wrapper .productGrid-header .productGrid-menu > nav").append(overlayContainer);
});*/
/*$(window).on("load",function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("display","block");
	$(".productGrid-menu #shopMicrositeSeller > div").html(" ");
	var span_container="<span></span><span></span><span></span>";
	$(".productGrid-menu nav > ul > li > div.toggle").append(span_container);
});*/
$(document).ajaxComplete(function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("display","block");
	$(".productGrid-menu #shopMicrositeSeller > div").html(" ");
	var span_container="<span></span><span></span><span></span>";
	$(".productGrid-menu nav > ul > li > div.toggle").append(span_container);
});
$(document).on("click","#shopMicrositeSeller > div",function(){
			var width= ($(".productGrid-header").width() - 50 ) + "px";
		$("#topul").css("width", width);
	});

$(document).on("click",".clicked-menu.active.clicked-menu-mobile > div.toggle",function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("margin-left","initial");
	//$(".productGrid-header-wrapper").css("height","75px");
});
$(document).on("click",".clicked-menu.active.clicked-menu-mobile .level1 > span#mobile-menu-toggle",function(){
	$(".productGrid-menu nav #mobile-menu-toggle + ul li.level1").css("background-color","#000");
	if(!$(this).hasClass("menu-dropdown-arrow"))
	$(this).parent(".level1").css("background-color","#A9143C");
	styleMobContainer = $(this).parent(".level1").attr("style");
});
/*$(".clicked-menu.active.clicked-menu-mobile .level1 > span#mobile-menu-toggle").click(function(){
	if(!$(this).hasClass("menu-dropdown-arrow"))
	$(this).parent(".level1").children("div.toggle").css("background-color","#A9143C");
});*/
/*$(document).on("click",".productGrid-header-wrapper .productGrid-header > div button",function(){
	alert();
	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 0},{duration:500});
});*/
$(".productGrid-header-wrapper .productGrid-header > div button#micrositesearchButton").click(function(){
	$(".productGrid-header-wrapper").addClass("active_adjust");
	
	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 2},{duration:200});
});
$(document).click(function (e)
		{
		    var container = $(".productGrid-header-wrapper .productGrid-header > div #search_form_microsite");
		    //var containerGlobal = $("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']");
		    //var containerGlobalIcon = $(".searchButtonGlobal");
		    /*if ((!container.is(e.target) // if the target of the click isn't the container...
		        && container.has(e.target).length === 0) && container.find("input[type='text']").val().length === 0) // ... nor a descendant of the container*/		    
		    //
		    /* Change for TISSQAUAT-687 :: IE throws error*/
		    if ((!container.is(e.target) // if the target of the click isn't the container...
			        && container.has(e.target).length === 0) && container.find("input[type='text']").length === 0)
		    {
		    	$(".productGrid-header-wrapper").removeClass("active_adjust");
		    	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 1000},{duration:200});
		    }
		   /* if ((((!containerGlobal.is(e.target) // if the target of the click isn't the container...
			        && containerGlobal.has(e.target).length === 0) && containerGlobal.find("input#js-site-search-input").val().length === 0) && 
			        (!containerGlobalIcon.is(e.target) // if the target of the click isn't the container...
			        && containerGlobalIcon.has(e.target).length === 0)) && $(window).width() < 791) // ... nor a descendant of the container
			    {
		    	containerGlobal.css("display","none");
			    }*/
		});
//$(document).ready(function(){
//	if(typeof($(".productGrid-header-wrapper .productGrid-header > div input[type='text']").val() != undefined)){
//		if($(".productGrid-header-wrapper .productGrid-header > div input[type='text']").val().length != 0){
//
//		$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").css("margin-left","0");
//		alert();
//		if(($("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() != "") && $(window).width() < 791)
//			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']").addClass("searchButtonGlobalTextBar");
//		else
//			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']").removeClass("searchButtonGlobalTextBar");
//		}
//	}
//		
//});

$(document).on("click",".searchButtonGlobal",function(){
	if(($("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() == "" && !$("form#search_form[name='search_form']").hasClass("searchButtonGlobalTextBar")) || 
			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() != "")
		$("form#search_form[name='search_form']").addClass("searchButtonGlobalTextBar");
	else
		$("form#search_form[name='search_form']").removeClass("searchButtonGlobalTextBar");
});
$(window).on("load resize",function(){
	//$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu clicked-menu-mobile");
	$(".productGrid-header-wrapper .productGrid-header>div.productGrid-menu").removeClass("prodGridMargin");
	//$(".productGrid-header-wrapper .overlay.overlay-sideNav").remove();
	if($(window).width() > 773){
		$(".productGrid-menu nav > ul > li > ul > li > ul.words").css("display","");
		$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu-mobile");
		$(".productGrid-menu  nav > ul > li > ul > li.level1").attr("style",null);
		if(deskVal === false){
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").addClass("heightZero").removeClass("heightEase");
			$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
		}
		else{
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").removeClass("heightZero").addClass("heightEase");	
			$(".productGrid-menu #shopMicrositeSeller").addClass("clicked-menu active");
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").css('display','');
		}
	}
	if($(window).width() <= 773 && mobileVal === true){
		$(".productGrid-menu #shopMicrositeSeller").addClass("clicked-menu clicked-menu-mobile active");
		$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent("li.level1").attr("style","background-color:rgb(169, 20, 60)");
		$(".productGrid-menu nav #mobile-menu-toggle + ul#topul").show();
		$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow + ul.words").css("display","block");
	}
	if($(window).width() <= 773 && mobileVal === false){
		$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
	}
	$(document).on("mouseenter",".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller",function(){
		$(".productGrid-menu nav>ul>li.clicked-menu>ul").removeClass("heightZero");
		if($(window).width() > 773){
			$(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").addClass("clicked-menu active");
			deskVal=true;
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
			$(".productGrid-menu nav > ul > li > ul > li:first-child").addClass("hovered");
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").css("display","");
		}
	});
	$(document).on("mouseleave",".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller",function(){
		if($(window).width() > 773){
			$(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
			deskVal=false;
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
		}
	});
	$(document).on("mouseenter",".productGrid-menu nav > ul > li > ul > li",function(){
		if($(window).width() > 773){
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
			$(this).addClass("hovered");
		}
	});
	var width= ($(".productGrid-header").width() - 50 ) + "px";
	$("#topul").css("width", width);
});

$(document).on("click",".overlay.overlay-sideNav",function(){
	$(".productGrid-menu nav > ul > li#shopMicrositeSeller > div").trigger("click");
});

$(document).ready(function(){
	$("head").append("<style id='headStyleId'></style>");
});
$(window).on("load resize",function(){
	var rightAdjust = "";
	if($(".productGrid-header-wrapper.active-header").hasClass("active_adjust"))
	rightAdjust = $(".productGrid-header-wrapper.active-header.active_adjust .productGrid-header > div input[type='text']").offset().left+2;
	if($(window).width() <= 773){
		$('head #headStyleId').html(('.productGrid-header-wrapper.active-header .productGrid-header > div.product-grid-search #micrositesearchButton:before{right:'+rightAdjust+'}'));
	}
		else
		$('head #headStyleId').html("");
	
	/*TPR-5061*/
	if($(window).width() <= 1007){
		var lenLi = $(".tabs-block .nav.pdp.productNav>li").length;
		widthPerLi = 100/lenLi;
		$(".tabs-block .nav.pdp.productNav>li").css("width",widthPerLi+"%");
	}
	else
		$(".tabs-block .nav.pdp.productNav>li").css("width","");
	/*TPR-5061*/
	
	/*$(".showcaseItem > a").removeClass("showcase-border");		TISSTRT-1525	
	$(".showcaseItem").eq(1).find("a").addClass("showcase-border");		TISSTRT-1525*/
	
});


//$(document).ready(function(){
//	var countBag = $(".productGrid-header-wrapper .productGrid-header div.bag .mini-cart-link.myBag-sticky > span:nth-of-type(2)").text();
//	$(".productGrid-header-wrapper .productGrid-header div.bag .mini-cart-link.myBag-sticky > span:nth-of-type(2)").text("("+countBag+")");
//});

/*TPR-4471 ends*/
$(document).ready(function(){
if ($(".facet-list.filter-opt").children().length){
	$("body.page-productGrid .product-listing.product-grid.lazy-grid, body.page-productGrid .product-listing.product-grid.lazy-grid-facet, body.page-productGrid .product-listing.product-grid.lazy-grid-normal").css("padding-top","15px");  //INC144315068
	$("body.page-productGrid .facet-list.filter-opt").css("padding-top","65px");
	var filter_height = $(".facet-list.filter-opt").height() - 8;   /* PRDI-69 */
	$("body.page-productGrid .listing.wrapper .left-block").css("margin-top",filter_height + "px");
	/* UF-253 start */
	if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
	var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
	$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
	}
	else{
		var sort_height =$(".facet-list.filter-opt").outerHeight() - 12 + "px";
		$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
	}
}
});
$(window).on("load resize", function() {
	if ($(".facet-list.filter-opt").children().length){
		$("body.page-productGrid .product-listing.product-grid.lazy-grid, body.page-productGrid .product-listing.product-grid.lazy-grid-facet, body.page-productGrid .product-listing.product-grid.lazy-grid-normal").css("padding-top","15px");  //INC144315068
		$("body.page-productGrid .facet-list.filter-opt").css("padding-top","65px");
		if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
			$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
			}
			else{
				var sort_height =$(".facet-list.filter-opt").height() + 12 + "px";
				$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
			}
	}	
	/* UF-257 start */
	/*if($('.smartbanner-show .smartbanner').css('display') == 'none'){
		$(".smartbanner-show").css("margin-top","0px");
	}*/
	/* UF-257 end */
});
/* UF-253 end */


$(document).ready(function(){ 
    $(window).scroll(function(){ 
        if ($(this).scrollTop() > 100) { 
            $('#scroll_to_top').fadeIn(); 
        } else { 
            $('#scroll_to_top').fadeOut(); 
        } 
    }); 
    //$('#scroll_to_top').click(function(e){ 
    $(document).on("click","#scroll_to_top",function(){
        $("html, body").animate({ scrollTop: 0 }, 600); 
        return false; 
    }); 
    
    /*UF-68 UF-69*/
    $(".page-cartPage .cart-total-block ul.checkOutBtnBtm > li.checkout-button").css("visibility","visible");
    //$("#pinCodeButtonIdsBtm").addClass("CheckAvailability");
    /*UF-68 UF-69*/
    
    /*TISSQAUATS-881*/
   /* if($(".product-info .product-detail .other-sellers#otherSellerInfoId .other-sellers-link #otherSellersId").text().length != 0)
    	$(".product-info .product-detail .other-sellers#otherSellerInfoId").css("display","block");*/
    /*TISSQAUATS-881*/
});

/* UF-68 UF-69 */
/*function topMarginAdjust(){
var arr_error = ["#unserviceablepincode_tooltip_btm","#error-Id_tooltip_btm","#emptyId_tooltip_btm"];
for(var i=0;i<arr_error.length;i++){
	if($(arr_error[i]).css("display") != "none")
		heightTopAdjust = $(arr_error[i]).outerHeight() + parseInt($(arr_error[i]).css("top"));
}
if($(window).width()>1007){
	var cartBottomCheckTopMargin = $("body.page-cartPage .cart-bottom-block .cart-total-block").height() - $("body.page-cartPage .cartBottomCheck button[name='pinCodeButtonId']").outerHeight() - heightTopAdjust;
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top",cartBottomCheckTopMargin);
}
else{
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top","");
	var changePinDivHeight = $("body.page-cartPage .cartBottomCheck.cartBottomCheckShow #changePinDiv").outerHeight();
	var cartBottomCheckTopMarginTab = $("body.page-cartPage .cart-bottom-block .cart-total-block .totals").outerHeight() + $("body.page-cartPage .cart-bottom-block .cart-total-block .checkout-types.onlyCheckoutButton.checkOutBtnBtm").outerHeight() + parseInt($("body.page-cartPage .cart-bottom-block .cart-total-block .checkout-types.onlyCheckoutButton.checkOutBtnBtm").css("margin-top"));
	$("body.page-cartPage .cartBottomCheck.cartBottomCheckShow").css("top",cartBottomCheckTopMarginTab);
}
}*/
function topMarginAdjust(){
	var heightTotals = $("body.page-cartPage .cart-bottom-block .cart-total-block .totals").outerHeight();
	if($(window).width() > 1007)
		$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm").css("margin-top",heightTotals+30);
	else
		$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm").css("margin-top","");
}

$(document).on("click","button[name='pinCodeButtonId']",function(){
	$("input[name='defaultPinCodeIds']").css("color","#000");
}); 

$(document).ajaxComplete(function(){

	//Modified for  UF-68 UF-69 UF-252
	//$("body.page-cartPage .cartBottomCheck button#pinCodeButtonIdsBtm").addClass("CheckAvailability");
	$("body.page-cartPage a[class='checkoutButton checkout button red']").attr("onclick","return checkServiceabilityRequired('typeCheckout',this);");
	$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm li.checkout-button a#checkout-down-enabled.checkout-disabled").css("pointer-events","");
	$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm li.checkout-button a#checkout-down-enabled.checkout-disabled").attr("onclick","");
	$("a#checkout-enabled.checkout-disabled").removeAttr("onclick");
	
	/*TISSQAUATS-881*/
	 /*if($(".product-info .product-detail .other-sellers#otherSellerInfoId .other-sellers-link #otherSellersId").text().length != 0)
	    	$(".product-info .product-detail .other-sellers#otherSellerInfoId").css("display","block");*/
	 /*TISSQAUATS-881*/
});

/* UF-68 UF-69 */


if($("#sameAsShippingEmi").is(":checked")){
	$("#sameAsShippingEmi").prev('h2').hide();
}
else{
	$("#sameAsShippingEmi").prev('h2').show();
}
$("#sameAsShippingEmi").click(function(){
	if($("#sameAsShippingEmi").is(":checked")){
		//$("#billingAddress fieldset .error-message").html("");
		$(this).prev('h2').hide();
	}
	else{
		$(this).prev('h2').show();
	}
	});

$("header .content nav > ul > li > ul > li").on("mouseover",function(){
	$(this).parent().parent().find(".toggle").addClass("show_arrow");
});
$("header .content nav > ul > li > ul > li").on("mouseout",function(){
	$(this).parent().parent().find(".toggle").removeClass("show_arrow");
});



/*TISSQAEE-335*/
$(window).on("load resize",function(){
	topLeftLocator();
	
	/* UF-338 starts */
	$(".product-info>div.tabs-block .tabs .tab-details > ul li:contains(Brand Color)").each(function(){
		var original = $(this).text();
		var result = original.substr(original.indexOf(" ") + 1);
		$(this).text(result);

		var original = $(this).text();
		var result = original.substr(original.indexOf(" ") + 1);
		$(this).text(result);		//Code Repeat
	});
	
	/* UF-338 ends */
	
	/* UF-338 starts */
	$(".product-info>div.tabs-block .tabs .tab-details > ul li:contains(Brand Color)").each(function(){
		var original = $(this).text();
		var result = original.substr(original.indexOf(" ") + 1);
		$(this).text(result);

		var original = $(this).text();
		var result = original.substr(original.indexOf(" ") + 1);
		$(this).text(result);		//Code Repeat
	});
	
	/* UF-338 ends */
	
	/* TPR-6013 responsive class addition starts*/
	$("body .account .right-account .info,body .account .right-account .password,body .account .right-account .signOut,body .account .right-account .order-history").removeClass("responsiveProfile");
	if($(window).width() <= 1007)
		$("body .account .right-account .info,body .account .right-account .password").addClass("responsiveProfile");
	if($(window).width() <= 773)
		$("body .account .right-account .signOut,body .account .right-account .order-history").addClass("responsiveProfile");
	/* TPR-6013 responsive class addition starts*/
});
$(document).ajaxComplete(function(){
	topLeftLocator();
});
window.onload = function (){
	$(".store-finder-legends").css("display","block");
}

function topLeftLocator(){
var topLegend = $(".store-finder-search").outerHeight() + parseInt($(".store-finder-search").css("margin-bottom")) + $(".gmnoprint.gm-bundled-control .gmnoprint").height() + parseInt($(".gmnoprint.gm-bundled-control").css("margin-top"))  + 20;	//TISSTRT-1611 fix

$(".store-finder-legends").css("top",topLegend);
var leftLegend = $(".store-finder-map.js-store-finder-map").outerWidth() + parseInt($(".store-finder-map.js-store-finder-map").parent(".js-store-finder").css("margin-left")) - $(".store-finder-legends").width() - parseInt($(".gmnoprint.gm-bundled-control").css("margin-right")) - 15;
$(".store-finder-legends").css("left",leftLegend);
/*$(".store-finder-legends").css("display","block");*/
if($(window).width() < 751){
	$(".store-finder-legends").css("top","");
	$(".store-finder-legends").parents(".body-Content").css("padding-bottom","");
}
if($(window).width() < 313)
	$(".store-finder-legends").css("left","");
}
/*TISSQAEE-335*/
/*TPR-1283-code added for change heading height for brand filtered PLP--Starts*/
$(window).on("load resize",function(){
	var htH1 = $("body.page-productGrid .list_title h1").height();
	var lineHtH1 = parseInt($("body.page-productGrid .list_title h1").css("line-height"));
	var htH1Multiple = htH1/lineHtH1;
	var paddingOrigin = parseInt($("body.page-productGrid .facet-list.filter-opt").css("padding-top"));
	paddingOrigin = paddingOrigin + (lineHtH1 * (htH1Multiple-1));
	$("body.page-productGrid .facet-list.filter-opt").css("padding-top",paddingOrigin);
});
/*TPR-1283-code added for change heading height for brand filtered PLP--Ends*/
$(document).on("mouseover","header .content nav > ul > li > ul > li",function(){
	$(this).parent().parent().find(".toggle").addClass("show_arrow");
});
$(document).on("mouseout","header .content nav > ul > li > ul > li",function(){
	$(this).parent().parent().find(".toggle").removeClass("show_arrow");
});
/*added for UF-353*/
$(document).ready(function(){
	$("#footerByAjaxId ul li a[title='Store Locator']").parent().remove();
});
/*UF-353 end*/
/*Issue in payment page by selecting payment mode in kidswear ST testing*/
$(document).on("click",".cart.wrapper.checkout-payment .left-block .payments.tab-view .nav li",function(){
	$(".cart.wrapper.checkout-payment .left-block .payments.tab-view .nav li").removeClass("active");
	$(this).addClass("active");
	});
/*Issue in payment page by selecting payment mode in kidswear ST testing*/

/* TPR-6013 starts*/
$(document).on("click","body .account .right-account .password .blue.changePass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","block");
	$(this).css("display","none");
	$("body .account .right-account .password .blue.changePassResponsive").css("display","none");
	$("body .account .right-account .password .blue.crossPass").css("display","block");
});
$(document).on("click","body .account .right-account .password .blue.changePassResponsive",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","block");
	$(this).css("display","none");
	$("body .account .right-account .password .blue.changePass").css("display","none");
	$("body .account .right-account .password .blue.crossPass").css("display","block");
});
$(document).on("click","body .account .right-account .password .blue.crossPass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","");
	$(this).css("display","");
	if ($(window).width() > 790) {
		$("body .account .right-account .password .blue.changePass").css("display","block");
		$("body .account .right-account .password .blue.changePassResponsive").css("display","none");
	} else {
		$("body .account .right-account .password .blue.changePass").css("display","none");
		$("body .account .right-account .password .blue.changePassResponsive").css("display","block");
	}   
	
});
/*$(document).on("click","body .account .right-account .password .blue.crossPass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","");
	$(this).css("display","");
	$("body .account .right-account .password .blue.changePass").css("display","none");
	$("body .account .right-account .password .blue.changePassResponsive").css("display","block");
});*/

$(".deliveryTrack.status.suman").each(function(){
	$(this).find(".progtrckr.tabs").find("li").each(function(){
	if($(this).find(".commonBlock").length === 2){
	var index = $(this).index();
	$(this).parents(".progtrckr.tabs").siblings(".nav").find("li").eq(index).addClass("greenProgress");








	}
	});
	});
/* TPR-6013 ends*/
/* UF-377 starts */
$(document).ready(function(){
	if ($(".product-specification-accordion").length) {
	    $(".product-specification-accordion").smk_Accordion({
	        closeAble: true,
	        closeOther: true,
	        slideSpeed: 750,
	    });
	}
});
/* UF-377 ends */
/*PRDI-402 start*/
$(document).ready(function(){
	if($(window).width() < 790){
		$(".pagination_ul .product-block").each(function(){
			if($(this).children().length == 1){
				$(this).find('li.viewDetails').css('margin-bottom','30px');
			}
		});
		$(".homepage-banner #rotatingImageTimeout").remove();	/*add for SDI-3883*/
	}	
	else{
		$(".homepage-banner #rotatingImageTimeoutMobile").remove();		/*add for SDI-3883*/
	}
});
/*PRDI-402 end*/
/* start change of PRDI-92 */
$(document).ready(function() {
	if ($(".facet_desktop .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
		$(".facet_desktop .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
	}
	/*start change of INC144319119*/
	/*if($('.facet_desktop .facet.js-facet.Dial').hasClass("Colour")){
		$('.facet_desktop .facet.js-facet.Dial.Colour .more-lessFacetLinks').remove();
	}*/
	if ($(".facet_mobile .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
        $(".facet_mobile .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
    }
	/*end change of INC144319119*/
});

$(window).on("load",function(){
	setTimeout(function(){
		if ($(".facet_desktop .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
			$(".facet_desktop .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
		}
		/*start change of INC144319119*/
		/*if($('.facet_desktop .facet.js-facet.Dial').hasClass("Colour")){
			$('.facet_desktop .facet.js-facet.Dial.Colour .more-lessFacetLinks').remove();
		}*/
		if ($(".facet_mobile .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
			$(".facet_mobile .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
        }
		/*end change of INC144319119*/
	},500);
});

$(window).on("resize click",function(){
	if ($(".facet_desktop .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
		$(".facet_desktop .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
	}
	/*start change of INC144319119*/
	if ($(".facet_mobile .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
		$(".facet_mobile .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
    }
	/*end change of INC144319119*/
});
/* end change of PRDI-92 */

/*start change of INC144319119*/
$(window).on("scroll",function(){
	if ($(".facet_desktop .facet.js-facet.Dial li.filter-colour").hasClass("deactivate")){
		$(".facet_desktop .facet.js-facet.Dial li.filter-colour").removeClass("deactivate");
	}
});
/*end change of INC144319119*/

$(".accordin").smk_Accordion({
    closeAble: true,
    closeOther: false,
    slideSpeed: 750,
});
/*$(".accordin").on("click", function(){
	$(this).parent().find(".accordin.FineJewellery .item div.title").slideToggle();
	$(this).parent().toggleClass("accordion-open");
	$(this).parent().siblings().find(".accordin.FineJewellery .item div.title").slideUp();
	$(this).parent().siblings().removeClass("accordion-open");
});*/

/*price breakup scroll start*/
$("span.price-breakup").click(function() {
	  $("body,html").animate({ scrollTop: $("#showPrice").offset().top - 150 }, "slow");
	  $("#showPrice").addClass("acc_active");
	  $("#showPrice").find(".detail.acc_content").slideDown();
});
/*price breakup scroll end*/
/*Feature for FineJewellery scroll start*/
$("span.more-link").click(function() {
	  if ($(window).width() > 767) {
		  $("body,html").animate({ scrollTop: $("#tabs_styleNotes").offset().top - 150 }, "slow");
		  $("#tabs_styleNotes").addClass("active");
		  $("#tabs_styleNotes_Refunds").removeClass("active");
		  $("#tabs_knowmore").removeClass("active");
		  $("#tabs_brandInfo").removeClass("active");
		  $("ul.tabs.pdp.productTabs li").removeClass('active'); 
		  $("ul.tabs.pdp.productTabs li").eq(0).addClass('active');  
	  }
	  else {
		  $("body,html").animate({ scrollTop: $(".product-specification-accordion.smk_accordion.acc_with_icon div#styleNotesMobile").offset().top - 150 }, "slow");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#styleNotesMobile").addClass("acc_active");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#styleNotesMobile .acc_content#stylenoteAccordion").css("display","block");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#knowmoreMobile").removeClass("acc_active");
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#detailsMobile").removeClass("acc_active");*/
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#descriptionMobile").removeClass("acc_active");*/
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#warrantyMobile").removeClass("acc_active");*/
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#brandInfoMobile").removeClass("acc_active");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#returnsAndRefundsMobile").removeClass("acc_active");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#knowmoreMobile .acc_content#knowmoreAccordion").css("display","none");
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#detailsMobile .acc_content#detailsAccordion").css("display","none");*/
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#descriptionMobile .acc_content#descriptionAccordion").css("display","none");*/
		  /*$(".product-specification-accordion.smk_accordion.acc_with_icon div#warrantyMobile .acc_content#warrantyAccordion").css("display","none");*/
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#brandInfoMobile .acc_content#brandInfoAccordion").css("display","none");
		  $(".product-specification-accordion.smk_accordion.acc_with_icon div#returnsAndRefundsMobile .acc_content#returnRefundAccordion").css("display","none");
	  }
	  
});
/*Feature for FineJewellery scroll end*/

/*start change of INC144316778*/
$(window).on("scroll resize",function(){
	fixTopAdjust();
	if($(window).scrollTop() == 0){
		$(".listing.wrapper .left-block").removeClass("topTheFix");
		/*start change for TISPRDT-1898*/
		if($("header .content .top").hasClass("header_fix"))
			$(".listing.wrapper .left-block").removeClass("fix"); 
		/*end change for TISPRDT-1898*/
	}
});
$(document).on("click",".product-facet .facet .facet-name",function(){
	fixTopAdjustTimeOut();
});
function fixTopAdjust(){
	if($(".left-block ul.product-facet.js-product-facet.listing-leftmenu").outerHeight() <= $(window).height() && ($(".listing.wrapper .left-block").hasClass("fix") && $("header .content .bottom").hasClass("active")))
		$(".listing.wrapper .left-block").addClass("topTheFix");
	else
		$(".listing.wrapper .left-block").removeClass("topTheFix");
}
function fixTopAdjustTimeOut(){
	setTimeout( function(){ 
		fixTopAdjust(); 
	}  , 100 );
}
/*end change of INC144316778*/
/*TPR-6148 start*/
function copyCode(elem) {
	//alert();
	  var x = $("<input>");
	  $("body").append(x);
	  x.val($(elem).parents(".coupon-box").find("p.copycode").text()).select();
	  document.execCommand("copy");
	  x.remove();
	  $('.copycode_message').fadeIn(function() {
		  window.setTimeout(function() {
		    $('.copycode_message').fadeOut('slow');
		  }, 1500);
		});
	}
	/*TPR-6148 end*/

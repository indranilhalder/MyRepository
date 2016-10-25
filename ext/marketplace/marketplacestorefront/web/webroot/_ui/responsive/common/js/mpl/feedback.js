/*----------Start of  validate email in feedback-----------*/
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
			var marketplaceHeader = !($("header").children().hasClass("header-inner"));
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
		 
			 var x = $(".js-mini-cart-count").text();
			   $(".js-mini-cart-count-hover").text(parseInt($(".js-mini-cart-count").text()));
			   
				  
			   $(window).scroll(function () {
				   if(marketplaceHeader){
					  /*--------changes for Sticky Header in MyBag--------------*/
				   if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login"))){
						 if( $(window).scrollTop() > $('.bottom').offset().top && !($('.bottom').hasClass('active'))){
				      $('.bottom').addClass('active');
				    } else if ($(window).scrollTop() == 0){
				      $('.bottom').removeClass('active');
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
			
			var sessionFacetName;
			$(document).on("click",".facet-name.js-facet-name h4",function(){
				if(typeof(Storage) !== "undefined") {
					
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
				if(sessionStorage.getItem($(this).find('.js-facet-name > h4').text()) == "false" && null != sessionStorage.getItem($(this).find('.js-facet-name > h4').text())) {
					$(this).find('.js-facet-name h4').removeClass('active');
					$(this).find('.facet-values.js-facet-values.js-facet-form').hide(100);
					$(this).find('.brandSelectAllMain').hide(100);
				}
			});
			if(sessionStorage.getItem($('ul.product-facet > .js-facet-name > h4').text()) == "false" && null != sessionStorage.getItem($('ul.product-facet > .js-facet-name > h4').text())) {
				$('ul.product-facet > .js-facet-name > h4').removeClass('active');
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
			
			 $("#signIn_link").on("click",function(e) {
			      $(this).addClass('active');
			      $("#SignUp_link,#sign_up_content").removeClass("active");
			      $("#sign_in_content").addClass("active");
			    });
			 $("#SignUp_link").on("click",function(e) {
			      $(this).addClass('active');
			      $("#signIn_link, #sign_in_content").removeClass("active");
			      $("#sign_up_content").addClass("active");
			    });
	/*----Start of SignIn & SignUp tab Switching -----*/
			 
	/*----Start of  PDP tabs -----*/
			 $(".tabs-block .nav.pdp li").on("click",function(e) {
				 $("ul.nav.pdp li").removeClass('active'); 
				 $(this).addClass('active');
				 var count = $(this).index();
				 $("ul.tabs.pdp>li").removeClass('active'); 
				 $("ul.tabs.pdp>li").eq(count).addClass("active");
			    }); 
	/*----END of  PDP tabs -----*/
	
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
			 var paymentModes =  $("#viewPaymentCredit, #viewPaymentDebit, #viewPaymentNetbanking, #viewPaymentCOD, #viewPaymentEMI");
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
				
		
	    $(".brandCategory").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    });
	    
	    
	    $(".cmsManagedBrands").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    });
	    
	
	    
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
							}
						},
						fail : function(data) {
							alert("Failed to load categories");
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
				
		$(".pagination").parents(".bottom-pagination").find("li.prev a").append("<span class='lookbook-only'> Page</span>");
		$(".pagination").parents(".bottom-pagination").find("li.next a").append("<span class='lookbook-only'> Page</span>");
		
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
	
	if($(".product-listing.product-grid.hero_carousel").children().length>0){
		$(".product-listing.product-grid.hero_carousel").css("border-bottom","2px solid #f0f4f5");
		$(".product-listing.product-grid.hero_carousel").before("<h3 class='heroTitle'>Shop Our Top Picks</h3>");
		
	}
	
	/*---- END of added for hero products demarcation ---*/
	
	/*---Start of Authentic and exclusive function call---*/
	
		 authentic();
	
		 $(window).on("load resize",function(){
				if($(".js-mini-cart-count").text() != undefined && $(".js-mini-cart-count").text()!=null)
					{
					$(".responsive-bag-count").text($(".js-mini-cart-count").text());
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
					$(this).parent('li').siblings().find('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
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
						if($(this).hasClass('menu-dropdown-arrow')){
							for(var i = $("."+id+" .short.words").eq(ind).index(); i < $("."+id+" .short.words").eq(ind+1).index()-1; i++) {
								$("."+id+" .long.words").eq(i-ind-1).show();
							}
							if(ind == ($("."+id+" .short.words").length/2)-1) {
								$("."+id+" .short.words").eq(ind).nextAll().show();
							}
						} else {
							$(".long.words").hide();
						}
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
		});
	$(".customer-care-tabs>.tabs .customer-care-tab>ul>li .tabs li").click(function(){
							$(this).nextAll().removeClass( "active" );
							$(this).prevAll().removeClass( "active" );
	 });	 
		
		/*--- END of  Mobile view Left menu Sign In toggle---- */
	/*--- Start of  Mobile view sort by arrow in SERP and PLP---- */
	
	$(".progtrckr .progress.processing").each(function(){
		var len = $(this).children("span.dot").length;
		if(len == 2) {
			$(this).children("span.dot").first().css("marginLeft","16.5%");
		} else if(len == 1) {
			$(this).children("span.dot").first().css("marginLeft","33%");
		}

	}); 
	$(".progtrckr").each(function(){
		$(this).find(".progress.processing .dot:not(.inactive)").last().find('img').show();
	});
	
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

	$(window).on("load resize", function() {
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
			$(".imageList ul li img").css("height", thumbnailImageHeight);
		});
	
	$('.marketplace-checkout').find('a').click(function(e){
		e.preventDefault();
	});
	$(".marketplace-checkout").parents().find('header .content .top .marketplace.compact a').hide();
	  $(window).scroll(function () {
		  if($(".ui-autocomplete").is(":visible") && marketplaceHeader){
			  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
				  left : $('#js-site-search-input').offset().left,
				  width: $('#js-site-search-input').outerWidth()
			  });
		  }
		  
	  });
	  $('#js-site-search-input').keydown(function () {
		  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
				  left : $('#js-site-search-input').offset().left,
				  width: $('#js-site-search-input').outerWidth()
			  });
	  });
	  
	  $("input[name='j_username']").parents("form#extRegisterForm").attr("autocomplete","off");
		$("input[name='email']").parents("form#loginForm").attr("autocomplete","off");
		$("input[name='j_username'],input[name='email']").attr("autocomplete","off");
		$("input[type='password']").attr("autocomplete","new-password");
		$(window).on("load resize", function() {
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
		});
		
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
		
		$(window).on("resize", function() {
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				var footer_height=$('footer').height() + 20 + 'px';
				$(".body-Content").css('padding-bottom',footer_height);
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
					$(this).children('ul').css("height","450px");
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
				filter_height=$(".searchSpellingSuggestionPrompt").outerHeight() + 95;
			} else {
				filter_height=$(".facet-list.filter-opt").height() + 55;
			}
			$(".listing.wrapper .left-block").css("margin-top",filter_height+"px");
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
			if($(window).width() <= 633){
				$('.listing.wrapper .left-block').css('margin-top','20px');
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
					var left_block_top_margin= $(".searchSpellingSuggestionPrompt").height() + 40;
					$('.listing.wrapper .left-block').css('margin-top',left_block_top_margin+'px');
				}
			}
		});
		setTimeout(function () {
		$("body.page-cartPage .cart.wrapper .product-block li.item>ul.desktop>li.delivery").addClass("collapsed");
				$(".mobile-delivery").click(function(){
					$(this).parents("li.delivery").toggleClass("collapsed");
				});
		}, 100);
		$(window).on("load resize", function() {
			$("body.page-cartPage .cart.wrapper .product-block li.item").each(function(){
				if($(this).find("ul.desktop>li.price").css("position")=="absolute"){
					var price_height=$(this).find("ul.desktop>li.price").height() + 15;
					$(this).find(".cart-product-info").css("padding-bottom",price_height+"px");
					var price_top = $(this).find(".cart-product-info").height() + 20;
					$(this).find("ul.desktop>li.price").css("top",price_top+"px");
					var qty_top = price_top + price_height - 1;
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
			}
			});
		
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
	
	
});
$(window).resize(function() {

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
	$("li.filter-Size.deactivate").removeClass("deactivate");
	$('.facet.js-facet.Size .more-lessFacetLinks .more').hide();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').show();
});
$(document).on("click",".facet.js-facet.Size .more-lessFacetLinks .less",function(e) {
	e.preventDefault();
	sizeSwatch();
	$('.facet.js-facet.Size .more-lessFacetLinks .more').show();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').hide();
});
$(document).on('click','.facet.js-facet.Colour .js-facet-name h4',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			colorSwatch();
		}
	},80)
});
$(document).on('click','.facet.js-facet.Size .js-facet-name h4',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			sizeSwatch();
		}
	},80)
});
//$(document).on('click','.left-block .toggle-filterSerp',function(){
function toggleFilter(){
		colorSwatch();
		sizeSwatch();
		//Mobile view filter ajax
		$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
		$(".toggle-filterSerp").toggleClass("active");
}
//});

function colorSwatch() {
	var row = 0, start = 0, count = 0, end = 0, back = true;
	$("li.filter-colour").removeClass("deactivate");

	end = $("li.filter-colour").length;

	$("li.filter-colour").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$("li.filter-colour").slice(start, end).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	
	count = $("li.filter-colour.deactivate").length;
	$(".colourNumber").text(count);
	if(count == 0) {
		$('.facet.js-facet.Colour .more-lessFacetLinks').hide();
	} else if (count == 1) {
		$('.facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colour");
		$('.facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	} else {
		$('.facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colours");
		$('.facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	}
	
}


function sizeSwatch() {
	var row = 0, start = 0, count = 0, end = 0, back = true;
	$("li.filter-size").removeClass("deactivate");

	end = $("li.filter-size").length;

	$("li.filter-size").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$("li.filter-size").slice(start, end).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	count = $("li.filter-size.deactivate").length;
	$(".facet.js-facet.Size .sizeNumber").text(count);
	if(count == 0) {
		$('.facet.js-facet.Size .more-lessFacetLinks').hide();
	} else if (count == 1) {
		$('.facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("size");
		$('.facet.js-facet.Size .more-lessFacetLinks').show();
	} else {
		$('.facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("sizes");
		$('.facet.js-facet.Size .more-lessFacetLinks').show();
	}

}
/*colour and size swatch 3 lines TISPRM-123*/

/*added for gigya   TISPT-203 */
function callGigya(){
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

/* Changes for TISPT-203 ends  */

/*TPR-198 : Page reload on SortBy and ViewBy*/
//Sort by filter
function sortByFilterResult(top){
	 $("#hidden-option-width").html($(".sort-refine-bar select").find('option:selected').text());
	 var option_width=$("#hidden-option-width").width() + 22;
	$(".sort-refine-bar select.black-arrow-left").css("background-position-x",option_width);
	
	$('#sortForm'+top).submit();
}

//View by filter
function viewByFilterResult(top){
	$('#pageSize_form'+top).submit();
}
/*Filter scroll changes start*/
$(window).on("scroll",function(){
	if($(window).width() > 790 && $('.listing.wrapper .right-block').height() > $('.listing.wrapper .left-block').height()) {
		
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
		hit({
			windowWidth : $(window).width()
		});
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
})

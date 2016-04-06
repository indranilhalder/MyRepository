
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
			  $("ul#ui-id-1").attr("style",style);
			  
			  $("#js-site-search-input").keypress(function(){
	
				  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css("border","1px solid #dfd1d5");
				});
	
				if($('body').hasClass("template-pages-layout-micrositePage1")){
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
		 $('a.stockLevelStatus').parents('div.image').find('a.thumb img').addClass('out-of-stock-product');
		 
	/* -----------------END  of Out of stock styling---------*/
		 
		 
	/*----start of Sticky Bag --------*/
		 
			 var x = $(".js-mini-cart-count").text();
			   $(".js-mini-cart-count-hover").text($(".js-mini-cart-count").text());
			   
				  
			   $(window).scroll(function () {
					  /*--------changes for Sticky Header in MyBag--------------*/
				   if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login"))){
						 if( $(window).scrollTop() > $('.bottom').offset().top && !($('.bottom').hasClass('active'))){
				      $('.bottom').addClass('active');
				    } else if ($(window).scrollTop() == 0){
				      $('.bottom').removeClass('active');
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
			
	$(".facet-name.js-facet-name h4").on("click",function(){
				
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
			});
	
			$(".toggle-filterSerp").click(function(){
				$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
				$(this).toggleClass("active");
				$(".facet-name.js-facet-name h4").toggleClass("active");
				$(".product-facet.js-product-facet.listing-leftmenu").find('.facet-list.js-facet-list').removeClass('active');
				$(".product-facet.js-product-facet.listing-leftmenu").find('div#searchPageDeptHierTree').hide();
				
			});
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
			
			$(".toggle").on("click",function(e){
				var p = $(e.currentTarget).parent();
			    if(p.hasClass('active')) {
			      p.removeClass('active');
			    } else {
			      p.addClass('active');
			    }
			    var style=null;
				if($(window).width() < 773) {
					$("span#mobile-menu-toggle").unbind('click');
					$("span#mobile-menu-toggle").click(function(){
						$(this).parent('li').siblings().find('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
						$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
						$(this).next().slideToggle();
						$(this).toggleClass("menu-dropdown-arrow");
					});
					/*--- Mobile view shop by brand and department ---*/

					 $("li.short.words").siblings("li.long.words").hide();
					 $("li.short.words").unbind('click');
					  $("li.short.words").click(function(){
						$(this).toggleClass('active');
					    $(this).nextAll().each(function(){

					      if($(this).hasClass("short")) {
					        return false;
					      }

					      $(this).toggle(200);
					    });

					  });

					/*--- Mobile view shop by brand and department ---*/ 
				}
				else {
					$("#mobile-menu-toggle").next().attr("style",style);
					$("li.short.words,li.long.words").next().attr("style",style); 
				} 
			});
			$("footer h3.toggle").click(function(e){
				
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
	
				 /*$(this).find('header').first().addClass('compact');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 });*/
				 
				 $(this).find('header').find('.compact-toggle').toggleClass('open');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 });
			 }
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
	    	window.history.pushState('obj', 'newtitle', '/store/mpl/en/brands/brandlist?cat='+elementId);
	    });
	    
	    
	    $(".cmsManagedBrands").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	window.history.pushState('obj', 'newtitle', '/store/mpl/en/brands/brandlist?cat='+elementId);
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
			console.log("active"+liHeight);
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
			$("header .content .top .toggle").click(function(){
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
		
		if($('body').find('a.wishlist#wishlist').length > 0){
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
			content: function() {
				return $(this).parents().find('.add-to-wishlist-container').html();
			}
		});
	  }
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
									console.log(data);
									$.each(data, function(k, v) {
										listSelect += '<option value="'+v+'">'
												+ v + '</option>';
									});
									console.log(listSelect);
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
				$("span#mobile-menu-toggle").unbind('click');
				$("span#mobile-menu-toggle").click(function(){
					$(this).parent('li').siblings().find('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
					$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
					$(this).next().slideToggle();
					$(this).toggleClass("menu-dropdown-arrow");
				});
				/*--- Mobile view shop by brand and department ---*/

				 $("li.short.words").siblings("li.long.words").hide();
				 $("li.short.words").unbind('click');
				  $("li.short.words").click(function(){
					  $(this).toggleClass('active');
				    $(this).nextAll().each(function(){

				      if($(this).hasClass("short")) {
				        return false;
				      }

				      $(this).toggle(200);
				    });

				  });

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
	$(window).on("load",function(e){
		$('.sort-refine-bar.mobile').append('<span id="hidden-option-width" style="display: none;"></span>')
		$(".sort-refine-bar select.black-arrow-left").css("display","block");
		/*$(".sort-refine-bar select.black-arrow-left").css("background-position-x","30%");*/
		$(".sort-refine-bar select").change(function(){
			 $("#hidden-option-width").html($(this).find('option:selected').text());
			 var option_width=$("#hidden-option-width").width() + 30;
			$(".sort-refine-bar select.black-arrow-left").css("background-position-x",option_width);
		});
	});
	/*--- End of  Mobile view sort by arrow in SERP and PLP---- */
	
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
	
	$(window).on("load resize",function(){
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
	});
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
		  if($(".ui-autocomplete").is(":visible")){
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
	 		$("header .content nav > ul > li > ul > li > .toggle a").click(function(){
	 			$(this).attr("href","#");
	 		});
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
		
		$(window).on("load resize", function() {
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				var footer_height=$('footer').height() + 20 + 'px';
				$(".body-Content").css('padding-bottom',footer_height);
			}
			else{
				$(".body-Content").css('padding-bottom','0px');
			}
		});		
});

ACC.carousel = {

	_autoload: [
	    // "myFun",
	     "shopTheStyleCarousel",
	     "shopTheStyleHomeCarousel",

	     "StyleEditCarousel",

	     

	     "shopTheLookCarousel",

	     "ClpTopDealsCarousel",

	     "ClpBestOffersCarousel",
	     "BlpBestOffersCarousel",

	     "blpTopDealsCarousel",		//add for INC_11189

	     "ClpBestSellerCarousel",

	     "shopByLookCarousel",
	     "offersCarousel",
	     /*"blpTopDealsCarousel",*/		//commented for INC_11189
	     "categoryCarousel",
	     "myStyleCarousel",
	     "heroProductCarousel",
	     "springflingCarousel",
	     "myReviewCarousel",
	     "advancedCategoryCarousel",
	     "pdpProductCarousel",
	     "AddressCarousel",
	       
		["bindCarousel", $(".js-owl-carousel").length >0]
	],

	carouselConfig:{
		"default":{
			navigation:true,
			rewindNav: false,
			navigationText : ["<span class='glyphicon glyphicon-chevron-left'></span>", "<span class='glyphicon glyphicon-chevron-right'></span>"],
			pagination:false,
			itemsCustom : [[0, 1], [640, 2], [1024, 5], [1400, 7]]
		},
		"rotating-image":{
			navigation:false,
			pagination:true,
			singleItem : true,
			rewindNav: false
		},
		"lazy-reference":{
			navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,7], 
			itemsDesktopSmall : [1400,5], 
			itemsTablet: [768,3], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true		
		}
	},

	bindCarousel: function(){
		
		$(".js-owl-carousel").each(function(){
			var $c = $(this)
			$.each(ACC.carousel.carouselConfig,function(key,config){
				if($c.hasClass("js-owl-"+key)){
					var $e = $(".js-owl-"+key);
					$e.owlCarousel(config);
				}
			});
		});

	},
	
	categoryCarousel: function(){
		
		$("#mplCategoryCarousel").owlCarousel({
		
					items:4,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
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
            				items:4,
            			}			
            		}	
		
			/*navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true*/
		});
	},
	
	myFun: function(){
		$(".electronic-rotatingImage").owlCarousel({
			items:1,
    		//loop: true,
			loop: $("#rotatingImage img").length == 1 ? false : true,
    		nav:true,
    		dots:false,
    		navText:[]
		});
		/*TPR-268*/
		/*$("#rotatingImageMobile").owlCarousel({
			items:1,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		});*/
		/*if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
			//alert(timeout);
			$("#rotatingImageTimeout").owlCarousel({
				navigation:false,
				rewindNav: true,
				autoPlay: timeout,
				navigationText : [],
				pagination:true,
				singleItem:true,
				autoHeight : true
			});
		}*/
	 
		//Desktop view
		/*TISSQAEE-403 Start*/
		alert("~~~~~~~"+homePageBannerTimeout);
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
			alert("-------------"+timeout);
			var loop = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false;
			var dots = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false; 
			//$(".home-rotatingImage").owlCarousel({
			$("#rotatingImageTimeout").owlCarousel({
				items:1,
				nav:false,
				dots:dots,
				loop: loop,
		        autoplay: true,
		        //autoHeight : true, //UF-365
		        autoplayTimeout: timeout
		    });
			//UF-291 starts here ---- UF-365 starts here 
			if ($(window).width() > 773) {
				$("#rotatingImageTimeout img").each(function() {
				    if ($(this).attr("data-src")) {
						$(this).attr("src",$(this).attr("data-src"));
						$(this).removeAttr("data-src");
						$(this).load(function(){
							$(this).css("display", "block");
						});
					}	
				});
			}
			//UF-291 ends here
		}
			//Mobile View
			if(typeof homePageBannerTimeout!== "undefined"){
				var timeout = parseInt(homePageBannerTimeout) * 1000 ;
				var loop = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
				var dots = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
				
				$("#rotatingImageTimeoutMobile").owlCarousel({
					
					items:1,
					nav:false,
					dots:dots,
					loop: loop,
			        autoplay: true,
			        //autoHeight : true, //UF-365
			        autoplayTimeout: timeout
			    });
				//UF-291 starts here ---- UF-365 starts here
				if ($(window).width() <= 773) {	
					$("#rotatingImageTimeoutMobile img").each(function() {
					    if ($(this).attr("data-src")) {
							$(this).attr("src",$(this).attr("data-src"));
							$(this).removeAttr("data-src");
							$(this).load(function(){
								$(this).css("display", "block");
							});
						}	
					});
				}
				//UF-291 ends here
			/*TISSQAEE-403 End*/
			/*TPR-268*/
			/*$("#rotatingImageTimeoutMobile").owlCarousel({
				items:1,
				dots:true,
				loop: false,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });*/
		/*	$("#rotatingImageTimeout").append('<div class="hbpagination"></div>');
			var bannerLength = $('#rotatingImageTimeout .owl-item').length;
			for (var i = 0 ; i<bannerLength; i++ ) {
				$("#rotatingImageTimeout .hbpagination").append('<span class="hb-page"></span>');
			}
			$("#rotatingImageTimeout .hb-page").first().addClass('active');*/
			
			/*$(document).on("click",".hb-page",function(){
				var req_pos = $(this).index(),cur_pos = $('.hb-page.active').index() ;
				
				if(req_pos < cur_pos) {
						ACC.carousel.homePageBannerCarousel(bannerLength - (cur_pos - req_pos));
				} else if (req_pos > cur_pos) {
						ACC.carousel.homePageBannerCarousel(req_pos - cur_pos);
				}
				$('.hb-page').removeClass('active');
				$(this).addClass('active');
			});*/
			
		/*	setInterval(function(){
				var ind = $('.hb-page.active').index();
				if(ind == bannerLength-1) {
					$('.hb-page').removeClass('active');
					$('.hb-page').eq(0).addClass('active');
				} else {
					$('.hb-page').removeClass('active');
					$('.hb-page').eq(ind+1).addClass('active');
				}
				ACC.carousel.homePageBannerCarousel(1);
			},timeout);*/
		}
	},
	
	
	StyleEditCarousel: function(){
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		}
		else{
			var timeout = 0 ;
		}
			$(".style_edit .home-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit .home-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit .home-rotatingImage img").length == 1)?false:true,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
			$(".style_edit .electronic-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit .electronic-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit .electronic-rotatingImage img").length == 1)?false:true,
				autoplay: true,
				autoHeight : true,
				autoplayTimeout: timeout
			});
		       // TISPRDT-1159
		       $(".style_edit_blp .home-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit_blp .home-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit_blp .home-rotatingImage img").length == 1)?false:true,
				autoplay: true,
				autoHeight : true,
				autoplayTimeout: timeout
			});
		       //TISPRDT-2196 starts    
 		       if ( $(".style_edit_blp .home-rotatingImage").length || $(".style_edit .home-rotatingImage").length) {
 			    	 if ($("#rotatingImageTimeout img").length) { 
 		    	   		$("#rotatingImageTimeout img").each(function() {
 						    if ($(this).attr("data-src")) {
 								$(this).attr("src",$(this).attr("data-src"));
 								$(this).removeAttr("data-src");
 								$(this).load(function(){
 									$(this).css("display", "block");
 								});
 							}	
 						});
 			    	 }	
 		       }
 		     //TISPRDT-2196 ends		
	},
	
	/*homePageBannerCarousel: function(count){
		
			var iw = $('#rotatingImageTimeout .owl-item').outerWidth(), ih = $("#rotatingImageTimeout .owl-item").first().next().find('.hero').height();
			$("#rotatingImageTimeout .owl-wrapper").css('transition','all 0.5s linear');
			$("#rotatingImageTimeout .owl-wrapper").css('transform','translate3d(-'+iw+'px, 0px, 0px)');
			$("#rotatingImageTimeout .owl-wrapper-outer").css('height',ih);
			setTimeout(function(){
			$("#rotatingImageTimeout .owl-wrapper").append($("#rotatingImageTimeout .owl-item").first());
			$("#rotatingImageTimeout .owl-wrapper").css('transition','all 0s linear');
			$("#rotatingImageTimeout .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
			count--;
			if(count > 0) {
				ACC.carousel.homePageBannerCarousel(count);
			}
		
		},500);
	},*/
	
	shopByLookCarousel: function(){
		$(".shopByLookCarousel").owlCarousel({
			items:2,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
		});
	},

	/*----------TPR-179(Shop The Style Start)------------*/
	shopTheStyleCarousel: function(){
		$("body.page-shopTheStyle .shopByLookCarousel").owlCarousel({
			items:3,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
			responsive : {
						// breakpoint from 0 up
            			0 : {
            				items:2,
            			},
            			// breakpoint from 785 up
            			785 : {
            				items:3,
            			}		
            		}
			
		});
	},
	shopTheStyleHomeCarousel: function(){
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		}
		else{
			var timeout = 5000 ;
		}
	//var timeout = parseInt(homePageBannerTimeout) * 1000 ;
	var owl1 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeout"); 
	owl1.owlCarousel({
			items:1,
			nav:false,
			dots:true,
			loop: true,
	        autoplay: true,
	        autoHeight : true,
	        autoplayTimeout: timeout
	    });
	var owl2 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeoutMobile"); 
	owl2.owlCarousel({
		items:1,
		nav:false,
		dots:true,
		loop: true,
        autoplay: true,
        autoHeight : true,
        autoplayTimeout: timeout
    });
	    $('.owl-dot1 a').click(function(e){
	    				
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  0);
			owl2.trigger('to.owl.carousel',  0);
			//console.log("1");
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot2 a').click(function(e){		
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  1);
			owl2.trigger('to.owl.carousel',  1);
			//console.log("2");	
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot3 a').click(function(e){		
			e.preventDefault();
			$('.owl-controls li').removeClass('active');				
			owl1.trigger('to.owl.carousel',  2);
			owl2.trigger('to.owl.carousel',  2);
			//console.log("3");	
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot4 a').click(function(e){	
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  3);
			owl2.trigger('to.owl.carousel',  3);
			//console.log("4");
			$(this).parent().parent().addClass('active');
		});
		 
		/*var owl2 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeoutMobile");*/ 
    },
	 /*----------End TPR-179(Shop The Style)------------*/  

	ClpBestSellerCarousel: function(){
		$(".best_seller_section .shopByLookCarousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		autoWidth: true,	/*add for TISCSS-64*/ 
    		responsive: {
                0: {
                    items: 1,
    				stagePadding: 50
                },
                480: {
                    items: 2,
    				stagePadding: 75
                },
                700: {
                    items: 3
                },
                1000: {
                    items: 5
                }
            }
		});
	},

	/*sprint8(TPR-1672 CLP)*/

	offersCarousel: function(){
		$(".offersCarousel").owlCarousel({
					/*autoWidth : true,*/	/*add for INC144315059*/	/*commented for TISPRDT-1936*/
					items:5,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:1,
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
		/*sprint8(TPR-1672 CLP)*/
		
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},

	shopTheLookCarousel: function(){
		$(".shop_the_look .shopByLookCarousel").owlCarousel({
			/*items:2,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		    responsive : {
			// breakpoint from 0 up
			0 : {
				items:1,
				stagePadding: 50,
			},
			// breakpoint from 480 up
			480 : {
				items:2,
				stagePadding: 75,
			},
			// breakpoint from 768 up
			700 : {
				items:3,
			},
			// breakpoint from 768 up
			1000 : {
				items:5,
			}			
		}*/
			items:3,
			nav: true,
	        navText: [],
	        loop: true,
	        responsive: {
	            0: {
	                items: 1,
					stagePadding: 75
	            },
	            480: {
	                items: 2,
					stagePadding: 75
	            },
	            768: {
	                items: 2
	            },
	            1280: {
	                items: 3
	            }
	        }
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
		});
	},

	/* start change for INC_11189*/
	/*blpTopDealsCarousel: function(){
		$(".top_deal_blp #mplAdvancedCategoryCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length == 1)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 2)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 3)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 768 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 5)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 5)?false:true,
            			}			
            		}	
			navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],
			});
	},*/
	blpTopDealsCarousel: function(){
		//changes for tpr-599(BLP and CLP changes)
		//changes for TISPRDT-6243
		var slideBy= $('.top_deal_blp #slideByOffer').val()?$('.top_deal_blp #slideByOffer').val():1;
		var autoplayTimeout= $('.top_deal_blp #autoplayTimeoutOffer').val()?$('.top_deal_blp #autoplayTimeoutOffer').val():5000;
		var autoPlay= $('.top_deal_blp #autoPlayOffer').val()?$.parseJSON($('.top_deal_blp #autoPlayOffer').val()):true;
		
		//CAROUSEL AUTOWIDTH ATTRIBUTE
		var autoWidthAttr = false;
		if($(window).width()<481){
			autoWidthAttr = true;
		} else {
			autoWidthAttr = false;
		}
		$(".top_deal_blp .offersCarousel").owlCarousel({
					/*autoWidth : true,*/	/*add for INC144315059*/	/*commented for TISPRDT-1936*/
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
            		autoWidth: autoWidthAttr,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal_blp .offersCarousel .image").length == 1)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 2)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 3)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 768 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 5)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}	
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	/* end change for INC_11189*/	

	ClpTopDealsCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('.top_deal #slideByOffer').val()?$('.top_deal #slideByOffer').val():1;
		var autoPlay= $('.top_deal #autoPlayOffer').val()?$.parseJSON($('.top_deal #autoPlayOffer').val()):true;
		var autoplayTimeout= $('.top_deal #autoplayTimeoutOffer').val()?$('.top_deal #autoplayTimeoutOffer').val():5000;
		
		$(".top_deal .offersCarousel").owlCarousel({
					autoWidth : true,	/*add for INC144315059*/	/*add for TISPRDT-1936*/
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},

	/*sprint8 TPR-1672*/
	ClpBestOffersCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('#slideByOffer').val()?$('#slideByOffer').val():1;
		var autoPlay= $('#autoPlayOffer').val()?$.parseJSON($('#autoPlayOffer').val()):true;
		var autoplayTimeout= $('#autoplayTimeoutOffer').val()?$('#autoplayTimeoutOffer').val():5000;
		
		$(".best-offers .offersCarousel").owlCarousel({
					/*autoWidth : true,*/	/*add for INC144315059*/	/*commented for TISPRDT-1936*/
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".best-offers .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".best-offers .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".best-offers .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".best-offers .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	
BlpBestOffersCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('#slideByOffer').val()?$('#slideByOffer').val():1;
		var autoPlay= $('#autoPlayOffer').val()?$.parseJSON($('#autoPlayOffer').val()):true;
		var autoplayTimeout= $('#autoplayTimeoutOffer').val()?$('#autoplayTimeoutOffer').val():5000;
		
		$(".best-offers_blp .offersCarousel").owlCarousel({
					/*autoWidth : true,*/	/*add for INC144315059*/	/*commented for TISPRDT-1936*/
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".best-offers_blp .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	
	/*sprint8 TPR-1672*/


	myStyleCarousel: function(){
		$(".mystyle-carousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				items:3,
    			},			
    			// breakpoint from 807 up
    			807 : {
    				items:5,
    			}			
    		}	
			
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:5,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: [807,3],
			itemsMobile : false*/
		});
		
	},
	
	heroProductCarousel: function() {
		$(".product-listing.product-grid.hero_carousel").owlCarousel({
					items:4,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
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
            				items:4,
            			}			
            		}	
			/*navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			lazyLoad:true*/
		});
	},
	
	springflingCarousel: function() {
		$("div.shop-sale.wrapper #defaultNowTrending").owlCarousel({
			items:6,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
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
    				items:6,
    			}			
    		}	
			/*navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,6], 
			itemsDesktopSmall : [1400,6], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2]*/
		});
	},
	
	advancedCategoryCarousel: function(){
		//setTimeout(function() {
			console.log("inside timeout");
		$("#mplAdvancedCategoryCarousel").owlCarousel({
					items:4,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
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
            				items:4,
            			}			
            		}	
		/*navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true*/
		});
		//},7000);
		console.log("outside timeout");
	},
	
	myReviewCarousel: function(){
		$("#my-review-carousel").on('initialize.owl.carousel initialized.owl.carousel', function() {
			$("#my-review-carousel").show();
                });
		$("#my-review-carousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
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
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			afterInit: function() {$("#my-review-carousel").show();}*/
		});
	},
	
	pdpProductCarousel: function(){
		$("#pdpProductCarousel").owlCarousel({
			items:1,

    		loop: $("#pdpProductCarousel img").length == 1 ? false : true,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				nav:false,
    	    		dots:true,
    			},
    			// breakpoint from 768 up
    			768 : {
    				nav:true,
    	    		dots:false,
    			}			
    		}	
		});
		$(".product-image-container.device .owl-stage-outer").prepend($(".product-image-container.device .wishlist-icon"))
	},
	AddressCarousel: function(){
		if($(".checkTab .addressList_wrapper .address-list").length == 2){
			$("#address_carousel").addClass("two_address");
		}
		if($(".checkTab .addressList_wrapper .address-list").length == 1){
			$("#address_carousel").addClass("one_address");
		}
	      $("#address_carousel").on('initialize.owl.carousel initialized.owl.carousel ' +
	                'initialize.owl.carousel initialize.owl.carousel ' +
	                'to.owl.carousel changed.owl.carousel',
	                function(event) {
						var items     = event.item.count;     // Number of items
						var item      = event.item.index;     // Position of the current item
						
						if($(window).width() > 1263){
						var page_no = parseInt(items/3);
						if(items%3 > 0){
							page_no = parseInt(items/3) + 1;
						}
						var current_page = parseInt(item/3) + 1;
						if(item%3 > 0){
							current_page = parseInt(item/3) + 2;
						}
						}
						else{
							var page_no = parseInt(items/2);
							if(items%2 > 0){
								page_no = parseInt(items/2) + 1;
							}
							var current_page = parseInt(item/2) + 1;
							if(item%2 > 0){
								current_page = parseInt(item/2) + 2;
							}
						}
						$(".page_count").html("<span>"+current_page + " / " + page_no+"</span>");
	                });
	              $("#address_carousel").owlCarousel({
	                items:3,
					loop: false,
					nav: ($(".checkTab .addressList_wrapper .address-list").length <= 3)?false:true,
					dots:false,
					navText:[],
					slideBy: 3,
					margin: 82,
					responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 36,
            				slideBy: 1,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:2,
            				slideBy: 2,
            			},
            			// breakpoint from 1280 up
            			1280 : {
            				items:3,
            			}			
            		},
            		onRefresh: function () {
            			$("#address_carousel").find('div.owl-item').height('');
                    },
                    onRefreshed: function () {
                    	$("#address_carousel").find('div.owl-item').height($("#address_carousel").height());
                    }
	              });
	},
	
	/*shopBannerCarousel: function(){
	  $("#shopstyleCarousel").owlCarousel({
	 
	      autoPlay: 3000, //Set AutoPlay to 3 seconds
	 
	      items : 1,
	      loop: false
	 
	  });
	}*/
	
	/*New Homepage change*/
	/*timeoutCarousel: function(){
		var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		//alert(timeout);
		$("#rotatingImageTimeout").owlCarousel({
			navigation:false,
			rewindNav: true,
			autoPlay: timeout,
			navigationText : [],
			pagination:true,
			singleItem:true
		});
	}*/
	
	

};


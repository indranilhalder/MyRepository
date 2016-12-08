ACC.carousel = {

	_autoload: [

	     "shopTheStyleCarousel",
	     "shopTheStyleHomeCarousel",

	     "StyleEditCarousel",

	     "myFun",

	     "shopTheLookCarousel",

	     "ClpTopDealsCarousel",
	     "ClpBestSellerCarousel",

	     "shopByLookCarousel",
	     "offersCarousel",
	     "blpTopDealsCarousel",
	     "categoryCarousel",
	     "myStyleCarousel",
	     "heroProductCarousel",
	     "springflingCarousel",
	     "myReviewCarousel",
	     "advancedCategoryCarousel",
	     "pdpProductCarousel",
	       
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
	 
		
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
			$(".home-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:true,
				loop: true,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
		    

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

	offersCarousel: function(){
		$(".offersCarousel").owlCarousel({
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
	blpTopDealsCarousel: function(){
		$(".top_deal_blp #mplAdvancedCategoryCarousel").owlCarousel({
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
            				stagePadding: 75,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            			},
            			// breakpoint from 768 up
            			1000 : {
            				items:5,
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
	
	

	ClpTopDealsCarousel: function(){
		$(".top_deal .offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
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


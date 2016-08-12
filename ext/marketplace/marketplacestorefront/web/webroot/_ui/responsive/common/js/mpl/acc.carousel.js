ACC.carousel = {

	_autoload: [
	     "myFun",
	     "shopByLookCarousel",
	     "offersCarousel",
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
		$("#rotatingImage").owlCarousel({
			items:1,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		});
		/*TPR-268*/
		$("#rotatingImageMobile").owlCarousel({
			items:1,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		});
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
			//alert(timeout);
			$("#rotatingImageTimeout").owlCarousel({
				items:1,
				dots:true,
				loop: true,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
			/*TPR-268*/
			$("#rotatingImageTimeoutMobile").owlCarousel({
				items:1,
				dots:true,
				loop: true,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
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
		
		$("#mplAdvancedCategoryCarousel").owlCarousel({
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
    		loop: true,
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
	}
	
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

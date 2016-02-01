ACC.carousel = {

	_autoload: [
	     "myFun",
	     "shopByLookCarousel",
	     "categoryCarousel",
	     "myStyleCarousel",
	     "heroProductCarousel",
	     "springflingCarousel",
	     "advancedCategoryCarousel",
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
			navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true
		});
	},
	
	myFun: function(){
		$("#rotatingImage").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			singleItem:true
		});
		
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
	},
	shopByLookCarousel: function(){
		$(".shopByLookCarousel").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false
		});
	},
	myStyleCarousel: function(){
		$(".mystyle-carousel").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:5,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: [807,3],
			itemsMobile : false
		});
		
	},
	
	heroProductCarousel: function() {
		$(".product-listing.product-grid.hero_carousel").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			lazyLoad:true
		});
	},
	
	springflingCarousel: function() {
		$("div.shop-sale.wrapper #defaultNowTrending").owlCarousel({
			navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,6], 
			itemsDesktopSmall : [1400,6], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2]
		});
	},
	
	advancedCategoryCarousel: function(){
		
		$("#mplAdvancedCategoryCarousel").owlCarousel({
			navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true
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

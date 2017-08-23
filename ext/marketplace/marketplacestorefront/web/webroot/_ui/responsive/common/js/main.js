if ( ! window.TATA )
{
	window.TATA = {};
}
var TATA = window.TATA;


TATA.CommonFunctions = {
	Toggle: function() {
		
		$('.toggle-link').on('click', function(){
			var Target = $(this).data("target-id");
			$('.toggle-skip').not(Target).removeClass('active');
			$(Target).toggleClass('active');
			return false;
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
		});
	},
	
	MainBanner: function() {

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
		//var Acc = $('.accordion').find('.accordion-title');
		
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
        $('.shop-by-catagory-slider').slick({
            slidesToScroll: 6,
            slidesToShow: 6,
            variableWidth: false,
            infinite: false,
            arrows: false,
            swipe: false,
            dots: true,
            responsive: [
                {
                  breakpoint: 768,
                  settings: {
                      slidesToScroll: 1,
                      slidesToShow: 1,
                      infinite: true,
                      swipe: true,
                      variableWidth: true 
                  }
                }
            ]
        });
    },
	
	
	
	addTowishlist: function(){
		$('.add-to-wishlist').on('click', function(){
			$(this).toggleClass('added');
		});
	},
	
	
	leftBarAccordian: function(){
		if($(window).width() >=768){
		$('.facet:first').find('.allFacetValues').show();
		$('.facetHead').on('click', function(e){
			e.stopPropagation();
			$(this).closest('.facet').toggleClass('open', function(){
				$(this).find('.allFacetValues').slideToggle();
			});
		});
		}else{
			$('.facet:first').removeClass('open');
			$('.facetHead').on('click', function(e){
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
		
		SigninPopup: function(){
			$('.header-login-target-link').on('click', function(){
				var targetID = $(this).data('target-id');
				$('#header-account').removeClass('active-sign-in active-sign-up active-forget-password').addClass('active-'+targetID);
			});
			
		},
		
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
	
		init: function() {
			this.MobileMenu();
			this.SigninPopup();
		}
	
	},
	
	Footer: function() {
		if($(window).width() >= 768){
			$('footer').find('.accordion-title').removeClass('accordion-title');
		}
		$('#main-nav > ul').addClass('footer-cloned-ul').clone().appendTo('.footer-popular-search');
		$('.footer-cloned-ul > li').append('<br/>');
		
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
		_self.addTowishlist();
		_self.leftBarAccordian();
	
    }

};

TATA.Pages = {
	
	// PLP Page
	PLP: {
		
		// PLP Page filter
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
			_self.Filtershow();
			_self.showModelImg();
			_self.addGiftWrap();
			_self.TwoColumnseperator();
			_self.ProductSort();
			_self.productGrid();
			_self.productHover();
		}
	},
	
	PDP:  {
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
		
		// PDP Page initiate
		init: function () {
			var _self = TATA.Pages.PDP;
			_self.Slider();
		}
	},
	
	init: function () {
		var _self = TATA.Pages;
		_self.PLP.init();
		_self.PDP.init();
	}
};

 $(document).ready(function () {
	TATA.CommonFunctions.init();
	TATA.Pages.init();
	$("select").selectBoxIt(); 
	if($(window).width() <= 767){
		$('.sort-by-fature .selectboxit-text').html('SORT');
	}
	
});

$(window).scroll(function () {
	TATA.CommonFunctions.WindowScroll();
});

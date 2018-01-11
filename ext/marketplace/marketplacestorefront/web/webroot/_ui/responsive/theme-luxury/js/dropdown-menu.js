$(document).ready(function() {

    var window_width = $(window).width();
    if (window_width > 768) {
        executeForDesktop();
    }else{
    	executeForMobile();
    }
    
    $('.logo').click(function() {
	   sessionStorage.removeItem('active-menu');
	});

	if ($('body').hasClass('template-pages-addon--luxurystoreaddon-pages-layout-luxuryGenderLandingPage')) {
	   sessionStorage.removeItem('active-menu');
	}

    function executeForDesktop() {
        //by default show first sub menu
        $('.tabs > .tab-link:first-child').addClass('current');
        //$('#main-nav > .mega-menu:first-child').addClass('current');

        // fade in effect
        $('.tabs .tab-link').mouseover(function() {
            var second_level_menu = $(this).data('tab');
            $('#main-nav > .mega-menu > li > a').css('display', 'none');
            $('#main-nav > #' + second_level_menu + '> li > a').fadeIn(600);
        }).mouseout(function() {
            //show first sub menu tab
            $('.tabs > .tab-link:first-child').addClass('current');
            $('#main-nav > .mega-menu:first-child').addClass('current');
            //$('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
        });


        //show first sub menu tab
        $('#main-nav > .mega-menu').mouseout(function() {
            $('.tabs > .tab-link:first-child').addClass('current');
            $('#main-nav > .mega-menu:first-child').addClass('current');
        });

        //for sub sub menu fade in effect
        $('.mega-menu > li').hover(function() {
            $(this).find('.sub-menu .sub-menu-inner').css('display', 'none');
            $(this).find('.sub-menu .sub-menu-inner').fadeIn(600);
        });
        
        $("ul.tabs li,.mega-menu.tab-content").mouseout(function() {
        	$('.tabs > .tab-link:first-child').addClass('current');
        	$('#main-nav > .mega-menu:first-child').addClass('current');
        	$('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
        });
        
        //for search on hover
//        $('#header-search-menu').mouseenter(function(e) {
//            $('#header-search').addClass('active');
//        });
//
//        $('.header-search').mouseleave(function(e) {
//            $('#header-search').removeClass('active');
//        });
        
        //outside search box click hide it
        $('body').click(function(e) {
            var container = $("#header-search");
            if (!container.is(e.target) && container.has(e.target).length === 0) 
            {
                container.removeClass('active');
            }
        });
        
        //set active menu link
        $('ul.tabs li').click(function(e) {
            var to_url = $(this).children().attr('href');
            var set_active_link = $(this).data('tab');
            
        	sessionStorage.setItem('active-menu', set_active_link);
        	if(to_url != '#'){
        		window.location.href = to_url;
        	}
        	
        });
        
        $('.mega-menu a').click(function(e) {
            e.preventDefault();
            var active_id = $(this).parents('.mega-menu').attr('id');
            var go_to_url = $(this).attr('href');
            
            sessionStorage.setItem('active-menu', active_id);
            if(go_to_url != '#'){
            	window.location.href = go_to_url;
            }
        });

    	var current_menu = sessionStorage.getItem('active-menu');
        if (current_menu) {
    		$('.tabs [data-tab='+current_menu+']').siblings().removeClass('current');
    		$('.tabs [data-tab='+current_menu+']').addClass('current');
    		$('#main-nav > #' + current_menu).siblings().removeClass('current');
            $('#main-nav > #' + current_menu).addClass('current');
        }
       
    }
    
    function executeForMobile(){
    	$('.tabs .tab-link a').click(function(e) {
    	    e.preventDefault();
    	    $(this).parent().siblings().removeClass('current');
    	    var second_level_menu = $(this).parent().data('tab');
    	    $(this).parent().toggleClass('current');
    	    $('#main-nav > #' + second_level_menu).siblings().removeClass('current');
    	    $('#main-nav > #' + second_level_menu).toggleClass('current');
    	});
    	
    	var current_menu = sessionStorage.getItem('active-menu');
    	
    	$('.tabs .tab-link').click(function(e) {
    	    e.preventDefault();
    	    $(this).siblings().removeClass('current');
    	    var second_level_menu = $(this).data('tab');
    	    if(!current_menu){
    	    	$(this).toggleClass('current');
    	    }
    	    $('#main-nav > #' + second_level_menu).siblings().removeClass('current');
    	    $('#main-nav > #' + second_level_menu).toggleClass('current');
    	});
    	
    	$('ul.tabs li a').click(function(e) {
		   e.preventDefault();
		   var to_url = $(this).attr('href');
		   var set_active_link = $(this).parent().data('tab');

		   sessionStorage.setItem('active-menu', set_active_link);
		   if (to_url != '#') {
		       window.location.href = to_url;
		   }

		});

		$('.mega-menu a').click(function(e) {
		   e.preventDefault();
		   var active_id = $(this).parents('.mega-menu').attr('id');
		   var go_to_url = $(this).attr('href');

		   sessionStorage.setItem('active-menu', active_id);
		   if (go_to_url != '#') {
		       window.location.href = go_to_url;
		   }
		});

		
		if (current_menu) {
		   $('.tabs [data-tab=' + current_menu + ']').siblings().removeClass('current');
		   $('.tabs [data-tab=' + current_menu + ']').addClass('current');
		   $('#main-nav > #' + current_menu).siblings().removeClass('current');
		   $('#main-nav > #' + current_menu).addClass('current');
		}
    }
    
});
$(document).ready(function() {

    var window_width = $(window).width();
    if (window_width > 768) {
        executeForDesktop();
        checkoutDisabledBtnClickDesktop();
    }else{
    	executeForMobile();
        checkoutDisabledBtnClickMobile();
    }
    

    function executeForDesktop() {
        
        $('.logo').click(function() {
           sessionStorage.setItem('active-menu', 'LuxuryNavBarComponent');
        });

        var page_type = $('#pageType').val();
        if ( page_type == "homepage") {
           sessionStorage.setItem('active-menu', 'LuxuryNavBarComponent');
        }

        //by default show first sub menu
        //$('.tabs > .tab-link:first-child').addClass('current');
        //$('#main-nav > .mega-menu:first-child').addClass('current');

        // fade in effect
        $('.tabs .tab-link').mouseover(function() {
            var second_level_menu = $(this).data('tab');
            $('#main-nav > .mega-menu > li > a').css('display', 'none');
            $('#main-nav > #' + second_level_menu + '> li > a').fadeIn(600);
        })

        /*.mouseout(function() {
            //show first sub menu tab
             $('.tabs > .tab-link:first-child').addClass('current');
             $('#main-nav > .mega-menu:first-child').addClass('current');
            //$('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
        });*/


        //show first sub menu tab
        // $('#main-nav > .mega-menu').mouseout(function() {
        //     $('.tabs > .tab-link:first-child').addClass('current');
        //     $('#main-nav > .mega-menu:first-child').addClass('current');
        // });

        //for sub sub menu fade in effect
        $('.mega-menu > li').hover(function() {
            $(this).find('.sub-menu .sub-menu-inner').css('display', 'none');
            $(this).find('.sub-menu .sub-menu-inner').fadeIn(600);
        });
        
        // $("ul.tabs li,.mega-menu.tab-content").mouseout(function() {
        // 	$('.tabs > .tab-link:first-child').addClass('current');
        // 	$('#main-nav > .mega-menu:first-child').addClass('current');
        // 	$('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
        // });
        
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

        $('#main-nav').mouseleave(function(e){ 
            if (current_menu) {
                $('.tabs [data-tab='+current_menu+']').siblings().removeClass('current');
                $('.tabs [data-tab='+current_menu+']').addClass('current');
                $('#main-nav > #' + current_menu).siblings().removeClass('current');
                $('#main-nav > #' + current_menu).addClass('current');
                $('#main-nav > #' + current_menu + '> li > a').fadeIn(600);
            }else{
                $('.tabs .tab-link').removeClass('current');
                $('#main-nav .mega-menu').removeClass('current');
                $('.tabs > .tab-link:first-child').addClass('current');
                $('#main-nav > .mega-menu:first-child').addClass('current');
                $('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
            } 
        });

        //show hide overlay on sub menu hover
        $("#main-nav > .mega-menu > li ").mouseenter(function() {
            $("#overlay").show();
        }).mouseleave(function() {
            $("#overlay").hide();
        });
       
    }
    
    function executeForMobile(){
        $('.logo').click(function() {
           sessionStorage.setItem('active-menu', 'LuxuryNavBarComponent');
        });

        var page_type = $('#pageType').val();
        if ( page_type == "homepage") {
           sessionStorage.setItem('active-menu', 'LuxuryNavBarComponent');
        }

    	$('ul.tabs li > a').attr('href','#');
    	$('.tabs .tab-link a').click(function(e) {
    	    e.preventDefault();
    	    $(this).parent().siblings().removeClass('current');
    	    var second_level_menu = $(this).parent().data('tab');
    	    $(this).parent().addClass('current');
    	    $('#main-nav > #' + second_level_menu).siblings().removeClass('current');
    	    $('#main-nav > #' + second_level_menu).addClass('current');
    	});
    	
    	var current_menu = sessionStorage.getItem('active-menu');
        //console.log(current_menu);
    	
    	/*$('.tabs .tab-link').click(function(e) {
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

		});*/


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

    //set active menu on search
    //on search within category - should active with searched category

    function searchcategory_mapping_with_menus(search_category) {
        switch (search_category) {
            case 'LSH1':
                var tobe_active_menu = 'LuxuryNavBarComponent';
                return tobe_active_menu;
                break;
            case 'ISH1':
                var tobe_active_menu = 'INDILUXNavBarComponent';
                return tobe_active_menu;
                break;
            case 'LBSH1':
                var tobe_active_menu = 'LUXBOXNavBarComponent';
                return tobe_active_menu;
                break;
            default:
                var tobe_active_menu = 'LuxuryNavBarComponent';
                return tobe_active_menu;
        }
    }

    //to execute on search page only 
    var page_type = $('#pageType').val();
    if (page_type == "productsearch") {

        var search_page_url = window.location.href;
        var search_category = search_page_url.split('searchCategory=').pop();

        var set_active_menu = searchcategory_mapping_with_menus(search_category);
        sessionStorage.setItem('active-menu', set_active_menu);

        $('.tabs .tab-link').removeClass('current');
        $('.tabs [data-tab=' + set_active_menu + ']').addClass('current');

        $('#main-nav > ul').removeClass('current');
        $('#main-nav > #' + set_active_menu).addClass('current');
    }

    //show enter pincode msg on checkout page
    function checkoutDisabledBtnClickDesktop() {
        console.log('desk');
        $('body.page-cartPage #checkout-id a').mouseenter(function(){
            var default_pincode = $("#defaultPinCodeIds").val();
            if(!default_pincode){
                $('.tooltiptext').css("visibility","visible");
            }
        }).mouseleave(function(){
            $('.tooltiptext').css("visibility","hidden");
        });
    }

    //show enter pincode msg on checkout page
    function checkoutDisabledBtnClickMobile() {
        $("body.page-cartPage #checkout-id a").on("click",function(){
            var default_pincode = $("#defaultPinCodeIds").val();
            if(!default_pincode){
                $('.tooltiptext').css("visibility","visible");
                setTimeout(function(){$('.tooltiptext').css("visibility","hidden");},4000);
            }
        });
    }

    
});
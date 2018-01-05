$(document).ready(function() {

    var window_width = $(window).width();
    if (window_width > 768) {
        executeForDesktop();
    }

    function executeForDesktop() {
        //by default show first sub menu
        $('.tabs > .tab-link:first-child').addClass('current');
        $('#main-nav > .mega-menu:first-child').addClass('current');

        // fade in effect
        $('.tabs .tab-link').mouseover(function() {
            var second_level_menu = $(this).data('tab');
            $('#main-nav > .mega-menu > li > a').css('display', 'none');
            $('#main-nav > #' + second_level_menu + '> li > a').fadeIn(600);
        }).mouseout(function() {
            //show first sub menu tab
            $('.tabs > .tab-link:first-child').addClass('current');
            $('#main-nav > .mega-menu:first-child').addClass('current');
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
        
        $("ul.tabs li, .mega-menu.tab-content").mouseout(function() {
        	$('.tabs > .tab-link:first-child').addClass('current');
        	$('#main-nav > .mega-menu:first-child').addClass('current');
        	$('#main-nav > .mega-menu:first-child > li > a').fadeIn(600);
        });
        
    }
    
});
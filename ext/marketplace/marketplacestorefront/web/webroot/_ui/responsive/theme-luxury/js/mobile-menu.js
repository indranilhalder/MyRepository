if ($(window).width() < 768) {
    $(".mega-menu > li > a").attr('href', '#');

    $(".mega-menu li a").css('pointer-events', 'auto');

    $(".mega-menu > li > a").on("click", function(event) {

        event.preventDefault();
        $(this).parent().toggleClass('parent');

        $(this).parents('li').siblings().find(".sub-menu").removeClass("active");
        $(this).parents('li').siblings().find(".sub-menu").find(".sub-menu-inner").removeClass("open-inner-menu");
        $(this).parents('li').siblings().find(".sub-menu-toggle").removeClass("active");

        $(this).siblings(".sub-menu").toggleClass("active");
        $(this).siblings(".sub-menu").find(".sub-menu-inner").toggleClass("open-inner-menu");
        $(this).siblings(".sub-menu-toggle").toggleClass("active");
    });

    $(".sub-menu-inner li a").on("click", function(event) {
    	//event.preventDefault();
        $(this).parents('.col-md-3').siblings().find(".sub-menu").removeClass("active");
        $(this).parents('.col-md-3').siblings().find(".sub-menu-toggle").removeClass("active");
        
        $(this).siblings(".sub-menu").toggleClass("active");
        $(this).siblings(".sub-menu-toggle").toggleClass("active");
        $("a", this).css("pointer-events", "auto"), $(this).closest(".parent").children(":first").css("pointer-events", "auto");
    });
}
ACC.brandcomponent = {

	_autoload : [ "bindBrandComponent","showOrHideBrands" ],

	bindBrandComponent : function() {
		$(window).on("load resize", function() {
			if ($(window).width() > 767) {
				
				switches = $('#switches .masterBrand');
				// slides = $('#navigation .block');
				switches.each(function(idx) {
					// $(this).data('slide', slides.eq(idx));
					switches.eq(idx).hover(function() {
						switches.removeClass('active');
						// slides.removeClass('active');
						/*Display the flyout on hover*/
						$('#navigation .block').eq(idx).addClass('active');
						// $(this).addClass('active');
						// $(this).addClass('active');
						
						var default_tab =$(".range.current").attr('id');
						$('[data-tab='+default_tab+']').css({
							"border-bottom":"3px solid",
							"font-weight":"bold"
						});
						
					}, function() {
						$('#navigation .block').removeClass('active');
					});
				});

				$(".block").hover(function(e) {
					var target = $(e.target);
					//console.log($(e.target).parent());
					$(this).addClass("active");
				}, function() {
					$(this).removeClass("active");
					$(".range").hide();
					$(".range.current").show();
					
					$(".brandGroupLink").css({
						"border-bottom":"none",
						"font-weight":"400"
					});
					
				});
			}
		});
	},

showOrHideBrands:function(){
	$("#atozbrandsdiplay").on("mouseover touchend", '.brandGroupLink', function(){
		$(".range").hide();
		var id =$(this).attr('data-tab');
		$('#'+id).show();
		
		if($('[data-tab='+id+']').is(":visible"))
			{
			$(".brandGroupLink").css({
				"border-bottom":"none",
				"font-weight":"400"
			});
				$('[data-tab='+id+']').css({
					"border-bottom":"3px solid",
					"font-weight":"bold"
				});
			}
	});
	
}
};

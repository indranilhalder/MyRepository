ACC.cart = {

	_autoload: [
		"bindClick"
	],

	bindClick: function(){
		
	}
};

$(function() {
	$(".node").on("change",function(e){
		var nodeParent=$(this).is(":selected").val();
		
		$.ajax({
			url: ACC.config.encodedContextPath + "/crmChildrenNodes",
			data: {"nodeParent": nodeParent },
			type: 'GET',
			success: function (data)
			{
				if (data != '') {
				    $("ul#giftYourselfProducts").html(data);
				}
			},
			 error : function(resp) { 

			 } 
		});
	}
});



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
		var nodeType=$(this).attr("name");
		var htmlOption="";
		$.ajax({
			url: ACC.config.encodedContextPath + "/crmChildrenNodes",
			data: {"nodeParent": nodeParent },
			type: 'GET',
			success: function (data)
			{
				if (nodeType.startsWith("nodeL1")) {
					$.each(data.nodes.nodes, function (index, data) {
				        htmlOption+="<option value='"+data.nodeCode+"'>"+data.nodeDesc + "</option>";
				    });
					
				    $("nodeL2").html(htmlOption);
				}
			},
			 error : function(resp) { 
				 console.log("Error in crmChildNodes"+resp);	
			 } 
		});
	});
});



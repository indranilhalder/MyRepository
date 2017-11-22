var product = "";
var MSD = {
		
		renderFlatWidgetMSD: function() {
	    	if($('#for_homefurnishing').val() == "true"){
	    	 var productCode = $('#pdp_product_code').val();
	    	 console.log(productCode);
	    	 msdService.call(productCode);
	    	}
	    },
		
}

var msdService = {
		
		call: function(productCode){
			$.ajax({
				url: '/getMSDWidgets',
	            dataType: "json",
                type: 'GET',
                crossDomain: true,
                data:{
                	"productCode": productCode
                }, 
                success: function(data){
                	console.log(data);
                	//
                	
                }
				
				
			});
		}
}

$(document).ready(function(){
	MSD.renderFlatWidgetMSD()
});




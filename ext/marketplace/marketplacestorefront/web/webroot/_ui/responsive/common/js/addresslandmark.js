
$(".address_postcode").blur(function()
		{
			//alert("Hi ! LandMarks Populate");
			var Pincode=$(".address_postcode").val();
	        //alert("Pincode is:  "+Pincode);
			console.log("Ajax Call Started");
	        $.ajax({
	    		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks",
	    		data: { 'pincode' : Pincode },
	    		type: "POST",	
	    		success : function(response) {
	    			console.log("Response : "+response);
	    			console.log(response.cityName);
	    			alert("Hi !");
	        		$(".address_townCity").val(response['cityName']);
	        		$('.address_landmarks').empty();
	        		 $('.address_landmarks').append($("<option></option>").attr("disabled","disabled").attr("selected","selected").text("select a land mark"));
	        		//then fill it with data from json post
	        		  $.each(response.landMarks, function(key, value) {
	        		       $('.address_landmarks').append($("<option></option>").attr("value",value.landmarksCode)
	        		         .text(value.landmark));
	        		    });
	        		  
	        		  $(".address_states").val(response.state.name)
					/*$("#state").val(response['state']);*/
	    		},
	    		error: function(jqXHR, textStatus, errorThrown) {
	    			  console.log(textStatus, errorThrown);
    			}
		});
});

        
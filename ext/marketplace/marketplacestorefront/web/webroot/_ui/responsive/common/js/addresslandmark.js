var addressLandMark = "";
$(".address_landmarkOtherDiv").hide();
$(".address_postcode").blur(function()
		{
			
			var Pincode=$(".address_postcode").val();
			//console.log("Ajax Call Started");
	        $.ajax({
	    		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks",
	    		data: { 'pincode' : Pincode },
	    		type: "POST",	
	    		success : function(response) {
	    			//console.log("Response : "+response);
	    			//console.log(response.cityName);
	    			$(".optionsLandmark, .optionsLandmark label, .optionsLandmark input,  .optionsLandmark select").show();
	    			$(".address_landmarkOtherDiv").hide();
	    			$(".address_landmarkOther").attr("value", "");
	    			$(".address_landmarkOther").val("");
	    			$('.otherOption').attr("value", "Other");
	        		$(".address_townCity").val(response['cityName']);
	        		$('.address_landmarks').empty();
	        		 $('.address_landmarks').append($("<option></option>").attr("disabled","disabled").attr("selected","selected").text("Select a Landmark"));
	        		//then fill it with data from json post
	        		  $.each(response.landMarks, function(key, value) {
	        		       $('.address_landmarks').append($("<option></option>").attr("value",value.landmark)
	        		         .text(value.landmark));
	        		    });
	        		  $('.address_landmarks').append($("<option class='otherOption'></option>").attr("value","Other").text("Other"));
	        		  
	        		  $(".address_states").val(response.state.name)
					/*$("#state").val(response['state']);*/
	    		},
	    		error: function(jqXHR, textStatus, errorThrown) {
	    			  console.log(textStatus, errorThrown);
    			}
		});
});

//onchange="changeFunction(this.value)"
	
$('.address_landmarks').attr("onchange","changeFunction(this.value)");
$(".address_landmarkOther, .address_landmarkOtherDiv label").hide();

function changeFunction(value) {
	//alert(value);
	if(value == "Other") {
		$(".address_landmarks").hide();
		$('.otherOption').attr("value", "");
		$('.otherOption').val("");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
		$(".optionsLandmark label").hide();
	}
}
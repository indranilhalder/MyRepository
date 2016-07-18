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


$(document).ready(
		function() {
			$("#deliveryAddressForm").submit(
					function(event) {
						if ($("#firstName").val().length < 1) {
							$(".firstNameError").show();
							$(".firstNameError").text("First Name cannot be Blank");
						} else if ($("#lastName").val().length < 1) {
							$(".lastNameError").show();
							$(".lastNameError").text("Last Name cannot be Blank ");
						} else if ($("#addressLine1").val().length < 1) {
							$(".address1Error").show();
							$(".address1Error").text("Address Line 1 cannot be blank");
						} else if ($("#addressLine2").val().length < 1) {
							$(".address2Error").show();
							$(".address2Error").text("Address Line 2 cannot be blank");
						} else if ($("#addressLine3").val().length < 1) {
							$(".address3Error").show();
							$(".address3Error").text("Address Line 3 cannot be blank");
						}else{
						
						var data = $("#deliveryAddressForm").serialize();
						var orderCode = $('#deliveryAddorderCode').val();
						$.ajax({
							url : ACC.config.encodedContextPath
									+ "/my-account/" + orderCode
									+ "/changeDeliveryAddress/",
							type : 'GET',
							data : data,
							  contentType: "application/json",
							  dataType: 'json',
							success : function(result) {
								if(result="sucess"){
									$("#changeAddressPopup").hide();
									$("wrapBG1").hide();
									$("#showOTP").show();
									$(".wrapBG").show();
									var height = $(window).height();
									$(".wrapBG1").css("height", height);
									$("#showOTP").css("z-index", "999999");
								}else{
									 $("#changeAddressPopup .error_text").text("Please Re-enter Address.");
								}

							},
							error : function(result) {
								alert("error")
							}

						});
						}
						event.preventDefault();
					});
		});


$(document).ready(function() {
	$("#geneateOTP").click(function() {
		$("#changeAddressPopup").hide();
		$("wrapBG1").hide();
		$("#showOTP").show();
		$(".wrapBG").show();
		var height = $(window).height();
		$(".wrapBG1").css("height", height);
		$("#showOTP").css("z-index", "999999");
	});
	$(".close").click(function() {
		$("#showOTP").hide();
		$(".wrapBG1").hide();
	});
});


var addressLandMark = "";
$(".address_landmarkOtherDiv").hide();
$(".address_postcode").blur(function() {
	loadPincodeData();			
});

function loadPincodeData() {
	var Pincode=$(".address_postcode").val();
	//console.log("Ajax Call Started");
    $.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks",
		data: { 'pincode' : Pincode },
		type: "POST",	
		success : function(response) {
			console.log("Response : "+response);
			if(response == "" || response == " " || response == "NULL") {
				console.log("Its Here");
				$(".address_landmarks").hide();
				$('.otherOption').attr("value", "");
				$('.otherOption').val("");
				$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
				$(".optionsLandmark label").hide();
			} else {
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
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			  console.log(textStatus, errorThrown);
		}
    });
}

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


$(document).ready(function() {
	onloadFunction();
			$("#deliveryAddressForm").submit(
					function(event) {
				      var mobile=$("#mobileNo").val();
				      var mobile=mobile.trim();
				      var isString = isNaN(mobile);
						$(".main_error").hide();
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
						}else if(isString==true || mobile.trim()==''){
							    $(".mobileNumberError").show();
					            $(".mobileNumberError").text("Enter only Numbers");
						}else if(mobile.length<=9 || mobile.length >= 11) {   
					    	  $(".mobileNumberError").show();
					          $(".mobileNumberError").text("Enter 10 Digit Number");
					      }	
						else{
						
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
								if(result=="success"){
									$("#changeAddressPopup").hide();
									$("wrapBG1").hide();
									$("#showOTP").show();
									$(".wrapBG").show();
									var height = $(window).height();
									$(".wrapBG1").css("height", height);
									$("#showOTP").css("z-index", "999999");
								}else{
									$(".main_error").show();
									 $("#changeAddressPopup .main_error").text("Please Re-Check the data, there is some error.");
								}

							},
							error : function(result) {
								alert("error")
							}

						});
						}
						event.preventDefault();
					});


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
	
	$(".addAddressToForm").click(function(){
		//console.log($(this).attr("data-item"));
		var className = $(this).attr("data-item");
	
		$("#firstName").val($("."+className+" .firstName").text());
		$("#lastName").val($("."+className+" .lastName").text());
		$("#addressLine1").val($("."+className+" .addressLine1").text());
		$("#addressLine2").val($("."+className+" .addressLine2").text());
		$("#addressLine3").val($("."+className+" .addressLine3").text());
		$("#landmark").val($("."+className+" .landmark").text());
		$("#state").val($("."+className+" .state").text());
		$("#pincode").val($("."+className+" .postalCode").text());
		$("#mobileNo").val($("."+className+" .phone").text());
	});
	
	
	function onloadFunction() {
		//$("#deliveryAddressForm #firstName").attr("value", "Dileep");
		//$("#deliveryAddressForm #firstName").val();
		
	}
		
});
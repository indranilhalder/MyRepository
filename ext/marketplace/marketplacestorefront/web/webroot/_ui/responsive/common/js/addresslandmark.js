var addressLandMark = "";

$(document).ready(function(){
	if($(".address_postcode").val().length >= "3") {
	//	alert("3");
		loadPincodeData();
	}
	
	$(".address_states").change(function(){
		if($(this).attr("readonly") == "readonly"){
			$(".address_states").val($(this).data("stateValue"))
		}
	});
});

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
				$(".address_landmarks").attr("disabled","disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
				$('.otherOption').attr("value", "");
				$('.otherOption').val("");
				$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
				$('.address_landmarks').append($("<option class=unableto></option>").text("Unable to find landmark").attr("selected","selected").attr("value",""));
				$(".address_townCity").prop("readonly", false);
				$(".address_states").removeAttr("readonly").removeData("stateValue");
				
				//$(".optionsLandmark label").hide();
			} else {
    			//console.log(response.cityName);
				$('.address_landmarks .unableto').remove();
				$(".address_landmarks").removeAttr("disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
    			$(".optionsLandmark, .optionsLandmark label, .optionsLandmark input,  .optionsLandmark select").show();
    			$(".address_landmarkOtherDiv").hide();
    			$(".address_landmarkOther").attr("value", "");
    			$(".address_landmarkOther").val("");
    			$('.otherOption').attr("value", "Other");
        		$(".address_townCity").val(response['cityName']).prop("readonly", true);
        		$('.address_landmarks').empty();
        		 $('.address_landmarks').append($("<option></option>").attr("selected","selected").text("Select a Landmark").attr("value", "sel"));
        		//then fill it with data from json post
        		  $.each(response.landMarks, function(key, value) {
        		       $('.address_landmarks').append($("<option></option>").attr("value",value.landmark)
        		         .text(value.landmark));
        		    });
        		  $('.address_landmarks').append($("<option class='otherOption'></option>").attr("value","Other").text("Other"));
        		  
        		  $(".address_states").val(response.state.name).attr("readonly", "true").data("stateValue",response.state.name);
				/*$("#state").val(response['state']);*/
			}
			
			var url = window.location.href;
			var string = "edit-address";
			if(url.indexOf(string) >= "0") {
				//alert(url);
				var value = $(".address_landmarkOtherDiv").attr("data-value");
				if($(".address_landmarks option[value='"+value+"']").length > "0") {
					console.log(value); 
					$(".address_landmarks").val(value);
				} else {
					if($(".address_landmarks option[value='Other']").length > "0") {
						$(".address_landmarks").val("Other"); 
		  			changeFunction("Other"); 
		  			$(".address_landmarkOther").val(value);
					} else {
						console.log(value);
						$(".address_landmarkOther").val(value);
					}
				}
			}
			
		},
		error: function(jqXHR, textStatus, errorThrown) {
			  console.log(textStatus, errorThrown);
		}
    });
}

$(".errland1, .errland2").hide();

$(".optionsLandmark .errorMessage").css("padding-bottom", "5px");
	
$('.address_landmarks').attr("onchange","changeFunction(this.value)");
$(".address_landmarkOther, .address_landmarkOtherDiv label").hide();

$("#addNewAddress").click(function(e){
	optionsLandmark(e);
});

function changeFunction(value) {
	$('.otherOption').attr("value", "Other");
	$(".errland1").hide();
	if(value == "Other") {
		$('.otherOption').attr("value", "");
		$('.otherOption').val("");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
	} else {
		$(".address_landmarkOther").attr("value", "");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").hide();
	}
}

function optionsLandmark(e) {
	$(".errland1").hide();
	if($(".address_landmarks").val().length <= "3" && $(".address_landmarks").val().length >= "1") {
		$(".errland1").show().css("color", "#ff1c47").text("Please Select Landmark");
		e.preventDefault();
	} else if($(".address_landmarks").val() == "") {
		optionsLandmark1(e);
	}
}

function optionsLandmark1(e) {
	$(".errland2").hide();
	if($(".address_landmarkOther").val().length <= "1") {
		$(".errland2").show().css("color", "#ff1c47").text("Please Enter Landmark");
		e.preventDefault();
	}	
}


$(document).ready(function() {
	onloadFunction();
			$("#deliveryAddressForm").submit(
					function(event) {
						$(".main_error").hide();
						$(".firstNameError").hide();
						$(".lastNameError").hide();
						$(".address1Error").hide();
						$(".address2Error").hide();
						$(".address3Error").hide();
						$(".mobileNumberError").hide();
						$(".cityError").hide();
						$(".pincodeNoError").hide();
				      var mobile=$("#mobileNo").val();
				      var mobile=mobile.trim();
				      var isString = isNaN(mobile);
				      var pincode=$("#pincode").val();
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
					      }	else if ($("#city").val().length < 1){
					    	  $(".cityError").show();
					          $(".cityError").text("City cannot be blank");
					      }else if(pincode.length < 1 && pincode.length > 6){
					    	  $(".pincodeNoError").show();
					          $(".pincodeNoError").text("Enter correct pincode");
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
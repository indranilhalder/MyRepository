var addressLandMark = "";

$(document).ready(function(){
	if($(".address_postcode").val().length >= "3") {
		loadPincodeData();
	}
	
	$(".address_states").change(function(){
		if($(this).attr("readonly") == "readonly"){
			$(".address_states").val($(this).data("stateValue"))
		}
	});
});

$(".address_landmarkOtherDiv").hide();
$(".dupDisplay").hide();
$(".address_postcode").blur(function() {
	loadPincodeData();			
});

function loadPincodeData() {
	var Pincode=$(".address_postcode").val();
    $.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks",
		data: { 'pincode' : Pincode },
		type: "GET",	
		success : function(response) {
			if(response == "" || response == " " || response == "NULL") {
				$(".address_landmarks").attr("disabled","disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
				$("#stateListBox").prop("disabled",false);
				$("#stateListBoxReadOnly").prop("disabled","disabled");
				$(".addressRead").prop("disabled",false);
				$(".addressDup").prop("disabled","disabled");
				$(".mainDrop").show();
				$(".dupDisplay").hide();
				$('.otherOption').attr("value", "");
				$('.otherOption').val("");
				$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
				$(".mainDrop").show();
				$(".dupDisplay").hide();
				$(".addState").show();
				$('.address_landmarks').append($("<option class=unableto></option>").text("Unable to find landmark").attr("selected","selected").attr("value",""));
				$(".address_townCity").prop("readonly", false);
				$(".address_states").removeAttr("readonly").removeData("stateValue");
				
			} else {
				$('.address_landmarks .unableto').remove();
				$(".address_landmarks").removeAttr("disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
    			$(".optionsLandmark, .optionsLandmark label, .optionsLandmark input,  .optionsLandmark select").show();
    			$(".address_landmarkOtherDiv").hide();
    			$("#stateListBox").prop("disabled","disabled");
    			$("#stateListBoxReadOnly").prop("disabled",false);
    			$(".addressRead").prop("disabled","disabled");
    			$(".addressDup").prop("disabled",false);
    			$(".mainDrop").hide();
				$(".dupDisplay").show();
    			$(".address_landmarkOther").attr("value", "");
    			$(".address_landmarkOther").val("");
    			$('.otherOption').attr("value", "Other");
        		$(".address_townCity").val(response['cityName']).prop("readonly", true);
        		$('.address_landmarks').empty();
        		 $('.address_landmarks').append($("<option></option>").attr("selected","selected").text("Select a Landmark").attr("value", "NA"));
        		//then fill it with data from json post
        		  $.each(response.landMarks, function(key, value) {
        		       $('.address_landmarks').append($("<option></option>").attr("value",value.landmark)
        		         .text(value.landmark));
        		    });
        		  $('.address_landmarks').append($("<option class='otherOption'></option>").attr("value","Other").text("Other"));
        		  
        		  $(".address_states").val(response.state.name).attr("readonly", "true").data("stateValue",response.state.name);
			}
			
			var url = window.location.href;
			var string = "edit-address";
			var shippingAddress = "my-account";
			if(url.indexOf(string) >= "0") {
				var value = $(".address_landmarkOtherDiv").attr("data-value");
				if($(".address_landmarks option[value='"+value+"']").length > "0") {
					$(".address_landmarks").val(value);
				} else {
					if($(".address_landmarks option[value='Other']").length > "0") {
						$(".address_landmarks").val("Other"); 
		  			changeFunction("Other"); 
		  			$(".address_landmarkOther").val(value);
					} else {
						$(".address_landmarkOther").val(value);
					}
				}
			}
			
		},
		error: function(jqXHR, textStatus, errorThrown) {
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

function checkPopupDataOrderHistory() {
					var validate = true;
					$(".main_error").hide();
					$(".firstNameError").hide();
					$(".lastNameError").hide();
					$(".address1Error").hide();
					$(".address2Error").hide();
					$(".address3Error").hide();
					$(".mobileNumberError").hide();
					$(".landMarkError").hide();
					$(".cityError").hide();
					$(".pincodeNoError").hide();
					$(".stateError").hide();
					$(".otherLandMarkError").hide();
				      
				      var fname=$("#firstName").val();
				      var lname=$("#lastName").val();
				      var al1=$("#addressLine1").val();
				      var al2=$("#addressLine2").val();
				      var al3=$("#addressLine3").val();
				      var state=$("#state").val();
				      var mobile=$("#mobileNo").val();
				      var pincode=$("#pincode").val();
				      var isString = isNaN(mobile);
				      var hasString = isNaN(pincode);
				      var city=$("#city").val();
				      var letters = new RegExp(/^[A-z]*$/);
				      var otherLandMark=$(".otherLandMark").val();
				      
				      if(otherLandMark != null){
				    	  if(otherLandMark.trim() == ''){
				    	    $(".otherLandMarkError").show();
					  		$(".otherLandMarkError").text("Other LandMark cannot be allow  space");
					  		validate = false;
				    	  }else if(/[^a-zA-Z0-9]/.test(otherLandMark)){
				    		  $(".otherLandMarkError").show();
						  	  $(".otherLandMarkError").text("Other LandMark cannot be allow special characters");
						  	 validate = false;
				    	  }
				      }
				      
				     if(fname == null || fname.trim() == '' ){
				  			$(".firstNameError").show();
				  			$(".firstNameError").text("First Name cannot be Blank");
				  			validate = false;
				  	}else if(letters.test(fname) == false){
				  		$(".firstNameError").show();
			  			$(".firstNameError").text("First Name should contain only alphabets");
			  			validate = false;
				  	}
				     if(lname == null || lname.trim() == '' ){
			  			$(".lastNameError").show();
			  			$(".lastNameError").text("Last Name cannot be Blank");
			  			validate = false;
				  	}else if(letters.test(lname) == false){
				  		$(".lastNameError").show();
			  			$(".lastNameError").text("Last Name should contain only alphabets");
			  			validate = false;
				  	}   
				     if(al1 == null || al1.trim() == '' ){
			  			$(".address1Error").show();
			  			$(".address1Error").text("Address Line 1 cannot be blank");
			  			validate = false;
				  	} if(al2 == null || al2.trim() == '' ){
			  			$(".address2Error").show();
			  			$(".address2Error").text("Address Line 2 cannot be blank");
			  			validate = false;
				  	} if(al3 == null || al3.trim() == '' ){
			  			$(".address3Error").show();
			  			$(".address3Error").text("Address Line 3 cannot be blank");
			  			validate = false;
				  	} if(state == null || state=="Select" ){
			  			$(".stateError").show();
			  			$(".stateError").text("State cannot be Blank");
			  			validate = false;
				  	}
				  	if(isString==true || mobile.trim()==''){
			  			$(".mobileNumberError").show();
			  			$(".mobileNumberError").text("Enter only Numbers");
			  			validate = false;
				  	} 
				  	if(hasString==true || pincode.trim()==''){
			  			$(".pincodeNoError").show();
			  			$(".pincodeNoError").text("Enter only Numbers");
			  			validate = false;
				  	}
				  	if(mobile.length < 9 || mobile.length > 11){
				    	  $(".mobileNumberError").show();
				          $(".mobileNumberError").text("Enter correct mobile number");
				          validate = false;
				  	} if(pincode.length < 6 ){
				    	  $(".pincodeNoError").show();
				          $(".pincodeNoError").text("Enter correct pincode");
				          validate = false;
				      }if(city == null || city.trim() == '' ){
			  			$(".cityError").show();
			  			$(".cityError").text("City cannot be blank");
			  			validate = false;
				  	}else if(letters.test(city) == false){
				  		$(".cityError").show();
			  			$(".cityError").text("City should contain only alphabets");
			  			validate = false;
				  	}
				      if(validate == true){
						var data = $("#deliveryAddressForm").serialize();
						var orderCode = $('#deliveryAddorderCode').val();
						$.ajax({
							url : ACC.config.encodedContextPath
									+ "/my-account/" + orderCode
									+ "/changeDeliveryAddress/",
							type : 'GET',
							data : data,
							contentType: "text/application/html",
							success : function(result){
								if(result=='Pincode not Serviceable'){
									$("#changeAddressPopup").show();
									$("#showOTP").hide();
									$(".wrapBG").show();
									var height = $(window).height();
									$(".wrapBG").css("height", height);
									$("#changeAddressPopup").css("z-index", "999999");
									$(".pincodeNoError").show();
									$(".pincodeNoError").text(result);
								}else if(result =='Updated'){
									window.location.href=ACC.config.encodedContextPath+"/my-account/order/?orderCode="+orderCode;
								}else{
									$("#changeAddressPopup").hide();
									$("#otpPopup").html(result).show();
									$(".wrapBG").show();
								}
							},
							error : function(result) {
								alert("error");
							}

						});
					}
						
					}
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
		$("#showOTP,#otpPopup").hide();
		$(".wrapBG1").hide();	
	});
	
	$(".addAddressToForm").click(function(){
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
	
});

function newOTPGenerate(orderCode){
	 $.ajax({
			type : "GET",
			url : ACC.config.encodedContextPath + "/my-account/newOTP",
			data :"orderCode="+orderCode,
			success : function(response) {
				if(response==true){
					$(".otpError").show();
					$(".otpError").text("OTP has been sent");
				}else{
					$(".otpError").show();
					$(".otpError").text("OTP sending fail try again ");
				}
			}
		}); 
} 

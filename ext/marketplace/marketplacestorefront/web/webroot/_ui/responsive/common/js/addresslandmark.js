var addressLandMark = "";

$(document).ready(function(){
	
	if($('div').hasClass('address_postcode')) {
		if($(".address_postcode").val().length >= "3") {
			
			loadPincodeData("edit").done(function() {
			 var value = $(".address_landmarkOtherDiv").attr("data-value");
			 otherLandMarkTri(value,"defult");
			});

			
		}
	}
	
	
	$(".address_states").change(function(){
		if($(this).attr("readonly") == "readonly"){
			$(".address_states").val($(this).data("stateValue"));
		}
	});
	
	$("#addNewAddressPOP").click(function() {
	    $("#deliveryAddressForm #firstName").prop('value','');
	    $("#deliveryAddressForm #lastName").prop('value','');
	    $("#deliveryAddressForm #addressLine1").prop('value','');
	    $("#deliveryAddressForm #addressLine2").prop('value','');
	    $("#deliveryAddressForm #addressLine3").prop('value','');
	    $("#deliveryAddressForm #landmark").prop("disabled",false).empty();
	    $("#deliveryAddressForm #otherLandmark").prop('value','');
	    $(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").hide();
	    $(".address_landmarkOtherDiv").attr('data-value','');
	    $("#deliveryAddressForm #state").prop('value','Select');
	    $(".dupDisplay").hide();
	    $(".mainDrop").show();
	    $(".mainDrop select").prop("disabled",false);
	    $("#deliveryAddressForm #city").prop('value','');
	    $("#deliveryAddressForm #pincode").prop('value','');
	    $("#deliveryAddressForm #mobileNo").prop('value','');
	    $(".error_text").hide();
	   });
});

$(".address_landmarkOtherDiv").hide();
$(".dupDisplay").hide();

var tmpValue= -1;
$(".address_postcode").blur(function() {
	 console.log("addresslandmark line 74 "+tmpValue);
	 tmpValue++;
	 if($(".address_postcode").val().length == "6") {
		
		loadPincodeData("edit").done(function() {
			console.log("blur line 394");
		 var value = $(".address_landmarkOtherDiv").attr("data-value");
		 console.log("blur line 396 "+value);
		 otherLandMarkTri(value,"blur");
		});
	 }else{
			$(".address_landmarks").empty();
			changeFuncLandMark("");
			//$(".address_states,#statesReadOnly,.address_townCity").attr('readonly',false).prop("value","");
			$(".mainDrop").show();
			$(".mainDrop select").prop("disabled",false);
			$(".stateInput input").prop("disabled","disabled");
			$(".dupDisplay").hide();
		}

});

/*function checkOutAdress(arg1, arg2){
	var tmpTribhuvan;
	tmpValue = arg1;
	console.log("addresslandmark 67 "+$(".address_postcode").val());
	if(loadPincodeData(arg2)){
		console.log("68");
		tmpTribhuvan = true;
	}else{
		console.log("addresslandmark 71 "+$(".address_postcode").val());
		tmpTribhuvan = false;
	}
	return tmpTribhuvan;
}*/

function loadPincodeData(parm) {
	
	
	
	//alert(parm);
	console.log("addresslandmark line 145 "+ parm);
	var Pincode=$(".address_postcode").val();
	console.log("addresslandmark line 85 "+ ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks");
   return  $.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/landmarks",
		data: { 'pincode' : Pincode },
		type: "GET",	
		success : function(response) {
			//var arg1 = false;
			console.log(response.cityName+ " response.cityName");
			//	TISPRDT-890
			if(response == "" || response == " " || response == "NULL" || response == null && (response.landMarks == null || response.landMarks == "" || response.cityName == "" || response.cityName == null)) {
				console.log("addresslandmark line 154"+ response+ "##");
				//alert("in if");
			/*	if(response.countryCode == undefined){
			
				if(Pincode.length > 0){
				if($("#pincodeError").length>0){
					$("#pincodeError").css('color','#ff1c47').show().text("Pincode None serviceble, please enter another pincode");
					
				}else if($(".pincodeNoError").length>0){
				$(".pincodeNoError").show().text("Pincode None serviceble, please enter another pincode");
				
				}else if($("#addAddressForm #pincode + .errorText").length>0){
					$("#addAddressForm #pincode + .errorText").show().text("Pincode None serviceble, please enter another pincode");	
				}
				}
				$('.address_landmarks').val("").empty();
				if(window.location.href.indexOf("my-account/address-book") >= 0){
					$("#erraddressPost").css('color','#CCC').show().text("*Note: Pincode None serviceble, yet you can save address for future reference.");
					$('.address_landmarks').html($("<option></option>").text("Unable to find landmark").attr("selected","selected").attr("value",""));
					changeFuncLandMark("Other"); 
					$(".address_townCity").prop("readonly", false).val('');
					$(".address_states").prop("value","");
					$(".dupDisplay").hide();
					$(".mainDrop").show();
					$(".mainDrop select").prop("disabled",false);
				}
				
				$(".saveBlockData,#newAddressButton,#saveAddress").css({'opacity':'0.5'}).prop("disabled","disabled");
				}else{*/
				//arg1 = false;
				
				$(".address_landmarks").attr("disabled","disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
				$("#stateListBox").prop("disabled",false);
				$("#stateListBoxReadOnly").prop("disabled","disabled");
				$(".addressRead").prop("disabled",false);
				$(".addressDup").prop("disabled","disabled");
				$(".mainDrop").show();
				$(".mainDrop select").prop("disabled",false);
				$(".stateInput input").prop("disabled","disabled");
				$(".dupDisplay").hide();
				$('.otherOption').attr("value", "");
				$('.otherOption').val("");
				$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
				$(".addState").show();
				 $('.address_landmarks').html("<option class='otherOption' value='Other'>Other</option>");
				 $(".address_landmarkOther").val("");
				 $(".address_townCity").prop("readonly", false);
				//alert("tmpValue is "+ tmpValue);
				if(parm != "edit" || tmpValue >= 0) {
					
					//$(".address_townCity").val('');
				//	added by sneha R2.3
					//$(".address_states").prop("value","");
				//	end of sneha R2.3
					tmpValue = -1;
			    }
				$(".address_states").removeAttr("readonly").removeData("stateValue");
				//}	
			} else {
				
				if((response.cityName != "" && response.cityName != null)) {
				console.log("addresslandmark line 186 "+ response);
				//$(".saveBlockData,#newAddressButton,#saveAddress").css({'opacity':'1'}).prop("disabled",false);
			//	$(".pincodeNoError,#pincodeError,#erraddressPost,#addAddressForm #pincode + .errorText").hide();
				$('.address_landmarks .unableto').remove();
				$(".address_landmarks").removeAttr("disabled").css("padding-left","5px");
				$(".half .address_landmarkOtherDiv").css("margin-left","10px");
				$(".row .address_landmarkOtherDiv label").css("margin-top","15px");
    			$(".optionsLandmark, .optionsLandmark label, .optionsLandmark input,  .optionsLandmark select").show();
    			$(".address_landmarkOtherDiv").hide();
    			$("#stateListBox").prop("disabled","disabled");
    			//$("#stateListBoxReadOnly").prop("disabled",false);
    			$(".addressRead").prop("disabled","disabled");
    			$(".addressDup").prop("disabled",false);
    			$(".mainDrop").hide();
				$(".dupDisplay").show();
				$(".mainDrop select").val(response.state.name).prop('disabled','disabled');
				$(".stateInput").html("<input id='statesReadOnly' name='state' type='text'/>");
				$(".stateInput input").prop("disabled",false).val(response.state.name).attr("readonly", "true");
    			$(".address_landmarkOther").attr("value", "");
    			$(".address_landmarkOther").val("");
    			$('.otherOption').attr("value", "Other");
        		$(".address_townCity").val(response['cityName']).prop("readonly", true);
if(response.landMarks != null && response.landMarks != ""){
        		$('.address_landmarks').empty();
        		 $('.address_landmarks').html($("<option></option>").attr("selected","selected").text("Select a Landmark").attr("value", "NA"));
        		//then fill it with data from json post
        		
        			//alert("in landmark if");
        			console.log("addresslandmark line 211 ");
        			  $.each(response.landMarks, function(key, value) {
        		       $('.address_landmarks').append($("<option></option>").attr("value",value.landmark)
        		         .text(value.landmark));
        		    });
        			  $('.address_landmarks').append($("<option class='otherOption'></option>").attr("value","Other").text("Other"));
              		/*added by sneha R2.3*/
        			  $('.address_landmarks').attr("onchange","changeFuncLandMark(this.value)");
        		  /*end of sneha R2.3*/
        		}
        		else {
        			 $('.address_landmarks').html("<option class='otherOption' value='Other'>Other</option>");
        			 changeFuncLandMark("Other"); 
        			//alert("in landmark else");
        			/*console.log("addresslandmark line 222 ");
        			$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
 					$(".address_landmarks").attr("disabled","disabled").css("padding-left","5px");
 					$('.address_landmarks').append($("<option class=unableto></option>").text("Unable to find landmark").attr("selected","selected").attr("value",""));
 					$(".address_landmarkOther").val("");*/
        			
        			
        		}
				} 
        		 // $(".address_states").val(response.state.name).attr("readonly", "true").data("stateValue",response.state.name);
        		 // arg1 = true;
			}
			 console.log("addresslandmark 184 ");
			//return arg1;
		},
		error: function(jqXHR, textStatus, errorThrown) {
			//alert("error");
			console.log("addresslandmark 183 ");
		}
    });
    
   
}
$(".errland1, .errland2").hide();

$(".optionsLandmark .errorMessage").css("padding-bottom", "5px");
	
/*$('.address_landmarks').attr("onchange","changeFuncLandMark(this.value)");
$(".address_landmarkOther, .address_landmarkOtherDiv label").hide();
*/
/*$("#addNewAddress").click(function(e){
	//optionsLandmark(e);
});*/


function changeFuncLandMark(value) {
	$(".errland1").hide();
	if(value == "Other") {
		$(".half .address_landmarkOtherDiv").css("margin-left","10px");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
		//$("#otherLandmark").prop('value','');
	} else {
		$(".address_landmarkOther").prop("value", "");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").hide();
	}
}


function changeFunction(value) {
	$('.otherOption').attr("value", "Other");
	$(".errland1").hide();
	if(value == "Other") {
		$('.otherOption').attr("value", "");
		$('.otherOption').val("");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").show();
		/*$("#otherLandmark").attr('value','');*/
	} else {
		$(".address_landmarkOther").attr("value", "");
		$(".address_landmarkOtherDiv, .address_landmarkOtherDiv label, .address_landmarkOther").hide();
	}
}

/*function optionsLandmark(e) {
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
}*/

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
				     if(!$("#state").prop("disabled")){
				    	 var state=$("#state").val();
				     }else{
				    	 var state=$("#statesReadOnly").val();
				     }
				      var mobile=$("#mobileNo").val();
				      var pincode=$("#pincode").val();
				      var isString = isNaN(mobile);
				      var hasString = isNaN(pincode);
				      var city=$("#city").val();
				      var letters = new RegExp(/^[A-z]*$/);
				      var otherLandMark=$(".otherLandMark").val();
				      if(otherLandMark != null && ! otherLandMark == ''){
				    	  if(otherLandMark.trim() == ''){
				    	    $(".otherLandMarkError").show();
					  		$(".otherLandMarkError").text("Other LandMark cannot be allow  space");
					  		validate = false;
				    	  }else if(!/[a-zA-Z0-9]/.test(otherLandMark)){
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
			  			$(".address1Error").text("Address Line cannot be blank");
			  			validate = false;
				  	} if(al2 != null && ! al2 == ''){
				    	  if(al2.trim() == ''){
				    		  $(".address2Error").show();
					  		  $(".address2Error").text("Address Line 2 cannot be blank");
					  		validate = false;
				    	  }else if(!/[a-zA-Z0-9]/.test(al2)){
				    		  $(".address2Error").show();
					  		  $(".address2Error").text("Address Line 2 cannot be blank");
						  	 validate = false;
				    	  }
				      }
				     if(al3 != null && ! al3 == ''){
				    	  if(al3.trim() == ''){
				    			$(".address3Error").show();
					  			$(".address3Error").text("Address Line 3 cannot be blank");
					  		validate = false;
				    	  }else if(!/[a-zA-Z0-9]/.test(al3)){
				    			$(".address3Error").show();
					  			$(".address3Error").text("Address Line 3 cannot be blank");
						  	 validate = false;
				    	  }
				     }
				    	  if(state == null || state=="Select" ){
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
				  	if(!/^[0-9]+$/.test(pincode))
			        {
				  		$(".pincodeNoError").show();
			  			$(".pincodeNoError").text("Enter only Numbers");
			  			validate = false;
			        }
				  	if(!/^[0-9]+$/.test(mobile))
			        {
				  		  $(".mobileNumberError").show();
				          $(".mobileNumberError").text("Enter only Numbers");
				          validate = false;
			        }
				  	if(mobile.length > 0 && mobile.length < 9 ){
				    	  $(".mobileNumberError").show();
				          $(".mobileNumberError").text("Enter correct mobile number");
				          validate = false;
				  	}if(pincode.length > 0 && pincode.length < 6 ){
				    	  $(".pincodeNoError").show();
				          $(".pincodeNoError").text("Enter correct pincode");
				          validate = false;
				      }if(city == null || city.trim() == '' ){
				  			$(".cityError").show();
				  			$(".cityError").text("City cannot be blank");
				  			validate = false;
					  	}else if(!/[a-zA-Z]/.test(city)){
					  		$(".cityError").show();
				  			$(".cityError").text("City should contain only alphabets");
				  			validate = false;
					  	}
				      if(validate == true){
				    	  $('.saveBlockData').prop('disabled', 'disabled');
				    	//TPR-5271 starts| save address
				    	  if(typeof(utag) !="undefined"){
				    			utag.link({
				    				link_text: "save_shipping_address_clicked",
				    				event_type : "save_shipping_address_clicked"
				    			});
				    		   }
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
								  $('.saveBlockData').prop('disabled', false);
									if(result=='Pincode not Serviceable'){
										$("#changeAddressPopup").show();
										$("#showOTP").hide();
										$(".wrapBG").show();
										var height = $(window).height();
										$(".wrapBG").css("height", height);
										//$("#changeAddressPopup").css("z-index", "999999");
										$(".pincodeNoError").show();
										$(".pincodeNoError").text(result);
										 $('.saveBlockData').prop('disabled', false);
									}else if(result =='Updated'){
										window.location.href=ACC.config.encodedContextPath+"/my-account/order/?orderCode="+orderCode;
									} else if(result == 'Validation Error')
									{
										$("#changeAddressPopup").show();
										$(".wrapBG").show();
										var height = $(window).height();
										$(".wrapBG").css("height", height);
										$(".main_error").show();
										$(".main_error").text("Please Enter Valid Address Information ");
										$('.saveBlockData').prop('disabled', false);
									}
									else{
										$("#changeAddressPopup").hide();
										$("#otpPopup").css({"z-index": "999999", "display":"block", "position":"absolute", "top":"0"});
										$("#otpPopup").html(result).show();
										$(".wrapBG").show();
									}
								},
								error : function(result) {
									$("#changeAddressPopup").show();
									$("#showOTP").hide();
									$(".wrapBG").show();
									var height = $(window).height();
									$(".wrapBG").css("height", height);
									$(".main_error").show();
									$(".main_error").text("Internal server error Please try after sometime");
									$('.saveBlockData').prop('disabled', false);
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
		//alert();
		$(".address_landmarkOtherDiv").attr('data-value',$("#otherLandmarkInitial").val());
		$("#showOTP,#otpPopup").hide();
		$(".wrapBG1").hide();	
	});
	
	$(".addAddressToForm").click(function(){
		var className = $(this).attr("data-item");
		//alert(className);
		$(".error_text").hide();
	
		$("#pincode").val($("."+className+" .postalCode").text());
	
		loadPincodeData("edit").done(function() {
			$("#new-address-option-1").val($("."+className+" .addressType").text());
			$("#firstName").val($("."+className+" .firstName").text());
			$("#lastName").val($("."+className+" .lastName").text());
			
			//TISUATSE-135 start
			var addressLine1 = ($("."+className+" .addressline1").text());
			if(($("."+className+" .addressline2").text()))
			{
			var addressLine2 = ($("."+className+" .addressline2").text());
			addressLine1 = addressLine1 + addressLine2;
			}
			if(($("."+className+" .addressline3").text()))
			{
			var addressLine3 = ($("."+className+" .addressline3").text());
			addressLine1 = addressLine1 + addressLine3;
			}
			$("#addressLine1").val(addressLine1);
			//TISUATSE-135 end
			//$("#addressLine2").val($("."+className+" .addressLine2").text());
			//$("#addressLine3").val($("."+className+" .addressLine3").text());
			$(".address_townCity").val($("."+className+" .town").text());
			$("#mobileNo").val($("."+className+" .phone").text());
			var value = $("."+className+" .landmark").text();
			  $(".address_landmarkOtherDiv").attr('data-value',value)
			 otherLandMarkTri(value,"defult");
			  if(!$("#state").prop("disabled")){
		       // var state=$("#state").val();
		        $("#state").val($("."+className+" .state").text());
		       }else{
		       // var state=$("#statesReadOnly").val();
		        $("#statesReadOnly").val($("."+className+" .state").text());
		       }
			//$("#state").val($("."+className+" .state").text());
		});
		
		
	});
	
});

var count=0;
function newOTPGenerate(orderCode){
	count++;
	if(count <= 9){
	 $.ajax({
			type : "GET",
			url : ACC.config.encodedContextPath + "/my-account/newOTP",
			data :"orderCode="+orderCode,
			success : function(response) {
				if(response==true){
					$(".otpError").show();
					$(".otpError").text("Resend OTP limit remaining "+(10-count));
					
				}else{
					$(".otpError").show();
					$(".otpError").text("OTP sending fail try again ");
				}
			}
		}); 
	 
	}else{
		$(".otpError").show();
		$(".otpError").text("OTP limt exceeded 10 times, pleae try again");
	}
} 
function closeOTP(){
	$("#changeAddressPopup, #otpPopup").hide();
	$(".wrapBG").hide();
}
function otherLandMarkTri(value,event){
	  setTimeout(function(){
		  console.log("line no 19 "+ value);
		  if($(".address_landmarks option[value='"+value+"']").length > "0") {
			  
			 console.log("line no 21");
				  $(".address_landmarks").val("");
				$(".address_landmarks option[value='"+value+"']").prop("selected",true);
				console.log("line no 25 "+ $(".address_landmarks").val());
				} else {
					console.log("line no 27");
				//alert(value+ " 3 in else");
					if($(".address_landmarks option[value='Other']").length > "0" && value != "" && event=="defult") {
						console.log("line no 30");
						  $(".address_landmarks").val("Other"); 
						  console.log("line no 35");
						  changeFuncLandMark("Other"); 
						  $(".address_landmarkOther").val(value);
						 }/*else{
							 $(".address_landmarks").val("");  
						 }*/
					
				
				console.log("line no 38");
				
			}
		  
		  },300);
}
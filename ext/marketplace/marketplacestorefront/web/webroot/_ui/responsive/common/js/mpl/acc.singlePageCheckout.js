//-----------------------------COMMENTS------------------------------------------------------------------//
//	ACC.singlePageCheckout.js contains code relevant to single page checkout only. Ajax calls fired in 
//	this js are mostly directed to MplSingleStepCheckoutController.java, With a few methods also directed to
//	DeliveryMethodCheckoutStepController.java. PaymentMethodCheckoutStepController.java will come into picture once 
//	PlaceOrder button is clicked.
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//-----------------------------COMMENTS ON DIV USED TO DISPLAY ERROR MESSAGE-----------------------------//
//	1.newAddressMobileErrorMessage	:	Used to display error messages in case of new address in responsive
//	2.addressMessage				:	Common div Used to display error messages in case of new address in responsive and web, For example validation errors.
//	3.selectedAddressMessageMobile	:	Used to display error messages(for ex: serviceability failure) in case a user selects an existing address in responsive
//	4.selectedAddressMessage		:	Used to display error messages(for ex: serviceability failure) in case a user selects an existing address in web
//////////////////////////////////////////////////////////////////////////////////////////////////////////
ACC.singlePageCheckout = {

	_autoload: [
	    //"mobileAccordion"
	],
	ajaxErrorMessages:function(code){
		var message="";
		switch(code){
			case "address.error.formentry.invalid": message="Errors were found with the address you provided. Please check the errors below and re-submit your address.";
													break;
			case "address.state.invalid" : message="Please enter state"; break;
			case "address.firstName.invalid" : message="Please enter first name"; break;
			case "address.firstName.invalid.new" : message="Please enter a valid first name"; break;
			case "address.lastName.invalid" : message="Please enter last name"; break;
			case "address.lastName.invalid.new" : message="Please enter a valid last name"; break;
			case "address.line1.invalid" : message="Please enter address Line 1"; break;
			case "address.line1.invalid.length" : message="Address line 1 should be of maximum 40 characters"; break;
			case "address.line2.invalid.length" : message="Address line 2 should be of maximum 40 characters"; break;
			case "address.line3.invalid.length" : message="Address line 3 should be of maximum 40 characters"; break;
			case "address.townCity.invalid"  : message="Please enter a Town/City"; break;
			case "address.state.invalid" : message="Please enter state"; break;
			case "address.mobileNumber.invalid" : message="Please enter a Mobile Number"; break;
			case "address.mobileNumber.invalid.numeric.length" : message="Mobile number should be of 10 digit numeric only"; break;
			case "address.postcode.invalid" : message="Please enter post code"; break;
			case "address.postcode.invalid.numeric.length" : message="Post code should be of 6 digit numeric only"; break;
			case "address.addressType.select" : message="Please select an address Type"; break;
			case "clientSideAddressFormValidationFailed" : message=ACC.singlePageCheckout.formValidationErrorCount+" errors occured. Please re-enter to continue"; break;
			case "noStoresFound" : message="Unable to find Stores"; break;
			case "jsonExceptionMsg" : message="Exception Occured in json block"; break;
			default:message="No message specified"; 
		}
		return message;
	},
	//Function used to refresh delivery address owl carousel once a new address is added or an existing address edited.
	getDeliveryAddresses:function(element){
		
		var url=ACC.config.encodedContextPath + "/checkout/single?isAjax=true&isResponsive="+ACC.singlePageCheckout.getIsResponsive();
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data) {
        	if(!ACC.singlePageCheckout.getIsResponsive())
        	{
	        	$("#chooseDeliveryAddress").html(data);
	        	//Carousel reformation
	        	if($(".checkTab .addressList_wrapper .address-list").length == 2){
	    			$("#address_carousel").addClass("two_address");
	    		}
	    		if($(".checkTab .addressList_wrapper .address-list").length == 1){
	    			$("#address_carousel").addClass("one_address");
	    		}
	        	$("#address_carousel").on('initialize.owl.carousel initialized.owl.carousel ' +
		                'initialize.owl.carousel initialize.owl.carousel ' +
		                'to.owl.carousel changed.owl.carousel',
		                function(event) {
							var items     = event.item.count;     // Number of items
							var item      = event.item.index;     // Position of the current item
							
							if($(window).width() > 1263){
								var page_no = parseInt(items/3);
								if(items%3 > 0){
									page_no = parseInt(items/3) + 1;
								}
								var current_page = parseInt(item/3) + 1;
								if(item%3 > 0){
									current_page = parseInt(item/3) + 2;
								}
								}
								else{
									var page_no = parseInt(items/2);
									if(items%2 > 0){
										page_no = parseInt(items/2) + 1;
									}
									var current_page = parseInt(item/2) + 1;
									if(item%2 > 0){
										current_page = parseInt(item/2) + 2;
									}
								}
							$(".page_count").html("<span>"+current_page + " / " + page_no+"</span>");
		                });
		              $("#address_carousel").owlCarousel({
		                items:3,
						loop: false,
						nav: ($(".checkTab .addressList_wrapper .address-list").length <= 3)?false:true,
						dots:false,
						navText:[],
						slideBy: 3,
						margin: 82,
						responsive : {
	            			// breakpoint from 0 up
	            			0 : {
	            				items:1,
	            				stagePadding: 36,
	            				slideBy: 1,
	            			},
	            			// breakpoint from 768 up
	            			768 : {
	            				items:2,
	            				slideBy: 2,
	            			},
	            			// breakpoint from 1280 up
	            			1280 : {
	            				items:3,
	            			}			
	            		},
	            		onRefresh: function () {
	            			$("#address_carousel").find('div.owl-item').height('');
	                    },
	                    onRefreshed: function () {
	                    	$("#address_carousel").find('div.owl-item').height($("#address_carousel").height());
	                    }
		              });
        	}
        	else
        	{
        		$("#chooseDeliveryAddressMobileDiv").html(data);
        	}
		});
        
        xhrResponse.always(function(){
			console.log("ALWAYS:");
		});
   
		return false;	
	},
	//Function used to fetch edit address form. 
	getEditAddress:function(element,event){
		event.preventDefault();
		//The ajax loader is loaded here and hidden in last line of showEditAddressDetails.jsp
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + $(element).attr("href");
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
			ACC.singlePageCheckout.hideAjaxLoader();
			data={displaymessage:"Network error occured",type:"error"};
			ACC.singlePageCheckout.processError("#selectedAddressMessage",data);
		});
        
        xhrResponse.done(function(data) {
        	var elementId="singlePageAddressPopup";
        	ACC.singlePageCheckout.modalPopup(elementId,data);
        	loadPincodeData("edit").done(function() {
        	var value = $(".address_landmarkOtherDiv").attr("data-value");
   			 otherLandMarkTri(value,"defult");
            });
		});
        
        xhrResponse.always(function(){
        	//ACC.singlePageCheckout.hideAjaxLoader();
		});
        
		return false;	
	},
	//Function used to post previously fetched and edited address form.
	postEditAddress:function(element){
		var form=$(element).closest("form");
		var validationResult=ACC.singlePageCheckout.validateAddressForm();
		if(validationResult!=false)
		{			
			var addressId=$(form).find(" #addressId").val();
			var url=ACC.config.encodedContextPath + "/checkout/single/edit-address/"+addressId;
			var data=$(form).serialize().replace(/\+/g,'%20');
			ACC.singlePageCheckout.showAjaxLoader();
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	            if (jqXHR.responseJSON) {
	                if(data.type!="response" && data.type!="confirm")
	                {
	                	ACC.singlePageCheckout.processError("#addressMessage",data);
	                }
	                else if(data.type=="confirm")
                	{
                	ACC.singlePageCheckout.processConfirm("#addressMessage",data,"","","editAddress",element);
                	}
	            } else {
	            	ACC.singlePageCheckout.getSelectedAddress();
	                $("#choosedeliveryMode").html(data);
	                ACC.singlePageCheckout.showAccordion("#choosedeliveryMode");
		        	//selectDefaultDeliveryMethod();
		        	$(".click-and-collect").addClass("click-collect");
		        	$("#singlePageAddressPopup").modal('hide');
		        	ACC.singlePageCheckout.getDeliveryAddresses();
		        	$("#selectedAddressMessage").hide();
		        	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
	            }
	        });
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else
		{
			data={displaymessage:"clientSideAddressFormValidationFailed",type:"errorCode"}
    		ACC.singlePageCheckout.processError("#addressMessage",data);
		}
		return false;	
	},
	//Function used to get new address form.
	getAddAddress:function(){
		//The ajax loader is loaded here and hidden in last line of showAddAddressDetails.jsp
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/new-address";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
			ACC.singlePageCheckout.hideAjaxLoader();
			data={displaymessage:"Network error occured",type:"error"};
			ACC.singlePageCheckout.processError("#selectedAddressMessage",data);
		});
        
        xhrResponse.done(function(data) {
        	var elementId="singlePageAddressPopup";
        	ACC.singlePageCheckout.modalPopup(elementId,data);
		});
        
        xhrResponse.always(function(){
        	//ACC.singlePageCheckout.hideAjaxLoader();
		});
        
		return false;	
	},
	//Function used to post new address form.
	postAddAddress:function(element){
		var form=$(element).closest("form");
		var validationResult=ACC.singlePageCheckout.validateAddressForm();
		if(validationResult!=false)
		{
			var url=ACC.config.encodedContextPath + "/checkout/single/new-address";
			var data=$(form).serialize().replace(/\+/g,'%20');
			
			ACC.singlePageCheckout.showAjaxLoader();
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	        	if (jqXHR.responseJSON) {
	        		if(data.type!="response" && data.type!="confirm")
	                {
	                	ACC.singlePageCheckout.processError("#addressMessage",data);
	                }
	                else if(data.type=="confirm")
                	{
	                	
                	ACC.singlePageCheckout.processConfirm("#addressMessage",data,"","","addAddress",element);
                	}
	            }  else {
					ACC.singlePageCheckout.getSelectedAddress();
					$("#choosedeliveryMode").html(data);
		        	ACC.singlePageCheckout.showAccordion("#choosedeliveryMode");
		        	//selectDefaultDeliveryMethod();
		        	$(".click-and-collect").addClass("click-collect");
		        	$("#singlePageAddressPopup").modal('hide');
		        	ACC.singlePageCheckout.getDeliveryAddresses();
		        	$("#selectedAddressMessage").hide();
		        	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
		        	if(typeof utag !="undefined"){
						utag.link({ link_text : 'add_new_address_saved' ,event_type : 'add_new_address_saved'});
					}
	            }
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else
		{
			data={displaymessage:"clientSideAddressFormValidationFailed",type:"errorCode"}
    		ACC.singlePageCheckout.processError("#addressMessage",data);
		}
		return false;	
	},
	//Function called when proceed button on delivery options page is clicked. 
	//This function will fetch slot delivery page or will proceed to review order page
	proceedOnDeliveryModeSelection:function(element){
		var entryNumbersId=$("#entryNumbersId").val();
		var isCncPresent=$("#selectDeliveryMethodForm #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
    	var cncSelected="false";
		if(entryNumbersId=="")
		{
			return false;
		}
		else
		{	
			//validate here
			var entryNumbers=entryNumbersId.split("#");
			for(var i=0;i<entryNumbers.length-1;i++)
			{
				if($("input[name='"+entryNumbers[i]+"']").length>0 && $("input[name='"+entryNumbers[i]+"']:checked").length<=0)
				{
				 data={displaymessage:"Please select a delivery option to proceed.",type:"error"};
				 ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
				 return false;
				}
				if(isCncPresent=="true")
	        	{
					if($("input[name='"+entryNumbers[i]+"']").length>0 && $('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
    				{
        				cncSelected="true";							
						if($("input[name='address"+entryNumbers[i]+"']:checked").length<=0)
						{
							$("."+entryNumbers[i]+"_select_store").show();
							return false;
						}
    				}
	        	}
			}
		}
		ACC.singlePageCheckout.showAjaxLoader();
		//var url=ACC.config.encodedContextPath + $("#selectDeliveryMethodForm").prop("action");
		var url=$("#selectDeliveryMethodForm").prop("action");
		var data=$("#selectDeliveryMethodForm").serialize();
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
            if (jqXHR.responseJSON) {
            	ACC.singlePageCheckout.isSlotDeliveryAndCncPresent=false;
            	if(data.type!="response" && data.type!="ajaxRedirect")
                {
                	ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
                }
            	else if(data.type=="ajaxRedirect" && data.redirectString=="redirectToReviewOrder")
        		{
                	if(isCncPresent=="true" && cncSelected=="true")
                	{
                		$("#singlePagePickupPersonPopup").modal('show');
                	}
                	else
                	{
                		ACC.singlePageCheckout.getReviewOrder();
                	}
            		ACC.singlePageCheckout.getSelectedDeliveryModes("");
        		}
            } else {
            	ACC.singlePageCheckout.getSelectedDeliveryModes("");
            	if(isCncPresent=="true" && cncSelected=="true")
            	{
            		$("#singlePagePickupPersonPopup").modal('show');
            		//Code to delay slot delivery till the user saves pickup person details
            		ACC.singlePageCheckout.isSlotDeliveryAndCncPresent=true;
            		$("#singlePageChooseSlotDeliveryPopup #modalBody").html(data);
            	}
            	else
            	{//Code to show slot delivery page if CNC is not present
            		var elementId="singlePageChooseSlotDeliveryPopup";
            		ACC.singlePageCheckout.modalPopup(elementId,data);
            	}
            }
        });
        
        xhrResponse.always(function() {
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	//Used to validate address form for mobile and web.
	validateAddressForm:function(){
		ACC.singlePageCheckout.formValidationErrorCount=0;
		var errorCount=0;
		$("form#addressForm :input[type=text]").each(function(){
   		 var input = $(this);    
   		 $(this).val($(this).val().trim());    		     		
		});  

		var validate=true;
		var regPostcode = /^([1-9])([0-9]){5}$/;
	    var mob = /^[1-9]{1}[0-9]{9}$/;
	    var letters = /^[a-zA-Z]+$/; 
	    var cityPattern = /^[a-zA-Z]+([\s]?[a-zA-Z]+)*$/;
	    var firstName = document.getElementById("address.firstName");
		var lastName = document.getElementById("address.surname");
		var address1 = document.getElementById("address.line1");
		var regAddress = /^[0-9a-zA-Z\-\/\,\s]+$/;
		var address2 = document.getElementById("address.line2");
		var address3 = document.getElementById("address.line3");
		var city= document.getElementById("address.townCity");
		var stateValue = document.getElementById("address.states");
		var zipcode = document.getElementsByName("postcode")[0].value;
		var txtMobile = document.getElementsByName("MobileNo")[0].value;
		var result=firstName.value;
		$(".otherLandMarkError").hide();
		 
		if(result == undefined || result == "" )
		{	
			$("#addressfirstNameError").show();
			$("#addressfirstNameError").html("<p>First Name cannot be Blank</p>");
			validate= false;
			errorCount++;
		}
		else if(letters.test(result) == false)  
		{ 
			$("#addressfirstNameError").show();
			/*Error message changed TISPRD-427*/
			$("#addressfirstNameError").html("<p>First name should not contain any special characters or space</p>");
			validate= false;
			errorCount++;
		}  
		else
		{
			$("#addressfirstNameError").hide();
		}
				
		 result=lastName.value;
		if(result == undefined || result == "")
		{	
			$("#addresssurnameError").show();
			$("#addresssurnameError").html("<p>Last Name cannot be Blank</p>");
			validate= false;
			errorCount++;
		}
		else if(letters.test(result) == false)  
		{ 
			$("#addresssurnameError").show();
			/*Error message changed TISPRD-427*/
			$("#addresssurnameError").html("<p>Last name should not contain any special characters or space</p>");
			validate= false;
			errorCount++;
		} 
		else
		{
			$("#addresssurnameError").hide();
		}
		
		result=address1.value;
		if(result == undefined || result == "")
		{
			$("#addressline1Error").show();
			$("#addressline1Error").html("<p>Address Line cannot be blank</p>");
			validate= false;
			errorCount++;
		}
		else
		{
			$("#addressline1Error").hide();
		}	
		  result=city.value;
		if(result == undefined || result == "")
		{	
			$("#addresstownCityError").show();
			$("#addresstownCityError").html("<p>City cannot be blank</p>");
			 validate=false;
			 errorCount++;
		}
		else if(cityPattern.test(result) == false)  
		{ 
			$("#addresstownCityError").show();
			$("#addresstownCityError").html("<p>City must be alphabet only</p>");
			validate= false;
			errorCount++;
		}
		else
		{
			$("#addresstownCityError").hide();
		}

		result=stateValue.options[stateValue.selectedIndex].value;
		if(result == undefined || result == "")
		{	
			$("#addressstatesError").show();
			$("#addressstatesError").html("<p>Please choose a state</p>");
			 validate = false;
			 errorCount++;
		}
		else
		{
			$("#addressstatesError").hide();
		}
		
	   if(zipcode == undefined || zipcode == "")
		{	
			$("#addresspostcodeError").show();
			$("#addresspostcodeError").html("<p>Please enter a pincode</p>");
			validate = false;
			errorCount++;
		}
	    else if(regPostcode.test(zipcode) == false){
	        $("#addresspostcodeError").show();
	        $("#addresspostcodeError").html("<p>Please enter a valid pincode</p>");
			validate= false;
			errorCount++;
		}
	    else
		{
			$("#addresspostcodeError").hide();
		}
	 
	   if(txtMobile  == undefined || txtMobile == "")
		{	
			$("#addressmobileError").show();
			$("#addressmobileError").html("<p>Please enter mobile no.</p>");
	        validate = false;
	        errorCount++;
		}
	    else if (mob.test(txtMobile) == false) {
			$("#addressmobileError").show();
			$("#addressmobileError").html("<p> Please enter correct mobile no.</p>");
			 validate=false;
			 errorCount++;
	    }
	       else
		{
			$("#addressmobileError").hide();
		}
	   
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
	   
	   	//var landMark=$( ".address_landmarks option:selected" ).val();
//	   	if(landMark=="Other")
//	   	{
//			result=$("#otherLandmark").val();
//			if(result != null){
//			   if(result.trim() == ''){
//		  	        $(".otherLandMarkError").show();
//			  		$(".otherLandMarkError").text("Other LandMark cannot be allow  space");
//			  	    validate = false;
//					errorCount++;
//		  	     }else if(/[^a-zA-Z0-9]/.test(result)){
//		  		      $(".otherLandMarkError").show();
//				  	  $(".otherLandMarkError").text("Other LandMark cannot be allow special characters");
//				  	 validate = false;
//					 errorCount++;
//		  	  }
//		    }
//	   	}
//	   	else if(landMark=="NA")
//   		{
//	   		$(".selectLandMarkError").show();
//		  	$(".selectLandMarkError").text("Select a landmark to proceed");
//		  	validate = false;
//		  	errorCount++;
//   		}
	   
	   	if(address1.value.indexOf('#')!=-1)
	   	{
			address1.value=encodeURIComponent(address1.value);
	   	}
			
			if(address2.value.indexOf('#')!=-1)
	   	{
			address2.value=encodeURIComponent(address2.value);
	   	}
			if(address3.value.indexOf('#')!=-1)
	   	{
			address3.value=encodeURIComponent(address3.value);
	   	}
	   	if(validate==false)
		{
	   		ACC.singlePageCheckout.formValidationErrorCount=errorCount;
			return false;
		}
		else{
			return true;
		}
	},
	//Function called when ever a user clicks on deliverHere/Save and Continue button(for edit and add address) button.
	proceedOnAddressSelection:function(element,addressId){
		var form=$(element).closest("form");
		$("#radio_"+addressId).prop("checked", true);
		a = $("input[name=selectedAddressCode]:checked").val();
        if (null == a || "undefined" == a)
            return $("#emptyAddress").show(),
            !1;
        ACC.singlePageCheckout.showAjaxLoader();
        var data=$(form).serialize();
        var url=ACC.config.encodedContextPath + form.attr("action");
        var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        var radio = $("#radio-default2_"+addressId);
    	var radio_label = $("#radio-default2_"+addressId).parent().find('label');
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        //calling tealium
        $("#checkoutPageName").val("Choose Your Delivery Options");
        tealiumCallOnPageLoad();
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type=="confirm")
                {
        			ACC.singlePageCheckout.processConfirm("#selectedAddressMessage",data,addressId,"","",element);
                }
        		if(data.type!="confirm")
                {
        			ACC.singlePageCheckout.processError("#selectedAddressMessage",data);
                }
            } else {
	        	//alert(data);
            	ACC.singlePageCheckout.getSelectedAddress();
	        	$("#choosedeliveryMode").html(data);
	        	ACC.singlePageCheckout.showAccordion("#choosedeliveryMode");
	        	//selectDefaultDeliveryMethod();
	        	$(".click-and-collect").addClass("click-collect");
	        	$("#selectedAddressMessage").hide();
	        	
	        	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
	        	
//				Below code is not required any more as default address is being set in controller  itself	        	
//	        	var urlSetDefault=ACC.config.encodedContextPath + "/checkout/multi/delivery-method/set-default-address/" + addressId;
//	        	var xhrResponseSetDefault=ACC.singlePageCheckout.ajaxRequest(urlSetDefault,"GET","",false);
//	        	xhrResponseSetDefault.fail(function(xhr, textStatus, errorThrown) {
//	    			console.log("ERROR:"+textStatus + ': ' + errorThrown);
//	    		});
//	        	
//	        	xhrResponseSetDefault.done(function(data) {
//	        		//alert(data);
//	        		if(data.toString()=="true"){
//	     				//console.log(radio);
//	     				$(".addressList_wrapper input[type='radio']+label").removeClass("radio-checked");
//	     				
//	     				radio.attr('checked', 'checked');
//	     				console.log(radio_label);
//	     				radio_label.addClass('radio-checked');
//	     				//radio_label.css('background-color',' #999999');
//	     			}else{
//	     				console.log("Unable to set default address");
//	     			}
//	        	});
	        	
	        	//Radio button of the currently selected address is checked below
	        	$(".addressList_wrapper input[type='radio']+label").removeClass("radio-checked");
 				
 				radio.attr('checked', 'checked');
 				console.log(radio_label);
 				radio_label.addClass('radio-checked');
 				//Radio button of the currently selected address is checked above
	        	
	        	//Resetting voucher on removal of cart item, we are doing it twice once in proceedOnAddressSelection,getDeliverOptionsPage
        		var couponCode=$("#couponFieldId").val();
        		if(couponCode!="")
        		{
        			//Code is in marketplacecheckoutadon.js
        			resetAppliedCouponFormOnRemoval();
        		}
            }
		});
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
      if(typeof utag !="undefined")  {
		utag.link({
	        link_text: "deliveryOptions_proceed_clicked",
	        event_type: "proceed_button_clicked"
	    })
      }
	},
	//Function used to hide CNC store block when a user toggels between HD/ED and CNC
	attachDeliveryModeChangeEvent:function()
	{
		$("#choosedeliveryMode input:radio")
		var entryNumbersId=$("#entryNumbersId").val();
		var entryNumbers=entryNumbersId.split("#");
		for(var i=0;i<entryNumbers.length-1;i++)
		{
			$('input:radio[name='+entryNumbers[i]+']').on('change',function(){
				var id=$(this).attr("id");
				if(!id.includes("click-and-collect"))
				{
					var idSplit=id.split("_");
					$("#cncStoreContainer"+idSplit[1]).slideUp();
				}
			})
		}
	},
	
	getPickUpPersonForm:function(pickupPersonName,pickupPersonMobileNo){		
    	var isCncPresent=$("#selectDeliveryMethodForm #isCncPresentInSinglePageCart").val();
    	if(isCncPresent=="true")
    	{
    		var htmlPopulated=$("#singlePagePickupPersonPopup span#modalBody").attr("data-htmlPopulated");
    		if(htmlPopulated=="NO")
        	{
    			var url=ACC.config.encodedContextPath + "/checkout/single/pickupPerson/popup";
        		var data="";
        		var xhrPickupPersonResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);;
        		xhrPickupPersonResponse.fail(function(xhr, textStatus, errorThrown) {
        				console.log("ERROR:"+textStatus + ': ' + errorThrown);
        		});
        	        
        		xhrPickupPersonResponse.done(function(data) { 
        			//Populating popup
        			$("#singlePagePickupPersonPopup #modalBody").html(data);
        			$("#singlePagePickupPersonPopup #modalBody").data("htmlPopulated","YES");
        			$('form[name="pickupPersonDetails"] #pickupPersonName').val(pickupPersonName);
            		$('form[name="pickupPersonDetails"] #pickupPersonMobile').val(pickupPersonMobileNo);
    			});
        	}
    	}
	},
	
	
	getSelectedAddress:function(){
		var url=ACC.config.encodedContextPath + "/checkout/single/deliveryAddress";
		var data="";
        var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
                if(data.type=="response")
                {              
                	$("#selectedAddressDivId").show();
                	var pickupPersonName=data.pickupPersonName;
                	var pickupPersonMobileNo=data.pickupPersonMobileNo;
                	$("#selectedAddressHighlight").html(data.fullAddress);
                	ACC.singlePageCheckout.getPickUpPersonForm(pickupPersonName,pickupPersonMobileNo);
            		
                }
            }
		});
	},
	
	getSelectedDeliveryModes:function(callFrom){
		var url=ACC.config.encodedContextPath + "/checkout/single/deliveryModesSelected";
		var data="";
        var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
                if(data.type=="response")
                {              
                	$("#selectedDeliveryOptionsDivId").show();
                	var str ="";
                	if(data.hd>0)
                	{
                		str+="Home Delivery ("+data.hd+" item),"
                	}
                	if(data.ed>0)
                	{
                		str+="Express Delivery ("+data.ed+" item),"
                	}
                	if(data.cnc>0)
                	{
                		str+="Store Pickup ("+data.cnc+" item)"
                	}
                	//Code to remove last , if present
                	var lastIndexOfComma=str.lastIndexOf(",")
                	var len=str.length;
                	if(lastIndexOfComma==len-1)
                	{
                		str=str.substring(0,len-1);
                	}
                	$("#selectedDeliveryOptionsHighlight").html(str);
                	
                	// For Review Order Highlight Display
                	//$("#selectedReviewOrderHighlight").html(data.CountItems + " Items, " + data.totalPrice);
                	$("#selectedReviewOrderHighlight").html(data.CountItems + " Items, ");
                	
                	if(callFrom=="removeCartItem")
                	{
                		$.each(data.deliveryDetails,function(entryNumber,obj){
                		    console.log("entryNumber="+entryNumber);	
                		    console.log("obj.deliveryMode="+obj.deliveryMode);
                		    console.log("obj.ussid="+obj.ussid);
                		    $("#radio_"+entryNumber+"_"+obj.deliveryMode).prop('checked', true);
                		    if(obj.deliveryMode=="click-and-collect" && typeof(obj.storeName)!='undefined' && obj.storeName!="")
                		    {
                		    	console.log("obj.storeName="+obj.storeName);
                		    	ACC.singlePageCheckout.fetchStores(entryNumber,obj.ussid,obj.deliveryMode,callFrom,obj.storeName);
                		    }
                		});
                	}
                }
            }
		});
	},
	
	ajaxRequest:function(url,method,data,cache){
		return $.ajax({
			url: url,
			type: method,
			data: data,
			cache: cache
		});
	},
	
	fetchStores:function(entryNumber,sellerArticleSKU,deliveryCode,callFrom,storeName)
	{
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/cncStores";
		var data="entryNumber="+entryNumber+"&sellerArticleSKU="+sellerArticleSKU+"&deliveryCode="+deliveryCode;
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type!="response")
                {
        			ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
                }
            } else {
            	$('#cncStoreContainer'+entryNumber).css("display","block");
            	$('#cncStoreContainer'+entryNumber).html(data);
            	var cnc_arrow_left, cnc_top;
            	if($('#cncStoreContainer'+entryNumber).parent().prev().find("li.click-and-collect").length > 0){
            	cnc_arrow_left = parseInt($('#cncStoreContainer'+entryNumber).parent().prev().find("li.click-and-collect").offset().left) + 40;
            	//cnc_top = parseInt($('#cncStoreContainer'+entryNumber).offset().top) - parseInt($('#cncStoreContainer'+entryNumber).parent().prev().find("li.click-and-collect").offset().top) - 8;
            	}
            	$('#cncStoreContainer'+entryNumber).find(".cnc_arrow").css("left",cnc_arrow_left+"px");
            	//$('#cncStoreContainer'+entryNumber).parent().css("margin-top","-"+cnc_top+"px");
            	//CNC Carousel
            	if($(".cnc_item .removeColor1").length == 2){
        			$("#cnc_carousel").addClass("two_address");
        		}
        		if($(".cnc_item .removeColor1").length == 1){
        			$("#cnc_carousel").addClass("one_address");
        		}
            	$(".cnc_carousel").on('initialize.owl.carousel initialized.owl.carousel ' +
        				'initialize.owl.carousel initialize.owl.carousel ' +
        				'to.owl.carousel changed.owl.carousel',
        				function(event) {
        			var items     = event.item.count;     // Number of items
        			var item      = event.item.index;     // Position of the current item
        			
        			if($(window).width() > 1263){
						var page_no = parseInt(items/3);
						if(items%3 > 0){
							page_no = parseInt(items/3) + 1;
						}
						var current_page = parseInt(item/3) + 1;
						if(item%3 > 0){
							current_page = parseInt(item/3) + 2;
						}
						}
						else{
							var page_no = parseInt(items/2);
							if(items%2 > 0){
								page_no = parseInt(items/2) + 1;
							}
							var current_page = parseInt(item/2) + 1;
							if(item%2 > 0){
								current_page = parseInt(item/2) + 2;
							}
						}
        			$(".page_count_cnc").html("<span>"+current_page + " / " + page_no+"</span>");
        		});
        		$(".cnc_carousel").owlCarousel({
        			items:3,
        			loop: false,
        			dots:false,
        			margin: 60,
        			navText:[],
        			slideBy: 3,
        			responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 36,
            				slideBy: 1,
            				margin: 0,
            				nav: ($(".cnc_item .removeColor1").length <= 1)?false:true,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:2,
            				slideBy: 2,
            				nav: ($(".cnc_item .removeColor1").length <= 2)?false:true,
            			},
            			// breakpoint from 1280 up
            			1280 : {
            				items:3,
            				nav: ($(".cnc_item .removeColor1").length <= 3)?false:true,
            			}			
            		},
            		onRefresh: function () {
            			$(".cnc_carousel").find('div.owl-item').height('');
                    },
                    onRefreshed: function () {
                    	$(".cnc_carousel").find('div.owl-item').height($(".cnc_carousel").height());
                    }
        		});
            	
        		$( '.cnc_carousel input.radio_btn' ).on( 'click change', function(event) {
        			event.stopPropagation();
        		});
        		
        		//After remove from cart in review order page, Pre select store name using the code below
        		if(callFrom=="removeCartItem")
        		{
        		    $("#cncStoreContainer"+entryNumber).find("[data-storeName='"+storeName+"']").prop('checked', true);
        		}
            }
		}); 
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
	},
	searchCNCStores: function(element,entryNumber){
		var container="div#cncStoreContainer"+entryNumber;
		var allItemselector=container+' .cncStoreAddressSection';		
			var  $allListElements =$(allItemselector);
			$allListElements.parent("li").removeClass("showStoreAddress");
			var searchText = document.getElementById(element).value.toUpperCase();
			$.each($allListElements,function(i,storeAddress){
				var addrElements=$(storeAddress).find(".searchable");
				$.each(addrElements,function(j,el){					
					var listItemText = $(el).text().trim().toUpperCase();					
					if(listItemText.includes(searchText))
					{						
						if(!$(storeAddress).closest("li").hasClass("showStoreAddress"))
						{
							$(storeAddress).closest("li").addClass("showStoreAddress");
						}
					}
				});
			});
			
			if(searchText!="" && $(".showStoreAddress").length>0)
			{				
				$allListElements.closest("li").hide();
				$allListElements.closest("li").parent("div.owl-item").hide();
				$allListElements.closest("li").parent("div.owl-item").removeClass("owl-item");
				$(".showStoreAddress").parent("div").addClass("owl-item");
				$(".showStoreAddress").parent("div").show();
				$(".showStoreAddress").show();
			}else if(searchText=="")
			{
				$allListElements.closest("li").show();
				$allListElements.closest("li").removeClass("showStoreAddress");
				$allListElements.closest("li").parent("div").addClass("owl-item");
				$allListElements.closest("li").parent("div.owl-item").show();
			}
			
			if( typeof utag !="undefined" && searchText!=""){
				utag.link({
					link_text: searchText+"entered",
					event_type : searchText+"entered",
					search_keyword : searchText
				});
			}
			
		
	},
	searchOnEnterPress:function(e,element,entryNumber)
	{
		var code = e.keyCode || e.which;
	    if(code==13){
	    	ACC.singlePageCheckout.searchCNCStores(element,entryNumber);
	        // Enter pressed
	    }
	},
	savePickupPersonDetails: function(element)
	{
		ACC.singlePageCheckout.hidePickupDetailsErrors();
		if(!ACC.singlePageCheckout.validatePickupPersonName())
		{
			console.log("Pickup person name validation failed");
		}
		else if(!ACC.singlePageCheckout.validatePickupPersonMobile())
		{
			console.log("Pickup person mobile number validation failed");
		}
		else{
			ACC.singlePageCheckout.submitPickupPersionDetails();
		}
	},
	
	submitPickupPersionDetails:function() {
		ACC.singlePageCheckout.showAjaxLoader();
		var pickupPersonName = $("#pickupPersonName").val();
		var pickupPersonMobile = $("#pickupPersonMobile").val();
		var url = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPickupPersonDetails";
		var data = 'pickupPersonName=' + pickupPersonName+ '&pickupPersonMobile=' + pickupPersonMobile;
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data) {
        	$(".pickUpPersonAjax").fadeIn(100);
			//if($("#pickupPersonSubmit").text() != "1") {
				$(".pickUpPersonAjax").html("<span class='pickupText'>Pickup Person Details Have Successfully Added.</span>");
			//}
			//$("#pickupPersonSubmit").text("1");
			
			$("#singlePagePickupPersonPopup").modal('hide');
			//Below code will only will execute for web as responsive does not have review order
			//and slot delivery is show on different button click
			if(!ACC.singlePageCheckout.getIsResponsive())
			{	
				//Slot delivery is present show slot delivery else goto review order.
				if(ACC.singlePageCheckout.isSlotDeliveryAndCncPresent)
				{
					$("#singlePageChooseSlotDeliveryPopup").modal('show');
				}
				else
				{
					ACC.singlePageCheckout.getReviewOrder();
				}
			}
			
			if(typeof(utag)!="undefined")
			{
				<!---TPR-639-->
				utag.link(
						{link_text: 'collectatstore_pickup_submit' , event_type : 'collectatstore_pickup_submit'}
					 );
				<!---End of TPR-639-->
			}
		});
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	
	validatePickupPersonNameOnKeyUp:function(){
		ACC.singlePageCheckout.hidePickupDetailsErrors();
		ACC.singlePageCheckout.validatePickupPersonName();
	},
	
	validatePickupPersonMobileOnKeyUp:function(){
		ACC.singlePageCheckout.hidePickupDetailsErrors();
		ACC.singlePageCheckout.validatePickupPersonMobile();
	},
	
	validatePickupPersonName:function(){
		var pickupPersonName = $("#pickupPersonName").val();
		var statusName = ACC.singlePageCheckout.allLetter(pickupPersonName);
		var nameCheck = ACC.singlePageCheckout.checkWhiteSpace(pickupPersonName);
		if(pickupPersonName.length <= "3"){ 
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
			return false;
		}
		else if(nameCheck == false){
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Spaces cannot be allowed");
			return false;
		 }
		else if(statusName == false) {
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Please Enter Only Alphabets");
			return false;
		}
		else{
			return true;
		}
	},
	
	validatePickupPersonMobile:function(){
		var pickupPersonMobile = $("#pickupPersonMobile").val();
		var isString = isNaN(pickupPersonMobile);
		var mobileSpaceCheck = ACC.singlePageCheckout.checkMobileNumberSpace(pickupPersonMobile);
		if(pickupPersonMobile.length <= "10") {
			if(isString==true || mobileSpaceCheck==true) {
				$(".pickupPersonMobileError").show();
				$(".pickupPersonMobileError").text("Enter only numbers");
				return false;
			}
			else if (pickupPersonMobile.length <= "9") {
				$(".pickupPersonMobileError").show();
				$(".pickupPersonMobileError").text("Enter 10 Digit Number");
				return false;
			}
			else if(pickupPersonMobile.indexOf("-") > -1 || pickupPersonMobile.indexOf("+") > -1 ) {
				$(".pickupPersonMobileError").show();
				$(".pickupPersonMobileError").text("Enter only numbers");
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return false;
		}
	},
	
	hidePickupDetailsErrors:function(){
		//$(".pickupPersonSubmitError").hide();
		$(".pickUpPersonAjax").hide();
		$(".pickupPersonMobileError").hide();
		$(".pickupPersonNameError").hide();
		$(".pickupDetails").hide();
	},
	
	modalPopup: function(elementId,data){

		var selector="#"+elementId+" #modalBody"
		$(selector).html(data);
		$("#"+elementId).modal('show');
		$(".new-address-form-mobile").html('');
	},
	
	showAccordion: function(showElementId){
		$.each($("#singlePageAccordion .checkout-accordion"),function(){
			if($(this).hasClass("accordion-open"))
			{
				$(this).removeClass("accordion-open");
				//$(this).find(".checkout-accordion-body").slideUp();
			}
		});
    	$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
    	//$(showElementId).closest(".checkout-accordion").find(".checkout-accordion-body").slideDown();
    	$(showElementId).closest(".checkout-accordion").prevAll(".checkout-accordion").addClass("checkout-selected");
	},

	allLetter:function (inputtxt) { 
        var letters = new RegExp(/^(\w+\s?)*\s*$/);
        var number = new RegExp(/\d/g);
        if(inputtxt.match(letters))
        {
        	if(inputtxt.match(number))
	        {
	            return false;
	        }
	        else
	        {
	            return true;
	        }
        }
        else
        {
            return false;
        }
    },
    
    checkMobileNumberSpace:function(number) {			
		return /\s/g.test(number);
	},
	
	checkWhiteSpace:function(text) {
        var letters = new RegExp(/^(\w+\s?)*\s*$/);
        var number = new RegExp(/\d/g);
        if(letters.test(text))
	        {
	        	if(number.test(text))
		        {
		            return false;
		        }
		        else
		        {
		            var enteredText = text.split(" ");
                    var length = enteredText.length;
                    var count = 0;
                    var countArray = new Array();
                    for(var i=0;i<=length-1;i++) {
                        if(enteredText[i]==" " || enteredText[i]=="" || enteredText[i]==null) {
                            countArray[i] = "space";
                            count++;
                        } else {
                            countArray[i] = "text";
                        }
                    }
                    var lengthC = countArray.length;
                    for(var i=0;i<=lengthC-1;i++) {
                        //console.log(countArray[i+1]);
                        if(countArray[i] == "space" && countArray[i+1] == "space" || countArray[i] == "text" && countArray[i+1] == "space" && countArray[i+2] == "text" || countArray[i] == "text" && countArray[i+1] == "space") {
                            return false;
                            break;
                        } else if (i == lengthC-1) {
                        	return true;
                        	break;
                        }   
                    }
		        }
	        }
	        else
	        {
	            return false;
	        }
    },
    getReviewOrder:function(){
    	ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/reviewOrder";
		var data="";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
            if (jqXHR.responseJSON) {
            	if(data.type!="response")
                {
            		//Hiding pickup person pop up incase of server side error
            		$("#singlePagePickupPersonPopup").modal('hide');
                	ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
                }
         } else {
        	$("#reviewOrder").html(data);
        	
        	//Adding highlight code here to get the correct price
        	var countItemsText=$("#selectedReviewOrderHighlight").html();
        	$("#selectedReviewOrderHighlight").html(countItemsText+$("#reviewOrder #totPriceWithoutRupeeSymbol").text());
        	//added for tealium
  		  $("#checkoutPageName").val("Review Order");
  	       tealiumCallOnPageLoad();
        	//START:Code to show strike off price
    		$("#off-bag").show();

    		$("li.price").each(function(){
    				if($(this).find(".off-bag").css("display") === "block"){
    					$(this).find("span.delSeat").addClass("delAction");
    				}
    				else{
    					$(this).find("span.delSeat").removeClass("delAction");
    				}
    			});
    		//END:Code to show strike off price
        	if($('body').find('a.cart_move_wishlist').length > 0){
        	$('a.cart_move_wishlist').popover({ 
        		html : true,
        		content: function() {
        			return $('.add-to-wishlist-container').html();
        		}
        	});
        	}
			ACC.singlePageCheckout.showAccordion("#reviewOrder");
			ACC.singlePageCheckout.ReviewPriceAlignment();
        	$(window).on("resize", function() {
        		ACC.singlePageCheckout.ReviewPriceAlignment();
        	});
			
         }
        });
				
		xhrResponse.always(function() {
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
    },
	processError: function(showElementId,jsonResponse){
		if(jsonResponse.type=="error")
		{
			//$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
			$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+jsonResponse.displaymessage+"</span>");
			$(showElementId).show();
		}
		if(jsonResponse.type=="errorCode")
		{
			$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+ACC.singlePageCheckout.ajaxErrorMessages(jsonResponse.displaymessage)+"</span>");
			$(showElementId).show();
		}
		if(jsonResponse.type=="redirect")
		{
			window.location.href=ACC.config.encodedContextPath+jsonResponse.url;
		}
	},
	processConfirm: function(showElementId,jsonResponse,addressId,selectedPincode,isNew,element){
		if(jsonResponse.type=="confirm")
		{
			if(showElementId=="#addressMessage")
				{
				document.getElementById('singlePageAddressPopup').style.display = "none";
				document.getElementById('confirmOverlay').style.display = "block";
				 document.getElementById('confirmBox').style.display = "block";
				 
				 $( "#exConfirmYes" ).click(function() {
					 $("#contExchnage").val("disableExchange");
					 $("#contExchnageAddEdit").val("disableExchange");
					 if(!ACC.singlePageCheckout.getIsResponsive() && isNew=="editAddress" )
					 {
						 ACC.singlePageCheckout.postEditAddress(element);
					 }
					 else if(!ACC.singlePageCheckout.getIsResponsive() && isNew=="addAddress" )
						 {
						 ACC.singlePageCheckout.postAddAddress(element);
						 }
				 else
					 {
					 ACC.singlePageCheckout.removeExchangeFromCart();
					 }
					 //In case of Continue Without Exchange show next thing
					 //document.getElementById('singlePageAddressPopup').style.display = "block";
					 document.getElementById('confirmOverlay').style.display = "none";
					 document.getElementById('confirmBox').style.display = "none";
					});
				 
				 $( "#exConfirmNo" ).click(function() {
					 $("#contExchnage").val("");
					 $("#contExchnageAddEdit").val("");
					$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+jsonResponse.displaymessage+"</span>");
						$(showElementId).show();
						ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId="";
	            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
					 document.getElementById('confirmOverlay').style.display = "none";
					 document.getElementById('confirmBox').style.display = "none";
					 document.getElementById('singlePageAddressPopup').style.display = "block";
					 
					});
				}
			
			else
				{
			document.getElementById('confirmOverlay').style.display = "block";
			 document.getElementById('confirmBox').style.display = "block";
			 $( "#exConfirmYes" ).click(function() {
				 $("#contExchnage").val("disableExchange");
				 $("#contExchnageAddEdit").val("disableExchange");
				 if(!ACC.singlePageCheckout.getIsResponsive())
				 {
			 ACC.singlePageCheckout.proceedOnAddressSelection(showElementId,addressId);
				 }
			 else
				 {
				 ACC.singlePageCheckout.removeExchangeFromCart();
				 }
				
				 document.getElementById('confirmOverlay').style.display = "none";
				 document.getElementById('confirmBox').style.display = "none";
				});
			 
			 $( "#exConfirmNo" ).click(function() {
				 $("#contExchnage").val("");
				 $("#contExchnageAddEdit").val("");
				$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+jsonResponse.displaymessage+"</span>");
					$(showElementId).show();
					ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId="";
            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
				 document.getElementById('confirmOverlay').style.display = "none";
				 document.getElementById('confirmBox').style.display = "none";
				 
				});
			 
			//$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
			
		}
		
	}
	},
removeExchangeFromCart : function (){
		

    	ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/removeExchangeFromCart";
		var data="";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		//do something
            }});
        
        xhrResponse.always(function(){

        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	
	removeCartItem : function(element,clickFrom) {			
		ACC.singlePageCheckout.showAjaxLoader();
		var productId;
			if(clickFrom=="removeItem")
			{
					var entryNumber1 = $(element).attr('id').split("_");
					var entryNumber = entryNumber1[1];
					var entryUssid = entryNumber1[2];
					var divId = "entryItemReview"+entryNumber;
					productId = $("#"+divId).find('#product').val();
			}
			if(clickFrom=="addItemToWl")
    		{
				var entryNumber = $("#entryNo").val();
    		}
			//tealium call for remove 
    		if(typeof(utag)!='undefined')
			{
				utag.link({
					link_text  : 'remove_from_review_order' , 
					event_type : 'remove_from_review_order',
					product_id :  productId
				});
			}
			var url=ACC.config.encodedContextPath + "/checkout/single/removereviewcart";
			var data="entryNumber="+entryNumber;
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	        	if (jqXHR.responseJSON) {
	        		if(data.type!="response")
	                {
	        			ACC.singlePageCheckout.processError("#reviewOrderMessage",data);
	                }
	            } 
	        	else {
	        		if(clickFrom=="removeItem")
	        		{
	        			ACC.product.addToBagFromWl(entryUssid,false);
	        			$("#reviewOrder").html(data);
	        			//tealium call for remove 
		        		if(typeof(utag)!='undefined')
						{
							utag.link({
								link_text  : 'remove_from_review_order' , 
								event_type : 'remove_from_review_order',
								product_id :  entryNumber
							});
						}
	        		}
	        		if(clickFrom=="addItemToWl")
	        		{
	        			$("#reviewOrder").html(data);
	        		}
	        		//START:Code to show strike off price
	        		$("#off-bag").show();

	        		$("li.price").each(function(){
	        				if($(this).find(".off-bag").css("display") === "block"){
	        					$(this).find("span.delSeat").addClass("delAction");
	        				}
	        				else{
	        					$(this).find("span.delSeat").removeClass("delAction");
	        				}
	        			});
	        		//END:Code to show strike off price
//	        		var entryNumbers=$("#entryNumbersId").val();
//	        		var removeEntryNo=entryNumber+"#";
//	        		$("#entryNumbersId").val(entryNumbers.replace(removeEntryNo,""));
	        		
//	        		//Updating delivery options based on item removed from review order page at client side
//	        		var matchingElements=[];
//	        		var latestEntryNumbers="";
//	        		$.each($("li.item.delivery_options"),function(i,li_deloptions){
//	        			$.each($("li.item.review_order_li"),function(index,li_revieworder){
//		        			if($(li_deloptions).attr("data-ussid")==$(li_revieworder).attr("data-ussid"))
//		        			{
//		        				matchingElements.push(li_deloptions);
//		        			}
//		        		});
//	        		});
//	        		$( "li.item.delivery_options" ).not(matchingElements).remove();
//	        		
//	        		//Updating entry number id's present in the cart delivery options at present
//	        		//Note: Entry number might not be correct at client side for delivery options as entry number is subject to change on removal.
//	        		//But it should not cause 
//	        		$.each($("li.item.delivery_options"),function(index,li_deloptions){
//	        			latestEntryNumbers=latestEntryNumbers+$(li_deloptions).attr("data-entryNumber")+"#";
//	        		});
//	        		$("#entryNumbersId").val(latestEntryNumbers);
	        		
	        		//ACC.singlePageCheckout.getSelectedDeliveryModes("");
	        		ACC.singlePageCheckout.getDeliverOptionsPage("removeCartItem");
	            }
			});
	        
	        xhrResponse.always(function(){
//	        	if(clickFrom=="addItemToWl")
//        		{
//	        		forceUpdateHeader();
//        		}
	        	ACC.singlePageCheckout.hideAjaxLoader();
	        });
	},
	//Function used to refresh delivery options page once an item is removed in review order
	getDeliverOptionsPage:function(callFrom)
	{
		var url=ACC.config.encodedContextPath + "/checkout/single/choose?isCallAfterRemoveCartItem=true";
		var data="";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type!="response")
                {
        			ACC.singlePageCheckout.processError("#reviewOrderMessage",data);
                }
            } 
        	else {
        		$("#choosedeliveryMode").html(data);
        		
        		ACC.singlePageCheckout.getSelectedDeliveryModes(callFrom);
        	}
        	
        	//Resetting voucher on removal of cart item, we are doing it twice once in proceedOnAddressSelection,getDeliverOptionsPage
    		var couponCode=$("#couponFieldId").val();
    		if(couponCode!="")
    		{
    			//Code is in marketplacecheckoutadon.js
    			resetAppliedCouponFormOnRemoval();
    		}
        });
        
	},
	addToWishlistForReviewCart:function(ussid,productCode,alreadyAddedWlName)
	{
			var wishName = "";
			var sizeSelected=true;
			
			if (wishListList == "") {
				wishName = $("#defaultWishName").val();
			} else {
				wishName = wishListList[$("#hidWishlist").val()];
			}
			
			
			if(wishName==""){
				var msg=$('#wishlistnotblank').text();
				$('#addedMessage').show();
				$('#addedMessage').html(msg);
				return false;
			}
		    if(wishName==undefined||wishName==null){
		    	if(alreadyAddedWlName!=undefined || alreadyAddedWlName!=""){
		    		if(alreadyAddedWlName=="[]"){
		    			$("#wishlistErrorId").html("Please select a wishlist");
		    		}
		    		else{
		    			alreadyAddedWlName=alreadyAddedWlName.replace("[","");
		    			alreadyAddedWlName=alreadyAddedWlName.replace("]","");
		    			$("#wishlistErrorId").html("Product already added in your wishlist "+alreadyAddedWlName);
		    		}
		    		$("#wishlistErrorId").css("display","block");
		    	}
		    	return false;
		    }
		   	
			$("#wishlistErrorId").css("display","none");
		    
		    
			var url = ACC.config.encodedContextPath + "/p"+ "-addToWishListInPDP";
			var data = 'wish='+wishName 
						    +'&product='+ productCode
						    +'&ussid='+ ussid 
						    +'&sizeSelected='+ sizeSelected;

			var entryNo = $("#entryNo").val();

			var productcodearray =[];
				productcodearray.push(productCode);
				
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	        	
				if (data == true) {
					
					$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);

					ACC.singlePageCheckout.removeCartItem("","addItemToWl");
					
					if(typeof(utag)!='undefined')
					{
						utag.link({
							link_text: 'review_order_add_to_wishlist' , 
							event_type : 'review_order__to_wishlist', 
							product_sku_wishlist : productcodearray
						});
					}
				}
	        });			
			//$('a.wishlist#wishlist').popover('hide');
	},
	
	validateCartForPayment:function()
	{
		var url = ACC.config.encodedContextPath + "/checkout/single/validatePayment";
		var data = "";			
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
		return xhrResponse;
	},
	proceedWithPaymentForResponsive:function(paymentMode,savedOrNew,radioId,callFromCvv){
		ACC.singlePageCheckout.showAjaxLoader();
		//function call to validate payment before proceeding
		var xhrValidateResponse=ACC.singlePageCheckout.validateCartForPayment();
		xhrValidateResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
		xhrValidateResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type!="response")
                {
        			ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
            		ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
                }
        		else if(data.type=="response" && data.validation=="success")
    			{
        			/*if(!ACC.singlePageCheckout.getIsResponsive())
        			{
	        			//$("#orderDetailsSectionId").html(data);
	        			$("#totalWithConvField").html(data.totalPrice);
	        			$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice);
	    	        	
	    				$("#selectedReviewOrderDivId").show();
	    	        	ACC.singlePageCheckout.showAccordion("#makePaymentDiv");
	    	      	}*/
        			//If data.validation=="success" set prePaymentValidationDone to true
        			ACC.singlePageCheckout.mobileValidationSteps.prePaymentValidationDone=true;
        			
        			//Function to re-create order totals section inorder to take delivery mode specific promotion into account
//        			var xhrResponse=ACC.singlePageCheckout.getOrderTotalsTag();
//        			xhrResponse.fail(function(xhr, textStatus, errorThrown) {
//        				console.log("ERROR:"+textStatus + ': ' + errorThrown);
//        			});
//        	        
//        			xhrResponse.done(function(data, textStatus, jqXHR) {
//        	        	if (jqXHR.responseJSON) {
//        	        		if(data.type!="response")
//        	                {
//        	        			ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
//		                		ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
//        	                }
//        	            }
//        	        	else
//    	        		{	//Updating order total div
//        	        		$("#orderTotalSpanId").html(data);
//        	        		//Open payment mode form//apply promotion is called within
//                    		ACC.singlePageCheckout.viewPaymentModeFormOnSelection(paymentMode);
//    	        		}
//        	        });
        			
        			//Populating sub-total which is subject to change on order item removal
        			$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice.formattedValue);
        			if(savedOrNew=="savedCard")
        			{
        				////Code to be executed if a saved card is selected
        				ACC.singlePageCheckout.paymentOnSavedCardSelection(paymentMode,savedOrNew,radioId,callFromCvv);
        			}
        			else
        			{
        				//Code to be executed if a new card is selected
        				//Open payment mode form//apply promotion is called within
        				ACC.singlePageCheckout.viewPaymentModeFormOnSelection(paymentMode);
        			}
            		
            		//If delivery modes will be changed all the validation flags will be reset.
            		ACC.singlePageCheckout.attachEventToResetFlagsOnDelModeChange();
            		
	    	        //Calling the below methods to populate the latest shipping address(These methods are in marketplacecheckoutaddon.js)
//        			populateAddress();
//	    	        populateAddressEmi();
    			}
        		else
    			{
        			console.log("ERROR:Cart validation failed, Redirectin to cart");
        			window.location.href=ACC.config.encodedContextPath+"/cart";
    			}
            }
        });
        
		xhrValidateResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	
	proceedToPayment:function(element){
		ACC.singlePageCheckout.showAjaxLoader();
		//function call to validate payment before proceeding
		var xhrValidateResponse=ACC.singlePageCheckout.validateCartForPayment();
		xhrValidateResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
		//tealium page name addition
		$("#checkoutPageName").val("Payment Options");
		  tealiumCallOnPageLoad();
		xhrValidateResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type!="response")
                {
        			ACC.singlePageCheckout.processError("#reviewOrderMessage",data);
                }
        		else if(data.type=="response" && data.validation=="success")
    			{
        			//Function to re-create order totals section inorder to take delivery mode specific promotion into account
//        			var xhrResponse=ACC.singlePageCheckout.getOrderTotalsTag();
//        			xhrResponse.fail(function(xhr, textStatus, errorThrown) {
//        				console.log("ERROR:"+textStatus + ': ' + errorThrown);
//        			});
//        	        
//        			xhrResponse.done(function(data, textStatus, jqXHR) {
//        	        	if (jqXHR.responseJSON) {
//        	        		if(data.type!="response")
//        	                {
//        	        			ACC.singlePageCheckout.processError("#reviewOrderMessage",data);
//        	                }
//        	            }
//        	        	else
//    	        		{	//Updating order total div
//        	        		$("#orderTotalSpanId").html(data);
//        	        		$("#selectedReviewOrderDivId").show();
//            				callOnReady();//This method is in showAddPaymentMethod.jsp
//    	        		}
//        	        });
        			
        			$("#selectedReviewOrderDivId").show();
    				callOnReady();//This method is in showAddPaymentMethod.jsp
        			
    				//Populating sub-total which is subject to change on order item removal
    				$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice.formattedValue);
        			/*//$("#orderDetailsSectionId").html(data);
        			$("#totalWithConvField").html(data.totalPrice);*/
    	        	
    	        	ACC.singlePageCheckout.showAccordion("#makePaymentDiv");
	    	        //Calling the below methods to populate the latest shipping address(These methods are in marketplacecheckoutaddon.js)
//	    	        populateAddress();
//	    	        populateAddressEmi();
    			}
        		else
    			{
        			console.log("ERROR:Cart validation failed, Redirectin to cart");
        			window.location.href=ACC.config.encodedContextPath+"/cart";
    			}
            }
        });
        
		xhrValidateResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
		if(typeof utag !="undefined")  {
			utag.link({
		        link_text: "reviewOrder_proceed_clicked",
		        event_type: "proceed_button_clicked"
		    })
	      }
	},
	
	getOrderTotalsTag:function()
	{
		var url = ACC.config.encodedContextPath + "/checkout/single/payment/orderDetails";
		var data = "";			
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
		return xhrResponse;
	},
	
	showAjaxLoader:function(){
		var staticHost = $('#staticHost').val();
		if($("#no-click,.loaderDiv").length==0)
		{
			//Below 2 lines for adding spinner
			//$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			//$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
			$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
		}
	},
	
	hideAjaxLoader:function(){
		$("#no-click,.loaderDiv").remove();
	},
	
	ReviewPriceAlignment:function(){
			$("#reviewOrder.cart.wrapper .product-block li.item").each(function(){
				if($(this).find("ul.desktop>li.price").css("position")=="absolute"){
					var price_height=$(this).find("ul.desktop>li.price").height() + 20;
					$(this).find(".cart-product-info").css("padding-bottom",price_height+"px");
					var price_top = $(this).find(".cart-product-info").height() + 8;
					$(this).find("ul.desktop>li.price").css("top",price_top+"px");
					var qty_top = price_top + $(this).find("ul.desktop>li.price").height() + 18;
					$(this).find("ul.desktop>li.qty").css("top",qty_top+"px");
				}
				else{
					$(this).find(".cart-product-info").css("padding-bottom","0px");
					$(this).find("ul.desktop>li.price").css("top","auto");
					$(this).find("ul.desktop>li.qty").css("top","auto");
				}
			});
	},
	
	
	
//	mobileAccordion:function(){
//		$("#address-change-link").on("click", function(){
//			$(this).parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").show();
//			//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
//			$(this).hide();
//		});
//		$("#delivery-mode-change-link").on("click", function(){
//			var entryNumbersId=$("#entryNumbersId").val();		            	
//	    	var entryNumbers=entryNumbersId.split("#");
//	    	for(var i=0;i<entryNumbers.length-1;i++)
//	    	{
//	    		$("input:radio[name='"+entryNumbers[i]+"']").parent("li").show();        	
//	    	}
//		});
//		
//		/*$(".cancel-mobile").on("click", function(){
//			$(this).parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").hide();
//			$(this).parents(".checkout_mobile_section").find(".change-mobile").show();
//			$(this).hide();
//		});*/
//	},
	getIsResponsive:function(){
		var winWidth=$(window).width();
		return winWidth<768?true:false;
	},
	formValidationErrorCount:0,
	isSlotDeliveryAndCncPresent:false,
/****************MOBILE STARTS HERE************************/
//-----------------------------COMMENTS ON mobileValidationSteps object-----------------------------//
//	1.isAddressSaved		:	Used to track if new address has been saved in cartModel for responsive
//	2.isAddressSet		:	Used to track if existing address has been set as delivery address in cartModel for responsive
//	3.isDeliveryModeSet	:	Used to track if delivery mode has been set in cartModel for responsive
//	4.saveNewAddress		:	Used to track if new address has to be saved in cartModel for responsive
//	5.selectedAddressId	:	Used to track the selected address id for responsive
//	6.isInventoryReserved	:	Used to track if inventory has been reserved for responsive
//	7.isScheduleServiceble:	Used to track scheduled delivery is available for responsive
//	8.paymentModeSelected:	Used to track the selected payment mode for responsive
//	9.prePaymentValidationDone:	Used to track cart validation before payment is made
//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	mobileValidationSteps:{
		isAddressSaved:false,
		isAddressSet:false,
		isDeliveryModeSet:false,
		saveNewAddress:false,
		selectedAddressId:"",
		isInventoryReserved:false,
		isScheduleServiceble:false,
		paymentModeSelected:"",
		prePaymentValidationDone:false
	},

	resetValidationSteps:function(){
		ACC.singlePageCheckout.mobileValidationSteps.isAddressSaved=false;
		ACC.singlePageCheckout.mobileValidationSteps.isAddressSet=false;
		ACC.singlePageCheckout.mobileValidationSteps.isDeliveryModeSet=false;
		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId="";
		ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved=false;
		ACC.singlePageCheckout.mobileValidationSteps.isScheduleServiceble=false;
		ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected="";
		ACC.singlePageCheckout.mobileValidationSteps.prePaymentValidationDone=false;
	},
	
	resetPaymentModes:function()
	{
		$("li.paymentModeMobile").removeClass("active");
		$("#payment_form")[0].reset();
		$("#debit_payment_form")[0].reset();
		$("#card").css("display","none");
		$("#cardDebit").css("display","none");
		$("li#netbanking").css("display","none");
		$("li#emi").css("display","none");
		$("li#COD").css("display","none");
		$("#MRUPEE").css("display","none");
		$("#make_saved_cc_payment_up").css("display","none");
		$("#make_cc_payment_up").css("display","none");
		$("#make_dc_payment_up").css("display","none");
		$("#make_saved_dc_payment_up").css("display","none");
		$("#make_nb_payment_up").css("display","none");
		$("#make_emi_payment_up").css("display","none");
		$("#paymentButtonId_up").css("display","none");
		$("#make_mrupee_payment_up").css("display","none");
		//Below is for Saved Card
		$(".cvvValdiation").val("");
		$("input:radio[name=debitCards]").prop("checked",false);
		$("input:radio[name=creditCards]").prop("checked",false);
	},
	
	resetPaymentModesOnSavedCardSelection:function(paymentMode)
	{
		$("li.paymentModeMobile").removeClass("active");
		$("#payment_form")[0].reset();
		$("#debit_payment_form")[0].reset();
		$("#card").css("display","none");
		$("#cardDebit").css("display","none");
		$("li#netbanking").css("display","none");
		$("li#emi").css("display","none");
		$("li#COD").css("display","none");
		$("#MRUPEE").css("display","none");
		if(paymentMode!="Credit Card")
		{
			$("#make_saved_cc_payment_up").css("display","none");
		}
		$("#make_cc_payment_up").css("display","none");
		$("#make_dc_payment_up").css("display","none");
		if(paymentMode!="Debit Card")
		{
			$("#make_saved_dc_payment_up").css("display","none");
		}
		$("#make_nb_payment_up").css("display","none");
		$("#make_emi_payment_up").css("display","none");
		$("#paymentButtonId_up").css("display","none");
		$("#make_mrupee_payment_up").css("display","none");
	},
	
	//Used to get blank popup for pickup person form on clicking on cnc store for mobile
	getPickUpPersonPopUpMobile:function(){
		var isCncPresent=$("#selectDeliveryMethodFormMobile #isCncPresentInSinglePageCart").val();
    	if(isCncPresent=="true")
    	{
    		var htmlPopulated=$("#singlePagePickupPersonPopup span#modalBody").attr("data-htmlPopulated");
    		if(htmlPopulated=="NO")
        	{
    			ACC.singlePageCheckout.showAjaxLoader();
				var url=ACC.config.encodedContextPath + "/checkout/single/pickupPerson/popup";
				var data="";
				var xhrPickupPersonResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);;
				xhrPickupPersonResponse.fail(function(xhr, textStatus, errorThrown) {
						console.log("ERROR:"+textStatus + ': ' + errorThrown);
				});
			        
				xhrPickupPersonResponse.done(function(data) { 
					//Populating popup
					var elementId="singlePagePickupPersonPopup";
            		ACC.singlePageCheckout.modalPopup(elementId,data);
					$("#singlePagePickupPersonPopup #modalBody").data("htmlPopulated","YES");
				});
				
				xhrPickupPersonResponse.always(function(data) { 
					ACC.singlePageCheckout.hideAjaxLoader();
				});
        	}
    		else
    		{
        		$("#singlePagePickupPersonPopup").modal('show');
    		}
    	}
	},
	
	
	
	getMobileAddAddress:function(){
		var formAlreadyLoaded=$(".new-address-form-mobile").attr("data-loaded");
		if(ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected!="")
		{//Will enter if an user selects new address after selecting a payment mode
			ACC.singlePageCheckout.resetValidationSteps();
			ACC.singlePageCheckout.resetPaymentModes();
		}
		//When ever a user selects a new address this flag should be set to true
		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=true;
		if(formAlreadyLoaded=="false")
		{
			ACC.singlePageCheckout.showAjaxLoader();
			var url=ACC.config.encodedContextPath + "/checkout/single/new-address";
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data) {
	        	//Unchecking the radio button of saved addresses
	        	$('#selectAddressFormMobile input[name=selectedAddressCode]').prop('checked', false);
	        	$(".mobile_add_address").addClass("form_open");
				$(".new-address-form-mobile").html(data);
				$(".new-address-form-mobile").attr("data-loaded","true");
				$(".new-address-form-mobile").slideDown();
				$("#singlePageAddressPopup #modalBody").html('');
				$("#newAddressButton").hide();
				ACC.singlePageCheckout.attachOnPincodeKeyUpEvent();
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else
		{
			ACC.singlePageCheckout.showAjaxLoader();
			$('#selectAddressFormMobile input[name=selectedAddressCode]').prop('checked', false);
        	$(".mobile_add_address").addClass("form_open");
			$(".new-address-form-mobile").slideDown();
			ACC.singlePageCheckout.hideAjaxLoader();
		}        
		return false;	
	},
	
	checkPincodeServiceabilityForRespoinsive:function(selectedPincode,addressId,isNew)
	{
		if(ACC.singlePageCheckout.mobileValidationSteps.isAddressSet || ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved)
		{
			ACC.singlePageCheckout.resetValidationSteps();
    		ACC.singlePageCheckout.resetPaymentModes();
		}
		if(addressId!="")
		{
			$("#radio_mobile_"+addressId).prop("checked", true);
		}
		else
		{
			//$("input[name=selectedAddressCodeMobile]").prop("checked", false);
		}
		if(selectedPincode!=null && selectedPincode != undefined && selectedPincode!=""){	
			 var url= ACC.config.encodedContextPath + "/checkout/single/delModesOnAddrSelect/"+selectedPincode;
			 var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
			  xhrResponse.fail(function(xhr, textStatus, errorThrown) {
					console.log("ERROR:"+textStatus + ': ' + errorThrown);
				});
				xhrResponse.done(function(response, textStatus, jqXHR) {
					//Hiding address pincode serviceability failure error messages
	            	$("#selectedAddressMessageMobile").hide();
	            	$("#addressMessage").hide();
	            	if(!isNew)
                	{
	            		//Remove new address radio nutton check
	            		$("div.mobile_add_address.form_open").removeClass("form_open");
	            		$(".new-address-form-mobile").slideUp();
                	}
					if (jqXHR.responseJSON) {
		                if(response.type!="response" && response.type!="confirm")
		                {
		                	if(isNew)
		                	{
		                		ACC.singlePageCheckout.processError("#addressMessage",response);
		                		ACC.singlePageCheckout.scrollToDiv("addressMessage",100);
		                	}
		                	else
	                		{
		                		//Remove new address radio nutton check
		                		$("div.mobile_add_address.form_open").removeClass("form_open");
		                		ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",response);
		                		ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
	                		}
		                	
		                }
		                //For Confirm Box TPR-1083
		                else if(response.type=="confirm")
		                {
		                	
		                		ACC.singlePageCheckout.processConfirm("#selectedAddressMessageMobile",response,addressId,selectedPincode,isNew,"");
	           
		                }
		            } else {
		            	if(isNew)
	                	{
		            		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId="";
		            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=true;
	                	}
		            	else
		            	{
		            		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId=addressId;
		            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
		            	}
		            	$("#choosedeliveryModeMobile").html(response);
		            	
//		            	var entryNumbersId=$("#entryNumbersId").val();		            	
//		            	var entryNumbers=entryNumbersId.split("#");
//		            	for(var i=0;i<entryNumbers.length-1;i++)
//		            	{
//		            		$("input:radio[name='"+entryNumbers[i]+"']").each(function(i,obj){
//			            		if(!$(obj).is(':checked'))
//			            		{
//			            			$(obj).parent("li").hide();
//			            		}
//			            	});
//		            	}
		            	
		            	
		            	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
		            }
		 		});
		}
		
	},
	
	changeAddress:function(element){
		//$("#address-change-link").on("click", function(){
			$(element).parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").show();
			//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
			$(element).hide();
			if(typeof utag !="undefined")  {
				utag.link({
			        link_text: "change_link_clicked",
			        event_type: "change_link_clicked"
			    })
		      }
	    //});
	},
	
	changeDeliveryMode:function(element){
//		var entryNumbersId=$("#entryNumbersId").val();
//		var entryNumbers=entryNumbersId.split("#");
//		for(var i=0;i<entryNumbers.length-1;i++)
//		{
//			//$("input:radio[name='"+entryNumbers[i]+"']").parent("li").show();
//			$("input:radio[name='"+entryNumbers[i]+"']").each(function(i,obj){
//        		if(!$(obj).is(':checked'))
//        		{
//        			$(obj).parent("li").show();
//        		}
//        	});
//		}
		if(typeof utag !="undefined")  {
			utag.link({
		        link_text: "change_link_clicked",
		        event_type: "change_link_clicked"
		    })
	      }
		//$(".hideDelModeMobile").show();
		$(".hideDelModeMobile").removeAttr('disabled');
		$(".hideDelModeMobile").css("opacity","1");
		$(".hideDelModeMobile").css("pointer-events","auto");
		$(element).hide();
	},
	//Method to reset validation flags and payment mode form on delivery mode change after payment mode is selected(For responsive)
	attachEventToResetFlagsOnDelModeChange:function()
	{
		$("#choosedeliveryModeMobile input[type=radio]").on("change",function(){
    		ACC.singlePageCheckout.resetValidationSteps();
    		ACC.singlePageCheckout.resetPaymentModes();
    	});
	},
	
	scrollToDiv:function(id,offset){
	      // Scroll
	    $('html,body').animate({
	        scrollTop: $("#"+id).offset().top-offset},
	        'slow');
	},
	
	attachOnPincodeKeyUpEvent:function()
	{	
		$('.address_postcode').on('keyup',function(){
			$("#addressMessage").html("");
			var pincode=$('.address_postcode').val();
			$(".otherLandMarkError").html("");
			var regPostcode = /^([1-9])([0-9]){5}$/;
			if(pincode.length>=6)
			{
				if(regPostcode.test(pincode) == false)
				{
					 $("#addresspostcodeError").show();
				     $("#addresspostcodeError").html("<p>Please enter a valid pincode</p>");
				}
				else
				{
					$("#addresspostcodeError").hide();
					ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive(pincode,"",true);
				}
			}
				
		});
	},
	
	saveAndSetNewDeliveryAddress:function()
	{
		var validationResult=ACC.singlePageCheckout.validateAddressForm();
		if(validationResult!=false)
		{
			ACC.singlePageCheckout.showAjaxLoader();
			try{
				var url=ACC.config.encodedContextPath + "/checkout/single/new-address-responsive";
				var data=$("#selectAddressFormMobile form#addressForm").serialize().replace(/\+/g,'%20');
				
				ACC.singlePageCheckout.showAjaxLoader();
				var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
		        
		        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
					console.log("ERROR:"+textStatus + ': ' + errorThrown);
					data={displaymessage:"Network error occured",type:"error"};
					ACC.singlePageCheckout.hideAjaxLoader();
					ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
                	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
				});
		        
		        xhrResponse.done(function(data, textStatus, jqXHR) {
		        	if (jqXHR.responseJSON) {
		                if(data.type!="response")
		                {
		            		ACC.singlePageCheckout.resetPaymentModes();
		                	ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
		                	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
		                }
		                if(data.type=="response")
		                {
		                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSaved=data.isAddressSaved=="true"?true:false;
		                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSet=data.isAddressSet=="true"?true:false;
		                	ACC.singlePageCheckout.getDeliveryAddresses();
		                	$("#address-change-link").show();
		                }
		            }
				});
		        
		        xhrResponse.always(function(){
				});
			}
			catch(e)
			{
				console.log("ERROR:"+e);
				ACC.singlePageCheckout.hideAjaxLoader();
			}
	        return true;
		}
		return false;
	},
	
	setDeliveryAddress:function()
	{
		$("#radio_mobile_"+ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId).prop("checked", true);
		a = $("form#selectAddressFormMobile input[name=selectedAddressCode]:checked").val();
	    
		if (null == a || "undefined" == a)
	        return $("#emptyAddressMobile").show(),
	        !1;
	    
	    var data=$("form#selectAddressFormMobile").serialize();
	    var url=ACC.config.encodedContextPath + $("#selectAddressFormMobile").attr("action");
	    var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
      
	    xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
			data={displaymessage:"Network error occured",type:"error"};
			ACC.singlePageCheckout.hideAjaxLoader();
			ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
        	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
		});  
           
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
                if(data.type!="response")
                {
                	ACC.singlePageCheckout.resetPaymentModes();
                	ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
                	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
                }
                if(data.type=="response")
                {
                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSet=data.isAddressSet=="true"?true:false;
                }
            }
		});
        
        xhrResponse.always(function(){
		});
		
		return false;
	},
	//function called when payment mode is selected in responsive.
	onPaymentModeSelection:function(paymentMode,savedOrNew,radioId,callFromCvv)
	{
		var formValidationSuccess=true;
		ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected=paymentMode;
		if(ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress && !ACC.singlePageCheckout.mobileValidationSteps.isAddressSet)
		{
			formValidationSuccess=ACC.singlePageCheckout.saveAndSetNewDeliveryAddress();
			if(!formValidationSuccess)
			{
				//Removing payment mode selection incase of address form validation failure
				$("#selectAddressFormMobile .clickableDivMobile,input,textarea,select").on("click.formValidationFailed,focus.formValidationFailed,change.formValidationFailed",function(){
					ACC.singlePageCheckout.resetPaymentModes();
					$("#selectAddressFormMobile .clickableDivMobile,input,textarea,select").off("click.formValidationFailed,focus.formValidationFailed,change.formValidationFailed");
				});
	    		data={displaymessage:"clientSideAddressFormValidationFailed",type:"errorCode"}
	    		ACC.singlePageCheckout.processError("#addressMessage",data);
	        	ACC.singlePageCheckout.scrollToDiv("addressMessage",100);
	        	return false;
			}
		}
		else if(ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId!="" && !ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress && !ACC.singlePageCheckout.mobileValidationSteps.isAddressSet)
		{
			ACC.singlePageCheckout.showAjaxLoader();
			ACC.singlePageCheckout.setDeliveryAddress();
		}
		if(formValidationSuccess && !ACC.singlePageCheckout.mobileValidationSteps.isDeliveryModeSet && !ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved)
		{	
			ACC.singlePageCheckout.showAjaxLoader();
			var isPincodeRestrictedPromoPresent="";
			if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
			{
				isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
			}
			var url=$("#selectDeliveryMethodFormMobile").attr("action")+"?locRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
			var data=$("#selectDeliveryMethodFormMobile").serialize();
		    var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
	      
		    xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
				data={displaymessage:"Network error occured",type:"error"};
				ACC.singlePageCheckout.hideAjaxLoader();
				ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
	        	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
			});  
	           
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	        	if (jqXHR.responseJSON) {
	                if(data.type!="response")
	                {
	                	ACC.singlePageCheckout.resetPaymentModes();
	                	ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
	    	        	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
	                }
	                if(data.type=="response")
	                {
	                	ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved=data.isInventoryReserved=="true"?true:false;
	                	ACC.singlePageCheckout.mobileValidationSteps.isScheduleServiceble=data.isScheduleServiceble=="true"?true:false;
	                	ACC.singlePageCheckout.mobileValidationSteps.isDeliveryModeSet=data.isDeliveryModeSet=="true"?true:false;
	                	if(ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved)
	                	{
	                		//Validate payment in responsive
	                		ACC.singlePageCheckout.proceedWithPaymentForResponsive(paymentMode,savedOrNew,radioId,callFromCvv);
	                	}
	                	if(ACC.singlePageCheckout.mobileValidationSteps.isScheduleServiceble)
	                	{
	                		//TODO Toast for slot delivery 
	                		ACC.singlePageCheckout.showSnackBar();
	                		
	                	}
	                }
	                
	            }
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else if(formValidationSuccess && ACC.singlePageCheckout.mobileValidationSteps.isDeliveryModeSet && ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved && ACC.singlePageCheckout.mobileValidationSteps.prePaymentValidationDone)
    	{
			if(savedOrNew=="savedCard")
			{
				////Code to be executed if a saved card is selected
				ACC.singlePageCheckout.paymentOnSavedCardSelection(paymentMode,savedOrNew,radioId,callFromCvv);
			}
			else
			{
				//Code to be executed if a new card is selected
				//Open payment mode form//apply promotion is called within
				ACC.singlePageCheckout.viewPaymentModeFormOnSelection(paymentMode);
			}
    	}
	},
	
	showSnackBar:function(){
		// Add the "show" class to DIV
		var x = $("#mobileSnackbar").addClass("show");
		$("#selectSnackbar").click(function(){
			ACC.singlePageCheckout.getResponsiveSlotSelectionPage();
		    
		});
		
	},
	//Method to get responsive slot delivery page
	getResponsiveSlotSelectionPage:function(){
		var htmlPopulated=$("#singlePageChooseSlotDeliveryPopup span#modalBody").attr("data-htmlPopulated");
		if(htmlPopulated=="NO")
    	{
			ACC.singlePageCheckout.showAjaxLoader();
			var url=ACC.config.encodedContextPath + "/checkout/single/slotDelivery-responsive";
			var data="";
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
	        	console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	            if (jqXHR.responseJSON) {
	            	if(data.type!="response")
	                {
	            		
	                	ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
	                }
	            } else {
	            	
	            	
	            		var elementId="singlePageChooseSlotDeliveryPopup";
	            		ACC.singlePageCheckout.modalPopup(elementId,data);
	            		$("#singlePageChooseSlotDeliveryPopup #modalBody").data("htmlPopulated","YES");
	            }
	        });
	        
	        xhrResponse.always(function() {
	        	ACC.singlePageCheckout.hideAjaxLoader();
	        });
    	}
		else
		{
			$("#singlePageChooseSlotDeliveryPopup").modal('show');
		}
	},
	//Method to open payment form in responsive
	viewPaymentModeFormOnSelection:function(paymentMode)
	{
		if(paymentMode=="Credit Card")
		{
			viewPaymentCredit();
		}
		if(paymentMode=="Debit Card")
		{
			viewPaymentDebit();
		}
		if(paymentMode=="Netbanking")
		{
			viewPaymentNetbanking();
		}
		if(paymentMode=="EMI")
		{
			viewPaymentEMI();
		}
		if(paymentMode=="COD")
		{
			viewPaymentCOD();
		}
		if(paymentMode=="MRUPEE")
		{
			viewPaymentMRupee();
		}
	},
	paymentOnSavedCardSelection:function(paymentMode,savedOrNew,radioId,callFromCvv)
	{
		$("#paymentMode").val(paymentMode);
		if(paymentMode=="Credit Card")
		{
			if($("#"+radioId).is(":checked") && callFromCvv=='true')
			{
				//Do Nothing
			}
			else{
				$("#"+radioId).prop("checked",true);
				ACC.singlePageCheckout.resetPaymentModesOnSavedCardSelection(paymentMode);
				savedCreditCardRadioChange(radioId);
			}
		}
		if(paymentMode=="Debit Card")
		{
			if($("#"+radioId).is(":checked") && callFromCvv=='true')
			{
				//Do Nothing
			}
			else{
				$("#"+radioId).prop("checked",true);
				ACC.singlePageCheckout.resetPaymentModesOnSavedCardSelection(paymentMode);
				savedDebitCardRadioChange(radioId);
			}
		}
	}
/****************MOBILE ENDS HERE************************/
}
//Calls to be made on dom ready.
$(document).ready(function(){
	var pageType=$("#pageType").val();
	if(pageType=="multistepcheckoutsummary")
	{
		var onLoadIsResponsive=ACC.singlePageCheckout.getIsResponsive();
		$(window).on("resize",function(){
			var onSizeChangeIsResponsive=ACC.singlePageCheckout.getIsResponsive();
			if(onLoadIsResponsive!=onSizeChangeIsResponsive && !ACC.singlePageCheckout.getIsResponsive())
			{
				window.location.href=ACC.config.encodedContextPath +"/checkout/single";
			}
			else if(onLoadIsResponsive!=onSizeChangeIsResponsive && ACC.singlePageCheckout.getIsResponsive())
			{
				window.location.href=ACC.config.encodedContextPath +"/checkout/single"+"?isResponsive=true";
			}
		});
		var deviceType=$("#deviceType").html();
		if(deviceType=="normal" && ACC.singlePageCheckout.getIsResponsive())
		{
			window.location.href=ACC.config.encodedContextPath +"/checkout/single"+"?isResponsive=true";
		}
		
		if(ACC.singlePageCheckout.getIsResponsive())
		{
			var defaultAddressPincode=$("#defaultAddressPincode").html();
			var defaultAddressId=$("#defaultAddressId").html();
			var defaultAddressPresent=$("#defaultAddressPresent").html();
			if(defaultAddressPresent=="true" && typeof(defaultAddressPincode)!='undefined' && typeof(defaultAddressId)!='undefined')
			{
				ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive(defaultAddressPincode.trim(),defaultAddressId.trim(),false);
			}
			if(defaultAddressPresent=="false")
			{
				$("#chooseDeliveryAddressMobileDiv").find(".mobileNotDefaultDelAddress").show();
				//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
				$("#chooseDeliveryAddressMobile .change-mobile").hide();
			}
			$("#makePaymentDiv").html("");
		}
		else
		{
			$("#makePaymentDivMobile").html("");
		}
	}
});

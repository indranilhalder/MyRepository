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
							//Below method will display correct page number.
							ACC.singlePageCheckout.carouselPageNumberDisplay(items,item,"page_count");
		                });
		              $("#address_carousel").owlCarousel({
		                items:3,
						loop: false,
						mouseDrag:($(".checkTab .addressList_wrapper .address-list").length <= 3)?false:true,
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
        		//Hide edit radio for responsive
        		ACC.singlePageCheckout.hideMobileEditRadio();
        	}
		});
        
        xhrResponse.always(function(){
			console.log("ALWAYS:");
		});
   
		return false;	
	},
	//Function used to fetch edit address form. 
	getEditAddress:function(element,event){
		/*TISPRNXIII-54*/
    	if(typeof utag !="undefined"){
			utag.link({ link_text : 'edit_address' ,event_type : 'edit_address_clicked'});
		}
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
			/*TISPRNXIII-57*/
        	if(typeof utag !="undefined"){
				utag.link({ error_type : 'address_error'});
			}
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
			//disableHideAjaxLoader will make sure that loader is not removed until CNC stores are fetched.
	        var disableHideAjaxLoader=false;
			var addressId=$(form).find(" #addressId").val();
			var isPincodeRestrictedPromoPresent=false;
			if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
			{
				isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
			}
			var url=ACC.config.encodedContextPath + "/checkout/single/edit-address/"+addressId+"?isPincodeRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
			var data=$(form).serialize().replace(/\+/g,'%20');
			ACC.singlePageCheckout.showAjaxLoader();
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
				/*TISPRNXIII-57*/
	        	if(typeof utag !="undefined"){
					utag.link({ error_type : 'address_error'});
				}
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	            if (jqXHR.responseJSON) {
	                if(data.type!="response" && data.type!="confirm")
	                {
	                	/*TISPRNXIII-57*/
	    	        	if(typeof utag !="undefined"){
	    					utag.link({ error_type : 'address_error'});
	    				}
	                	ACC.singlePageCheckout.processError("#addressMessage",data);
	                }
	                else if(data.type=="confirm")
                	{
                	ACC.singlePageCheckout.processConfirm("#addressMessage",data,"","","editAddress",element);
                	}
	            } else {
	            	ACC.singlePageCheckout.getSelectedAddress();
	                $("#choosedeliveryMode").html(data);
	                //This method will check if CNC is checked as default delivery mode. If so an ajax call is fired to fetch CNC stores. 
		        	//disableHideAjaxLoader will make sure that loader is not removed until stores are fetched.
	                disableHideAjaxLoader=ACC.singlePageCheckout.callFetchCNCStoresForWeb();
	                ACC.singlePageCheckout.showAccordion("#choosedeliveryMode");
		        	//selectDefaultDeliveryMethod();
		        	$(".click-and-collect").addClass("click-collect");
		        	$("#singlePageAddressPopup").modal('hide');
		        	ACC.singlePageCheckout.getDeliveryAddresses();
		        	$("#selectedAddressMessage").hide();
		        	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
		        	/*TISPRNXIII-55*/
		        	if(typeof utag !="undefined"){
						utag.link({ link_text : 'save_edited_address' ,event_type : 'save_edited_address_clicked'});
					}
		        	//calling tealium 
		            $("#checkoutPageName").val("Choose Your Delivery Options");
		            if(typeof utag_data !="undefined"){
		            	var checkoutDeliveryPage = "Multi Checkout Summary Page:Choose Your Delivery Options";
		            	utag_data.page_name = checkoutDeliveryPage;
		            	$("#pageName").val(checkoutDeliveryPage);
		            }
		        	//TPR-6362 |track checkout activity
		        	if(typeof (_satellite)!= "undefined") {  
		        		_satellite.track('cpj_checkout_save_address');
		        	}
		        	  if(typeof (digitalData.page.pageInfo)!= 'undefined'){
		          		digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
		          	}
	            }
	        });
	        
	        xhrResponse.always(function(){
	        	if(!disableHideAjaxLoader)
	        	{
	        		ACC.singlePageCheckout.hideAjaxLoader();
	        	}
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
			//disableHideAjaxLoader will make sure that loader is not removed until stores are fetched.
			var disableHideAjaxLoader=false;
			var isPincodeRestrictedPromoPresent=false;
			if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
			{
				isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
			}
			var url=ACC.config.encodedContextPath + "/checkout/single/new-address"+"?isPincodeRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
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
					//This method will check if CNC is checked as default delivery mode. If so an ajax call is fired to fetch CNC stores. 
		        	//disableHideAjaxLoader will make sure that loader is not removed until stores are fetched.
					disableHideAjaxLoader=ACC.singlePageCheckout.callFetchCNCStoresForWeb();
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
		        	//TPR-6362 |track checkout activity
		        	if(typeof (_satellite)!= "undefined") {  
		        		_satellite.track('cpj_checkout_save_address');
		        	}
	            }
			});
	        
	        xhrResponse.always(function(){
	        	if(!disableHideAjaxLoader)
	        	{
	        		ACC.singlePageCheckout.hideAjaxLoader();
	        	}
			});
		}
		else
		{
			data={displaymessage:"clientSideAddressFormValidationFailed",type:"errorCode"}
    		ACC.singlePageCheckout.processError("#addressMessage",data);
		}
		return false;	
	},
	//Fetch CNC stores from here for web
	callFetchCNCStoresForWeb:function()
	{
		var disableHideAjaxLoader=false;
		//fetching cnc stores if only click n collect delivery mode is present
    	var entryNumbersId=$("#selectDeliveryMethodForm #entryNumbersId").val();
		var isCncPresent=$("#selectDeliveryMethodForm #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
    	var entryNumbers=entryNumbersId.split("#");
		for(var i=0;i<entryNumbers.length-1;i++)
		{
		    if(isCncPresent && $("input[name='"+entryNumbers[i]+"']").length>0 && $('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
		    {
			    var ussid=$('input[name="deliveryMethodEntry['+entryNumbers[i]+'].sellerArticleSKU"]').val();
			    ACC.singlePageCheckout.fetchStores(entryNumbers[i],ussid,'click-and-collect','','');
			    disableHideAjaxLoader=true;
		    }
		}
	  //end of fetching cnc stores if only click n collect delivery mode is present
		return disableHideAjaxLoader;
	},
	//Function called when proceed button on delivery options page is clicked. 
	//This function will fetch slot delivery page or will proceed to review order page
	proceedOnDeliveryModeSelection:function(element){
		var entryNumbersId=$("#selectDeliveryMethodForm #entryNumbersId").val();
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
				//Hiding/Showing COD if cnc is selcted
				if(cncSelected=="true")
				{
					$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","none");
					$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","none");
				}
				else
				{
					$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","block");
					$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","block");
				}
			}
		}
		ACC.singlePageCheckout.showAjaxLoader();
		//var url=ACC.config.encodedContextPath + $("#selectDeliveryMethodForm").prop("action");
		var url=$("#selectDeliveryMethodForm").prop("action")+"?isDelModeRestrictedPromoPresent="+$("#isDelModeRestrictedPromoPresent").text();
		var data=$("#selectDeliveryMethodForm").serialize();
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
            if (jqXHR.responseJSON) {//Below block will execute if slot delivery is not present
            	ACC.singlePageCheckout.isSlotDeliveryAndCncPresent=false;
            	if(data.type!="response" && data.type!="ajaxRedirect")
                {
                	ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
                }
            	else if(data.type=="ajaxRedirect" && data.redirectString=="redirectToReviewOrder")
        		{
            		ACC.singlePageCheckout.getSelectedDeliveryModes("");
                	if(isCncPresent=="true" && cncSelected=="true")
                	{//If cnc selected show pickup person pop up
                		ACC.singlePageCheckout.hidePickupDetailsErrors();
                		$("#singlePagePickupPersonPopup").modal('show');
                	}
                	else
                	{//get review order
                		ACC.singlePageCheckout.getReviewOrder();
                	}
        		}
            } else {//Below block will execute if slot delivery is present
            	ACC.singlePageCheckout.getSelectedDeliveryModes("");
            	if(isCncPresent=="true" && cncSelected=="true")
            	{
            		ACC.singlePageCheckout.hidePickupDetailsErrors();
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
        if(typeof utag !="undefined")  {
    		utag.link({
    	        link_text: "deliveryMode_proceed_clicked",
    	        event_type: "proceed_button_clicked"
    	    })
          } 
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
        var isPincodeRestrictedPromoPresent=false;
		if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
		{
			isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
		}
        var url=ACC.config.encodedContextPath + form.attr("action")+"?isPincodeRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
        var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        var radio = $("#radio-default2_"+addressId);
    	var radio_label = $("#radio-default2_"+addressId).parent().find('label');
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        //calling tealium
        $("#checkoutPageName").val("Choose Your Delivery Options");
        if(typeof utag_data !="undefined"){
        	var checkoutDeliveryPage = "Multi Checkout Summary Page:Choose Your Delivery Options";
        	utag_data.page_name = checkoutDeliveryPage;
        	$("#pageName").val(checkoutDeliveryPage);
        }
        //tpr-TPR-6362 | track checkout activity
        if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_delivery_option');
    	}
        //TISCSXII-2176 fix 
        if(typeof (digitalData.page.pageInfo)!= 'undefined'){
    		digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
    	}
        //disableHideAjaxLoader will make sure that loader is not removed until CNC stores are fetched.
        var disableHideAjaxLoader=false;
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
            	ACC.singlePageCheckout.getSelectedAddress();
	        	$("#choosedeliveryMode").html(data);
	        	ACC.singlePageCheckout.showAccordion("#choosedeliveryMode");
	        	//selectDefaultDeliveryMethod();
	        	$(".click-and-collect").addClass("click-collect");
	        	$("#selectedAddressMessage").hide();
	        	
	        	//This method will check if CNC is checked as default delivery mode. If so an ajax call is fired to fetch CNC stores. 
	        	//disableHideAjaxLoader will make sure that loader is not removed until stores are fetched.
	        	disableHideAjaxLoader=ACC.singlePageCheckout.callFetchCNCStoresForWeb();
	        	
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
        	if(!disableHideAjaxLoader)
        	{
        		ACC.singlePageCheckout.hideAjaxLoader();
        	}
		});
      if(typeof utag !="undefined")  {
		utag.link({
	        link_text: "deliveryAddress_proceed_clicked",
	        event_type: "proceed_button_clicked"
	    })
      }
	},
	//Function used to hide CNC store block when a user toggels between HD/ED and CNC
	attachDeliveryModeChangeEvent:function()
	{
		//$("#choosedeliveryMode input:radio")
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
	//Function to get pick up persson form from server, Once fetched it will not be fetched again[For web].
	getPickUpPersonForm:function(pickupPersonName,pickupPersonMobileNo){
    	var isCncPresent=$("#selectDeliveryMethodForm #isCncPresentInSinglePageCart").val();
    	if(isCncPresent=="true")
    	{
    		var htmlPopulated=$("#singlePagePickupPersonPopup span#modalBody").attr("data-htmlPopulated");
    		if(htmlPopulated=="NO")
        	{	//If form has not been fetched yet
    			var url=ACC.config.encodedContextPath + "/checkout/single/pickupPerson/popup";
        		var data="";
        		var xhrPickupPersonResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);;
        		xhrPickupPersonResponse.fail(function(xhr, textStatus, errorThrown) {
        				console.log("ERROR:"+textStatus + ': ' + errorThrown);
        		});
        	        
        		xhrPickupPersonResponse.done(function(data) { 
        			//Populating popup
        			$("#singlePagePickupPersonPopup #modalBody").html(data);
        			$("#singlePagePickupPersonPopup #modalBody").attr("data-htmlPopulated","YES");
        			$('form[name="pickupPersonDetails"] #pickupPersonName').val(pickupPersonName);
            		$('form[name="pickupPersonDetails"] #pickupPersonMobile').val(pickupPersonMobileNo);
    			});
        	}
    		else if(htmlPopulated=="YES")
    		{	//If form has already been fetched, We just need to update the form entries.
    			$('form[name="pickupPersonDetails"] #pickupPersonName').val(pickupPersonName);
        		$('form[name="pickupPersonDetails"] #pickupPersonMobile').val(pickupPersonMobileNo);
    		}
    	}
	},
	
	//Function to get address selected, To be highlighted when address accordion is closed.
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
	//Function to get selected delivery modes, To be highlighted when delivery mode accordion is closed.
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
                	var str ="";
                	if(data.hd>0)
                	{
                		str+="Standard Shipping ("+data.hd+" item),"
                	}
                	if(data.ed>0)
                	{
                		str+="Express Shipping ("+data.ed+" item),"
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
                	if(!ACC.singlePageCheckout.isReviewOrderCalled)
                	{
                		$("#selectedDeliveryOptionsHighlight").hide();//Hide here show in getReviewOrder
                	}
                	else
            		{
                		//If review order has completed its call before this method returns show it here
                		$("#selectedDeliveryOptionsHighlight").show();
                		$("#selectedDeliveryOptionsDivId").show();
            		}
                	$("#selectedDeliveryOptionsHighlight").html(str);
                	
                	// For Review Order Highlight Display
                	$("#selectedReviewOrderHighlight").html(data.CountItems + " Item(s), " + data.totalPrice);
                	ACC.singlePageCheckout.countItemsForReviewOrder=data.CountItems;
                	
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
                		$("#selectedDeliveryOptionsHighlight").show();
                	}
                }
            }
		});
	},
	//Generic method used to initiate ajax call
	ajaxRequest:function(url,method,data,cache){
		return $.ajax({
			url: url,
			type: method,
			data: data,
			cache: cache
		});
	},
	//Function to display page number for carousel
	carouselPageNumberDisplay:function(items,item,displayClassName){
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
		$("."+displayClassName).html("<span>"+current_page + " / " + page_no+"</span>");
	},
	//Function used to fetch stores for web and responsive. This is called when a user clicks on CNC delivery mode.
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
            	if($('#cncStoreContainer'+entryNumber+' .checkout-shipping-items').length>0)
            	{
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
	        				'changed.owl.carousel',
	        				function(event) {
	        			var items     = event.item.count;     // Number of items
	        			var item      = event.item.index;     // Position of the current item
	        			//Below method will display correct page number.
	        			ACC.singlePageCheckout.carouselPageNumberDisplay(items,item,"page_count_cnc");
	        		});
	        		$(".cnc_carousel").owlCarousel({
	        			items:3,
	        			loop: false,
	        			dots:true,
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
	            				mouseDrag:($(".cnc_item .removeColor"+entryNumber).length <= 1)?false:true,
	            				nav: ($(".cnc_item .removeColor"+entryNumber).length <= 1)?false:true,
	            			},
	            			// breakpoint from 768 up
	            			768 : {
	            				items:2,
	            				slideBy: 2,
	            				mouseDrag:($(".cnc_item .removeColor"+entryNumber).length <= 2)?false:true,
	            				nav: ($(".cnc_item .removeColor"+entryNumber).length <= 2)?false:true,
	            			},
	            			// breakpoint from 1280 up
	            			1280 : {
	            				items:3,
	            				mouseDrag:($(".cnc_item .removeColor"+entryNumber).length <= 3)?false:true,
	            				nav: ($(".cnc_item .removeColor"+entryNumber).length <= 3)?false:true,
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
            	}//End of if
            }
		}); 
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
	},
	//Function called to search CNC stores. If no stores are matching, All the stores will be shown to the user. If one or more stores are matching, the matching stores will be displayed and rest will be hidden.
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
				
				//User will be taken to first page on search success
				$(".cnc_carousel").trigger('to.owl.carousel',[0]);
				var cnc_count = $(".showStoreAddress").length;	//Used in case of search stores
				ACC.singlePageCheckout.carouselPageNumberDisplay(cnc_count,0,"page_count_cnc");
				
				//Hiding arrows
				var width=$(window).width();
				if(cnc_count<=2 && width>=768 && width<1280)
				{
					$("#cncStoreContainer"+entryNumber+" .owl-prev").hide();
					$("#cncStoreContainer"+entryNumber+" .owl-next").hide();
				}
				if(cnc_count<=3 && width>=1280)
				{
					$("#cncStoreContainer"+entryNumber+" .owl-prev").hide();
					$("#cncStoreContainer"+entryNumber+" .owl-next").hide();
				}
				
			}else if(searchText=="")
			{
				$allListElements.closest("li").show();
				$allListElements.closest("li").removeClass("showStoreAddress");
				$allListElements.closest("li").parent("div").addClass("owl-item");
				$allListElements.closest("li").parent("div.owl-item").show();
				
				//User will be taken to first page on search complete
				$(".cnc_carousel").trigger('to.owl.carousel',[0]);
				var cnc_count = $("#cncUlDiv"+entryNumber+" .cnc_item .removeColor"+entryNumber).length;	//Used in case of search stores
				ACC.singlePageCheckout.carouselPageNumberDisplay(cnc_count,0,"page_count_cnc");
				
				//Showing arrows
				var width=$(window).width();
				if(cnc_count>2 && width>=768 && width<1280)
				{
					$("#cncStoreContainer"+entryNumber+" .owl-prev").show();
					$("#cncStoreContainer"+entryNumber+" .owl-next").show();
				}
				if(cnc_count>3 && width>=1280)
				{
					$("#cncStoreContainer"+entryNumber+" .owl-prev").show();
					$("#cncStoreContainer"+entryNumber+" .owl-next").show();
				}
			}
			
			if( typeof utag !="undefined" && searchText!=""){
				utag.link({
					link_text: searchText+"entered",
					event_type : searchText+"entered",
					search_keyword : searchText
				});
			}
	},
	//Function to perform serach on enter key press
	searchOnEnterPress:function(e,element,entryNumber)
	{
		var code = e.keyCode || e.which;
	    if(code==13){
	    	ACC.singlePageCheckout.searchCNCStores(element,entryNumber);
	        // Enter pressed
	    }
	},
	//Function to select a cnc store on carousel item click
	selectStore:function(storeName,entryNumber,index)
	{
		if(ACC.singlePageCheckout.getIsResponsive())
		{
			$('#selectDeliveryMethodFormMobile input[name="deliveryMethodEntry['+entryNumber+'].selectedStore"]').val(storeName);
			$("#selectDeliveryMethodFormMobile #address"+entryNumber+""+index).prop("checked",true);
			$("#selectDeliveryMethodFormMobile ."+entryNumber+"_select_store").hide();
			ACC.singlePageCheckout.getPickUpPersonPopUpMobile();
		}
		else
		{
			$('#selectDeliveryMethodForm input[name="deliveryMethodEntry['+entryNumber+'].selectedStore"]').val(storeName);
			$("#selectDeliveryMethodForm #address"+entryNumber+""+index).prop("checked",true);
			$("#selectDeliveryMethodForm ."+entryNumber+"_select_store").hide();
		}
		if(typeof(utag)!='undefined')
		{
			utag.link({
				link_text  : storeName+'_store_selected', 
				event_type : storeName+'_store_selected'
			});
		}
		//TPR-6029 | DTM For checkout
		dtmStoreSelection(storeName);
		
	},
	//Function to validate and submit pick up person form
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
	//Function to submit pick up person form to the server
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
			//Below is for responsive
			if(ACC.singlePageCheckout.getIsResponsive())
			{	
				ACC.singlePageCheckout.mobileValidationSteps.isPickUpPersonDetailsSaved=true;
				ACC.singlePageCheckout.resetPaymentModes();
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
	//Validation method
	validatePickupPersonNameOnKeyUp:function(){
		ACC.singlePageCheckout.hidePickupDetailsErrors();
		ACC.singlePageCheckout.validatePickupPersonName();
	},
	//Validation method
	validatePickupPersonMobileOnKeyUp:function(){
		ACC.singlePageCheckout.hidePickupDetailsErrors();
		ACC.singlePageCheckout.validatePickupPersonMobile();
	},
	//Validation method
	validatePickupPersonName:function(){
		var pickupPersonName = $("#pickupPersonName").val();
		var statusName = ACC.singlePageCheckout.allLetter(pickupPersonName);
		var nameCheck = ACC.singlePageCheckout.checkWhiteSpace(pickupPersonName);
		if(pickupPersonName.length <= "3"){ 
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
			return false;
		}
		else if(statusName == false) {
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Please Enter Only Alphabets");
			return false;
		}
		else if(nameCheck == false){
			$(".pickupPersonNameError").show();
			$(".pickupPersonNameError").text("Spaces cannot be allowed");
			return false;
		 }
		else{
			return true;
		}
	},
	//Validation method
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
	//Generic function to display modals
	modalPopup: function(elementId,data){

		var selector="#"+elementId+" #modalBody"
		$(selector).html(data);
		$("#"+elementId).modal('show');
		//$("#newAddressFormMobile.new-address-form-mobile").html('');
	},
	//Generic function to showAccordion
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
	//Validation method
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
    //Validation method
    checkMobileNumberSpace:function(number) {			
		return /\s/g.test(number);
	},
	//Validation method
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
    addReviewOrderPriceStrikeThrough:function(){
    	$("#off-bag").show();

		$("li.price").each(function(){
			if(($(this).find(".off-bag").css("display") === "inline-block") || ($(this).find(".off-bag").css("display") === "block")){
				if($(this).find("span.delSeat.mop").length > 0){
				$(this).find("span.delSeat:not(.mop)").addClass("delAction");
				}
			}
			else{
				$(this).find("span.delSeat:not(.mop)").removeClass("delAction");
			}
		});
    },
    //Function to fetch review order page from server
    getReviewOrder:function(){
    	{
	    	$("#selectedDeliveryOptionsHighlight").show();
	    	$("#selectedDeliveryOptionsDivId").show();
	    	ACC.singlePageCheckout.isReviewOrderCalled=true;
    	}
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
        	if($("#reviewOrder #totPriceWithoutRupeeSymbol").text()!="")
        	{
        		var countItemsText=ACC.singlePageCheckout.countItemsForReviewOrder;
        		$("#selectedReviewOrderHighlight").html(countItemsText+" Item(s), "+$("#reviewOrder #totPriceWithoutRupeeSymbol").text());
        	}
        	//added for tealium
  		  $("#checkoutPageName").val("Review Order");
        	if(typeof utag_data != "undefined"){
            	var checkoutDeliveryPage = "Multi Checkout Summary Page:Review Order";
            	utag_data.page_name = checkoutDeliveryPage;
            	$("#pageName").val(checkoutDeliveryPage);
            	 
            }
        	 //tpr-TPR-6362 | track checkout activity
        	if(typeof (_satellite)!= "undefined") {  
        		_satellite.track('cpj_checkout_proceed_to_review');
        	}	
        	 if(typeof (digitalData.page.pageInfo)!= 'undefined'){
         		digitalData.page.pageInfo.pageName =  $('#pageName').val().toLowerCase();
         	}
        	
        	//START:Code to show strike off price
        	ACC.singlePageCheckout.addReviewOrderPriceStrikeThrough();
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
    //Generic function to process error messages
	processError: function(showElementId,jsonResponse){
		if(jsonResponse.type=="error")
		{
			//$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
			$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+jsonResponse.displaymessage+"</span>");
			$(showElementId).show();
			// TPR-6369 |Error tracking for  dtm
	 			dtmErrorTracking("pincode_servicability_error","Issue in PincodeServiceability");
		}
		if(jsonResponse.type=="errorCode")
		{
			$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+ACC.singlePageCheckout.ajaxErrorMessages(jsonResponse.displaymessage)+"</span>");
			$(showElementId).show();
			// TPR-6369 |Error tracking for  dtm
	 			dtmErrorTracking("pincode_servicability_error","Issue in PincodeServiceability");
		}
		if(jsonResponse.type=="redirect")
		{
			window.location.href=ACC.config.encodedContextPath+jsonResponse.url;
		}
	},
	//Function to process confirm box for exchange
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
	            		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
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
            		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
				 document.getElementById('confirmOverlay').style.display = "none";
				 document.getElementById('confirmBox').style.display = "none";
				 
				});
			 
			//$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
			
		}
		
	}
	},
	//Function to remove exchange from cart for responsive
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
	checkPromotionRestriction: function(){
		var url=ACC.config.encodedContextPath + "/checkout/single/checkPromotions";
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
        		if(data.type=="response")
                {
	        		$("#isPincodeRestrictedPromoPresent").text(data.isPincodeRestrictedPromoPresent);
	        		$("#isDelModeRestrictedPromoPresent").text(data.isDelModeRestrictedPromoPresent);
                }
            }
		});
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	//Function to removeCart entry number for reviewOrder page[For Web]
	removeCartItem : function(element,clickFrom) {			
		ACC.singlePageCheckout.showAjaxLoader();
		var productId ="";
			if(clickFrom=="removeItem")
			{
					var entryNumber1 = $(element).attr('id').split("_");
					var entryNumber = entryNumber1[1];
					var entryUssid = entryNumber1[2];
					var divId = "entryItemReview"+entryNumber;
					productId = $("#"+divId).find('#product').val();
					//tealium call for remove 
		    		if(typeof(utag)!='undefined')
					{
						utag.link({
							link_text  : 'remove_from_review_order' , 
							event_type : 'remove_from_review_order',
							product_id :  productId
						});
					}
			}
			if(clickFrom=="addItemToWl")
    		{
				var entryNumber = $("#entryNo").val();
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
	        		}
	        		if(clickFrom=="addItemToWl")
	        		{
	        			$("#reviewOrder").html(data);
	        		}
	        		ACC.singlePageCheckout.checkPromotionRestriction();//Method to check and update if promotion calls have changed after item removal
	        		ACC.singlePageCheckout.getTealiumData();
	        		//START:Code to show strike off price
	        		ACC.singlePageCheckout.addReviewOrderPriceStrikeThrough();
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
        	if(callFrom=="removeCartItem")
        	{
        		ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
        		ACC.singlePageCheckout.showHideCodTab();
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
	//Function to add entry item to wishlist from review order page[For Web]
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
							event_type : 'review_order_add_to_wishlist', 
							product_sku_wishlist : productcodearray
						});
					}
				}
	        });			
			//$('a.wishlist#wishlist').popover('hide');
	},
	//Function to call pre-payment validation  method
	validateCartForPayment:function()
	{
		var url = ACC.config.encodedContextPath + "/checkout/single/validatePayment";
		var data = "";			
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
		return xhrResponse;
	},
	//Function called from onPaymentModeSelection() function for responsive
	proceedWithPaymentForResponsive:function(paymentMode,savedOrNew,radioId,callFromCvv){
		//alert("proceedWithPaymentForResponsive called with paymentMode:"+paymentMode+" savedOrNew:"+savedOrNew+" radioId:"+radioId+" callFromCvv:"+callFromCvv);
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
        			if(ACC.singlePageCheckout.mobileValidationSteps.isApplypromoCalled == false) {	
        				recalculateCart();
        				ACC.singlePageCheckout.mobileValidationSteps.isApplypromoCalled=true;
        			}
        			
        			
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
        			
        			//CAR-343 Starts
        			try {
    					var subTotalPrice=jQuery.parseJSON(data.subTotalPrice);
    					//Populating sub-total which is subject to change on order item removal
        				$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(subTotalPrice.formattedValue);
    				}
    				catch(e) 
    				{
    					console.log("Subtotal JS Exception="+e);
    				}
    				//Populating sub-total which is subject to change on order item removal
        			//$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice.formattedValue);
    				//CAR-343 Ends
        			
        			//alert("Saved or New"+savedOrNew);
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
	//Function called when proceed button of review order page is clicked.
	proceedToPayment:function(element){
		ACC.singlePageCheckout.showAjaxLoader();
		//SDI-2158 FIX
		resetAppliedCouponFormOnRemoval();
		//TISUAT-6037 fix starts here
		$(".error_msg_backfrom_payment").html("");
		$('.error_msg_backfrom_payment').css("display","none");
		//TISUAT-6037 fix ens here
		//function call to validate payment before proceeding
		var xhrValidateResponse=ACC.singlePageCheckout.validateCartForPayment();
		xhrValidateResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
		//tealium page name addition
		$("#checkoutPageName").val("Payment Options");
		if(typeof utag_data !="undefined"){
        	var checkoutDeliveryPage = "Multi Checkout Summary Page:Payment Options";
        	utag_data.page_name = checkoutDeliveryPage;
        	$("#pageName").val(checkoutDeliveryPage);
        	 
        }
		
		//tpr-TPR-6362 | track checkout activity
		if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_proceed_to_payment');
    	}
		 if(typeof (digitalData.page.pageInfo)!= 'undefined'){
	    		digitalData.page.pageInfo.pageName =  $('#pageName').val().toLowerCase();
	    }
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
        			
    				//CAR-343 Starts
    				try {
    					var subTotalPrice=jQuery.parseJSON(data.subTotalPrice);
    					//Populating sub-total which is subject to change on order item removal
        				$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(subTotalPrice.formattedValue);
    				}
    				catch(e) 
    				{
    					console.log("Subtotal JS Exception="+e);
    				}
    				//Populating sub-total which is subject to change on order item removal
        			//$("#orderTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice.formattedValue);
    				//CAR-343 Ends
        			/*//$("#orderDetailsSectionId").html(data);
        			$("#totalWithConvField").html(data.totalPrice);*/
    				
    				// TPR-7486 start
    				//if(data.totalDiscount.value != 0){
						//$("#promotionApplied").css("display","block");
						//document.getElementById("promotion").innerHTML=data.totalDiscount.formattedValue;
					//}
    				//document.getElementById("totalWithConvField").innerHTML=data.totalPrice;
    				//if(document.getElementById("outstanding-amount")!=null)
    				//document.getElementById("outstanding-amount").innerHTML=data.totalPrice;
    				//document.getElementById("outstanding-amount-mobile").innerHTML=data.totalPrice;
    				//$("#codAmount").text(data.totalPrice);
    				
    				recalculateCart();

    				// TPR-7486 end 
    				
    				//$("#totalPriceConvChargeId span#totalWithConvField span.priceFormat").html(data.totalPrice); // TPR-7486
    				//$("#discountApplied span#promotion span.priceFormat").html(data.totalDiscount.formattedValue); // TPR-7486
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
	//Generic function to show ajax loader
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
	//Generic function to hide ajax loader
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
	//Function to submit slot delivery selection form to server
	submitSlotDeliverySelection:function()
	{
		var url=$("#selectDeliverySlotForm").prop("action");
		var data=$("#selectDeliverySlotForm").serialize();
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
            var result = data.split("-");
 			$("#deliveryCostSpanId").empty().text(result[0]);
 			$("#totalWithConvField").empty().text(result[1]);
 			$("#outstanding-amount-mobile").empty().text(result[1]);
 			$("#singlePageChooseSlotDeliveryPopup input[type=radio]:checked").attr("data-submitted","true");
        });
        
        xhrResponse.always(function() {
        });
	},	
	//UF-398
	getTealiumData:function()
	{
	var url=ACC.config.encodedContextPath + "/checkout/single/updateTealiumData";
	var data="";
	var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);

	xhrResponse.fail(function(xhr, textStatus, errorThrown) {
		console.log(" Tealium data error:"+textStatus + ': ' + errorThrown);
	});
    
    xhrResponse.done(function(data, textStatus, jqXHR) {
    	if(jqXHR.responseJSON){
			var updatedProduct= data.productIdList;
		    var updatedQuantity = data.productQuantityList ;
		    var updatedproductListPrice = data.productListPriceList;
		    var updatedProductName = data.productNameList;
		    var updatedUnitPrice = data.productUnitPriceList;
		    var updatedCartTotal = data.cart_total;
		    var updatedBrand = data.productBrandList;
		    var updatedCategory =  data.productCategoryList;
		    var updatedSubCategory = data.pageSubCategories;
		    var updatedSubCategoryL3 = data.page_subcategory_name_L3;
		    var updatedSubCategoryL4 = data.page_subcategory_name_L4;
		    var updatedAdobeSku = data.adobe_product;
		    var updatedCheckoutSeller = data.checkoutSellerIDs;
			$("#product_id").val(updatedProduct);
			$("#product_sku").val(updatedProduct);
			$("#adobe_product").val(updatedAdobeSku);
		    $("#product_name").val(updatedProductName);
			$("#product_quantity").val(updatedQuantity);
			$("#product_list_price").val(updatedproductListPrice);
			$("#product_unit_price").val(updatedUnitPrice);
			$("#cart_total").val(updatedCartTotal);
			$("#product_brand").val(updatedBrand);
			$("#page_subcategory_L1").val(updatedCategory);
			$("#product_category").val(updatedCategory);
			$("#page_subcategory_L2").val(updatedSubCategory);
			$("#page_subcategory_name").val(updatedSubCategory);
			$("#page_subcategory_l3").val(updatedSubCategoryL3);
			$("#page_subcategory_name_l3").val(updatedSubCategoryL3);
			$("#page_subcategory_l4").val(updatedSubCategoryL4);
			$("#page_subcategory_name_l4").val(updatedSubCategoryL4);
			$("#checkoutSellerIDs").val(updatedCheckoutSeller);
			if(typeof utag_data !="undefined"){
	        	utag_data.product_id = updatedProduct;
	        	utag_data.product_sku = updatedProduct;
	        	utag_data.adobe_product = updatedAdobeSku;
	        	utag_data.product_name = updatedProductName;
	        	utag_data.product_quantity = updatedQuantity;
	        	utag_data.product_list_price = updatedproductListPrice;
	        	utag_data.product_unit_price = updatedUnitPrice;
	        	utag_data.cart_total = updatedCartTotal;
	        	utag_data.product_brand = updatedBrand;
	        	utag_data.page_subcategory_L1 = updatedCategory;
	        	utag_data.product_category = updatedCategory;
	        	utag_data.page_subcategory_L2 = updatedSubCategory;
	        	utag_data.page_subcategory_name = updatedSubCategory;
	        	utag_data.page_subcategory_l3 = updatedSubCategoryL3;
	        	utag_data.page_subcategory_name_l3 = updatedSubCategoryL3;
	        	utag_data.page_subcategory_l4 = updatedSubCategoryL4;
	        	utag_data.page_subcategory_name_l4 = updatedSubCategoryL4;
	        	utag_data.checkoutSellerIDs = updatedCheckoutSeller;
	        	
	        }
			
    	}
				
	});
    
    xhrResponse.always(function(){        	
	});
  },
  //Function to show hide cod for CNC 
  showHideCodTab:function(){
	  if(ACC.singlePageCheckout.getIsResponsive()){
		  //Mobile
		  var entryNumbersId=$("#selectDeliveryMethodFormMobile #entryNumbersId").val();
		  var isCncPresent=$("#selectDeliveryMethodFormMobile #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
	  }
	  else{
		  //Desktop
		  var entryNumbersId=$("#selectDeliveryMethodForm #entryNumbersId").val();
		  var isCncPresent=$("#selectDeliveryMethodForm #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
	  }
	  var cncSelected="false";
	  var entryNumbers=entryNumbersId.split("#");
	  for(var i=0;i<entryNumbers.length-1;i++)
	  {
		if(isCncPresent=="true")
	  	{
	  		if($("input[name='"+entryNumbers[i]+"']").length>0 && $('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
	  		{
	  			cncSelected="true";	
	  		}
	  	}
	  }
	  if(cncSelected=="true")
	  {
	  	$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","none");
	  	$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","none");
	  	$("#viewPaymentCODMobile").parent("li").removeClass("paymentModeMobile");
	  }
	  else
	  {
	  	$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","block");
	  	$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","block");
	  	$("#viewPaymentCODMobile").parent("li").addClass("paymentModeMobile");
	  }
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
	countItemsForReviewOrder:"",
	needHelpContactNumber:"",
	currentPincode:"",
	previousPincode:"",
	isReviewOrderCalled:false,
/****************MOBILE STARTS HERE************************/
//-----------------------------COMMENTS ON mobileValidationSteps object-----------------------------//
//	1.isAddressSaved		:	Used to track if new address has been saved in cartModel for responsive
//	2.isAddressSet			:	Used to track if existing address has been set as delivery address in cartModel for responsive
//	3.isDeliveryModeSet		:	Used to track if delivery mode has been set in cartModel for responsive
//	4.saveNewAddress		:	Used to track if new address has to be saved in cartModel for responsive
//	5.selectedAddressId		:	Used to track the selected address id for responsive
//	6.isInventoryReserved	:	Used to track if inventory has been reserved for responsive
//	7.isScheduleServiceble	:	Used to track scheduled delivery is available for responsive
//	8.paymentModeSelected	:	Used to track the selected payment mode for responsive
//	9.prePaymentValidationDone:	Used to track cart validation before payment is made
//	10.isCncSelected		:	Used to track if CNC delivery mode has been selected
//	11.isPickUpPersonDetailsSaved:	Used to track if CNC pickup person details have been saved
//	12.isPincodeServiceable	:	Used to track if pincode is serviceable or not
//	13.saveEditAddress		:	Used to track if edit address has to be saved in cartModel for responsive
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
		prePaymentValidationDone:false,
		isCncSelected:false,
		isPickUpPersonDetailsSaved:false,
		isPincodeServiceable:false,
		isApplypromoCalled:false,
		saveEditAddress:false,
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
		ACC.singlePageCheckout.mobileValidationSteps.isCncSelected=false;
		ACC.singlePageCheckout.mobileValidationSteps.isPickUpPersonDetailsSaved=false;
		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
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
		//Hiding SnackBar
		ACC.singlePageCheckout.hideSnackBar();
	},
	//Function used to reset payment modes when a saved card is clicked after clicking a payment mode[For responsive]
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
		//Hiding SnackBar
		//ACC.singlePageCheckout.hideSnackBar();
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
					$("#singlePagePickupPersonPopup #modalBody").attr("data-htmlPopulated","YES");
				});
				
				xhrPickupPersonResponse.always(function(data) { 
					ACC.singlePageCheckout.hideAjaxLoader();
				});
        	}
    		else
    		{
    			ACC.singlePageCheckout.hidePickupDetailsErrors();
        		$("#singlePagePickupPersonPopup").modal('show');
    		}
    	}
	},
	
	
	//Function used to fetch add address form for responsive
	getMobileAddAddress:function(){
		var formAlreadyLoaded=$("#newAddressFormMobile.new-address-form-mobile").attr("data-loaded");
		if(ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected!="")
		{//Will enter if an user selects new address after selecting a payment mode
			ACC.singlePageCheckout.resetValidationSteps();
			ACC.singlePageCheckout.resetPaymentModes();
		}
		//When ever a user selects a new address this flag should be set to true
		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=true;
		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
		if(formAlreadyLoaded=="false")
		{
			ACC.singlePageCheckout.showAjaxLoader();
			var url=ACC.config.encodedContextPath + "/checkout/single/new-address";
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data) {
	        	$("#editAddressFormMobile.new-address-form-mobile").html("");//Removing edit address form when new address is selected
	        	//Remove edit address radio button check
        		$("#editAddressForResponsive div.mobile_add_address.form_open").removeClass("form_open");//Remove radio of edit address
        		$("#editAddressForResponsive").css("display","none");//Hide edit address block
	        	//Unchecking the radio button of saved addresses
	        	$('#selectAddressFormMobile input[name=selectedAddressCode]').prop('checked', false);
	        	$("#addNewAddressForResponsive div.mobile_add_address").addClass("form_open");
				$("#newAddressFormMobile.new-address-form-mobile").html(data);
				$("#newAddressFormMobile.new-address-form-mobile").attr("data-loaded","true");
				$("#newAddressFormMobile.new-address-form-mobile").slideDown();
				$("#singlePageAddressPopup #modalBody").html('');
				$("#newAddressButton").hide();
				ACC.singlePageCheckout.attachOnPincodeKeyUpEvent("newAddressFormMobile",true,false);//OnKeyUp even will be attached so that after 6 digits are enter pincode serviceability call is made
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else
		{
			$("#newAddressFormMobile form#addressForm")[0].reset();
			ACC.singlePageCheckout.showAjaxLoader();
			$('#selectAddressFormMobile input[name=selectedAddressCode]').prop('checked', false);
        	$("#addNewAddressForResponsive .mobile_add_address").addClass("form_open");
        	$("#newAddressFormMobile.new-address-form-mobile").slideDown();
			ACC.singlePageCheckout.hideAjaxLoader();
		} 
		
		//UF-429
		if(typeof(utag)!='undefined')
		{
			utag.link({
				link_text  : 'new_address_clicked', 
				event_type : 'new_address_clicked'
			});
		}
		return false;	
	},
	//Function used to fetch edit address form for responsive
	getMobileEditAddress:function(pincode,addressId){
		if(ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected!="")
		{//Will enter if an user selects new address after selecting a payment mode
			ACC.singlePageCheckout.resetValidationSteps();
			ACC.singlePageCheckout.resetPaymentModes();
		}
		//When ever a user selects a edit address this flag should be set to true
		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=true;
		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
		
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/edit-address/"+addressId;
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
		
		xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
		
		xhrResponse.done(function(data) {
			$("#editAddressForResponsive").show();//Show edit address block
			$("#newAddressFormMobile.new-address-form-mobile").html("");//Removing new address form when edit address is selected
			$("#newAddressFormMobile.new-address-form-mobile").attr("data-loaded","false");//As the form has been removed above we need to reset data-loaded attribute to false 
			ACC.singlePageCheckout.changeAddress();//Hiding change link and displaying other addressess
			//Unchecking the radio button of saved addresses
			$('#selectAddressFormMobile input[name=selectedAddressCode]').prop('checked', false);
			$("#addNewAddressForResponsive .mobile_add_address").removeClass("form_open");//Closing new address form
			$("#editAddressForResponsive .mobile_add_address").addClass("form_open");	  //Opeining edit address form
			$("#editAddressFormMobile.new-address-form-mobile").html(data);
			$("#editAddressFormMobile.new-address-form-mobile").attr("data-loaded","true");
			$("#editAddressFormMobile.new-address-form-mobile").slideDown();
			$("#editAddressFormMobile #modalBody").html('');
			$("#newAddressButton").hide();
			loadPincodeData("edit").done(function() {
        	var value = $(".address_landmarkOtherDiv").attr("data-value");
   			 otherLandMarkTri(value,"defult");
            });
			//Call pincode serviceability on edit.
			ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive(pincode,addressId,false,true);
			ACC.singlePageCheckout.attachOnPincodeKeyUpEvent("editAddressFormMobile",false,true);//OnKeyUp even will be attached so that after 6 digits are enter pincode serviceability call is made
			ACC.singlePageCheckout.scrollToDiv("editAddressFormMobile",100);
		});
		
		xhrResponse.always(function(){
			ACC.singlePageCheckout.hideAjaxLoader();
		});
		
		//UF-429
		if(typeof(utag)!='undefined')
		{
			utag.link({
				link_text  : 'new_address_clicked', 
				event_type : 'new_address_clicked'
			});
		}
		return false;	
	},
	//Function used to check pincode serviceability for responsive
	checkPincodeServiceabilityForRespoinsive:function(selectedPincode,addressId,isNew,isEdit)
	{
		//alert("Calling pincode serviceability for responsive with Pincode:"+selectedPincode+"/AddressId:"+addressId+"/isNew:"+isNew);
		if(!ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable || ACC.singlePageCheckout.mobileValidationSteps.isAddressSet || ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved)
		{
			ACC.singlePageCheckout.resetValidationSteps();
    		ACC.singlePageCheckout.resetPaymentModes();
		}
		if(addressId!="" && !isEdit)
		{
			$("#radio_mobile_"+addressId).prop("checked", true);
		}
		else
		{
			//$("input[name=selectedAddressCodeMobile]").prop("checked", false);
		}
		if(selectedPincode!=null && selectedPincode != undefined && selectedPincode!=""){
			 ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable=false;
			 var isPincodeRestrictedPromoPresent=false;
			if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
			{
				isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
			}
			 var url= ACC.config.encodedContextPath + "/checkout/single/delModesOnAddrSelect/"+selectedPincode+"?locRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
			 var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
			  xhrResponse.fail(function(xhr, textStatus, errorThrown) {
					console.log("ERROR:"+textStatus + ': ' + errorThrown);
				});
				xhrResponse.done(function(response, textStatus, jqXHR) {
					//alert("Pincode serviceability ajax called successfully");
					//Hiding address pincode serviceability failure error messages
	            	$("#selectedAddressMessageMobile").hide();
	            	$("#addressMessage").hide();
	            	if(!isNew)
                	{
	            		//Remove new address radio button check
	            		$("#addNewAddressForResponsive div.mobile_add_address.form_open").removeClass("form_open");
	            		$("#newAddressFormMobile").slideUp();
                	}
	            	if(!isEdit)
	            	{
	            		//Hide edit radio for responsive
	            		 ACC.singlePageCheckout.hideMobileEditRadio();
	            	}
	            	if(isNew)
                	{
	            		//Flags set for new address
	            		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId="";
	            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=true;
	            		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
                	}
	            	else if(isEdit)
                	{
	            		//Flags set for edit address
	            		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId=addressId;
	            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
	            		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=true;
                	}
	            	else
	            	{
	            		//Flags set for select address
	            		ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId=addressId;
	            		ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress=false;
	            		ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress=false;
	            	}
	            	//Processing ajax response below
					if (jqXHR.responseJSON) {
						//In case of some error at server end below block will execute.
		                if(response.type!="response" && response.type!="confirm")
		                {
		                	if(isNew || isEdit)
		                	{
		                		ACC.singlePageCheckout.processError("#addressMessage",response);
		                		ACC.singlePageCheckout.scrollToDiv("addressMessage",100);
		                	}
		                	else
	                		{
		                		//Remove new address radio nutton check
		                		//$("#addNewAddressForResponsive div.mobile_add_address.form_open").removeClass("form_open");
		                		ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",response);
		                		ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
	                		}
		                	ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable=false;
		                }
		                //For Confirm Box TPR-1083
		                else if(response.type=="confirm")
		                {
		                		ACC.singlePageCheckout.processConfirm("#selectedAddressMessageMobile",response,addressId,selectedPincode,isNew,"");
		                }
		            } else {
		            	//In case of no error at server end below block will execute.
		            	//alert("Before setting #choosedeliveryModeMobile");
		            	$("#choosedeliveryModeMobile").html(response);
		            	ACC.singlePageCheckout.fetchStoresResponsive();//Fetch CNC stores if CNC is selected by default
		            	ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable=true;
		            	//alert("ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable="+ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable);
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
	//Function to hide edit radio for responsive
	hideMobileEditRadio:function(){
		//Remove edit address radio button check
		$("#editAddressForResponsive div.mobile_add_address.form_open").removeClass("form_open");
		$("#editAddressFormMobile").slideUp();
		$("#editAddressFormMobile").html("");
		$("#editAddressForResponsive").css("display","none");
	},
	//Function called when change link of address is clicked for responsive
	changeAddress:function(){
		//$("#address-change-link").on("click", function(){
			$("#address-change-link").parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").show();
			//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
			$("#address-change-link").hide();
			if(typeof utag !="undefined")  {
				utag.link({
			        link_text: "change_link_clicked",
			        event_type: "change_link_clicked"
			    })
		      }
	    //});
	},
	//Fetch CNC stores in responsive, This will in turn call fetchStores() method
	fetchStoresResponsive:function(){
		//fetching cnc stores if only click n collect delivery mode is present
    	var entryNumbersId=$("#selectDeliveryMethodFormMobile #entryNumbersId").val();
		var isCncPresent=$("#selectDeliveryMethodFormMobile #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
		var cncSelected="false";
		if(entryNumbersId == undefined) {
			return false
		}
    	var entryNumbers=entryNumbersId.split("#");
		for(var i=0;i<entryNumbers.length-1;i++)
		{
		    if(isCncPresent && $("input[name='"+entryNumbers[i]+"']").length>0 && $('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
		    {
		    	cncSelected="true";
			    var ussid=$('input[name="deliveryMethodEntry['+entryNumbers[i]+'].sellerArticleSKU"]').val();
			    ACC.singlePageCheckout.fetchStores(entryNumbers[i],ussid,'click-and-collect','','');
		    }
		}
		//Hiding/Showing COD if cnc is selcted
		if(cncSelected=="true")
		  {
		  	$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","none");
		  	$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","none");
		  	$("#viewPaymentCODMobile").parent("li").removeClass("paymentModeMobile");
		  }
		  else
		  {
		  	$("#viewPaymentCOD, #viewPaymentCODMobile").css("display","block");
		  	$("#viewPaymentCOD, #viewPaymentCODMobile").parent("li").css("display","block");
		  	$("#viewPaymentCODMobile").parent("li").addClass("paymentModeMobile");
		  }
	},
	//Function called when change link of delivery mode is clicked for responsive
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
		$(".hideDelModeMobile").show();//Show hidden delivery modes
//		$(".hideDelModeMobile").removeAttr('disabled');
//		$(".hideDelModeMobile").css("opacity","1");
//		$(".hideDelModeMobile").css("pointer-events","auto");
		$(element).hide();			//Hide change link
	},
	//Method to reset validation flags and payment mode form on delivery mode change after payment mode is selected(For responsive)
	attachEventToResetFlagsOnDelModeChange:function()
	{
		$("#choosedeliveryModeMobile input[type=radio]").off("change.deliveryModeRadioChange");
		$("#choosedeliveryModeMobile input[type=radio]").on("change.deliveryModeRadioChange",function(){
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
	//Function called when ever a user enters pincode for responsive, When the character length becomes 6 a pincode serviceability call is fired.
	attachOnPincodeKeyUpEvent:function(id,isNew,isEdit)
	{	
		var selector1='keyup'+"."+id;
		var selector2='change'+"."+id;//Adding change event to handle form autocomplete/autofill scenario
		$('#'+id+' .address_postcode').off(selector1+" "+selector2);
		$('#'+id+' .address_postcode').on(selector1+" "+selector2,function(){
			//$("#"+id+" #addressMessage").html("");
			var pincode=$('.address_postcode').val();
			$("#"+id+" .otherLandMarkError").html("");
			var regPostcode = /^([1-9])([0-9]){5}$/;
			if(pincode.length>=6)
			{
				if(regPostcode.test(pincode) == false)
				{
					 $("#"+id+" #addresspostcodeError").show();
				     $("#"+id+" #addresspostcodeError").html("<p>Please enter a valid pincode</p>");
				}
				else
				{
					ACC.singlePageCheckout.currentPincode=pincode;
					if(ACC.singlePageCheckout.currentPincode!=ACC.singlePageCheckout.previousPincode)
					{
						console.log("CONSOLE========>"+pincode);
						ACC.singlePageCheckout.previousPincode=pincode;
						$("#"+id+" #addresspostcodeError").hide();
						ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive(pincode,"",isNew,isEdit);
					}
				}
			}
				
		});
	},
	//Function used to save a new/edited address and set it as delievry address[for responsive]. Called from onPaymentModeSelection() method
	saveAndSetDeliveryAddress:function(isNew,isEdit)
	{
		var validationResult=ACC.singlePageCheckout.validateAddressForm();
		if(validationResult!=false)
		{
			ACC.singlePageCheckout.showAjaxLoader();
			try{
				var url="";
				if(isNew)
				{
					url=ACC.config.encodedContextPath + "/checkout/single/new-address-responsive";
				}
				else if(isEdit)
				{
					url=ACC.config.encodedContextPath + "/checkout/single/new-address-responsive?isEdit=true";
				}
				var data=$("#chooseDeliveryAddressMobileDiv form#addressForm").serialize().replace(/\+/g,'%20');
				
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
	//Function used to set an existing address as a delievry address[for responsive]. Called from onPaymentModeSelection() method
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
	//Function used to validate if pickup person details are provided and are saved.
	checkPickUpDetailsSavedForCnc:function()
	{
		var entryNumbersId=$("#selectDeliveryMethodFormMobile #entryNumbersId").val();
		var isCncPresent=$("#selectDeliveryMethodFormMobile #isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
    	var cncSelected="false";
		if(entryNumbersId=="")
		{
			ACC.singlePageCheckout.resetPaymentModes();
			return false;
		}
		else
		{	
			//validate here
			if(entryNumbersId == undefined)  {
				return false;
			}
			var entryNumbers=entryNumbersId.split("#");
			for(var i=0;i<entryNumbers.length-1;i++)
			{
				if(isCncPresent=="true")
	        	{
					if($("input[name='"+entryNumbers[i]+"']").length>0 && $('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
    				{
        				cncSelected="true";		
        				ACC.singlePageCheckout.mobileValidationSteps.isCncSelected=true;
        				//Below if will execute if no stores are selected
        				if($("input[name='address"+entryNumbers[i]+"']:checked").length<=0)
						{
							$("."+entryNumbers[i]+"_select_store").show();
							ACC.singlePageCheckout.scrollToDiv("cncStoreContainer"+entryNumbers[i],100);
							//Removing payment mode selection incase of pickup location has not been selected
							$("#cncStoreContainer"+entryNumbers[i]).on("click.cncStoreRadioValidationFailed,focus.cncStoreRadioValidationFailed,change.cncStoreRadioValidationFailed",function(){
								ACC.singlePageCheckout.resetPaymentModes();
								$("#cncStoreContainer"+entryNumbers[i]).off("click.cncStoreRadioValidationFailed,focus.cncStoreRadioValidationFailed,change.cncStoreRadioValidationFailed");
							});
							//If delivery modes will be changed all the validation flags will be reset.
		            		ACC.singlePageCheckout.attachEventToResetFlagsOnDelModeChange();
							return false;
						}
        				else if($("input[name='address"+entryNumbers[i]+"']:checked").length>0 && !ACC.singlePageCheckout.mobileValidationSteps.isPickUpPersonDetailsSaved)
						{
        					//Else If will execute if a stores is selected but pick up person details are not saved, Display the pick up person form.
							ACC.singlePageCheckout.getPickUpPersonPopUpMobile();
							return false;
						}
    				}
	        	}
			}
			if(cncSelected=="false")
			{
				ACC.singlePageCheckout.mobileValidationSteps.isCncSelected=false;
			}
		}
		return true;
	},
	//function called when payment mode is selected in responsive.
	onPaymentModeSelection:function(paymentMode,savedOrNew,radioId,callFromCvv)
	{
		var formValidationSuccess=true;
		ACC.singlePageCheckout.mobileValidationSteps.paymentModeSelected=paymentMode;
		//TPR-7486
		if(savedOrNew=="savedCard")
		{
			$("#paymentMode_newcard_savedcard").val("savedCard"); //for responsive
		}
		else
		{
			$("#paymentMode_newcard_savedcard").val("newCard");  //for responsive
		}
		//TPR-7486
		if(ACC.singlePageCheckout.getIsResponsive()) {
			$('#continue_payment_after_validate_responsive').show();
		} else {
			$('#continue_payment_after_validate').show();
		}	
		
		//TPR-7486 //REMOVE CONV CHARGE 				
		if(paymentMode != 'COD') {
			resetConvChargeElsewhere(); //REMOVE CONV CHARGE 
		}
		
		//Below we are checking if pincode is serviceabile
		if(!ACC.singlePageCheckout.mobileValidationSteps.isPincodeServiceable)
		{
			if(!ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress  && !ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress)
			{
				ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
			}
			else if((ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress||ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress))
			{
				ACC.singlePageCheckout.scrollToDiv("addressMessage",100);
			}
			return false;
		}
		
		//Below we are checking that if CNC is present and if CNC pick up person details have been saved, If not we are not allowing the user to proceed with payment
		if(!ACC.singlePageCheckout.checkPickUpDetailsSavedForCnc())
		{
			$("#singlePageMobile #singlePagePickupPersonPopup,.overlay,.content").on("click.pickupPersonDetailsNotSaved,focus.pickupPersonDetailsNotSaved",function(){
				ACC.singlePageCheckout.resetPaymentModes();
				$("#singlePageMobile #singlePagePickupPersonPopup,.overlay,.content").off("click.pickupPersonDetailsNotSaved,focus.pickupPersonDetailsNotSaved");
			});
			return false;
		}
		//Below is for if a new address has been selected or an existing address has been edited. First validate the form and then submit the form using ajax.
		if((ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress||ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress) && !ACC.singlePageCheckout.mobileValidationSteps.isAddressSet)
		{
			if(ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress)
			{
				//Validate and submit form for new address
				formValidationSuccess=ACC.singlePageCheckout.saveAndSetDeliveryAddress(true,false);
			}
			else if(ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress)
			{
				//Validate and submit form for edit address
				formValidationSuccess=ACC.singlePageCheckout.saveAndSetDeliveryAddress(false,true);
			}
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
		else if(ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId!="" && !ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress  && !ACC.singlePageCheckout.mobileValidationSteps.saveEditAddress && !ACC.singlePageCheckout.mobileValidationSteps.isAddressSet)
		{
			//This block will execute if an existing address is selected. On selection of an existing address, The selected existing address is set as delivery address using ajax call here.
			ACC.singlePageCheckout.showAjaxLoader();
			ACC.singlePageCheckout.setDeliveryAddress();
		}
		//Below block will execute to set delivery modes using ajax and also reserve inventory.
		if(formValidationSuccess && !ACC.singlePageCheckout.mobileValidationSteps.isDeliveryModeSet && !ACC.singlePageCheckout.mobileValidationSteps.isInventoryReserved)
		{	
			ACC.singlePageCheckout.showAjaxLoader();
//			var isPincodeRestrictedPromoPresent=false;
//			if(typeof($("#isPincodeRestrictedPromoPresent").text())!='undefined')
//			{
//				isPincodeRestrictedPromoPresent=$("#isPincodeRestrictedPromoPresent").text().trim();
//			}
			//var url=$("#selectDeliveryMethodFormMobile").attr("action")+"?locRestrictedPromoPresent="+isPincodeRestrictedPromoPresent;
			var url=$("#selectDeliveryMethodFormMobile").attr("action");
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
	                		$("#singlePageChooseSlotDeliveryPopup #modalBody").attr("data-htmlPopulated","NO");
	                		$.each(data.dlvrySltAvlbleForUssid,function(key, value){
	                			$("#slotMsgId_"+key).show();
	                		});
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
		$("#selectSnackbar").on("click.showSnackBar",function(){
			ACC.singlePageCheckout.getResponsiveSlotSelectionPage();
		    
		});
		
	},
	hideSnackBar:function(){
		// Remove the "show" class to DIV
		var x = $("#mobileSnackbar").removeClass("show");
		$("#selectSnackbar").off("click.showSnackBar",function(){
			ACC.singlePageCheckout.getResponsiveSlotSelectionPage();
		    
		});
		
	},
	//Function called when done button on slot delivery jsp is clicked.
	onSlotDeliveryDoneClick:function(){
		ACC.singlePageCheckout.submitSlotDeliverySelection();
		$('#singlePageChooseSlotDeliveryPopup').modal('hide');
		if(!ACC.singlePageCheckout.getIsResponsive())
		{
			ACC.singlePageCheckout.getReviewOrder();
		}
		
	},
	//Function called when cancel button on slot delivery jsp is clicked.
	onSlotDeliveryCancelClick:function(){
		$('#singlePageChooseSlotDeliveryPopup').modal('hide');
		if(!ACC.singlePageCheckout.getIsResponsive())
		{
			ACC.singlePageCheckout.getReviewOrder();
		}
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
	            		$("#singlePageChooseSlotDeliveryPopup #modalBody").attr("data-htmlPopulated","YES");
	            }
	        });
	        
	        xhrResponse.always(function() {
	        	ACC.singlePageCheckout.hideAjaxLoader();
	        });
    	}
		else
		{
			$("#singlePageChooseSlotDeliveryPopup").modal('show');
			$('#singlePageChooseSlotDeliveryPopup input[data-submitted="false"]').prop("checked",false)
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
		if(paymentMode=="PAYTM")
		{
			viewPaymentPaytm();
		}
	},
	//Function called when a saved card is selected.
	paymentOnSavedCardSelection:function(paymentMode,savedOrNew,radioId,callFromCvv)
	{
		//Call can be intiated either from saved card radio button selection or on focus on cvv text box
		$("#paymentMode").val(paymentMode);
		if(paymentMode=="Credit Card")
		{
			if($("#"+radioId).is(":checked") && callFromCvv=='true')
			{
				//Do Nothing if radio button is already checked and user clicks on cvv text box
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
				//Do Nothing if radio button is already checked and user clicks on cvv text box
			}
			else{
				$("#"+radioId).prop("checked",true);
				ACC.singlePageCheckout.resetPaymentModesOnSavedCardSelection(paymentMode);
				savedDebitCardRadioChange(radioId);
			}
		}
	},
	selectPaymentSpecificOffers:function(offerID){
	   if(ACC.singlePageCheckout.getTopoffers(offerID) == false) { //not top 3 offers
              $('input:radio[name=offer_name_more]').each(function () { 
		if($(this).val() == offerID) {
		    $(this).prop('checked', true);
		    $(this).addClass("promoapplied");
		    var SelectedOfferRadioId = $(this).attr("id");
		    var title_text   = $("#"+SelectedOfferRadioId+" + label").html();
			var title_desc   = $("#"+SelectedOfferRadioId).parent().find('.offer_des').html();
			var title_maxmin = $("#"+SelectedOfferRadioId).parent().find('.max-min-value').html();
			
			//replace
			$("#offer_name0").val(offerID);
			$("#offer_name0 + label").html(title_text);
			$("#offer_name0").parent().find('.offer_des').html(title_desc);
			$("#offer_name0").parent().find('.max-min-value').html(title_maxmin);
			
			$('input:radio[name=offer_name]').each(function () { 
				if($(this).val() == offerID) {
				  $(this).prop('checked', true);
				  $(this).addClass("promoapplied");
				} 
			});
					  
		} 
	});	 				
        } else { 

		$('input:radio[name=offer_name]').each(function () { 
			if($(this).val() == offerID) {
			  $(this).prop('checked', true);
			  $(this).addClass("promoapplied");
			} 
		});
		$('input:radio[name=offer_name_more]').each(function () { 
			if($(this).val() == offerID) {
			  $(this).prop('checked', true);
			  $(this).addClass("promoapplied");
			 } 
		});
				
         }
	},
	populatePaymentSpecificOffers:function(selectedOffer){

		//ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/paymentRelatedOffers";
		var data = {isResposive:ACC.singlePageCheckout.getIsResponsive()};
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(response, textStatus, jqXHR) {
        	var path = window.location.pathname;
        	if(ACC.singlePageCheckout.getIsResponsive() && (path.indexOf("multi/payment-method") == -1)) {
            	$('.offer_section_responsive').css("display","block");
            	$('.offer_section_responsive').html(response);
        	} else {
            	$('.offers_section_paymentpage').css("display","block");
            	$('.offers_section_paymentpage').html(response);
        	}
		if(typeof selectedOffer !== "undefined" && selectedOffer != "") {
		        ACC.singlePageCheckout.selectPaymentSpecificOffers(selectedOffer);
	        }else {
		   $('input:radio[name=offer_name]').each(function () { $(this).prop('checked', false);$(this).removeClass("promoapplied");  });
	           $('input:radio[name=offer_name_more]').each(function () { $(this).prop('checked', false); $(this).removeClass("promoapplied"); });
	        }

            	ACC.singlePageCheckout.populatePaymentSpecificOffersTermsConditions();
            	
		}); 
        
        xhrResponse.always(function(){
        	//ACC.singlePageCheckout.hideAjaxLoader();
		});
	
	},
	populatePaymentSpecificOffersTermsConditions:function(){

		//ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/paymentRelatedOffersTerms";
		var data = {isResposive:ACC.singlePageCheckout.getIsResponsive()};
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(response, textStatus, jqXHR) {
      
            	$('.offer_terms_container_poppup').html(response);	
            	
		}); 
        
        xhrResponse.always(function(){
        	//ACC.singlePageCheckout.hideAjaxLoader();
		});
	
	},	
	chooseOffer:function(id,value){
		
		var radioId	= id;
		var offerID =  value;
		
	  if(ACC.singlePageCheckout.getIsResponsive()) { //for responsive view	
		   if(ACC.singlePageCheckout.mobileValidationSteps.isApplypromoCalled == false) {	
			   recalculateCart(false,offerID,radioId);

		   } else {
			   //ACC.singlePageCheckout.chooseOfferAjaxCall(offerID,radioId);
			   if($( "#"+radioId ).hasClass( "promoapplied" )) { // need release
					var releaseCode = offerID;
					if(releaseCode != undefined && releaseCode != "") {
				 		ACC.singlePageCheckout.releasePromoVoucher(releaseCode);
				 	}
				} else { //apply coupon
					ACC.singlePageCheckout.chooseOfferAjaxCall(offerID,radioId);
				}
		   }	
		} else {
			//ACC.singlePageCheckout.chooseOfferAjaxCall(offerID,radioId);
			 if($( "#"+radioId ).hasClass( "promoapplied" )) { // need release
					var releaseCode = offerID;
					if(releaseCode != undefined && releaseCode != "") {
				 		ACC.singlePageCheckout.releasePromoVoucher(releaseCode);
				 	}
				} else { ////apply coupon
					ACC.singlePageCheckout.chooseOfferAjaxCall(offerID,radioId);
				}
		}	
		
	},	
	chooseOfferAjaxCall:function(offerID,radioId){
		//alert(offerID);
		//$('input[name=offer_name]:checked+label::before').css("background", "black");
		//$('.promoapplied').removeClass("promoapplied"); 
		//$("#"+radioId).addClass("promoapplied");
		$("#juspayconnErrorDiv").css("display","none");
		document.getElementById("juspayErrorMsg").innerHTML="";
	       if(ACC.singlePageCheckout.getIsResponsive()) {
		$("#offer_section_responsive_error_msgDiv").css("display","none");
		document.getElementById("offer_section_responsive_error_msg").innerHTML="";
		}
		
		
			
		$('input:radio[name=offer_name]').each(function () { 
			if($(this).val() == offerID) {
			  $(this).prop('checked', true);
			  $(this).addClass("promoapplied");
			} else {
			  $(this).prop('checked', false);
			  $(this).removeClass("promoapplied"); 
			}
		});
		$('input:radio[name=offer_name_more]').each(function () { 
			if($(this).val() == offerID) {
			  $(this).prop('checked', true);
			  $(this).addClass("promoapplied");
			 } else {
			  $(this).prop('checked', false);
			  $(this).removeClass("promoapplied"); 
			}
		});
		
		
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/multi/coupon/usevoucher";
		var guid = $('#guid').val();
		var data= {manuallyselectedvoucher:offerID,guid:guid};
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
			//internet issue
			$('input:radio[name=offer_name]').each(function () { $(this).prop('checked', false);$(this).removeClass("promoapplied");  });
			$('input:radio[name=offer_name_more]').each(function () { $(this).prop('checked', false); $(this).removeClass("promoapplied"); });
			if(ACC.singlePageCheckout.getIsResponsive()) {
				$("#offer_section_responsive_error_msg").html("Oops, something went wrong! Please re-select the offer and complete your purchase.");
				$("#offer_section_responsive_error_msgDiv").css("display","block");
			} else {
				$("#juspayErrorMsg").html("Oops, something went wrong! Please re-select the offer and complete your purchase.");
				$("#juspayconnErrorDiv").css("display","block");
			}

		});
        
        xhrResponse.done(function(response, textStatus, jqXHR) {
        	$("#paymentoffersPopup").modal('hide'); //for poppup
        	
        	if(response.couponRedeemed != false && response.redeemErrorMsg == null && response.totalPrice.formattedValue != null) { //coupon applied successfully
	 			document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
	 			if(document.getElementById("outstanding-amount")!=null)
	 			{
	 				document.getElementById("outstanding-amount").innerHTML=response.totalPrice.formattedValue;
	 			}
				document.getElementById("outstanding-amount-mobile").innerHTML=response.totalPrice.formattedValue;
	 			$("#codAmount").text(response.totalPrice.formattedValue);
	 			
	 			if(response.couponDiscount.value != 0){
					$("#promotionApplied").css("display","block");
					document.getElementById("promotion").innerHTML=response.couponDiscount.formattedValue;
				}
	 		
	 			
	 			//offer applied from view more section Not top 3 offer | shall be the first Bank Offer visible
	 			if(ACC.singlePageCheckout.getTopoffers(offerID) == false) {
	 				var title_text = $("#"+radioId+" + label").html();
	 				var title_desc = $("#"+radioId).parent().find('.offer_des').html();
	 				var title_maxmin = $("#"+radioId).parent().find('.max-min-value').html();
	 				
	 				//replace
	 				$("#offer_name0").val(offerID);
	 				$("#offer_name0 + label").html(title_text);
	 				$("#offer_name0").parent().find('.offer_des').html(title_desc);
	 				$("#offer_name0").parent().find('.max-min-value').html(title_maxmin);
	 				$('input:radio[name=offer_name]').each(function () { 
	 					if($(this).val() == offerID) {
	 					  $(this).prop('checked', true);
	 					  $(this).addClass("promoapplied");
	 					} else {
	 					  $(this).prop('checked', false);
	 					  $(this).removeClass("promoapplied"); 
	 					}
	 				});
	 				
	 			} 	
	
	 			
        	} else { // not applied
			   if(response.totalPrice !=null && response.totalPrice.formattedValue != null) {
        			document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
    	 			if(document.getElementById("outstanding-amount")!=null)
    	 			{
    	 				document.getElementById("outstanding-amount").innerHTML=response.totalPrice.formattedValue;
    	 			}
    				document.getElementById("outstanding-amount-mobile").innerHTML=response.totalPrice.formattedValue;
    	 			$("#codAmount").text(response.totalPrice.formattedValue);
        		}
        		 			
	 			if(response.couponDiscount.value != 0 && response.couponDiscount.value !=null){
					$("#promotionApplied").css("display","block");
					document.getElementById("promotion").innerHTML=response.couponDiscount.formattedValue;
				} else {
					$("#promotionApplied").css("display","none");
				}
	 			if(ACC.singlePageCheckout.getIsResponsive()) {
	        		document.getElementById("offer_section_responsive_error_msg").innerHTML="Sorry! The Offer cannot be used for this purchase.";
					$("#offer_section_responsive_error_msgDiv").css("display","block");
	 			} else {
	        		document.getElementById("juspayErrorMsg").innerHTML="Sorry! The Offer cannot be used for this purchase.";
					$("#juspayconnErrorDiv").css("display","block");
	 			}

				$('input:radio[name=offer_name]').each(function () { $(this).prop('checked', false);$(this).removeClass("promoapplied");  });
				$('input:radio[name=offer_name_more]').each(function () { $(this).prop('checked', false); $(this).removeClass("promoapplied"); });
        	}
        	
            	
		}); 
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
        
		
	},
	getTopoffers:function(val){
         var status = false;  
		 $('input:radio[name=offer_name]').each(function (index) { 
			 if(index < 3) {
			   if($(this).val() == val) {
				   //alert("top");
				   status = true;
			   }
			 }
		   });
		    return status;
	},
	releasePromoVoucher:function(offerID){		
    	//$('.promoapplied').prop('checked', false);
    	//$('.promoapplied').removeClass("promoapplied"); 
    	//release voucher ajax call
		ACC.singlePageCheckout.showAjaxLoader();
		$("#juspayconnErrorDiv").css("display","none");
		document.getElementById("juspayErrorMsg").innerHTML="";
		if(ACC.singlePageCheckout.getIsResponsive()) {
		$("#offer_section_responsive_error_msgDiv").css("display","none");
		document.getElementById("offer_section_responsive_error_msg").innerHTML="";
		}	
		
		var url=ACC.config.encodedContextPath + "/checkout/multi/coupon/releasevoucher";
		var guid = $('#guid').val();
		var data= {manuallyselectedvoucher:offerID,guid:guid};
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
			//internet issue
			$('input:radio[name=offer_name]').each(function () { 
				if($(this).val() == offerID) {
				  $(this).prop('checked', true);
				  $(this).addClass("promoapplied");
				} 
			});
			$('input:radio[name=offer_name_more]').each(function () { 
				if($(this).val() == offerID) {
				  $(this).prop('checked', true);
				  $(this).addClass("promoapplied");
				 } 
			});
			if(ACC.singlePageCheckout.getIsResponsive()) {
				$("#offer_section_responsive_error_msg").html("Oops, something went wrong! Please de-select the offer and complete your purchase.");
				$("#offer_section_responsive_error_msgDiv").css("display","block");
			} else {
				$("#juspayErrorMsg").html("Oops, something went wrong! Please de-select the offer and complete your purchase.");
				$("#juspayconnErrorDiv").css("display","block");
			}

		});
        
        xhrResponse.done(function(response, textStatus, jqXHR) {
            $("#paymentoffersPopup").modal('hide'); //for poppup
        	
        	if(response.couponRedeemed == false && response.totalPrice != undefined && response.totalPrice != null) { //coupon released successfully
	 			document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
	 			if(document.getElementById("outstanding-amount")!=null)
	 			{
	 				document.getElementById("outstanding-amount").innerHTML=response.totalPrice.formattedValue;
	 			}
				document.getElementById("outstanding-amount-mobile").innerHTML=response.totalPrice.formattedValue;
	 			$("#codAmount").text(response.totalPrice.formattedValue);
	 			
	 			if(response.couponDiscount.value != 0){
					$("#promotionApplied").css("display","block");
					document.getElementById("promotion").innerHTML=response.couponDiscount.formattedValue;
				} else {
					$("#promotionApplied").css("display","none");
				}
	 			$('input:radio[name=offer_name]').each(function () { $(this).prop('checked', false); $(this).removeClass("promoapplied"); });
	 			$('input:radio[name=offer_name_more]').each(function () { $(this).prop('checked', false);  $(this).removeClass("promoapplied"); });
	 	    	//$('.promoapplied').prop('checked', false);
	 	    	//$('.promoapplied').removeClass("promoapplied");
        	} else { // not applied
        		console.log("Sorry! Unable to release the Offer");
				//$("#juspayconnErrorDiv").css("display","block");
        	}
        	
        	
            	
		}); 
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
	},
	showAllOffers:function(){
		//$(".offer_container_poppup").show();
		ACC.singlePageCheckout.paymentOffersPopup($(".offer_container_poppup").html());
		$(".offer_container_poppup").remove();
	},	
	paymentOffersPopup:function(data){		
     	   $("body").append('<div class="modal fade" id="paymentoffersPopup"><div class="content offer-content" style="padding: 40px;min-width: 45%;">'+data+'<button class="close" data-dismiss="modal" style="border:0px !important;margin: 0px !important;"></button></div><div class="overlay" data-dismiss="modal"></div></div>');

		   $("#paymentoffersPopup").modal('show');	 
		   if ($("input[name='offer_name']:checked").length > 0) {
			   var selected = $("input[name='offer_name']:checked").val();
			   $('input:radio[name=offer_name_more]').each(function () { 
					if($(this).val() == selected) {
					  $(this).prop('checked', true);
					  $(this).addClass("promoapplied");
					 } else {
					  $(this).prop('checked', false);
					  $(this).removeClass("promoapplied"); 
					}
				});
			  
		   } 
		   
		  
	},
	showPaymentSpecificOffersTermsConditions:function(){	
		if($("#offer_terms_container_poppup").html() != "") {
	    	ACC.singlePageCheckout.paymentTermsConditionsOffersPopup($(".offer_terms_container_poppup").html());
	    	$("#accordion-tnc > li > span").click(function() {
	    	    $(this).addClass('active').next('div').show(250)
	    	    .closest('li').siblings().find('span').removeClass('active').next('div').hide(250);
	    	});
		}

	},
	paymentTermsConditionsOffersPopup:function(data){
	    if($("#paymenttermsoffersPopup").length) {
	        $("#paymenttermsoffersPopup").remove();
            }
  	   $("body").append('<div class="modal fade" id="paymenttermsoffersPopup"><div class="content offer-content" style="padding: 40px;min-width: 45%;">'+data+'<button class="close" data-dismiss="modal" style="border:0px !important;margin: 0px !important;"></button></div><div class="overlay" data-dismiss="modal"></div></div>');
           $("#paymenttermsoffersPopup").modal('show');
		$("#accordion-tnc > li > span").click(function() {
	   		if($(this).hasClass('active')) {
	   			    $(this).removeClass('active').next('div').hide(250)
	   			    .closest('li').siblings().find('span').removeClass('active').next('div').hide(250);
	   		} else {
	   			    $(this).addClass('active').next('div').show(250)
	   			    .closest('li').siblings().find('span').removeClass('active').next('div').hide(250);
	   		}
   		});
	}
	
	
	
	
/****************MOBILE ENDS HERE************************/
}
//Calls to be made on dom ready.
$(document).ready(function(){
	
	var pageType=$("#pageType").val();
	if(pageType=="multistepcheckoutsummary")
	{
		//Updating need help number ACC.singlePageCheckout.needHelpContactNumber is set in 'needhelpcomponent.jsp' file.
		var needHelpNumber=ACC.singlePageCheckout.needHelpContactNumber;
		if(needHelpNumber!="" && needHelpNumber!=null)
		{
			needHelpNumber=needHelpNumber.replace(/\-/g, " ");
			$("#singlePageNeedHelpComponent").text("Need Help? Call "+needHelpNumber);
		}
		var onLoadIsResponsive=ACC.singlePageCheckout.getIsResponsive();
		$(window).on("resize",function(){
			//Reload the page if a user resizes the device and viewport width changes from desktop to responsive
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
		var deviceType=$("#deviceType").text();
		if(deviceType=="normal" && ACC.singlePageCheckout.getIsResponsive())
		{
			//These is to handle abnormal scenarios, Which are likely to occur in test environment.
			//Reload the page if a user directly accesses the site in a small sized desktop browser.
			window.location.href=ACC.config.encodedContextPath +"/checkout/single"+"?isResponsive=true";
		}
		else if(deviceType=="mobile" && !ACC.singlePageCheckout.getIsResponsive())
		{
			//These is to handle abnormal scenarios, Which are likely to occur in test environment.
			//Reload the page if a user directly accesses the site in desktop browser modifying the user agent in browser dev tools to mobile where as width of view port is more than 768px.
			window.location.href=ACC.config.encodedContextPath +"/checkout/single"+"?isNormal=true";
		}
		
		if(ACC.singlePageCheckout.getIsResponsive())
		{
			var defaultAddressPincode=$("#defaultAddressPincode").text();
			var defaultAddressId=$("#defaultAddressId").text();
			var defaultAddressPresent=$("#defaultAddressPresent").text();
			if(defaultAddressPresent=="true" && typeof(defaultAddressPincode)!='undefined' && typeof(defaultAddressId)!='undefined')
			{
				//Calling pincode serviceability on page load for responsive cases.
				ACC.singlePageCheckout.checkPincodeServiceabilityForRespoinsive(defaultAddressPincode.trim(),defaultAddressId.trim(),false,false);
			}
			//Hiding change link if no dafault address is present
			if(defaultAddressPresent=="false")
			{
				$("#chooseDeliveryAddressMobileDiv").find(".mobileNotDefaultDelAddress").show();
				//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
				$("#chooseDeliveryAddressMobile .change-mobile").hide();
			}
			//For responsive site we are removing payment page laoded for web view inorder to keep unique id's in the view
			$("#makePaymentDiv").html("");
			
			//TPR-7486
			$('#continue_payment_after_validate_responsive').show();
			$('#continue_payment_after_validate').hide();
			ACC.singlePageCheckout.populatePaymentSpecificOffers();
			//Fetch CNC stores in responsive if CNC is selected on page load
			ACC.singlePageCheckout.fetchStoresResponsive();
			
		}
		else
		{
			//For web site we are removing payment page laoded for responsive view inorder to keep unique id's in the view
			$("#makePaymentDivMobile").html("");
			//TPR-7486
			$('#continue_payment_after_validate_responsive').hide();
			$('#continue_payment_after_validate').show();
			
			
		}
		//Preventing auto complete of forms in single page
//		$( document ).on( 'focus', ':input', function(){
//	        //$( this ).attr( 'autocomplete', 'off' );
//	        $( this ).attr( 'autocomplete', 'false' );
//	    });
		
	}
	 
	
});

/*$(document).on('click','.promoapplied',function(e){
	event.preventDefault();
	var releaseCode = $('.promoapplied').val();
 	if(releaseCode != undefined && releaseCode != "") {
 		ACC.singlePageCheckout.releasePromoVoucher(releaseCode);
 	}
	    
});*/






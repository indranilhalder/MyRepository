ACC.singlePageCheckout = {

	_autoload: [
	    "mobileAccordion"
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
			default:message="No message specified"; 
		}
		return message;
	},
	
	getDeliveryAddresses:function(element){
		
		var url=ACC.config.encodedContextPath + "/checkout/single?isAjax=true";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data) {
        	$("#chooseDeliveryAddress").html(data);
        	//Carousel reformation
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
        	
		});
        
        xhrResponse.always(function(){
			console.log("ALWAYS:");
		});
   
		return false;	
	},
	
	getEditAddress:function(element,event){
		event.preventDefault();
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + $(element).attr("href");
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
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
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
        
		return false;	
	},
	
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
	                if(data.type!="response")
	                {
	                	ACC.singlePageCheckout.processError("#addressMessage",data);
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
		return false;	
	},
	
	getAddAddress:function(){
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + "/checkout/single/new-address";
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET","",false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data) {
        	var elementId="singlePageAddressPopup";
        	ACC.singlePageCheckout.modalPopup(elementId,data);
		});
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
        
		return false;	
	},
	
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
	                if(data.type!="response")
	                {
	                	ACC.singlePageCheckout.processError("#addressMessage",data);
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
		        	if(typeof utag !="undefined"){
						utag.link({ link_text : 'add_new_address_saved' ,event_type : 'add_new_address_saved'});
					}
	            }
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		return false;	
	},
	
	proceedOnDeliveryModeSelection:function(element){
		var entryNumbersId=$("#entryNumbersId").val();
		var isCncPresent=$("#isCncPresentInSinglePageCart").val();//This will be true if any cart item has CNC as delivery mode
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
				if($("input[name='"+entryNumbers[i]+"']:checked").length<=0)
				{
				 alert("No del mode checked");
				 return false;
				}
				if(isCncPresent=="true")
	        	{
					if($('input:radio[name='+entryNumbers[i]+']:checked').attr("id").includes("click-and-collect"))
    				{
        				cncSelected="true";							
						if($("input[name='address"+entryNumbers[i]+"']:checked").length<=0)
						{
							$("."+entryNumbers[i]+"_select_store").show();
							alert("in here");
							return false;
						}
    				}
	        	}
			}
		}
		ACC.singlePageCheckout.showAjaxLoader();
		var url=ACC.config.encodedContextPath + $("#selectDeliveryMethodForm").prop("action");
		var data=$("#selectDeliveryMethodForm").serialize();
		var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
        
        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
            if (jqXHR.responseJSON) {
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
            		ACC.singlePageCheckout.getSelectedDeliveryModes();
        		}
            } else {
            	ACC.singlePageCheckout.getSelectedDeliveryModes();
            	
            	//else
            	{
            		//$("#choosedeliveryMode").html(data);
            		var elementId="singlePageChooseSlotDeliveryPopup";
            		ACC.singlePageCheckout.modalPopup(elementId,data);
            	}
//            	$("#reviewOrder").html(data);
//            	//START:Code to show strike off price
//        		$("#off-bag").show();
//
//        		$("li.price").each(function(){
//        				if($(this).find(".off-bag").css("display") === "block"){
//        					$(this).find("span.delSeat").addClass("delAction");
//        				}
//        				else{
//        					$(this).find("span.delSeat").removeClass("delAction");
//        				}
//        			});
//        		//END:Code to show strike off price
//            	if($('body').find('a.cart_move_wishlist').length > 0){
//            	$('a.cart_move_wishlist').popover({ 
//            		html : true,
//            		content: function() {
//            			return $('.add-to-wishlist-container').html();
//            		}
//            	});
//            	}
            	
//            	else
//            	{
//            		ACC.singlePageCheckout.showAccordion("#reviewOrder");
//            	}
//            	ACC.singlePageCheckout.ReviewPriceAlignment();
//            	$(window).on("resize", function() {
//            		ACC.singlePageCheckout.ReviewPriceAlignment();
//            	});
            	
            }
        });
        
        xhrResponse.always(function() {
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	
	validateAddressForm:function(){
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
		 
		if(result == undefined || result == "" )
		{	
			$("#addressfirstNameError").show();
			$("#addressfirstNameError").html("<p>First Name cannot be Blank</p>");
			validate= false;
		}
		else if(letters.test(result) == false)  
		{ 
			$("#addressfirstNameError").show();
			/*Error message changed TISPRD-427*/
			$("#addressfirstNameError").html("<p>First name should not contain any special characters or space</p>");
			validate= false;
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
		}
		else if(letters.test(result) == false)  
		{ 
			$("#addresssurnameError").show();
			/*Error message changed TISPRD-427*/
			$("#addresssurnameError").html("<p>Last name should not contain any special characters or space</p>");
			validate= false;
		} 
		else
		{
			$("#addresssurnameError").hide();
		}
		
		result=address1.value;
		if(result == undefined || result == "")
		{
			$("#addressline1Error").show();
			$("#addressline1Error").html("<p>Address Line 1 cannot be blank</p>");
			validate= false;
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
		}
		else if(cityPattern.test(result) == false)  
		{ 
			$("#addresstownCityError").show();
			$("#addresstownCityError").html("<p>City must be alphabet only</p>");
			validate= false;
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
		}
	    else if(regPostcode.test(zipcode) == false){
	        $("#addresspostcodeError").show();
	        $("#addresspostcodeError").html("<p>Please enter a valid pincode</p>");
			validate= false;  
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
		}
	    else if (mob.test(txtMobile) == false) {
			$("#addressmobileError").show();
			$("#addressmobileError").html("<p> Please enter correct mobile no.</p>");
			 validate=false;   
	    }
	       else
		{
			$("#addressmobileError").hide();
		}
	   
	   
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
			return false;
		}
		else{
			return true;
		}
	},
	
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
        
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
        		if(data.type=="confirm")
                {
        			ACC.singlePageCheckout.processConfirm("#selectedAddressMessage",data,element,addressId);
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
	        	
	        	var urlSetDefault=ACC.config.encodedContextPath + "/checkout/multi/delivery-method/set-default-address/" + addressId;
	        	var xhrResponseSetDefault=ACC.singlePageCheckout.ajaxRequest(urlSetDefault,"GET","",false);
	        	xhrResponseSetDefault.fail(function(xhr, textStatus, errorThrown) {
	    			console.log("ERROR:"+textStatus + ': ' + errorThrown);
	    		});
	        	
	        	xhrResponseSetDefault.done(function(data) {
	        		//alert(data);
	        		if(data.toString()=="true"){
	     				//console.log(radio);
	     				$(".addressList_wrapper input[type='radio']+label").removeClass("radio-checked");
	     				
	     				radio.attr('checked', 'checked');
	     				console.log(radio_label);
	     				radio_label.addClass('radio-checked');
	     				//radio_label.css('background-color',' #999999');
	     			}else{
	     				console.log("Unable to set default address");
	     			}
	        	});
            }
		});
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
		});
      if(typeof utag !="undefined")  {
		utag.link({
	        link_text: "proceed_pay_bottom_id2",
	        event_type: "proceed_pay"
	    })
      }
	},
	
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
                	var isCncPresent=$("#isCncPresentInSinglePageCart").val();
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
	                			$("#singlePagePickupPersonPopup .content").html(data);
	                			$("#singlePagePickupPersonPopup").data("htmlPopulated","YES");
	                			$('form[name="pickupPersonDetails"] #pickupPersonName').val(pickupPersonName);
	                    		$('form[name="pickupPersonDetails"] #pickupPersonMobile').val(pickupPersonMobileNo);
	            			});
	                	}
                	}
                }
            }
		});
	},
	
	getSelectedDeliveryModes:function(){
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
                	$("#selectedReviewOrderHighlight").html(data.CountItems + " Items, " + data.totalPrice);
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
	
	fetchStores:function(entryNumber,sellerArticleSKU,deliveryCode)
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
        			nav: true,
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
            			$(".cnc_carousel").find('div.owl-item').height('');
                    },
                    onRefreshed: function () {
                    	$(".cnc_carousel").find('div.owl-item').height($(".cnc_carousel").height());
                    }
        		});
            	
        		$( '.cnc_carousel input.radio_btn' ).on( 'click change', function(event) {
        			event.stopPropagation();
        		});
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
				$(".pickUpPersonAjax").append("<span class='pickupText'>Pickup Person Details Have Successfully Added.</span>");
			//}
			//$("#pickupPersonSubmit").text("1");
			
			$("#singlePagePickupPersonPopup").modal('hide');
			ACC.singlePageCheckout.getReviewOrder();
    		//ACC.singlePageCheckout.showAccordion("#reviewOrder");
			
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
                	ACC.singlePageCheckout.processError("#selecteDeliveryModeMessage",data);
                }
         } else {
        	$("#reviewOrder").html(data);
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
	processConfirm: function(showElementId,jsonResponse,element,addressId){
		if(jsonResponse.type=="confirm")
		{
			document.getElementById('confirmOverlay').style.display = "block";
			 document.getElementById('confirmBox').style.display = "block";
			 $( "#exConfirmYes" ).click(function() {
				 $("#contExchnage").val("disableExchange");
				 ACC.singlePageCheckout.proceedOnAddressSelection(element,addressId);
				 document.getElementById('confirmOverlay').style.display = "none";
				 document.getElementById('confirmBox').style.display = "none";
				});
			 
			 $( "#exConfirmNo" ).click(function() {
				 $("#contExchnage").val("");
				$(showElementId).html("<span class='alert alert-danger alert-dismissable' style='padding:10px;'>"+jsonResponse.displaymessage+"</span>");
					$(showElementId).show();
				 document.getElementById('confirmOverlay').style.display = "none";
				 document.getElementById('confirmBox').style.display = "none";
				 
				});
			 
			//$(showElementId).closest(".checkout-accordion").addClass("accordion-open");
			
		}
		
	},
	
	removeCartItem : function(element,clickFrom) {			
		ACC.singlePageCheckout.showAjaxLoader();
			if(clickFrom=="removeItem")
			{
					var entryNumber1 = $(element).attr('id').split("_");
					var entryNumber = entryNumber1[1];
					var entryUssid = entryNumber1[2];			
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
	        		var entryNumbers=$("#entryNumbersId").val();
	        		var removeEntryNo=entryNumber+"#";
	        		$("#entryNumbersId").val(entryNumbers.replace(removeEntryNo,""));
	        		$("#delmode_item_li_"+entryNumber).remove();
	        		ACC.singlePageCheckout.getSelectedDeliveryModes();
	        		
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
					/*TPR-656*//*TPR-4738*/
						utag.link({
							link_obj: this, 
							link_text: 'cart_add_to_wishlist' , 
							event_type : 'cart_add_to_wishlist', 
							product_sku_wishlist : productcodearray
						});
					}
				}
	        });			
			//$('a.wishlist#wishlist').popover('hide');
	},
	
	proceedToPayment:function(element){
		ACC.singlePageCheckout.showAjaxLoader();
		var url = ACC.config.encodedContextPath + "/checkout/single/validatePayment";
		var data = "";			
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
        		else if(data.type=="response" && data.validation=="success")
    			{
        			//$("#orderDetailsSectionId").html(data);
        			$("#totalWithConvField").html(data.totalPrice);
        			$("#oredrTotalSpanId ul.totals li.subtotal span.amt span.priceFormat").html(data.subTotalPrice);
    	        	
    				$("#selectedReviewOrderDivId").show();
    	        	ACC.singlePageCheckout.showAccordion("#makePaymentDiv");
    	        	
    	        	//Calling the below methods to populate the latest shipping address(These methods are in marketplacecheckoutaddon.js)
    	        	populateAddress();
    	        	populateAddressEmi();
    			}
        		else
    			{
        			console.log("ERROR:Cart validation failed, Redirectin to cart");
        			window.location.href=ACC.config.encodedContextPath+"/cart";
    			}
            }
        });
        
        xhrResponse.always(function(){
        	ACC.singlePageCheckout.hideAjaxLoader();
        });
	},
	
	showAjaxLoader:function(){
		var staticHost = $('#staticHost').val();
		//Below 2 lines for adding spinner
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	},
	
	hideAjaxLoader:function(){
		$("#no-click,.spinner").remove();
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
	mobileAccordion:function(){
		$(".change-mobile").on("click", function(){
			$(this).parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").show();
			//$(this).parents(".checkout_mobile_section").find(".cancel-mobile").show();
			$(this).hide();
		});
		/*$(".cancel-mobile").on("click", function(){
			$(this).parents(".checkout_mobile_section").find(".mobileNotDefaultDelAddress").hide();
			$(this).parents(".checkout_mobile_section").find(".change-mobile").show();
			$(this).hide();
		});*/
	},
	getIsResponsive:function(){
		var winWidth=$(window).width();
		return winWidth<768?true:false;
	},
	
/****************MOBILE STARTS HERE************************/
	mobileValidationSteps:{
		isAddressSaved:false,
		isAddressSet:false,
		isDeliveryModeSte:false,
		saveNewAddress:false,
		selectedAddressId:"",
		isInventoryReserved:false,
		isSlotDeliveryPresent:false
	},
	
	getMobileAddAddress:function(){
		var formAlreadyLoaded=$(".new-address-form-mobile").attr("data-loaded");
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
	        	$('input[name=selectedAddressCodeMobile]').prop('checked', false);
	        	$(".mobile_add_address").addClass("form_open");
				$(".new-address-form-mobile").html(data);
				$(".new-address-form-mobile").attr("data-loaded","true");
				$(".new-address-form-mobile").slideDown();
				$("#singlePageAddressPopup #modalBody").html('');
				ACC.singlePageCheckout.attachOnPincodeKeyUpEvent();
			});
	        
	        xhrResponse.always(function(){
	        	ACC.singlePageCheckout.hideAjaxLoader();
			});
		}
		else
		{
			ACC.singlePageCheckout.showAjaxLoader();
			$('input[name=selectedAddressCodeMobile]').prop('checked', false);
        	$(".mobile_add_address").addClass("form_open");
			$(".new-address-form-mobile").slideDown();
			ACC.singlePageCheckout.hideAjaxLoader();
		}        
		return false;	
	},
	
	checkIsServicableResponsive:function(selectedPincode,addressId,isNew)
	{
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
	            	$("#newAddressMobileErrorMessage").hide();
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
		                		ACC.singlePageCheckout.processError("#newAddressMobileErrorMessage",response);
		                		ACC.singlePageCheckout.scrollToDiv("newAddressMobileErrorMessage",100);
		                	}
		                	else
	                		{
		                		//Remove new address radio nutton check
		                		$("div.mobile_add_address.form_open").removeClass("form_open");
		                		ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",response);
		                		ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
	                		}
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
		            	ACC.singlePageCheckout.attachDeliveryModeChangeEvent();
		            }
		 		});
		}
		
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
			var pincode=$('.address_postcode').val();
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
					ACC.singlePageCheckout.checkIsServicableResponsive(pincode,"",true);
				}
			}
				
		});
	},
	
	saveAndSetNewDeliveryAddress:function()
	{
		var validationResult=ACC.singlePageCheckout.validateAddressForm();
		if(validationResult!=false)
		{
			var url=ACC.config.encodedContextPath + "/checkout/single/new-address-responsive";
			var data=$("form#selectAddressFormMobile").serialize().replace(/\+/g,'%20');
			
			ACC.singlePageCheckout.showAjaxLoader();
			var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"POST",data,false);
	        
	        xhrResponse.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
			});
	        
	        xhrResponse.done(function(data, textStatus, jqXHR) {
	        	if (jqXHR.responseJSON) {
	                if(data.type!="response")
	                {
	                	ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
	                	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
	                }
	                if(data.type=="response")
	                {
	                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSaved=data.isAddressSaved;
	                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSet=data.isAddressSet;
	                }
	            }
			});
	        
	        xhrResponse.always(function(){
			});
		}
		return false;
	},
	
	setDeliveryAddress:function()
	{
		$("#radio_mobile_"+addressId).prop("checked", true);
		a = $("input[name=selectedAddressCodeMobile]:checked").val();
	    if (null == a || "undefined" == a)
	        return $("#emptyAddressMobile").show(),
	        !1;
	    var data=$("form#selectAddressFormMobile").serialize();
	    var url=ACC.config.encodedContextPath + $("#selectAddressFormMobile").attr("action");
	    var xhrResponse=ACC.singlePageCheckout.ajaxRequest(url,"GET",data,false);
      
	    xhrResponse.fail(function(xhr, textStatus, errorThrown) {
			console.log("ERROR:"+textStatus + ': ' + errorThrown);
		});  
           
        xhrResponse.done(function(data, textStatus, jqXHR) {
        	if (jqXHR.responseJSON) {
                if(data.type!="response")
                {
                	ACC.singlePageCheckout.processError("#selectedAddressMessageMobile",data);
                	ACC.singlePageCheckout.scrollToDiv("selectedAddressMessageMobile",100);
                }
                if(data.type=="response")
                {
                	ACC.singlePageCheckout.mobileValidationSteps.isAddressSet=data.isAddressSet;
                }
            }
		});
        
        xhrResponse.always(function(){
		});
		
		return false;
	},
	
	onPaymentModeSelection:function(paymentMode)
	{
		if(ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress)
		{
			ACC.singlePageCheckout.saveAndSetNewDeliveryAddress();
		}
		else if(ACC.singlePageCheckout.mobileValidationSteps.selectedAddressId!="" && !ACC.singlePageCheckout.mobileValidationSteps.saveNewAddress)
		{
			ACC.singlePageCheckout.setDeliveryAddress();
		}
	}
}
//Calls to be made on dom ready.
$(document).ready(function(){
	var onLoadIsResponsive=ACC.singlePageCheckout.getIsResponsive();
	$(window).on("resize",function(){
		var onSizeChangeIsResponsive=ACC.singlePageCheckout.getIsResponsive();
		if(onLoadIsResponsive!=onSizeChangeIsResponsive)
		{
			location.reload(true);
		}
	});
	if(ACC.singlePageCheckout.getIsResponsive())
	{
		var pageType=$("#pageType").val();
		var defaultAddressPincode=$("#defaultAddressPincode").html();
		if(pageType=="multistepcheckoutsummary" && typeof(defaultAddressPincode)!='undefined')
		{
			ACC.singlePageCheckout.checkIsServicableResponsive(defaultAddressPincode.trim(),"",false);
		}
		$("#makePaymentDiv").html("");
	}
	else
	{
		$("#makePaymentDivMobile").html("");
	}
});

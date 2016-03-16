ACC.checkout = {

	_autoload: [
		"bindCheckO",
		"bindForms",
		"bindSavedPayments"
	],


	bindForms:function(){

		$(document).on("click","#addressSubmit",function(e){
			e.preventDefault();
			$('#addressForm').submit();	
		})
		
		$(document).on("click","#deliveryMethodSubmit",function(e){
			e.preventDefault();
			$('#selectDeliveryMethodForm').submit();	
		})
		
		//TISCR-305 
		$(document).on("click","#deliveryMethodSubmitUp",function(e){
			e.preventDefault();
			$('#selectDeliveryMethodForm').submit();	
		})
		
		
		$(document).on("click","#deliveryAddressSubmit",function(e){
			e.preventDefault();
			
			var selectedAddressCode = $('input[name=selectedAddressCode]:checked').val();
			if(selectedAddressCode == null || selectedAddressCode=='undefined')
			{
				alert("Please select a delivery address");
				return false;
			}
			$('#selectAddressForm').submit();	
		})
		
		//TISCR-305 
			$(document).on("click","#deliveryAddressSubmitUp",function(e){
			e.preventDefault();
			
			var selectedAddressCode = $('input[name=selectedAddressCode]:checked').val();
			if(selectedAddressCode == null || selectedAddressCode=='undefined')
			{
				alert("Please select a delivery address");
				return false;
			}
			$('#selectAddressForm').submit();	
		})
	},

	bindSavedPayments:function(){
		$(document).on("click",".js-saved-payments",function(e){
			e.preventDefault();

			ACC.colorbox.open("",{
				href: "#savedpayments",
				inline:true,
				width:"320px"
			});
		})
	},

	bindCheckO: function ()
	{
		var cartEntriesError = false;
		
		// Alternative checkout flows options
		$('.doFlowSelectedChange').change(function ()
		{
			if ('multistep-pci' == $('#selectAltCheckoutFlow').attr('value'))
			{
				$('#selectPciOption').css('display', '');
			}
			else
			{
				$('#selectPciOption').css('display', 'none');

			}
		});



		$('.continueShoppingButton').click(function ()
		{
			var checkoutUrl = $(this).data("continueShoppingUrl");
			window.location = checkoutUrl;
		});

		
		$('.expressCheckoutButton').click(function()
				{
					document.getElementById("expressCheckoutCheckbox").checked = true;
		});
		
		$(document).on("change",".confirmGuestEmail,.guestEmail",function(){
			  
			  var orginalEmail = $(".guestEmail").val();
			  var confirmationEmail = $(".confirmGuestEmail").val();
			  
			  if(orginalEmail === confirmationEmail){
			    $(".guestCheckoutBtn").removeAttr("disabled");
			  }else{
			     $(".guestCheckoutBtn").attr("disabled","disabled");
			  }
		});
		
		//TISBOX-879
//		$('.checkoutButton').click(function ()
//		{
//			var checkoutUrl = $(this).data("checkoutUrl");
//			var pincodeCheck=checkPincodeServiceability();
//			alert("pincodeCheck "+pincodeCheck);
//			if(pincodeCheck == false || pincodeCheck == undefined){
//				
//			}
//			else
//			{
//				cartEntriesError = ACC.pickupinstore.validatePickupinStoreCartEntires();
//				if (!cartEntriesError)
//				{
//					var expressCheckoutObject = $('.express-checkout-checkbox');
//					if(expressCheckoutObject.is(":checked"))
//					{
//						window.location = expressCheckoutObject.data("expressCheckoutUrl");
//					}
//					else
//					{
//						var flow = $('#selectAltCheckoutFlow').attr('value');
//						if ( flow == undefined || flow == '')
//						{
//							// No alternate flow specified, fallback to default behaviour
//							window.location = checkoutUrl;
//						}
//						else
//						{
//							// Fix multistep-pci flow
//							if ('multistep-pci' == flow)
//							{
//							flow = 'multistep';
//							}
//							var pci = $('#selectPciOption').attr('value');
//
//							// Build up the redirect URL
//							var redirectUrl = checkoutUrl + '/select-flow?flow=' + flow + '&pci=' + pci;
//							window.location = redirectUrl;
//						}
//					}
//				}
//				return false;
//				
//			}
//		});

	}


};

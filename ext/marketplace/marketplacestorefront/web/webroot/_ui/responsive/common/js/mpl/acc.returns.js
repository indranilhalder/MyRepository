	function checkReturnSelected() {
			$(".secondTataCliq .accContents .errorText").remove();
			var checkStatus = $(".firstTataCliq select").val();
			var ifShowReverseSeal = $("#ifShowReverseSeal").val();
			var reverSeal = $("input[name='reverseSeal']:checked").val();
			console.log(checkStatus);
			$("#returnReason").find(":selected").text();
			if(checkStatus == "NA" ) {
				//alert("Please Select a reason for Why are you returning this product?");
				if($(".firstTataCliq .accContents .errorText").length > 0) {
					$(".firstTataCliq .accContents .errorText").remove();
					$(".firstTataCliq .accContents").append("<div class='errorText'>Please Select a reason for Why are you returning this product?</div>");
				}
				else {
					$(".firstTataCliq .accContents").append("<div class='errorText'>Please Select a reason for Why are you returning this product?</div>");
				}
			}
			else if(ifShowReverseSeal == "true" && (!$("input[name='reverseSeal']:checked").val())){
				if($(".firstTataCliq .accContents .errorText").length > 0) {
					$(".firstTataCliq .accContents .errorText").remove();
					$(".firstTataCliq .accContents").append("<div class='errorText'>You need a Reverse Seal to return Jewellery products to us. Please confirm if you have a Reverse Seal or not.</div>");
				}
				else {
					$(".firstTataCliq .accContents").append("<div class='errorText'>You need a Reverse Seal to return Jewellery products to us. Please confirm if you have a Reverse Seal or not.</div>");
				}
			} else {
				//alert("Not Null");
				$(".firstTataCliq .errorText, .firstTataCliq .selectReason").hide();
				$(".secondTataCliq .reasonType").show();
				$(".thirdTataCliq").removeClass("removeMargin");
				$(".secondTataCliq").addClass("removeMargin");
				//$(".secondTataCliq .reasonType .selectReason .selectRadio input[name=return]:checked").val();
				$(".secondTataCliq .reasonType .selectRefund").addClass("greyColor");
				$(".secondTataCliq .reasonType .selectReplace").addClass("greyColor");
				$(".secondTataCliq .reasonType .selectReplace").addClass("greyColor").removeClass("blackColor"); 
				$(".secondTataCliq .reasonType .selectRefund").removeClass("greyColor").addClass("blackColor");
				$(".secondTataCliq .reasonType .slectionReplace, .secondTataCliq .errorTextSelection").hide();
				$(".secondTataCliq .reasonType .slectionRefund").show();
			}
			var dtmReturnReason = $("#returnReason").find(":selected").text();
			var dtmReturnProduct = $("#dtmPrdtReturnCode").val();
			var dtmReturnProductCat = $("#dtmPrdtReturnCat").val();
			dtmOrderReturn(dtmReturnReason,dtmReturnProduct,dtmReturnProductCat);
			
			
		}
		
		function goBackToFirstTataCliq () {
			$('.slectionRefund').find('input:text').val('');
			$(".secondTataCliq .reasonType .selectRefund").removeClass("errorText");
			$(".secondTataCliq .reenteraccountnumber .errorTextAN").remove();
			$(".secondTataCliq .ifsccode .errorTextifsc").remove();
			$(".thirdTataCliq").addClass("removeMargin");
			$(".secondTataCliq").removeClass("removeMargin");
			$(".firstTataCliq .selectReason").show();
			$(".secondTataCliq .reasonType").hide();
		}
		
		function changeRadioColor(type){
			if(type == "replace"){
				$(".secondTataCliq .reasonType .selectRefund").addClass("greyColor").removeClass("blackColor"); 
				$(".secondTataCliq .reasonType .selectReplace").removeClass("greyColor").addClass("blackColor"); 
				$(".secondTataCliq .reasonType .slectionReplace").show();
				$(".secondTataCliq .reasonType .slectionRefund, .secondTataCliq .errorTextSelection").hide();
			} else if(type == "refund") {
				$(".secondTataCliq .reasonType .selectReplace").addClass("greyColor").removeClass("blackColor"); 
				$(".secondTataCliq .reasonType .selectRefund").removeClass("greyColor").addClass("blackColor");
				$(".secondTataCliq .reasonType .slectionReplace, .secondTataCliq .errorTextSelection").hide();
				$(".secondTataCliq .reasonType .slectionRefund").show();
			} else if(type == "quick") {
				$(".thirdTataCliq .returnMethod .quick").addClass("blackColor").removeClass("greyColor");
				$(".thirdTataCliq .returnMethod .scheduled").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .self").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .quickDrop").show(); 
				$(".thirdTataCliq .returnMethod .selfCourier, .thirdTataCliq .returnMethod .scheduledPickup").hide();
				
				if($(".quickDropArea .checkButton:checked").length < 1){
					
					
					$('.quickDrop .quickDropArea .error_text').show().text("please select atleast one store.");
					$('#saveBlockData').prop('disabled', true);
				}else{
					$('.quickDrop .quickDropArea .error_text').hide();
					$('#saveBlockData').prop('disabled', false);
				}
			} else if(type == "scheduled") {
				$(".thirdTataCliq .returnMethod .quick").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .scheduled").addClass("blackColor").removeClass("greyColor");
				$(".thirdTataCliq .returnMethod .self").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .scheduledPickup").show(); 
				$(".thirdTataCliq .returnMethod .selfCourier, .thirdTataCliq .returnMethod .quickDrop").hide();
				showPickupTimeDate('address1');
				$(".scheduledPickupArea .address1 input, .selectRadioDate0, .scheduledPickup #scheduleReturnTime1").prop("checked", true);
				$('#saveBlockData').prop('disabled', false);
				
			} else if(type == "self") {
				$(".thirdTataCliq .returnMethod .quick").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .scheduled").removeClass("blackColor").addClass("greyColor");
				$(".thirdTataCliq .returnMethod .self").addClass("blackColor").removeClass("greyColor");
				$(".thirdTataCliq .returnMethod .selfCourier").show(); 
				$(".thirdTataCliq .returnMethod .quickDrop, .thirdTataCliq .returnMethod .scheduledPickup").hide();
				$('#saveBlockData').prop('disabled', false);
			} 
		}
		
		/*Select Return Type validations*/
		$(document).ready(function(){
			$(".slectionRefund #accountNumber").change(function() {
				checkACCValidat();
			});
			$(".slectionRefund #reEnterAccountNumber").change(function() {
				checkREACCValidat();
			});
			$(".slectionRefund #iFSCCode").change(function() {
				checkIFSCValidat();
			});
		});

				function checkACCValidat(){
					var validate = true;
					var accNum = $(".slectionRefund #accountNumber").val();
					if(accNum.length < 4 || accNum.length > 24 || accNum.trim() == '') {
						//Please Enter 16 Digit Account Number
						$('.slectionRefund .accountnumber .errorText').remove();
						$(".secondTataCliq .accountnumber").append("<div class='errorText'>Please Enter Valid Account Number.</div>");
						validate = false;
					}else{
						  $('.slectionRefund .accountnumber .errorText').remove();
					}
					return validate;
				}
				function checkREACCValidat(){
					var validate = true;
					reAccNum = $(".slectionRefund #reEnterAccountNumber").val();
					if(reAccNum.length < 4 || reAccNum.length > 24 || reAccNum.trim() == '') {
						//Please Enter 16 Digit Account Number
						$('.slectionRefund .reenteraccountnumber .errorText').remove();
						$(".secondTataCliq .reenteraccountnumber").append("<div class='errorText'>Inalid Account Number.</div>");
						validate = false;
					}else{
						$('.slectionRefund .reenteraccountnumber .errorText').remove();
					}
					if ($(".slectionRefund #accountNumber").val() != $(".slectionRefund #reEnterAccountNumber").val()) {
						//Account Number and Re-Account Number are not same
						$('.errorTextAN').remove();
						$(".secondTataCliq .reenteraccountnumber").append("<div class='errorTextAN' style='color:red;'>Account Numbers do not match.</div>");
						validate = false;
					}else{
						$('.errorTextAN').remove();
					} 
					return validate;
				}
				function checkIFSCValidat(){
					var ifscregEx = /[A-Z|a-z]{4}[0][A-Za-z0-9_]{6}$/;
					var validate = true;
					 if($(".slectionRefund #iFSCCode").val().length < 11 || !ifscregEx.test($(".slectionRefund #iFSCCode").val()) == true) {
							//Please Enter Valid IFSC Code.
						 $('.errorTextifsc').remove();
						 $(".secondTataCliq .ifsccode").append("<div class='errorText errorTextifsc' style='color:red;'>Please Enter Valid 11 characters IFSC Code. </div>");
							validate = false;
							console.log('validate:'+ validate);
						} else{
							$('.errorTextifsc').remove();
						}
					 return validate;
				}
				
				function checkCODValidations() {
						var validate = true;
						
						checkACCValidat();
						checkREACCValidat();
						checkIFSCValidat();
						
						$(".secondTataCliq .accContents .errorText").remove();
						if($(".slectionRefund #accountNumber").val().length < 4 || $(".slectionRefund #accountNumber").val().length > 24) {
							//Please Enter 16 Digit Account Number
							$(".secondTataCliq .accountnumber").append("<div class='errorText'>Invalid Account Number.</div>");
							validate = false;
						}
						if($(".slectionRefund #reEnterAccountNumber").val().length < 4 || $(".slectionRefund #reEnterAccountNumber").val().length > 24) {
							//Please Enter 16 Digit Account Number
							$(".secondTataCliq .reenteraccountnumber").append("<div class='errorText'>Invalid Account Number.</div>");
							validate = false;
						}
						if($(".slectionRefund #iFSCCode").val().length < 11) {
							//Please Enter Valid IFSC Code.
						 $('.errorTextifsc').remove();
						 $(".secondTataCliq .ifsccode").append("<div class='errorText errorTextifsc' style='color:red;'>Please Enter Valid 11 characters IFSC Code. </div>");
							validate = false;
							console.log('validate:'+ validate);
						}
						if($(".slectionRefund #accountHolderName").val().length < 4 || $(".slectionRefund #accountHolderName").val().trim() == '') {
							//Please Enter Valid Account Holder Name.
							$(".secondTataCliq .accountholdername").append("<div class='errorText'>Mininum  4 characters required</div>");
							validate = false;
						}  if($(".slectionRefund #bankName").val().length < 3 || $(".slectionRefund #bankName").val().trim() == '') {
							//Please Enter Valid Bank Name.
							$(".secondTataCliq .bankname").append("<div class='errorText'>Mininum  3 characters required</div>");
							validate = false;
						}  if(validate == true && checkACCValidat()== true && checkREACCValidat()==true && checkIFSCValidat()==true){
							return true;
						}else{
							return false;
						}
						//return true;
				}
				
				/*End of Select Return Type validations*/
		
		/*function checkCODValidations() {
			$(".secondTataCliq .accContents .errorText").remove();
			if($(".slectionRefund #accountNumber").val().length < 16) {
				//Please Enter 16 Digit Account Number
				$(".secondTataCliq .accContents").append("<div class='errorText'>Please Enter 16 Digit Account Number.</div>");
				return false;
			} else if ($(".slectionRefund #accountNumber").val() != $(".slectionRefund #reEnterAccountNumber").val()) {
				//Account Number and Re-Account Number are not same
				$(".secondTataCliq .accContents").append("<div class='errorText'>Account Number and Re-Account Number are not same.</div>");
				return false;
			} else if($(".slectionRefund #accountHolderName").val().length < 4) {
				//Please Enter Valid Account Holder Name.
				$(".secondTataCliq .accContents").append("<div class='errorText'>Please Enter Valid Account Holder Name. Mininum  4 charactor required</div>");
				return false;
			} else if($(".slectionRefund #bankName").val().length < 3) {
				//Please Enter Valid Bank Name.
				$(".secondTataCliq .accContents").append("<div class='errorText'>Please Enter Valid Bank Name. Mininum  3 charactor required</div>");
				return false;
			} else if($(".slectionRefund #iFSCCode").val().length < 11) {
				//Please Enter Valid IFSC Code.
				$(".secondTataCliq .accContents").append("<div class='errorText'>Please Enter Valid 11 charactor IFSC Code. </div>");
				return false;
			} else {
				return true;
			}
			//return true;
		}*/
		
		function checkSecondTataCliq(returnMethod) {
			//alert("Hi");
			var selection = $(".secondTataCliq .selectReason .selectRadio input[name=refundType]:checked").val();
			console.log(selection);
			if(selection == "replace"){
				$(".thirdTataCliq").addClass("removeMargin");
				$(".secondTataCliq").addClass("removeMargin");
				$(".thirdTataCliq .returnMethod").show();
				$(".secondTataCliq .reasonType").hide();
				$(".thirdTataCliq .returnMethod .selectReturnMethod").addClass("greyColor");
			} else if(selection == "R") {
				console.log(returnMethod);
				try
				{
				if(returnMethod == "COD")  {
					checkCODValidations();
					if(checkCODValidations() == true){
					$(".secondTataCliq .reasonType .slectionRefund input").each(function(i){
						//console.log($(this).val()+"gfdgdgd");
						if($(this).val().length <= "1") {
							if($(".secondTataCliq .accContents").find(".errorText").length <= "0") {
								$(".secondTataCliq .accContents").append("<div class='errorText'>Please fill out all details to avail refund.</div>");
							} 
							else 
							{
								$(".secondTataCliq .accContents .errorText").show();
							}
								return false;
						} else {
							$(".secondTataCliq .accContents .errorText").hide();
							
								$(".thirdTataCliq").addClass("removeMargin");
								$(".secondTataCliq").addClass("removeMargin");
								$(".thirdTataCliq .returnMethod").show();
								$(".secondTataCliq .reasonType").hide();
								$(".thirdTataCliq .returnMethod .selectReturnMethod").addClass("greyColor");
								$(".thirdTataCliq .returnMethod .quick").addClass("blackColor").removeClass("greyColor");
								$(".thirdTataCliq .returnMethod .scheduled").removeClass("blackColor").addClass("greyColor");
								$(".thirdTataCliq .returnMethod .self").removeClass("blackColor").addClass("greyColor");
								if($(".thirdTataCliq .returnMethod .quickDrop").length > 0){
									$(".thirdTataCliq .returnMethod .quickDrop").show();
									$(".thirdTataCliq .returnMethod .selfCourier, .thirdTataCliq .returnMethod .scheduledPickup").hide();
								}else{
									changeRadioColor('scheduled');//$(".thirdTataCliq .returnMethod .selfCourier, .thirdTataCliq .returnMethod .scheduledPickup").show();
								}
								
								getQuickDropData();
						       // getScheduledPikupData();
								
							
						}
					});
					}
				
				
						
					} else {
						/*if(checkCODValidations())
						{*/
							$(".thirdTataCliq").addClass("removeMargin");
							$(".secondTataCliq").addClass("removeMargin");
							$(".thirdTataCliq .returnMethod").show();
							$(".secondTataCliq .reasonType").hide();
							$(".thirdTataCliq .returnMethod .selectReturnMethod").addClass("greyColor");
							$(".thirdTataCliq .returnMethod .quick").addClass("blackColor").removeClass("greyColor");
							$(".thirdTataCliq .returnMethod .scheduled").removeClass("blackColor").addClass("greyColor");
							$(".thirdTataCliq .returnMethod .self").removeClass("blackColor").addClass("greyColor");
							$(".thirdTataCliq .returnMethod .quickDrop").show(); 
							//alert($(".selectReturnMethod .quickDropRadio").length);
							if($(".selectReturnMethod .quickDropRadio").length == 0){
								changeRadioColor('scheduled');
								$(".thirdTataCliq .returnMethod .selfCourier").hide();
							}else{
							$(".thirdTataCliq .returnMethod .selfCourier, .thirdTataCliq .returnMethod .scheduledPickup").hide();
							}
							if(true) { // Trigger Quick Drop True Flag
								getQuickDropData();
							} else {
								getScheduledPikupData();
							}
						/*}*/
					}
				}
				catch(e)
				{
					console.log(e);
				}
				hideRspShowRss();
				} else {
				$(".secondTataCliq .accContents").append("<div class='errorTextSelection'>Please select atleast one Return Type.</div>");
			}
		} 
		
		function getScheduledPikupData() {
			changeRadioColor('scheduled');
			$(".thirdTataCliq  .quick").hide();
			$(".thirdTataCliq  .scheduled  input").prop("checked", true);
		}

		function getQuickDropData() {
			try {
				var dataMap = $("input[name='storeIds']:first")[0]['attributes'][2]['nodeValue'];
				dataMap = dataMap.replace("updatePointersNew","");
				dataMap = dataMap.replace("(","");
				dataMap = dataMap.replace(")","");
				dataMap = dataMap.replace("'","");
				dataMap = dataMap.replace("'","");
				dataMap = dataMap.split(",");
				console.log("Data Map : "+dataMap);
				lat = dataMap[1].replace("'","").replace("'","");
				lng = dataMap[2].replace("'","").replace("'","");
				textDisplay = dataMap[3].replace("'","").replace("'","");
				//updateMap('quickDropRadio0', '5.376964', '100.399383', 'This Map Pointer Click Text');
				updateMap(dataMap[0], lat, lng, textDisplay);
				$( "#QuickDrop" ).prop( "checked", true );
				
				
				//console.log("checking quick drop radio");
				$(".quickDropRadio0 input").prop("checked", true);
			} catch(error) {
				$( "#QuickDrop" ).prop( "checked", true );
				console.log("Error"+error);
				/*$(".quickDropArea").append("<div class='errorText'>No Stores Available.</div>");*/
				updateMap();
				$("#map-canvas").prepend("<div style='padding: 15px 10px;'>No stores availabe to show Map, please try again.</div>");
			}
		}
		
		
		function goBackToSecondTataCliq() {
			$(".secondTataCliq").addClass("removeMargin");
			$(".thirdTataCliq").removeClass("removeMargin");
			$(".thirdTataCliq .returnMethod").hide();
			$(".secondTataCliq .reasonType").show();
			$('.quickDrop .quickDropArea .error_Text').hide();
		}
		
		function updateMap(classId, lat, lng, text) {
			$(".accordtionTataCliq .accContents .quickDrop .quickDropArea .selectquickDrop").addClass("greyColor").removeClass("blackColor");
			$(".thirdTataCliq ."+classId).addClass("blackColor");
			initialise(lat,lng,text);
			$(".accordtionTataCliq .accContents .quickDrop .mapArea").show();
		}
		
		function showPickupTimeDate(classId) {
			$(".accordtionTataCliq .accContents .scheduledPickup .selectScheduledPickup").addClass("greyColor").removeClass("blackColor");
			$(".thirdTataCliq ."+classId).addClass("blackColor");
			$(".accordtionTataCliq .accContents .scheduledPickup .selectDateTime").show();
			//alert($(".update"+classId+" li span.firstName").text());
			//$(".update"+temp+" li span.firstName").text("rajesh");
			$("#hiddenFields #firstName").val($(".update"+classId+" li span.firstName").text());
			$("#hiddenFields #lastName").val($(".update"+classId+" li span.lastName").text());
		    $("#hiddenFields #addressLine1").val($(".update"+classId+" li span.addressline1").text());
			$("#hiddenFields #addressLine2").val($(".update"+classId+" li span.addressline2").text());
			$("#hiddenFields #addressLine3").val($(".update"+classId+" li span.addressline3").text());			
			$("#hiddenFields #landmark").val($(".update"+classId+" li span.landMark").text());
			$("#hiddenFields #pincode").val($(".update"+classId+" li span.postalCode").text());
			$("#hiddenFields #city").val($(".update"+classId+" li span.addressTown").text());
			$("#hiddenFields #stateListBoxForm").val($(".update"+classId+" li span.state").text());
			$("#hiddenFields #country").val($(".update"+classId+" li span.country").text());
			$("#hiddenFields #phoneNumber").val($(".update"+classId+" li span.phoneNumber").text());
			//$("#hiddenFields #addressType").val($("#addAddressForm input[name='addressRadioType']:checked").val());
			
			
		}
		
		$(document).ready(function() {
			$(".thirdTataCliq").addClass("removeMargin");
			$(".accordtionTataCliq .accContents .quickDrop .quickDropArea .selectquickDrop, .accordtionTataCliq .accContents .scheduledPickup .selectScheduledPickup").addClass("greyColor");
		});
		
		
	
		function validateFormReturn() {
		  
		   if($("#reasonSelectBox").val()!=null)
			  {
			   $("#returnReasonvalMsg").hide();
			   return true;
			  }
		   //alert("--"+$("#reasonSelectBox").val());
		   $("#returnReasonvalMsg").show();
		   return false;
		}


		function initialise(lat,lng,textDisplay) {
			var myLatlng = new google.maps.LatLng(lat, lng); // Add the coordinates
			var mapOptions = {
				zoom: 16, // The initial zoom level when your map loads (0-20)
				minZoom: 6, // Minimum zoom level allowed (0-20)
				maxZoom: 17, // Maximum soom level allowed (0-20)
				zoomControl:true, // Set to true if using zoomControlOptions below, or false to remove all zoom controls.
				zoomControlOptions: {
						style:google.maps.ZoomControlStyle.DEFAULT // Change to SMALL to force just the + and - buttons.
				},
				center: myLatlng, // Centre the Map to our coordinates variable
				mapTypeId: google.maps.MapTypeId.ROADMAP, // Set the type of Map
				scrollwheel: false, // Disable Mouse Scroll zooming (Essential for responsive sites!)
				// All of the below are set to true by default, so simply remove if set to true:
				panControl:false, // Set to false to disable
				mapTypeControl:false, // Disable Map/Satellite switch
				scaleControl:false, // Set to false to hide scale
				streetViewControl:false, // Set to disable to hide street view
				overviewMapControl:false, // Set to false to remove overview control
				rotateControl:false // Set to false to disable rotate control
		  	}
			var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions); // Render our map within the empty div
			var image = new google.maps.MarkerImage("data:image/png;base64,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", null, null, null, new google.maps.Size(48,48)); // Create a variable for our marker image.
			var marker = new google.maps.Marker({ // Set the marker
				position: myLatlng, // Position marker to coordinates
				icon:image, //use our image as the marker
				map: map, // assign the market to our map variable
				title: 'Click here for more details' // Marker ALT Text
			});
			// 	google.maps.event.addListener(marker, 'click', function() { // Add a Click Listener to our marker
			//		window.location='http://www.snowdonrailway.co.uk/shop_and_cafe.php'; // URL to Link Marker to (i.e Google Places Listing)
			// 	});
			var infowindow = new google.maps.InfoWindow({ // Create a new InfoWindow
					content: textDisplay // HTML contents of the InfoWindow
				});
			google.maps.event.addListener(marker, 'click', function() { // Add a Click Listener to our marker
					infowindow.open(map,marker); // Open our InfoWindow
				});
			google.maps.event.addDomListener(window, 'resize', function() { map.setCenter(myLatlng); }); // Keeps the Pin Central when resizing the browser on responsive sites
		}

		
		$(document).ready(function() {
			$(".changeAddressLinkPop").click(function() {
				$(".errorCodemessage").hide();
				$("#addAddressForm .errorText").hide();
				$("#changeAddressPopup, .wrapBG").fadeIn(300);
				var height = $(window).height();
				$(".wrapBG").css("height", height);
				$("#changeAddressPopup, .changeAdddd").css("z-index", "999999");
				$(".wrapBG").css("z-index", "99");
				$(".changeAdddd").css("overflow-y", "hidden");
				
				var className = $(this).attr("data-class");
				//alert(className);
				//alert("Test"+($("."+className+" .addressline1").text())+($("."+className+" .addressline2").text()+$("."+className+" .addressline3").text()));
				$("#popupFields #firstName").val($("."+className+" .firstName").text());
				$("#popupFields #lastName").val($("."+className+" .lastName").text());
				//TISUATSE-125 start
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
				$("#popupFields #addressLine1").val(addressLine1);
				//TISUATSE-125 end
				//$("#popupFields #addressLine1").val($("."+className+" .addressline1").text());
				//$("#popupFields #addressLine2").val($("."+className+" .addressline2").text());
				//$("#popupFields #addressLine3").val($("."+className+" .addressline3").text());
				$("#popupFields #pincode").val($("."+className+" .postalCode").text());
				$("#popupFields #mobileNo").val($("."+className+" .phoneNumber").text());
				 loadPincodeData("edit");
			        var value = $("." + className + " .landmark").text();
					  
					  setTimeout(function(){
					  if($(".address_landmarks option[value='"+value+"']").length > "0") {
						  
						  //alert(value+ " 2 in if x"+$(".address_landmarks option[value='"+value+"']").val());
							  $(".address_landmarks").val("");
							$(".address_landmarks option[value='"+value+"']").prop("selected",true);
							
							} else {
							//alert(value+ " 3 in else");
								if($(".address_landmarks option[value='Other']").length > "0") {
									  $(".address_landmarks").val("Other"); 
									 }else{
										 $(".address_landmarks").val("");  
									 }
								changeFuncLandMark("Other"); 
							$(".address_landmarkOther").val(value);
							
						}
					  
					  });
			        
			       // alert($("." + className + " .landmark").text());
			       // $("#popupFields #landmark").val($("." + b + " .landmark").text());
				$("#popupFields #city").val($("."+className+" .addressTown").text());
				$("#popupFields #stateListBox").val($("."+className+" .state").text());
				$("#popupFields #country").val($("."+className+" .country").text());
				$("#popupFields #addressId").val($("."+className+" #addressUniqueId").text());
				$("#popupFields #temp").val(className);
				$("#saveAddress").attr("data-value","editAddress");
				$("#addAddressForm h4.newAddress").hide();
				$("#addAddressForm h4.editAddress").show();
				
			});

			$(".close").click(function() {
				$("#changeAddressPopup").hide();
				$(".wrapBG").hide();
			});
			
			$("#saveBlockData").click(function(event){
				$("#changeAddressPopup").hide();
				$(".wrapBG").hide();
				//TPR-5273 starts
				 var returnMethod =	 $('input[name="returnMethod"]:checked').parents('.selectReturnMethod').find("div > b").text();
				 
				 if(returnMethod == 'Return to Store'){
					 if(typeof(utag) != "undefined"){
							utag.link({
								link_text: "quick_drop_selected",
								event_type : "quick_drop_selected",
								pin_quick_drop : $('#pin').val()
							});
						}
				 }
				 else if(returnMethod == 'Schedule Pickup'){
						 var scheduleReturnDate =	 $('input[name="scheduleReturnDate"]:checked').val();
						 var scheduleReturnTime =	 $('input[name="scheduleReturnTime"]:checked').val();
						 if(typeof(utag) != "undefined"){
							 utag.link({
								 link_text: "schedule_pickup_selected",
								 event_type : "schedule_pickup_selected",
								 preferred_dop : scheduleReturnDate,
								 preferred_top : scheduleReturnTime
							 });
						 }
				 }
				 else{
					 if(typeof(utag) != "undefined"){
						 utag.link({
							 link_text: "self_courier_selected",
							 event_type : "self_courier_selected",
						 });
					 } 
				 }
				//TPR-5273 ends
			});
			
			$(".addNewAddressPopup").click(function(){
				$("#addAddressForm .errorText").hide();
				$("#changeAddressPopup").show();
				$(".wrapBG").show();
				var height = $(window).height();
				$(".wrapBG").css("height", height);
				$("#changeAddressPopup").css("z-index", "999999");
				$("#saveAddress").attr("data-value","newAddress");
				$("#changeAddressPopup input").each(function(){
					$(this).val("");
				});
				$("#changeAddressPopup #country").val("India");
				$("#addAddressForm h4.newAddress").show();
				$("#addAddressForm h4.editAddress").hide();
				loadPincodeData('new');	
				$("#new-address-option-1").val("Residential");
				$("#new-address-option-2").val("Commercial");
			});
			

			$(".close").click(function() {
				$("#changeAddressPopup").hide();
				$(".wrapBG").hide();
			});
			
			
		});
		
		
		function updatePointersNew(classId, lat, lng, text) {
			console.log(classId);
			console.log(lat+"@@@@@"+lng);
			var dataJson = [];
			$(".accordtionTataCliq .accContents .quickDrop .quickDropArea .selectquickDrop").addClass("greyColor").removeClass("blackColor");
			//$().addClass("blackColor");
			if($("input[name='storeIds']:checked").length > 0) {
				for(var i=0; i < $("input[name='storeIds']:checked").length; i++) {
					console.log($("input[name='storeIds']:checked")[i]);
					var dataMap = $("input[name='storeIds']:checked")[i]['attributes'][2]['nodeValue'];
					dataMap = dataMap.replace("updatePointersNew","");
					dataMap = dataMap.replace("(","");
					dataMap = dataMap.replace(")","");
					dataMap = dataMap.replace("'","");
					dataMap = dataMap.split(",");
					//console.log(pointer);
					//var pointer = jQuery.parseJSON("{'class': "+dataMap['0']+", 'lat': "+dataMap['1']+", 'lng': "+dataMap['2']+", 'text': "+dataMap['3']+"}");
					//var pointer = jQuery.parseJSON("{'class': 'quickDropRadio1', 'lat': '5.376964'', 'lng': '100.399383', 'text': 'This Map Pointer Click Text'}");
					console.log(dataMap);
					var pointer = new Object();
						pointer['class'] = dataMap['0'];
						pointer['lat'] = dataMap['1'];
						pointer['lng'] = dataMap['2'];
						pointer['text'] = dataMap['3'];
					dataJson.push(pointer);
				}
			}
			console.log(dataJson);
			var myLatlng = new google.maps.LatLng('5.376964', '100.399383'); // Add the coordinates
				
			var mapOptions = {
				zoom: 16, // The initial zoom level when your map loads (0-20)
				minZoom: 6, // Minimum zoom level allowed (0-20)
				maxZoom: 17, // Maximum soom level allowed (0-20)
				zoomControl:true, // Set to true if using zoomControlOptions below, or false to remove all zoom controls.
				zoomControlOptions: {
						style:google.maps.ZoomControlStyle.DEFAULT // Change to SMALL to force just the + and - buttons.
				},
				center: myLatlng, // Centre the Map to our coordinates variable
				mapTypeId: google.maps.MapTypeId.ROADMAP, // Set the type of Map
				scrollwheel: false, // Disable Mouse Scroll zooming (Essential for responsive sites!)
				// All of the below are set to true by default, so simply remove if set to true:
				panControl:false, // Set to false to disable
				mapTypeControl:false, // Disable Map/Satellite switch
				scaleControl:false, // Set to false to hide scale
				streetViewControl:false, // Set to disable to hide street view
				overviewMapControl:false, // Set to false to remove overview control
				rotateControl:false // Set to false to disable rotate control
		  	}
			
			var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions); // Render our map within the empty div
			var image = new google.maps.MarkerImage("data:image/png;base64,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", null, null, null, new google.maps.Size(48,48)); // Create a variable for our marker image.
			var bounds = new google.maps.LatLngBounds ();
			for(var i=0; i < dataJson.length; i++) {
				var lat = ""; 
				var lng = ""; 
				var textDisplay = ""; 
				var latlng = "";
				var classId = "";	
				console.log(dataJson[i]['lat']);
				lat = dataJson[i]['lat'].replace("'","").replace("'","");
				lng = dataJson[i]['lng'].replace("'","").replace("'","");
				textDisplay = dataJson[i]['text'].replace("'","").replace("'","");
				classId = dataJson[i]['class'].replace("'","").replace("'","");
				$("."+classId).addClass("blackColor");
				console.log(lat+"@@@@"+lng+"@@@"+textDisplay);
				latlng = new google.maps.LatLng(lat, lng);
			    marker = new google.maps.Marker({
			            position: latlng, icon: image,
			            map: map, title: 'Click here for more details'
			        });
				var infowindow = new google.maps.InfoWindow({ // Create a new InfoWindow
						content: textDisplay // HTML contents of the InfoWindow
					});
				google.maps.event.addListener(marker, 'click', function() { // Add a Click Listener to our marker
						infowindow.open(map,marker); // Open our InfoWindow
					});
				
				bounds.extend(latlng);
			}
			

			map.fitBounds(bounds);
			
		}
		
	
		/*$(document).ready(function(){
			$("#saveBlockData").click(function(){
				
				//alert($("#firstName").val());
				$("#hiddenFields #firstName").val("rajesh");
				$("#hiddenFields #lastName").val("ss");
			    $("#hiddenFields #addressLine1").val("ddd");
				$("#hiddenFields #addressLine2").val("jj");
				$("#hiddenFields #pincode").val("kk");
				$("#hiddenFields #city").val("kk");
				$("#hiddenFields #stateListBox").val("ff");
				$("#hiddenFields #country").val("dd");
				temp = $("#temp").val();
				
				$(".update"+temp+" li span.firstName").text("rajesh");
				
				$(".update"+temp+" li span.lastName").text("rajesh");
				$(".update"+temp+" li span.addressLine1").text("rajesh");
				$(".update"+temp+" li span.addressLine2").text("rajesh");
				$(".update"+temp+" li span.pincode").text("rajesh");
				$(".update"+temp+" li span.city").text("rajesh");
				$(".update"+temp+" li span.stateListBox").text("rajesh");
				$(".update"+temp+" li span.country").text("rajesh");
				
				
			//	console.log("@@@@1 "+$(".update"+temp+" li span.firstName").text());
			

			});
			
		});*/
		
		
		
		
		function checkPopupValidations() {
			console.log('checkPopupValidations()');
			var letters = new RegExp(/^[A-z]*$/);
			var isString = isNaN($("#addAddressForm #mobileNo").val());
			//alert($("#addAddressForm #landmark").prop('disabled'));
			var validate = true;
			$("#addAddressForm .errorText").hide();
			if($("#addAddressForm #firstName").val().length < 2) {
				$("#addAddressForm #firstName + .errorText").show().text("Please enter First name");
				validate = false;
			}else if(letters.test($("#addAddressForm #firstName").val()) == false ){
				$("#addAddressForm #firstName + .errorText").show().text("First Name should contain only alphabets");
				validate=  false;
			}if($("#addAddressForm #lastName").val().length < 2) {
				$("#addAddressForm #lastName + .errorText").show().text("Please enter Last name");
				validate = false;
			}else if(letters.test($("#addAddressForm #lastName").val()) == false){
				$("#addAddressForm #lastName + .errorText").show().text("Last Name should contain only alphabets");
				validate =  false;
			}if($("#addAddressForm #addressLine1").val().length < 2) {
				$("#addAddressForm #addressLine1 + .errorText").show().text("Please enter Address Line");
				validate =  false;
			}
			if($("#addAddressForm #pincode").val().length < 6) {
//alert($("#addAddressForm #pincode").val());
				$("#addAddressForm #pincode + .errorText").show().text("Please enter valid pincode");
				validate =  false;
			}if($("#addAddressForm #mobileNo").val().length < 10) {
				$("#addAddressForm #mobileNo + .errorText").show().text("Please enter valid Mobile number");
				validate =  false;
			}else if(isString==true || $("#addAddressForm #mobileNo").val().trim()==''){
				$("#addAddressForm #mobileNo + .errorText").show().text("Enter only Numbers");
				validate =  false;
			}
			
			/* 
			TISRLEE-1655
			else if($("#addAddressForm #addressLine2").val().length < 2) {
				$("#addAddressForm .errorText").show().text("Please enter Address Line 2");
				return false;
			}else if($("#addAddressForm #addressLine3").val().length < 2) {
				$("#addAddressForm .errorText").show().text("Please enter Address Line 3");
				return false;
			}
			TISRLEE-1655
			*/ 
			if($("#addAddressForm #city").val().length < 2) {
				$("#addAddressForm #city + .errorText").show().text("Please enter City");
				validate =  false;
			}
			//alert($("#addAddressForm #stateListBox").val()); 
			if($("#addAddressForm #stateListBox").val() == null || $("#addAddressForm #stateListBox").val().length < 2) {

				$("#addAddressForm #stateListBox + .errorText").show().text("Please enter State");
				validate =  false;
			}
			 if($("#addAddressForm #country").val().length < 2) {
				$("#addAddressForm #country + .errorText").show().text("Please enter Country");
				validate =  false;
			}
			return validate;
		}
		
		
		function showAddressPopup(addressId) {
			//alert('showAddressPopup');
			$("#addAddressForm .errorText").hide();
			$("#changeAddressPopup, .wrapBG").fadeIn(300);
			var height = $(window).height();
			$(".wrapBG").css("height", height);
			$("#changeAddressPopup").css("z-index", "999999");
			var className = addressId;
			//alert(className);
			console.log("Test"+$(".update"+className+" .lastName").text());
			$("#popupFields #firstName").val($("."+className+" .firstName").text());
			$("#popupFields #lastName").val($("."+className+" .lastName").text());
			$("#popupFields #addressLine1").val($("."+className+" .addressline1").text());
			$("#popupFields #addressLine2").val($("."+className+" .addressline2").text());
			$("#popupFields #addressLine3").val($("."+className+" .addressline3").text());
			$("#popupFields #pincode").val($("."+className+" .postalCode").text());
			$("#popupFields #mobileNo").val($("."+className+" .phoneNumber").text());
			$("#popupFields #landmark").val($("."+className+" .landmark").text());
			$("#landmark option").each(function() {
			   // alert(this.text + ' ' + this.value);
			});
			//alert($("#landmark option").length);
			$("#popupFields #city").val($("."+className+" .addressTown").text());
			$("#popupFields #stateListBox").val($("."+className+" .state").text());
			$("#popupFields #country").val($("."+className+" .country").text());
			$("#popupFields #addressId").val($("."+className+" #addressUniqueId").text());
			$("#popupFields #temp").val(className);
			$("#saveAddress").attr("data-value","editAddress");
		}
		
		$("#saveAddress" ).click(function(e) {
			
		  e.preventDefault();
		  
		  var addressType = $(this).attr("data-value");
		  
		  if(addressType == "newAddress") {
			//  alert("Saving New Address");
			  var count = $(".scheduledPickupArea .selectScheduledPickup").length;
			  count = parseInt(count) + 1;
			 // alert(count);
			  //alert( ACC.config.encodedContextPath+"/my-account/returns/addNewReturnAddress");
			
			  
			  if(checkPopupValidations()) {
				  $.ajax({
					  url: ACC.config.encodedContextPath+"/my-account/returns/addNewReturnAddress",
					  type: "GET",
					  data : $("#addAddressForm").serialize(),
					  success: function(data) {
						console.log("New Address Saved Successfully");
						  $(".scheduledPickupArea").html("<div class='address"
								  + count+ " col-md-12 col-sm-12 col-xs-12 greyColor selectScheduledPickup blackColor'><div class='col-md-2 col-sm-2 col-xs-2 selectRadio'><input class=radioButton name=selectScheduledPickup onclick='showPickupTimeDate(\"address"
								  + count+ "\")' type='radio' value='schedule'></div><div class='col-md-6 col-sm-6 col-xs-6 updateaddress"
								  +count+ "'><ul><li><span class=firstName>"
								  +$('#addAddressForm  #firstName').val()+"</span>&nbsp;<span class=lastName>"+$('#addAddressForm #lastName').val()+"</span><li><span class=addressline1>"
								  +$('#addAddressForm  #addressline1').val()+"</span><li><span class=addressline2>"+$('#addAddressForm  #addressline2').val()+"</span><li><span class=addressline3>"+$('#addAddressForm  #addressline3').val()+"</span><li><span class=addressTown>"
								  +$('#addAddressForm  #addressTown').val()+"</span><li><span class=state>"+$('#addAddressForm  #state').val()+"</span><li><span class=postalCode>"
								  +$('#addAddressForm  #pincode').val()+"</span><li><span class=country>"
								  +$('#addAddressForm  #country').val()+"</span><li><span class=phoneNumber>"
								  +$('#addAddressForm  #mobileNo').val()+"</span><li id=addressUniqueId style=display:none></ul></div><div class='col-md-4 col-sm-4 col-xs-4 editAddress'><a class=changeAddressLink onclick='showAddressPopup(\"address"
								  + count+ "\")' data-class=address"
								  + count+ " href=#>Edit Address</a></div></div>");
			  showPickupTimeDate("address"+count);
			  $(".address"+count+" input").prop("checked", true);
			  $("#changeAddressPopup, .wrapBG").fadeOut(300);
			  $(".scheduledPickupArea #spaAddress").remove();
			  $(".scheduledPickupArea").prepend("<p style='color:#a9143c;'>Address Saved Successfully</p>");
			  
					  },
					  error:function(data){
						  console.log("Error in NewAddress");
					  }
				  });
							
			  }
		  }else {
			  console.log(ACC.config.encodedContextPath+"/my-account/returns/editReturnAddress");

			  
			  if(checkPopupValidations()) {
				  $.ajax({
					  type: "POST",
					  data : $("#addAddressForm").serialize(), 
					  url: ACC.config.encodedContextPath+"/my-account/returns/editReturnAddress",
					  success: function(data) {
						//  alert("New Address");
						  
						 // alert("saving Address");
						 // console.log($("#addAddressForm #firstName").val());
						 // console.log($("#addAddressForm #lastName").val());
						  
						    if(data.title == null )
							{
						        $("#changeAddressPopup, .wrapBG").fadeOut(300);
						        $("#hiddenFields #firstName").val($("#addAddressForm #firstName").val());
								$("#hiddenFields #lastName").val($("#addAddressForm #lastName").val());
							    $("#hiddenFields #addressLine1").val($("#addAddressForm #addressLine1").val());
								$("#hiddenFields #addressLine2").val($("#addAddressForm #addressLine2").val());
								$("#hiddenFields #addressLine3").val($("#addAddressForm #addressLine3").val());
								if($("#addAddressForm #landmark").val() != "Other"){
									$("#hiddenFields #landmark").val($("#addAddressForm #landmark").val());
								}else{
									$("#hiddenFields #landmark").val($("#addAddressForm #otherLandmark").val());
								}
								$("#hiddenFields #pincode").val($("#addAddressForm #pincode").val());
								$("#hiddenFields #city").val($("#addAddressForm #city").val());
								$("#hiddenFields #stateListBoxForm").val($("#addAddressForm #stateListBox").val());
								$("#hiddenFields #country").val($("#addAddressForm #country").val());
								$("#hiddenFields #phoneNumber").val($("#addAddressForm #mobileNo").val());
								$("#hiddenFields #addressType").val($("#addAddressForm input[name='addressRadioType']:checked").val());
								
								temp = $("#temp").val();
								
								$(".update"+temp+" li span.firstName").text($("#addAddressForm #firstName").val());
								$(".update"+temp+" li span.lastName").text($("#addAddressForm #lastName").val());
								$(".update"+temp+" li span.addressLine1").text($("#addAddressForm #addressLine1").val());
								$(".update"+temp+" li span.addressLine2").text($("#addAddressForm #addressLine2").val());
								$(".update"+temp+" li span.addressLine3").text($("#addAddressForm #addressLine3").val());
								$(".update"+temp+" li span.postalCode").text($("#addAddressForm #pincode").val());
								if($("#addAddressForm #landmark").val()!="Other"){
									$(".update"+temp+" li span.landmark").text($("#addAddressForm #landmark").val());
								}else{
									$(".update"+temp+" li span.landmark").text($("#addAddressForm #otherLandmark").val());
								}
								$(".update"+temp+" li span.city").text($("#addAddressForm #city").val());
								$(".update"+temp+" li span.stateListBox").text($("#addAddressForm #stateListBox").val());
								$(".update"+temp+" li span.country").text($("#addAddressForm #country").val());
								$(".update"+temp+" li span.phoneNumber").text($("#addAddressForm #mobileNo").val());
								
								$(".update"+temp).prev().find("input").prop("checked", "checked");
								showPickupTimeDate(temp);
								$(".scheduledPickupArea #spaAddress").remove();
								$(".scheduledPickupArea").prepend("<p id='spaAddress' style='color:#a9143c;'>Address Updated Successfully</p>");
							
							
								//alert("saving Address");
								  console.log($("#addAddressForm #firstName").val());
								  console.log($("#addAddressForm #lastName").val());
								  
									$("#hiddenFields #firstName").val($("#addAddressForm #firstName").val());
									$("#hiddenFields #lastName").val($("#addAddressForm #lastName").val());
								    $("#hiddenFields #addressLine1").val($("#addAddressForm #addressLine1").val());
									$("#hiddenFields #addressLine2").val($("#addAddressForm #addressLine2").val());
									$("#hiddenFields #addressLine3").val($("#addAddressForm #addressLine3").val());
									
									if($("#addAddressForm #landmark").val()!="Other"){
										$("#hiddenFields #landmark").val($("#addAddressForm #landmark").val());
									}else{
										$("#hiddenFields #landmark").val($("#addAddressForm #otherLandmark").val());
									}
									
									
									$("#hiddenFields #pincode").val($("#addAddressForm #pincode").val());
									$("#hiddenFields #city").val($("#addAddressForm #city").val());
									$("#hiddenFields #stateListBoxForm").val($("#addAddressForm #stateListBox").val());
									$("#hiddenFields #country").val($("#addAddressForm #country").val());
									$("#hiddenFields #phoneNumber").val($("#addAddressForm #mobileNo").val());
									$("#hiddenFields #addressType").val($("#addAddressForm input[name='addressRadioType']:checked").val());
									
									temp = $("#temp").val();
									
									$(".update"+temp+" li span.firstName").text($("#addAddressForm #firstName").val());
									$(".update"+temp+" li span.lastName").text($("#addAddressForm #lastName").val());
									$(".update"+temp+" li span.addressLine1").text($("#addAddressForm #addressLine1").val());
									$(".update"+temp+" li span.addressLine2").text($("#addAddressForm #addressLine2").val());
									$(".update"+temp+" li span.addressLine3").text($("#addAddressForm #addressLine3").val());
									$(".update"+temp+" li span.otherLandmark").text($("#addAddressForm #otherLandmark").val());
									if($("#addAddressForm #landmark").val()!="Other"){
										$(".update"+temp+" li span.landmark").text($("#addAddressForm #landmark").val());
									}else{
										$(".update"+temp+" li span.landmark").text($("#addAddressForm #otherLandmark").val());
									}
									
									$(".update"+temp+" li span.postalCode").text($("#addAddressForm #pincode").val());
									/*TISPRDT-1071 Start*/
									$(".update"+temp+" li span.addressTown").text($("#addAddressForm #city").val());
									$(".update"+temp+" li span.state").text($("#addAddressForm #stateListBox").val());
									/*TISPRDT-1071 End*/
									$(".update"+temp+" li span.country").text($("#addAddressForm #country").val());
									$(".update"+temp+" li span.phoneNumber").text($("#addAddressForm #mobileNo").val());
									
									$(".update"+temp).prev().find("input").prop("checked", "checked");
									//alert($(".update"+temp+" li span.landmark").text()+" li span.landmark");
									showPickupTimeDate(temp);
							}
						  else
							{
							$('.errorCodemessage').show(); 
							$(".errorCodemessage").text(data.title);
							 $("#hideRsp").text("true");//TPR-7140
							}
							
							//$("#changeAddressPopup, .wrapBG").fadeOut(300);
					  
					  
				  
					  },
					  error:function(data){
						  
						  console.log("Error in ajax of Editing Address");
						  
					  }
				  });
					//$("#changeAddressPopup, .wrapBG").fadeOut(300);
			  }
		  }
		  
				
		/*$.ajax({
		  type: "POST",
		  data : $("#addAddressForm").serialize(), 
		  url: "http://localhost:9001/store/my-account/returns/editReturnAddress",
		  success: function(data)
		  {
			  alert(data.title);
			  console.log(data.title);
			 
			  alert(data.firstName);
			  alert(data.phoneNumber);
				$("#hiddenFields #firstName").val(data.firstName);
				$("#hiddenFields #lastName").val(data.lastName);
			    $("#hiddenFields #addressLine1").val(data.line1);
				$("#hiddenFields #addressLine2").val(data.line2);
				$("#hiddenFields #pincode").val(data.postalCode);
				$("#hiddenFields #city").val(data.city);
				$("#hiddenFields #stateListBox").val(data.state);
				$("#hiddenFields #country").val(data.country.name);
				$("#hiddenFields #phoneNumber").val(data.phoneNumber);
				temp = $("#temp").val();
				
				$(".update"+temp+" li span.firstName").text(data.firstName);
				$(".update"+temp+" li span.lastName").text(data.lastName);
				$(".update"+temp+" li span.addressLine1").text(data.line1);
				$(".update"+temp+" li span.addressLine2").text(data.line2);
				$(".update"+temp+" li span.pincode").text(data.postalCode);
				$(".update"+temp+" li span.city").text(data.city);
				$(".update"+temp+" li span.stateListBox").text(data.state);
				$(".update"+temp+" li span.country").text(data.country.name);
				$(".update"+temp+" li span.phoneNumber").text(data.phoneNumber);
 
		  },
		  error:function(data){
		  }
		});*/

		 
		});
		
		/*$.ajax({
		  type: "GET",
		  url: "http://localhost:9001/store/my-account/returnToStore",
		  success: function(data)
		  {
			  console.log(data);
			 
			  //item.geoPoint.latitude, item.geoPoint.longitude
			  //updateMap(" quickdropradio3',="" 17.4249653,="" 78.6055046,="" 'this="" map="" pointer="" click="" text')'="
			  var dataLoaded = $(".quickDrop .quickDropArea").attr("data-loaded");
			  if(dataLoaded == "false") {
				  $.each(data, function(i, item){
					 //console.log(i+"@@@"+item.geoPoint.latitude+"@@@"+item.geoPoint.longitude+"@@@"+item);
					 var quickAreaDiv = "<div class='selectquickDrop greyColor quickDropRadio"+i+" col-md-12'>";
					 var quickAreaDivEnd = "</div>";
					 var radioOnClick = "\"updateMap(\'quickDropRadio"+i+"\', \'"+item.geoPoint.latitude+"\', \'"+item.geoPoint.longitude+"\', \'This Map Pointer Click Text\')\"";
					 var inputRadio = "<div class='selectRadio col-md-2 col-sm-2 col-xs-2'><input class='radioButton' type='radio' value='replace' onclick="+radioOnClick+" name='QuickDrop' /></div>";
					 var address = "<div class='col-md-10 col-sm-10 col-xs-10'><b>Croma</b> <br /> <span>67/71, Krishnaswamy Road, Brookefields Mall, Coimbatore</span></div>";
					 $(".quickDropArea").append(quickAreaDiv+inputRadio+address+quickAreaDivEnd);
					 if(i=="0") {
						 updateMap('quickDropRadio0',item.geoPoint.latitude,item.geoPoint.longitude,'This Map Pointer Click Text');
						 $(".quickDropRadio0 input").attr('checked', true);
						 $(".quickDrop .quickDropArea").attr("data-loaded", "true");
					 }
				  });
			  }
			  
			  //$(".quickDropArea").append("<script>function updateMapAjax(a, b, c, d) {console.log('Update Map'); $('.accordtionTataCliq .accContents .quickDrop .quickDropArea .selectquickDrop').addClass('greyColor').removeClass('blackColor'); $('.thirdTataCliq .' + a).addClass('blackColor'); initialise(b, c, d); $('.accordtionTataCliq .accContents .quickDrop .mapArea').show(); }</script>");
		  },
		  error:function(data){
		  }
		});
*/
		//TPR-4134
$(document).ready(function(){
	$(".revSeal").popover({
	    html: 'true',
	    placement: 'bottom',
	    trigger: 'manual',
	    content: $("#revSealHelpContent").html()
	}).on("mouseenter", function () {
	    var _this = this;
	    $(this).popover("show");
	    $(".popover").on("mouseleave", function () {
	        $(_this).popover('hide');
	    });
	}).on("mouseleave", function () {
	    var _this = this;
	    setTimeout(function () {
	        if (!$(".popover:hover").length) {
	            $(_this).popover("hide");
	        }
	    }, 300);
	});
	
	$(".accordtionTataCliq .accContents .reverseSealJwlry .radioButton").each(function(){
		var idContainer = $(this).attr("id");
		$(this).next("label").attr("for",idContainer);
		});
	
	$(".ifscPopOver").popover({
	    html: 'true',
	    placement: 'bottom',
	    trigger: 'hover',
	    content: $("#ifscPopOverHelp").html()
	});
	
	//TISJEW-3484
	var catType = $("#productCategoryTypeReturn").val();
	if("FineJewellery" == catType){
		$(".accContents.returnMethod .selectReturnMethod.self").css("display","none");
		$(".accContents.returnMethod .selfCourier").css("display","none");
	}
	
});	


function fetchCatSpecificReason(element){
	try{
	var code=$("#returnReason :selected").val();
		  $.ajax({
			  url: ACC.config.encodedContextPath+"/my-account/returns/fetchSubReason",
			  type: "GET",
			  data :"parentReasonCode="+code,
			  success: function(data) {
				  var options = $("#returnSubReason");
				  $("#returnSubReason").html('');				  
				    $.each(data, function(item,obj) {					
				        options.append($("<option />").val(obj.code).text(obj.reasonDescription));
				    });
				    if($.isEmptyObject(data)){
				    	$("select [name=subReasonList]").hide();
				    }
			  },
			  error:function(data){
				  console.log("Error in fetchCatSpecificReason"+data);
			  }
		  });
	}
	catch(e)
	{
		console.log("Error:"+e);
	}
}


function uploadImage(){
	try{
	var ajaxData = new FormData();
	//var code=$("#upload-files").val();
	var files = $('#upload-files').prop('files');
	for(var i=0;i<files.length;i++){
	    ajaxData.append('files['+i+']', files[i]);
	}
	//ajaxData.append('files[]', files);
	//var formData1 = JSON.stringify((ajaxData).serializeArray());
	//ajaxData.append("file", files);
	/*$.each(files, function(key, value)
		    {
		ajaxData.append(key, value);
		    });*/
	
	
	alert ('fsdgfd');
	var outputLog = {}, iterator = ajaxData.entries(), end = false;
	while(end == false) {
	   var item = iterator.next();
	   if(item.value!=undefined) {
	       outputLog[item.value[0]] = item.value[1];
	   } else if(item.done==true) {
	       end = true;
	   }
	    }
	console.log(outputLog);
		  $.ajax({
			  url: ACC.config.encodedContextPath+"/my-account/returns/uploadImg?files="+JSON.stringify(ajaxData),
			  type: "POST",
              contentType: false,
              processData: false,
             // data :{'files' : ajaxData},
			  success: function(data) {
				 console.log("success");
			  },
			  error:function(data){
				  console.log("Error in fetchCatSpecificReason"+data);
			  }
		  });
	}
	catch(e)
	{
		console.log("Error:"+e);
	}
}

$('#fileUploader').on('change', uploadFile);


function uploadFile(event)
	{
	    //event.stopPropagation(); 
	    //event.preventDefault(); 
	var files = $('#upload-files').prop('files');
	    var data = new FormData();
	    $.each(files, function(key, value)
	    {
	        data.append(key, value);
	    });
	    postFilesData(data); 
	 }
	
function postFilesData(data)
	{
	 $.ajax({
        url: ACC.config.encodedContextPath+"/my-account/returns/uploadImg",
        type: 'POST',
        data: data,
        cache: false,
        dataType: 'json',
        processData: false, 
        contentType: false, 
        success: function(data, textStatus, jqXHR)
        {
        	//success
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            console.log('ERRORS: ' + textStatus);
        }
	    });
	}
		

//TPR-7140		
function hideRspShowRss(){
	var hideRsp=$("#hideRsp").text();
	if(hideRsp=="true")
	{
		$(".scheduled").hide();
		changeRadioColor('self');
		$("input[name='returnMethod']").prop( "checked", false );
		$(".selectReturnMethod.self").show();
		$(".selectReturnMethod.self input[name='returnMethod']").prop( "checked", true );
	}
}


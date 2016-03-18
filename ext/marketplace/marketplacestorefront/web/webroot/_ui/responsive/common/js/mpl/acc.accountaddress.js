
/*-------------- For account address --------------*/

/*--------------Added to create a common tag for left navigation in my account-----------*/
 $(document).ready(function(){
	 /*To highlight the active link*/
	 
		var pageName = $("#pageName").val();
		if(pageName=="overViews"){
       		$("#lnOverView a").addClass("active"); 		
       	}
       	else if(pageName=="mplPref"){
       		$("#lnMplPref a").addClass("active"); 		
       	}
       	else if(pageName=="addressBook"){
       		$("#lnAddress a").addClass("active"); 		
       	}
       	else if(pageName=="orderDetail"){
       		$("#lnOrder a").addClass("active"); 		
       	}
       	else if(pageName=="orderHistory"){
       		$("#lnOrder a").addClass("active"); 		
       	}
       	else if(pageName=="savedCards"){
       		$("#lnSavedCards a").addClass("active"); 		
       	}
       	else if(pageName=="personalInfo"){
       		$("#lnUpdateProfile a").addClass("active"); 		
       	}
       	else if(pageName=="invite"){
       		$("#lnInvite a").addClass("active"); 		
       	}
       	else if(pageName=="coupons"){
       		$("#lnCoupons a").addClass("active"); 		
       	}
       	else if(pageName=="review"){
       		$("#lnReview a").addClass("active"); 		
       	}
 });
 /*-----------End of Left Nav script -----------------*/
 
function editAddress(addressId) {
       var requiredUrl = ACC.config.encodedContextPath+"/my-account/populateAddressDetail";
       var dataString = "&addressId="+addressId;
 
       $.ajax({
    	   url: requiredUrl,
    	   type: "GET",
    	   data: dataString,
    	   dataType : "json",
    	   cache: false,
    	   contentType : "application/json; charset=utf-8",
    	   success : function(data) {
   				$('#addressId').val(addressId);
   				$('#firstName').val(data.firstName);
   				$('#lastName').val(data.lastName);
   				$('#line1').val(data.line1);
   				$('#line2').val(data.line2);
   				$('#line3').val(data.line3);
   				$('#postcode').val(data.postcode);
   				$('#townCity').val(data.townCity);
   				$('#mobileNo').val(data.mobileNo);
   				$('#stateListBox').val(data.state);
   				if(data.addressType=="Home")
   				{
   					document.getElementById("new-address-option-1").checked= true;
   				}
   				if(data.addressType=="Work")
				{
					document.getElementById("new-address-option-2").checked= true;
				}
   				
   				$("#headerAdd").css("display","none");
   				$("#headerEdit").css("display","block");
   				
   				$("#addNewAddress").css("display","none");
   				$("#edit").css("display","block");
    	   },
    	   error : function(data) {
    		   	console.log(data.responseText) 
    	   }
       });
    } 
    
    $(document).ready(function(){    	
    	$("#addNewAddress").css("display","block");
    	$('#firstName').val("");
    	$('#lastName').val("");
    	$("#headerAdd").css("display","block");
    	$("#headerEdit").css("display","none");
    	$("#edit").css("display","none");
    	$("#edit").click(function(){
    		document.getElementById("addressForm").action = "editAddress";
    		document.getElementById("addressForm").commandName = "addressForm"; 		
    	});
    	
    	$("#checkBox1").click(function(){
    		var style=null;
    		if($(this).is(":checked")) {
    			$(this).parent().css("color","#000");
    			$(this).parent().find('label').css("color","#a9143c");
    		} else {
    			$(this).parent().css("color","#a9a9a9");
    			$(this).parent().find('label').css("color","#a9a9a9");
    		}
    	});
    	$("#new-address-option-1").prop("checked",true);
    });
    
/*-------------- For account address --------------*/
    
    
    $(".submit-request").click(function(){
    	if($("#reasonSelectBox").val()==null)
    	{
    		alert("Please select a reason");
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    });
    
    $("#returnReject").click(function(){
    	window.location=ACC.config.encodedContextPath + "/my-account"+"/orders";
    });
    
    

    
/*-------------- For account address --------------*/

    
    function refreshModal(bogoCheck,transactionId){
    	$("#cancellationreasonSelectBox_"+transactionId+" option#defaultReason").attr("selected","selected");
    	if(bogoCheck=='true'){
    		alert("All the related products in the promotion will get cancelled");
    	}
    }
    
    var Rejectionselectedvalue=null;
    
    function setDropDownValue(transactionId)
    {
    	Rejectionselectedvalue = $("#cancellationreasonSelectBox_"+transactionId+" option:selected").val();
    }
    
    $(document).ready(function(){
//    	var Rejectionselectedvalue=null;
//    	$(".questions #cancellationreasonSelectBox").change(function(){
//    		Rejectionselectedvalue=$(this).val();
//    		Rejectionselectedvalue=$(".questions #cancellationreasonSelectBox option:selected").val();
//		});
    	$(".cancel-confirm,.cancel-confirm-detail").click(function(){
    		
    		var formId=$(this).parents(".return-form").attr("id");
    		
    		var orderCode=$("#"+formId+" .subOrderCodeClass").val();
    		var ussid=$("#"+formId+" .ussidClass").val();
    		var transactionId=$("#"+formId+" .transactionIdClass").val();
    		var ticketTypeCode=$("#"+formId+" .ticketTypeCodeClass").val();
    		var entryNumber=$("#entryNumber").val();
    		
    		var nowValue = $("#cancellationreasonSelectBox_"+transactionId+" option:selected").val();
    		var reasonCode=Rejectionselectedvalue;
    		console.log("Reasone code : "+Rejectionselectedvalue);
        	if(reasonCode==null)
        	{
        		alert("Do let us know why you would like to cancel this item.");
        		return false;
        	}
        	else if(nowValue !=Rejectionselectedvalue)
        	{
        		alert("Do let us know why you would like to cancel this item.");
        		return false;
        	}	
        	else
        	{
        		
        		
        		$.ajax({
        			url: ACC.config.encodedContextPath + "/my-account/cancelSuccess",
        			type: "GET",
        			beforeSend:function() {
        				$("body").append("<div id='no-click' style='opacity:0.40; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div><img src='/store/_ui/responsive/common/images/spinner.gif' class='spinner' style=' z-index: 10001;position: fixed;top: 50%;left:50%;height: 30px;'>");
        			},
        			data: { 'orderCode' : orderCode, 'transactionId' : transactionId, 'reasonCode' : reasonCode, 'ticketTypeCode' : ticketTypeCode, 'ussid' : ussid },
        			cache: false,
        			success : function(response) {
        				//alert(response);
        				splitData = response.split("|");
        				var result = splitData[0];
        				var bogoreason = splitData[1];
        				var reasonDesc = splitData[2];
        				if(result == "success"){
        					
        					$(".cancellation-request-block #resultTitle").text("Say goodbye!");
        					$(".cancellation-request-block #resultDesc").text("You've managed to cancel your order sucessfully. More power to you.");
        					$(".reason").css("display","block");
        					$(".reason #reasonTitle").text("Reason for Cancellation:");
        					$(".reason #reasonDesc").text(reasonDesc);
        					$("body .spinner,body #no-click").remove();
        				}
        				else{
        					$(".cancellation-request-block #resultTitle").text("Failure!");
        					$(".cancellation-request-block #resultDesc").text(bogoreason);
        					$(".reason").css("display","none");
        					$("body .spinner,body #no-click").remove();
        				}
        				
        				//$("#cancelOrder"+orderCode).modal('hide');
        				//$("#cancelSuccess"+orderCode+ussid).modal('show');
           				
        				$("#cancelOrder"+orderCode).hide();
        				$("#cancelSuccess"+orderCode+ussid).show();
        			},
        			complete:function() {
        				$("body .spinner,body #no-click").remove();
        			},
        			error : function(resp) {
        				alert("Error");
        				$("body .spinner,body #no-click").remove();
        			}
        		});
        	}
        	event.preventDefault();
    	});
    	
    });

    
    
    
    $(document).ready(function() {
    	var loc = window.location;
        var pathName = loc.pathname.substring(loc.pathname.lastIndexOf('/') + 1, loc.pathname.length);
    	var mainPreferenceUrl = ACC.config.encodedContextPath + "/my-account/marketplace-preference";
    	var mainProfileUrl = ACC.config.encodedContextPath + "/my-account/update-profile";
//    	if(pathName == 'marketplace-preference'){
//    		changeUrl(mainPreferenceUrl);
//    	}
//    	if(pathName == 'update-profile'){
//    		changeUrl(mainProfileUrl);
//    	}
    	
    	$("#isUnsubcribed").val(false);
    	
//    	if($("#radioInterest1").is(":checked")) {
//    		alert("asdasjdsaj");
//			$("#radioInterest1").click();
//		}
    	
    	
    	
    	
    	if($("#radioInterest0").is(":checked")) {
    		$("#isUnsubcribed").val(false);
		}
    	$("#saveMarketPrefButton").click(function(){
    		var isUnsubcribed = $("#isUnsubcribed").val();
    		var favoriteCategories = [];
    		var favoriteBrands = [];
    		var feedbackArea = [];
    		var myInterest = "";
    		var emailFrequency = "";
    		
    		$("input[name='interest']:checked").each(function() {
    			myInterest = this.value;
    		});
    		
    		$("input[name='frequency']:checked").each(function() {
    			emailFrequency = this.value;
    		});
    		
    		$.each($("input[name='categoryData']:checked"), function(){            
    			favoriteCategories.push($(this).val());
    		});
    		$.each($("input[name='brandData']:checked"), function(){            
    			favoriteBrands.push($(this).val());
    		});
    		$.each($("input[name='feedbackArea']:checked"), function(){            
    			feedbackArea.push($(this).val());
    		});
//    		alert("Chosen Interest: " + myInterest
//    				+"\n"+"Chosen Categories: " + favoriteCategories.join(",")
//    				+"\n"+"Chosen Brands: " + favoriteBrands.join(",")
//    				+"\n"+"Chosen Frequency: " + emailFrequency
//    				+"\n"+"Chosen FeedBackAreas: " + feedbackArea.join(","));
    		var dataString="interest="+myInterest
							+"&categoryData="+JSON.stringify(favoriteCategories)
							+"&brandData="+JSON.stringify(favoriteBrands)
							+"&frequency="+emailFrequency
							+"&feedBackArea="+JSON.stringify(feedbackArea)
							+"&isUnsubscibed="+isUnsubcribed;

//    		alert("Data to Controller: \n" + dataString);
    		var requiredUrl = ACC.config.encodedContextPath + "/my-account/saveMplPreferences";  

    		jQuery.ajax({
    			type: 'GET',
    			contentType: "application/json; charset=utf-8",
    			url: requiredUrl,
    			data: dataString, 	
    			success: function(data){
    				if(data=="success" || data=="unsubscribed"){
    					window.location.href=ACC.config.encodedContextPath+"/my-account/marketplace-preference?param="+data;
    				}
    			},
    			error: function(){
    				alert("Something is not right! Please try after sometime");
    			}
    		});
    	});
    	
    	$("#unsubcribe-link").click(function(){
    		$("#radioInterest1").prop("checked", true);
    		$("#isUnsubcribed").val(true);
        	if($("#radioInterest1").is(":checked")) {
    			$("#radioInterest1").click();
    			$.each($("input[name='categoryData']:checked"), function(){            
        			$(this).prop("checked", false);
        		});
    			$.each($("input[name='brandData']:checked"), function(){            
    				$(this).prop("checked", false);
        		});
        		$.each($("input[name='feedbackArea']:checked"), function(){            
        			$(this).prop("checked", false);
        		});
        		$("input[name='frequency']:checked").each(function() {
        			$(this).prop("checked", false);
        		});
    		}
    	});
    });

    
    function changeUrl(url){
    	if (typeof (history.pushState) != "undefined") {
			var obj = {
					Url : url
			};
			history.pushState(obj, obj.Title, obj.Url);
		} else {
			alert("ERROR!!!");
		}
    }
    
 /*---------------- Invite Friends -------------------*/ 
    
    $(document).ready(function() {
    	$("#inviteFriends").click(function(){
    		var proceed = false;
    		var friends_email_List = [];
    		var email = $("#friendsEmail").val();
    		if(email.length>0){
    			if(email.indexOf(",")>=0){
    				splitData = email.split(",");
    				splitLen = splitData.length;
    				for(var i=0; i<splitLen; i++){
    					if (validateEmailInvite(splitData[i])) {
    						proceed=true;
    						friends_email_List.push(splitData[i]);
    					}
    					else{
    						proceed=false;
    					}
    				}
    			}
    			else{
    				if (validateEmailInvite(email)){
						proceed=true;
						friends_email_List.push(email);
					}
					else{
						proceed=false;
					}
    			}
    			if(proceed){
    				var textMessage = $("#mytextarea").val();
    				var dataString="friends_email_List="+JSON.stringify(friends_email_List)+"&textMessage="+textMessage;
    				var requiredUrl = ACC.config.encodedContextPath + "/my-account/inviteFriends";  

    				jQuery.ajax({
    					type: 'GET',
    					contentType: "JSON",
    					url: requiredUrl,
    					data: dataString, 
    					success: function(data){
    						if(data=="success"){
    							$('#friendsEmail').val("");
    							document.getElementById("errfemail").innerHTML = "<font color='green' size='2'>Invite is sent successfully</font>";
    						}
    						if(data=="error_email_sending"){
    							document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>Error in email sending</font>";
    						}
    						if(data=="already_registered_email"){
    							document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>One or more entered email id is/are already registered</font>";
    						}
    						if(data=="customer_email"){
    							document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>One or more email is/are same as user's email id</font>";
    						}
    					},
    					error: function(){
    						alert("Something is not right! Please try after sometime");
    					}
    				});
    			}
    			else{
    				document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>Please enter one or more valid email id(s) (for multiple - Separated with commas (,))</font>";
    			}
    		}
    		else{
    			document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>Please enter one or more email id(s)</font>";
    		}
    	});
    });
 
    function kpressfemail(){
    	document.getElementById("errfemail").innerHTML = "";
    }
    function validateEmailInvite(email) {
    	var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    	//return emailRegex.test(email);
    	
    	if(!emailRegex.test(email)){
    		return false;
    	}
    	else if(emailRegex.test(email)){
    		if(email.length>140){
    			document.getElementById("errfemail").innerHTML = "<font color='red' size='2'>Please enter valid length for email id </font>";
    			return false;
    		}
    		else{
    			return true;
    		}
    	}
    	
    }
    /*---------------- Invite Friends -------------------*/
    

//    function automateMyrecomendationBrandModification(catids){
//    	var categoryData="categoryData="+catids;
//    	var requiredUrl = ACC.config.encodedContextPath + "/my-account"+"/myInterest"+"/brands";  
//    	var contentPath = "/store/_ui";
//    	jQuery.ajax({
//    		type: "GET",
//    		contentType: "JSON",
//    		url: requiredUrl,
//    		data: categoryData, 	
//    		success: function(data){
//    			var htmlData = "";
//    			var index = 0;
//    			$(".myInterestGender").css("display","none");
//    			$(".myInterestCategory").css("display","none");
//    			$("fieldset.brands").addClass("brands active");
//    			$.each(data , function( key, value ) {
//    				index++;
//    				htmlData = htmlData+'<input type="checkbox" class="allBrands" name="brand" '+'id="question-2-'
//    				+index+'" value='+key+'>'
//    				+'<label for="question-2-'
//    				+index+'">'
//    				+'<img src="'+ACC.config.commonResourcePath+'/images/logob-bNuon.png">' +
//    				'<img class="hover-image" src="'+ACC.config.commonResourcePath+'/images/logou-uNuon.png"></label>';
//    			});
//    			$("#brandContainer").html(htmlData);
//    		},
//    		error: function(){
//    			alert("Something is not right! Please try after sometime");
//    		}
//    	});
//    }
    
        
//    Update Profile *********************************************
    function validateForm() {
		$("#errdoaDay,#errdobDay").empty();
		var regexCharSpace = /^[a-zA-Z ]*$/;
		var mob = /^[1-9]{1}[0-9]{9}$/;
		var regexSpace = /\s/;
		var regexDate = /^(?=\d{2}([-.,\/])\d{2}\1\d{4}$)(?:0[1-9]|1\d|[2][0-8]|29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\d{4}$/;
		var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		var proceed = true;
		// First Name and Last Name Validation
		if ((document.getElementById("profilefirstName").value) != "") {
			if (!regexCharSpace
					.test(document.getElementById("profilefirstName").value)) {
				$("#errfn").css({
					"display" : "block",
					"margin-top" : "10px"
				});
				document.getElementById("errfn").innerHTML = "<font color='red' size='2'>First name should contain alphabates and space only</font>";
				proceed = false;
			}
		}
		if ((document.getElementById("profilelastName").value) != "") {
			if (!regexCharSpace
					.test(document.getElementById("profilelastName").value)) {
				$("#errln").css({
					"display" : "block",
					"margin-top" : "10px"
				});
				document.getElementById("errln").innerHTML = "<font color='red' size='2'>Last name should contain alphabates and space only</font>";
				proceed = false;
			}
		}
		if ((document.getElementById("profileEmailID").value) != "") {
			if (!emailRegex
					.test(document.getElementById("profileEmailID").value)) {
				$("#errEmail").css({
					"display" : "block",
					"margin-top" : "10px"
				});
				document.getElementById("errEmail").innerHTML = "<font color='red' size='2'>Please enter a valid Email ID</font>";
				proceed = false;
			}
		}
		

		if ((document.getElementById("profileMobileNumber").value) != "") {
			if (document.getElementById("profileMobileNumber").value.length > 10
					|| document.getElementById("profileMobileNumber").value.length < 10
					|| isNaN(document.getElementById("profileMobileNumber").value)
					|| regexSpace.test(document.getElementById("profileMobileNumber").value)) {
				$("#errMob").css({
					"display" : "block",
					"padding-top" : "40px"
				});
				document.getElementById("errMob").innerHTML = "<font color='red' size='2'>Mobile number should contain 10 digit numbers only</font>";
				proceed = false;
			}
		}
		
			/////////////Date Of Birth and Date of Anniversary Validation Check//////////////////

			var selectValueDOB = document.getElementById("dateOfBirth").selectedIndex;
			var selectValueMOB = document.getElementById("monthOfBirth").selectedIndex;
			var selectValueYOB = document.getElementById("yearOfBirth").selectedIndex;

			var ValueDOB = document.getElementById("dateOfBirth").value;
			var ValueMOB = document.getElementById("monthOfBirth").value;
			var ValueYOB = document.getElementById("yearOfBirth").value;

			var isDOB = false;

			var selectValueDOA = document.getElementById("dateOfAnniversary").selectedIndex;
			var selectValueMOA = document.getElementById("monthOfAnniversary").selectedIndex;
			var selectValueYOA = document.getElementById("yearOfAnniversary").selectedIndex;
			
			var ValueDOA = document.getElementById("dateOfAnniversary").value;
			var ValueMOA = document.getElementById("monthOfAnniversary").value;
			var ValueYOA = document.getElementById("yearOfAnniversary").value;

			var tempFinalDOB = ValueDOB + "/" + ValueMOB + "/" + ValueYOB;
			var tempFinalDOA = ValueDOA + "/" + ValueMOA + "/" + ValueYOA;

			var tempFinalDateDOB = new Date(ValueMOB + "/" + ValueDOB + "/" + ValueYOB);
			var tempFinalDateDOA = new Date(ValueMOA + "/" + ValueDOA + "/" + ValueYOA);
			
			var dateOfBirthIntVal = parseInt(ValueYOB+ValueMOB+ValueDOB);
			var dateOfAnnIntVal = parseInt(ValueYOA+ValueMOA+ValueDOA);
			
		    var today = new Date();
		    var dd = today.getDate();
		    var mm = today.getMonth()+1; //January is 0!

		    var yyyy = today.getFullYear();
		    if(dd<10){
		        dd='0'+dd
		    } 
		    if(mm<10){
		        mm='0'+mm
		    } 
		    var todaydateFormat = new Date(mm+'/'+dd+'/'+yyyy);	
		    var sysDateIntVal = parseInt(yyyy+""+mm+""+dd);
			var isDOA = false;
			// Date of Birth
			if (selectValueDOB > 0 || selectValueMOB > 0 || selectValueYOB > 0) {

				if ((isNaN(ValueDOB) || isNaN(ValueMOB) || isNaN(ValueYOB))) {
					$("#errdobDay").css({
						"display" : "block",
						"margin-top" : "10px"
					});
					document.getElementById("errdobDay").innerHTML = "<font color='red' size='2'>Please Enter Valid Date </font>";
					proceed = false;
				} 
//				else if (tempFinalDateDOB > todaydateFormat){
				else if (dateOfBirthIntVal > sysDateIntVal){
					$("#errdobDay").css({
						"display" : "block",
						"margin-top" : "10px"
					});
					document.getElementById("errdobDay").innerHTML = "<font color='red' size='2'>Date of Birth cannot be Future Date</font>";
					proceed = false;
					
				} else {
					if (!regexDate.test(tempFinalDOB)) {
						$("#errdobDay").css({
							"display" : "block",
							"margin-top" : "10px"
						});
						document.getElementById("errdobDay").innerHTML = "<font color='red' size='2'>Please Enter Valid Date </font>";
						proceed = false;
					}

					else if (regexDate.test(tempFinalDOB)) {
						document.getElementById("errdobDay").innerHTML = "<font display='none' size='2'></font>";
					}
				}
			}

			// Anniversary

			if (selectValueDOA > 0 || selectValueMOA > 0 || selectValueYOA > 0) {
				if ((isNaN(ValueDOA) || isNaN(ValueMOA) || isNaN(ValueYOA))) {
					$("#errdoaDay").css({
						"display" : "block",
						"margin-top" : "10px"
					});
					document.getElementById("errdoaDay").innerHTML = "<font color='red' size='2'>Please Enter Valid Date </font>";
					proceed = false;
				} 
				else if (tempFinalDateDOA > todaydateFormat){
					document.getElementById("errdoaDay").innerHTML = "<font color='red' size='2'>Date of Anniversary cannot be Future Date</font>";
					proceed = false;
					//return proceed;
				}
				else if (dateOfAnnIntVal > sysDateIntVal){
					
					$("#errdoaDay").css({
						"display" : "block",
						"margin-top" : "10px"
					});
					document.getElementById("errdoaDay").innerHTML = "<font color='red' size='2'>Date of Anniversary cannot be Future Date</font>";
					proceed = false;
				}
				else 
				{
					if (!regexDate.test(tempFinalDOA)) 
					{
						$("#errdoaDay").css({
							"display" : "block",
							"margin-top" : "10px"
						});
						document.getElementById("errdoaDay").innerHTML = "<font color='red' size='2'>Please Enter Valid Date </font>";
						proceed = false;
						
					}
					else if (regexDate.test(tempFinalDOA)) 
					{
						if ((tempFinalDOB == tempFinalDOA)) 
							{
							$("#errdata").css({
								"display" : "block",
								"margin-top" : "10px"
							});
							document.getElementById("errdata").innerHTML = "<font color='red' size='2'>Date of birth and Anniversary date cannot be same.</font>";
							proceed = false;
							
							} 
						else if ((tempFinalDateDOB > tempFinalDateDOA)) 
						{
							$("#errdata").css({
								"display" : "block",
								"margin-top" : "10px"
							});
							document.getElementById("errdata").innerHTML = "<font color='red' size='2'>Date of Birth cannot be after Anniversary Date.</font>";
							proceed = false;
						} 
						else 
						{
							$("#errdoaDay").css({
								"display" : "block",
								"margin-top" : "10px"
							});
							document.getElementById("errdoaDay").innerHTML = "<font display='none' size='2'></font>";
							proceed = true;
						}
					}
				}
			}
		
		return proceed;
//		if ($(".errorMessage").hasClass('active')) {
//			
//		}
	}
    
	function kpressfn() {
		document.getElementById("errfn").innerHTML = "";
	}
	function kpressln() {
		document.getElementById("errln").innerHTML = "";
	}
	function kpressmob() {
		document.getElementById("errMob").innerHTML = "";
	}
	function kpressemail() {
		document.getElementById("errEmail").innerHTML = "";
	}
	function selectBoxChange() {
		document.getElementById("errdobDay").innerHTML = "";
		document.getElementById("errdoaDay").innerHTML = "";
		document.getElementById("errdata").innerHTML = "";
	}
	
//  End Update Profile *********************************************
    
    
//  Update NickName *********************************************
	function validateNickName() {
		var regexCharSpace = /^[a-zA-Z ]*$/;
		if ((document.getElementById("profilenickName").value) != "") {
			if (!regexCharSpace
					.test(document.getElementById("profilenickName").value)) {
				$("#errfn").css({
					"display" : "block",
					"margin-top" : "10px"
				});
				document.getElementById("errnn").innerHTML = "<font color='red' size='2'>Nick name should contain alphabates and space only</font>";
				return false;
			}
		}
	}

	function kpressnn() {
		document.getElementById("errnn").innerHTML = "";
	}
	
//  End Update NickName *********************************************
	
	
	
	
//  Update Password *********************************************
	function validatePassword() {
		var flag=true;
		var regexPasswordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#^$%*&!^~]).{8,16}$/;
		if (document.getElementById("currentPassword").value == null
				|| document.getElementById("currentPassword").value == "") {
			$("#errCurpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errCurpwd").innerHTML = "<font color='red' size='2'>Please enter Current Password</font>";
			flag = false;
		}
		if (document.getElementById("newPassword").value == null
				|| document.getElementById("newPassword").value == "") {
			$("#errNewpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errNewpwd").innerHTML = "<font color='red' size='2'><b>Please enter New Password</b></font>";
			flag = false;
		}
		if (document.getElementById("checkNewPassword").value == null
				|| document.getElementById("checkNewPassword").value == "") {
			$("#errCnfNewpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errCnfNewpwd").innerHTML = "<font color='red' size='2'>Please Confirm New Password</font>";
			flag = false;
		}

		else if (document.getElementById("newPassword").value.length < 8
				|| document.getElementById("newPassword").value.length > 16) {
			$("#errNewpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errNewpwd").innerHTML = "<font color='red' size='2'>Password should contain more than 8 and less than 16 characters</font>";
			flag = false;
		}

		else if (!regexPasswordPattern.test(document
				.getElementById("newPassword").value)) {
			$("#errNewpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errNewpwd").innerHTML = "<font color='red' size='2'>Please enter a valid password according to Password Policy (Password should meet the basic criteria of minimum 8 characters long up to 16 characters. Password should consist of atleast one alphabet, one numeric, one special symbols and Password doesn't consist of spaces and password should consists of atleast one UPPER and one LOWER case alphabet )</font>";
			flag = false;
		}
		else if (document.getElementById("newPassword").value != document
				.getElementById("checkNewPassword").value) {
			$("#errCnfNewpwd").css({
				"display" : "block",
				"margin-top" : "10px"
			});
			document.getElementById("errCnfNewpwd").innerHTML = "<font color='red' size='2'>Passwords do not match</font>";
			flag = false;
		}
		else{
			/*var currentPassword = $("#currentPassword").val();
			var newPassword= $("#newPassword").val();
			var checkNewPassword = $("#checkNewPassword").val();
			var dataString="currentPassword="+encodeURIComponent(currentPassword);*/
			
			//Fix for defect TISEE-3986 : handling special character like #
			var currentPassword = $("#currentPassword").val();			
			var newPassword= $("#newPassword").val();			
			newPassword= encodeURIComponent(newPassword);
			$("#newPassword").val(newPassword);
			
			var checkNewPassword = $("#checkNewPassword").val();			
			checkNewPassword= encodeURIComponent(checkNewPassword);
			$("#checkNewPassword").val(checkNewPassword);		
			
			var dataString=encodeURIComponent(currentPassword);	
			$("#currentPassword").val(dataString);
			
			var requiredUrl = ACC.config.encodedContextPath + "/my-account/checkCurrentPassword";  
			
			jQuery.ajax({
				type: 'POST',
				//contentType: "JSON",
				url: requiredUrl,
				//data: $("#frmUpdatePassword").serialize(),
				data: {currentPassword:dataString},
				success: function(data){
					
					if(data=="invalidPassword"){
						document.getElementById("errCurpwd").innerHTML = "<font color='red' size='2'>Oops! This password is incorrect.</font>";
						flag = false;
					}
					else if(data=="validPassword"){
						if(currentPassword==newPassword)
						{
							document.getElementById("errCurpwd").innerHTML = "<font color='red' size='2'>Current and New Password cannot be same</font>";
							flag = false;
						}
						else
						{
							document.getElementById("errCurpwd").innerHTML = "";
							document.getElementById("errNewpwd").innerHTML = "";
							document.getElementById("errCnfNewpwd").innerHTML = "";
							chk=0;
							$("#frmUpdatePassword").submit();
						}
					}
				},
				error: function(){
					globalErrorPopup('Something went wrong. Please try after sometime');
				}
			});
		}
		return flag;
	}
	
	function kpresscp() {
		document.getElementById("errCurpwd").innerHTML = "";
	}
	function kpressnp() {
		document.getElementById("errNewpwd").innerHTML = "";
	}
	function kpresscnp() {
		document.getElementById("errCnfNewpwd").innerHTML = "";
	}
	
//  End Update Password *********************************************
    
//  Order history page *********************************************
    function reloadOrderPage(){
    	window.location.href=ACC.config.encodedContextPath+"/my-account/orders";
    } 
//  Order detail page *********************************************
    function reloadOrderDetailPage(){
    	var ordercode=$('#newCode').val();
    	window.location.href=ACC.config.encodedContextPath+"/my-account/order/?orderCode="+ordercode;
    }
    
    
 // Validation of Account address page
    function validateAccountAddress() {
    	   
        var selectedValueState = document.getElementById('stateListBox').selectedIndex;
        var regexCharSpace = /^[a-zA-Z ]*$/;
//        var regexCharSpace = /^[a-zA-Z]+(\s[a-zA-Z]+)?$/;
        var regexSpace = /\s/;
        var equalNoCheck = /^\D*(\d)(?:\D*|\1)*$/;
        var flagFn = true; 
        var flagLn = true;
        var flagAd1 = true;
        var flagAd2 = true;
        var flagAd3 = true;
        var flagPost = true;
        var flagCity = true;
        var flagState = true;
        var flagMob = true;
        
        if ((addressForm.addressRadioType[0].checked == false) && (addressForm.addressRadioType[1].checked == false )){
        	document.getElementById("errtype").innerHTML = "<font color='red' size='2'>Please select an address type</font>";
        	flagFn = false;
        }
        if (addressForm.firstName.value == null || addressForm.firstName.value == "") { 
        	$("#erraddressfn").css({"display":"block"});
            document.getElementById("erraddressfn").innerHTML = "<font color='red' size='2'>Please enter first name</font>";
            flagFn = false;
        }
        else if (!regexCharSpace.test(document.getElementById("firstName").value)) { 
        	$("#errddressfn").css({"display":"block"});
            document.getElementById("erraddressfn").innerHTML = "<font color='red' size='2'>First name should contain alphabates and space only</font>";
            flagFn = false;
        }
        if (addressForm.lastName.value == null || addressForm.lastName.value == "") {
        	$("#errddressln").css({"display":"block"});
        	document.getElementById("erraddressln").innerHTML = "<font color='red' size='2'>Please enter last name</font>";
        	flagLn = false;
        }
        else if (!regexCharSpace.test(document.getElementById("lastName").value)) { 
        	$("#errddressln").css({"display":"block"});
            document.getElementById("erraddressln").innerHTML = "<font color='red' size='2'>Last name should contain alphabates and space only</font>";
            flagLn = false;
        }
        if (addressForm.line1.value == null || addressForm.line1.value == "") {
        	$("#errddressline1").css({"display":"block"});
        	document.getElementById("erraddressline1").innerHTML = "<font color='red' size='2'>Please enter address line 1</font>";
        	flagAd1 = false;
        }
        if (addressForm.line2.value == null || addressForm.line2.value == "") {
        	$("#errddressline2").css({"display":"block"});
        	document.getElementById("erraddressline2").innerHTML = "<font color='red' size='2'>Please enter address line 2</font>";
        	flagAd2 = false;
        }
        if (addressForm.line3.value == null || addressForm.line3.value == "") {
        	$("#errddressline3").css({"display":"block"});
        	document.getElementById("erraddressline3").innerHTML = "<font color='red' size='2'>Please enter landmark</font>";
        	flagAd3 = false;
        }
        if (addressForm.postcode.value == null || addressForm.postcode.value == "") {
        	$("#errddressPost").css({"display":"block"});
        	document.getElementById("erraddressPost").innerHTML = "<font color='red' size='2'>Please enter post code</font>";
        	flagPost = false;
        }
        else if (addressForm.postcode.value.length > 6 || addressForm.postcode.value.length < 6 || isNaN(addressForm.postcode.value) || regexSpace.test(addressForm.postcode.value)) {
        	$("#errddressPost").css({"display":"block"});
        	document.getElementById("erraddressPost").innerHTML = "<font color='red' size='2'>Post code should contain 6 digit numeric characters only</font>";
        	flagPost = false;
        }
        if (addressForm.townCity.value == null || addressForm.townCity.value == "") {
        	$("#errddressCity").css({"display":"block"});
        	document.getElementById("erraddressCity").innerHTML = "<font color='red' size='2'>Please enter city</font>";
        	flagCity = false;
        }
        else if (!regexCharSpace.test(document.getElementById("townCity").value)) { 
        	$("#errddressCity").css({"display":"block"});
        	document.getElementById("erraddressCity").innerHTML = "<font color='red' size='2'>City should contain alphabets only</font>";
        	flagCity = false;
        }
        if (selectedValueState == 0) {
        	$("#errddressState").css({"display":"block"});
        	document.getElementById("erraddressState").innerHTML = "<font color='red' size='2'>Please select state</font>";
        	flagState = false;
        }
        if (addressForm.mobileNo.value == null || addressForm.mobileNo.value == "") {
        	$("#errddressMob").css({"display":"block"});
        	document.getElementById("erraddressMob").innerHTML = "<font color='red' size='2'>Please enter mobile number</font>";
        	flagMob = false;
        }
        else if (addressForm.mobileNo.value.length > 10 || addressForm.mobileNo.value.length < 10 || isNaN(addressForm.mobileNo.value)) {
        	$("#errddressMob").css({"display":"block"});
        	document.getElementById("erraddressMob").innerHTML = "<font color='red' size='2'>Mobile number should contain 10 digit numbers only</font>";
        	flagMob = false;
        }
        else if (regexSpace.test(addressForm.mobileNo.value)) {
        	$("#errddressMob").css({"display":"block"});
        	document.getElementById("erraddressMob").innerHTML = "<font color='red' size='2'>Mobile number should contain 10 digit numbers only</font>";
        	flagMob = false;
        }
        else if(equalNoCheck.test(addressForm.mobileNo.value)){
        	$("#errddressMob").css({"display":"block"});
        	document.getElementById("erraddressMob").innerHTML = "<font color='red' size='2'>Mobile number should contain 10 digit numbers only</font>";
        	flagMob = false;
        }
        
        if(flagFn && flagLn && flagAd1 && flagAd2 && flagAd3 && flagPost && flagCity && flagState && flagMob)
        {
        	 return true;
        }
        else{
        	return false;
        }
       
    }
    function onSelectRadio(){
    	document.getElementById("errtype").innerHTML = "";
    }
    function kpressaddressfn(){
    	document.getElementById("erraddressfn").innerHTML = "";
    }
    function kpressaddressln(){
    	document.getElementById("erraddressln").innerHTML = "";
    }
    function kpressaddressln1(){
    	document.getElementById("erraddressline1").innerHTML = "";
    }
    function kpressaddressln2(){
    	document.getElementById("erraddressline2").innerHTML = "";
    }
    function kpressaddressln3(){
    	document.getElementById("erraddressline3").innerHTML = "";
    }
    function kpressaddresspost(){
    	document.getElementById("erraddressPost").innerHTML = "";
    }
    function kpressaddresscity(){
    	document.getElementById("erraddressCity").innerHTML = "";
    }
    function kpressaddressmob(){
    	document.getElementById("erraddressMob").innerHTML = "";
    }
    function onAddressSelectValidate(){
    	document.getElementById("erraddressState").innerHTML = "";
    }

 // End Validation of Account address page

    
 // Pagination ***********************************
   
    
    /*function nextAcc()
    {
    	if(pageCount<noofpageCount-1)
    	{
    		pageCount++;
    		dispPageLimit(pageCount*pagelimitAcc,(pageCount+1)*pagelimitAcc);
    	}
    }

    function prevAcc()
    {
    	if(pageCount>0)
    	{
    		pageCount=pageCount-1;
    		dispPageLimit(pageCount*pagelimitAcc,(pageCount+1)*pagelimitAcc);
    	}
    }

    function dispPageLimit(start,end)
    {
    	for(var i=0;i<totalItem;i++)
    	{
    		if(i>=start && i<end){
    			$('#p_'+i).show();
    		}
    		else
    		{
    			$('#p_'+i).hide();
    		}
    	}
    }*/
  //End | Pagination  
    

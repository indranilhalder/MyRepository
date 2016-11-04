<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/responsive/returns"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/returns/initiateReturn" var="returnUrl" />

<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAYOJ5Pa-kx8HoOt_GzZpzTfpTD_q9PlQo"></script>
<template:page pageTitle="${pageTitle}">

	<div class="account">
		<div class="wrapper">
			<a class="return-order-history" href="order-history.php"><spring:theme
					code="text.returRequest.backtoHistory" text="Back to Order History" /></a>
				
			});
			var firstname = $(".firstName").val();
			var lastname=$(".latName").val();
			var mobileno=$(".mobileNo").val();
			var isString=isNaN(mobileno);
			var Adreess1=$(".Address1").val();
			var Adreess2=$(".Address2").val();
			var pincodeNo=$(".pincodeno").val();
			var landmark=$(".landmark").val();
			var city=$(".city").val();
			var state=$(".state").val();
			var country=$(".country").val();
			
			
			//console.log(isString);
			
			$("#savebtn").click(function(e) {
				hideErrorMessage();
			<div class="returnItemForm">
				<div class="return-container">
					<form:form
						action="${returnUrl}"
						method="post" name="returnForm" commandName="returnForm">
						<return:selectReason />
						<return:selectReturntype />
						<return:selectReturnMethod />
					</form:form>
				
				 
				if ($("#firstName").val().length < 1 || $("#lastName").val().length <1 || isNaN($("#mobileNo").val()) == true || $("#mobileNo").val().length < 10 || $("#addressLane1").val().length < 1 || isNaN($("#pincode").val()) == true || $("#pincode").val().length < 5 || $("#city").val().length < 1 || $("#state").val() == "00"){	
					e.preventDefault();
					if ($("#firstName").val().length <1 ) {
			            $(".firstNameError").show();
			            $(".firstNameError").text("First Name cannot be Blank");
			        }

			        if ($("#lastName").val().length < 1) {
			            $(".lastNameError").show();
			            $(".lastNameError").text("Last Name cannot be Blank ");
			        }



			        if (isNaN($("#mobileNo").val()) == true) {
			            $(".mobileNumberError").show();
			            $(".mobileNumberError").text("Enter only Numbers");
			        } else if (isNaN($("#mobileNo").val()) == false) {
			            if ($("#mobileNo").val().length < 10) {
			                $(".mobileNumberError").show();
			                $(".mobileNumberError").text("Please enter mobile no.");
			            }
			        }


			        if ($("#addressLane1").val().length < 1) {
			            $(".address1Error").show();
			            $(".address1Error").text("Address Line 1 cannot be blank");
			        }

			       /*  if ($("#addressLane2").val().length < 1) {
			            $(".address2Error").show();
			            $(".address2Error").text("Address Line 2 cannot be blank");
			        } */



			        if (isNaN($("#pincode").val()) == true) {
			            $(".pincodeNoError").show();
			            $(".pincodeNoError").text("Enter only Numbers");
			        } else if (isNaN($("#pincode").val()) == false) {
			            if ($("#pincode").val().length < 5) {
			                $(".pincodeNoError").show();
			                $(".pincodeNoError").text("Please enter a pincode");
			            }
			        }


			       /*  if ($("#landmark").val().length < 1) {
			            $(".landMarkError").show();
			            $(".landMarkError").text("Landmark cannot be blank");
			        } */

			        if ($("#city").val().length < 1) {
			            $(".cityError").show();
			            $(".cityError").text("City cannot be blank");
			        }


			        if ( $("#state").val() == "00") {
			            $(".stateError").show();
			            $(".stateError").text("Please choose a state");
			        }
			    } else {
			    	
			    	
			    	var addressLine1 = $("#addressLane1").val();
			    	var addressLine2 = $("#addressLane2").val();
			    	var addressLine3 = $("#landmark").val();
			    	if(addressLine1.indexOf('#')!=-1)
			    	{
			    		
			    	$("#addressLane1").val(encodeURIComponent($("#addressLane1").val()));
			    	}
			    	
			    	if(addressLine2.indexOf('#')!=-1)
		    		{
			    		
		    		$("#addressLane2").val(encodeURIComponent($("#addressLane2").val()));
		    		
		    		}
			    	if(addressLine3.indexOf('#')!=-1)
		    		{
			    	
		    		$("#landmark").val(encodeURIComponent($("#landmark").val()));
		    		
		    		}
			    	
			    	/* $("#addressLane1").val(encodeURIComponent($("#addressLane1").val()));
			    	$("#addressLane2").val(encodeURIComponent($("#addressLane2").val()));
			    	$("#addressLane2").val(encodeURIComponent($("#landmark").val())); */
				
					$("#returnPincodeCheckForm").submit();
						
			   		 }
			});
			
			function hideErrorMessage() {
				$(".firstNameError").hide();
				$(".lastNameError").hide();
				$(".mobileNumberError").hide();
				$(".address1Error").hide();
				$(".address2Error").hide();
				$(".pincodeNoError").hide();
				$(".landMarkError").hide();
				$(".cityError").hide();
				$(".stateError").hide();
				$(".countryError").hide();
			}

			

		});
	</script>

		<div
			class="/checkout-content cart checkout wrapper">
			<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
			<div class="top checkout-top">
				<div class="content returnPickupHead">
					<div class="pickupHeading">
				      Add PickUp Person Address
				     </div>
		 <return:pickupAddressPopup />

			<br> <br>
			<div class="col-md-2"></div>
			<div class="col-md-9">
				<div class="container">
					<div class="panel panel-default addAddress" style="padding: 10px;">
						<!-- <h4 class="col-md-12">Add New Address</h4> -->
						<form:form method="POST"
							action="${request.contextPath}/my-account/order/returnPincodeSubmit"
							commandName="returnPincodeCheckForm">
							
							<div class="row">
								<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">First Name*</label>
										<form:input path="firstName" maxlength="40" placeholder="Enter first Name" class="firstName" />
										<div class="error_text firstNameError"></div>
									</div>
									<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">Last Name*</label><br>
										<form:input path="lastName" maxlength="40" placeholder="Enter Last Name" class="lastName"  />
										<div class="error_text lastNameError"></div>
									</div>
									<div class="col-md-4 inputBoxHeight">
	
										<label class="returnLabel">Mobile No*</label><br>
										<form:input path="mobileNo" maxlength="10" placeholder="Enter Mobile No" class="mobileNo" />
										<div class="error_text mobileNumberError"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-12 inputBoxHeight">
										<label class="returnLabel">Address Line1*</label><br>
										<form:input path="addressLane1" class="Address1" maxlength="40" placeholder="Enter Address Line1" />
										<div class="error_text address1Error"></div>
									</div>
								</div>
								
								
								<div class="row">
									<div class="col-md-12 inputBoxHeight">
										<label class="returnLabel">Address Line2</label><br>
										<form:input path="addressLane2" maxlength="40" class="Address2"
											placeholder="Enter Address Line2"
											 />
										<div class="error_text address2Error"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">Pincode*</label><br>
										<form:input maxlength="6" path="pincode" placeholder="Enter PinCode" />
										<div class="error_text pincodeNoError"></div>
									</div>
									
									<div class="col-md-8 inputBoxHeight">
										<label class="returnLabel">LandMark</label><br>
										<form:input path="landmark" maxlength="40" placeholder="Enter Your Nearest Land mark" />
										<div class="error_text landMarkError"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">Country*</label><br>
										<form:input path="country" placeholder="Enter Country" class="country"
											/>
										<div class="error_text countryError"></div>
									</div>
									
									<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">City*</label><br>
										<form:input path="city" maxlength="40" placeholder="Enter city" class="city"
											 />
										<div class="error_text cityError"></div>
									</div>
									
									<div class="col-md-4 inputBoxHeight">
										<label class="returnLabel">State*</label><br><%-- ${returnPincodeCheckForm.state} --%>
										<form:select path="state"  items="${stateDataList}" itemValue="code" itemLabel="name"></form:select>
<%-- 										<form:input path="state" placeholder="Enter State" class="state" --%>
<%-- 											 /> --%>
										<div class="error_text stateError"></div>
									</div>
								</div>
								<form:hidden path="orderCode" value="${orderCode}" />
								<form:hidden path="ussid" value="${ussid}" />
								<form:hidden path="transactionId" value="${transactionId}" />
						
							<br>
							<div class="row">
									<div class="col-md-4"></div>
									
									<c:choose>
										<c:when test="${returnLogisticsCheck eq false}">
											<input type="submit" id="savebtnDisabled" value="Continue" class="btn btn-info"	style="border: none; width: 100px;" disabled />
										</c:when>
										<c:otherwise>
											<input type="submit" id="savebtn" value="Continue" class="btn btn-info"	style="border: none; width: 100px;" />
										</c:otherwise>
									</c:choose>	
									
									<div class="col-md-1"></div>
									<c:choose>
										<c:when test="${returnLogisticsCheck eq false}">
											<input type="button" id="proceedBtnNew" value="Self Ship" class="btn btn-info" data-url="${request.contextPath}/my-account/order/returnReplace?orderCode=${returnPincodeCheckForm.orderCode}&ussid=${returnPincodeCheckForm.ussid}&transactionId=${returnPincodeCheckForm.transactionId}" style="border: none; width: 100px;" />
										</c:when>
										<c:otherwise>
											<input type="button" id="proceedBtn" value="Self Ship" class="btn btn-info"	style="border: none; width: 100px;" disabled />
										</c:otherwise>
									</c:choose>									
								</div>
								
						</div>

					</div>





				</div>
			</div>

		<script>
			$(document).ready(function() {
				function selectState(stateName){
					$("#state option").each(function(){
						if($(this).text() == stateName) {
							var value = $(this).val();
							$("#state").val(value);
							//console.log($(this).text());
						}
					});
				}
				document.onload = selectState("${returnPincodeCheckForm.state}");
		//		console.log("${stateDataList}");
				$("#state").css("height", "40px");
				$("#state").css("width", "100%");
			});
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
				function loadFormData() {
					
					$(".firstName").val("${returnPincodeCheckForm.firstName }");
					$(".lastName").val("${returnPincodeCheckForm.lastName }");
					$(".mobileNo").val("${returnPincodeCheckForm.mobileNo }");
					$(".addressLane1").val("${returnPincodeCheckForm.addressLane1 }");
					$(".addressLane2").val("${returnPincodeCheckForm.addressLane2 }");
					$(".pincode").val("${returnPincodeCheckForm.pincode }");
					$(".landmark").val("${returnPincodeCheckForm.landmark }");
					$(".city").val("${returnPincodeCheckForm.city }");
					$(".state").val("${returnPincodeCheckForm.state }");
					$(".country").val("${returnPincodeCheckForm.country }");
					$(".country").css('pointer-events', 'none');
					$(".country").attr('tabindex', '-1');
					$(".country").val("India");
				}
				document.onload = loadFormData();
				/* $.delay(1000, function(){
					loadFormData();
				}); */
				
				setTimeout(function(){ loadFormData(); }, 100);
				
				
				
			});
		</script>
		
</template:page>

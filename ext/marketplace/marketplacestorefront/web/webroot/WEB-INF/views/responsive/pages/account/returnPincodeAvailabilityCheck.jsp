<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>


<template:page pageTitle="${pageTitle}">


<style>
	.addAddress input[type="text"] {
		width: 100% !important;
	}
	
	.deliver_message {
		color: red;
	}
	
	.error_text {
		color: red;
	}
	.top.checkout-top .content ul li {
		font-weight: 600 !important;
		font-family: 'Avenir Next' !important;
	}
	.panel-default {
		border-color: #DDD;
		margin-bottom: 20px;
		background-color: #FFF;
		border: 1px solid #CBC9C9 !important;
		border-radius: 4px;
		box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
	}
	label {
		display: inline-block;
		max-width: 100%;
		margin-bottom: 5px;
		font-weight: 700;
		margin-top: 10px;
	}
	.btn-info {
	    background-color: #5BC0DE !important;
	    border-color: #46B8DA !important;
	    margin-top: 10px;
		margin-bottom: 5px;
		color: #fff !important;
		font-weight: 500;
    }
    .error_text{
    	color: red;
    }
    .inputBoxHeight {
	  height: 80px !important;
	}
</style>

<script type="text/javascript">
		$(document).ready(function() {
			$("#proceedBtnNew").click(function(){
				//console.log("Proceed Button Clicked!!!")
				var url = $("#proceedBtnNew").attr("data-url");
				window.location = url;
				
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
				 //alert("coming"); 
				 
			if($("#firstName").val().length <= 3 || $("#lastName").val().length <= 2 || isNaN($("#mobileNo").val()) == true || $("#mobileNo").val().length < 10 || $("#addressLane1").val().length <= 4 || $("#addressLane2").val().length <= 4 || isNaN($("#pincode").val()) == true || $("#pincode").val().length < 5 ||$("#landmark").val().length <= 5 || $("#city").val().length <= 1 || $("#state").val().length <= 1 || $("#country").val().length <= 1) {	
				e.preventDefault();
					if ($("#firstName").val().length <= 3) {
						$(".firstNameError").show();
						$(".firstNameError").text("Enter Atleat 4 letters");
					} 
					
					if ($("#lastName").val().length <= 2) {
						$(".lastNameError").show();
						$(".lastNameError").text("Enter Atleast 3 Letters ");
					} 
					
					
						
					if (isNaN($("#mobileNo").val()) == true) {
						$(".mobileNumberError").show();
						$(".mobileNumberError").text("Enter only Numbers");
					} else if (isNaN($("#mobileNo").val()) == false){
						if ($("#mobileNo").val().length < 10) {
							$(".mobileNumberError").show();
							$(".mobileNumberError").text("Enter 10 digit mobile number");
						}
					} 

					if ($("#addressLane1").val().length <= 4) {
							$(".address1Error").show();
							$(".address1Error").text(" Enter Above 5 Characters");
						} 				
						
					if ($("#addressLane2").val().length <= 4) {
							$(".address2Error").show();
							$(".address2Error").text("Enter Above 5 Characters");
						} 
	
					if (isNaN($("#pincode").val()) == true) {
							$(".pincodeNoError").show();
							$(".pincodeNoError").text("Enter only Numbers");
						} else if (isNaN($("#pincode").val()) == false) {
							if ($("#pincode").val().length < 5) {
								$(".pincodeNoError").show();
								$(".pincodeNoError").text("Enter 6 digit pincode");
							}
						}
			
						 
					if ($("#landmark").val().length <= 4) {
							$(".landMarkError").show();
							$(".landMarkError").text("Enter Above 5 Characters");
						}
		
					if ($("#city").val().length <= 1) {
							$(".cityError").show();
							$(".cityError").text("Enter Above 1 Characters");
						} 
						
		
					if ($("#state").val().length <= 1) {
							$(".stateError").show();
							$(".stateError").text("Enter Above 1 Characters");
						} 
						
					if ($("#country").val().length <= 1) {
							$(".countryError").show();
							$(".countryError").text("Enter Above 1 Characters");
						}
				} 
				else {
						
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

	<div class="body-Content">
		<div
			class="/checkout-content cart checkout wrapper">
			<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
			<div class="top checkout-top">
				<div class="content" style="height: 100px;">
					<ul class="nav">
						<li  style="margin-top: -128px;">Add PickUp
							Person Address</li>
					</ul>
				</div>
			</div>

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
										<label>First Name*</label>
										<form:input path="firstName" placeholder="Enter first Name" class="firstName" />
										<div class="error_text firstNameError"></div>
									</div>
									<div class="col-md-4 inputBoxHeight">
										<label>Last Name*</label><br>
										<form:input path="lastName" placeholder="Enter Last Name" class="lastName" />
										<div class="error_text lastNameError"></div>
									</div>
									<div class="col-md-4 inputBoxHeight">
	
										<label>Mobile No*</label><br>
										<form:input path="mobileNo" maxlength="10" placeholder="Enter Mobile No" class="mobileNo" />
										<div class="error_text mobileNumberError"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-12 inputBoxHeight">
										<label>Address Line1*</label><br>
										<form:input path="addressLane1" class="Address1" placeholder="Enter Address Line" />
										<div class="error_text address1Error"></div>
									</div>
								</div>
								
								
								<div class="row">
									<div class="col-md-12 inputBoxHeight">
										<label>Address Line2*</label><br>
										<form:input path="addressLane2" class="Address2"
											placeholder="Enter Address Line"
											 />
										<div class="error_text address2Error"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-4 inputBoxHeight">
										<label>Pincode*</label><br>
										<form:input maxlength="6" path="pincode" placeholder="Enter PinCode" />
										<div class="error_text pincodeNoError"></div>
									</div>
									
									<div class="col-md-8 inputBoxHeight">
										<label>LandMark*</label><br>
										<form:input path="landmark" placeholder="Enter Your Nearest Land mark" />
										<div class="error_text landMarkError"></div>
									</div>
								</div>
								
								<div class="row">
									<div class="col-md-4 inputBoxHeight">
										<label>Country*</label><br>
										<form:input path="country" placeholder="Enter Country" class="country"
											/>
										<div class="error_text countryError"></div>
									</div>
									
									<div class="col-md-4 inputBoxHeight">
										<label>City*</label><br>
										<form:input path="city" placeholder="Enter city" class="city"
											 />
										<div class="error_text cityError"></div>
									</div>
									
									<div class="col-md-4 inputBoxHeight">
										<label>State*</label><br><%-- ${returnPincodeCheckForm.state} --%>
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
									
										<input type="submit" id="savebtn" value="Continue" class="btn btn-info"	style="border: none; width: 100px;" />
									
									<div class="col-md-1"></div>
									<c:choose>
										<c:when test="${returnLogisticsCheck eq false}">
											<input type="button" id="proceedBtnNew" value="Proceed" class="btn btn-info" data-url="${request.contextPath}/my-account/order/returnReplace?orderCode=${returnPincodeCheckForm.orderCode}&ussid=${returnPincodeCheckForm.ussid}&transactionId=${returnPincodeCheckForm.transactionId}" style="border: none; width: 100px;" />
										</c:when>
										<c:otherwise>
											<input type="button" id="proceedBtn" value="Proceed" class="btn btn-info"	style="border: none; width: 100px;" disabled />
										</c:otherwise>
									</c:choose>									
								</div>
								
							<div class="deliver_message"><!-- SORRY! We cannot pickup from
								the address provided, Please provide other address or You can
								Self - ship and let us know! -->
								<c:if test="${returnLogisticsCheck eq false}">
								${notServiceable}
								</c:if></div>
						</form:form>
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
				console.log("${stateDataList}");
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
					$("country").val("${returnPincodeCheckForm.country }");
				}
				document.onload = loadFormData();
				$.delay(1000, function(){
					loadFormData();
				});
				
			});
		</script>
		
</template:page>
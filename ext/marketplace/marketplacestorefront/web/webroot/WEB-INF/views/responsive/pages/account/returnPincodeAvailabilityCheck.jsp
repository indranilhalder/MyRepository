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
</style>
<script type="text/javascript">
					$(document).ready(function() {
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
						
						
						console.log(isString);
						
						$('#savebtn').click(function() {
							/* alert("coming"); */
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
							
							
							if(firstname.length<=3){
								//alert("soon");
								
								$(".firstNameError").show();
								$(".firstNameError").text("Enter Atleat 4 letters");
							}
							if(lastname.length<=2){
								//alert("Hi i am coming");
								$(".lastNameError").show();
								$(".lastNameError").text("Enter Atleast 3 Letters ");
							}
							if(mobileno.length<=9){
								if(isString==true){
									$(".mobileNumberError").show();
									$(".mobileNumberError").text("Enter only Numbers");
								}else{
									$(".mobileNumberError").show();
									$(".mobileNumberError").text("Enter 10 digit Numbers");
								}
							}
							if(Adreess1.length<=10){
								$(".address1Error").show();
								$(".address1Error").text("Enter Address1 Details");
							}
							if(Adreess2.length<=10){
								$(".address2Error").show();
								$(".address2Error").text("Enter Address2 Details");
							}
							if(pincodeNo.length<=5){
								
								$(".pincodeNoError").show();
								$(".pincodeNoError").text("Enter Picode Number");
							}

							  if(landmark.length<=5){
								$(".landMarkError").show();
								$(".landMarkError").text("Enter Nearest landMark");
							} 

							if(city.length<=4){
								$(".cityError").show();
								$(".cityError").text("Enter City Details");
							}

							if(state.length<=9){
								$(".stateError").show();
								$(".stateError").text("Enter State Details");
							}
							if(country.length<=5){
								$(".countryError").show();
								$(".countryError").text("Enter Country Details");
							} 
						});
						
						
						

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
								<div class="col-md-4">
									<label>First Name*</label>
									<form:input path="firstName" placeholder="Enter First Name" value="${returnPincodeCheckForm.firstName }"  />
									<div class="error_text firstNameError"></div>
								</div>
								<div class="col-md-4">
									<label>Last Name*</label><br>
									<form:input path="lastName" placeholder="Enter Last Name" value="${returnPincodeCheckForm.lastName }" />
									<div class="error_text lastNameError"></div>
								</div>
								<div class="col-md-4">

									<label>Mobile No*</label><br>
									<form:input path="mobileNo" placeholder="Enter Mobile No" value="${returnPincodeCheckForm.mobileNo }"  />
									<div class="error_text mobileNumberError"></div>
								</div>

								<div class="col-md-12">
									<label>Address Line1*</label><br>
									<form:input path="addressLane1"
										placeholder="Enter Address Line" value="${returnPincodeCheckForm.addressLane1 }"  />
									<div class="error_text address1Error"></div>

								</div>
								<br>
								<div class="col-md-12">
									<label>Address Line2*</label><br>
									<form:input path="addressLane2"
										placeholder="Enter Address Line" value="${returnPincodeCheckForm.addressLane2 }" />
									<div class="error_text address2Error"></div>
								</div>
								<div class="col-md-4">
									<label>Pincode*</label><br>
									<form:input path="pincode" placeholder="Enter PinCode" value="${returnPincodeCheckForm.pincode }" />
									<div class="error_text pincodeNoError"></div>
								</div>
								<div class="col-md-8">
									<label>LandMark*</label><br>
									<form:input path="landmark"
										placeholder="Enter Your Nearest Land mark" value="${returnPincodeCheckForm.landmark }"  />
									<div class="error_text landMarkError"></div>
								</div>

								<div class="col-md-4">
									<label>City*</label><br>
									<form:input path="city" placeholder="Enter city" value="${returnPincodeCheckForm.city }" />
									<div class="error_text cityError"></div>
								</div>
								<div class="col-md-4">
									<label>State*</label><br>
									<form:input path="state" placeholder="Enter State" value="${returnPincodeCheckForm.state }" />
									<div class="error_text stateError"></div>
								</div>
								<div class="col-md-4">
									<label>Country*</label><br>
									<form:input path="country" placeholder="Enter Country" value="${returnPincodeCheckForm.country }"/>
									<div class="error_text countryError"></div>
								</div>
								<form:hidden path="orderCode" value="${orderCode}" />
								<form:hidden path="ussid" value="${ussid}" />
								<form:hidden path="transactionId" value="${transactionId}" />
							</div>
							<br>
							<input type="submit" id="savebtn" value="Continue" class="btn btn-info" style="border: none;width: 100px;margin-left:358px; ">
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
</template:page>

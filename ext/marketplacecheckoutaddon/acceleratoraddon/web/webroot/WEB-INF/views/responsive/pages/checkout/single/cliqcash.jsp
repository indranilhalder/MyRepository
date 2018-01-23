<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="dateStyle" value="dd/MM/yyyy	" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style>
.createWalletModel {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 1; /* Sit on top */
	padding-top: 100px; /* Location of the box */
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

/* Modal Content */
.createWalletModel-content {
	background-color: #fefefe;
	margin: auto;
	padding: 20px;
	border: 1px solid #888;
	width: 50%;
}

#closePop {
	float: right;
}
#createWalletData {
	display: inline-block;
	width: 100%;
}
</style>
<template:page pageTitle="${pageTitle}">


	<div class="cliqCashContainer">
		<div class="cliqCashContainerHead">
			<p class="cliqCashInnerTop">
			<img alt="Cliq Cash" width="160" id="cliqCash_logo_img" src="\_ui\responsive\common\images\CliqCash.png">
			<img src="">
<%-- 				<strong><spring:theme code="text.add.cliq.Cash.label" --%>
<%-- 						text="Add CLiQ Cash" /></strong> --%>
			</p>
			<br />
			<p>
				<spring:theme code="text.add.cliq.cash.info" text="When you add Gift Card, the amount will be instantly added to your CLiQ Cash balance." />
			</p>
			<br />&nbsp;
		</div>
		<div class="cliqCashContainerOne">
			<div class="clearfix">
				<div class="cliqCashContainerLeft">

					<table>

						<tr>
							<%--  <c:if test="${not empty WalletBalance }"> --%>
							<td><img src="\_ui\responsive\common\images\walletImg.png" /></td>
							<td><p class="cliqCashInnerTop">
									<c:if test="${not empty WalletBalance }">
										<strong>&#8377; ${WalletBalance} </strong>
									</c:if>
								</p>
								<p class="cliqCashInnerBottom">
									<spring:theme code="text.add.cliq.cash.balance.info"
										text="CliQ Cash balance as of  "/> &nbsp;${getCurrentDate}
								</p></td>
						</tr>
					</table>
				</div>
				
				<%-- <c:choose>
				<c:when test="${isCustomerWalletActive}"> --%>
						<div class="cliqCashContainerRight">
					     <a class="cliqCashBtns" href="<c:url value="/wallet"/>"><spring:theme
							text="ADD GIFT CARD" code="text.add.cliq.cash.addgiftcard.label" />
				     	</a>
				     </div>
				<%-- </c:when>
				<c:otherwise>
				<div class="cliqCashContainerRight">
					     <a class="cliqCashBtns" style="display: none; " href="<c:url value="/wallet"/>"><spring:theme
							text="ADD GIFT CARD" code="text.add.cliq.cash.addgiftcard.label" />
				     	</a>
				     </div>
				
				</c:otherwise>
				</c:choose> --%>
		
			</div>
		</div>
		<br />
	
		<div class="cliqCashBannerSection">
		   <div class="cliqCashBannerSectionInfo">
		   Don't forget to redeem your <br /><span class="cliqCashBannerSectionInfoC1">CliQ Cash during checkout</span>
		   </div>
		</div>
		<div style="display: none;">
			<div class="cliqCashContainerTwo">
				<%-- <button class="cliqCashTablinks"
					onclick="selectSection(event, 'statement')"
					id="defaultCliqCashCategory">
					<spring:theme code="text.add.cliq.cash.tab.statement"
						text="STATEMENT" />
				</button> --%>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'cashback')"
					id="defaultCliqCashCategory">
					<spring:theme code="text.add.cliq.cash.tab.cashback"
						text="CASHBACK" />
				</button>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'refund')">
					<spring:theme code="text.add.cliq.cash.tab.refund" text="REFUND" />
				</button>
				<%-- <button class="cliqCashTablinks"
					onclick="selectSection(event, 'help')">
					<spring:theme code="text.add.cliq.cash.tab.help" text="HELP" />
				</button> --%>
			</div>

			<div id="statement" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">

								<tr>
									<td><c:set var="string1"
											value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
										<c:set var="string2" value="${fn:join(string1, ' ')}" /> 
<%-- 										<fmt:parseDate --%>
<%-- 											pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 										${formatedDate} --%>
										${string2}</td>
									<td>${walletTrasacationsListData.walletNumber}</td>
									<td>&#8377; ${walletTrasacationsListData.amount}</td>
									<c:choose>
										<c:when
											test="${fn:containsIgnoreCase(walletTrasacationsListData.notes, 'Redeem')}">
											<td>REDEEM</td>
										</c:when>
										<c:otherwise>
											<td>${walletTrasacationsListData.transactionStatus}</td>
										</c:otherwise>
									</c:choose>
									<td>${walletTrasacationsListData.transactionId}</td>
								</tr>

							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="cashback" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">
								<c:if
									test="${fn:containsIgnoreCase(walletTrasacationsListData.notes, 'CASHBACK') or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'GOODWILL')
									 or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'CREDIT') or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'PROMOTION')}">

									<tr>
										<td><c:set var="string1"
												value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
											<c:set var="string2" value="${fn:join(string1, ' ')}" /> 
<%-- 											<fmt:parseDate --%>
<%-- 												pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 											${formatedDate} --%>
											${string2}</td>
										<td>${walletTrasacationsListData.walletNumber}</td>
										<td>&#8377; ${walletTrasacationsListData.amount}</td>
										<td>${walletTrasacationsListData.transactionStatus}</td>
										<td>${walletTrasacationsListData.transactionId}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="refund" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">
								<c:if
									test=" ${fn:containsIgnoreCase(refundBaskwallet.notes, 'CANCEL REDEEM')}">
									<tr>
										<td><c:set var="string1"
												value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
											<c:set var="string2" value="${fn:join(string1, ' ')}" />
<%-- 											 <fmt:parseDate --%>
<%-- 												pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 											${formatedDate} --%>
											${string2}</td>
										<td>${walletTrasacationsListData.walletNumber}</td>
										<td>&#8377; ${walletTrasacationsListData.amount}</td>
										<td>${walletTrasacationsListData.transactionStatus}</td>
										<td>${walletTrasacationsListData.transactionId}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="help" class="cliqCashContainerTwoContent">
				<h3>
					<spring:theme code=" text.add.cliq.cash.help"
						text=" Please Contact Tata Cliq Customer Care" />
				</h3>
			</div>
		</div>

		<br />&nbsp;<br />&nbsp;<br />
	</div>
	
	<div class="createWalletModel" id="createWalletPopup">
		<div class="createWalletModel-content">
			<button type="button" onclick="closepop()" id="closePop"><span class="glyphicon glyphicon-remove-circle"></span></button>
			<span id="createWalletPopup" class="close">&times;</span>
			<div id="createWalletData"></div>

		</div>

	</div>
	
		
	<script>
	$(document).ready(function(){
		var modelAttributeValue = '${isOTPValidtion}';
		  if(modelAttributeValue === 'false'){
			  createWallet();
		  }
		});
	
	var createWalletModel = document.getElementById('createWalletPopup');
	var createWalletData = document.getElementById('createWalletData');
	var createWalletPopup = document.getElementById('createWalletPopup');
	function createWallet() {
			$.ajax({
				type : "GET",
				url : ACC.config.encodedContextPath+ "/wallet/walletOTPPopup",
				contentType : "html/text",
				success : function(response){
					createWalletData.innerHTML=response;
					createWalletModel.style.display = "block";   
						  },	
						failure : function(data) {
						}
					}); 
	}

	function closepop(){
		
		    	createWalletModel.style.display = "none";
		
	}
	window.onclick = function(event) {
	/*     if (event.target == createWalletModel) {
	    	createWalletModel.style.display = "none";
	    } */
	}
	/* createWalletPopup.onclick = function() {
		createWalletModel.style.display = "none";
	} */
	var count=0;
	function createWalletOTP(){
		    $(".mobileNumberError").hide();
		    $(".otpLastNameError").hide();
		    $(".otpFirstNameError").hide();
		    var mobileNo=$("#otpPhonenumber").val();
		    var firstName = $("#otpFirstName").val();
			var lastName = $("#otpLastName").val();
			
	     var isString = isNaN(mobileNo);

	 	//First Name Validation
	 	if(firstName == null || firstName.trim() == '' ){
	 			$(".otpFirstNameError").show();
	 			$(".otpFirstNameError").text("First name cannot be blank.");
	 			formValid = false;
	 	}else {
	 		$(".otpFirstNameError").hide();
	 	}
	 	
	 	//Last Name Validation
	     if(lastName == null || lastName.trim() == '' ){
	 			$(".otpLastNameError").show();
	 			$(".otpLastNameError").text("Last name cannot be blank.");
	 			formValid = false;
	 	}else {
	 		$(".otpLastNameError").hide();
	 	} 
	     if(isString==true || mobileNo.trim()==''){
				$(".mobileNumberError").show();
				$(".mobileNumberError").text("Enter only Numbers");
		  	}
	     else if(!/^[0-9]+$/.test(mobileNo))
	        {
		  		  $(".mobileNumberError").show();
		          $(".mobileNumberError").text("Enter only Numbers");
	      }
	     else if(mobileNo.length > 0 && mobileNo.length < 9 ){
		    	  $(".mobileNumberError").show();
		          $(".mobileNumberError").text("Enter correct mobile number");
		  	}
	     else{
	 	count++;
		if(count <= 4){
		 $.ajax({
				type : "POST",
				url : ACC.config.encodedContextPath + "/wallet/walletCreateOTP",
				data :"mobileNumber="+mobileNo,
				success : function(response) {
					if(response =='isUsed'){
						$(".mobileNumberError").show();
						$(".mobileNumberError").text("This mobile number is alredy used. Please enter different number and try again");
						$('#otp-submit-section').hide();
					}else{
					$(".wcOTPError").show();
					$(".wcOTPError").html("<span class='text-success'>OTP sent succesfully</span>");
					$('#otp-submit-section').show();
					}
				}
			}); 
		 
		}else{
			$(".otpError").show();
			$(".otpError").text("OTP limt exceeded 5 times, pleae try again");
		}
		  	}
	} 


	function submitWalletData(){
		var data = $("#walletForm").serialize();
		alert(data);
		 $.ajax({
				type : "GET",
				url : ACC.config.encodedContextPath + "/wallet/validateWalletOTP",
				data :data,
				contentType: "text/application/html",
				success : function(response) {
					
				   if(response =="isUsed"){
						$(".mobileNumberError").text("This mobile number is alredy used. Please enter other mobile number for this account");
						$(".mobileNumberError").show();
					}
					else if(response=='OTPERROR'){
						$(".wcOTPError").text("OTP verification failed. Please try again");
						$(".wcOTPError").show();
					}
					else if(response=='qcDown'){
						$(".wcOTPError").text("Unable to verify mobile number. Please try after sometime");
						$(".wcOTPError").show();
					}
					else {
						closepop();
						
					} 
					
				}
			}); 
	} 
	</script>

</template:page>

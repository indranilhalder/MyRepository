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
<style>
ul.dropdown-menu {
	border-top: none;
    border-radius: 0;
    box-shadow: none;
}

@media(max-width: 480px){
	ul.dropdown-menu {
		border: none;
	}
	
	ul.dropdown-menu li a {
		padding: 0;
		height: 50px;
		padding-top: 13px;
	}
}
</style>

<template:page pageTitle="${pageTitle}">

<input type="hidden" id="isEmailRequest" value="${isEmailRequest}" />
<input type="hidden" id="emailOrderCode" value="${orderCode}" />
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
				
		<c:choose>
				<c:when test="${isCustomerWalletActive}"> 
						<div class="cliqCashContainerRight">
					     <a class="cliqCashBtns" href="<c:url value="/wallet"/>"><spring:theme
							text="ADD GIFT CARD" code="text.add.cliq.cash.addgiftcard.label" />
				     	</a>
				     </div>
			 </c:when>
				<c:otherwise>
				<div class="cliqCashContainerRight">
					     <%-- <a class="cliqCashBtns" style="display: none; " href="<c:url value="/wallet"/>"><spring:theme
							text="ADD GIFT CARD" code="text.add.cliq.cash.addgiftcard.label" />
				     	</a> --%>
				     	<span class="addNewCard" onclick="createWallet();"><a href="#">
				     	<spring:theme code="text.cliq.cash.payment.addcard.label" /></a></span>
			    </div>
				</c:otherwise>
		</c:choose>
		
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
		var modelAttributeValue = '${isCustomerWalletActive}';
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
			var isFirstName = false;
			var isLastName = false;
			
	     var isString = isNaN(mobileNo);

	 	//First Name Validation
	 	if(firstName == null || firstName.trim() == '' ){
	 			$(".otpFirstNameError").show();
	 			$(".otpFirstNameError").text("First name cannot be blank.");
	 			isFirstName = false;
	 	}else {
	 		$(".otpFirstNameError").hide();
	 		isFirstName = true;
	 	}
	 	
	 	//Last Name Validation
	     if(lastName == null || lastName.trim() == '' ){
	 			$(".otpLastNameError").show();
	 			$(".otpLastNameError").text("Last name cannot be blank.");
	 			isLastName = false;
	 	}else {
	 		$(".otpLastNameError").hide();
	 		isLastName = true;
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
	    	 
	    	 if(isFirstName && isLastName) {
	    		 count++;
	    		 	var staticHost = $('#staticHost').val();
	    			$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	    			$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
	    			
	    			if(count <= 4){
	    			 $.ajax({
	    					type : "POST",
	    					url : ACC.config.encodedContextPath + "/wallet/walletCreateOTP",
	    					data :"mobileNumber="+mobileNo,
	    					success : function(response) {
	    						$("#no-click,.loaderDiv").remove();
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
	} 

	function editOtpField(fieldId) {
		var value = document.getElementById(fieldId);
		value.readOnly = false;
		value.focus();
	}

	function submitWalletData(){
		var data = $("#walletForm").serialize();
		$(".mobileNumberError").hide();
		$(".wcOTPError").hide();
		var staticHost = $('#staticHost').val();
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
		
		 $.ajax({
				type : "GET",
				url : ACC.config.encodedContextPath + "/wallet/validateWalletOTP",
				data :data,
				contentType: "text/application/html",
				success : function(response) {
					$("#no-click,.loaderDiv").remove();
					
				   if(response =="isUsed"){
						$(".mobileNumberError").text("This mobile number is alredy used. Please enter other mobile number for this account");
						$(".mobileNumberError").show();
					}
					else if(response=='OTPERROR'){
						$(".wcOTPError").text("OTP verification failed. Please try again");
						$(".wcOTPError").show();
					}
					else if(response=='EXPIRED'){
						$(".wcOTPError").text("Your OTP is valid for 2 minutes only, please generate again.");
						$(".wcOTPError").show();
					}
					else if(response=='success'){
						var isEmailRequest = $('#isEmailRequest').val();	
						if(isEmailRequest=='true'){
							var emailOrderCode = $('#emailOrderCode').val();
							$(location).attr('href',ACC.config.encodedContextPath+"/wallet/redimWalletFromEmail/?orderCode="+emailOrderCode);
						}else{
						closepop();
						location.reload();
						}
					}
					else {
						$(".wcOTPError").text(response);
						$(".wcOTPError").show();
					} 
					
				}
			}); 
	} 
	</script>

</template:page>

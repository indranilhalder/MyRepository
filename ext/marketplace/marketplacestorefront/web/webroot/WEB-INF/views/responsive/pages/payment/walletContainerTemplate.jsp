<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:url value="/my-account/profile" var="profileUrl" />
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>


<template:page pageTitle="${pageTitle}">

<!--     <div id="globalMessages"> -->
<%--         <common:globalMessages/> --%>
<!--     </div> -->
 	
<spring:theme code="text.addEgv.page.error.message" var="egvError" text="Please enter voucher and pin number"/>
<spring:theme code="text.addEgv.voucher.error.message" var="egvVoucherError" text="Please enter voucher number"/>
<spring:theme code="text.addEgv.pin.error.message" var="egvPinError" text="Please enter pin number"/>
<spring:theme code="text.addEGV.button.label" var="addEgvButton" text="ADD TO WALLET"/>   
<c:url value="${request.contextPath}/wallet/redimWallet" var="addEgvToWalletURL" />

<form:form id="addToCardWalletForm" action="${addEgvToWalletURL}" method="post" modelAttribute="addToCardWalletForm">
	<div>
	<br/>
	<div class="clearfix addGiftContainer">
		<div class="giftVoucherContainer">
			<div class="addGiftBlock">
				<div id="addGiftCardId" class="col-sm-12 addGiftVoucherSection" data-egvError="${egvError}" data-egvVoucherError="${egvVoucherError}" data-egvPinError="${egvPinError}">
<!-- 					<span class="addMoreGiftVouchersBtn"><input type="button" class="addMoreBtn" value="+ ADD MORE" /></span> -->
					<div class="col-sm-6 addGiftVoucherSub">
						<div class="voucherHeadingTexts"><spring:theme code="text.addEGV.card.label" text="Card Number"/><br />&nbsp;</div>
						<div class=""><!-- <input class="giftVoucherNo" type="text" placeholder="Enter Voucher Number" /><br />&nbsp; --></div>
						<form:input path="cardNumber" type="text" placeholder="16 digit card no."  onkeypress="return isNumberKey(event)"  class="giftVoucherNo" />
					</div>
					<div class="col-sm-6 addGiftVoucherSub">
						<div class="voucherHeadingTexts giftCardPinNo"><spring:theme code="text.addEGV.pin.label" text="Card Pin"/><br />&nbsp;</div>
					    <div class=""><!-- <input class="giftVoucherPin" type="text" placeholder="Enter Voucher Pin" /><br />&nbsp; --></div>
					    <form:input path="cardPin" class="giftVoucherPin" type="text" onkeypress="return isNumberKey(event)" placeholder="6 digit pin no." />
					</div>
				</div>&nbsp;
				<hr class="dividerVS" />
			</div>
		</div>
		
		<div class="col-sm-12 addMoreGiftVouchers clearfix">
		<div class="alert alert-warning redeemVoucherAlert col-sm-4">
				<span id="redeemVoucherError"></span>
			</div>
			<!-- <span class="btn addVoucherBtn addMoreGiftVouchersBtn pull-left">Add Gift Voucher<span class="glyphicon glyphicon-plus"></span></span> -->
			<span class="redeemToWalletBtn"><form:input path="submit" type="submit"  value="${addEgvButton}" class="addVoucherBtn" /></span>
		</div>
		<br />&nbsp;
	</div>
</div>
<br />&nbsp;
	</form:form>
</template:page>
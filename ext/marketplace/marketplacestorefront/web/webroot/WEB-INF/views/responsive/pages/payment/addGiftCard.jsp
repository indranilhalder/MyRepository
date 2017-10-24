<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:theme code="text.addEgv.page.error.message" var="egvError" text="Please enter voucher and pin number"/>
<spring:theme code="text.addEgv.voucher.error.message" var="egvVoucherError" text="Please enter voucher number"/>
<spring:theme code="text.addEgv.pin.error.message" var="egvPinError" text="Please enter pin number"/>
<spring:theme code="text.addEGV.button.label" var="addEgvButton" text="ADD TO WALLET"/>   
<form:form id="addToCardWalletFormId"  method="post" commandName="addToCardWalletForm">
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
						<form:input path="cardNumber" id="giftVoucherNoId" type="text" placeholder="Enter 16 digit card number"  onkeypress="return isNumberKey(event)"  class="giftVoucherNo" />
					</div>
					<div class="col-sm-6 addGiftVoucherSub">
						<div class="voucherHeadingTexts giftCardPinNo"><spring:theme code="text.addEGV.pin.label" text="Card Pin"/><br />&nbsp;</div>
					    <div class=""><!-- <input class="giftVoucherPin" type="text" placeholder="Enter Voucher Pin" /><br />&nbsp; --></div>
					    <form:input path="cardPin" id="giftVoucherPinId" class="giftVoucherPin" type="password" onkeypress="return isNumberKey(event)" placeholder="Enter 6 digit pin number" />
					</div>
				</div>&nbsp;
				<hr class="dividerVS" />
			</div>
		</div>
		
		<div class="col-sm-12 addMoreGiftVouchers clearfix">
		<div id="redeemVoucherAlertId" class="alert alert-warning redeemVoucherAlert redeemVoucherPopupAlert col-lg-4">
				<span id="redeemVoucherError"></span>
	   </div>
			<!-- <span class="btn addVoucherBtn addMoreGiftVouchersBtn pull-left">Add Gift Voucher<span class="glyphicon glyphicon-plus"></span></span> -->
			<span class="redeemToWalletBtn addVoucherBtn addVoucherPopupBtn" onclick= "addEGVAjax();">${addEgvButton}</span>
		</div>
		<br />&nbsp;
	</div>
</div>
<br />&nbsp;
	</form:form>
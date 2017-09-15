<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:url value="/my-account/profile" var="profileUrl" />


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,places&callback=initializeGoogleMaps"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">	
	

<style>
	.addGiftContainer {border: 1px solid #ddd;}
	/* .addGiftVoucherSection {padding: 0 !important;}
	.addGiftVoucherSub {padding: 0;} */
	.giftVoucherNo, .giftVoucherPin {
	  width: 100%;
	  height: 37px !important;
	  border-radius: 4px;
	  border: none !important;
	  background-color: #ededed;
	}
	.addVoucherBtn, .addVoucherBtn:hover  {
	  max-width: 230px;
	  height: 30px;
	  border: none;
	  line-height: 12px;
	  background-color: #a9143c;
	  color: white;
	  font-size: 12px;
	  font-weight: 400;
	  padding: 0 15px;
	}
	
	.addMoreBtn, .addMoreBtn:hover, .addMoreBtn:active {
	  font-size: 10px;
	  border: 1px solid #a9143c;
	  padding: 0 5px;
	  height: 25px;
	  line-height: normal;
	  margin-top: 5px;
	  color: #a9143c;
	  position: absolute;
      right: 2%;
      cursor: pointer;
      z-index: 1;
	}
	
	.addMoreGiftVouchers {margin-top: 2%;}
	
	.addMoreGiftVouchersBtn {float: right;}
	
	.voucherHeadingTexts {padding-left: 0 !important;}
	.dividerVS {margin: 2%; border-bottom: none; border-top: 1px solid #ddd;}
	
	@media(min-width: 651px){
		.addGiftContainer{margin-left: 10% ;margin-right: 10%;}
		.voucherHeadingTexts {padding-top: 4%;}
		.dividerVS { margin-bottom: 0;}
		.redeemToWalletBtn {float: right;}
		.redeemVoucherAlert {padding: 10px !important; margin: 0 !important;}
	}
	
	@media(max-width: 650px){
		.addGiftContainer {margin: 3%; border-radius: 8px; padding-top: 8px;}
		.addVoucherBtn {font-size: 12px !important;}
		.addGiftBlock {padding-top: 2%;}
		.addMoreGiftVouchers {margin-top: 5%;}
		.addMoreBtn, .addMoreBtn:hover, .addMoreBtn:active {font-size: 8px; height: 20px; margin-top: -5px; right: 5%;}
		.redeemVoucherAlert {border-radius: 4px !important;}
	}
</style>	
<script>

//Allowing only numbers
function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

	$(document).ready(function (){
		var giftVoucherTemplate = $(".addGiftBlock").html();
		$(".giftVoucherContainer .addMoreBtn").click(function (){
			$(".giftVoucherContainer").append(giftVoucherTemplate);
			$(".addMoreGiftVouchersBtn").eq(1).remove();
		});
		//Validations
		$(".redeemVoucherAlert").hide();
		$(".addVoucherBtn").click(function (){
			//redeemVoucherError
			//var redeemGiftValidation = $("#redeemVoucherError").html();
			if ($(".giftVoucherNo").val() == '' && $(".giftVoucherPin").val() == '') {
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = 'Please enter voucher and pin number';
				return false;
			} else if ($(".giftVoucherNo").val() == '') {
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = 'Please enter voucher number';
				return false;
			} else if ($(".giftVoucherPin").val() == ''){
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = 'Please enter pin number';
				return false;
			} else {
				$(".redeemVoucherAlert").hide();

			}
		});
	});
</script>
<template:page pageTitle="${pageTitle}">

<form:form id="addToCardWalletForm" action="${request.contextPath}/wallet/redimWallet" method="post" modelAttribute="addToCardWalletForm">
	<div>
	<br />
	<div class="clearfix addGiftContainer">
		<div class="giftVoucherContainer">
			<div class="addGiftBlock">
				<div class="col-sm-12 addGiftVoucherSection">
					<span class="addMoreGiftVouchersBtn"><input type="button" class="addMoreBtn" value="+ ADD MORE" /></span>
					<div class="col-sm-6 addGiftVoucherSub">
						<div class="voucherHeadingTexts">Card No<br />&nbsp;</div>
						<div class=""><!-- <input class="giftVoucherNo" type="text" placeholder="Enter Voucher Number" /><br />&nbsp; --></div>
						<form:input path="cardNumber" type="text" placeholder="Enter Voucher Number"  onkeypress="return isNumberKey(event)"  class="giftVoucherNo" />
					</div>
					<div class="col-sm-6 addGiftVoucherSub">
						<div class="voucherHeadingTexts">Card Pin<br />&nbsp;</div>
					    <div class=""><!-- <input class="giftVoucherPin" type="text" placeholder="Enter Voucher Pin" /><br />&nbsp; --></div>
					    <form:input path="cardPin" class="giftVoucherPin" type="text" onkeypress="return isNumberKey(event)" placeholder="Enter Voucher Pin" />
					</div>
				</div>
				<br />&nbsp;
				<hr class="dividerVS" />
			</div>
		</div>
		
		<div class="col-sm-12 addMoreGiftVouchers clearfix">
		<div class="alert alert-warning redeemVoucherAlert col-sm-4">
				<span id="redeemVoucherError"></span>
			</div>
			<!-- <span class="btn addVoucherBtn addMoreGiftVouchersBtn pull-left">Add Gift Voucher<span class="glyphicon glyphicon-plus"></span></span> -->
			<span class="redeemToWalletBtn"><form:input path="submit" type="submit"  value="ADD TO WALLET" class="addVoucherBtn" /></span>
		</div>
		<br />&nbsp;
	</div>
</div>
<br />&nbsp;
	</form:form>
</template:page>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
<cart:tealiumCartParameters/>
<c:if test="${deviceType ne 'mobile'}">
<div id="singlePageDesktop">
	<!-- Accordions -->
	<div id="singlePageAccordion" class="checkout-accordion-wrapper">
	    <div class="checkout-accordion accordion-open">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>1</span> Delivery Address</h4></div>
	            <div style="display:none" id="selectedAddressDivId"><span id="selectedAddressHighlight"></span><span>CHANGE ADDRESS</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="chooseDeliveryAddress">
	                <single-checkout:showDeliveryAddressDetails deliveryAddresses="${deliveryAddresses}"/>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>2</span> Delivery Method</h4></div>
	            <div style="display:none" id="selectedDeliveryOptionsDivId"><span id="selectedDeliveryOptionsHighlight"></span><span>CHANGE DELIVERY OPTION</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="choosedeliveryMode" class="cart wrapper">
	                <p></p>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	        <div class="checkout-mobile-heading">Review Order</div>
	            <div><h4><span>3</span> Review Order</h4></div>
	            <div style="display:none" id="selectedReviewOrderDivId"><span id="selectedReviewOrderHighlight"></span><span>REVIEW ORDER</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="reviewOrder" class="cart wrapper">
	                <p></p>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>4</span> Payment</h4></div>
	            <div style="display:none"><span></span><span></span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="makePaymentDiv">
	                <c:if test="${fn:contains(prePopulateTab, 'payment')}" >
	                	<c:set var="is_responsive" value="false"></c:set>
	                	<%@include file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/pages/checkout/single/showAddPaymentMethodPage.jsp"%> 
	                </c:if>
	            </div>
	        </div>
	    </div>
	</div>
</div>
</c:if>
<span id="deviceType" style="display:none;">${deviceType}</span>
<c:if test="${deviceType ne 'normal'}">
<div id="singlePageMobile">
	<div class="checkout_mobile_section" id="chooseDeliveryAddressMobile">
		<div class="checkout-mobile-heading">Delivery Address</div>
		<div class="change-cancel-wrapper">
		<p class="change-mobile" id="address-change-link">Change</p>
		<p class="cancel-mobile">Cancel</p>
		</div>
		<div id="chooseDeliveryAddressMobile">
			<single-checkout:showDeliveryAddressDetailsMobile deliveryAddresses="${deliveryAddresses}"/>
		</div>
	</div>
	<div  class="checkout_mobile_section cart wrapper"  id="selectedDeliveryOptionsDivIdMobile">
		<div class="checkout-mobile-heading">Delivery Method</div>
		<div class="change-cancel-wrapper">
		<p class="change-mobile">Change</p>
		<p class="cancel-mobile">Cancel</p>
		</div>
		<div id="choosedeliveryModeMobile">
			<%@include
				file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/fragments/checkout/single/showDeliveryModesDetailsMobile.jsp"%>
		</div>
	</div>
	<div  class="checkout_mobile_section" id="makePaymentMobile">
	<div class="checkout-mobile-heading">Payment</div>
	<div class="change-cancel-wrapper">
		<p class="change-mobile">Change</p>
		<p class="cancel-mobile">Cancel</p>
		</div>
		<div id="makePaymentDivMobile">
			<c:set var="is_responsive" value="true"></c:set>
			<%@include file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/pages/checkout/single/showAddPaymentMethodPage.jsp"%>
		</div>
	</div>
</div>
</c:if>
<div class="modal fade" id="singlePageAddressPopup">
	<div class="content" style="padding: 40px;max-width: 650px;">
		<span id="modalBody"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div class="modal fade" id="singlePagePickupPersonPopup">
	<div class="content" style="padding: 40px;max-width: 650px;">
		<span id="modalBody" data-htmlPopulated="NO"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div class="modal fade" id="singlePageChooseSlotDeliveryPopup">
	<div class="content" style="padding: 40px;">
		<span id="modalBody" data-htmlPopulated="NO"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>

<div id="confirmOverlay" style="display:none">
    <div id="confirmBox" style="display:none">

         <h1>OOPss Exchange Is not Serviceable</h1>
        <p>Do you want to continue without Exchange?</p>

        <div id="confirmButtons">
            <a class="button blue" id="exConfirmYes" href="#">Yes<span></span></a>
            <a class="button gray"  id="exConfirmNo" href="#">No<span></span></a>
            </div>
    </div>
	</div>	

<style>
#confirmOverlay{
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	background:url('ie.png');
	background: -moz-linear-gradient(rgba(11,11,11,0.1), rgba(11,11,11,0.6)) repeat-x rgba(11,11,11,0.2);
	background:-webkit-gradient(linear, 0% 0%, 0% 100%, from(rgba(11,11,11,0.1)), to(rgba(11,11,11,0.6))) repeat-x rgba(11,11,11,0.2);
	z-index:100000;
}

#confirmBox{
	background:url('body_bg.jpg') repeat-x left bottom #e5e5e5;
	width:460px;
	position:fixed;
	left:50%;
	top:50%;
	margin:-130px 0 0 -230px;
	border: 1px solid rgba(33, 33, 33, 0.6);

	-moz-box-shadow: 0 0 2px rgba(255, 255, 255, 0.6) inset;
	-webkit-box-shadow: 0 0 2px rgba(255, 255, 255, 0.6) inset;
	box-shadow: 0 0 2px rgba(255, 255, 255, 0.6) inset;
}

#confirmBox h1,
#confirmBox p{
	font:26px/1 'Cuprum','Lucida Sans Unicode', 'Lucida Grande', sans-serif;
	background:url('header_bg.jpg') repeat-x left bottom #f5f5f5;
	padding: 18px 25px;
	text-shadow: 1px 1px 0 rgba(255, 255, 255, 0.6);
	color:#666;
}

#confirmBox h1{
	letter-spacing:0.3px;
	color:#888;
}

#confirmBox p{
	background:none;
	font-size:16px;
	line-height:1.4;
	padding-top: 35px;
}

#confirmButtons{
	padding:15px 0 25px;
	text-align:center;
}

#confirmBox .button{
	display:inline-block;
	background:url('buttons.png') no-repeat;
	color:white;
	position:relative;
	height: 33px;

	font:17px/33px 'Cuprum','Lucida Sans Unicode', 'Lucida Grande', sans-serif;

	margin-right: 15px;
	padding: 0 35px 0 40px;
	text-decoration:none;
	border:none;
}

#confirmBox .button:last-child{	margin-right:0;}

#confirmBox .button span{
	position:absolute;
	top:0;
	right:-5px;
	background:url('buttons.png') no-repeat;
	width:5px;
	height:33px
}

#confirmBox .blue{				background-position:left top;text-shadow:1px 1px 0 #5889a2;}
#confirmBox .blue span{			background-position:-195px 0;}
#confirmBox .blue:hover{		background-position:left bottom;}
#confirmBox .blue:hover span{	background-position:-195px bottom;}

#confirmBox .gray{				background-position:-200px top;text-shadow:1px 1px 0 #707070;}
#confirmBox .gray span{			background-position:-395px 0;}
#confirmBox .gray:hover{		background-position:-200px bottom;}
#confirmBox .gray:hover span{	background-position:-395px bottom;}

</style>
<script>
$(".checkout-accordion-wrapper .checkout-accordion .checkout-accordion-heading").on("click", function(){
	//$(this).parent().find(".checkout-accordion-body").slideToggle();
	$(this).parent().toggleClass("accordion-open");
	//$(this).parent().siblings().find(".checkout-accordion-body").slideUp();
	$(this).parent().siblings().removeClass("accordion-open");
});
</script>
<c:if test="${isCart eq true}">
	<script>
		
		var timeoutID;
					function setup() {
					    this.addEventListener("mousemove", resetTimer, false);
					    this.addEventListener("mousedown", resetTimer, false);
					    this.addEventListener("keypress", resetTimer, false);
					    this.addEventListener("DOMMouseScroll", resetTimer, false);
					    this.addEventListener("mousewheel", resetTimer, false);
					    this.addEventListener("touchmove", resetTimer, false);
					    this.addEventListener("MSPointerMove", resetTimer, false);
					    startTimer();
					}
					setup();
	
					function startTimer() {
					    // wait 2 seconds before calling goInactive
					    timeoutID = window.setTimeout(goInactive, '${timeout}');
					}
	
					function resetTimer(e) {
					    window.clearTimeout(timeoutID);
	
					    goActive();
					}
	
					function goInactive() {
					   window.location = '${request.contextPath}/cart';
					}
	
					function goActive() {
					      startTimer();
					}
					
					$(document).ready(function(){
						//Preventing form submission on key press.
						$(document).on('keypress','input,select,textarea', function(e) {
							var code = e.keyCode || e.which;
							if(code == 13)
						        return false;

						});
					})
					
	</script>
</c:if>
</template:page>

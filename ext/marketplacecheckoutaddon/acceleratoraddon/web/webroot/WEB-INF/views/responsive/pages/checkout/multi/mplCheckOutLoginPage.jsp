<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>




<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
 <input type="hidden" id="isSignInActive" value="${isSignInActive}">
 
	<div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div>
	<div class="row checkout-content">
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
			<script>
    				window.onload =	function(){
    					//resetConvChargeElsewhere();
    					activateSignInTab();
    				}
				</script>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping">
						<div class="checkout-indent">
							<div class="sign-in tab-view checkoutSignPage">
								<ul class="nav">
									<li id="signIn_link" class="active"><spring:theme code="text.signin" text="Sign In"/></li>
									<li id="SignUp_link"><spring:theme code="text.signup" text="New to CliQ?"/></li>
				
								</ul>

		
								<ul class="tabs">
									<li id="sign_in_content" class="active">
										<c:url value="/checkout/j_spring_security_check" var="loginAndCheckoutActionUrl" />
										<multi-checkout:login actionNameKey="checkout.login.loginAndCheckout" action="${loginAndCheckoutActionUrl}"/>
									</li>
									<li id="sign_up_content">
										<c:url value="/checkout/multi/checkoutlogin/checkoutRegister" var="registerAndCheckoutActionUrl" />
										<multi-checkout:register actionNameKey="checkout.login.registerAndCheckout" action="${registerAndCheckoutActionUrl}" />
									</li>
			
								</ul>
							</div>
						</div>
					</div>
				</ycommerce:testId>
			</jsp:body>
		</multi-checkout:checkoutSteps>			
	
		
		<div class="col-sm-12 col-lg-9">
			<cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
	</div>

</template:page>

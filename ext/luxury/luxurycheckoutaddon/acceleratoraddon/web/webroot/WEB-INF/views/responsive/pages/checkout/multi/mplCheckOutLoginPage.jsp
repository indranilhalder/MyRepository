<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/address"%>



from addon page
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
 <input type="hidden" id="isSignInActive" value="${isSignInActive}">
 
	<div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div>
	<div class="row checkout-content checkoutLoginSection">
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
			<script>
    				/* window.onload =	function(){
    					//resetConvChargeElsewhere();
    					activateSignInTab();
    				} */
    				$(window).on('load resize',function(){	
    					if($(".sign-in.tab-view .nav").css("display") == "block"){
    						activateSignInTab();
    					}
    					else{
    						$("#sign_in_content, #sign_up_content").addClass('active');
    					}
    				});
				</script>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping">
						<div class="checkout-indent">
						<div class="sign-in wrapper">
							<div class="sign-in tab-view checkoutSignPage">
								<ul class="nav">
									<li id="signIn_link" class="active"><spring:theme code="text.signin" text="Sign In"/></li>
									<li id="SignUp_link"><spring:theme code="text.signup" text="New to CliQ?"/></li>
				
								</ul>

		
								<ul class="tabs row">
									<li id="sign_in_content" class="active col-xs-12 col-sm-5 ">
										<c:url value="/checkout/j_spring_security_check" var="loginAndCheckoutActionUrl" />
										<multi-checkout:login actionNameKey="checkout.login.loginAndCheckout" action="${loginAndCheckoutActionUrl}"/>
									</li>
									<li class="or col-xs-12 col-sm-2 text-center">			
					<div class="row">
						<div class="hidden-sm-down col-sm-12">
							<span class="vr-line">&nbsp;</span>
						</div>
						<div class="col-sm-12 orTxt">
							<span class="or-rounded">or</span>
						</div>
						<div class="hidden-sm-down col-sm-12">
							<span class="vr-line">&nbsp;</span>
						</div>
					</div>
				</li>
									<li id="sign_up_content" class="col-xs-12 col-sm-5">
										<c:url value="/checkout/multi/checkoutlogin/checkoutRegister" var="registerAndCheckoutActionUrl" />
										<multi-checkout:register actionNameKey="checkout.login.registerAndCheckout" action="${registerAndCheckoutActionUrl}" />
									</li>
			
								</ul>
							</div>
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

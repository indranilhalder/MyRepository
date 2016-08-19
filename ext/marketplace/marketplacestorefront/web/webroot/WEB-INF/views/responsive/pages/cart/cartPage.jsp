<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<template:page pageTitle="${pageTitle}">
	<cart:tealiumCartParameters/>
	<cart:cartValidation/>
	<cart:cartPickupValidation/>

		<%--<div class="cart-top-bar">
		<div class="container">
		 <div class="text-right">
				<a href="" class="help js-cart-help" data-help="<spring:theme code="text.help" />"><spring:theme code="text.help" text="Help" />
					<span class="glyphicon glyphicon-info-sign"></span>
				</a>
				<div class="help-popup-content-holder js-help-popup-content">
					<div class="help-popup-content">
						<strong>${cartData.code }</strong>
						<spring:theme code="basket.page.cartHelpContent" text="Need Help? Contact us or call Customer Service at 1-###-###-####. If you are calling regarding your shopping cart, please reference the Shopping Cart ID above." />
					</div>
				</div>
			</div> 
		</div>

	</div>--%>

	<div>
	<div class="cart wrapper">
	   <c:if test="${not empty cartData.entries}">
			   <cms:pageSlot position="CenterLeftContentSlot" var="feature" >
				   <cms:component component="${feature}"/>
			   </cms:pageSlot>
		</c:if>
		<%-- <cms:pageSlot position="CenterRightContentSlot" var="feature" element="div" class="right-block">
				<cms:component component="${feature}"/>
			</cms:pageSlot> --%>
		<cms:pageSlot position="TopContent" var="feature" element="div" class="cart-top-block" >
			<cms:component component="${feature}"/>
		</cms:pageSlot> 
		 
		 <c:if test="${not empty cartData.entries}">
			<cms:pageSlot position="CenterRightContentSlot" var="feature" element="div" class="right-block">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			<%-- <cms:pageSlot position="BottomContentSlot" var="feature" element="div" class="col-xs-9" >
				<cms:component component="${feature}"/>
			</cms:pageSlot> --%>
		</c:if>
				
				
		 <c:if test="${empty cartData.entries}">
		 <div class="emptyCart-mobile">
			<cms:pageSlot position="EmptyCartMiddleContent" var="feature" element="div"  >
				<cms:component component="${feature}"/>
			</cms:pageSlot>
				<span id="removeFromCart_Cart" style="display:none;color:#60A119;"><!-- And it's out!</span> --><spring:theme code="remove.product.cartmsg"/></span>
			</div>
		</c:if>
		</div>
<c:if test="${not empty cartData.entries}">	
		<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products"></div>
<!-- For Infinite Analytics End -->
</c:if>
	</div>

</template:page>

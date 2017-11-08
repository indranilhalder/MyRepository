<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/cart" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/common" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi" %>
<!-- LW-230 -->
<input type="hidden" id="isLuxury" value="true"/>
<div class="content"><div class="header-promo text-center only-in-cart"><p>Free Shipping + free returns <a href="#">Know More</a></p></div></div>
<template:page pageTitle="${pageTitle}">


<cart:tealiumCartParameters/>
	<%--<cart:cartValidation/>
	<cart:cartPickupValidation/> --%>

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

	<section class="cartSection">
		<section class="cartContent">
			<div >

				<div class="row">
					<div class="col-xs-12 <c:out value="${not empty cartData.entries ? 'col-md-8 col-sm-8' : 'col-md-12 col-sm-12'}" />">

						<div class="cartItems cart wrapper">

							<!-- UF-62 -->
							<c:if test="${not empty cartData.entries}">
								<h3 class="grayTxt"><spring:theme code="mpl.myBag" />(<label id="mybagcnt"></label> item) <span>Items in your cart are not reserved and can sell out.</span></h3>
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




							<c:if test="${empty cartData.entries}">
								<div class="emptyCart-mobile">
									<cms:pageSlot position="EmptyCartMiddleContent" var="feature" element="div"  >
										<cms:component component="${feature}"/>
									</cms:pageSlot>
									<div class="emptyCart">
										<img src="${themeResourcePath}/images/empty-cardicon.png">
										<h2>Your Shopping cart is empty</h2>
										<p>Add products to it.</p>
										<a href="/menlanding" class="btn btn-primary btn-lg">Shop Men</a></class>
										<a href="/womenlanding" class="btn btn-primary btn-lg">Shop Women</a></class>
									</div>
									<span id="removeFromCart_Cart" style="display:none;color:#60A119;"><!-- And it's out!</span> --><spring:theme code="remove.product.cartmsg"/></span>
								</div>
							</c:if>
						</div>
							<%--<c:if test="${not empty cartData.entries}">
                                <!-- For Infinite Analytics Start -->
                                <div class="trending"  id="ia_products"></div>
                                <!-- For Infinite Analytics End -->
                            </c:if>--%>

					</div>
					<c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>


					<c:if test="${not empty cartData.entries}">
						<div class="col-xs-12 col-sm-4 col-md-4 right-block">
							<div class="orderSummaryBlock mt-30">
								<cms:pageSlot position="CenterRightContentSlot" var="feature"
											  element="div" class="orderSummary mb-20">
									<cms:component component="${feature}" />
								</cms:pageSlot>
									<%-- <cms:pageSlot position="BottomContentSlot" var="feature" element="div" class="col-xs-9" >
                                        <cms:component component="${feature}"/>
                                    </cms:pageSlot> --%>
								<h4 class="grayTxt text-center">Summary</h4>
								<multi-checkout:orderTotals isCart="true" cartData="${cartData}"
															showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
							</div>
							<div id="checkout-id" class="checkoutBtn text-center mt-20">
								<a id="checkout-enabled" href="${checkoutUrl}" class="btn btn-primary btn-lg proceedCheckout" >Proceed to Checkout</a>
								<p class="normalSizeRegularTxt grayTxt">Promo codes can be added at checkout.</p>
							</div>
							<div class="cartBottomCheck">
								<div id="changePinDiv">
									<p></p>
									<p id="cartPinCodeAvailable" class="cartPins"><spring:theme code="product.pincode.input" /></p>
									<!-- TPR_1055 EQA -->
									<!-- <p id="AvailableMessage" style="display:none"></p> -->
										<%--
                                                                            <p id="unserviceablepincodeBtm" style="display:none" class="unservicePins"><spring:theme code="cart.unserviceable.pincode" /></p>
                                        --%>
									<p id="error-Id" style="display:none" ><spring:theme code="product.invalid.pincode" /></p>
									<p id="emptyId" style="display:none"><spring:theme code="product.empty.pincode" /></p>
									<c:choose>
										<c:when test="${not empty defaultPinCode}">
											<input type="text" id= "defaultPinCodeIds" name = "defaultPinCodeIds" style="" value="${defaultPinCode}" placeholder="Pincode" maxlength="6" onkeypress="return isNumber(event)" />
										</c:when>
										<c:otherwise>
											<input type="text" id= "defaultPinCodeIds" name = "defaultPinCodeIds" style="" value="" placeholder="Pincode" maxlength="6" onkeypress="return isNumber(event)" />
										</c:otherwise>
									</c:choose>
									<button id= "pinCodeButtonIds" name="pinCodeButtonId" style="" type="" onclick="return luxurycheckPincodeServiceability('typeSubmit',this);"><spring:theme code="text.submit"/></button>
									<!-- <p id="AvailableMessage" style="display:none" class="availablePins"></p> -->
									<p id="unserviceablepincode" style="display:none" class="unservicePins error_text"><spring:theme code="cart.unserviceable.pincode" /></p>
									<p id="error-Id" style="display:none" class="errorPins"><spring:theme code="product.invalid.pincode" /></p>
									<p id="emptyId" style="display:none" class="emptyPins"><spring:theme code="product.empty.pincode" /></p>
								</div>
							</div>
						
						</div>
					</c:if>


				</div>
			</div>
		</section>
	</section>

</template:page>

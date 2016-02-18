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

<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>



<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">

	<%-- <div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div> --%>
	<div class="checkout-content cart checkout wrapper">
	<script type="text/javascript" src="/store/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
		<c:if test="${showDeliveryMethod eq true}">
		
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
				<script>
    				//window.onload =	function(){
    					//resetConvChargeElsewhere(); Commented for TIS 400
    				//}
    				$(document).ready(function() {
    					calculateDeliveryCost();
    				});
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
				</script>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping left-block">
						<div class="checkout-indent">
							<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm">
									<multi-checkout:shipmentItems cartData="${cartData}" defaultPincode="${defaultPincode}" showDeliveryAddress="true" />
								<button class="button" id="deliveryMethodSubmit" type="submit" class="checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
							</form:form>
							<%-- <p><spring:theme code="checkout.multi.deliveryMethod.message" text="Items will ship as soon as they are available. <br> See Order Summary for more information." /></p> --%>
						</div>
					</div>
				</ycommerce:testId>
			</jsp:body>
			</multi-checkout:checkoutSteps>			
		</c:if>
		
	
	
		<c:if test="${showAddress eq true}">
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
			<script>
			//TISST-13010
			$(document).ready(function() {
				showPromotionTag();
			});
			
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
    			</script>
				<ycommerce:testId code="checkoutStepTwo"> 
					<div class="checkout-shipping"> 
						<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<p class="items-no"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></p>
									<ul class="product-block addresses saved">		
									<li class="header">		
  										<ul>		
             								 <li><spring:theme code="checkout.multi.deliveryAddress.savedAddress"	text="Saved Addresses" /></li>		
              								<li class="pincode-button"><a href="${request.contextPath}/checkout/multi/delivery-method/new-address"> 
											<spring:theme code="checkout.multi.deliveryAddress.useNewAddress" text="Use New Address"></spring:theme>
										</a></li>		
          								</ul>		
  									</li>
									<c:if test="${not empty deliveryAddresses}"> 
								
										<form id="selectAddressForm" action="${request.contextPath}/checkout/multi/delivery-method/select-address" method="get">
											<c:set var='countWork'  value='1' />
											<c:set var='countHome'  value='1' />
											<div class="addressList">
												<c:forEach items="${deliveryAddresses}" var="deliveryAddress" varStatus="status">
												<li class="item"> 
													<div class="addressEntry">
														<ul>
															<c:choose>
																<c:when test="${deliveryAddress.defaultAddress}">
																	<input type="radio"  name="selectedAddressCode" value="${deliveryAddress.id}"  id="radio_${deliveryAddress.id}" checked="checked"/>
																	<label class="addressLabel" for="radio_${deliveryAddress.id}"></label>
																</c:when>
																<c:otherwise>
																	<input type="radio"  name="selectedAddressCode" value="${deliveryAddress.id}"  id="radio_${deliveryAddress.id}"/>
																	<label class="addressLabel" for="radio_${deliveryAddress.id}"></label>
																</c:otherwise>
															</c:choose>	
															<c:choose>
																<c:when test="${deliveryAddress.addressType eq 'Work'}">
																<label class="commercial" >
																	<spring:theme code="checkout.multi.deliveryAddress.commercialAddress" text="Commercial Addresses"/> <c:out value="${countWork}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countWork'  value='${countWork+1}' />
																</c:when>
																<c:otherwise>
																<label class="residential">
																	<spring:theme code="checkout.multi.deliveryAddress.residentialAddress" text="Residential Addresses"/> <c:out value="${countHome}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countHome'  value='${countHome+1}' />
																</c:otherwise>
															</c:choose>
															
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp;  ${fn:escapeXml(deliveryAddress.line2)},  &nbsp;  ${fn:escapeXml(deliveryAddress.line3)},  &nbsp; </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															
															<a href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"><spring:theme code="checkout.multi.deliveryAddress.editAddress" text="Edit Address"></spring:theme> </a>
														</ul>
												
													</div>
								
												</c:forEach>
													
											</div>
											<button id="deliveryAddressSubmit" type="submit" class="button checkout-next" ><spring:theme code="checkout.multi.deliveryMethod.continue"  text="Next"/></button>
										</form>
									</c:if>
						</div>
					</div>
				</ycommerce:testId>
			</jsp:body>
			</multi-checkout:checkoutSteps>	
		</c:if>
				
			
								
		<c:if test="${showEditAddress eq true}">
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
				<jsp:body>
					<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
					});
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
					</script>
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping">
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></div>
								<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme code="text.account.addressBook.addressDetails"/></li>
									        <li class="pincode-button"><a href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									       
									       
									        <c:if test="${addressFlag eq 'T'}"> 
										<spring:theme code="checkout.multi.deliveryAddress.useSavedAddress" text="Use a Saved Address"></spring:theme>	
										</c:if>																
									</a></li>
									      </ul>
									  	</li>
									  	<li class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector supportedCountries="${countries}"
												regions="${regions}" cancelUrl="${currentStepUrl}"
												country="${country}" />
									  	</li>
									  	</ul>
							</div>
						</div>
					</ycommerce:testId>
				</jsp:body>
			</multi-checkout:checkoutSteps>			
		</c:if>		
	
		
		<c:if test="${addAddress eq true}">
		  
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
				<jsp:body>
					<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
					});
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
					</script>
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping">
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></div>
									<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme	code="checkout.multi.deliveryAddress.newAddress" text="New Address"> </spring:theme> </li>																  			 			 							        
									        <li class="pincode-button"><a href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									        
									         <c:if test="${addressFlag eq 'T'}"> 
											<spring:theme code="checkout.multi.deliveryAddress.useSavedAddress" text="Use a Saved Address"></spring:theme>
											 </c:if>
										</a></li>
										
									      </ul>
									  	</li>
									  	<li class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector supportedCountries="${countries}"
																				regions="${regions}" cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									</div>
							</div>
						
					</ycommerce:testId>
				</jsp:body>
			</multi-checkout:checkoutSteps>			
				
		</c:if>
		
	<div class="right-block shipping">
			<div class="checkout-order-summary">
				<multi-checkout:orderTotals cartData="${cartData}"
					showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
			</div>
			</div>
		<%-- <div class="col-sm-12 col-lg-9">
			<cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div> --%>
	</div>

</template:page>

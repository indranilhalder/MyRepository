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
<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>



<template:page pageTitle="${pageTitle}" hideHeaderLinks="true"
	showOnlySiteLogo="true">
	<cart:tealiumCartParameters />
	<%-- <div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div> --%>
	<div class="checkout-content cart checkout wrapper delivery">
		<!-- store url fix -->
		<script type="text/javascript"
			src="/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
		<c:if test="${showDeliveryMethod eq true}">

			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
				progressBarId="${progressBarId}">
				<jsp:body>
				<script>
					//window.onload =	function(){
					//resetConvChargeElsewhere(); Commented for TIS 400
					//}
					$(document).ready(function() {
						selectDefaultDeliveryMethod();
						
						/* $('#deliveryMethodSubmit').click(function(e) {
							alert(5);
							e.preventDefault();
							//$('#selectDeliveryMethodForm').submit();
						}); */
					});
					var timeoutID;
					function setup() {
						this.addEventListener("mousemove", resetTimer, false);
						this.addEventListener("mousedown", resetTimer, false);
						this.addEventListener("keypress", resetTimer, false);
						this.addEventListener("DOMMouseScroll", resetTimer,
								false);
						this.addEventListener("mousewheel", resetTimer, false);
						this.addEventListener("touchmove", resetTimer, false);
						this.addEventListener("MSPointerMove", resetTimer,
								false);
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
				
				<form:form id="selectDeliveryMethodForm"
							action="${request.contextPath}/checkout/multi/delivery-method/check"
							method="post" commandName="deliveryMethodForm">
				<!-- TISCR-305 starts -->
				<!-- TISPRO-625 starts -->
				<input type="hidden" id="isExpressCheckoutSelected"
								value="${isExpressCheckoutSelected}" />
					<c:choose>
						<c:when test="${isExpressCheckoutSelected}">
								<button class="button proceed-button" id="deliveryMethodSubmitUp" type="submit"
										class="checkout-next">
										<spring:theme
											code="checkout.multi.deliveryMethod.expresscheckout.continue"
											text="Next" />
									</button>
						</c:when>
						<c:otherwise>
									<button class="button proceed-button" id="deliveryMethodSubmitUp"
										type="submit" class="checkout-next">
										<spring:theme code="checkout.multi.deliveryMethod.continue"
											text="Next" />
									</button>
						</c:otherwise>
					</c:choose>
				<!-- TISPRO-625 ends -->
				<!-- TISCR-305 ends -->
					<div class="checkout-shipping left-block">
					
						<div class="checkout-indent">
							<%-- <form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm"> --%>
									<multi-checkout:shipmentItems cartData="${cartData}"
										defaultPincode="${defaultPincode}" showDeliveryAddress="true" />
								<%--
								<button class="button" id="deliveryMethodSubmit" type="submit" class="checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
							--%>
								<!-- TISPRO-625 starts -->
									<%-- <c:choose>
										<c:when test="${isExpressCheckoutSelected}">
												<button class="button" id="deliveryMethodSubmit"
												type="submit" class="checkout-next">
												<spring:theme
													code="checkout.multi.deliveryMethod.expresscheckout.continue"
													text="Next" />
											</button>
										</c:when>
										<c:otherwise>
													<button class="button" id="deliveryMethodSubmit"
												type="submit" class="checkout-next">
												<spring:theme code="checkout.multi.deliveryMethod.continue"
													text="Next" />
											</button>
										</c:otherwise>
									</c:choose> --%>
							 <!-- TISPRO-625 ends -->
						
							
							<%-- </form:form> --%>
							<%-- <p><spring:theme code="checkout.multi.deliveryMethod.message" text="Items will ship as soon as they are available. <br> See Order Summary for more information." /></p> --%>
						</div>
						
						
						
					</div>
					
					</form:form>
					
				</ycommerce:testId>
			</jsp:body>
			</multi-checkout:checkoutSteps>
		</c:if>



		<c:if test="${showAddress eq true}">
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
				progressBarId="${progressBarId}">
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
					<div class="checkout-shipping checkTab"> 
					<c:if test="${not empty deliveryAddresses}"> 
								
										<form id="selectAddressForm"
									action="${request.contextPath}/checkout/multi/delivery-method/select-address"
									method="get">
											<c:set var='countWork' value='1' />
											<c:set var='countHome' value='1' />
											<!-- TISCR-305 starts -->
											<%-- <button id="deliveryAddressSubmitUp" type="submit" class="button checkout-next" ><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></button> --%>
											<!-- TISCR-305 ends -->
						<%-- <div class="checkout-indent left-block address-form">
						
								<h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<p class="items-no"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></p>
									<ul class="product-block addresses saved">		
									<li class="header">		
  										<ul>		
             								 <li><spring:theme code="checkout.multi.deliveryAddress.savedAddress"	text="Saved Addresses" /></li>		
              								<li class="pincode-button"><a href="#${request.contextPath}/checkout/multi/delivery-method/new-address"> 
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
																<label class="commercial" for="radio_${deliveryAddress.id}">
																	<spring:theme code="checkout.multi.deliveryAddress.commercialAddress" text="Commercial Addresses"/> <c:out value="${countWork}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countWork'  value='${countWork+1}' />
																</c:when>
																<c:otherwise>
																<label class="residential" for="radio_${deliveryAddress.id}">
																	<spring:theme code="checkout.multi.deliveryAddress.residentialAddress" text="Residential Addresses"/> <c:out value="${countHome}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countHome'  value='${countHome+1}' />
																</c:otherwise>
															</c:choose>
															
															<c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}"/>
															<c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}"/>
															<c:if test="${empty myline2  && empty myline3}">
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp;   </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
															<c:if test="${not empty myline2  && empty myline3}">
															
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp;  ${fn:escapeXml(deliveryAddress.line2)},    &nbsp; </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
															<c:if test="${ empty myline2  && not empty myline3}">
															
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp; ${fn:escapeXml(deliveryAddress.line3)},  &nbsp; </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
	                                                         <c:if test="${not empty myline2  && not empty myline3}">
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
															</c:if>
															
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
															
															<a href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}" class="edit_address"><spring:theme code="checkout.multi.deliveryAddress.editAddress" text="Edit Address"></spring:theme> </a>
															<a href="#nogo" class="defaultAddress" data-address-id="${deliveryAddress.id}">Make this default address</a>
														</ul>
												
													</div>
								</li>
												</c:forEach>
													
											</div>
											<button id="deliveryAddressSubmit" type="submit" class="button checkout-next" ><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></button>


										</ul>
						</div> --%>
						
						
						
						<!-- change here for modified checkout page starts -->
									
									<div class="address-accordion smk_accordion acc_with_icon">
  <div class="choose-address accordion_in acc_active">
    
      <div class="acc_head">
												<div class="acc_icon_expand"></div>
												<h2>Choose Address</h2>
												<p class="cart-items">
													<spring:theme code="checkout.multi.shipment.items"
														arguments="${cartData.deliveryItemsQuantity}"
														text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
												</p>
											</div>
      
	  	<div class="acc_content" style="display:block">
		
      <form>
        <!-- <div class="address-list ">
          <input type="radio" class="radio1" name="address" id="list1" checked="">
          <label for="list1"></label>
          <p class="address"> <span class="name">Lucy Gellert</span> <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> 
		   <span class="default">
		  <input type="radio" value="Make this default address" class="regular-radio" name="default" id="radio-default1" checked="">				      
		  <label for="radio-default1">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit"></span>
          </div> -->
          
          <c:forEach items="${deliveryAddresses}" var="deliveryAddress"
														varStatus="status">
        <div class="address-list">
        	<c:choose>
			<c:when test="${deliveryAddress.defaultAddress}">
          <input type="radio" class="radio1" name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" checked="checked" />
          <label for="radio_${deliveryAddress.id}"></label>
           </c:when>
           <c:otherwise>
           	<input type="radio" class="radio1"
																		name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" />
          	<label for="radio_${deliveryAddress.id}"></label>
           </c:otherwise>
           
           </c:choose>
           
          
          <p class="address"> 
          <c:choose>
          <c:when test="${deliveryAddress.addressType eq 'Work'}">
          <span class="name commercial"
																			for="radio_${deliveryAddress.id}">
          <spring:theme
																				code="checkout.multi.deliveryAddress.commercialAddress"
																				text="Commercial Addresses" />
          <c:if test="${deliveryAddress.defaultAddress}">
          - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
          </c:if>
          </span> 
          
          <c:set var='countWork' value='${countWork+1}' />
          </c:when>
          <c:otherwise>
           <span class="name residential"
																			for="radio_${deliveryAddress.id}" ${countHome}>
           <spring:theme
																				code="checkout.multi.deliveryAddress.residentialAddress"
																				text="Residential Addresses" />
           <c:out value="${countHome}"></c:out>
           <c:if test="${deliveryAddress.defaultAddress}">
           - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
           </c:if>
           </span>
           <c:set var='countHome'  value='${countHome+1}' />
          </c:otherwise>
          
          </c:choose>
          
          <c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}" />
		  <c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}" />
		  
		  <c:if test="${empty myline2  && empty myline3}">
		  <span style="padding-bottom: 0px;">
		  
		  ${fn:escapeXml(deliveryAddress.title)}</span>	
		  <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br></span>
		  <%-- <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		  
		  </c:if>
		  
		  <c:if test="${not empty myline2  && empty myline3}">
		   <span style="padding-bottom: 0px;">
		   ${fn:escapeXml(deliveryAddress.title)}</span>
		  <span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		   <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span> <span>${fn:escapeXml(deliveryAddress.state)},&nbsp;
		${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
		  <%--  <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		   <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		 
		  </c:if>
		  <c:if test="${ empty myline2  && not empty myline3}">
		  
		  <span style="padding-bottom: 0px;">${fn:escapeXml(deliveryAddress.title)}</span>
		 <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		 <span> ${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp; 
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
		 <%--  <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		  </c:if>
		  
		  <c:if test="${not empty myline2  && not empty myline3}">
		  <span style="padding-bottom: 0px;">
		  ${fn:escapeXml(deliveryAddress.title)}</span>
																	<span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;
		${fn:escapeXml(deliveryAddress.town)},&nbsp;</span><span>${fn:escapeXml(deliveryAddress.state)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		 <span> <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br></span>
		  <%-- <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  </c:if>
		  
		  
		
         <!--  <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> -->
        
 	<c:choose>
 	<c:when test="${deliveryAddress.defaultAddress}">
 		<span class="default default-selected">
 	  <input type="radio" value="Make this default address"
 																class="regular-radio" name="default"
 																id="radio-default2_${deliveryAddress.id}"
 																data-address-id="${deliveryAddress.id}">				      
 	  <label class="radio-checked" for="radio-default2_${deliveryAddress.id}">Make this default address</label>
 	  </span>
 	</c:when>
 		</c:choose>

		  <span class="default">
		  <input type="radio" value="Make this default address"
																	class="regular-radio" name="default"
																	id="radio-default2_${deliveryAddress.id}"
																	data-address-id="${deliveryAddress.id}">				      
		  <label for="radio-default2_${deliveryAddress.id}">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit">
		  <a
																href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"
																class="edit_address" id="link_${deliveryAddress.id}"></a>
		  </span>
		  
		    <c:set var="adressid" value="${deliveryAddress.id}" />
		  
		  <div class="editnewAddresPage" id="${adressid}"></div>
           </div>
           </c:forEach>
		 <!--   <div class="address-list">
          <input type="radio" class="radio1" name="address" id="list3">
          <label for="list3"></label>
          <p class="address"> <span class="name">Lucy Gellert</span> <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> 
		  <span class="default">
		  <input type="radio" value="Make this default address" class="regular-radio" name="default" id="radio-default3">				      
		  <label for="radio-default3">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit"></span>
           </div> -->
      </form>
	  <div class="formaddress" style="display: none;">
		<div class="heading-form">
														<h3>Add New Address</h3>
														<input type="button" value="cancel" class="cancelBtn">
													</div>
	  <%-- <form>		
		<input type="text" placeholder="Firstname*" class="name-address">
		<input type="text" placeholder="Lastname*" class="name-address">
		<input type="text" placeholder="phone number*">
		<input type="text" placeholder="Post code*">
		<input type="text" placeholder="address line1*">
		<input type="text" placeholder="address line2">
		<input type="text" placeholder="city*">
		<input type="text" placeholder="state*">
		<input type="text" placeholder="country*">  
		<input type="submit" value="save" class="saveBtn"> 
	  </form>	   --%>
	  
	   <div class="checkout-indent left-block address-form">
								<%-- <h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></div> --%>
									<ul class="product-block addresses new-form account-section">
									  	<%-- <li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme	code="checkout.multi.deliveryAddress.newAddress" text="New Address"> </spring:theme> </li>																  			 			 							        
									        <li class="pincode-button"><a href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									        
									         <c:if test="${addressFlag eq 'T'}"> 
											<spring:theme code="checkout.multi.deliveryAddress.useSavedAddress" text="Use a Saved Address"></spring:theme>
											 </c:if>
										</a></li>
										
									      </ul>
									  	</li> --%>
									  	<li
																class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
																	supportedCountries="${countries}" regions="${regions}"
																	cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									  	<!-- <input type="submit" value="save" class="saveBtn"> -->
									</div>
	  
	  </div>
     <div class="add-address" style="display: block;">
        <p id="address-form">
														<span class="addsign pincode-button">
        
        </span>
        <a class="pincode-button"> 
		<spring:theme code="checkout.multi.deliveryAddress.useNewAddress"
																text="Use New Address"></spring:theme>
		</a>
        </p>
		
      </div>
	  
	  </div> 
   
  </div>
 <%--  <div class="choose-address choose-retailer accordion_in">
			
			<!-- <div class="acc_head"><div class="acc_icon_expand"></div><h2>Choose Retailer</h2><p class="cart-items">To collect three items in your cart</p></div> -->			
			<div class="acc_content">
			
			<form>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list4" checked="">
				<label for="list4"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
			</div>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list5">
				<label for="list5"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
				
			</div>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list6">
				<label for="list6"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
				
			</div>
			</form>
			<div class="map-view">
				<p>Map view</p>				
			</div>
			</div>
		
		</div> --%>
</div>
				
				
									
									<!-- change here for modified checkout page ends -->
						
						</form>
						
									</c:if>
									
						<!--  If no address is present -->
						<c:if test="${empty deliveryAddresses}"> 
								
										<form id="selectAddressForm"
									action="${request.contextPath}/checkout/multi/delivery-method/select-address"
									method="get">
											<c:set var='countWork' value='1' />
											<c:set var='countHome' value='1' />
											<!-- TISCR-305 starts -->
											<%-- <button id="deliveryAddressSubmitUp" type="submit" class="button checkout-next" ><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></button> --%>
											<!-- TISCR-305 ends -->
						<%-- <div class="checkout-indent left-block address-form">
						
								<h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<p class="items-no"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></p>
									<ul class="product-block addresses saved">		
									<li class="header">		
  										<ul>		
             								 <li><spring:theme code="checkout.multi.deliveryAddress.savedAddress"	text="Saved Addresses" /></li>		
              								<li class="pincode-button"><a href="#${request.contextPath}/checkout/multi/delivery-method/new-address"> 
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
																<label class="commercial" for="radio_${deliveryAddress.id}">
																	<spring:theme code="checkout.multi.deliveryAddress.commercialAddress" text="Commercial Addresses"/> <c:out value="${countWork}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countWork'  value='${countWork+1}' />
																</c:when>
																<c:otherwise>
																<label class="residential" for="radio_${deliveryAddress.id}">
																	<spring:theme code="checkout.multi.deliveryAddress.residentialAddress" text="Residential Addresses"/> <c:out value="${countHome}"></c:out>
																	<c:if test="${deliveryAddress.defaultAddress}">
																	 - <spring:theme code="checkout.multi.deliveryAddress.defaultAddress" text="Default Addresses"/> <br/>
															</c:if>
																</label>
																	<c:set var='countHome'  value='${countHome+1}' />
																</c:otherwise>
															</c:choose>
															
															<c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}"/>
															<c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}"/>
															<c:if test="${empty myline2  && empty myline3}">
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp;   </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
															<c:if test="${not empty myline2  && empty myline3}">
															
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp;  ${fn:escapeXml(deliveryAddress.line2)},    &nbsp; </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
															<c:if test="${ empty myline2  && not empty myline3}">
															
															<address>
															<li>${fn:escapeXml(deliveryAddress.title)}	 ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</br>
															${fn:escapeXml(deliveryAddress.line1)}, &nbsp; ${fn:escapeXml(deliveryAddress.line3)},  &nbsp; </br> 
															${fn:escapeXml(deliveryAddress.town)}, &nbsp;${fn:escapeXml(deliveryAddress.state)}, &nbsp; ${fn:escapeXml(deliveryAddress.postalCode)} <!--DSC_006 : Fix for Checkout Address State display issue -->
															${fn:escapeXml(deliveryAddress.country.isocode)}<c:if test="${not empty deliveryAddress.region.name}">&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}</c:if></br>
															<spring:theme code="checkout.phone.no" text="+91"/>&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
															<c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
															<c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if>  															
															</li>
															</address>
															</c:if>
															
	                                                         <c:if test="${not empty myline2  && not empty myline3}">
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
															</c:if>
															
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
															
															<a href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}" class="edit_address"><spring:theme code="checkout.multi.deliveryAddress.editAddress" text="Edit Address"></spring:theme> </a>
															<a href="#nogo" class="defaultAddress" data-address-id="${deliveryAddress.id}">Make this default address</a>
														</ul>
												
													</div>
								</li>
												</c:forEach>
													
											</div>
											<button id="deliveryAddressSubmit" type="submit" class="button checkout-next" ><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></button>


										</ul>
						</div> --%>
						
						
						
						<!-- change here for modified checkout page starts -->
									
									<div class="address-accordion smk_accordion acc_with_icon">
  <div class="choose-address accordion_in acc_active">
    
      <div class="acc_head">
												<div class="acc_icon_expand"></div>
												<h2>Add New Address</h2>
												<p class="cart-items">
													<spring:theme code="checkout.multi.shipment.items"
														arguments="${cartData.deliveryItemsQuantity}"
														text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
												</p>
											</div>
      
	  	<div class="acc_content" style="display: block;">
		
      <form>
        <!-- <div class="address-list ">
          <input type="radio" class="radio1" name="address" id="list1" checked="">
          <label for="list1"></label>
          <p class="address"> <span class="name">Lucy Gellert</span> <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> 
		   <span class="default">
		  <input type="radio" value="Make this default address" class="regular-radio" name="default" id="radio-default1" checked="">				      
		  <label for="radio-default1">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit"></span>
          </div> -->
          
          <c:forEach items="${deliveryAddresses}" var="deliveryAddress"
														varStatus="status">
        <div class="address-list">
        	<c:choose>
			<c:when test="${deliveryAddress.defaultAddress}">
          <input type="radio" class="radio1" name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" checked="checked" />
          <label for="radio_${deliveryAddress.id}"></label>
           </c:when>
           <c:otherwise>
           	<input type="radio" class="radio1"
																		name="selectedAddressCode"
																		value="${deliveryAddress.id}"
																		id="radio_${deliveryAddress.id}" />
          	<label for="radio_${deliveryAddress.id}"></label>
           </c:otherwise>
           
           </c:choose>
           
          
          <p class="address"> 
          <c:choose>
          <c:when test="${deliveryAddress.addressType eq 'Work'}">
          <span class="name commercial"
																			for="radio_${deliveryAddress.id}">
          <spring:theme
																				code="checkout.multi.deliveryAddress.commercialAddress"
																				text="Commercial Addresses" />
          <c:if test="${deliveryAddress.defaultAddress}">
          - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
          </c:if>
          </span> 
          
          <c:set var='countWork' value='${countWork+1}' />
          </c:when>
          <c:otherwise>
           <span class="name residential"
																			for="radio_${deliveryAddress.id}">
           <spring:theme
																				code="checkout.multi.deliveryAddress.residentialAddress"
																				text="Residential Addresses" />
           <c:out value="${countHome}"></c:out>
           <c:if test="${deliveryAddress.defaultAddress}">
           - <spring:theme
																					code="checkout.multi.deliveryAddress.defaultAddress"
																					text="Default Addresses" /> <br />
           </c:if>
           </span>
           <c:set var='countHome'  value='${countHome+1}' />
          </c:otherwise>
          
          </c:choose>
          
          <c:set var="myline2" value="${fn:trim(deliveryAddress.line2)}" />
		  <c:set var="myline3" value="${fn:trim(deliveryAddress.line3)}" />
		  
		  <c:if test="${empty myline2  && empty myline3}">
		  <span style="padding-bottom: 0px;">
		  
		  ${fn:escapeXml(deliveryAddress.title)}</span>	
		  <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)} <br>
		  <%-- <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		  
		  </c:if>
		  
		  <c:if test="${not empty myline2  && empty myline3}">
		   <span style="padding-bottom: 0px;">
		   ${fn:escapeXml(deliveryAddress.title)}</span>
		  <span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		   <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;
		${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span>${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		   <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		  <%--  <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		   <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		 
		  </c:if>
		  <c:if test="${ empty myline2  && not empty myline3}">
		  
		  <span style="padding-bottom: 0px;">${fn:escapeXml(deliveryAddress.title)}</span>
		 <span class="name">${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		 <span> ${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp; 
		 ${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		 <%--  <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  
		  </c:if>
		  
		  <c:if test="${not empty myline2  && not empty myline3}">
		  <span style="padding-bottom: 0px;">
		  ${fn:escapeXml(deliveryAddress.title)}</span>
																	<span class="name"> ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</span>
		  <span>${fn:escapeXml(deliveryAddress.line1)},&nbsp;${fn:escapeXml(deliveryAddress.line2)},&nbsp;${fn:escapeXml(deliveryAddress.line3)},&nbsp;
		${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.state)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}<!--DSC_006 : Fix for Checkout Address State display issue -->
		 ${fn:escapeXml(deliveryAddress.country.isocode)}</span>
																	<c:if test="${not empty deliveryAddress.region.name}">&nbsp;<span> ${fn:escapeXml(deliveryAddress.region.name)}</span>
																	</c:if>
		  <spring:theme code="checkout.phone.no" text="+91" />&nbsp;${fn:escapeXml(deliveryAddress.phone)}<br>
		  <%-- <c:if test="${deliveryAddress.addressType eq 'Home'}"> <spring:theme code="checkout.addresstype.residential"/> </c:if>  
		  <c:if test="${deliveryAddress.addressType eq 'Work'}">  <spring:theme code="checkout.addresstype.commercial"/> </c:if> --%>
		  </c:if>
		  
		  
		
         <!--  <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> -->
		  <span class="default">
		  <input type="radio" value="Make this default address"
																	class="regular-radio" name="default"
																	id="radio-default2_${deliveryAddress.id}"
																	data-address-id="${deliveryAddress.id}">				      
		  <label for="radio-default2_${deliveryAddress.id}">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit">
		  <a
																href="${request.contextPath}/checkout/multi/delivery-method/edit-address/${deliveryAddress.id}"
																class="edit_address" id="link_${deliveryAddress.id}"></a>
		  </span>
		  
		    <c:set var="adressid" value="${deliveryAddress.id}" />
		  
		  <div class="editnewAddresPage" id="${adressid}"></div>
           </div>
           </c:forEach>
		 <!--   <div class="address-list">
          <input type="radio" class="radio1" name="address" id="list3">
          <label for="list3"></label>
          <p class="address"> <span class="name">Lucy Gellert</span> <span>37 Noalimba Avenue, LONG PLAIN, NSW 2360</span> <span>Phone: 02 9736 2453</span> 
		  <span class="default">
		  <input type="radio" value="Make this default address" class="regular-radio" name="default" id="radio-default3">				      
		  <label for="radio-default3">Make this default address</label>
		  </span>
		  </p>
		  <span class="edit"></span>
           </div> -->
      </form>
	  <div class="formaddress" style="display: none;">
		<div class="heading-form">
														<h3>Add New Address</h3>
														<input type="button" value="cancel" class="cancelBtn">
													</div>
	  <%-- <form>		
		<input type="text" placeholder="Firstname*" class="name-address">
		<input type="text" placeholder="Lastname*" class="name-address">
		<input type="text" placeholder="phone number*">
		<input type="text" placeholder="Post code*">
		<input type="text" placeholder="address line1*">
		<input type="text" placeholder="address line2">
		<input type="text" placeholder="city*">
		<input type="text" placeholder="state*">
		<input type="text" placeholder="country*">  
		<input type="submit" value="save" class="saveBtn"> 
	  </form>	   --%>
	  
	   <div class="checkout-indent left-block address-form">
								<%-- <h1>
									<spring:theme code="checkout.summary.shippingAddress" text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header"><spring:theme code="checkout.multi.shipment.items" arguments="${cartData.deliveryItemsQuantity}" text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme></div> --%>
									<ul class="product-block addresses new-form account-section">
									  	<%-- <li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme	code="checkout.multi.deliveryAddress.newAddress" text="New Address"> </spring:theme> </li>																  			 			 							        
									        <li class="pincode-button"><a href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									        
									         <c:if test="${addressFlag eq 'T'}"> 
											<spring:theme code="checkout.multi.deliveryAddress.useSavedAddress" text="Use a Saved Address"></spring:theme>
											 </c:if>
										</a></li>
										
									      </ul>
									  	</li> --%>
									  	<li
																class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
																	supportedCountries="${countries}" regions="${regions}"
																	cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									  	<!-- <input type="submit" value="save" class="saveBtn"> -->
									</div>
	  
	  </div>
     <div class="add-address" style="display: block;">
        <p id="address-form">
														<span class="addsign pincode-button">
        
        </span>
        <a class="pincode-button"> 
		<spring:theme code="checkout.multi.deliveryAddress.useNewAddress"
																text="Use New Address"></spring:theme>
		</a>
        </p>
		
      </div>
	  
	  </div> 
   
  </div>
 <%--  <div class="choose-address choose-retailer accordion_in">
			
			<!-- <div class="acc_head"><div class="acc_icon_expand"></div><h2>Choose Retailer</h2><p class="cart-items">To collect three items in your cart</p></div> -->			
			<div class="acc_content">
			
			<form>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list4" checked="">
				<label for="list4"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
			</div>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list5">
				<label for="list5"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
				
			</div>
			<div class="address-list">	
				<input type="radio" class="radio1" name="address" id="list6">
				<label for="list6"></label>				
				<p class="address">
					<span class="name">Croma Phoenix Market City</span>
					<span>Phoenix Market City, Chennai 142</span>
					<span>Velachery 600042</span>
					<span class="work-hours">PiQ up hrs : 11:00 - 20:30</span>
					<span class="week-off">Weekly Off : All Days Open</span>
				</p>
				<p class="dist">
					<span class="dist-calc">2.5 miles</span>
					<span class="get-directions">get directions</span>
				</p>
				
			</div>
			</form>
			<div class="map-view">
				<p>Map view</p>				
			</div>
			</div>
		
		</div> --%>
</div>
				
				
									
									<!-- change here for modified checkout page ends -->
						
						</form>
						
									</c:if>								
									
									
									
									
									
					</div>
					
	</div>
	</ycommerce:testId>
	</jsp:body>
	</multi-checkout:checkoutSteps>
	</c:if>



	<c:if test="${showEditAddress eq true}">
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
			progressBarId="${progressBarId}">
			<jsp:body>
					<script>
						//TISST-13010
						$(document).ready(function() {
							showPromotionTag();
						});
						var timeoutID;
						function setup() {
							this.addEventListener("mousemove", resetTimer,
									false);
							this.addEventListener("mousedown", resetTimer,
									false);
							this
									.addEventListener("keypress", resetTimer,
											false);
							this.addEventListener("DOMMouseScroll", resetTimer,
									false);
							this.addEventListener("mousewheel", resetTimer,
									false);
							this.addEventListener("touchmove", resetTimer,
									false);
							this.addEventListener("MSPointerMove", resetTimer,
									false);
							startTimer();
						}
						setup();

						function startTimer() {
							// wait 2 seconds before calling goInactive
							timeoutID = window.setTimeout(goInactive,
									'${timeout}');
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
						<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button id="editAddressButtonUp"
										class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress"
											text="Save address" />
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccountUp"
												class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose> 
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress"
									text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header">
								<spring:theme code="checkout.multi.shipment.items"
									arguments="${cartData.deliveryItemsQuantity}"
									text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
							</div>
								<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme
												code="text.account.addressBook.addressDetails" /></li>
									        <li class="pincode-button"><a
											href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									       
									       
									        <c:if test="${addressFlag eq 'T'}"> 
										<spring:theme
														code="checkout.multi.deliveryAddress.useSavedAddress"
														text="Use a Saved Address"></spring:theme>	
										</c:if>																
									</a></li>
									      </ul>
									  	</li>
									  	<li
									class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
										supportedCountries="${countries}" regions="${regions}"
										cancelUrl="${currentStepUrl}" country="${country}" />
									  	</li>
									  	</ul>
							</div>
						</div>
					</ycommerce:testId>
				</jsp:body>
		</multi-checkout:checkoutSteps>
	</c:if>


	<c:if test="${addAddress eq true}">

		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}"
			progressBarId="${progressBarId}">
			<jsp:body>
					<script>
						//TISST-13010
						$(document).ready(function() {
							showPromotionTag();
						});
						var timeoutID;
						function setup() {
							this.addEventListener("mousemove", resetTimer,
									false);
							this.addEventListener("mousedown", resetTimer,
									false);
							this
									.addEventListener("keypress", resetTimer,
											false);
							this.addEventListener("DOMMouseScroll", resetTimer,
									false);
							this.addEventListener("mousewheel", resetTimer,
									false);
							this.addEventListener("touchmove", resetTimer,
									false);
							this.addEventListener("MSPointerMove", resetTimer,
									false);
							startTimer();
						}
						setup();

						function startTimer() {
							// wait 2 seconds before calling goInactive
							timeoutID = window.setTimeout(goInactive,
									'${timeout}');
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
					<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button id="editAddressButtonUp"
										class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress"
											text="Save address" />
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccountUp"
												class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue"
													text="Continue" />
									</button>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose> 
							<div class="checkout-indent left-block address-form">
								<h1>
									<spring:theme code="checkout.summary.shippingAddress"
									text="Shipping Address"></spring:theme>
								</h1>
								<div class="checkout-shipping-items-header">
								<spring:theme code="checkout.multi.shipment.items"
									arguments="${cartData.deliveryItemsQuantity}"
									text="Shipment - ${cartData.deliveryItemsQuantity} Item(s)"></spring:theme>
							</div>
									<ul class="product-block addresses new-form account-section">
									  	<li class="header">
									  		<ul class="account-section-header">
									        <li><spring:theme
												code="checkout.multi.deliveryAddress.newAddress"
												text="New Address"> </spring:theme> </li>																  			 			 							        
									        <li class="pincode-button"><a
											href="${request.contextPath}/checkout/multi/delivery-method/selectaddress">
									        
									         <c:if test="${addressFlag eq 'T'}"> 
											<spring:theme
														code="checkout.multi.deliveryAddress.useSavedAddress"
														text="Use a Saved Address"></spring:theme>
											 </c:if>
										</a></li>
										
									      </ul>
									  	</li>
									  	<li
									class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
										supportedCountries="${countries}" regions="${regions}"
										cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									</div>
							</div>
						
					</ycommerce:testId>
				</jsp:body>
		</multi-checkout:checkoutSteps>

	</c:if>
	<div class="addnewAddresPage"></div>


	<div class="right-block shipping">
		<div class="checkout-order-summary">
			<multi-checkout:orderTotals cartData="${cartData}"
				showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
				<c:if test="${showDeliveryMethod eq true}">
		<c:choose>
			<c:when test="${isExpressCheckoutSelected}">
					<button class="button" id="deliveryMethodSubmit"
					type="submit" class="checkout-next">
					<spring:theme
						code="checkout.multi.deliveryMethod.expresscheckout.continue"
						text="Next" />
				</button>
			</c:when>
			<c:otherwise>
						<button class="button" id="deliveryMethodSubmit"
					type="submit" class="checkout-next">
					<spring:theme code="checkout.multi.deliveryMethod.continue"
						text="Next" />
				</button>
			</c:otherwise>
		</c:choose>
		</c:if>
		</div>
		
	</div>
	
									
<%-- <div class="outstanding-amt" style="display:none;">	
<ul class="totals outstanding-totalss">
          <li id="totals" class="outstanding-amounts"><spring:theme code="basket.page.totals.outstanding.amount"/><span class="amt"><ycommerce:testId code="cart_totalPrice_label">
                <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose>
            </ycommerce:testId></span></li>
          </ul>	 
          </div> --%>
	<%-- <div class="col-sm-12 col-lg-9">
			<cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div> --%>
	</div>

</template:page>